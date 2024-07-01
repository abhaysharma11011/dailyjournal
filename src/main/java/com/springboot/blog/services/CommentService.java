package com.springboot.blog.services;

import com.springboot.blog.payloads.CommentDTO;

import java.util.List;

public interface CommentService {

    CommentDTO createComment(CommentDTO commentDTO , Integer postId/*, Integer userId*/);

    void deleteComment(Integer commentId);

    public List<CommentDTO> getCommentsByPostId(Integer postId);
    }
