package com.example.ticketingprojectrest.controller;

import com.example.ticketingprojectrest.annotation.ExecutionTime;
import com.example.ticketingprojectrest.dto.ProjectDTO;
import com.example.ticketingprojectrest.entiy.ResponseWrapper;
import com.example.ticketingprojectrest.exception.TicketingProjectException;
import com.example.ticketingprojectrest.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('Manager','Admin')")
    @ExecutionTime
    public ResponseEntity<ResponseWrapper> getAllProjects(){
        return ResponseEntity.ok(new ResponseWrapper("successfully retrieved",projectService.listAllProjects(), HttpStatus.OK));
    }

    @GetMapping("/{projectCode}")
    @PreAuthorize("hasRole('Manager')")
    public ResponseEntity<ResponseWrapper> getProjectByCode(@PathVariable("projectCode")String projectCode){
        return ResponseEntity
                .ok(new ResponseWrapper("successfully retrieved",projectService.getByProjectCode(projectCode),HttpStatus.OK));
    }

    @PostMapping(consumes = "application/json")
    @PreAuthorize("hasRole('Manager')")
    public ResponseEntity<ResponseWrapper> createProject(@RequestBody ProjectDTO projectDTO){
        projectService.save(projectDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseWrapper("project created",projectDTO,HttpStatus.CREATED));
    }

    @PutMapping(consumes = "application/json")
    @PreAuthorize("hasRole('Manager')")
    public ResponseEntity<ResponseWrapper> updateProject(@RequestBody ProjectDTO projectDTO){
        projectService.update(projectDTO);
        return ResponseEntity
                .ok(new ResponseWrapper("project updated",projectDTO,HttpStatus.OK));
    }

    @DeleteMapping("/{projectCode}")
    @PreAuthorize("hasRole('Manager')")
    public ResponseEntity<ResponseWrapper> deleteProject(@PathVariable("projectCode")String projectCode){
        projectService.delete(projectCode);
        return ResponseEntity
                .ok(new ResponseWrapper("project deleted",HttpStatus.OK));
    }

    @GetMapping("/manager/projectStatus")
    @PreAuthorize("hasRole('Manager')")
    public ResponseEntity<ResponseWrapper> getProjectByManager() throws TicketingProjectException {
        return ResponseEntity
                .ok(new ResponseWrapper("successfully retrieved",projectService.listAllProjectDetails(),HttpStatus.OK));
    }

    @PutMapping("/manager/complete/{projectCode}")
    @PreAuthorize("hasRole('Manager')")
    public ResponseEntity<ResponseWrapper> managerCompletedProject(@PathVariable("projectCode")String projectCode){
        projectService.complete(projectCode);
        return ResponseEntity.ok(new ResponseWrapper("successfully updated",HttpStatus.OK));
    }
}
