package com.rafaelhosaka.shareme.applicationuser;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rafaelhosaka.shareme.email.EmailService;
import com.rafaelhosaka.shareme.email.OnRegistrationCompleteEvent;
import com.rafaelhosaka.shareme.exception.ApplicationUserNotFoundException;
import com.rafaelhosaka.shareme.exception.EmailTokenExpiredException;
import com.rafaelhosaka.shareme.exception.EmailTokenNotFoundException;
import com.rafaelhosaka.shareme.exception.WrongPasswordException;
import com.rafaelhosaka.shareme.jwt.JwtUtils;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("api/auth")
public class ApplicationUserController {
    private final ApplicationUserService userService;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public ApplicationUserController(ApplicationUserService userService, ApplicationEventPublisher applicationEventPublisher) {
        this.userService = userService;
        this.eventPublisher = applicationEventPublisher;
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<ApplicationUser> getUser(@PathVariable("username") String username){
        return ResponseEntity.ok().body(userService.getUser(username));
    }

    @GetMapping("/user/all")
    public ResponseEntity<List<ApplicationUser>> getUsers(){
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @PostMapping("/user/save")
    public ResponseEntity<ApplicationUser> saveUser(@RequestBody ApplicationUser applicationUser, HttpServletRequest request){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/auth/user/save").toUriString());
        try {
            ApplicationUser user = userService.saveUser(applicationUser);
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user,request.getLocale() , request.getContextPath()));
            return ResponseEntity.created(uri).body(user);
        }catch(IllegalStateException | ApplicationUserNotFoundException e){
            return new ResponseEntity(e.getMessage(), BAD_REQUEST);
        }
    }

    @PostMapping("/role/save")
    public ResponseEntity<Role> saveRole(@RequestBody Role role){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/auth/role/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveRole(role));
    }

    @PostMapping("/role/addtouser")
    public ResponseEntity<?> addRoleToUser(@RequestBody RoleToUserForm form){
        try {
            userService.addRoleToUser(form.getUsername(), form.getRoleName());
            return ResponseEntity.ok().build();
        }catch (ApplicationUserNotFoundException e){
            return new ResponseEntity(e.getMessage(), BAD_REQUEST);
        }
    }

    @GetMapping("/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            try {
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                JwtUtils jwtUtils = new JwtUtils();

                DecodedJWT decodedRefreshToken = jwtUtils.decodeToken(refreshToken);

                String username = decodedRefreshToken.getSubject();

                ApplicationUser user = userService.getUser(username);

                String accessToken =
                        jwtUtils.createAccessToken(
                                user.getUsername(),
                                request.getRequestURL().toString(),
                                user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));

                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), jwtUtils.createTokenMap(accessToken, refreshToken));
            }catch(Exception e){
                response.setHeader("error", e.getMessage());
                response.setStatus(FORBIDDEN.value());

                Map<String, String> error = new HashMap<>();
                error.put("error_message", e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        }else{
            throw new RuntimeException("Refresh token is missing");
        }
    }

    @PutMapping("/password/username")
    public ResponseEntity<String> changePassword(@RequestPart("username") String username,@RequestPart("currentPassword")String currentPassword, @RequestPart("newPassword") String newPassword){
        try {
            return ResponseEntity.ok().body(userService.changePasswordByUsername(username, currentPassword, newPassword));
        }catch (UsernameNotFoundException | WrongPasswordException e){
            return new ResponseEntity(e.getMessage(),BAD_REQUEST);
        }
    }

    @PutMapping("/password/token")
    public ResponseEntity<String> changePasswordByToken(@RequestPart("token") String token, @RequestPart("newPassword") String newPassword){
        try {
            return ResponseEntity.ok().body(userService.changePasswordByToken(token, newPassword));
        }catch (EmailTokenNotFoundException| EmailTokenExpiredException e){
            return new ResponseEntity(e.getMessage(),BAD_REQUEST);
        }
    }
}




@Data
class RoleToUserForm{
    private String username;
    private String roleName;
}
