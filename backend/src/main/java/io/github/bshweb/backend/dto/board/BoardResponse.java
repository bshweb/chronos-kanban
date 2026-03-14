package io.github.bshweb.backend.dto.board;

import java.util.UUID;

public record BoardResponse(
    UUID id,
    String title,
    String description
) {}