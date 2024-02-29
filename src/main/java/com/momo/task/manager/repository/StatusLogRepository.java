package com.momo.task.manager.repository;

import com.momo.task.manager.model.StatusLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface StatusLogRepository extends JpaRepository<StatusLog,Long> {
    @Transactional
    @Modifying
    @Query(nativeQuery = true,
            value = "UPDATE status_log l " +
                    "SET l.active_flg = 0 , " +
                    "WHERE l.task_id = :taskId ")
    void deleteByTaskId(Long taskId);
}
