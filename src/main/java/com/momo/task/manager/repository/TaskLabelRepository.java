package com.momo.task.manager.repository;

import com.momo.task.manager.model.TaskLabel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskLabelRepository  extends JpaRepository<TaskLabel,Long> {

    @Query(nativeQuery = true,
            value = "Select t.task_id " +
                    "from task_label t " +
                    "where t.label_id = :labelId")
    List<Long> getTaskIdFromLabel(Long labelId);
}
