<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { api } from '@/shared/api/axios.ts'

interface Board {
  id: string
  title: string
  description: string
}

interface BoardFormState {
  title: string
  description: string
}

function initialFormState(): BoardFormState {
  return {
    title: '',
    description: '',
  }
}

const boards = ref<Board[]>([])
const isLoading = ref(false)
const isSubmitting = ref(false)
const errorMessage = ref<string | null>(null)
const form = reactive<BoardFormState>(initialFormState())

function validateForm(): boolean {
  const title = form.title.trim()
  const description = form.description.trim()

  if (!title) {
    errorMessage.value = 'Title is required.'
    return false
  }
  if (title.length > 100) {
    errorMessage.value = 'Title must be 100 characters or less.'
    return false
  }
  if (description.length > 500) {
    errorMessage.value = 'Description must be 500 characters or less.'
    return false
  }
  errorMessage.value = null
  return true
}

async function fetchBoards(): Promise<void> {
  isLoading.value = true
  errorMessage.value = null

  try {
    const response = await api.get<Board[]>('/boards')
    boards.value = response.data
  } catch (error) {
    console.error('Failed to fetch boards:', error)
    errorMessage.value = 'Failed to load boards.'
  } finally {
    isLoading.value = false
  }
}

async function submitForm(): Promise<void> {
  if (!validateForm()) return

  isSubmitting.value = true
  errorMessage.value = null

  try {
    const payload: BoardFormState = {
      title: form.title.trim(),
      description: form.description.trim(),
    }

    const response = await api.post<Board>('/boards', payload)

    boards.value.push(response.data)
    resetForm()
  } catch (error) {
    console.error('Failed to create board:', error)
    errorMessage.value = 'Failed to create board.'
  } finally {
    isSubmitting.value = false
  }
}

function resetForm(): void {
  Object.assign(form, initialFormState())
}

onMounted(() => {
  fetchBoards()
})
</script>

<template>
  <form @submit.prevent="submitForm">
    <div>
      <label for="title">Title</label>
      <input id="title" v-model="form.title" type="text" :disabled="isSubmitting" />
    </div>

    <div>
      <label for="description">Description</label>
      <input id="description" v-model="form.description" type="text" :disabled="isSubmitting" />
    </div>

    <button type="submit" :disabled="isSubmitting">
      {{ isSubmitting ? 'Submitting...' : 'Submit' }}
    </button>
    <button type="button" @click="resetForm" :disabled="isSubmitting">Reset</button>
  </form>

  <p v-if="errorMessage">{{ errorMessage }}</p>
  <p v-if="isLoading">Loading boards...</p>

  <template v-else>
    <p>Current boards:</p>
    <ul v-if="boards.length > 0">
      <li v-for="board in boards" :key="board.id">
        {{ board.id }}: <strong>{{ board.title }}</strong> — {{ board.description }}
      </li>
    </ul>
    <p v-else>No boards yet.</p>
  </template>
</template>

<style scoped></style>
