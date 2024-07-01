package com.springboot.blog.security;


import com.springboot.blog.payloads.UserDTO;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class JwtResponse {
    private String token;
    private UserDTO user;
}
