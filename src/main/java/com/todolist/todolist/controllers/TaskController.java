package com.todolist.todolist.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.todolist.todolist.dto.TaskDto;
import com.todolist.todolist.models.Task;
import com.todolist.todolist.services.TaskService;

import jakarta.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;


@Controller
@RequestMapping("api/v1/task")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping("/")
    public ResponseEntity<?> creatTask(Authentication authentication, @Valid @RequestBody Task task) {

        TaskDto newTask = taskService.createTask(task, authentication.getName());

        return ResponseEntity.status(HttpStatus.OK).body(newTask);
    }

    @GetMapping("/tasks")
    public ResponseEntity<?> getAllTask(
            Authentication authentication,
            @RequestParam(value = "name", defaultValue = "") String name) {
        // Ensure authentication is not null
        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

        String usernameOrEmail = authentication.getName();

        try {
            if (name.isEmpty()) {
                // Fetch all tasks
                return ResponseEntity.ok(taskService.getAllTask(usernameOrEmail));
            } else {
                // Fetch tasks by name
                return ResponseEntity.ok(taskService.findTaskName(usernameOrEmail, name));
            }
        } catch (Exception e) {
            // Handle unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTask(Authentication authentication, @PathVariable Long id) {
        String usernameOrEmail = authentication.getName();
        Optional<Task> task = taskService.findTaskById(id, usernameOrEmail);
        return ResponseEntity.status(HttpStatus.OK).body(task);
    }

    @GetMapping("/completed")
    public ResponseEntity<?> getAllTaskCompleted(Authentication authentication) {
        String usernameOrEmail = authentication.getName();
        List<Task> tasks = taskService.findAllTaskCompleted(usernameOrEmail);
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    @GetMapping("/uncompleted")
    public ResponseEntity<?> getAllTaskUnCompleted(Authentication authentication) {
        String usernameOrEmail = authentication.getName();
        List<Task> tasks = taskService.findAllUnCompletedTask(usernameOrEmail);
        return ResponseEntity.status(HttpStatus.OK).body(tasks);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTask(Authentication authentication, @PathVariable Long id, @RequestBody Task task) {        
        String usernameOrEmail = authentication.getName();
        Optional<Task> taskUpdate = taskService.updateTask(id, task, usernameOrEmail);
        if(taskUpdate.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        }

        return ResponseEntity.status(HttpStatus.OK).body(taskUpdate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(Authentication authentication, @PathVariable Long id) {
        String usernameOrEmail = authentication.getName();
        boolean isDelete = taskService.deleteTask(id, usernameOrEmail);
        if(!isDelete)
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
        }

        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }

}
