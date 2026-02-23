package cn.lyz.backend.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO for user responses - masks sensitive fields like password before sending
 * to client.
 */
@Data
@Builder
public class UserResponse {

    private Long id;
    private String username;
    private String email;
    private LocalDateTime createdAt;
}
