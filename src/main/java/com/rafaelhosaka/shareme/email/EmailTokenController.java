package com.rafaelhosaka.shareme.email;

import com.rafaelhosaka.shareme.exception.EmailTokenExpiredException;
import com.rafaelhosaka.shareme.exception.EmailTokenNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("registrationConfirm")
@Slf4j
public class EmailTokenController {
    private  final EmailService emailService;

    @Autowired
    public EmailTokenController(EmailService emailService){
        this.emailService = emailService;
    }

    @GetMapping
    public ResponseEntity<String> confirmRegistration(@RequestParam("token") String token){
        try {
            return ResponseEntity.ok().body(emailService.confirmEmail(token));
        } catch (EmailTokenNotFoundException | EmailTokenExpiredException e) {
            log.error(e.getMessage());
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
