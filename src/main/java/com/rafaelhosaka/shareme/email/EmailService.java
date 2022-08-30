package com.rafaelhosaka.shareme.email;

import com.rafaelhosaka.shareme.applicationuser.ApplicationUser;
import com.rafaelhosaka.shareme.applicationuser.ApplicationUserRepository;
import com.rafaelhosaka.shareme.exception.EmailTokenExpiredException;
import com.rafaelhosaka.shareme.exception.EmailTokenNotFoundException;
import com.rafaelhosaka.shareme.exception.UserProfileNotFoundException;
import com.rafaelhosaka.shareme.user.LanguagePreference;
import com.rafaelhosaka.shareme.user.UserProfile;
import com.rafaelhosaka.shareme.user.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
public class EmailService{

    private JavaMailSender emailSender;
    private EmailTokenRepository emailRepository;
    private ApplicationUserRepository userRepository;
    private UserProfileRepository userProfileRepository;

    private final String FROM = "shareme.authentication@gmail.com";
    private final String URL = "http://localhost:3000";

    @Autowired
    public EmailService(JavaMailSender emailSender, EmailTokenRepository emailRepository, ApplicationUserRepository userRepository, UserProfileRepository userProfileRepository){
        this.emailRepository = emailRepository;
        this.emailSender = emailSender;
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
    }

    public void sendVerificationEmail(
            String to, String token, String language) throws MessagingException {
            String confirmationUrl
                = URL + "/verify?token=" + token;

            List<String> emailBuilt = new EmailBuilder(language).verificationEmail(confirmationUrl);

            sendEmail(emailBuilt.get(1), to , emailBuilt.get(0));
    }

    public void sendPasswordRecoveryEmail(
            String to, String token, String language) throws MessagingException {
        String resetPasswordUrl
                = URL + "/resetPassword?token=" + token;
        List<String> emailBuilt =  new EmailBuilder(language).passwordRecoveryEmail(resetPasswordUrl, to);
        sendEmail(emailBuilt.get(1), to , emailBuilt.get(0));
    }

    public void sendPasswordChangedNotification(
            String to, String language) throws MessagingException {
        List<String> emailBuilt  = new EmailBuilder(language).passwordChangedEmail();
        sendEmail(emailBuilt.get(1), to , emailBuilt.get(0));
    }

    private void sendEmail(String body, String to, String subject) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED,"UTF-8");
        helper.setText(body,true);
        helper.addInline("logo", new ClassPathResource("image/logo-full.png"));
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setFrom(FROM);
        emailSender.send(message);
    }

    public EmailToken createEmailToken(ApplicationUser user) {
        String token = UUID.randomUUID().toString();
        EmailToken emailToken = new EmailToken();
        emailToken.setToken(token);
        emailToken.setUser(user);
        return emailRepository.save(emailToken);
    }

    public String confirmEmail(String token) throws EmailTokenNotFoundException, EmailTokenExpiredException {
        EmailToken emailToken=  emailRepository.getEmailTokenByToken(token).orElseThrow(
                () -> new EmailTokenNotFoundException("Invalid URL"));
        ApplicationUser user = emailToken.getUser();
        if(emailToken.isExpired()){
            throw  new EmailTokenExpiredException("EmailToken expired");
        }
        user.setEnabled(true);
        userRepository.save(user);
        return "Your email has been verified";
    }

    public String resendEmail(String email) throws MessagingException, UsernameNotFoundException, UserProfileNotFoundException {
        ApplicationUser user = userRepository.findByUsername(email).orElseThrow(
                () -> new UsernameNotFoundException("User with "+email+" not found"));;

        Optional<EmailToken> emailToken = emailRepository.getEmailTokenByUserId(user.getId());

        UserProfile userProfile = userProfileRepository.findUserProfileByEmail(email).orElseThrow(
                () -> new UserProfileNotFoundException("User Profile with "+email+" not found")
        );

        if(emailToken.isPresent()){
            if(!emailToken.get().isExpired()){
                sendVerificationEmail(email,emailToken.get().getToken(), userProfile.getLanguagePreference().getShortName());
                return "Resent email";
            }else{
                emailRepository.delete(emailToken.get());
            }
        }

        EmailToken newEmailToken = emailRepository.save(createEmailToken(user));
        sendVerificationEmail(email, newEmailToken.getToken(), userProfile.getLanguagePreference().getShortName());
        return "Resent email";
    }

    public String passwordRecoveryEmail(String email) throws MessagingException, UserProfileNotFoundException {
        ApplicationUser user = userRepository.findByUsername(email).orElseThrow(
                () -> new UsernameNotFoundException("User with "+email+" not found"));;

        Optional<EmailToken> emailToken = emailRepository.getEmailTokenByUserId(user.getId());
        UserProfile userProfile = userProfileRepository.findUserProfileByEmail(email).orElseThrow(
                () -> new UserProfileNotFoundException("User Profile with "+email+" not found")
        );
        if(emailToken.isPresent()){
            if(!emailToken.get().isExpired()){
                sendPasswordRecoveryEmail(email,emailToken.get().getToken(), userProfile.getLanguagePreference().getShortName());
                return "Email for resetting password sent";
            }else{
                emailRepository.delete(emailToken.get());
            }
        }

        EmailToken newEmailToken = emailRepository.save(createEmailToken(user));
        sendPasswordRecoveryEmail(email, newEmailToken.getToken(), userProfile.getLanguagePreference().getShortName());
        return "Email for resetting password sent";
    }
}
