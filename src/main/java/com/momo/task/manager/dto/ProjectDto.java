package com.momo.task.manager.dto;

import com.momo.task.manager.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {

    private Long projectId;

    private String projectName;

    private String key;

    private String template;

    private String type;

    private Long projectLead;
}
