package com.momo.task.manager.service.interfaces;

import com.momo.task.manager.dto.TaskDto;
import com.momo.task.manager.model.Task;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface TaskService {
    void createTask(Task task, MultipartFile file) throws IOException;

    void deleteTask(Long taskId);

    void updateTask(Long taskId, TaskDto taskDto);
}
