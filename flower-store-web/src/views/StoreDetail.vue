<template>
  <div>
    <el-button @click="$router.push('/')" style="margin-bottom:15px;">← 返回</el-button>

    <h3>🏪 {{ store?.storeName }} 的鲜花</h3>
    <p style="color:#999;">{{ store?.address }} | 📞 {{ store?.contactPhone }}</p>

    <el-row :gutter="15">
      <el-col :span="6" v-for="f in flowers" :key="f.flowerId">
        <el-card shadow="hover" :body-style="{padding:'10px'}" style="margin-bottom:10px;">
          <div style="font-weight:bold;">{{ f.flowerName }}</div>
          <div style="color:#999; font-size:12px;">{{ f.flowerType }} | {{ f.flowerMeaning || '' }}</div>
          <div style="color:#e4393c; font-size:18px; margin:8px 0;">¥{{ f.unitPrice }}</div>
          <div style="color:#999; font-size:12px;">库存: {{ f.stock }}</div>
          <el-button v-if="userStore.isLoggedIn && userStore.role==='CUSTOMER'" size="small" type="danger"
                     @click="addCart(f.flowerId, store.storeId)" style="margin-top:5px;">加入购物车</el-button>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useUserStore } from '../store/user'
import api from '../api'
import { ElMessage } from 'element-plus'

const route = useRoute()
const userStore = useUserStore()
const store = ref(null)
const flowers = ref([])

onMounted(async () => {
  const id = route.params.id
  store.value = (await api.get('/stores/' + id)).data
  flowers.value = (await api.get('/stores/' + id + '/flowers')).data
})

async function addCart(flowerId, storeId) {
  await api.post('/cart/items', null, { params: { flowerId, storeId, quantity: 1 } })
  ElMessage.success('已加入购物车')
}
</script>
