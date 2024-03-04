package com.momo.task.manager.repository;

import com.momo.task.manager.model.TaskLabel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface TaskLabelRepository extends JpaRepository<TaskLabel, Long> {

    @Query(nativeQuery = true,
            value = "Select t.task_id " +
                    "from task_label t " +
                    "where t.label_id = :labelId")
    List<Long> getTaskIdFromLabel(Long labelId);

    @Query(nativeQuery = true,
            value = "Select * " +
                    "from task_label tl " +
                    "where tl.task_id = :taskId and tl.label_id = :labelId")
    TaskLabel findByTaskIdAndLabelId(Long taskId, Long labelId);

    @Query(nativeQuery = true,
            value = "Select tl.label_id " +
                    "from task_label tl " +
                    "where tl.task_id = :taskId ")
    List<Long> getAllLabelIdFromTaskId(Long taskId);

    @Modifying
    @Transactional
    @Query(nativeQuery = true,
            value = "DELETE " +
                    "from task_label tl " +
                    "where tl.task_id = :taskId and tl.label_id = :labelId ")
    void deleteByTaskIdAndLabelName(Long taskId, Long labelId);
}
