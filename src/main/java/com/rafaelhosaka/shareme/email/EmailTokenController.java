package com.rafaelhosaka.shareme.email;

import com.rafaelhosaka.shareme.exception.EmailTokenExpiredException;
import com.rafaelhosaka.shareme.exception.EmailTokenNotFoundException;
import com.rafaelhosaka.shareme.exception.UserProfileNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

@RestController
@RequestMapping("api")
@Slf4j
public class EmailTokenController {
    private  final EmailService emailService;

    @Autowired
    public EmailTokenController(EmailService emailService){
        this.emailService = emailService;
    }

    @GetMapping("/registrationConfirm")
    public ResponseEntity<String> confirmRegistration(@RequestParam("token") String token){
        try {
            return ResponseEntity.ok().body(emailService.confirmEmail(token));
        } catch (EmailTokenNotFoundException | EmailTokenExpiredException e) {
            log.error(e.getMessage());
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/resend/{email}")
    public ResponseEntity<String> reSendEmail(@PathVariable("email") String email){
        try {
            return ResponseEntity.ok().body(emailService.resendEmail(email));
        } catch (MessagingException | UsernameNotFoundException | UserProfileNotFoundException e) {
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/recovery/{email}")
    public ResponseEntity<String> passwordRecoveryEmail(@PathVariable("email") String email){
        try {
            return ResponseEntity.ok().body(emailService.passwordRecoveryEmail(email));
        } catch (MessagingException | UsernameNotFoundException | UserProfileNotFoundException e) {
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
