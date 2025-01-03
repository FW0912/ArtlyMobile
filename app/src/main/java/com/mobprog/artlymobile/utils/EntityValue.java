package com.mobprog.artlymobile.utils;

public class EntityValue {
    private final String id;
    private final String description;

    public EntityValue(String id, String description) {
        this.id = id;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
