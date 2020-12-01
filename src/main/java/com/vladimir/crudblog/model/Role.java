package com.vladimir.crudblog.model;

public enum Role {
    ADMIN("admin"), MODERATOR("moder"), USER("user");
    private final String role;

    Role(String role) {
        this.role = role;
    }

    public String toString() {
        return role;
    }

    public static Role parseRole(String role){
        if(role.equals("user"))
            return USER;
        if(role.equals("moder"))
            return MODERATOR;
        if(role.equals("admin"))
            return ADMIN;
        throw new IllegalArgumentException(role + " is not a role");
    }
}
