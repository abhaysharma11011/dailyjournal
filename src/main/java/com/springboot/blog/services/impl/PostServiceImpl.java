package com.springboot.blog.services.impl;

import com.springboot.blog.entities.Category;
import com.springboot.blog.entities.Post;
import com.springboot.blog.entities.User;
import com.springboot.blog.exceptions.ResourceNotFoundException;
import com.springboot.blog.payloads.PostDTO;
import com.springboot.blog.payloads.PostResponse;
import com.springboot.blog.respositories.CategoryRepo;
import com.springboot.blog.respositories.PostRepo;
import com.springboot.blog.respositories.UserRepo;
import com.springboot.blog.services.PostService;
import io.micrometer.common.util.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
    private PostRepo postRepo;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public PostDTO createPost(PostDTO postDTO, Integer userId, Integer categoryId) {
        User user = userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","userId",userId));
        Category category = categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category","categoryId",categoryId));

        Post post = modelMapper.map(postDTO,Post.class);
        post.setImageName("default.png");
        post.setAddedDate(new Date().toString());
        post.setUser(user);
        post.setCategory(category);

        Post savedPost =  postRepo.save(post);
        return modelMapper.map(savedPost, PostDTO.class);
    }

    @Override
    public PostDTO updatePost(PostDTO postDTO, Integer postId) {
        Post post = postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","postId",postId));

        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        if(StringUtils.isNotBlank(postDTO.getImageName())){
            post.setImageName(postDTO.getImageName());
        }

        Post updatedPost = postRepo.save(post);
        return modelMapper.map(updatedPost,PostDTO.class);
    }

    @Override
    public void deletePost(Integer postId) {
        Post post = postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","postId",postId));
        postRepo.delete(post);
    }

    @Override
    public PostDTO getPostByPostId(Integer postId) {
        Post post = postRepo.findById(postId).orElseThrow(()->new ResourceNotFoundException("Post","postId",postId));
        return modelMapper.map(post,PostDTO.class);
    }

    @Override
    public PostResponse getAllPosts(Integer pageNumber, Integer pageSize, String sortBy,String sortDir) {
        //        List<Post> posts = postRepo.findAll();
        Pageable pageable = PageRequest.of(pageNumber,pageSize, "desc".equalsIgnoreCase(sortDir) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending());
        Page<Post> pagePost = postRepo.findAll(pageable);
        List<Post> allPosts = pagePost.getContent();
        List<PostDTO> postDTOs = allPosts.stream().map(post -> modelMapper.map(post,PostDTO.class)).collect(Collectors.toList());

        PostResponse postResponse = new PostResponse();
        postResponse.setContent(postDTOs);
        postResponse.setPageNumber(pagePost.getNumber());
        postResponse.setPageSize(pagePost.getSize());
        postResponse.setTotalElements(pagePost.getTotalElements());
        postResponse.setTotalPages(pagePost.getTotalPages());
        postResponse.setLastPage(pagePost.isLast());
        return postResponse;
    }

    @Override
    public List<PostDTO> getAllPostsByUserId(Integer userId) {
        User user = userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User", "userId", userId));
        List<Post> posts = postRepo.findByUser(user);
        return posts.stream().map(post -> modelMapper.map(post,PostDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<PostDTO> getAllPostsByCategoryId(Integer categoryId) {
        Category category = categoryRepo.findById(categoryId).orElseThrow(()->new ResourceNotFoundException("Category", "categoryId", categoryId));
        List<Post> posts = postRepo.findByCategory(category);
        return posts.stream().map(post -> modelMapper.map(post,PostDTO.class)).collect(Collectors.toList());
    }

    @Override
    public List<PostDTO> searchPosts(String key) {
        List<Post> posts = postRepo.searchByTitle(key);
        return posts.stream().map(post -> modelMapper.map(post,PostDTO.class)).collect(Collectors.toList());
    }
}
