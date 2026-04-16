package io.github.bshweb.backend.dto.task;

import java.util.UUID;

//TODO Delete? We don't need separate get for task because board will be sent fully?
public record TaskDetailsResponse(
    UUID id,
    String title,
    String description
) {}
