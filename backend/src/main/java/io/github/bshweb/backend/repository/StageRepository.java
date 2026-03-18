package io.github.bshweb.backend.repository;

import io.github.bshweb.backend.entity.Stage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StageRepository extends JpaRepository<Stage, UUID> {

    List<Stage> findAllByBoardIdOrderByPositionAsc(UUID boardId);

    Optional<Stage> findByIdAndBoardId(UUID stageId, UUID boardId);

    Optional<Stage> findTopByBoardIdOrderByPositionDesc(UUID boardId);
}
