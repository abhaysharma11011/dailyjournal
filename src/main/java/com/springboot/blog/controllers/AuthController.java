package com.springboot.blog.controllers;

import com.springboot.blog.entities.User;
import com.springboot.blog.exceptions.ResourceNotFoundException;
import com.springboot.blog.payloads.UserDTO;
import com.springboot.blog.security.CustomUserDetailsService;
import com.springboot.blog.security.JwtHelper;
import com.springboot.blog.security.JwtRequest;
import com.springboot.blog.security.JwtResponse;
import com.springboot.blog.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private JwtHelper helper;

    @Autowired
    private UserService userService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
        authenticateUser(request.getEmail(), request.getPassword());
        
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = helper.generateToken(userDetails);

        JwtResponse response = new JwtResponse();
        response.setToken(token);
        response.setUser(modelMapper.map((User) userDetails,UserDTO.class));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void authenticateUser(String email, String password) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
        try {
            manager.authenticate(authentication);
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(" Invalid Username or Password  !!");
        }
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity exceptionHandler() {
        return new ResponseEntity<>("Invalid Password", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity invalidUserName() {
        return new ResponseEntity<>("Looks like you haven't registered yet. Please Register now", HttpStatus.NOT_FOUND);
    }


    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO userDTO) {
        UserDTO newUser = userService.registerNewUser(userDTO);
        return new ResponseEntity<>(newUser,HttpStatus.CREATED);
    }

}
