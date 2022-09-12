package com.rafaelhosaka.shareme.product;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Category {
    VEHICLE("vehicle"),
    APPAREL("apparel"),
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
