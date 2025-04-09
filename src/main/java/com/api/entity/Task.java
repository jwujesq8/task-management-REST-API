package com.api.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import java.util.List;
import java.util.UUID;

/**
 * Class Task
 *
 * Entity class representing a task in the system.
 * A task has a title, description, status, priority, creator, executor, and associated comments.
 */
@Entity
@Table(name = "tasks")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Task {

    /**
     * The unique identifier of the task.
     * This is a UUID that uniquely identifies the task.
     */
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    /**
     * The title of the task.
     * This is a short description of what the task is about.
     */
    private String title;

    /**
     * The detailed description of the task.
     * This is a longer explanation of the task's requirements or context.
     */
    private String description;

    /**
     * The status of the task.
     * Possible statuses: "pending", "in progress", or "completed".
     */
    private String status;

    /**
     * The priority of the task.
     * Possible priorities: "high", "mid", or "low".
     */
    private String priority;

    /**
     * The creator of the task.
     * This is a reference to the user who created the task.
     *
     * @see User
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_creator", nullable = false)
    private User creator;

    /**
     * The executor of the task.
     * This is a reference to the user assigned to execute the task.
     *
     * @see User
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_executor", nullable = false)
    private User executor;

    /**
     * The comments associated with the task.
     * A task can have multiple comments, and each comment is linked to this task.
     *
     * @see Comment
     */
    @OneToMany(mappedBy = "task", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments;

}
