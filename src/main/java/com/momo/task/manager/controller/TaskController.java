package com.momo.task.manager.controller;

import com.momo.task.manager.dto.TaskDto;
import com.momo.task.manager.model.Task;
import com.momo.task.manager.service.interfaces.TaskService;
import com.momo.task.manager.utils.CheckUtils;
import com.momo.task.manager.utils.ResourceEndpoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping(ResourceEndpoints.MAIN_TASK_KEY)
public class TaskController {
    TaskService taskService;
    CheckUtils checkUtils;

    @Autowired
    public TaskController(TaskService taskService, CheckUtils checkUtils) {
        this.taskService = taskService;
        this.checkUtils = checkUtils;
    }

    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    @PostMapping(ResourceEndpoints.CREATE_TASKS_ENDPOINT)
    public ResponseEntity<String> createTasks(@ModelAttribute TaskDto taskDto) {
        return taskService.createTask(taskDto);
    }

    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    @PostMapping(ResourceEndpoints.DELETE_TASKS_ENDPOINT)
    public ResponseEntity<String> deleteTasks(@PathVariable("taskId") Long taskId) {
        return taskService.deleteTask(taskId);
    }

    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    @PostMapping(value = ResourceEndpoints.UPDATE_TASKS_ENDPOINT)
    public ResponseEntity<String> updateTasks(@PathVariable("taskId") Long taskId, @ModelAttribute TaskDto taskDto) {
        return taskService.updateTask(taskId, taskDto);
    }

    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN','USER')")
    @GetMapping(ResourceEndpoints.GET_ALL_TASKS_IN_PROJECT_ENDPOINT)
    public ResponseEntity<List<Task>> getAllTasksOfProjects(@PathVariable("projectKey") String projectKey) {
        return taskService.getAllTask(projectKey);
    }

}
