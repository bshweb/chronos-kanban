import { reactive } from 'vue'
import { defineStore } from 'pinia'
import type { Board } from '@/types/board'
import { api } from '@/shared/api/http.ts'

const createEmptyBoard = (): Board => ({
  id: '',
  title: '',
  description: null,
  stages: [],
})

export const useBoardStore = defineStore('board', () => {
  const board = reactive<Board>(createEmptyBoard())

  const fetchBoard = async (boardId: string) => {
    try {
      const { data } = await api.get<Board>(`/boards/${boardId}`)
      Object.assign(board, createEmptyBoard(), data) // Reset defaults first to avoid stale fields from previous board data
    } catch (error) {
      console.error('Error by fetching a board', error)
    }
  }

  const moveStage = async (
    boardId: string,
    stageId: string,
    prevStageId: string | null,
    nextStageId: string | null,
  ) => {
    await api.patch(`/boards/${boardId}/stages/${stageId}/move`, {
      prevStageId,
      nextStageId,
    })
  }

  const persistStageMove = async (
    stageId: string,
    prevStageId: string | null,
    nextStageId: string | null,
  ) => {
    await moveStage(board.id, stageId, prevStageId, nextStageId)
    await fetchBoard(board.id)
  }

  const findStageById = (stageId: string) => {
    return board.stages.find((stage) => stage.id === stageId) ?? null
  }

  // const moveTask = async (
  //   boardId: string,
  //   taskId: string,
  //   targetStageId: string,
  //   prevTaskId: string | null,
  //   nextTaskId: string | null,
  // ) => {
  //   await api.patch(`/boards/${boardId}/tasks/${taskId}/move`, {
  //     targetStageId,
  //     prevTaskId,
  //     nextTaskId,
  //   })
  // }

  return {
    board,
    fetchBoard,
    moveStage,
    persistStageMove,
    findStageById,
  }
})
