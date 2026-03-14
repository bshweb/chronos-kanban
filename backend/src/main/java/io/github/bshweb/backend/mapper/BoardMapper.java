package io.github.bshweb.backend.mapper;

import io.github.bshweb.backend.dto.board.BoardResponse;
import io.github.bshweb.backend.dto.board.CreateBoardRequest;
import io.github.bshweb.backend.dto.board.UpdateBoardRequest;
import io.github.bshweb.backend.entity.Board;
import org.springframework.stereotype.Component;

@Component
public class BoardMapper {

    public Board toEntity(CreateBoardRequest request) {
        Board board = new Board();
        board.setTitle(request.title());
        board.setDescription(request.description());
        return board;
    }

    public void updateEntity(UpdateBoardRequest request, Board board) {
        board.setTitle(request.title());
        board.setDescription(request.description());
    }

    public BoardResponse toResponse(Board board) {
        return new BoardResponse(
                board.getId(),
                board.getTitle(),
                board.getDescription()
        );
    }
}
