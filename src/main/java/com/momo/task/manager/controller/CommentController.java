package com.momo.task.manager.controller;

import com.momo.task.manager.dto.CommentDto;
import com.momo.task.manager.dto.CommentValidation;
import com.momo.task.manager.dto.UpdateCommentDto;
import com.momo.task.manager.service.interfaces.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/cmt")
public class CommentController {

    @Autowired
    CommentService commentService;

    // add for no file upload
    @PostMapping("/projects/{projectId}/comment")
    private String createComment(@PathVariable("projectId") Long projectId,
                                 @ModelAttribute CommentDto commentDto) throws IOException {
        commentService.createComment(projectId,commentDto);
        return "comment added.";
    }


    @DeleteMapping("/projects/{projectId}/comment/delete")
    private String deleteComment(@PathVariable("projectId") Long projectId,@RequestBody CommentValidation commentValidation) {
        commentService.deleteComment(projectId,commentValidation);
        return "comment deleted.";
    }

    @PutMapping("/projects/{projectId}/comment/{commentId}/update/{userId}")
    private String updateComment(@PathVariable("projectId") Long projectId,
                                 @PathVariable("commentId")Long commentId,
                                 @PathVariable("userId") Long userId,
                                 @ModelAttribute UpdateCommentDto updateCommentDto) throws IOException {
        commentService.updateComment(projectId,userId,commentId,updateCommentDto);
        return "Update success";
    }
}
