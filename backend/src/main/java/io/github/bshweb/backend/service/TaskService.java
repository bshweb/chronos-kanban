package io.github.bshweb.backend.service;

import io.github.bshweb.backend.dto.task.MoveTaskRequest;
import io.github.bshweb.backend.entity.Stage;
import io.github.bshweb.backend.entity.Task;
import io.github.bshweb.backend.ordering.PositionManager;
import io.github.bshweb.backend.repository.StageRepository;
import io.github.bshweb.backend.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskService {

    // TODO Custom Exceptions
    // TODO check that prevTask and nextTask are the direct neighbors

    private final TaskRepository taskRepository;
    private final StageRepository stageRepository;
    private final PositionManager positionManager;

    // FIXME Fix race condition when two clients move one entity at the same time
    // TODO add @Version to the entities
    @Transactional
    public void moveTask(UUID boardId, UUID taskId, MoveTaskRequest request) {
        // Fetch the task to be moved within the given board
        Task task = taskRepository.findByIdAndStageBoardId(taskId, boardId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found in board"));

        // Fetch the target stage within the same board
        Stage targetStage = stageRepository.findByIdAndBoardId(request.targetStageId(), boardId)
                .orElseThrow(() -> new IllegalArgumentException("Target stage not found in board"));

        // Fetch neighboring tasks within the target stage
        Task prevTask = getTaskIfPresent(targetStage.getId(), request.prevTaskId(), "Prev task not found in target stage");
        Task nextTask = getTaskIfPresent(targetStage.getId(), request.nextTaskId(), "Next task not found in target stage");

        // Validate the request
        validateMoveRequest(taskId, prevTask, nextTask);

        // Calculate the new position inside the target stage
        Long newPosition = calculateNewPosition(targetStage.getId(), prevTask, nextTask);

        // Move the task to the target stage and assign the new position
        task.setStage(targetStage);
        task.setPosition(newPosition);
    }

    private Task getTaskIfPresent(UUID stageId, UUID taskId, String errorMessage) {
        if (taskId == null) {
            return null;
        }

        return taskRepository.findByIdAndStageId(taskId, stageId)
                .orElseThrow(() -> new IllegalArgumentException(errorMessage));
    }

    private void validateMoveRequest(UUID taskId, Task prevTask, Task nextTask) {
        if (prevTask != null && prevTask.getId().equals(taskId)) {
            throw new IllegalArgumentException("Prev task cannot be the moved task itself");
        }

        if (nextTask != null && nextTask.getId().equals(taskId)) {
            throw new IllegalArgumentException("Next task cannot be the moved task itself");
        }

        if (prevTask != null && nextTask != null) {
            if (prevTask.getId().equals(nextTask.getId())) {
                throw new IllegalArgumentException("Prev and Next tasks must be different");
            }

            if (prevTask.getPosition() >= nextTask.getPosition()) {
                throw new IllegalArgumentException("Prev task position must be less than Next task position");
            }
        }
    }

    private Long calculateNewPosition(UUID targetStageId, Task prevTask, Task nextTask) {

        // If there are no other tasks in the target stage
        if (prevTask == null && nextTask == null) {
            // TODO check that the target stage task list is empty (or only contains the moved task)
            return positionManager.initialPosition();
        }

        // If the task was moved to the last position
        if (prevTask != null && nextTask == null) {
            if (positionManager.canAppendAfter(prevTask.getPosition())) {
                long candidate = positionManager.newLast(prevTask.getPosition());
                if (positionManager.isValidLastPosition(candidate, prevTask.getPosition())) {
                    return candidate;
                }
            }

            // If there is no room at the end
            rebalance(targetStageId);

            Task refreshedPrev = taskRepository.findByIdAndStageId(prevTask.getId(), targetStageId)
                    .orElseThrow(() -> new IllegalStateException("Prev task disappeared after rebalance"));

            // If there is still no room at the end after rebalance
            if (!positionManager.canAppendAfter(refreshedPrev.getPosition())) {
                throw new IllegalStateException("Unable to allocate position after last task");
            }

            long candidate = positionManager.newLast(refreshedPrev.getPosition());

            if (!positionManager.isValidLastPosition(candidate, refreshedPrev.getPosition())) {
                throw new IllegalStateException("Unable to allocate valid position after last task");
            }

            return candidate;
        }

        // If the task was moved to the first position
        if (prevTask == null) {
            if (positionManager.canPrependBefore(nextTask.getPosition())) {
                long candidate = positionManager.newFirst(nextTask.getPosition());
                if (positionManager.isValidFirstPosition(candidate, nextTask.getPosition())) {
                    return candidate;
                }
            }

            // If there is no room at the start
            rebalance(targetStageId);

            Task refreshedNext = taskRepository.findByIdAndStageId(nextTask.getId(), targetStageId)
                    .orElseThrow(() -> new IllegalStateException("Next task disappeared after rebalance"));

            // If there is still no room at the start after rebalance
            if (!positionManager.canPrependBefore(refreshedNext.getPosition())) {
                throw new IllegalStateException("Unable to allocate position before first task");
            }

            long candidate = positionManager.newFirst(refreshedNext.getPosition());

            if (!positionManager.isValidFirstPosition(candidate, refreshedNext.getPosition())) {
                throw new IllegalStateException("Unable to allocate position before first task");
            }

            return candidate;
        }

        // If there is no gaps between two neighbors
        if (!positionManager.hasGapBetween(prevTask.getPosition(), nextTask.getPosition())) {
            rebalance(targetStageId);

            Task refreshedPrev = taskRepository.findByIdAndStageId(prevTask.getId(), targetStageId)
                    .orElseThrow(() -> new IllegalStateException("Prev task disappeared after rebalance"));

            Task refreshedNext = taskRepository.findByIdAndStageId(nextTask.getId(), targetStageId)
                    .orElseThrow(() -> new IllegalStateException("Next task disappeared after rebalance"));

            long candidate = positionManager.between(refreshedPrev.getPosition(), refreshedNext.getPosition());

            if (!positionManager.isValidBetweenPosition(candidate, refreshedPrev.getPosition(), refreshedNext.getPosition())) {
                throw new IllegalStateException("Unable to allocate position between tasks after rebalance");
            }

            return candidate;
        }

        // Else
        long candidate = positionManager.between(prevTask.getPosition(), nextTask.getPosition());

        if (!positionManager.isValidBetweenPosition(candidate, prevTask.getPosition(), nextTask.getPosition())) {
            throw new IllegalStateException("Unable to allocate position between tasks");
        }

        return candidate;
    }

    private void rebalance(UUID stageId) {
        List<Task> tasks = taskRepository.findAllByStageIdOrderByPositionAsc(stageId);

        long totalCount = tasks.size();
        for (int i = 0; i < tasks.size(); i++) {
            tasks.get(i).setPosition(positionManager.rebalanceValue(i, totalCount));
        }
    }
}
