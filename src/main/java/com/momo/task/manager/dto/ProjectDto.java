package com.momo.task.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDto {

    private String projectName;
    private String projectKey;
    private String template;
    private String type;
    private Long projectLead;
}
