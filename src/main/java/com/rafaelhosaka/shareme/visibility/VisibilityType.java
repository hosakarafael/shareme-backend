package com.rafaelhosaka.shareme.visibility;

import com.fasterxml.jackson.annotation.JsonValue;

public enum VisibilityType {
    PUBLIC("public"),
    GROUP("group");

    private String name;

    VisibilityType(String name) {
        this.name = name;
    }


    @JsonValue
    public String getName() {
        return name;
    }
}
