package io.github.bshweb.backend.repository;

import io.github.bshweb.backend.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

    Optional<Task> findByIdAndStageBoardId(UUID id, UUID boardId);

    Optional<Task> findByIdAndStageId(UUID id, UUID stageId);

    List<Task> findAllByStageIdOrderByPositionAsc(UUID stageId);
}
