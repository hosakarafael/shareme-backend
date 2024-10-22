package com.rafaelhosaka.shareme;

import com.rafaelhosaka.shareme.applicationuser.ApplicationUser;
import com.rafaelhosaka.shareme.applicationuser.ApplicationUserService;
import com.rafaelhosaka.shareme.applicationuser.Role;
import com.rafaelhosaka.shareme.user.UserProfile;
import com.rafaelhosaka.shareme.user.UserProfileService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import io.github.cdimascio.dotenv.Dotenv;
import java.time.LocalDate;
import java.util.ArrayList;

@SpringBootApplication
public class SharemeApplication {

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		System.setProperty("PROJECT_CLIENT_URL", dotenv.get("PROJECT_CLIENT_URL"));
		System.setProperty("SPRING_DATA_MONGODB_URI", dotenv.get("SPRING_DATA_MONGODB_URI"));
		System.setProperty("SPRING_DATA_MONGODB_DATABASE", dotenv.get("SPRING_DATA_MONGODB_DATABASE"));
		System.setProperty("SPRING_MAIL_PASSWORD", dotenv.get("SPRING_MAIL_PASSWORD"));
		SpringApplication.run(SharemeApplication.class, args);
	}

	@Bean
	CommandLineRunner run(UserProfileService userService){
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
