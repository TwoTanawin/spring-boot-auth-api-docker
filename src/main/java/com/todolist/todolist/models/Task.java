package com.todolist.todolist.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id")
    private User owner;
    
    @NotNull(message = "name is mandatory")
    private String name;

    @NotNull(message = "description is mandatory")
    @Column(name = "description")
    private String description;

    @NotNull(message = "completed is mandatory")
    @Column(name = "completed") // Map the field to the database column
    private boolean completed;
}
