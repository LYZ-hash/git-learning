package cn.lyz.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateProjectRequest {

    @NotBlank(message = "Project name cannot be blank")
    @Size(max = 100, message = "Project name must not exceed 100 characters")
    private String name;

    private String description;
}
