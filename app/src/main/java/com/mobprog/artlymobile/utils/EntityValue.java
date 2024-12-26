package com.mobprog.artlymobile.utils;

public class EntityValue {
    private final String id;
    private final String Description;

    public EntityValue(String id, String description) {
        this.id = id;
        Description = description;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return Description;
    }
}
