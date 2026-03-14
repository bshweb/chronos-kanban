package io.github.bshweb.backend.dto.board;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateBoardRequest(
    @NotBlank
    @Size(max = 100)
    String title,

    @Size(max = 5000)
    String description
) {}
