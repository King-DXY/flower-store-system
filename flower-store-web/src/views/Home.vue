<template>
  <div>
    <!-- 热门店铺 -->
    <h3>🏪 热门花店</h3>
    <el-row :gutter="15">
      <el-col :span="6" v-for="s in stores" :key="s.storeId">
        <el-card shadow="hover" @click="$router.push('/store/'+s.storeId)" style="cursor:pointer; margin-bottom:10px;">
          <div style="font-weight:bold;">{{ s.storeName }}</div>
          <div style="font-size:12px; color:#999;">{{ s.address }}</div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 搜索 -->
    <div style="margin:20px 0;">
      <el-input v-model="keyword" placeholder="搜索鲜花..." style="width:300px;" clearable @change="search" />
    </div>

    <!-- 鲜花列表 -->
    <h3>💐 鲜花列表</h3>
    <el-row :gutter="15">
      <el-col :span="6" v-for="f in flowers" :key="f.flowerId">
        <el-card shadow="hover" :body-style="{padding:'10px'}" style="margin-bottom:10px;">
          <div style="font-weight:bold;">{{ f.flowerName }}</div>
          <div style="color:#999; font-size:12px;">{{ f.flowerType }} | 库存: {{ f.stock }}</div>
          <div style="color:#e4393c; font-size:18px; margin:8px 0;">¥{{ f.unitPrice }}</div>
          <el-button v-if="userStore.isLoggedIn && userStore.role==='CUSTOMER'" size="small" type="danger"
                     @click="addCart(f.flowerId, f.storeId)">加入购物车</el-button>
        </el-card>
      </el-col>
    </el-row>

    <el-pagination v-model:current-page="page" :total="total" :page-size="10"
                   @current-change="load" layout="prev,pager,next" style="margin-top:20px; justify-content:center;" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '../store/user'
import api from '../api'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()
const stores = ref([])
const flowers = ref([])
const keyword = ref('')
const page = ref(1)
const total = ref(0)

onMounted(() => { loadStores(); load() })

async function loadStores() {
  const res = await api.get('/stores/top', { params: { n: 8 } })
  stores.value = res.data
}

async function load() {
  const res = await api.get('/flowers', { params: { page: page.value, size: 10, keyword: keyword.value } })
  flowers.value = res.data.list
  total.value = res.data.total
}

function search() { page.value = 1; load() }

async function addCart(flowerId, storeId) {
  await api.post('/cart/items', null, { params: { flowerId, storeId, quantity: 1 } })
  ElMessage.success('已加入购物车')
}
</script>
