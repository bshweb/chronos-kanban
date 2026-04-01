package io.github.bshweb.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "tasks",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_task_stage_position", columnNames = {"stage_id", "position"})
    })
@NoArgsConstructor
@Getter
@Setter
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    @Size(max = 100)
    @Column(length = 100, nullable = false)
    private String title;

    @Size(max = 5000)
    @Column(length = 5000)
    private String description;

    @NotNull
    @Positive
    @Column(nullable = false)   // TODO check(position > 0) on db-level
    private Long position;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "stage_id", nullable = false)
    private Stage stage;
}
