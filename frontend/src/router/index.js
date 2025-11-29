import { createRouter, createWebHistory } from 'vue-router'
import UserLogin from "@/views/UserLogin.vue";
import Step from "@/views/Step.vue";

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: UserLogin
  },
  {
    path: '/step',
    name: 'Step',
    component: Step
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
