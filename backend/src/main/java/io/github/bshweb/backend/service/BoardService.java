package io.github.bshweb.backend.service;

import io.github.bshweb.backend.dto.board.BoardResponse;
import io.github.bshweb.backend.dto.board.CreateBoardRequest;
import io.github.bshweb.backend.dto.board.UpdateBoardRequest;
import io.github.bshweb.backend.entity.Board;
import io.github.bshweb.backend.mapper.BoardMapper;
import io.github.bshweb.backend.repository.BoardRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardMapper boardMapper;

    public List<BoardResponse> getAllBoards() {
        return boardRepository.findAll()
                .stream()
                .map(boardMapper::toResponse)
                .toList();
    }

    public BoardResponse getBoardById(UUID id) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Board not found"));

        return boardMapper.toResponse(board);
    }

    public BoardResponse createBoard(CreateBoardRequest request) {
        Board board = boardMapper.toEntity(request);
        Board savedBoard = boardRepository.save(board);
        return boardMapper.toResponse(savedBoard);
    }

    public BoardResponse updateBoard(UUID id, UpdateBoardRequest request) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Board not found"));

        boardMapper.updateEntity(request, board);

        Board updatedBoard = boardRepository.save(board);
        return boardMapper.toResponse(updatedBoard);
    }

}
