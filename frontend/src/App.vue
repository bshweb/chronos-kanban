<script setup lang="ts">
import {
  BApp,
  BButton,
  BCard,
  BCardBody,
  BCol,
  BContainer,
  BFormInput,
  BRow,
} from 'bootstrap-vue-next'
import BoardComponent from '@/components/board/BoardComponent.vue'
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

      <BoardComponent />

      <footer class="text-center pt-1">Footer placeholder</footer>
    </BContainer>
  </BApp>
</template>

<style scoped></style>
