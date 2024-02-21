package com.momo.task.manager.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.momo.task.manager.model.Project;
import com.momo.task.manager.model.Stages;
import com.momo.task.manager.model.TaskStatus;
import com.momo.task.manager.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.util.Date;

@Data
public class TaskDto {

    private String taskName;

    private String description;

    private TaskStatus status;

    private String label;

    private Date startDate;

    private Date endDate;

    private User assigneeId;

    private User reporterId;

    private Stages stageId;

//    boolean updatedFlag;
//
//    private Date updatedDate;
//    private Date updatedStageDate;
//
//    private Date updatedStatusDate;
}
