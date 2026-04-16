package io.github.bshweb.backend.service;

import io.github.bshweb.backend.dto.stage.MoveStageRequest;
import io.github.bshweb.backend.entity.Stage;
import io.github.bshweb.backend.ordering.PositionManager;
import io.github.bshweb.backend.repository.StageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StageService {

    // TODO Custom Exceptions
    // TODO check that prevStage and nextStage are the direct neighbors

    private final StageRepository stageRepository;
    private final PositionManager positionManager;

    // FIXME Fix race condition when two clients move one entity at the same time
    // TODO add @Version to the entities
    @Transactional
    public void moveStage(UUID boardId, UUID stageId, MoveStageRequest request) {
        // Fetch the stage to be moved
        Stage stage = stageRepository.findByIdAndBoardId(stageId, boardId)
                .orElseThrow(() -> new IllegalArgumentException("Stage not found in board"));

        // Fetch the neighboring stages from the request between which the stage should be placed
        Stage prevStage = getStageIfPresent(boardId, request.prevStageId(), "Prev stage not found in board");
        Stage nextStage = getStageIfPresent(boardId, request.nextStageId(), "Next stage not found in board");

        // Check if the move request is valid. If not, throw a corresponding exception
        validateMoveRequest(stageId, prevStage, nextStage);

        // Calculate a new position for the moved stage, rebalancing positions if necessary
        Long newPosition = calculateNewPosition(boardId, prevStage, nextStage);

        // Assign the calculated position to the moved stage (in managed state)
        stage.setPosition(newPosition);
    }

    private Stage getStageIfPresent(UUID boardId, UUID stageId, String errorMessage) {
        if (stageId == null) {
            return null;
        }

        return stageRepository.findByIdAndBoardId(stageId, boardId)
                .orElseThrow(() -> new IllegalArgumentException(errorMessage));
    }

    private void validateMoveRequest(UUID stageId, Stage prevStage, Stage nextStage) {
        if (prevStage != null && prevStage.getId().equals(stageId)) {
            throw new IllegalArgumentException("Prev stage cannot be the moved stage itself");
        }

        if (nextStage != null && nextStage.getId().equals(stageId)) {
            throw new IllegalArgumentException("Next stage cannot be the moved stage itself");
        }

        if (prevStage != null && nextStage != null) {
            if (prevStage.getId().equals(nextStage.getId())) {
                throw new IllegalArgumentException("Prev and Next stages must be different");
            }

            if (prevStage.getPosition() >= nextStage.getPosition()) {
                throw new IllegalArgumentException("Prev stage position must be less than Next stage position");
            }
        }
    }

    private Long calculateNewPosition(UUID boardId, Stage prevStage, Stage nextStage) {

        // If there are no other stages
        if (prevStage == null && nextStage == null) {
            // TODO check that the repo is empty
            return positionManager.initialPosition();
        }

        // If the stage was moved to the last position
        if (prevStage != null && nextStage == null) {
            if (positionManager.canAppendAfter(prevStage.getPosition())) {
                long candidate = positionManager.newLast(prevStage.getPosition());
                if (positionManager.isValidLastPosition(candidate, prevStage.getPosition())) {
                    return candidate;
                }
            }

            // If there is no room at the end
            rebalance(boardId);

            Stage refreshedPrev = stageRepository.findByIdAndBoardId(prevStage.getId(), boardId)
                    .orElseThrow(() -> new IllegalStateException("Prev stage disappeared after rebalance"));

            // If there is still no room at the end after rebalance
            if (!positionManager.canAppendAfter(refreshedPrev.getPosition())) {
                throw new IllegalStateException("Unable to allocate position after last stage");
            }

            long candidate = positionManager.newLast(refreshedPrev.getPosition());

            if (!positionManager.isValidLastPosition(candidate, refreshedPrev.getPosition())) {
                throw new IllegalStateException("Unable to allocate valid position after last stage");
            }

            return candidate;
        }

        // If the stage was moved to the first position
        if (prevStage == null) {
            if (positionManager.canPrependBefore(nextStage.getPosition())) {
                long candidate = positionManager.newFirst(nextStage.getPosition());
                if (positionManager.isValidFirstPosition(candidate, nextStage.getPosition())) {
                    return candidate;
                }
            }

            // If there is no room at the start
            rebalance(boardId);

            Stage refreshedNext = stageRepository.findByIdAndBoardId(nextStage.getId(), boardId)
                    .orElseThrow(() -> new IllegalStateException("Next stage disappeared after rebalance"));

            // If there is still no room at the start after rebalance
            if (!positionManager.canPrependBefore(refreshedNext.getPosition())) {
                throw new IllegalStateException("Unable to allocate position before first stage");
            }

            long candidate = positionManager.newFirst(refreshedNext.getPosition());

            if (!positionManager.isValidFirstPosition(candidate, refreshedNext.getPosition())) {
                throw new IllegalStateException("Unable to allocate position before first stage");
            }

            return candidate;
        }

        // If there is no gaps between two neighbors
        if (!positionManager.hasGapBetween(prevStage.getPosition(), nextStage.getPosition())) {
            rebalance(boardId);

            Stage refreshedPrev = stageRepository.findByIdAndBoardId(prevStage.getId(), boardId)
                    .orElseThrow(() -> new IllegalStateException("Prev stage disappeared after rebalance"));

            Stage refreshedNext = stageRepository.findByIdAndBoardId(nextStage.getId(), boardId)
                    .orElseThrow(() -> new IllegalStateException("Next stage disappeared after rebalance"));

            long candidate = positionManager.between(refreshedPrev.getPosition(), refreshedNext.getPosition());

            if (!positionManager.isValidBetweenPosition(candidate, refreshedPrev.getPosition(), refreshedNext.getPosition())) {
                throw new IllegalStateException("Unable to allocate position between stages after rebalance");
            }

            return candidate;
        }

        // Else
        long candidate = positionManager.between(prevStage.getPosition(), nextStage.getPosition());

        if (!positionManager.isValidBetweenPosition(candidate, prevStage.getPosition(), nextStage.getPosition())) {
            throw new IllegalStateException("Unable to allocate position between stages");
        }

        return candidate;
    }

    private void rebalance(UUID boardId) {
        List<Stage> stages = stageRepository.findAllByBoardIdOrderByPositionAsc(boardId);

        long totalCount = stages.size();
        for (int i = 0; i < stages.size(); i++) {
            stages.get(i).setPosition(positionManager.rebalanceValue(i, totalCount));
        }
    }
}
