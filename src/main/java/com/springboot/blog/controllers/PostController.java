package com.springboot.blog.controllers;

import com.springboot.blog.config.AppConstants;
import com.springboot.blog.payloads.ApiResponse;
import com.springboot.blog.payloads.PostDTO;
import com.springboot.blog.payloads.PostResponse;
import com.springboot.blog.services.FileService;
import com.springboot.blog.services.PostService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;

    //create Post
    @PostMapping("/user/{userId}/category/{categoryId}/post")
    public ResponseEntity<PostDTO> createPost(
            @RequestBody PostDTO postDTO,
            @PathVariable Integer userId,
            @PathVariable Integer categoryId){
        PostDTO createdPostDTO = postService.createPost(postDTO,userId,categoryId);
        return new ResponseEntity<>(createdPostDTO, HttpStatus.CREATED);
    }


    //Get Post by userId
    @GetMapping("/user/{userId}/posts")
    public ResponseEntity<List<PostDTO>> getPostsByUserId(@PathVariable Integer userId){
        List<PostDTO> postDTOs = postService.getAllPostsByUserId(userId);
        return new ResponseEntity<>(postDTOs, HttpStatus.OK);
    }

    //Get Post by categoryId
    @GetMapping("/category/{categoryId}/posts")
    public ResponseEntity<List<PostDTO>> getPostsByCategoryId(@PathVariable Integer categoryId){
        List<PostDTO> postDTOs = postService.getAllPostsByCategoryId(categoryId);
        return new ResponseEntity<>(postDTOs, HttpStatus.OK);
    }

    //Get All Posts
    @GetMapping("/posts")
    public ResponseEntity<PostResponse> getAllPosts(
            @RequestParam (value="pageNumber",defaultValue= AppConstants.PAGE_NUMBER,required=false) Integer pageNumber,
            @RequestParam (value="pageSize",defaultValue=AppConstants.PAGE_SIZE,required=false)  Integer pageSize,
            @RequestParam (value="sortBy",defaultValue=AppConstants.SORT_BY,required=false) String sortBy,
            @RequestParam (value="sortDir",defaultValue=AppConstants.SORT_DIR,required=false) String sortDir){
        PostResponse postResponse = postService.getAllPosts(pageNumber,pageSize,sortBy,sortDir);
        return new ResponseEntity<>(postResponse, HttpStatus.OK);
    }

    //Get Post By PostId
    @GetMapping("/post/{postId}")
    public ResponseEntity<PostDTO> getAllPosts(@PathVariable Integer postId){
        PostDTO postDTOs = postService.getPostByPostId(postId);
        return new ResponseEntity<>(postDTOs, HttpStatus.OK);
    }

    //Delete Post By PostId
    @DeleteMapping("/post/{postId}")
    public ApiResponse deletePost(@PathVariable Integer postId){
        postService.deletePost(postId);
        return new ApiResponse( HttpStatus.OK.toString(),"Post Deleted SuccessFully");
    }

    //Update Post By PostId
    @PutMapping("/post/{postId}")
    public ResponseEntity<PostDTO> updatePost(@RequestBody PostDTO postDTO, @PathVariable Integer postId){
        PostDTO updatePostDTO = postService.updatePost(postDTO,postId);
        return new ResponseEntity<>(updatePostDTO, HttpStatus.OK);
    }

    //Search Post By Title
    @GetMapping("/post/search/{key}")
    public ResponseEntity<List<PostDTO>> updatePost(@PathVariable String key){
        List<PostDTO> postDTOs = postService.searchPosts(key);
        return new ResponseEntity<>(postDTOs, HttpStatus.OK);
    }

    //Upload Image
    @PostMapping("/post/image/upload/{postId}")
    public ResponseEntity<PostDTO> uploadImage(@RequestParam("image") MultipartFile image, @PathVariable Integer postId) throws IOException {
        PostDTO postDTO = postService.getPostByPostId(postId);

        String fileName = fileService.uploadImage(path,image);
        postDTO.setImageName(fileName);

        PostDTO updatedPostDTO  = postService.updatePost(postDTO,postId);
        return new ResponseEntity<>(updatedPostDTO,HttpStatus.OK);
    }

    //Download Image
    @GetMapping(value = "/post/image/{imageName}",produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadImage(@PathVariable String imageName, HttpServletResponse response) throws IOException {
        InputStream resource  = fileService.getResource(path,imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());
    }
}
