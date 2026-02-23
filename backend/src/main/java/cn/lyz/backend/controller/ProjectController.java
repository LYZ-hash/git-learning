package cn.lyz.backend.controller;

import cn.lyz.backend.dto.CreateProjectRequest;
import cn.lyz.backend.dto.ProjectResponse;
import cn.lyz.backend.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ProjectController - REST endpoints for project management.
 *
 * POST /api/projects - Create project
 * GET /api/projects - List all projects
 * GET /api/projects/{id} - Get project by ID
 * PATCH /api/projects/{id}/archive - Archive project
 * DELETE /api/projects/{id} - Delete project
 */
@RestController
@RequestMapping("/api/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectResponse> create(@Valid @RequestBody CreateProjectRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getAll() {
        return ResponseEntity.ok(projectService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getById(id));
    }

    @PatchMapping("/{id}/archive")
    public ResponseEntity<ProjectResponse> archive(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.archive(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        projectService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
