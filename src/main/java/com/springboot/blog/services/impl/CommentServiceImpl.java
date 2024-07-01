package com.springboot.blog.services.impl;

import com.springboot.blog.entities.Comment;
import com.springboot.blog.entities.Post;
import com.springboot.blog.entities.User;
import com.springboot.blog.exceptions.ResourceNotFoundException;
import com.springboot.blog.payloads.CommentDTO;
import com.springboot.blog.respositories.CommentRepo;
import com.springboot.blog.respositories.PostRepo;
import com.springboot.blog.respositories.UserRepo;
import com.springboot.blog.services.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CommentDTO createComment(CommentDTO commentDTO, Integer postId/*, Integer userId*/) {
        Post post  = postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","postId",postId));
//        User user = userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","userId",userId));
        Comment comment  = modelMapper.map(commentDTO,Comment.class);
        comment.setPost(post);
//        comment.setUser(user);
        Comment savedComment = commentRepo.save(comment);
        return modelMapper.map(savedComment,CommentDTO.class);
    }

    @Override
    public void deleteComment(Integer commentId) {
        Comment comment = commentRepo.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment", "commentId", commentId));
        commentRepo.delete(comment);
    }

    @Override
    public List<CommentDTO> getCommentsByPostId(Integer postId) {
        Post post = postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","postId",postId));
        List<Comment> allComments = commentRepo.getCommentsByPostId(post);
        List<CommentDTO> allCommentsDTOs = allComments.stream().map(comment -> modelMapper.map(comment,CommentDTO.class)).collect(Collectors.toList());
        return allCommentsDTOs;
    }
}
