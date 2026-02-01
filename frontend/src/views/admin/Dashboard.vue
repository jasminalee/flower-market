<template>
  <div class="admin-dashboard">
    <h2 class="page-title">Admin Dashboard</h2>

    <!-- Data Overview Cards -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-info">
              <div class="stat-label">Total Users</div>
              <div class="stat-value">{{ stats.totalUsers }}</div>
            </div>
            <el-icon class="stat-icon" color="#409eff"><User /></el-icon>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-info">
              <div class="stat-label">Total Merchants</div>
              <div class="stat-value">{{ stats.totalMerchants }}</div>
            </div>
            <el-icon class="stat-icon" color="#67c23a"><Shop /></el-icon>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-info">
              <div class="stat-label">Total Orders</div>
              <div class="stat-value">{{ stats.totalOrders }}</div>
            </div>
            <el-icon class="stat-icon" color="#e6a23c"><ShoppingCart /></el-icon>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-info">
              <div class="stat-label">Total Sales</div>
              <div class="stat-value">¥{{ stats.totalSales }}</div>
            </div>
            <el-icon class="stat-icon" color="#f56c6c"><Money /></el-icon>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="content-row">
      <!-- Recent Registered Users -->
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>Recent Registered Users</span>
              <el-button type="primary" link @click="$router.push('/admin/customers')">View All</el-button>
            </div>
          </template>
          <el-table :data="recentUsers" style="width: 100%" v-loading="loading">
            <el-table-column prop="name" label="Username" />
            <el-table-column prop="email" label="Email" />
            <el-table-column prop="phone" label="Phone" width="120" />
            <el-table-column prop="createDate" label="Registration Time" width="160">
              <template #default="{ row }">
                {{ formatDate(row.createDate) }}
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>

      <!-- Recent Registered Merchants -->
      <el-col :span="12">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>Recent Registered Merchants</span>
              <el-button type="primary" link @click="$router.push('/admin/merchants')">View All</el-button>
            </div>
          </template>
          <el-table :data="recentMerchants" style="width: 100%" v-loading="loading">
            <el-table-column prop="name" label="Store Name" />
            <el-table-column prop="phone" label="Contact Phone" width="120" />
            <el-table-column prop="status" label="Status" width="80">
              <template #default="{ row }">
                <el-tag :type="getMerchantStatusType(row.status)">{{ getMerchantStatusText(row.status) }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createDate" label="Registration Time" width="160">
              <template #default="{ row }">
                {{ formatDate(row.createDate) }}
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <!-- Order Trend Chart -->
    <el-row :gutter="20" class="chart-row">
      <el-col :span="24">
        <el-card shadow="hover">
          <template #header>
            <div class="card-header">
              <span>Order Trend (Last 7 Days)</span>
            </div>
          </template>
          <div class="chart-container" v-loading="loading">
            <div class="chart-item" v-for="(item, index) in orderTrend" :key="index">
              <div class="chart-label">{{ item.date }}</div>
              <div class="chart-bar">
                <div class="chart-bar-fill" :style="{ width: (item.count / maxOrderCount * 100) + '%' }"></div>
              </div>
              <div class="chart-value">{{ item.count }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'
import { formatDate } from '@/utils/format'

const loading = ref(false)
const stats = ref({
  totalUsers: 0,
  totalMerchants: 0,
  totalOrders: 0,
  totalSales: 0
})
const recentUsers = ref([])
const recentMerchants = ref([])
const orderTrend = ref([])

// Calculate Maximum Order Count
const maxOrderCount = computed(() => {
  if (orderTrend.value.length === 0) return 0
  return Math.max(...orderTrend.value.map(item => item.count))
})

// Fetch Dashboard Data
const fetchDashboardData = async () => {
  loading.value = true
  try {
    const { data } = await request({
      url: '/api/admin/dashboard',
      method: 'get'
    })
    stats.value = data.stats
    recentUsers.value = data.recentUsers
    recentMerchants.value = data.recentMerchants
    orderTrend.value = data.orderTrend
  } catch (error) {
    ElMessage.error('Failed to load data')
  } finally {
    loading.value = false
  }
}

// Merchant Status Type
const getMerchantStatusType = (status) => {
  const map = {
    PENDING: 'warning',
    ACTIVE: 'success',
    REJECTED: 'danger',
    SUSPENDED: 'info'
  }
  return map[status] || 'info'
}

// Merchant Status Text
const getMerchantStatusText = (status) => {
  const map = {
    PENDING: 'Pending',
    ACTIVE: 'Active',
    REJECTED: 'Rejected',
    SUSPENDED: 'Suspended'
  }
  return map[status] || 'Unknown'
}

onMounted(() => {
  fetchDashboardData()
})
</script>

<style scoped>
.admin-dashboard {
  padding: 20px;
}

.page-title {
  margin: 0 0 20px;
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
  opacity: 0.8;
}

.content-row {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.chart-row {
  margin-bottom: 20px;
}

.chart-container {
  padding: 20px 0;
}

.chart-item {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.chart-label {
  width: 80px;
  font-size: 14px;
  color: #606266;
}

.chart-bar {
  flex: 1;
  height: 24px;
  background: #f5f7fa;
  border-radius: 4px;
  overflow: hidden;
  margin: 0 15px;
}

.chart-bar-fill {
  height: 100%;
  background: linear-gradient(90deg, #409eff, #66b1ff);
  transition: width 0.3s;
}

.chart-value {
  width: 50px;
  text-align: right;
  font-size: 14px;
  font-weight: bold;
  color: #303133;
}
</style>
