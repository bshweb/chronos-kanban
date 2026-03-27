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
</script>

<template>
  <BApp>
    <BContainer fluid class="d-flex flex-column vh-100 py-4">
      <header class="text-center mb-4">Header placeholder</header>

      <div class="text-center mb-1">
        <h1><b>Kanban Board</b></h1>
      </div>

      <BRow class="justify-content-center g-2 mb-3">
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

      <div class="board-wrapper flex-grow-1">
        <div class="board-stages">
          <div class="stage-column" v-for="stage in 8" :key="stage">
            <BCard class="h-100">
              <div class="d-flex align-items-center px-3 py-2 border-bottom">
                <h5 class="mb-0 me-2">To Do {{ stage }}</h5>
                <BBadge pill>12</BBadge>
              </div>

              <BCardBody class="stage-body">
                <BCard class="mb-2" v-for="task in 8" :key="task">
                  <BCardBody>Task {{ task }}</BCardBody>
                </BCard>
              </BCardBody>
            </BCard>
          </div>
        </div>
      </div>

      <footer class="mt-auto text-center">Footer placeholder</footer>
    </BContainer>
  </BApp>
</template>

<style scoped>
.board-wrapper {
  flex: 1;
  min-height: 0;
  overflow: auto;
}

.board-stages {
  display: flex;
  align-items: flex-start;
  gap: 1rem;
  min-height: 100%;
  padding-bottom: 0.5rem;
}

.stage-column {
  flex: 0 0 320px;
  height: 100%;
}

.stage-body {
  overflow-y: auto;
  max-height: calc(100vh - 260px);
}
</style>
