package com.todolist.todolist.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.todolist.todolist.models.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {
    public List<Task> findAll();

    public List<Task> findByOwner_UsernameOrOwner_Email(String username, String email);

    public List<Task> findByOwner_UsernameOrOwner_EmailAndNameIsContaining(String username,String email, String name); 
    
    public List<Task> findByOwner_UsernameOrOwner_EmailAndCompletedTrue(String username,String email);

    public List<Task> findByOwner_UsernameOrOwner_EmailAndCompletedFalse(String username,String email); 
}
