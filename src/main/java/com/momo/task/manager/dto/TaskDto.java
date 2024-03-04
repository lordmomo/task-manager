package com.momo.task.manager.dto;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskDto {

    private String taskName;
    private String description;
    private Long status;
    private String type;
    private List<String> label;
    private String projectKey;
    private Date startDate;
    private Date endDate;
    private Long assigneeId;
    private Long reporterId;

    @Nullable
    private MultipartFile file;

}
