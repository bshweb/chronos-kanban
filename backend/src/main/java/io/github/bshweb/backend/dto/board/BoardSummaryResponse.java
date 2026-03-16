package io.github.bshweb.backend.dto.board;

import java.util.UUID;

public record BoardSummaryResponse(
    UUID id,
    String title,
    String description
) {}