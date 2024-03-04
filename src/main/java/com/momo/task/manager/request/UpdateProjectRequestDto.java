package com.momo.task.manager.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProjectRequestDto {

    private String projectName;
    private String key;
    private Long projectLead;
}
