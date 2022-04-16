package com.rafaelhosaka.shareme.bucket;

public enum BucketName {
    PROFILE_IMAGE("shareme-image-upload");

    private String name;

    BucketName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
