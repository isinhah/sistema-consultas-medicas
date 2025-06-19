package com.agendador.api_agendador.entity.enums;

public enum Role {
    DOCTOR,
    RECEPTIONIST,
    PATIENT,
    ADMIN;

    public static Role fromString(String role) {
        try {
            return Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + role);
        }
    }
}