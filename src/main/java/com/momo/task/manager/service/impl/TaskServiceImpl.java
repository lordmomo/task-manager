package com.momo.task.manager.service.impl;

import com.momo.task.manager.dto.TaskDto;
import com.momo.task.manager.model.*;
import com.momo.task.manager.repository.*;
import com.momo.task.manager.service.interfaces.TaskService;
import com.momo.task.manager.utils.CheckUtils;
import org.hibernate.boot.jaxb.SourceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.ast.OpAnd;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.handler.UserRoleAuthorizationInterceptor;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Stack;

@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    TaskRepository taskRepository;

    @Autowired
    FileRepository fileRepository;

    @Autowired
    TaskStatusRepository taskStatusRepository;

    @Autowired
    SuperAdminRepository superAdminRepository;
    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    StagesRepository stagesRepository;
    @Autowired
    CheckUtils checkUtils;

    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    Date date = new Date();
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
                Optional<TaskStatus> status = taskStatusRepository.findById(taskDto.getStatus());
                if(status.isPresent()) {
                    task.setStatus(status.get());
                }
                else{
                    System.out.println("task status not found");
                }
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
                Optional<User> user = superAdminRepository.findById(taskDto.getAssigneeId());
                if(user.isPresent()) {
                    task.setAssigneeId(user.get());
                }
                else {
                    System.out.println("User not found");
                }
            }
            if(task.getReporterId()!=null){
                Optional<User> user = superAdminRepository.findById(taskDto.getReporterId());
                if(user.isPresent()) {
                    task.setReporterId(user.get());
                }
                else {
                    System.out.println("User not found");
                }
            }
            if(task.getStageId()!=null){
                Optional<Stages> stages = stagesRepository.findById(taskDto.getStageId());
                if(stages.isPresent()) {
                    task.setStageId(stages.get());
                }
                else {
                    System.out.println("User not found");
                }
            }

            task.setUpdatedFlag(true);
            task.setUpdatedDate(new Date());
            task.setUpdatedStageDate(new Date());
            task.setUpdatedStatusDate(new Date());

            taskRepository.save(task);
            System.out.println("save success in service");
        }
        else{
            System.out.println("save failed in service");
            throw new RuntimeException();
        }
    }

    @Override
    public List<Task> getAllTask(Long projectId) {
        Optional<Project> project = projectRepository.findById(projectId);
        if(project.isPresent()) {
            return taskRepository.findAllByProject_ProjectId(project.get().getProjectId());
        }
        else{
            System.out.println("project not found");
            return null;
        }
    }


}
