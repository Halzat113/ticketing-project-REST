package com.example.ticketingprojectrest.service.impl;


import com.example.ticketingprojectrest.dto.ProjectDTO;
import com.example.ticketingprojectrest.dto.TaskDTO;
import com.example.ticketingprojectrest.dto.UserDTO;
import com.example.ticketingprojectrest.entiy.User;
import com.example.ticketingprojectrest.mapper.MapperUtil;
import com.example.ticketingprojectrest.repository.UserRepository;
import com.example.ticketingprojectrest.service.ProjectService;
import com.example.ticketingprojectrest.service.TaskService;
import com.example.ticketingprojectrest.service.UserService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final MapperUtil mapperUtil;
    private final ProjectService projectService;
    private final TaskService taskService;

    public UserServiceImpl(UserRepository userRepository, MapperUtil mapperUtil, ProjectService projectService, TaskService taskService) {
        this.userRepository = userRepository;
        this.mapperUtil = mapperUtil;
        this.projectService = projectService;
        this.taskService = taskService;
    }

    @Override
    public List<UserDTO> listAllUsers() {

        List<User> userList = userRepository.findAll(Sort.by("firstName"));
        return userList.stream().map(user -> mapperUtil.convert(user,new UserDTO())).collect(Collectors.toList());

    }

    @Override
    public UserDTO findByUserName(String username) {
        User user = userRepository.findByUserName(username);
        return mapperUtil.convert(user,new UserDTO());
    }

    @Override
    public void save(UserDTO dto) {

        dto.setEnabled(true);

        User obj = mapperUtil.convert(dto,new User());

        userRepository.save(obj);

    }

    @Override
    public UserDTO update(UserDTO dto) {

       //Find current user
        User user = userRepository.findByUserName(dto.getUserName());
        //Map updated user dto to entity object
        User convertedUser = mapperUtil.convert(dto,new User());
        //set id to converted object
        convertedUser.setId(user.getId());
        //save updated user
        userRepository.save(convertedUser);

        return findByUserName(dto.getUserName());
    }

    @Override
    public void deleteByUserName(String username) {
        userRepository.deleteByUserName(username);

    }

    @Override
    public void delete(String username) {
        User user = userRepository.findByUserName(username);

        if (checkIfUserCanBeDeleted(user)) {
            user.setIsDeleted(true);
            user.setUserName(user.getUserName() + "-" + user.getId());
            userRepository.save(user);
        }

    }

    private boolean checkIfUserCanBeDeleted(User user) {

        switch (user.getRole().getDescription()) {
            case "Manager" -> {
                List<ProjectDTO> projectDTOList = projectService.readAllByAssignedManager(mapperUtil.convert(user,new UserDTO()));
                return projectDTOList.size() == 0;
            }
            case "Employee" -> {
                List<TaskDTO> taskDTOList = taskService.readAllByAssignedEmployee(user);
                return taskDTOList.size() == 0;
            }
            default -> {
                return true;
            }
        }

    }

    @Override
    public List<UserDTO> listAllByRole(String role) {

        List<User> users = userRepository.findAllByRoleDescriptionIgnoreCase(role);

        return users.stream().map(user -> mapperUtil.convert(user,new UserDTO())).collect(Collectors.toList());
    }
}