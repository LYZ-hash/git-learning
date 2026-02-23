package cn.lyz.backend.repository;

import cn.lyz.backend.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    /**
     * Find all projects by status (e.g. ACTIVE / ARCHIVED).
     */
    List<Project> findByStatus(Project.ProjectStatus status);

    /**
     * Check if a project name already exists (case-insensitive).
     */
    boolean existsByNameIgnoreCase(String name);
}
