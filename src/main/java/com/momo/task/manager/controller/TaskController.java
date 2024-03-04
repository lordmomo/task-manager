package com.momo.task.manager.controller;

import com.momo.task.manager.dto.TaskDto;
import com.momo.task.manager.response.CustomResponse;
import com.momo.task.manager.service.interfaces.TaskService;
import com.momo.task.manager.utils.CheckUtils;
import com.momo.task.manager.utils.ConstantEndpoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(ConstantEndpoints.MAIN_TASK_KEY)
public class TaskController {
    TaskService taskService;
    CheckUtils checkUtils;

    @Autowired
    public TaskController(TaskService taskService, CheckUtils checkUtils) {
        this.taskService = taskService;
        this.checkUtils = checkUtils;
    }

    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    @PostMapping(ConstantEndpoints.CREATE_TASKS_ENDPOINT)
    public ResponseEntity<String> createTasks(@ModelAttribute TaskDto taskDto) {
        return taskService.createTask(taskDto);
    }

    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    @PostMapping(ConstantEndpoints.DELETE_TASKS_ENDPOINT)
    public CustomResponse<Object> deleteTasks(@PathVariable("taskId") Long taskId, @PathVariable("projectKey") String projectKey) {
        return taskService.deleteTask(projectKey, taskId);
    }

    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN')")
    @PostMapping(value = ConstantEndpoints.UPDATE_TASKS_ENDPOINT)
    public CustomResponse<Object> updateTasks(@PathVariable("taskId") Long taskId, @PathVariable("projectKey") String projectKey, @ModelAttribute TaskDto taskDto) {
        return taskService.updateTask(projectKey, taskId, taskDto);
    }

    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN','USER')")
    @GetMapping(ConstantEndpoints.GET_ALL_TASKS_IN_PROJECT_ENDPOINT)
    public CustomResponse<Object> getAllTasksOfProjects(@PathVariable("projectKey") String projectKey) {
        return taskService.getAllTask(projectKey);
    }

    @PreAuthorize("hasAnyAuthority('SUPER_ADMIN','ADMIN','USER')")
    @GetMapping(ConstantEndpoints.GET_ALL_TASKS_OF_LABEL)
    public CustomResponse<Object> getAllTasksOfLabel(@PathVariable("projectKey") String projectKey, @PathVariable("labelName") String labelName) {
        return taskService.getAllTaskOfLabel(projectKey, labelName);
    }

}
