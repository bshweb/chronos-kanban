package io.github.bshweb.backend.dto.task;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record MoveTaskRequest(
    @NotNull
    UUID targetStageId,
    UUID prevTaskId,
    UUID nextTaskId
) {}
