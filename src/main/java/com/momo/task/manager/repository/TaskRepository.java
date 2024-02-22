package com.momo.task.manager.repository;

import com.momo.task.manager.model.Project;
import com.momo.task.manager.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {
//    @Query(nativeQuery = true,
//            value="SELECT * from task WHERE project_id = :")
    List<Task>findAllByProject_ProjectId(Long projectId);
}
