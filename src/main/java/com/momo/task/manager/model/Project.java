package com.momo.task.manager.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "project_name", nullable = false)
    private String projectName;

    // distinguishly  identify project
    @Column(name = "project_key", nullable = false)
    private String key;

    // kanban,scrum
    @Column(name = "project_template", nullable = false)
    private String template;
    //team, company managed
    @Column(name = "project_type", nullable = false)
    private String type;

    @ManyToOne
    @JoinColumn(name = "project_lead", referencedColumnName = "user_id")
    private User projectLead;

}
