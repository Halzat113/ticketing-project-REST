package com.example.ticketingprojectrest.service;

import com.example.ticketingprojectrest.dto.UserDTO;
import com.example.ticketingprojectrest.exception.TicketingProjectException;

import java.util.List;

public interface UserService {
    List<UserDTO> listAllUsers();
    UserDTO findByUserName(String username) throws TicketingProjectException;
    void save(UserDTO dto);
    UserDTO update(UserDTO dto) throws TicketingProjectException;
    void deleteByUserName(String username);
    void delete(String username);
    List<UserDTO> listAllByRole(String role);

}
