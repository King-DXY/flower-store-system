<template>
  <div>
    <h3>🏬 店铺管理后台</h3>

    <el-tabs v-model="activeTab">
      <!-- 库存管理 -->
      <el-tab-pane label="我的库存" name="stock">
        <el-button type="primary" @click="dialogCultivate = true">培育新品种</el-button>
        <el-table :data="stock" style="margin-top:10px;">
          <el-table-column prop="flowerId" label="ID" width="60" />
          <el-table-column prop="flowerName" label="名称" />
          <el-table-column prop="flowerType" label="类型" />
          <el-table-column prop="unitPrice" label="售价" />
          <el-table-column prop="costPrice" label="成本价" />
          <el-table-column prop="stock" label="库存" />
          <el-table-column label="操作">
            <template #default="{ row }">
              <el-input-number v-model="qty[row.flowerId]" :min="1" size="small" style="width:80px;" />
              <el-button size="small" @click="stockIn(row.flowerId)" style="margin-left:5px;">入库</el-button>
              <el-button size="small" type="danger" @click="stockOut(row.flowerId)">出库</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-tab-pane>

      <!-- 销售统计 -->
      <el-tab-pane label="销售统计" name="sales">
        <el-descriptions v-if="stats" :column="3" border>
          <el-descriptions-item label="总订单">{{ stats.totalOrders }}</el-descriptions-item>
          <el-descriptions-item label="总销售额">¥{{ stats.totalRevenue }}</el-descriptions-item>
          <el-descriptions-item label="总利润">¥{{ stats.totalProfit }}</el-descriptions-item>
        </el-descriptions>
        <el-table :data="stats?.salesList||[]" style="margin-top:10px;">
          <el-table-column prop="orderId" label="订单号" />
          <el-table-column prop="flowerName" label="鲜花" />
          <el-table-column prop="revenue" label="收入" />
          <el-table-column prop="profit" label="利润" />
          <el-table-column prop="orderTime" label="时间" />
        </el-table>
      </el-tab-pane>
    </el-tabs>

    <!-- 培育新品种弹窗 -->
    <el-dialog v-model="dialogCultivate" title="培育新品种" width="400px">
      <el-input v-model="form.flowerName" placeholder="鲜花名称" style="margin-bottom:10px;" />
      <el-input v-model="form.flowerType" placeholder="鲜花类型" style="margin-bottom:10px;" />
      <el-input v-model="form.unitPrice" placeholder="售价" style="margin-bottom:10px;" />
      <el-input v-model="form.costPrice" placeholder="成本价" style="margin-bottom:10px;" />
      <el-input v-model="form.flowerMeaning" placeholder="花语（选填）" style="margin-bottom:10px;" />
      <el-input-number v-model="form.quantity" :min="1" placeholder="库存数量" />
      <template #footer>
        <el-button @click="dialogCultivate = false">取消</el-button>
        <el-button type="primary" @click="cultivate">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import api from '../api'
import { ElMessage } from 'element-plus'

const activeTab = ref('stock')
const dialogCultivate = ref(false)
const stock = ref([])
const stats = ref(null)
const qty = reactive({})

const form = reactive({ flowerName:'', flowerType:'', unitPrice:'', costPrice:'', flowerMeaning:'', quantity: 10 })

onMounted(() => { loadStock(); loadSales() })

async function loadStock() {
  const res = await api.get('/store/manage/stock')
  stock.value = res.data || []
}

async function loadSales() {
  const res = await api.get('/store/manage/sales')
  stats.value = res.data
}

async function stockIn(flowerId) {
  const q = qty[flowerId] || 1
  await api.post('/store/manage/stock/in', null, { params: { flowerId, quantity: q } })
  ElMessage.success('入库成功')
  loadStock()
}

async function stockOut(flowerId) {
  const q = qty[flowerId] || 1
  await api.post('/store/manage/stock/out', null, { params: { flowerId, quantity: q } })
  ElMessage.success('出库成功')
  loadStock()
}

async function cultivate() {
  await api.post('/store/manage/cultivate', null, { params: form })
  ElMessage.success('培育成功')
  dialogCultivate.value = false
  loadStock()
}
</script>
