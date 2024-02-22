package com.momo.task.manager.dto;

import jakarta.annotation.Nullable;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
public class TaskDto {

    private String taskName;

    private String description;

    private Long status;

    private String type;

    private String label;

    private Long project;

    private Date startDate;

    private Date endDate;

    private Long assigneeId;

    private Long reporterId;

    private Long stageId;

    @Nullable
    private MultipartFile file;

}
