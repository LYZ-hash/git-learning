package cn.lyz.backend.dto;

import cn.lyz.backend.entity.Project;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ProjectResponse {

    private Long id;
    private String name;
    private String description;
    private Project.ProjectStatus status;
    private LocalDateTime createdAt;
}
