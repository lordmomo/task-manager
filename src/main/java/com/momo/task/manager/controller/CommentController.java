package com.momo.task.manager.controller;

import com.momo.task.manager.dto.CommentDto;
import com.momo.task.manager.dto.CommentValidation;
import com.momo.task.manager.dto.UpdateCommentDto;
import com.momo.task.manager.service.interfaces.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/cmt")
public class CommentController {

    @Autowired
    CommentService commentService;

    @PostMapping("/projects/{projectId}/comment")
    public ResponseEntity<String> createComment(@PathVariable("projectId") Long projectId,
                                                @ModelAttribute CommentDto commentDto) throws IOException {
        return commentService.createComment(projectId,commentDto);
    }


    @DeleteMapping("/projects/{projectId}/comment/delete")
    public ResponseEntity<String> deleteComment(@PathVariable("projectId") Long projectId,@RequestBody CommentValidation commentValidation) {
        return commentService.deleteComment(projectId,commentValidation);
    }

    @PutMapping("/projects/{projectId}/comment/{commentId}/update/{userId}")
    public ResponseEntity<String> updateComment(@PathVariable("projectId") Long projectId,
                                 @PathVariable("commentId")Long commentId,
                                 @PathVariable("userId") Long userId,
                                 @ModelAttribute UpdateCommentDto updateCommentDto) throws IOException {
        return commentService.updateComment(projectId,userId,commentId,updateCommentDto);
    }

    @GetMapping("/projects/{projectId}/{taskId}/list-all-comments")
    public ResponseEntity<?> listOfAllComments(@PathVariable("projectId")Long projectId,@PathVariable("taskId")Long taskId){
        return commentService.listAllComments(projectId,taskId);
    }

}
