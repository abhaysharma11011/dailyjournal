package com.springboot.blog.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="Comment")
@NoArgsConstructor
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String content;

//    @ManyToOne
//    @JoinColumn(name="user_id")
//    private User user;

    @ManyToOne
    @JoinColumn(name="post_id")
    private Post post;
}
