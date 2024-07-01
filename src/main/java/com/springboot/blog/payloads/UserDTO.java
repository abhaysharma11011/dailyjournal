package com.springboot.blog.payloads;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
public class UserDTO {

    private int id;

    @NotBlank
    @Size(min = 3, message =  "Username must be min of 3 characters !!")
    private String name;

    @Email(message =  "Email address is not valid !!")
    private String email;

    @NotBlank
    @Size(min = 3, max = 20, message = "Password must be min of 3 chars and max of 10 chars !!")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @NotBlank
    private String about;

    @NotBlank
    private Set<RoleDTO> roles = new HashSet<>();
}
