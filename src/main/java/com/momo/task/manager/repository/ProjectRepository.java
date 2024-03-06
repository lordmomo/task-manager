package com.momo.task.manager.repository;

import com.momo.task.manager.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project,Long> {
    @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = "UPDATE project p " +
                    "SET p.active_flg = 0 , " +
                    "p.updated_flg = 1, " +
                    "p.updated_date = NOW(), " +
                    "p.end_date = NOW() " +
                    "WHERE p.project_id = :projectId ")
    void deleteProjectById(Long projectId);

    @Query(nativeQuery = true,
            value = "SELECT * " +
                    "FROM project p " +
                    "WHERE p.project_key = :projectKey ")
    Optional<Project> findByProjectKey(String projectKey);

    @Query(nativeQuery = true,
            value="SELECT p.project_id FROM PROJECT p WHERE project_key = :projectKey ")
    Long getProjectIdFromProjectKey(String projectKey);
}
