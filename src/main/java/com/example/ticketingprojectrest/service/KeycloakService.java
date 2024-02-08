package com.example.ticketingprojectrest.service;

import com.example.ticketingprojectrest.dto.UserDTO;

import javax.ws.rs.core.Response;


public interface KeycloakService {
    Response userCreate(UserDTO userDTO);
    void delete (String username);
}
