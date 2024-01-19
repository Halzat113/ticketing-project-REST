package com.example.ticketingprojectrest.repository;

import com.example.ticketingprojectrest.entiy.Project;
import com.example.ticketingprojectrest.entiy.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project,String> {
    Project findByProjectCode(String code);
    List<Project> findAllByAssignedManager(User manager);
}
