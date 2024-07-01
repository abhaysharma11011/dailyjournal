package com.springboot.blog.services;
import com.springboot.blog.payloads.UserDTO;

import java.util.List;

public interface UserService {

    UserDTO registerNewUser(UserDTO userDTO);
    UserDTO createUser(UserDTO user);
    UserDTO updateUser(UserDTO user, Integer userId) throws Throwable;
    UserDTO getUserById(Integer userId) throws Throwable;
    List<UserDTO> getAllUsers();
    void deleteUser(Integer userId) throws Throwable;

}
