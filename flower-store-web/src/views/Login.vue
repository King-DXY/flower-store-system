<template>
  <div style="max-width:400px; margin:60px auto;">
    <h2 style="text-align:center;">登录鲜花商城</h2>

    <el-radio-group v-model="role" style="margin:20px 0; display:flex; justify-content:center;">
      <el-radio-button value="CUSTOMER">我是顾客</el-radio-button>
      <el-radio-button value="STORE">我是花店</el-radio-button>
    </el-radio-group>

    <el-input v-model="account" :placeholder="role==='CUSTOMER'?'手机号':'店铺名'" style="margin-bottom:15px;" />
    <el-input v-model="password" type="password" placeholder="密码" show-password style="margin-bottom:20px;" />

    <el-button type="primary" @click="login" :loading="loading" style="width:100%;">登录</el-button>

    <div style="text-align:center; margin-top:15px;">
      <router-link to="/register">还没有账号？去注册</router-link>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../store/user'
import api from '../api'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const role = ref('CUSTOMER')
const account = ref('')
const password = ref('')
const loading = ref(false)

async function login() {
  if (!account.value || !password.value) {
    return ElMessage.warning('请填写完整')
  }
  loading.value = true
  try {
    const path = role.value === 'CUSTOMER' ? '/auth/customer/login' : '/auth/store/login'
    const res = await api.post(path, { account: account.value, password: password.value })
    userStore.setLogin(res.data)
    ElMessage.success('登录成功')
    router.push(role.value === 'CUSTOMER' ? '/' : '/dashboard')
  } catch (e) { /* 拦截器已提示 */ }
  finally { loading.value = false }
}
</script>
