package com.momo.task.manager.service.interfaces;

import com.momo.task.manager.dto.TaskDto;
import com.momo.task.manager.model.Task;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface TaskService {
    void createTask(TaskDto taskDto) throws IOException;

    void deleteTask(Long taskId);

    void updateTask(Long taskId, TaskDto taskDto) throws ParseException;

    List<Task> getAllTask(Long projectId);
}
