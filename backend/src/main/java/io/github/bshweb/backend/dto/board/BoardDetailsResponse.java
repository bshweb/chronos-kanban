package io.github.bshweb.backend.dto.board;

import io.github.bshweb.backend.dto.stage.StageDetailsResponse;

import java.util.List;
import java.util.UUID;

public record BoardDetailsResponse(
    UUID id,
    String title,
    String description,
    List<StageDetailsResponse> stages
) {}
