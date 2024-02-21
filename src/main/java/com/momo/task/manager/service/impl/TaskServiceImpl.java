package com.momo.task.manager.service.impl;

import com.momo.task.manager.dto.TaskDto;
import com.momo.task.manager.model.File;
import com.momo.task.manager.model.Task;
import com.momo.task.manager.repository.FileRepository;
import com.momo.task.manager.repository.TaskRepository;
import com.momo.task.manager.service.interfaces.TaskService;
import com.momo.task.manager.utils.CheckUtils;
import org.hibernate.boot.jaxb.SourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.ast.OpAnd;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    TaskRepository taskRepository;

    @Autowired
    FileRepository fileRepository;

    @Autowired
    CheckUtils checkUtils;
    @Override
    public void createTask(Task task,MultipartFile file) throws IOException {
        Long checkReporterHasAccessToProject = checkUtils.checkUserProjectAccess(
                task.getReporterId().getUserId(),task.getProject().getProjectId()
        );
        if(checkReporterHasAccessToProject == 1){
            Long checkAssigneeHasAccessToProject = checkUtils.checkUserProjectAccess(
                    task.getAssigneeId().getUserId(),task.getProject().getProjectId()
            );
            if(checkAssigneeHasAccessToProject == 1){
                taskRepository.save(task);
                File file1 = new File();
                file1.setTask(task);
                file1.setFile(file);
                file1.setFileData(file.getBytes());
                fileRepository.save(file1);
                System.out.println("save done in both repo");
            }
            else{
                System.out.println("Assignee has no access to project");
                throw new RuntimeException();
            }
        }
        else{
            System.out.println("Reporter has no access to project");
            throw new RuntimeException();
        }

    }

    @Override
    public void deleteTask(Long taskId) {
        fileRepository.deleteByTaskId(taskId);
        taskRepository.deleteById(taskId);
    }

    @Override
    public void updateTask(Long taskId, TaskDto taskDto) {
        Optional<Task> optTask =taskRepository.findById(taskId);
        if (optTask.isPresent()){
            Task task = optTask.get();

            if( task.getTaskName() != null && !"".equalsIgnoreCase(task.getTaskName())){
                task.setTaskName(taskDto.getTaskName());
            }

            if( task.getDescription() != null && !"".equalsIgnoreCase(task.getDescription())){
                task.setDescription(taskDto.getDescription());
            }
            if (task.getStatus().getStatusId() != null){
                task.setStatus(taskDto.getStatus());
            }
            if( task.getLabel() != null && !"".equalsIgnoreCase(task.getLabel())){
                task.setLabel(taskDto.getLabel());
            }
            if(task.getStartDate() != null){
                task.setStartDate(taskDto.getStartDate());
            }
            if(task.getEndDate() != null){
                task.setEndDate(taskDto.getEndDate());
            }
            if(task.getAssigneeId()!=null){
                task.setAssigneeId(taskDto.getAssigneeId());
            }
            if(task.getReporterId()!=null){
                task.setReporterId(taskDto.getReporterId());
            }
            if(task.getStageId()!=null){
                task.setStageId(taskDto.getStageId());
            }

            taskRepository.save(task);
            System.out.println("save success in service");
        }
        else{
            System.out.println("save failed in service");
            throw new RuntimeException();
        }
    }


}
