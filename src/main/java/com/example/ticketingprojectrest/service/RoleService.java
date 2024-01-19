package com.example.ticketingprojectrest.service;

import com.example.ticketingprojectrest.dto.RoleDTO;

import java.util.List;

public interface RoleService {
    List<RoleDTO> listAllRoles();
    RoleDTO findById(Long id);
}
