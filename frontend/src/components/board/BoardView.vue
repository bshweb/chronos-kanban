<script setup lang="ts">
import GripIcon from '@/components/icons/GripIcon.vue'
import BoardCreateArea from '@/components/board/BoardCreateArea.vue'
import { ref, reactive, onMounted, toRef } from 'vue'
import PlusIcon from '@/components/icons/PlusIcon.vue'
import { useDraggable } from 'vue-draggable-plus'
import type { Board } from '@/types/board.ts'
import { api } from '@/shared/api/http.ts'

const board = reactive<Board>({
  id: '',
  title: '',
  description: null,
  stages: [],
})

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

const fetchBoard = async (boardId: string) => {
  try {
    const { data } = await api.get<Board>(`/boards/${boardId}`)
    Object.assign(board, data)
  } catch (error) {
    console.error('Ошибка загрузки доски', error)
  }
}

const stagesRef = toRef(board, 'stages') // Convention of the vue-draggable-plus: https://github.com/Alfred-Skyblue/vue-draggable-plus/issues/238
const boardRefDnd = ref()
const draggableStages = useDraggable(boardRefDnd, stagesRef, {
  animation: 300,
  draggable: '.board-page-stage',
  direction: 'horizontal',
  scroll: true,
  bubbleScroll: true,
  scrollSensitivity: 300,
  scrollSpeed: 16,
  handle: '.handle-dnd-stages',
  ghostClass: 'ghost',
  forceFallback: true,
  fallbackOnBody: true,
  onStart() {
    document.body.classList.add('is-dragging')
  },
  async onEnd(event) {
    document.body.classList.remove('is-dragging')
    try {
      draggableStages.pause()

      if (event.oldIndex == null || event.newIndex == null) return
      if (event.oldIndex === event.newIndex) return

      const movedStage = board.stages[event.newIndex]
      const prevStage = board.stages[event.newIndex - 1] ?? null
      const nextStage = board.stages[event.newIndex + 1] ?? null

      if (!movedStage) return

      await moveStage(board.id, movedStage.id, prevStage?.id ?? null, nextStage?.id ?? null)

      await fetchBoard(board.id)
    } finally {
      draggableStages.resume()
    }
  },
})

onMounted(() => {
  fetchBoard('11111111-1111-1111-1111-111111111111')
})
</script>

<template>
  <div id="board-page-content" class="flex flex-col flex-1 min-h-0 w-full">
    <div id="board-page-title" class="flex flex-col items-center">
      <h1 class="font-bold">Kanban Board</h1>
      <span>Optimize your tasks</span>
    </div>
    <BoardCreateArea />
    <div
      id="board-page-board"
      ref="boardRefDnd"
      class="flex w-full flex-1 min-h-0 gap-3 overflow-x-auto overflow-y-hidden"
    >
      <div
        class="board-page-stage flex flex-col shrink-0 h-full min-h-0 w-full max-w-100 rounded-xl bg-(--color-surface) border border-(--color-border) shadow-2xs"
        v-for="stage in board.stages"
        :key="stage.id"
      >
        <div
          class="board-page-stage-title flex items-center shrink-0 rounded-xl p-2 mx-1 my-1 gap-x-2"
        >
          <GripIcon class="handle-dnd-stages size-5 shrink-0 cursor-grab active:cursor-grabbing" />
          <span class="font-bold min-w-0 wrap-break-word"
            >Lorem ipsum testtttttttttttttttttttttttttttttttttttttttttttttttttt
            {{ stage.title }}</span
          >
          <span>{{ stage.tasks?.length ?? 0 }}</span>
        </div>
        <div class="board-page-stage-tasks flex flex-col flex-1 min-h-0 overflow-y-auto">
          <div
            class="board-page-stage-task flex rounded-xl bg-(--color-surface-2) p-2 mx-1 my-1 items-center gap-x-2"
            v-for="task in stage.tasks"
            :key="task.id"
          >
            <GripIcon class="size-5 shrink-0 cursor-grab active:cursor-grabbing" />
            <span class="min-w-0 wrap-break-word"
              >Do thingfdsfdsafdsafdsafdsafasfsdafsdaffsadfasdfasdfsdafasdfsda
              {{ task.title }}</span
            >
          </div>
        </div>
      </div>
      <div
        class="flex flex-col shrink-0 h-full min-h-0 w-full max-w-20 rounded-xl bg-(--color-surface) border border-(--color-border) shadow-2xs justify-center items-center hover:bg-(--color-bg) cursor-pointer"
      >
        <PlusIcon class="size-15" />
      </div>
    </div>
  </div>
</template>

<style scoped></style>
