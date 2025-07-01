package com.agendador.api_agendador.util;

import com.agendador.api_agendador.entity.enums.Role;
import com.agendador.api_agendador.security.CustomUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityContextTestUtil {

    public static void mockAuthenticatedUser(Long userId, String email, Role role) {
        CustomUserDetails principal = new CustomUserDetails(
                userId,
                "Test User",
                email,
                role
        );

        Authentication auth = new UsernamePasswordAuthenticationToken(
                principal,
                null,
                principal.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    public static void clear() {
        SecurityContextHolder.clearContext();
    }
}