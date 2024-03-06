package com.momo.task.manager.repository;

import com.momo.task.manager.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    @Query(nativeQuery = true,
            value = "SELECT * FROM TASK t WHERE t.project_id = :projectId AND t.active_flg = 1"
    )
    List<Task> findByProdId(Long projectId);

    @Query(nativeQuery = true,
            value = "SELECT * FROM TASK t where t.project_id=:projectId and t.task_id=:taskId"
    )
    Task doesTaskIdBelongToProjectId(Long projectId, Long taskId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = "UPDATE task t " +
                    "SET t.active_flg = 0 , " +
                    "t.updated_flg = 1, " +
                    "t.updated_date = NOW(), " +
                    "t.end_date = CURRENT_DATE " +
                    "WHERE t.task_id = :taskId")
    void deleteByTaskId(Long taskId);

    @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = "UPDATE task t " +
                    "SET t.active_flg = 0 , " +
                    "t.updated_flg = 1, " +
                    "t.updated_date = NOW(), " +
                    "t.end_date = CURRENT_DATE " +
                    "WHERE t.project_id = :projectId ")
    void deleteByProjectId(Long projectId);

    @Query(nativeQuery = true,
            value = "Select t.task_id from task t where t.project_id = :projectId and t.active_flg = 1")
    List<Long> getAllTaskIdFromProjectId(Long projectId);

    @Query(nativeQuery = true,
            value = "Select t.task_id from task t where t.task_name = :taskName and t.active_flg = 1")
    Long getTaskIdFromTaskName(String taskName);
}
