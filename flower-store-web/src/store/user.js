import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

// 用户状态管理（Pinia）
export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userId = ref(localStorage.getItem('userId') || '')
  const name = ref(localStorage.getItem('name') || '')
  const role = ref(localStorage.getItem('role') || '')

  const isLoggedIn = computed(() => !!token.value)

  function setLogin(data) {
    token.value = data.token
    userId.value = data.userId
    name.value = data.name
    role.value = data.role
    localStorage.setItem('token', data.token)
    localStorage.setItem('userId', data.userId)
    localStorage.setItem('name', data.name)
    localStorage.setItem('role', data.role)
  }

  function restoreLogin() {
    token.value = localStorage.getItem('token') || ''
    userId.value = localStorage.getItem('userId') || ''
    name.value = localStorage.getItem('name') || ''
    role.value = localStorage.getItem('role') || ''
  }

  function logout() {
    token.value = ''
    userId.value = ''
    name.value = ''
    role.value = ''
    localStorage.clear()
  }

  return { token, userId, name, role, isLoggedIn, setLogin, restoreLogin, logout }
})
