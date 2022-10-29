import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'
import { useUserStore } from "@/store/user";

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'ground',
    component: () => import('@/views/ground/index.vue')
  }, {
    path: '/login',
    name: 'login',
    component: () => import('@/views/login/index.vue')
  }, {
    path: '/user',
    name: 'user',
    component: () => import('@/views/user/index.vue')
  }, {
    path: '/canvas',
    name: 'canvas',
    component: () => import('@/views/canvas/index.vue')
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

router.beforeEach(async (to, from) => {
  if (
    // 检查用户是否已登录
    !useUserStore().hasLogin &&
    to.name !== 'ground'
  ) {
    // 将用户重定向到登录页面
    return { name: 'ground' }
  }
})

export default router
