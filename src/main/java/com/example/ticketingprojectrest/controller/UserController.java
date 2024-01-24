package com.example.ticketingprojectrest.controller;

import com.example.ticketingprojectrest.dto.UserDTO;
import com.example.ticketingprojectrest.entiy.ResponseWrapper;
import com.example.ticketingprojectrest.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<ResponseWrapper> getUsers(){
        List<UserDTO> userDTOSList = userService.listAllUsers();
        return ResponseEntity.ok(new ResponseWrapper("successfully retrieved",userDTOSList, HttpStatus.OK));
    }

    @GetMapping("/{username}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<ResponseWrapper> getUserByUsername(@PathVariable("username")String username){
        UserDTO userDTO = userService.findByUserName(username);
        return ResponseEntity.ok(new ResponseWrapper("successfully retrieved",userDTO,HttpStatus.OK));
    }

    @PostMapping(consumes = "application/json")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<ResponseWrapper> createUser(@RequestBody UserDTO userDTO){
        userService.save(userDTO);
       return ResponseEntity
               .status(HttpStatus.CREATED)
               .body(new ResponseWrapper("user created",HttpStatus.CREATED));
    }

    @PutMapping(consumes = "application/json")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<ResponseWrapper> updateUser(@RequestBody UserDTO userDTO){
        userService.update(userDTO);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseWrapper("user created",userDTO,HttpStatus.OK));
    }

    @DeleteMapping("/{username}")
    @PreAuthorize("hasRole('Admin')")
    public ResponseEntity<ResponseWrapper> deleteByUsername(@PathVariable("username")String username){
        userService.deleteByUserName(username);
        return ResponseEntity.ok(new ResponseWrapper("user is deleted",HttpStatus.OK));
    }

}
