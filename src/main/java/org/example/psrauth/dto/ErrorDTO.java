package org.example.psrauth.dto;

import java.time.LocalDateTime;

public record ErrorDTO(
         int status,
         String error,
         String message
) {
}
