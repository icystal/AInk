import { createRouter, createWebHistory } from 'vue-router'
import UserLogin from "@/views/UserLogin.vue";

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: UserLogin
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
