package io.github.bshweb.backend.controller;

import io.github.bshweb.backend.dto.board.BoardDetailsResponse;
import io.github.bshweb.backend.dto.board.BoardSummaryResponse;
import io.github.bshweb.backend.dto.board.CreateBoardRequest;
import io.github.bshweb.backend.dto.board.UpdateBoardRequest;
import io.github.bshweb.backend.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    public ResponseEntity<List<BoardSummaryResponse>> getAllBoards() {
        return ResponseEntity.ok(boardService.getAllBoards());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardDetailsResponse> getBoardWithStagesAndTasksById(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(boardService.getBoardWithStagesAndTasksById(id));
    }

    @PostMapping
    public ResponseEntity<BoardSummaryResponse> createBoard(
            @Valid @RequestBody CreateBoardRequest request
    ) {
        BoardSummaryResponse createdBoard = boardService.createBoard(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdBoard);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoardSummaryResponse> updateBoard(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateBoardRequest request
    ) {
        return ResponseEntity.ok(boardService.updateBoard(id, request));
    }
}
