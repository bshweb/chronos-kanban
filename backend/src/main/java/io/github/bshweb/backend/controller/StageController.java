package io.github.bshweb.backend.controller;

import io.github.bshweb.backend.dto.stage.MoveStageRequest;
import io.github.bshweb.backend.service.StageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/boards/{boardId}/stages")
@RequiredArgsConstructor
public class StageController {

    private final StageService stageService;

    @PatchMapping("/{stageId}/move")
    public ResponseEntity<Void> moveStage(
            @PathVariable UUID boardId,
            @PathVariable UUID stageId,
            @Valid @RequestBody MoveStageRequest request
    ) {
        stageService.moveStage(boardId, stageId, request);
        return ResponseEntity.noContent().build();
    }
}
