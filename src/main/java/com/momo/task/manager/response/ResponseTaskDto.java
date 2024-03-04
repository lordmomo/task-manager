package com.momo.task.manager.response;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


@Data
@Builder
public class ResponseTaskDto implements Serializable {
    private String taskName;
    private String description;
    private String status;
    private String type;

    private Date startDate;
    private Date endDate;
    private String assigneeId;
    private String reporterId;
    private String stageId;

}
