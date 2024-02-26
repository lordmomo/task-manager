package com.momo.task.manager.controller;

import com.momo.task.manager.dto.CheckDto;
import com.momo.task.manager.dto.TaskDto;
import com.momo.task.manager.service.interfaces.TaskService;
import com.momo.task.manager.utils.CheckUtils;
import com.momo.task.manager.utils.ResourceInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;



@RestController
@RequestMapping(ResourceInformation.MAIN_TASK_KEY)
public class TaskController {
    TaskService taskService;
    CheckUtils checkUtils;
    @Autowired
    public TaskController(TaskService taskService, CheckUtils checkUtils) {
        this.taskService = taskService;
        this.checkUtils = checkUtils;
    }

    @PostMapping(ResourceInformation.CREATE_TASKS_ENDPOINT)
    public ResponseEntity<String> createTasks(@ModelAttribute TaskDto taskDto) throws IOException {
        return taskService.createTask(taskDto);
    }

    @DeleteMapping(ResourceInformation.DELETE_TASKS_ENDPOINT)
    public String deleteTasks(@PathVariable("taskId") Long taskId) {
        taskService.deleteTask(taskId);
        return "task removed";
    }

    @PutMapping(ResourceInformation.UPDATE_TASKS_ENDPOINT)
    public String updateTasks(@PathVariable("taskId") Long taskId, @RequestBody TaskDto taskDto) throws ParseException {
        taskService.updateTask(taskId, taskDto);
        return "update successfully";
    }

    @GetMapping(ResourceInformation.GET_ALL_TASKS_IN_PROJECT_ENDPOINT)
    public ResponseEntity<?> getAllTasksOfProjects(@PathVariable("projectId") Long projectId) {
        return taskService.getAllTask(projectId);
    }

    @GetMapping("/check")
    public Long check(@RequestBody CheckDto checkDto) {
        return checkUtils.checkUserProjectAccess(checkDto.getUserId(), checkDto.getProjectId());
    }
}
