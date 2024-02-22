package com.momo.task.manager.service.impl;

import com.momo.task.manager.dto.TaskDto;
import com.momo.task.manager.dto.UserDto;
import com.momo.task.manager.model.*;
import com.momo.task.manager.repository.*;
import com.momo.task.manager.service.interfaces.TaskService;
import com.momo.task.manager.utils.CheckUtils;
import org.hibernate.boot.jaxb.SourceType;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.ast.OpAnd;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.handler.UserRoleAuthorizationInterceptor;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    ModelMapper mapper ;
    @Autowired
    public TaskServiceImpl() {
        this.mapper = new ModelMapper();
        configureModelMapper();
    }
    private void configureModelMapper(){
        // Use strict matching strategy to avoid conflicts
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // Explicitly define mappings to avoid conflicts
        mapper.createTypeMap(TaskDto.class, Task.class)
                .addMapping(TaskDto::getReporterId, Task::setReporterId)
                .addMapping(TaskDto::getAssigneeId, Task::setAssigneeId)
                .addMapping(TaskDto::getStageId, Task::setStageId);
    }


//    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    @Override
    public void createTask(TaskDto taskDto) throws IOException {

        Long checkReporterHasAccessToProject = checkUtils.checkUserProjectAccess(
                taskDto.getReporterId(),taskDto.getProject()
        );
        if(checkReporterHasAccessToProject == 1){
            Long checkAssigneeHasAccessToProject = checkUtils.checkUserProjectAccess(
                    taskDto.getAssigneeId(),taskDto.getProject()
            );
            if(checkAssigneeHasAccessToProject == 1){
                Task task = new Task();
                task.setTaskName(taskDto.getTaskName());
                task.setDescription(taskDto.getDescription());
                task.setType(taskDto.getType());
                task.setStatus(checkUtils.getStatusFromId(taskDto.getStatus()));
                task.setStartDate(taskDto.getStartDate());
                task.setEndDate(taskDto.getEndDate());
                task.setProject(checkUtils.getProjectFromId(taskDto.getProject()));
                task.setAssigneeId(checkUtils.getUserFromId(taskDto.getAssigneeId()));
                task.setReporterId(checkUtils.getUserFromId(taskDto.getReporterId()));
                task.setStageId(checkUtils.getStageFromId(taskDto.getStageId()));

                taskRepository.save(task);

                MultipartFile attachment = taskDto.getFile();
                File file = new File();
                file.setTask(task);
                if(attachment!=null && !attachment.isEmpty())
                {
                    file.setFileData(attachment.getBytes());
                }else{
                    file.setFileData(null);
                }
                fileRepository.save(file);
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
    public void updateTask(Long taskId, TaskDto taskDto) throws ParseException {
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

            LocalDateTime currentDateTime = LocalDateTime.now();
            
            task.setUpdatedDate(currentDateTime);
            task.setUpdatedStageDate(currentDateTime);
            task.setUpdatedStatusDate(currentDateTime);

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
