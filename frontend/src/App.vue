<script setup lang="ts">
import {
  BApp,
  BBadge,
  BButton,
  BCard,
  BCardBody,
  BCol,
  BContainer,
  BFormInput,
  BRow,
} from 'bootstrap-vue-next'

import { computed, ref } from 'vue'

interface Stage {
  id: string
  title: string
  order: number | null
}

const stages = ref<Stage[]>([
  { id: 'todo-1', title: 'To Do 1', order: null },
  { id: 'todo-2', title: 'To Do 2', order: null },
  { id: 'todo-3', title: 'To Do 3', order: null },
  { id: 'todo-4', title: 'To Do 4', order: null },
  { id: 'todo-5', title: 'To Do 5', order: null },
  { id: 'todo-6', title: 'To Do 6', order: null },
  { id: 'todo-7', title: 'To Do 7', order: null },
  { id: 'todo-8', title: 'To Do 8', order: null },
  { id: 'todo-9', title: 'To Do 9', order: null },
  { id: 'todo-10', title: 'To Do 10', order: null },
])

const isCentered = computed(() => stages.value.length <= 6)
</script>

<template>
  <BApp>
    <BContainer fluid class="d-flex flex-column vh-100 py-2">
      <header class="text-center mb-1">Header placeholder</header>

      <div class="text-center mb-1">
        <h3><b>Kanban Board</b></h3>
      </div>

      <BRow class="justify-content-center g-1 mb-1">
        <BCol md="6" lg="5">
          <BCard>
            <BCardBody class="p-0">
              <h6 class="mb-2">Add New Task</h6>
              <div class="d-flex gap-2">
                <BFormInput placeholder="Enter task name..." size="sm" class="flex-grow-1" />
                <BButton variant="primary" size="sm" class="text-nowrap">+ Add</BButton>
              </div>
            </BCardBody>
          </BCard>
        </BCol>

        <BCol md="6" lg="5">
          <BCard>
            <BCardBody class="p-0">
              <h6 class="mb-2">Add New Stage</h6>
              <div class="d-flex gap-2">
                <BFormInput placeholder="Enter stage name..." size="sm" class="flex-grow-1" />
                <BButton variant="primary" size="sm" class="text-nowrap">+ Add</BButton>
              </div>
            </BCardBody>
          </BCard>
        </BCol>
      </BRow>

      <div class="board-wrapper">
        <div class="board-stages" :class="{ 'is-centered': isCentered }">
          <div class="stage-column" v-for="(stage, index) in stages" :key="index">
            <BCard class="stage-card">
              <div class="d-flex align-items-center px-3 pb-2 border-bottom">
                <h5 class="mb-0 me-2">{{ stage }}</h5>
                <BBadge pill>12</BBadge>
              </div>

              <BCardBody class="stage-body">
                <BCard class="mb-2" v-for="task in 24" :key="task">
                  <BCardBody class="p-0">Task {{ task }}</BCardBody>
                </BCard>
              </BCardBody>
            </BCard>
          </div>
        </div>
      </div>

      <footer class="text-center pt-1">Footer placeholder</footer>
    </BContainer>
  </BApp>
</template>

<style scoped>
.board-wrapper {
  flex: 1 1 auto;
  min-height: 0;
  overflow-x: auto;
  overflow-y: hidden;
}

.board-stages {
  display: flex;
  align-items: flex-start;
  gap: 0.25rem;
  min-height: 100%;
  padding-bottom: 0.5rem;
}

.board-stages.is-centered {
  justify-content: center;
}

.stage-column {
  flex: 0 0 350px;
}

.stage-body {
  overflow-y: auto;
  padding-bottom: 0;
  height: calc(100vh - 280px);
}

.stage-card {
  height: 100%;
  --bs-card-spacer-y: 0.5rem;
  --bs-card-spacer-x: 0.25rem;
}
</style>
