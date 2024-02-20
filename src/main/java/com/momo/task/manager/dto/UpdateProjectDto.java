package com.momo.task.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProjectDto {

    private String projectName;

    private String key;

    private Long projectLead;
}
