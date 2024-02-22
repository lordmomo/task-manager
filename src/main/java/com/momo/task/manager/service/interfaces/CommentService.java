package com.momo.task.manager.service.interfaces;

import com.momo.task.manager.dto.CommentDto;
import com.momo.task.manager.dto.CommentValidation;
import com.momo.task.manager.dto.UpdateCommentDto;

import java.io.IOException;

public interface CommentService {
    void createComment(Long projectId, CommentDto commentDto) throws IOException;

    void deleteComment(Long projectId, CommentValidation commentValidation);

    void updateComment(Long projectId, Long userId,Long commentId,UpdateCommentDto updateCommentDto) throws IOException;
}
