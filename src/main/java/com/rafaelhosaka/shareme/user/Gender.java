package com.rafaelhosaka.shareme.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Gender {
    MALE("male"),
    FEMALE("female"),
    NONE("");

    private String name;

    Gender(String name) {
            this.name = name;
        }


    @JsonValue
    public String getName() {
            return name;
        }
}