package com.example.tasktracker.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorResponse> notFound(NotFoundException ex, HttpServletRequest req) {
        return build(HttpStatus.NOT_FOUND, "NOT_FOUND", ex.getMessage(), req.getRequestURI(), null);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiErrorResponse> conflict(ConflictException ex, HttpServletRequest req) {
        return build(HttpStatus.CONFLICT, "CONFLICT", ex.getMessage(), req.getRequestURI(), null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> validation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        List<ApiErrorResponse.FieldErrorItem> fields = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::toItem)
                .toList();

        return build(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "Validation failed", req.getRequestURI(), fields);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> unexpected(Exception ex, HttpServletRequest req) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", ex.getMessage(), req.getRequestURI(), null);
    }

    private ApiErrorResponse.FieldErrorItem toItem(FieldError fe) {
        String msg = fe.getDefaultMessage() != null ? fe.getDefaultMessage() : "Invalid value";
        return new ApiErrorResponse.FieldErrorItem(fe.getField(), msg);
    }

    private ResponseEntity<ApiErrorResponse> build(HttpStatus status, String error, String message, String path,
                                                   List<ApiErrorResponse.FieldErrorItem> fieldErrors) {
        ApiErrorResponse body = new ApiErrorResponse(
                LocalDateTime.now(),
                status.value(),
                error,
                message,
                path,
                fieldErrors
        );
        return ResponseEntity.status(status).body(body);
    }
}
