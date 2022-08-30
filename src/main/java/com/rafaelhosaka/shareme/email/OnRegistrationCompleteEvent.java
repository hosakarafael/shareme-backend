package com.rafaelhosaka.shareme.email;

import com.rafaelhosaka.shareme.applicationuser.ApplicationUser;
import com.rafaelhosaka.shareme.user.LanguagePreference;
import com.rafaelhosaka.shareme.user.UserProfile;
import lombok.Data;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

@Getter
public class OnRegistrationCompleteEvent extends ApplicationEvent {
    private LanguagePreference language;
    private UserProfile user;

    public OnRegistrationCompleteEvent(
            UserProfile user, LanguagePreference language) {
        super(user);

        this.user = user;
        this.language = language;
    }

}
