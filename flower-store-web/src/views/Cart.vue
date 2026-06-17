<template>
  <div>
    <h3>🛒 我的购物车</h3>

    <el-table :data="items" v-if="items.length">
      <el-table-column prop="flowerName" label="鲜花" />
      <el-table-column prop="unitPrice" label="单价" />
      <el-table-column label="数量">
        <template #default="{ row }">
          <el-input-number v-model="row.quantity" :min="1" :max="row.stock" size="small"
                           @change="updateQty(row.flowerId, row.quantity)" />
        </template>
      </el-table-column>
      <el-table-column prop="subtotal" label="小计" />
      <el-table-column label="操作">
        <template #default="{ row }">
          <el-button size="small" type="danger" @click="remove(row.flowerId)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-empty v-else description="购物车空空如也" />

    <div v-if="items.length" style="margin-top:20px; text-align:right;">
      <el-input v-model="address" placeholder="收货地址" style="width:250px; margin-right:10px;" />
      <el-select v-model="payMethod" placeholder="支付方式" style="width:150px; margin-right:10px;">
        <el-option label="微信支付" value="微信支付" />
        <el-option label="支付宝" value="支付宝" />
        <el-option label="货到付款" value="货到付款" />
      </el-select>
      <el-button type="success" @click="checkout" :disabled="!address">下单结算</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '../api'
import { ElMessage } from 'element-plus'

const items = ref([])
const address = ref('')
const payMethod = ref('微信支付')

onMounted(load)

async function load() {
  const res = await api.get('/cart')
  items.value = res.data || []
}

async function updateQty(flowerId, qty) {
  await api.put('/cart/items/' + flowerId, null, { params: { quantity: qty } })
  load()
}

async function remove(flowerId) {
  await api.delete('/cart/items/' + flowerId)
  load()
}

async function checkout() {
  await api.post('/orders', null, {
    params: { address: address.value, paymentMethod: payMethod.value, shippingAddress: address.value }
  })
  ElMessage.success('下单成功！')
  load()
}
</script>
