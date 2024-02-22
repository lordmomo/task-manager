package com.momo.task.manager.dto;

import lombok.Data;

@Data
public class CommentValidation {
    Long taskId;
    Long userId;
    Long commentId;
}
