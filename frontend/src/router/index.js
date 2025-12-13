import { createRouter, createWebHistory } from 'vue-router'
import UserLogin from "@/views/UserLogin.vue";
import Outline from "@/views/Outline.vue";

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: UserLogin
  },
  {
    path: '/outline',
    name: 'Outline',
    component: Outline
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
