package com.rafaelhosaka.shareme.security;

import com.rafaelhosaka.shareme.filter.CustomAuthenticationFilter;
import com.rafaelhosaka.shareme.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import java.util.List;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private final UserDetailsService userDetailsService;
    private final BCryptPasswordEncoder encoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean());
        customAuthenticationFilter.setFilterProcessesUrl("/api/auth/login");

        http.csrf().disable();
        http.cors().configurationSource(request -> {
                var cors = new CorsConfiguration();
                cors.setAllowedOrigins(List.of("http://localhost:3000"));
                cors.setAllowedMethods(List.of("GET","POST", "PUT", "DELETE", "OPTIONS"));
                cors.setAllowedHeaders(List.of("*"));
                return cors;
        });

        http.sessionManagement().sessionCreationPolicy(STATELESS);
        authorizeRequests(http);

        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }



    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }


    private void authorizeRequests(HttpSecurity http) throws Exception {
        //all
        http.authorizeRequests().antMatchers(getPermitAllPaths()).permitAll();

        //like
        http.authorizeRequests().antMatchers(PUT, "/api/like/post").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN");

        //comment
        http.authorizeRequests().antMatchers(PUT, "/api/comment/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN");

        //post
        http.authorizeRequests().antMatchers(GET, "/api/post/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN");
        http.authorizeRequests().antMatchers(POST, "/api/post/upload").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN");

        //user
        http.authorizeRequests().antMatchers(GET, "/api/user/**").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN");
        http.authorizeRequests().antMatchers(PUT, "/api/user/upload").hasAnyAuthority("ROLE_USER", "ROLE_ADMIN");

        //auth
        http.authorizeRequests().antMatchers(GET, "/api/auth/**").hasAnyAuthority("ROLE_ADMIN");

        http.authorizeRequests().anyRequest().authenticated();

    }

    public String[] getPermitAllPaths(){
        return new String[]{
                "/error",
                "/api/auth/login/**",
                "/api/auth/user/save/**",
                "/api/auth/refresh/**",
                "/api/post/download/**",
                "/api/user/save/**"
                };
    }
}
