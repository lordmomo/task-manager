package com.momo.task.manager.service.interfaces;

import com.momo.task.manager.dto.TaskDto;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.text.ParseException;

public interface TaskService {
    ResponseEntity<String> createTask(TaskDto taskDto) throws IOException;

    ResponseEntity<String> deleteTask(Long taskId);

    ResponseEntity<String> updateTask(Long taskId, TaskDto taskDto) throws ParseException;

    ResponseEntity<?> getAllTask(Long projectId);
}
