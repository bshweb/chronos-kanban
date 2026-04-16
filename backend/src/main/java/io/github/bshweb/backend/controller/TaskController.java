package io.github.bshweb.backend.controller;

import io.github.bshweb.backend.dto.task.MoveTaskRequest;
import io.github.bshweb.backend.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/boards/{boardId}/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PatchMapping("/{taskId}/move")
    public ResponseEntity<Void> moveTask(
            @PathVariable UUID boardId,
            @PathVariable UUID taskId,
            @Valid @RequestBody MoveTaskRequest request
    ) {
        taskService.moveTask(boardId, taskId, request);
        return ResponseEntity.noContent().build();
    }

}
