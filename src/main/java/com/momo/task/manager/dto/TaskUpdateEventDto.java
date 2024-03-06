package com.momo.task.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskUpdateEventDto implements Serializable {
    private Long taskId;
    private String eventType; // CommentAdded, AttachmentAdded, StatusChanged, etc.
    private String projectKey; // Project identifier
//    private List<User> adminList;

}
