package com.rafaelhosaka.shareme.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ThemePreference {
    LIGHT("light"),
    DARK("dark"),
    DEVICE("device");


    private String name;

    ThemePreference(String name) {
        this.name = name;
    }


    @JsonValue
    public String getName() {
        return name;
    }
}
