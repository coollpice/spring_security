package com.boot.security.model;


public enum ERole {

    ADMIN("ROLE_ADMIN") , MANAGER("ROLE_MANAGER"), GUEST("ROLE_GUEST");
    private String value;

    private ERole(String value) {
        this.setValue(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
