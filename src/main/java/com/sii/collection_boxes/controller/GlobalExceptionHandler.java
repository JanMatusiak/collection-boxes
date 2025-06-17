package com.sii.collection_boxes.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String,Object>> handleUncaught(@NotNull RuntimeException ex, @NotNull HttpServletRequest req) {
        return buildErrorResponse(ex.getMessage(), req.getRequestURI());
    }

    private @NotNull ResponseEntity<Map<String,Object>> buildErrorResponse(
            String message, String path) {
        Map<String,Object> body = Map.of(
                "message", message,
                "timestamp", Instant.now(),
                "error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "path", path,
                "status", HttpStatus.INTERNAL_SERVER_ERROR.value()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }

}
