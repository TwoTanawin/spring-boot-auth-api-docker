package com.todolist.todolist.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.todolist.todolist.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.todolist.todolist.dto.TaskDto;
import com.todolist.todolist.models.Task;
import com.todolist.todolist.repository.TaskRepository;
import com.todolist.todolist.repository.UserRepository;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Task> getAllTask(String usernameOrEmail) {
        return taskRepository.findByOwner_UsernameOrOwner_Email(usernameOrEmail, usernameOrEmail);
    }

    public List<Task> findTaskName(String name, String usernameOrEmail) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Task name must not be null or empty");
        }
        if (usernameOrEmail == null || usernameOrEmail.isEmpty()) {
            throw new IllegalArgumentException("Username or email must not be null or empty");
        }
        return taskRepository.findByOwner_UsernameOrOwner_EmailAndNameIsContaining(usernameOrEmail, usernameOrEmail, name);
    }

    public Optional<Task> findTaskById(Long id, String usernameOrEmail) {
        return taskRepository.findById(id).filter(task ->
            Objects.equals(task.getOwner().getName(), usernameOrEmail) ||
            Objects.equals(task.getOwner().getEmail(), usernameOrEmail));
    }

    public List<Task> findAllTaskCompleted(String usernameOrEmail) {
        return taskRepository.findByOwner_UsernameOrOwner_EmailAndCompletedTrue(usernameOrEmail, usernameOrEmail);
    }

    public List<Task> findAllUnCompletedTask(String usernameOrEmail) {
        return taskRepository.findByOwner_UsernameOrOwner_EmailAndCompletedFalse(usernameOrEmail, usernameOrEmail);
    }

    public TaskDto createTask(Task task, String usernameOrEmail) {
        Optional<User> user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User not found for username or email: " + usernameOrEmail);
        }
        task.setOwner(user.get());

        Task taskSaved = taskRepository.save(task);

        TaskDto taskDto = new TaskDto();
        taskDto.setOwner_id(taskSaved.getOwner().getId());
        taskDto.setName(taskSaved.getName());
        taskDto.setDescription(taskSaved.getDescription());
        taskDto.setCompleted(taskSaved.isCompleted()); // Correct method call

        return taskDto;
    }

    public Optional<Task> updateTask(Long id, Task task, String usernameOrEmail) {
        Optional<Task> getTask = taskRepository.findById(id).filter(findTask ->
            Objects.equals(findTask.getOwner().getName(), usernameOrEmail) ||
            Objects.equals(findTask.getOwner().getEmail(), usernameOrEmail)
        );
    
        if (getTask.isEmpty()) {
            return getTask;
        }
    
        if (task.getName() != null) {
            getTask.get().setName(task.getName());
        }
    
        if (task.getDescription() != null) {
            getTask.get().setDescription(task.getDescription());
        }
    
        // Fix: Check if the `completed` field is being updated
        getTask.get().setCompleted(task.isCompleted());
    
        return Optional.of(taskRepository.save(getTask.get()));
    }

    public boolean deleteTask(Long id, String usernameOrEmail){
        Optional<Task> getTask = taskRepository.findById(id).filter(findtask ->
                Objects.equals(findtask.getOwner().getName(), usernameOrEmail) ||
                Objects.equals(findtask.getOwner().getEmail(), usernameOrEmail));

        if(getTask.isEmpty())
        {
            return false;
        }
        getTask.get().setOwner(null);
        taskRepository.save(getTask.get());
        taskRepository.deleteById(id);
        return true;
    }
    
}

