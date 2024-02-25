package com.momo.task.manager.repository;

import com.momo.task.manager.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {
    @Query(nativeQuery = true,
            value = "SELECT * FROM TASK t WHERE t.project_id = :projectId"
    )
    List<Task>findByProdId(Long projectId);
    @Query(
            nativeQuery = true,
            value = "SELECT COUNT(*) FROM task t WHERE t.task_id = :taskId AND t.project_id = :projectId"
    )
    Long validateCheckTaskIdBelongsToProjectId(Long projectId, Long taskId);
}
