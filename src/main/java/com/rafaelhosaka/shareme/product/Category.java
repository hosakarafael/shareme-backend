package com.rafaelhosaka.shareme.product;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Category {
    VEHICLE("vehicle"),
    APPAREL("apparel"),
    ELECTRONIC("electronic"),
    HOME_GOODS("home_goods"),
    MUSICAL_INSTRUMENT("musical_instrument"),
    GAME("game"),
    OTHER("other");

    private String name;

    Category(String name) {
        this.name = name;
    }


    @JsonValue
    public String getName() {
        return name;
    }
}
