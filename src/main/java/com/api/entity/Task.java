package com.api.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "tasks", schema = "task_management_system")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Task {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    private String title;
    private String description;
    private String status;
    private String priority;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_creator", nullable = false)
    private User creator;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_executor", nullable = false)
    private User executor;

}
