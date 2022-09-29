package com.rafaelhosaka.shareme.applicationuser;

import com.rafaelhosaka.shareme.email.EmailService;
import com.rafaelhosaka.shareme.email.EmailToken;
import com.rafaelhosaka.shareme.email.EmailTokenRepository;
import com.rafaelhosaka.shareme.exception.*;
import com.rafaelhosaka.shareme.user.UserProfile;
import com.rafaelhosaka.shareme.user.UserProfileRepository;
import com.rafaelhosaka.shareme.user.UserProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Slf4j
public class ApplicationUserService implements UserDetailsService {
    private final ApplicationUserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final EmailTokenRepository emailTokenRepository;
    private final EmailService emailService;
    private final UserProfileRepository userProfileRepository;
    private final UserProfileService userProfileService;

    @Autowired
    public ApplicationUserService(ApplicationUserRepository userRepository,
                                  RoleRepository roleRepository,
                                  PasswordEncoder encoder,
                                  EmailTokenRepository emailTokenRepository,
                                  EmailService emailService,
                                  UserProfileRepository userProfileRepository,
                                  UserProfileService userProfileService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.emailTokenRepository = emailTokenRepository;
        this.emailService = emailService;
        this.userProfileRepository = userProfileRepository;
        this.userProfileService = userProfileService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ApplicationUser applicationUser = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User with "+username+" not found"));

        log.info("User {} found",username);


        return new User(
                applicationUser.getUsername(),
                applicationUser.getPassword(),
                applicationUser.isEnabled(),
                applicationUser.isAccountNonLocked(),
                applicationUser.isAccountNonExpired(),
                applicationUser.isCredentialsNonExpired(),
                applicationUser.getAuthorities()
        );
    }

    public ApplicationUser createAccount(ApplicationUser applicationUser, UserProfile userProfile) throws ApplicationUserNotFoundException {
        log.info("save applicationUser {}",applicationUser.getUsername());
        if(applicationUser.getUsername() == null || applicationUser.getUsername().trim().isEmpty()){
            throw new IllegalStateException("errorUsernameEmpty");
        }

        if(applicationUser.getPassword() == null || applicationUser.getPassword().trim().isEmpty()) {
            throw new IllegalStateException("errorPasswordEmpty");
        }

        if(userRepository.findByUsername(applicationUser.getUsername()).isPresent()){
            throw new IllegalStateException("errorUsernameAlreadyRegistered");
        }
        applicationUser.setPassword(encoder.encode(applicationUser.getPassword()));

        try {
            applicationUser = userRepository.save(applicationUser);
            addRoleToUser(applicationUser.getUsername(), "ROLE_USER");
            userProfileService.save(userProfile);
        }catch (IllegalStateException exception){
            userRepository.delete(applicationUser);
            throw exception;
        }

        return applicationUser;
    }

    public Role saveRole(Role role){
        log.info("save role {}", role.getName());
        return roleRepository.save(role);
    }

    public void addRoleToUser(String username, String roleName) throws ApplicationUserNotFoundException {
        log.info("adding role {} to user {}",roleName, username);
        ApplicationUser user = userRepository.findByUsername(username).orElseThrow(
                () -> new ApplicationUserNotFoundException("User with "+username+" not found"));;

        Role role = roleRepository.findByName(roleName);
        user.addRole(role);
        userRepository.save(user);
    }

    public ApplicationUser getUser(String username) throws UsernameNotFoundException{
        log.info("fetching user {}",username);
        ApplicationUser user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User with "+username+" not found"));

        return user;
    }

    public List<ApplicationUser> getUsers(){
        log.info("fetching all users");
        return userRepository.findAll();
    }


    public String changePasswordByUsername(String username,String currentPassword, String newPassword) throws UsernameNotFoundException, WrongPasswordException, MessagingException, UserProfileNotFoundException {
        ApplicationUser user = userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User with "+username+" not found")
        );
        if(!encoder.matches(currentPassword, user.getPassword())){
            throw new WrongPasswordException("Wrong password");
        }
        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);
        UserProfile userProfile = userProfileRepository.findUserProfileByEmail(username).orElseThrow(
                () -> new UserProfileNotFoundException("User Profile with email "+username+" not found")
        );
        emailService.sendPasswordChangedNotification(user.getUsername(), userProfile.getLanguagePreference().getShortName());
        return "Password updated";
    }

    public String changePasswordByToken(String token, String newPassword) throws EmailTokenNotFoundException, EmailTokenExpiredException, MessagingException, UserProfileNotFoundException {
        EmailToken emailToken=  emailTokenRepository.getEmailTokenByToken(token).orElseThrow(
                () -> new EmailTokenNotFoundException("Token does not exist"));
        if(emailToken.isExpired()) {
            throw new EmailTokenExpiredException("Token expired");
        }
        ApplicationUser user = emailToken.getUser();
        user.setPassword(encoder.encode(newPassword));
        userRepository.save(user);
        UserProfile userProfile = userProfileRepository.findUserProfileByEmail(user.getUsername()).orElseThrow(
                () -> new UserProfileNotFoundException("User Profile with email "+user.getUsername()+" not found")
        );
        emailService.sendPasswordChangedNotification(user.getUsername(), userProfile.getLanguagePreference().getShortName());
        return "Password updated";
    }
}
