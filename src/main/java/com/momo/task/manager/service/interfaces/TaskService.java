package com.momo.task.manager.service.interfaces;

import com.momo.task.manager.dto.TaskDto;
import com.momo.task.manager.model.Task;
import com.momo.task.manager.response.CustomResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TaskService {
    ResponseEntity<String> createTask(TaskDto taskDto);

    CustomResponse<Object> deleteTask(String projectKey,Long taskId);

    CustomResponse<Object> updateTask(String projectKey,Long taskId, TaskDto taskDto);

    CustomResponse<Object> getAllTask(String projectKey);

    CustomResponse<Object> getAllTaskOfLabel(String projectKey, String labelName);
}
