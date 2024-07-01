package com.springboot.blog.controllers;

import com.springboot.blog.payloads.ApiResponse;
import com.springboot.blog.payloads.CommentDTO;
import com.springboot.blog.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/post/{postId}/comment")
    public ResponseEntity<CommentDTO> createComment(@RequestBody CommentDTO commentDTO, @PathVariable Integer postId /*, @PathVariable Integer userId*/){
        CommentDTO createdComment = commentService.createComment(commentDTO,postId/*,userId*/);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }

    @DeleteMapping("comment/delete/{commentId}")
    public ResponseEntity<ApiResponse> deleteComment(@PathVariable Integer commentId){
        commentService.deleteComment(commentId);
        ApiResponse response = new ApiResponse("200", "Comment Deleted Successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/post/{postId}/comments")
    public ResponseEntity<List<CommentDTO>> getCommentsByPostId(@PathVariable Integer postId){
        List<CommentDTO> commentDTOs = commentService.getCommentsByPostId(postId);
        return new ResponseEntity<>(commentDTOs, HttpStatus.OK);
    }
}
