<script setup lang="ts"></script>

<template>
  <div id="board-page-content" class="flex flex-col flex-1 min-h-0 w-full">
    <div id="board-page-title" class="flex flex-col items-center">
      <h1 class="font-bold">Kanban Board</h1>
      <span>Optimize your tasks</span>
    </div>
    <div id="board-page-main-create-area" class="flex flex-wrap justify-center gap-4">
      <span>add task</span>
      <span>add stage</span>
    </div>
    <div
      id="board-page-board"
      class="flex w-full flex-1 min-h-0 gap-3 overflow-x-auto overflow-y-hidden"
    >
      <div
        class="board-page-stage flex flex-col h-full min-h-0 shrink-0 w-full max-w-100 rounded-xl bg-(--color-surface)"
        v-for="(stage, index) in 4"
        :key="index"
      >
        <div class="board-page-stage-title flex items-center shrink-0">
          <span>⋮⋮</span>
          <span>To Do {{ stage }}</span>
          <span>12</span>
        </div>
        <div class="board-page-stage-tasks flex flex-col flex-1 min-h-0 overflow-y-auto">
          <div
            class="board-page-stage-task rounded-xl bg-(--color-surface-2) p-2 mx-2 my-1"
            v-for="(task, index) in 120"
            :key="index"
          >
            <span>⋮⋮</span>
            <span>Do thing {{ task }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
