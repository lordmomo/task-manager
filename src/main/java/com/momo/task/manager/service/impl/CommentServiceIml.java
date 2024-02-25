package com.momo.task.manager.service.impl;

import com.momo.task.manager.dto.CommentDto;
import com.momo.task.manager.dto.CommentValidation;
import com.momo.task.manager.dto.UpdateCommentDto;
import com.momo.task.manager.model.Comment;
import com.momo.task.manager.model.Project;
import com.momo.task.manager.model.Task;
import com.momo.task.manager.model.User;
import com.momo.task.manager.repository.CommentRepository;
import com.momo.task.manager.service.interfaces.CommentService;
import com.momo.task.manager.utils.CheckUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class CommentServiceIml implements CommentService {

    @Autowired
    CheckUtils checkUtils;

    @Autowired
    CommentRepository commentRepository;


    @Override
    public void createComment(Long projectId, CommentDto commentDto) throws IOException {


        Project project = checkUtils.getProjectFromId(projectId);
        User user = checkUtils.getUserFromId(Long.valueOf(commentDto.getUserId()));

        if (project == null || user == null) {
            log.info("project or user is not found");
            return;
        }

        if (checkUtils.checkUserProjectAccess(user.getUserId(), projectId) != 1) {
            log.info("User has no access to project");
            return;
        }

        Comment comment = new Comment();
        Task task = checkUtils.checkTaskExists(Long.valueOf(commentDto.getTaskId()));
        comment.setTaskId(task);
        comment.setUserId(user);
        comment.setMessage(commentDto.getMessage());
        comment.setMessagePostDate(new Date());

        MultipartFile file = commentDto.getDocumentFile();

        if (file != null && !file.isEmpty()) {
            comment.setFileData(file.getBytes());

        } else {
            comment.setFileData(null);
        }
        commentRepository.save(comment);

    }

    @Override
    public void deleteComment(Long projectId, CommentValidation commentValidation) {

        Project project = checkUtils.getProjectFromId(projectId);
        User user = checkUtils.getUserFromId(commentValidation.getUserId());

        if (project == null || user == null) {
            log.info("project or user is not found");
            return;
        }

        if (checkUtils.checkUserProjectAccess(user.getUserId(), projectId) != 1) {
            log.info("User has no access to project");
            return;
        }

        // try to use custom method

        commentRepository.deleteById(commentValidation.getCommentId());

    }

    @Override
    public void updateComment(Long projectId, Long userId, Long commentId, UpdateCommentDto updateCommentDto) throws IOException {

        Project project = checkUtils.getProjectFromId(projectId);
        User user = checkUtils.getUserFromId(userId);

        if (project == null || user == null) {
            log.info("project or user is not found");
            return;
        }

        if (checkUtils.checkUserProjectAccess(user.getUserId(), projectId) != 1) {
            log.info("User has no access to project");
            return;
        }

        Optional<Comment> optComment = commentRepository.findById(commentId);
        if (optComment.isPresent()) {
            Comment comment = optComment.get();
            comment.setMessage(updateCommentDto.getMessage());
            MultipartFile file = updateCommentDto.getDocumentFile();

            if (file != null && !file.isEmpty()) {
                comment.setFileData(file.getBytes());

            } else {
                comment.setFileData(null);
            }
            commentRepository.save(comment);
        } else {
            log.info("Comment not found");
        }

    }

    @Override
    public List<Comment> listAllComments(Long projectId,Long taskId) {
        return commentRepository.findAllByCommentByTaskId(taskId);
    }


}
