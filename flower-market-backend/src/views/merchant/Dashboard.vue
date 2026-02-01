<template>
  <div class="merchant-dashboard">
    <h2 class="page-title">Dashboard</h2>

    <!-- Overview cards -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-info">
              <div class="stat-label">Orders Today</div>
              <div class="stat-value">{{ stats.todayOrders }}</div>
            </div>
            <el-icon class="stat-icon" color="#409eff"><ShoppingCart /></el-icon>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-info">
              <div class="stat-label">Sales Today</div>
              <div class="stat-value">¥{{ stats.todaySales }}</div>
            </div>
            <el-icon class="stat-icon" color="#67c23a"><Money /></el-icon>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-info">
              <div class="stat-label">Orders This Week</div>
              <div class="stat-value">{{ stats.weekOrders }}</div>
            </div>
            <el-icon class="stat-icon" color="#e6a23c"><DataLine /></el-icon>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-info">
              <div class="stat-label">Sales This Month</div>
              <div class="stat-value">¥{{ stats.monthSales }}</div>
            </div>
            <el-icon class="stat-icon" color="#f56c6c"><TrendCharts /></el-icon>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="content-row">
      <!-- Recent orders -->
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>Recent Orders</span>
              <el-button type="primary" link @click="$router.push('/merchant/orders')">View All</el-button>
            </div>
          </template>
          <el-table :data="recentOrders" style="width: 100%" v-loading="loading">
            <el-table-column prop="orderNo" label="Order No" width="140" />
            <el-table-column prop="customerName" label="Customer" width="100" />
            <el-table-column prop="totalAmount" label="Amount" width="100">
              <template #default="{ row }">
                ¥{{ row.totalAmount }}
              </template>
            </el-table-column>
            <el-table-column prop="status" label="Status" width="100">
              <template #default="{ row }">
                <el-tag :type="getStatusType(row.status)">{{ row.statusText }}</el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>

      <!-- Top products -->
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>Top Products</span>
              <el-button type="primary" link @click="$router.push('/merchant/products')">View All</el-button>
            </div>
          </template>
          <el-table :data="topProducts" style="width: 100%" v-loading="loading">
            <el-table-column prop="name" label="Product Name" />
            <el-table-column prop="sales" label="Sales" width="100" />
            <el-table-column prop="revenue" label="Revenue" width="120">
              <template #default="{ row }">
                ¥{{ row.revenue }}
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <!-- Sales trend -->
    <el-row class="content-row">
      <el-col :span="24">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>Sales Trend (Last 7 Days)</span>
            </div>
          </template>
          <div class="chart-container">
            <div class="simple-chart">
              <div v-for="(item, index) in salesTrend" :key="index" class="chart-bar">
                <div class="bar" :style="{ height: item.percentage + '%' }"></div>
                <div class="bar-label">{{ item.date }}</div>
                <div class="bar-value">¥{{ item.amount }}</div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { getMerchantDashboard } from '@/api/merchant'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'
import { ShoppingCart, Money, DataLine, TrendCharts } from '@element-plus/icons-vue'

const userStore = useUserStore()
const merchId = computed(() => userStore.userInfo?.merchId)

const loading = ref(false)
const stats = ref({
  todayOrders: 0,
  todaySales: 0,
  weekOrders: 0,
  monthSales: 0
})
const recentOrders = ref([])
const topProducts = ref([])
const salesTrend = ref([])

const getStatusType = (status) => {
  const types = {
    SUBMITTED: 'warning',
    PAID: 'info',
    SHIPPED: 'primary',
    COMPLETED: 'success',
    CANCELLED: 'danger'
  }
  return types[status] || 'info'
}

const fetchDashboardData = async () => {
  if (!merchId.value) {
    ElMessage.error('Merchant information not available')
    return
  }
  
  loading.value = true
  try {
    const res = await getMerchantDashboard(merchId.value)
    if (res.data) {
      stats.value = res.data.stats || stats.value
      recentOrders.value = res.data.recentOrders || []
      topProducts.value = res.data.topProducts || []
      
      // Process sales trend data
      const trend = res.data.salesTrend || []
      const maxAmount = Math.max(...trend.map(t => t.amount), 1)
      salesTrend.value = trend.map(item => ({
        ...item,
        percentage: (item.amount / maxAmount) * 100
      }))
    }
  } catch (error) {
    console.error('Failed to load dashboard data:', error)
    ElMessage.error('Failed to load data')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchDashboardData()
})
</script>

<style scoped>
.merchant-dashboard {
  padding: 20px;
}

.page-title {
  margin: 0 0 20px 0;
  font-size: 24px;
  font-weight: 500;
  color: #303133;
}

.stats-row {
  margin-bottom: 20px;
}

.stat-card {
  cursor: pointer;
  transition: transform 0.3s;
}

.stat-card:hover {
  transform: translateY(-5px);
}

.stat-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.stat-info {
  flex: 1;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 10px;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
}

.stat-icon {
  font-size: 48px;
  opacity: 0.3;
}

.content-row {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chart-container {
  padding: 20px 0;
}

.simple-chart {
  display: flex;
  justify-content: space-around;
  align-items: flex-end;
  height: 250px;
  padding: 0 20px;
}

.chart-bar {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-end;
  margin: 0 10px;
}

.bar {
  width: 100%;
  max-width: 60px;
  background: linear-gradient(to top, #409eff, #79bbff);
  border-radius: 4px 4px 0 0;
  transition: all 0.3s;
  min-height: 20px;
}

.bar:hover {
  background: linear-gradient(to top, #66b1ff, #409eff);
}

.bar-label {
  margin-top: 10px;
  font-size: 12px;
  color: #909399;
}

.bar-value {
  margin-top: 5px;
  font-size: 14px;
  font-weight: bold;
  color: #303133;
}
</style>

