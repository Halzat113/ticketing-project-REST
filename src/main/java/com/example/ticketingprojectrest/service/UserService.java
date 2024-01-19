package com.example.ticketingprojectrest.service;

import com.example.ticketingprojectrest.dto.UserDTO;

import java.util.List;

public interface UserService {
    List<UserDTO> listAllUsers();
    UserDTO findByUserName(String username);
    void save(UserDTO dto);
    UserDTO update(UserDTO dto);
    void deleteByUserName(String username);
    void delete(String username);
    List<UserDTO> listAllByRole(String role);

}
