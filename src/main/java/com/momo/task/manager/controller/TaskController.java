package com.momo.task.manager.controller;

import com.momo.task.manager.dto.CheckDto;
import com.momo.task.manager.dto.TaskDto;
import com.momo.task.manager.model.Stages;
import com.momo.task.manager.model.Task;
import com.momo.task.manager.model.TaskStatus;
import com.momo.task.manager.repository.StagesRepository;
import com.momo.task.manager.repository.TaskStatusRepository;
import com.momo.task.manager.service.interfaces.TaskService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.momo.task.manager.utils.CheckUtils;
import java.io.IOException;


@RestController
@RequestMapping("/task")
public class TaskController {
    @Autowired
    TaskService taskService;
    @Autowired
    StagesRepository stagesRepository;
    @Autowired
    TaskStatusRepository taskStatusRepository;

    @Autowired
    CheckUtils checkUtils;

    @PostMapping("/create-tasks")
    private String createTask(@ModelAttribute Task task,
                              @RequestParam String status,
                              @RequestParam String stageId,
                              @RequestParam MultipartFile file) throws IOException {
        // Fetch Stages and TaskStatus entities from the database
        Stages stage = stagesRepository.findById(Long.valueOf(stageId))
                .orElseThrow(() -> new EntityNotFoundException("Stages not found with ID: " + stageId));
        task.setStageId(stage);

        TaskStatus taskStatus = taskStatusRepository.findById(Long.valueOf(status))
                .orElseThrow(() -> new EntityNotFoundException("TaskStatus not found with ID: " + status));
        task.setStatus(taskStatus);

        taskService.createTask(task,file);
        return "success";
    }

    @DeleteMapping("/delete-task/{taskId}")
    public String deleteTask(@PathVariable("taskId") Long taskId){
        taskService.deleteTask(taskId);
        return "task removed";
    }

    @PutMapping("/update-task/{taskId}")
    public String updateTask(@PathVariable("taskId") Long taskId,@RequestBody TaskDto taskDto){
        taskService.updateTask(taskId,taskDto);
        return "update successfully";
    }

    @GetMapping("/check")
    private Boolean check(@RequestBody CheckDto checkDto){
       Long num = checkUtils.checkUserProjectAccess(checkDto.getUserId(), checkDto.getProjectId());
       if(num == 1){
           return true;
       }
       return false;
    }
}
