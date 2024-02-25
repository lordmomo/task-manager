package com.momo.task.manager.service.interfaces;

import com.momo.task.manager.dto.CommentDto;
import com.momo.task.manager.dto.CommentValidation;
import com.momo.task.manager.dto.UpdateCommentDto;
import com.momo.task.manager.model.Comment;

import java.io.IOException;
import java.util.List;

public interface CommentService {
    void createComment(Long projectId, CommentDto commentDto) throws IOException;

    void deleteComment(Long projectId, CommentValidation commentValidation);

    void updateComment(Long projectId, Long userId,Long commentId,UpdateCommentDto updateCommentDto) throws IOException;

    List<Comment> listAllComments(Long projectId,Long taskId);
}
