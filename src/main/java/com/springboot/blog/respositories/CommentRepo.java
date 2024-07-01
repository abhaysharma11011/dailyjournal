package com.springboot.blog.respositories;

import com.springboot.blog.entities.Comment;
import com.springboot.blog.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepo extends JpaRepository<Comment,Integer> {


    @Query("select c from Comment c where c.post = :post")
    List<Comment> getCommentsByPostId(@Param("post")Post post);
}
