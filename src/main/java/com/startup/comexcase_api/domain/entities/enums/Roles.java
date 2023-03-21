package com.startup.comexcase_api.domain.entities.enums;

public enum Roles {
    DEALER("ROLE_DEALER"), ADMIN("ROLE_ADMIN"), PROVIDER("ROLE_PROVIDER");

    private final String name;

    Roles(String name) {
        this.name = name;
    }

    public String getValue() {
        return this.name;
    }
}
