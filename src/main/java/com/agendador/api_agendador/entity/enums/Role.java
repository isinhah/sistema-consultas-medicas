package com.agendador.api_agendador.entity.enums;

import com.agendador.api_agendador.web.exception.BadRequestException;

public enum Role {
    USER,
    DOCTOR,
    RECEPTIONIST,
    PATIENT,
    ADMIN;

    public static Role fromString(String role) {
        try {
            return Role.valueOf(role.toUpperCase());
        } catch (BadRequestException e) {
            throw new BadRequestException("Invalid role: " + role);
        }
    }
}