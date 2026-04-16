<script setup lang="ts">
import BoardCreateArea from '@/components/board/BoardCreateArea.vue'
import { ref, onMounted, toRef } from 'vue'
import PlusIcon from '@/components/icons/PlusIcon.vue'
import { useDraggable } from 'vue-draggable-plus'
import { useBoardStore } from '@/stores/board.ts'
import BoardStageColumn from '@/components/board/BoardStageColumn.vue'

const boardStore = useBoardStore()
const board = boardStore.board

const stagesRef = toRef(board, 'stages') // Convention of the vue-draggable-plus: https://github.com/Alfred-Skyblue/vue-draggable-plus/issues/238
const boardRefDnd = ref<HTMLElement | null>(null)

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
      if (!movedStage) return

      const prevStage = board.stages[event.newIndex - 1] ?? null
      const nextStage = board.stages[event.newIndex + 1] ?? null

      await boardStore.persistStageMove(movedStage.id, prevStage?.id ?? null, nextStage?.id ?? null)
    } finally {
      draggableStages.resume()
    }
  },
})

onMounted(() => {
  boardStore.fetchBoard('11111111-1111-1111-1111-111111111111')
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
      class="flex w-full flex-1 min-h-0 gap-2 overflow-x-auto overflow-y-hidden"
    >
      <BoardStageColumn v-for="stage in board.stages" :key="stage.id" :stage-id="stage.id" />
      <div
        class="flex flex-col shrink-0 h-full min-h-0 w-full max-w-20 rounded-xl bg-(--color-surface) border border-(--color-border) shadow-2xs justify-center items-center hover:bg-(--color-bg) cursor-pointer"
      >
        <PlusIcon class="size-15" />
      </div>
    </div>
  </div>
</template>

<style scoped></style>
