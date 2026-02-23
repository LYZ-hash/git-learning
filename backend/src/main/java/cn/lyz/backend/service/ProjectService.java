package cn.lyz.backend.service;

import cn.lyz.backend.dto.CreateProjectRequest;
import cn.lyz.backend.dto.ProjectResponse;
import cn.lyz.backend.entity.Project;
import cn.lyz.backend.exception.ResourceNotFoundException;
import cn.lyz.backend.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    @Transactional
    public ProjectResponse create(CreateProjectRequest request) {
        if (projectRepository.existsByNameIgnoreCase(request.getName())) {
            throw new IllegalArgumentException("Project name '" + request.getName() + "' already exists");
        }
        Project project = Project.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        return toResponse(projectRepository.save(project));
    }

    @Transactional(readOnly = true)
    public List<ProjectResponse> getAll() {
        return projectRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ProjectResponse getById(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Transactional
    public ProjectResponse archive(Long id) {
        Project project = findOrThrow(id);
        project.setStatus(Project.ProjectStatus.ARCHIVED);
        return toResponse(projectRepository.save(project));
    }

    @Transactional
    public void delete(Long id) {
        if (!projectRepository.existsById(id)) {
            throw new ResourceNotFoundException("Project not found with id: " + id);
        }
        projectRepository.deleteById(id);
    }

    private Project findOrThrow(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
    }

    private ProjectResponse toResponse(Project p) {
        return ProjectResponse.builder()
                .id(p.getId())
                .name(p.getName())
                .description(p.getDescription())
                .status(p.getStatus())
                .createdAt(p.getCreatedAt())
                .build();
    }
}
