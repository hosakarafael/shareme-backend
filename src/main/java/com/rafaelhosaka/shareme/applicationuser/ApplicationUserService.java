package com.rafaelhosaka.shareme.applicationuser;

import com.rafaelhosaka.shareme.email.EmailService;
import com.rafaelhosaka.shareme.email.EmailToken;
import com.rafaelhosaka.shareme.email.EmailTokenRepository;
import com.rafaelhosaka.shareme.exception.*;
import com.rafaelhosaka.shareme.user.UserProfile;
import com.rafaelhosaka.shareme.user.UserProfileRepository;
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

    @Autowired
    public ApplicationUserService(ApplicationUserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder, EmailTokenRepository emailTokenRepository, EmailService emailService, UserProfileRepository userProfileRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.emailTokenRepository = emailTokenRepository;
        this.emailService = emailService;
        this.userProfileRepository = userProfileRepository;
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

    public ApplicationUser saveUser(ApplicationUser user) throws ApplicationUserNotFoundException {
        log.info("save user {}",user.getUsername());
        if(user.getUsername().isEmpty()){
            throw new IllegalStateException("Username cannot be empty");
        }

        if(user.getPassword().isEmpty()) {
            throw new IllegalStateException("Password cannot be empty");
        }

        if(userRepository.findByUsername(user.getUsername()).isPresent()){
            throw new IllegalStateException("This username is already registered");
        }

        user.setPassword(encoder.encode(user.getPassword()));
        user = userRepository.save(user);
        addRoleToUser(user.getUsername(), "ROLE_USER");

        return user;
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
