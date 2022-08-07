package com.rafaelhosaka.shareme.email;

import com.rafaelhosaka.shareme.applicationuser.ApplicationUser;
import com.rafaelhosaka.shareme.applicationuser.ApplicationUserRepository;
import com.rafaelhosaka.shareme.applicationuser.ApplicationUserService;
import com.rafaelhosaka.shareme.exception.EmailTokenExpiredException;
import com.rafaelhosaka.shareme.exception.EmailTokenNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailService{

    private JavaMailSender emailSender;
    private EmailTokenRepository emailRepository;
    private ApplicationUserRepository userRepository;

    @Autowired
    public EmailService(JavaMailSender emailSender, EmailTokenRepository emailRepository, ApplicationUserRepository userRepository){
        this.emailRepository = emailRepository;
        this.emailSender = emailSender;
        this.userRepository = userRepository;
    }

    public void sendEmail(
            String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("shareme.authentication@gmail.com");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }

    public void createEmailToken(ApplicationUser user, String token) {
        EmailToken emailToken = new EmailToken();
        emailToken.setToken(token);
        emailToken.setUser(user);
        emailRepository.save(emailToken);
    }

    public String confirmEmail(String token) throws EmailTokenNotFoundException, EmailTokenExpiredException {
        EmailToken emailToken=  emailRepository.getEmailTokenByToken(token).orElseThrow(() -> new EmailTokenNotFoundException("EmailToken "+token+" does not exist"));
        ApplicationUser user = emailToken.getUser();
        if(emailToken.isExpired()){
            throw  new EmailTokenExpiredException("EmailToken expired");
        }
        user.setEnabled(true);
        userRepository.save(user);
        return "Your email has been verified";
    }
}
