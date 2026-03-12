package io.github.bshweb.backend.dto;

import java.util.UUID;

public record BoardDTO(
    UUID id,
    String title,
    String description
) {}