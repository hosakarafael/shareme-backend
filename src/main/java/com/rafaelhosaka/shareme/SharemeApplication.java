package com.rafaelhosaka.shareme;

import com.rafaelhosaka.shareme.applicationuser.ApplicationUserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SharemeApplication {

	public static void main(String[] args) {
		SpringApplication.run(SharemeApplication.class, args);
	}

	@Bean
	CommandLineRunner run(ApplicationUserService userService){
		return  args -> {
//			userService.saveRole(new Role(null, "ROLE_USER"));
//			userService.saveRole(new Role(null, "ROLE_ADMIN"));
//
//			userService.saveUser(new ApplicationUser(null, "rafaelhosaka@shareme.com", "1234", new ArrayList<>()));
//			userService.saveUser(new ApplicationUser(null, "admin@shareme.com", "admin", new ArrayList<>()));
//
//			userService.addRoleToUser("rafaelhosaka@shareme.com", "ROLE_USER");
//
//			userService.addRoleToUser("admin@shareme.com","ROLE_ADMIN");
	//		userService.save(new UserProfile(null, "Rafael", "Hosaka", "rafaelhosaka@gmail.com", LocalDate.of(1992,5,27));
		};
	}
}
