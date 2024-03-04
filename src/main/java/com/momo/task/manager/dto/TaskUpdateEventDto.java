package com.momo.task.manager.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskUpdateEventDto implements Serializable {
    private String taskId;
    private String eventType; // CommentAdded, AttachmentAdded, StatusChanged, etc.
    private String projectId; // Project identifier
    private boolean isAdmin; // true for admins, false for regular users

}
