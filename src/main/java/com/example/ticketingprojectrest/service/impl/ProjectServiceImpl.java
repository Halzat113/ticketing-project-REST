package com.example.ticketingprojectrest.service.impl;

import com.example.ticketingprojectrest.dto.ProjectDTO;
import com.example.ticketingprojectrest.dto.UserDTO;
import com.example.ticketingprojectrest.entiy.Project;
import com.example.ticketingprojectrest.entiy.User;
import com.example.ticketingprojectrest.enums.Status;
import com.example.ticketingprojectrest.exception.TicketingProjectException;
import com.example.ticketingprojectrest.mapper.MapperUtil;
import com.example.ticketingprojectrest.repository.ProjectRepository;
import com.example.ticketingprojectrest.service.ProjectService;
import com.example.ticketingprojectrest.service.TaskService;
import com.example.ticketingprojectrest.service.UserService;
import org.keycloak.adapters.springsecurity.account.SimpleKeycloakAccount;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.stream.Collectors;
@Service
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final MapperUtil mapperUtil;
    private final UserService userService;
    private final TaskService taskService;

    public ProjectServiceImpl(ProjectRepository projectRepository, MapperUtil mapperUtil, @Lazy UserService userService, TaskService taskService) {
        this.projectRepository = projectRepository;
        this.mapperUtil = mapperUtil;
        this.userService = userService;
        this.taskService = taskService;
    }

    @Override
    public ProjectDTO getByProjectCode(String code) {
        return mapperUtil.convert(projectRepository.findByProjectCode(code),new ProjectDTO());
    }

    @Override
    public List<ProjectDTO> listAllProjects() {
        return projectRepository.findAll().stream().map(project->mapperUtil.convert(project,new ProjectDTO())).collect(Collectors.toList());
    }

    @Override
    public void save(ProjectDTO dto) {
        dto.setProjectStatus(Status.OPEN);
        Project project =mapperUtil.convert(dto,new Project());
        projectRepository.save(project);
    }

    @Override
    public void update(ProjectDTO dto) {
        Project project = projectRepository.findByProjectCode(dto.getProjectCode());
        Project convertedProject = mapperUtil.convert(dto,new Project());
        convertedProject.setId(project.getId());
        convertedProject.setProjectStatus(project.getProjectStatus());

        projectRepository.save(convertedProject);

    }

    @Override
    public void delete(String code) {
        Project project = projectRepository.findByProjectCode(code);

        project.setIsDeleted(true);
        project.setProjectCode(project.getProjectCode() + "-" + project.getId());

        projectRepository.save(project);

        taskService.deleteByProject(mapperUtil.convert(project,new ProjectDTO()));
    }

    @Override
    public void complete(String projectCode) {

        Project project = projectRepository.findByProjectCode(projectCode);
        project.setProjectStatus(Status.COMPLETE);

        projectRepository.save(project);

        taskService.completeByProject(mapperUtil.convert(project,new ProjectDTO()));
    }

    @Override
    public List<ProjectDTO> listAllProjectDetails() throws TicketingProjectException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        SimpleKeycloakAccount details = (SimpleKeycloakAccount) authentication.getDetails();
        String username = details.getKeycloakSecurityContext().getToken().getPreferredUsername();

        UserDTO currentUserDTO = userService.findByUserName(username);

        User user = mapperUtil.convert(currentUserDTO,new User());

        List<Project> list = projectRepository.findAllByAssignedManager(user);

        return list.stream().map(project -> {

            ProjectDTO obj = mapperUtil.convert(project,new ProjectDTO());

            obj.setUnfinishedTaskCounts(taskService.totalNonCompletedTask(project.getProjectCode()));
            obj.setCompleteTaskCounts(taskService.totalCompletedTask(project.getProjectCode()));


            return obj;



        }).collect(Collectors.toList());
    }

    @Override
    public List<ProjectDTO> readAllByAssignedManager(UserDTO assignedManager) {
        List<Project> list = projectRepository.findAllByAssignedManager(mapperUtil.convert(assignedManager,new User()));
        return list.stream().map(project -> mapperUtil.convert(project,new ProjectDTO())).collect(Collectors.toList());
    }
}
