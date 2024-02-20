package com.momo.task.manager.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id",nullable = false)
    private Long taskId;

    @Column(name = "task_name",nullable = false)
    private String taskName;

    @Column(name = "task_description",nullable = false)
    private String description;

    @Column(name = "task_type",nullable = false)
    private String type;

    @OneToOne
    @JoinColumn(name = "task_status",referencedColumnName = "status_id")
    private TaskStatus status;

    @Column(name = "task_label")
    private String label;

    @Column(name = "start_date",nullable = false)
    private Date startDate;

    @Column(name = "end_date",nullable = false)
    private Date endDate;

    @ManyToOne
    @JoinColumn(name = "project_id",referencedColumnName = "project_id")
    private Project project;

    @OneToMany(mappedBy = "task")
//    @JoinColumn(name="fileList",referencedColumnName = "file_id")
    private List<File> files;

    @OneToOne
    @JoinColumn(name = "assignee",referencedColumnName = "user_id",nullable = false)
    private User assigneeId;

    @OneToOne
    @JoinColumn(name = "reporter",referencedColumnName = "user_id",nullable = false)
    private User reporterId;

}
