package com.momo.task.manager.controller;

import com.momo.task.manager.dto.CommentDto;
import com.momo.task.manager.dto.CommentValidation;
import com.momo.task.manager.dto.UpdateCommentDto;
import com.momo.task.manager.service.interfaces.CommentService;
import com.momo.task.manager.utils.ResourceEndpoints;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping(ResourceEndpoints.MAIN_COMMENT_KEY)
public class CommentController {

    @Autowired
    CommentService commentService;

    @PostMapping(ResourceEndpoints.CREATE_COMMENTS_ENDPOINT)
    public ResponseEntity<String> createComment(@PathVariable("projectId") Long projectId,
                                                @PathVariable("taskId") Long taskId,
                                                @ModelAttribute CommentDto commentDto) throws IOException {
        return commentService.createComment(projectId, taskId, commentDto);
    }


    @PostMapping(ResourceEndpoints.DELETE_COMMENTS_ENDPOINT)
    public ResponseEntity<String> deleteComment(@PathVariable("projectId") Long projectId,
                                                @PathVariable("taskId") Long taskId,
                                                @PathVariable("commentId") Long commentId,
                                                @RequestBody CommentValidation commentValidation) {
        return commentService.deleteComment(projectId, taskId, commentId, commentValidation);
    }

    @PutMapping(ResourceEndpoints.UPDATE_COMMENTS_ENDPOINT)
    public ResponseEntity<String> updateComment(@PathVariable("projectId") Long projectId,
                                                @PathVariable("taskId") Long taskId,
                                                @PathVariable("commentId") Long commentId,
                                                @ModelAttribute UpdateCommentDto updateCommentDto) throws IOException {
        return commentService.updateComment(projectId, taskId, commentId, updateCommentDto);
    }

    @GetMapping(ResourceEndpoints.VIEW_ALL_COMMENTS_ENDPOINT)
    public ResponseEntity<Object> listOfAllComments(@PathVariable("projectId") Long projectId, @PathVariable("taskId") Long taskId) {
        return commentService.listAllComments(projectId, taskId);
    }

}
