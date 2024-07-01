package com.springboot.blog.services;

import com.springboot.blog.entities.Post;
import com.springboot.blog.payloads.PostDTO;
import com.springboot.blog.payloads.PostResponse;

import java.util.List;

public interface PostService {

    public PostDTO createPost(PostDTO postDTO, Integer userId, Integer categoryId);

    public PostDTO updatePost(PostDTO postDTO,Integer postId);

    public void deletePost(Integer postId);

    public PostDTO getPostByPostId(Integer postId);

    public PostResponse getAllPosts(Integer pageNumber, Integer pageSize, String sortBy,String sortDir);

    public List<PostDTO> getAllPostsByUserId(Integer userId);

    public List<PostDTO> getAllPostsByCategoryId(Integer categoryId);

    public List<PostDTO> searchPosts(String key);

}
