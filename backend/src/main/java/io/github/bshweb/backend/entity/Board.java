package io.github.bshweb.backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "boards")
@NoArgsConstructor
@Getter
@Setter
public class Board {

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

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("position ASC")
    private List<Stage> stages = new ArrayList<>();

    /**
     * Custom helper method to keep both sides of the Board-Stage relationship in sync.
     *
     * @param stage stage to add
     */
    public void addStage(Stage stage) {
        if (stage == null) {
            throw new IllegalArgumentException("stage must not be null");
        }
        stages.add(stage);
        stage.setBoard(this);
    }

    /**
     * Custom helper method to keep both sides of the Board-Stage relationship in sync.
     *
     * @param stage stage to remove
     */
    public void removeStage(Stage stage) {
        if (stage == null) {
            return;
        }
        stages.remove(stage);
    }
}
