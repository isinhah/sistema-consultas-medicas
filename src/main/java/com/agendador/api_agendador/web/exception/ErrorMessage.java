package com.agendador.api_agendador.web.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorMessage {

    private final String path;
    private final String method;
    private final int status;
    private final String statusMessage;
    private final String errorMessage;
    private Map<String, String> validationErrors;

    public ErrorMessage(
            HttpServletRequest request,
            HttpStatus status,
            String errorMessage) {
        this.path = request.getRequestURI();
        this.method = request.getMethod();
        this.status = status.value();
        this.statusMessage = status.getReasonPhrase();
        this.errorMessage = errorMessage;
    }

    public ErrorMessage(
            HttpServletRequest request,
            HttpStatus status,
            String errorMessage,
            BindingResult validationResult) {
        this.path = request.getRequestURI();
        this.method = request.getMethod();
        this.status = status.value();
        this.statusMessage = status.getReasonPhrase();
        this.errorMessage = errorMessage;
        addErrors(validationResult);
    }

    private void addErrors(BindingResult validationResult) {
        this.validationErrors = new HashMap<>();

        for (FieldError fieldError : validationResult.getFieldErrors()) {
            this.validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
    }
}