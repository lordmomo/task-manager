package com.momo.task.manager.model;

import com.momo.task.manager.utils.ResourceInformation;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long projectId;

    @NotBlank(message = ResourceInformation.PROJECT_NAME_REQUIRED_MESSAGE)
    @Column(name = "project_name", nullable = false, unique = true)
    private String projectName;

    // distinguishly  identify project
    @NotBlank(message = ResourceInformation.PROJECT_KEY_REQUIRED_MESSAGE)
    @Column(name = "project_key", nullable = false, unique = true)
    private String projectKey;

    // kanban,scrum
    @NotBlank(message = ResourceInformation.PROJECT_TEMPLATE_REQUIRED_MESSAGE)
    @Column(name = "project_template", nullable = false)
    private String template;
    //team, company managed
    @NotBlank(message = ResourceInformation.PROJECT_TYPE_REQUIRED_MESSAGE)
    @Column(name = "project_type", nullable = false)
    private String type;

    @ManyToOne
    @JoinColumn(name = "project_lead", referencedColumnName = "user_id")
    private User projectLead;

    @Column(name = "active_flg")
    private boolean activeFlg;

    @Column(name = "updated_flg")
    private boolean updatedFlg;

    @PastOrPresent(message = ResourceInformation.UPDATE_DATE_BEAN_VALIDATION_MESSAGE)
    @Column(name = "updated_date")
    private LocalDateTime updatedDate;

    @PastOrPresent(message = ResourceInformation.START_DATE_BEAN_VALIDATION_MESSAGE)
    @Column(name = "start_date")
    private LocalDateTime startDate;

    @FutureOrPresent(message = ResourceInformation.END_DATE_BEAN_VALIDATION_MESSAGE)
    @Column(name = "end_date")
    private LocalDateTime endDate;

}
