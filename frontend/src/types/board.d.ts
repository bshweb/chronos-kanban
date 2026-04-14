export interface Board {
  id: string
  title: string
  description: string | null
  stages: Stage[]
}

export interface Stage {
  id: string
  title: string
  description: string | null
  tasks: Task[]
}

export interface Task {
  id: string
  title: string
  description: string | null
}
