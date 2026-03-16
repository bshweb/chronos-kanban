package io.github.bshweb.backend.dto.task;

import java.util.UUID;

public record TaskDetailsResponse(
    UUID id,
    String title,
    String description,
    Long position
) {}
