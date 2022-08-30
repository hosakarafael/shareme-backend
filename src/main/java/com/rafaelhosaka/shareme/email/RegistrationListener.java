package com.rafaelhosaka.shareme.email;

import com.rafaelhosaka.shareme.applicationuser.ApplicationUser;
import com.rafaelhosaka.shareme.applicationuser.ApplicationUserService;
import com.rafaelhosaka.shareme.exception.UserProfileNotFoundException;
import com.rafaelhosaka.shareme.user.UserProfile;
import com.rafaelhosaka.shareme.user.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
    private final EmailService emailService;
    private final ApplicationUserService userService;

    @Autowired
    public RegistrationListener(EmailService emailService, ApplicationUserService userService) {
        this.emailService = emailService;
        this.userService = userService;
    }

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) {
        try {
            UserProfile userProfile = event.getUser();
            String recipientAddress = userProfile.getEmail();
            ApplicationUser appUser = userService.getUser(userProfile.getEmail());
            EmailToken emailToken = emailService.createEmailToken(appUser);
            emailService.sendVerificationEmail(recipientAddress,emailToken.getToken(), event.getLanguage().getShortName());

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
