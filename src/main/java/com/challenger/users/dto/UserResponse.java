package com.challenger.users.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponse(UUID id, LocalDateTime created, LocalDateTime modified, LocalDateTime lastLogin
        , String token, boolean isActive) {
}
