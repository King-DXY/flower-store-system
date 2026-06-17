<template>
  <div>
    <h3>📦 我的订单</h3>

    <el-table :data="orders" v-if="orders.length">
      <el-table-column prop="orderId" label="订单号" width="80" />
      <el-table-column prop="totalAmount" label="总金额" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="['info','warning','primary','success'][row.orderStatus]">
            {{ ['待支付','已支付','已发货','已完成'][row.orderStatus] }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="paymentMethod" label="支付方式" />
      <el-table-column prop="orderTime" label="下单时间">
        <template #default="{ row }">{{ row.orderTime?.substring(0,16) }}</template>
      </el-table-column>
      <el-table-column label="操作" v-if="userStore.role==='CUSTOMER'">
        <template #default="{ row }">
          <el-button v-if="row.orderStatus===0" size="small" type="warning" @click="pay(row.orderId)">支付</el-button>
          <span v-else style="color:#999;">-</span>
        </template>
      </el-table-column>
    </el-table>

    <el-empty v-else description="暂无订单" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '../store/user'
import api from '../api'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()
const orders = ref([])

onMounted(async () => {
  const res = await api.get('/orders/my')
  orders.value = res.data || []
})

async function pay(id) {
  await api.put('/orders/' + id + '/pay')
  ElMessage.success('支付成功')
  // 刷新
  const res = await api.get('/orders/my')
  orders.value = res.data || []
}
</script>
