package com.springboot.blog.payloads;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class PostDTO {
    private Integer postId;

    private String title;

    private String content;

    private String imageName;

    private String addedDate;

    private CategoryDTO category;

    private UserDTO user;

    private Set<CommentDTO> comments;
}
