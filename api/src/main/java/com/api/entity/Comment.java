package com.api.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

/**
 * Class Comment
 *
 * Entity class representing a comment in the system.
 * A comment is associated with a task and an author.
 */
@Entity
@Table(name = "task_comment")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class Comment {

    /**
     * The unique identifier of the comment.
     * This is a UUID that uniquely identifies the comment.
     */
    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    /**
     * The text content of the comment.
     * This is a description field that contains the comment's content.
     */
    @Column(name = "description")
    private String text;

    /**
     * The author of the comment.
     * This is a reference to the user who created the comment.
     *
     * @see User
     */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_author", nullable = false)
    private User author;

    /**
     * The task associated with the comment.
     * Each comment is linked to a specific task in the system.
     *
     * @see Task
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_task", nullable = false)
    private Task task;
}
