import { createRouter, createWebHistory } from 'vue-router'
import { ElMessage } from 'element-plus'

const routes = [
  { path: '/', name: 'Home', component: () => import('../views/Home.vue') },
  { path: '/login', name: 'Login', component: () => import('../views/Login.vue') },
  { path: '/register', name: 'Register', component: () => import('../views/Register.vue') },
  { path: '/store/:id', name: 'StoreDetail', component: () => import('../views/StoreDetail.vue') },
  { path: '/cart', name: 'Cart', component: () => import('../views/Cart.vue') },
  { path: '/orders', name: 'Orders', component: () => import('../views/Orders.vue') },
  { path: '/dashboard', name: 'Dashboard', component: () => import('../views/Dashboard.vue') },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫：需要登录的页面自动跳转登录
router.beforeEach((to, from, next) => {
  const needsAuth = ['/cart', '/orders', '/dashboard']
  const token = localStorage.getItem('token')
  if (needsAuth.includes(to.path) && !token) {
    ElMessage.warning('请先登录')
    next('/login')
  } else {
    next()
  }
})

export default router
