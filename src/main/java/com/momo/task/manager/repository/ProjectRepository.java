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
    Optional<Project> findByProjectName(String projectName);
    @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = "UPDATE project p " +
                    "SET p.active_flg = 0 , " +
                    "p.updated_flg = 1, " +
                    "p.updated_date = NOW(), " +
                    "p.end_date = CURRENT_DATE " +
                    "WHERE p.project_id = :projectId ")
    void deleteProjectById(Long projectId);
}
