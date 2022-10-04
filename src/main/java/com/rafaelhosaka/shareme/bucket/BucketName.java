package com.rafaelhosaka.shareme.bucket;

public enum BucketName {
    USERS("shareme-image-upload/users"),
    POSTS("shareme-image-upload/posts"),
    PRODUCTS("shareme-image-upload/products"),
    GROUPS("shareme-image-upload/groups");

    private String name;

    BucketName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
