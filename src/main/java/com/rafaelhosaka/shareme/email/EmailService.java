package com.rafaelhosaka.shareme.email;

import com.rafaelhosaka.shareme.applicationuser.ApplicationUser;
import com.rafaelhosaka.shareme.applicationuser.ApplicationUserRepository;
import com.rafaelhosaka.shareme.applicationuser.ApplicationUserService;
import com.rafaelhosaka.shareme.exception.EmailTokenExpiredException;
import com.rafaelhosaka.shareme.exception.EmailTokenNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;


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

    public void sendVerificationEmail(
            String to, String token) throws MessagingException {
            String from = "shareme.authentication@gmail.com";
            String subject = "ShareMe Account Verification";
            String confirmationUrl
                = "http://localhost:8080/api/registrationConfirm?token=" + token;

            String containerStyles = "max-width:600px;" +
                                    "margin:0 auto;"+
                                    "color:black;";

            String btnStyles = "display:inline-block;"+
                    "border-radius: 30px;" +
                    "padding: 1rem;" +
                    "font-weight: 700;" +
                    "border: 0;" +
                    "background: #02690b;"+
                    "color: white;"+
                    "text-decoration:none;"+
                    "padding: 20px;";


            String body =
                    "<div style=\"margin:0;padding:0;\">"+
                        "<div style=\""+containerStyles+"\">" +
                            "<div style=\"background:#f1f2f2;padding:20px;\" align=\"center\">"+
                                "<img style=\"width:200px;height:75px;\" src=\"cid:logo\">"+
                            "</div>"+
                            "<div style=\"margin-bottom:24px\" align=\"center\">"+
                                "<h1 style=\"margin:24px\">Welcome to ShareMe!</h1>"+
                                "<p>You're receiving this message because you signed up for an account on ShareMe. (If you didn't sign up, you can ignore this email.)</p>"+
                                "<div style=\"margin-top:24px\" align=\"center\">"+
                                    "<a href=\""+confirmationUrl+"\" style=\"" + btnStyles + "\">Click here to activate your account</a>" +
                                "</div>"+
                            "</div>"+
                            "<hr style=\"border-top-width:1px;border-top-color:#c5c5c5;margin:8px 0 48px;border-style:solid none none;\">"+
                            "<div align=\"center\">"+
                                "Rafael Hideki Hosaka © 2022 ShareMe"+
                            "</div>"+
                        "</div>"+
                        "<span style=\"opacity:0\">"+LocalDateTime.now()+"</span>"+
                    "</div>";



            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED,"UTF-8");
            helper.setText(body,true);
            helper.addInline("logo", new ClassPathResource("static/img/logo-full.png"));
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom(from);
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
        EmailToken emailToken=  emailRepository.getEmailTokenByToken(token).orElseThrow(() -> new EmailTokenNotFoundException("EmailToken "+token+" does not exist"));
        ApplicationUser user = emailToken.getUser();
        if(emailToken.isExpired()){
            throw  new EmailTokenExpiredException("EmailToken expired");
        }
        user.setEnabled(true);
        userRepository.save(user);
        return "Your email has been verified";
    }

    public String resendEmail(String email) throws MessagingException, UsernameNotFoundException {
        ApplicationUser user = userRepository.findByUsername(email).orElseThrow(
                () -> new UsernameNotFoundException("User with "+email+" not found"));;

        Optional<EmailToken> emailToken = emailRepository.getEmailTokenByUserId(user.getId());
        if(emailToken.isPresent()){
            if(!emailToken.get().isExpired()){
                sendVerificationEmail(email,emailToken.get().getToken());
                return "Resent email";
            }else{
                emailRepository.delete(emailToken.get());
            }
        }

        EmailToken newEmailToken = emailRepository.save(createEmailToken(user));
        sendVerificationEmail(email, newEmailToken.getToken());
        return "Resent email";
    }
}
