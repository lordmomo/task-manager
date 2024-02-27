package com.momo.task.manager.controller;

import com.momo.task.manager.dto.CheckDto;
import com.momo.task.manager.dto.TaskDto;
import com.momo.task.manager.model.Task;
import com.momo.task.manager.service.interfaces.TaskService;
import com.momo.task.manager.utils.CheckUtils;
import com.momo.task.manager.utils.ResourceEndpoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
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

    @PostMapping(ResourceEndpoints.CREATE_TASKS_ENDPOINT)
    public ResponseEntity<String> createTasks(@ModelAttribute TaskDto taskDto) throws IOException {
        return taskService.createTask(taskDto);
    }

    @DeleteMapping(ResourceEndpoints.DELETE_TASKS_ENDPOINT)
    public ResponseEntity<String> deleteTasks(@PathVariable("taskId") Long taskId) {
        return taskService.deleteTask(taskId);
    }

    @PutMapping(ResourceEndpoints.UPDATE_TASKS_ENDPOINT)
    public ResponseEntity<String> updateTasks(@PathVariable("taskId") Long taskId, @RequestBody TaskDto taskDto) throws ParseException {
        return taskService.updateTask(taskId, taskDto);
    }

    @GetMapping(ResourceEndpoints.GET_ALL_TASKS_IN_PROJECT_ENDPOINT)
    public ResponseEntity<List<Task>> getAllTasksOfProjects(@PathVariable("projectId") Long projectId) {
        return taskService.getAllTask(projectId);
    }

    @GetMapping("/check")
    public boolean check(@RequestBody CheckDto checkDto) {
        return checkUtils.checkUserProjectAccess(checkDto.getUserId(), checkDto.getProjectId());
    }
}
