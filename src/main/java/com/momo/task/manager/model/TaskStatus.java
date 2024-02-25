package com.momo.task.manager.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskStatus {
    @Id
    @Column(name = "status_id")
    private Long statusId;

    @Column(name = "status_name", nullable = false)
    private String statusName;
}
