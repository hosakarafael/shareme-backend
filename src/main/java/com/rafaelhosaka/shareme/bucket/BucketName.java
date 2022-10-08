package com.rafaelhosaka.shareme.bucket;

public enum BucketName {
    USERS("users"),
    POSTS("posts"),
    PRODUCTS("products"),
    GROUPS("groups");

    private String name;

    BucketName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
