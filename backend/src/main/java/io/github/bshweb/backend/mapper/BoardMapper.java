package io.github.bshweb.backend.mapper;

import io.github.bshweb.backend.dto.board.BoardDetailsResponse;
import io.github.bshweb.backend.dto.board.BoardSummaryResponse;
import io.github.bshweb.backend.dto.board.CreateBoardRequest;
import io.github.bshweb.backend.dto.board.UpdateBoardRequest;
import io.github.bshweb.backend.dto.stage.StageDetailsResponse;
import io.github.bshweb.backend.dto.task.TaskDetailsResponse;
import io.github.bshweb.backend.entity.Board;
import io.github.bshweb.backend.entity.Stage;
import io.github.bshweb.backend.entity.Task;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

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

    public BoardSummaryResponse toSummaryResponse(Board board) {
        return new BoardSummaryResponse(
                board.getId(),
                board.getTitle(),
                board.getDescription()
        );
    }

    public BoardDetailsResponse toDetailsResponse(Board board) {
        List<StageDetailsResponse> stageDetailsResponses = board.getStages().stream()
                .map(this::toStageDetailsResponse)
                .toList();

        return new BoardDetailsResponse(
                board.getId(),
                board.getTitle(),
                board.getDescription(),
                stageDetailsResponses
        );
    }

    private StageDetailsResponse toStageDetailsResponse(Stage stage) {
        List<TaskDetailsResponse> taskResponses = stage.getTasks().stream()
                .sorted(Comparator.comparing(Task::getPosition).reversed()) // Frontend gets tasks in descending order
                .map(this::toTaskDetailsResponse)
                .toList();

        return new StageDetailsResponse(
                stage.getId(),
                stage.getTitle(),
                taskResponses
        );
    }

    private TaskDetailsResponse toTaskDetailsResponse(Task task) {
        return new TaskDetailsResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription()
        );
    }
}
