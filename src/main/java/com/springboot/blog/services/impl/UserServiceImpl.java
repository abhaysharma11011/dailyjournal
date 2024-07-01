package com.springboot.blog.services.impl;

import com.springboot.blog.config.AppConstants;
import com.springboot.blog.entities.Role;
import com.springboot.blog.entities.User;
import com.springboot.blog.exceptions.ResourceNotFoundException;
import com.springboot.blog.payloads.UserDTO;
import com.springboot.blog.respositories.RoleRepo;
import com.springboot.blog.respositories.UserRepo;
import com.springboot.blog.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepo roleRepo;


    public UserDTO registerNewUser(UserDTO userDTO){
        User user = modelMapper.map(userDTO,User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role memberRole = roleRepo.findById(AppConstants.ROLE_MEMBER_ID).get();
        user.getRoles().add(memberRole);

        User newUser = userRepo.save(user);
        return modelMapper.map(newUser,UserDTO.class);
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
         User user = userDTOToUser(userDTO);
         User savedUser = (User) userRepo.save(user);
         return userToUserDTO(savedUser);
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO, Integer userId) {
        User user = (User) userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","userId",userId));

        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setAbout(userDTO.getAbout());

        User updatedUser = (User) userRepo.save(user);
        return userToUserDTO(updatedUser);
    }

    @Override
    public UserDTO getUserById(Integer userId) {
        User user = (User) userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","userId",userId));
        return userToUserDTO(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepo.findAll();
        return users.stream().map(user -> userToUserDTO(user)).collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Integer userId) {
        User user = userRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("User"," userId ",userId));
        userRepo.delete(user);
    }


    private User userDTOToUser(UserDTO userDTO){
        User user = modelMapper.map(userDTO,User.class);
//        user.setId(userDTO.getId());
//        user.setName(userDTO.getName());
//        user.setEmail(userDTO.getEmail());
//        user.setPassword(userDTO.getPassword());
//        user.setAbout(userDTO.getAbout());
        return user;
    }

    private UserDTO userToUserDTO(User user){
        UserDTO userDTO = modelMapper.map(user,UserDTO.class);
//        userDTO.setId(user.getId());
//        userDTO.setName(user.getName());
//        userDTO.setEmail(user.getEmail());
//        userDTO.setPassword(user.getPassword());
//        userDTO.setAbout(user.getAbout());
        return userDTO;
    }
}
