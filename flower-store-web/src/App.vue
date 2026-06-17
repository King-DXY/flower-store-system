<template>
  <div id="app-container">
    <!-- 顶部导航栏 -->
    <el-menu mode="horizontal" :ellipsis="false" router>
      <el-menu-item index="/">
        <el-icon><Shop /></el-icon>
        <span>鲜花商城</span>
      </el-menu-item>

      <div style="flex-grow:1"></div>

      <!-- 未登录 -->
      <template v-if="!userStore.isLoggedIn">
        <el-menu-item index="/login">登录</el-menu-item>
        <el-menu-item index="/register">注册</el-menu-item>
      </template>

      <!-- 已登录 -->
      <template v-else>
        <template v-if="userStore.role === 'CUSTOMER'">
          <el-menu-item index="/cart">
            <el-icon><ShoppingCart /></el-icon>购物车
          </el-menu-item>
          <el-menu-item index="/orders">我的订单</el-menu-item>
        </template>
        <template v-else>
          <el-menu-item index="/dashboard">店铺管理</el-menu-item>
        </template>
        <el-menu-item @click="logout">退出({{ userStore.name }})</el-menu-item>
      </template>
    </el-menu>

    <!-- 页面内容区 -->
    <div style="padding:20px; max-width:1200px; margin:0 auto;">
      <router-view />
    </div>
  </div>
</template>

<script setup>
import { useUserStore } from './store/user'
import { useRouter } from 'vue-router'

const userStore = useUserStore()
const router = useRouter()

function logout() {
  userStore.logout()
  router.push('/login')
}
</script>
