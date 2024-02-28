package com.momo.task.manager.model;

import com.momo.task.manager.utils.ResourceInformation;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "task_status")
public class TaskStatus {
    @Id
    @Column(name = "status_id")
    private Long statusId;

    @NotBlank(message = ResourceInformation.STATUS_NOT_BLANK_VALIDATION_MESSAGE)
    @Column(name = "status_name", nullable = false)
    private String statusName;
}
