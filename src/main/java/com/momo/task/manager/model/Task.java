package com.momo.task.manager.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.momo.task.manager.utils.CustomTaskSerializer;
import com.momo.task.manager.utils.ConstantInformation;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "task")
@JsonSerialize(using = CustomTaskSerializer.class)
public class Task implements Serializable {

    @Column(name = "active_flg", nullable = false)
    boolean activeFlg;

    @Column(name = "updated_flg", nullable = false)
    boolean updatedFlg;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id", nullable = false)
    private Long taskId;

    @NotBlank(message = ConstantInformation.TASK_NAME_REQUIRED_MESSAGE)
    @Column(name = "task_name", nullable = false)
    private String taskName;

    @NotBlank(message = ConstantInformation.TASK_DESCRIPTION_REQUIRED_MESSAGE)
    @Column(name = "task_description", nullable = false)
    private String description;

    @NotBlank(message = ConstantInformation.TASK_TYPE_REQUIRED_MESSAGE)
    @Column(name = "task_type", nullable = false)
    private String type;

    @ManyToOne
    @JoinColumn(name = "task_status", referencedColumnName = "status_id")
    private TaskStatus status;

    @NotNull(message = ConstantInformation.TASK_START_DATE_REQUIRED_MESSAGE)
    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @NotNull(message = ConstantInformation.TASK_END_DATE_REQUIRED_MESSAGE)
    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @ManyToOne
    @JoinColumn(name = "project_id", referencedColumnName = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "assignee", referencedColumnName = "user_id", nullable = false)
    private User assigneeId;

    @ManyToOne
    @JoinColumn(name = "reporter", referencedColumnName = "user_id", nullable = false)
    private User reporterId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "updated_date")
    @PastOrPresent(message = ConstantInformation.UPDATE_DATE_BEAN_VALIDATION_MESSAGE)
    private LocalDateTime updatedDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "updated_stage_date")
    private LocalDateTime updatedStageDate;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Column(name = "updated_status_date")
    private LocalDateTime updatedStatusDate;

}
