package com.rafaelhosaka.shareme.product;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Currency {
    USD("$"),
    JPY("¥");

    private String name;

    Currency(String name) {
        this.name = name;
    }


    @JsonValue
    public String getName() {
        return name;
    }
}
