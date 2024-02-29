package com.momo.task.manager.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "status_log")
public class StatusLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long logId;

    @ManyToOne
    @JoinColumn(name = "project_id",referencedColumnName = "project_id",nullable = false)
    private Project projectId;

    @ManyToOne
    @JoinColumn(name = "task_id",referencedColumnName = "task_id",nullable = false)
    private Task taskId;

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "user_id",nullable = false)
    private User userId;

    @Column(name = "transition_date",nullable = false)
    private LocalDateTime transitionDate;

    @ManyToOne
    @JoinColumn(name = "previous_status",referencedColumnName = "status_id")
    private TaskStatus previousStatus;

    @ManyToOne
    @JoinColumn(name = "current_status",referencedColumnName = "status_id",nullable = false)
    private TaskStatus currentStatus;

    @Column(name = "active_flg")
    private boolean activeFlag;

}
