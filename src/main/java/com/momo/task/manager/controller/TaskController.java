package com.momo.task.manager.controller;

import com.momo.task.manager.dto.CheckDto;
import com.momo.task.manager.dto.TaskDto;
import com.momo.task.manager.model.Task;
import com.momo.task.manager.repository.StagesRepository;
import com.momo.task.manager.repository.TaskStatusRepository;
import com.momo.task.manager.service.interfaces.TaskService;
import com.momo.task.manager.utils.CheckUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;


@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    TaskService taskService;

    @Autowired
    CheckUtils checkUtils;

    @PostMapping("/create-tasks")
    public ResponseEntity<String> createTask(@ModelAttribute TaskDto taskDto) throws IOException {
        return taskService.createTask(taskDto);
    }

    @DeleteMapping("/delete-task/{taskId}")
    public String deleteTask(@PathVariable("taskId") Long taskId) {
        taskService.deleteTask(taskId);
        return "task removed";
    }

    @PutMapping("/update-task/{taskId}")
    public String updateTask(@PathVariable("taskId") Long taskId, @RequestBody TaskDto taskDto) throws ParseException {
        taskService.updateTask(taskId, taskDto);
        return "update successfully";
    }

    //CIRCULAR REFERENCE ERROR TO BE SOLVED
    @GetMapping("/projects/{projectId}/all-tasks")
    public ResponseEntity<?> getAllTasksOfProject(@PathVariable("projectId") Long projectId) {
        return taskService.getAllTask(projectId);
    }

    @GetMapping("/check")
    public Long check(@RequestBody CheckDto checkDto) {
        return checkUtils.checkUserProjectAccess(checkDto.getUserId(), checkDto.getProjectId());
    }
}
