package com.api.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tasks")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
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

    @OneToMany(mappedBy = "task", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments;

}
