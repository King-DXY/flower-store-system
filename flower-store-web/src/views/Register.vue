<template>
  <div style="max-width:400px; margin:40px auto;">
    <h2 style="text-align:center;">注册</h2>

    <el-radio-group v-model="role" style="margin:20px 0; display:flex; justify-content:center;">
      <el-radio-button value="CUSTOMER">顾客注册</el-radio-button>
      <el-radio-button value="STORE">花店注册</el-radio-button>
    </el-radio-group>

    <el-input v-model="form.name" :placeholder="role==='CUSTOMER'?'姓名':'店铺名'" style="margin-bottom:12px;" />
    <el-input v-model="form.phone" placeholder="手机号" style="margin-bottom:12px;" />
    <el-input v-if="role==='CUSTOMER'" v-model="form.email" placeholder="邮箱（选填）" style="margin-bottom:12px;" />
    <el-input v-if="role==='STORE'" v-model="form.address" placeholder="店铺地址" style="margin-bottom:12px;" />
    <el-input v-model="form.password" type="password" placeholder="密码（至少6位）" show-password style="margin-bottom:20px;" />

    <el-button type="primary" @click="register" :loading="loading" style="width:100%;">注册</el-button>

    <div style="text-align:center; margin-top:15px;">
      <router-link to="/login">已有账号？去登录</router-link>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../store/user'
import api from '../api'
import { ElMessage } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()
const role = ref('CUSTOMER')
const loading = ref(false)
const form = reactive({ name: '', phone: '', email: '', address: '', password: '' })

async function register() {
  if (!form.name || !form.phone || !form.password) {
    return ElMessage.warning('请填写必填项')
  }
  loading.value = true
  try {
    const path = role.value === 'CUSTOMER' ? '/auth/customer/register' : '/auth/store/register'
    const res = await api.post(path, { ...form })
    userStore.setLogin(res.data)
    ElMessage.success('注册成功')
    router.push(role.value === 'CUSTOMER' ? '/' : '/dashboard')
  } catch (e) { /* 拦截器已提示 */ }
  finally { loading.value = false }
}
</script>
