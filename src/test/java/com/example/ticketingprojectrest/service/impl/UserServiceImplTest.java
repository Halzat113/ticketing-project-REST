package com.example.ticketingprojectrest.service.impl;

import com.example.ticketingprojectrest.dto.RoleDTO;
import com.example.ticketingprojectrest.dto.UserDTO;
import com.example.ticketingprojectrest.entiy.Role;
import com.example.ticketingprojectrest.entiy.User;
import com.example.ticketingprojectrest.exception.TicketingProjectException;
import com.example.ticketingprojectrest.repository.ProjectRepository;
import com.example.ticketingprojectrest.repository.UserRepository;
import com.example.ticketingprojectrest.service.KeycloakService;
import com.example.ticketingprojectrest.service.ProjectService;
import com.example.ticketingprojectrest.service.TaskService;
import com.example.ticketingprojectrest.util.MapperUtil;
import static org.assertj.core.api.AssertionsForInterfaceTypes.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProjectService projectService;

    @Mock
    ProjectRepository projectRepository;

    @Mock
    private TaskService taskService;

    @Mock
    private KeycloakService keycloakService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Spy
    private MapperUtil mapperUtil = new MapperUtil(new ModelMapper());

    User user;
    UserDTO userDTO;

    @BeforeEach
    void setup(){
        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setUserName("user");
        user.setPassWord("Abc1");
        user.setEnabled(true);
        user.setRole(new Role("Manager"));

        userDTO = new UserDTO();
        userDTO.setId(1L);
        userDTO.setFirstName("John");
        userDTO.setLastName("Doe");
        userDTO.setUserName("user");
        userDTO.setPassWord("Abc1");
        userDTO.setEnabled(true);

        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setDescription("Manager");

        userDTO.setRole(roleDTO);

    }

    private List<User> getUsers(){
        User user2 = new User();
        user2.setId(2L);
        user2.setFirstName("Emily");
        user2.setUserName("clark");
        return List.of(user,user2);
    }

    private List<UserDTO> getUserDTOs(){
        UserDTO userDTO2 = new UserDTO();
        userDTO2.setId(2L);
        userDTO2.setFirstName("Emily");
        userDTO2.setUserName("clark");
        return List.of(userDTO,userDTO2);
    }

    @Test
    void should_list_all_users(){
        //stub
        //given
        when(userRepository.findAll(Sort.by("firstName"))).thenReturn(getUsers());

        List<UserDTO> expectedUserDTOs = getUserDTOs();
        List<UserDTO> actualUserDTOs = userService.listAllUsers();

        assertThat(actualUserDTOs).usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedUserDTOs);
    }

    @Test
    void find_user_by_username_exception_test()  {
        List<String> usernames = getUsers().stream().map(User::getUserName).toList();
        when(userRepository.findALlUsernames()).thenReturn(usernames);

        for (int i = 0; i < getUsers().size(); i++) {
            lenient().when(userRepository.findByUserName(usernames.get(i))).thenReturn(getUsers().get(i));
        }

        Throwable exception = assertThrows(TicketingProjectException.class,()->userService.findByUserName(""));
        assertEquals("User not found",exception.getMessage());
    }

    @Test
    void should_find_user_by_username()  {
        List<String> usernames = getUsers().stream().map(User::getUserName).toList();

        when(userRepository.findALlUsernames()).thenReturn(usernames);

        for (int i = 0; i < getUsers().size(); i++) {
           lenient().when(userRepository.findByUserName(usernames.get(i))).thenReturn(getUsers().get(i));
        }
        assertThat(userService.findByUserName("user")).usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(userDTO);
    }

    @Test
    void should_save_user(){
        when(passwordEncoder.encode(anyString())).thenReturn("abcd");
        when(userRepository.save(any())).thenReturn(user);

        UserDTO actualDTO = userService.save(userDTO);

        verify(keycloakService).userCreate(any());

        assertThat(actualDTO).usingRecursiveComparison().isEqualTo(userDTO);
    }

    @Test
    void should_delete_user(){
        userRepository.delete(user);

        verify(userRepository).delete(user);
    }



}