package com.example.ticketingprojectrest.service;

import com.example.ticketingprojectrest.dto.ProjectDTO;
import com.example.ticketingprojectrest.dto.UserDTO;
import com.example.ticketingprojectrest.entiy.User;

import java.util.List;

public interface ProjectService {
    ProjectDTO getByProjectCode(String code);
    List<ProjectDTO> listAllProjects();
    void save(ProjectDTO dto);
    void update(ProjectDTO dto);
    void delete(String code);
    void complete(String projectCode);

    List<ProjectDTO> listAllProjectDetails();

    List<ProjectDTO> readAllByAssignedManager(UserDTO assignedManager);
}
