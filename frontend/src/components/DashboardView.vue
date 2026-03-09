<script setup lang="ts">
import { reactive, ref } from 'vue'

interface Board {
  id: number
  title: string
  description: string
}

interface BoardFormState {
  title: string
  description: string
}

const boards = ref<Board[]>([
  {
    id: Date.now(),
    title: 'Board 1',
    description: 'My first board',
  },
])

const form: BoardFormState = reactive<BoardFormState>(initialFormState())

function initialFormState(): BoardFormState {
  return {
    title: '',
    description: '',
  }
}

function validateForm(): boolean {
  return true
}

function submitForm(): void {
  if (!validateForm()) return

  const newBoard: Board = {
    id: Date.now(),
    title: form.title.trim(),
    description: form.description.trim(),
  }

  boards.value.push(newBoard)
  resetForm()
}

function resetForm() {
  Object.assign(form, initialFormState())
}
</script>

<template>
  <form @submit.prevent="submitForm">
    <div>
      <label for="title">Title</label>
      <input id="title" v-model="form.title" type="text" />
    </div>

    <div>
      <label for="description">Description</label>
      <input id="description" v-model="form.description" type="text" />
    </div>

    <button type="submit">Submit</button>
    <button type="button" @click="resetForm">Reset</button>
  </form>

  <p>Current boards:</p>
  <ul v-for="board in boards" :key="board.id">
    <li>{{ board.id }}</li>
    <li>{{ board.title }}</li>
    <li>{{ board.description }}</li>
  </ul>
</template>

<style scoped></style>
