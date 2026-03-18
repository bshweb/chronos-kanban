package io.github.bshweb.backend.dto.stage;

import java.util.UUID;

public record MoveStageRequest(
    UUID prevStageId,
    UUID nextStageId
) {}
