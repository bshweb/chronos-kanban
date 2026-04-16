<script setup lang="ts">
import GripIcon from '@/components/icons/GripIcon.vue'
import { computed, ref } from 'vue'
import { useDraggable } from 'vue-draggable-plus'
import { useBoardStore } from '@/stores/board.ts'

const props = defineProps<{
  stageId: string
}>()

const boardStore = useBoardStore()

const stage = computed(() => boardStore.findStageById(props.stageId))

const tasksRef = computed({
  get: () => stage.value?.tasks ?? [],
  set: (value) => {
    if (!stage.value) return
    stage.value.tasks = value
  },
})

const taskListRef = ref<HTMLElement | null>(null)

const persistCurrentTaskPosition = async (newIndex: number) => {
  if (!stage.value) return

  const movedTask = stage.value.tasks[newIndex]
  if (!movedTask) return

  // backend order reversed
  const prevTask = stage.value.tasks[newIndex + 1] ?? null
  const nextTask = stage.value.tasks[newIndex - 1] ?? null

  await boardStore.persistTaskMove(
    movedTask.id,
    stage.value.id,
    prevTask?.id ?? null,
    nextTask?.id ?? null,
  )
}

const draggableTasks = useDraggable(taskListRef, tasksRef, {
  animation: 300,
  draggable: '.board-page-stage-task',
  group: 'board-tasks',
  scroll: true,
  bubbleScroll: true,
  scrollSensitivity: 300,
  scrollSpeed: 16,
  handle: '.handle-dnd-tasks',
  ghostClass: 'ghost',
  forceFallback: true,
  fallbackOnBody: true,
  onStart() {
    document.body.classList.add('is-dragging')
  },
  onEnd() {
    document.body.classList.remove('is-dragging')
  },
  async onUpdate(event) {
    try {
      draggableTasks.pause()

      if (event.newIndex == null) return
      await persistCurrentTaskPosition(event.newIndex)
    } finally {
      draggableTasks.resume()
    }
  },
  async onAdd(event) {
    try {
      draggableTasks.pause()

      if (event.newIndex == null) return
      await persistCurrentTaskPosition(event.newIndex)
    } finally {
      draggableTasks.resume()
    }
  },
})
</script>

<template>
  <div
    v-if="stage"
    class="board-page-stage flex flex-col shrink-0 h-full min-h-0 w-full max-w-100 rounded-xl bg-(--color-surface) border border-(--color-border) shadow-2xs"
  >
    <div class="board-page-stage-title flex items-center shrink-0 rounded-xl p-2 mx-1 my-1 gap-x-2">
      <GripIcon class="handle-dnd-stages size-5 shrink-0 cursor-grab active:cursor-grabbing" />
      <span class="font-bold min-w-0 wrap-break-word"
        >Lorem ipsum testtttttttttttttttttttttttttttttttttttttttttttttttttt {{ stage.title }}</span
      >
      <span>{{ stage.tasks.length }}</span>
    </div>
    <div
      ref="taskListRef"
      class="board-page-stage-tasks flex flex-col flex-1 min-h-0 overflow-y-auto overflow-x-hidden"
    >
      <div
        v-for="task in stage.tasks"
        :key="task.id"
        class="board-page-stage-task flex rounded-xl bg-(--color-surface-2) p-2 mx-1 my-1 items-center gap-x-2"
      >
        <GripIcon class="handle-dnd-tasks size-5 shrink-0 cursor-grab active:cursor-grabbing" />
        <span class="min-w-0 wrap-break-word"
          >Do thingfdsfdsafdsafdsafdsafasfsdafsdaffsadfasdfasdfsdafasdfsda {{ task.title }}</span
        >
      </div>
    </div>
  </div>
</template>
