<script setup lang="ts">
import GripVerticalIcon from '@/components/icons/GripVerticalIcon.vue'
import BoardCreateArea from '@/components/board/BoardCreateArea.vue'
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
      class="flex w-full flex-1 min-h-0 gap-3 overflow-x-auto overflow-y-hidden"
    >
      <div
        class="board-page-stage flex flex-col h-full min-h-0 shrink-0 w-full max-w-100 rounded-xl bg-(--color-surface)"
        v-for="(stage, index) in 40"
        :key="index"
      >
        <div
          class="board-page-stage-title flex items-center shrink-0 rounded-xl p-2 mx-1 my-1 gap-x-2"
        >
          <GripVerticalIcon class="size-5 shrink-0 cursor-grab active:cursor-grabbing" />
          <span class="font-bold min-w-0 wrap-break-word"
            >To Dofdsaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa {{ stage }}</span
          >
          <span>12</span>
        </div>
        <div class="board-page-stage-tasks flex flex-col flex-1 min-h-0 overflow-y-auto">
          <div
            class="board-page-stage-task flex rounded-xl bg-(--color-surface-2) p-2 mx-1 my-1 items-center gap-x-2"
            v-for="(task, index) in 120"
            :key="index"
          >
            <GripVerticalIcon class="size-5 shrink-0 cursor-grab active:cursor-grabbing" />
            <span class="min-w-0 wrap-break-word"
              >Do thingfdsfdsafdsafdsafdsafasfsdafsdaffsadfasdfasdfsdafasdfsda {{ task }}</span
            >
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
