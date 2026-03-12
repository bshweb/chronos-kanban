package io.github.bshweb.backend.controller;

import io.github.bshweb.backend.dto.BoardDTO;
import io.github.bshweb.backend.entity.Board;
import io.github.bshweb.backend.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping
    public List<BoardDTO> getAllBoards() {
        return boardService.getAllBoards()
                .stream()
                .map(BoardController::toDTO)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardDTO> getBoardById(@PathVariable Long id) {
        Board board = boardService.getBoardById(id);
        return ResponseEntity.ok(toDTO(board));
    }

    private static BoardDTO toDTO(Board board) {
        return new BoardDTO(board.getId(), board.getTitle(), board.getDescription());
    }
}
