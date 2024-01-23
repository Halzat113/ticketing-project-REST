package com.example.ticketingprojectrest.controller;

import com.example.ticketingprojectrest.dto.TaskDTO;
import com.example.ticketingprojectrest.entiy.ResponseWrapper;
import com.example.ticketingprojectrest.enums.Status;
import com.example.ticketingprojectrest.service.TaskService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @PreAuthorize("hasRole('Manager')")
    public ResponseEntity<ResponseWrapper> getAllTasks(){
        return ResponseEntity.ok(new ResponseWrapper("successfully retrieved",taskService.listAllTasks(), HttpStatus.OK));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('Manager')")
    public ResponseEntity<ResponseWrapper> getTaskById(@PathVariable("id")Long id){
        return ResponseEntity.ok(new ResponseWrapper("successfully retrieved",taskService.findById(id),HttpStatus.OK));
    }

    @PostMapping(consumes = "application/json")
    @PreAuthorize("hasRole('Manager')")
    public ResponseEntity<ResponseWrapper> createTask(@RequestBody TaskDTO taskDTO){
        taskService.save(taskDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseWrapper("created",taskDTO,HttpStatus.CREATED));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('Manager')")
    public ResponseEntity<ResponseWrapper> deleteTask(@PathVariable("id")Long id){
        taskService.delete(id);
        return ResponseEntity.ok(new ResponseWrapper("Task is deleted",HttpStatus.OK));
    }

    @PutMapping(consumes = "application/json")
    @PreAuthorize("hasRole('Manager')")
    public ResponseEntity<ResponseWrapper> updateTask(@RequestBody TaskDTO taskDTO){
        taskService.update(taskDTO);
        return ResponseEntity
                .ok(new ResponseWrapper("task updated",taskDTO,HttpStatus.OK));
    }

    @GetMapping("/employee/pending-tasks")
    @PreAuthorize("hasRole('Employee')")
    public ResponseEntity<ResponseWrapper> employeePendingTasks(){
        return ResponseEntity.ok(new ResponseWrapper("successfully retrieved",taskService.listAllTasksByStatusIsNot(Status.COMPLETE),HttpStatus.OK))    ;
    }

    @PutMapping(value = "/employee/update",consumes = "application/json")
    @PreAuthorize("hasRole('Employee')")
    public ResponseEntity<ResponseWrapper> employeeUpdateInsert(@RequestBody TaskDTO taskDTO){
        taskService.updateStatus(taskDTO);
        return ResponseEntity.ok(new ResponseWrapper("status updated",HttpStatus.OK));
    }

    @GetMapping(value = "/employee/archive")
    @PreAuthorize("hasRole('Employee')")
    public ResponseEntity<ResponseWrapper> employeeArchive(){
        return ResponseEntity
                .ok(new ResponseWrapper("successfully retrieved",taskService.listAllTasksByStatus(Status.COMPLETE),HttpStatus.OK));
    }




}
