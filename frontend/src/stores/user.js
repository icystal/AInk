import {defineStore} from "pinia";
import {ref} from "vue";

export const useUserStore = defineStore('user', () => {
  const email = ref(localStorage.getItem('email') || '')
  const token = ref(localStorage.getItem('token') || '')

  const isLogin = () => email.value && email.value !== '' && token.value && token.value !== ''

  const login = (userEmail, userToken) => {
    email.value = userEmail
    token.value = userToken

    localStorage.setItem('email', userEmail)
    localStorage.setItem('token', userToken)
  }

  const logout = () => {
    email.value = ''
    token.value = ''

    localStorage.removeItem('email')
    localStorage.removeItem('token')
  }

  const getEmail = () => {
    return email.value
  }

  const getToken = () => {
    return token.value
  }

  return {
    token,
    email,
    isLogin,
    login,
    logout,
    getEmail,
    getToken
  }

})
