<script setup lang="ts">
import GripIcon from '@/components/icons/GripIcon.vue'
import BoardCreateArea from '@/components/board/BoardCreateArea.vue'
import { ref } from 'vue'
import PlusIcon from '@/components/icons/PlusIcon.vue'
import { useDraggable } from 'vue-draggable-plus'

interface Stage {
  id: string
  title: string
  position: string // JSON.stringify() used in vue-draggable-plus can't serialize a BigInt value
  tasks?: Task[]
}

interface Task {
  id: string
  title: string
  description?: string
  position: bigint
}

const stages = ref<Stage[]>([
  { id: 'blah-id-0', title: 'Backlog', position: '0' },
  { id: 'blah-id-1', title: 'To Do', position: '1' },
  { id: 'blah-id-2', title: 'In Progress', position: '2' },
  { id: 'blah-id-3', title: 'Review required', position: '3' },
  { id: 'blah-id-4', title: 'Reviewed', position: '4' },
  { id: 'blah-id-5', title: 'Done', position: '5' },
])

const boardRefDnd = ref()
useDraggable(boardRefDnd, stages, {
  animation: 150,
  draggable: '.board-page-stage',
  direction: 'horizontal',
  scroll: true,
  scrollSensitivity: 300,
  scrollSpeed: 16,
  handle: '.handle-dnd-stages',
  ghostClass: 'ghost',
  forceFallback: true,
  fallbackOnBody: true,
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
        v-for="stage in stages"
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
          <span>12</span>
        </div>
        <div class="board-page-stage-tasks flex flex-col flex-1 min-h-0 overflow-y-auto">
          <div
            class="board-page-stage-task flex rounded-xl bg-(--color-surface-2) p-2 mx-1 my-1 items-center gap-x-2"
            v-for="(task, index) in 120"
            :key="index"
          >
            <GripIcon class="size-5 shrink-0 cursor-grab active:cursor-grabbing" />
            <span class="min-w-0 wrap-break-word"
              >Do thingfdsfdsafdsafdsafdsafasfsdafsdaffsadfasdfasdfsdafasdfsda {{ task }}</span
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

<style scoped>
.ghost {
  opacity: 0.5;
  background: var(--color-border);
}
</style>
