package com.momo.task.manager.service.interfaces;

import com.momo.task.manager.dto.TaskDto;
import com.momo.task.manager.model.Task;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TaskService {
    ResponseEntity<String> createTask(TaskDto taskDto);

    ResponseEntity<String> deleteTask(Long taskId);

    ResponseEntity<String> updateTask(Long taskId, TaskDto taskDto);

    ResponseEntity<List<Task>>getAllTask(Long projectId);
}
