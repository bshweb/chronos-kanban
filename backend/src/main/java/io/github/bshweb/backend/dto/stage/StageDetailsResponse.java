package io.github.bshweb.backend.dto.stage;

import io.github.bshweb.backend.dto.task.TaskDetailsResponse;

import java.util.List;
import java.util.UUID;

public record StageDetailsResponse(
    UUID id,
    String title,
    Long position,
    List<TaskDetailsResponse> tasks
) {}
