<template>
  <div class="merchant-detail">
    <el-page-header @back="$router.back()" content="Merchant Details" />

    <div class="content" v-loading="loading">
      <!-- Basic info -->
      <el-card shadow="never" class="info-card">
        <template #header>
          <span class="card-title">Basic Info</span>
        </template>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="Store Logo">
            <el-image :src="merchant.shopLogo || '/default-shop.png'" fit="cover" style="width: 80px; height: 80px; border-radius: 4px;" />
          </el-descriptions-item>
          <el-descriptions-item label="Status">
            <el-tag :type="getStatusType(merchant.status)">{{ getStatusText(merchant.status) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="Store Name">{{ merchant.name }}</el-descriptions-item>
          <el-descriptions-item label="Merchant ID">{{ merchant.merchId }}</el-descriptions-item>
          <el-descriptions-item label="Phone">{{ merchant.phone }}</el-descriptions-item>
          <el-descriptions-item label="Email">{{ merchant.email || '-' }}</el-descriptions-item>
          <el-descriptions-item label="Registered At" :span="2">{{ formatDate(merchant.createDate) }}</el-descriptions-item>
        </el-descriptions>
      </el-card>

      <!-- Store info -->
      <el-card shadow="never" class="info-card">
        <template #header>
          <span class="card-title">Store Info</span>
        </template>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="Description" :span="2">{{ merchant.description || '-' }}</el-descriptions-item>
          <el-descriptions-item label="Address" :span="2">{{ merchant.address || '-' }}</el-descriptions-item>
          <el-descriptions-item label="Qualification" :span="2">
            <el-image v-if="merchant.qualification" :src="merchant.qualification" fit="cover" style="width: 100px; height: 100px;" :preview-src-list="[merchant.qualification]" />
            <span v-else>-</span>
          </el-descriptions-item>
        </el-descriptions>
      </el-card>

      <!-- Business info -->
      <el-card shadow="never" class="info-card">
        <template #header>
          <span class="card-title">Business Info</span>
        </template>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="Updated At" :span="2">{{ formatDate(merchant.updateDate) }}</el-descriptions-item>
        </el-descriptions>
      </el-card>

      <!-- Stats -->
      <el-row :gutter="20" class="stats-row">
        <el-col :span="12">
          <el-card shadow="never">
            <template #header>
              <span class="card-title">Product Stats</span>
            </template>
            <el-descriptions :column="1" border>
              <el-descriptions-item label="Active Products">{{ stats.activeProducts || 0 }}</el-descriptions-item>
              <el-descriptions-item label="Inactive Products">{{ stats.inactiveProducts || 0 }}</el-descriptions-item>
              <el-descriptions-item label="Total Products">{{ stats.totalProducts || 0 }}</el-descriptions-item>
            </el-descriptions>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card shadow="never">
            <template #header>
              <span class="card-title">Order Stats</span>
            </template>
            <el-descriptions :column="1" border>
              <el-descriptions-item label="Total Orders">{{ stats.totalOrders || 0 }}</el-descriptions-item>
              <el-descriptions-item label="Completed Orders">{{ stats.completedOrders || 0 }}</el-descriptions-item>
              <el-descriptions-item label="Total Sales">¥{{ stats.totalSales || 0 }}</el-descriptions-item>
            </el-descriptions>
          </el-card>
        </el-col>
      </el-row>

      <!-- Audit actions -->
      <el-card shadow="never" class="action-card" v-if="merchant.status === 'PENDING'">
        <template #header>
          <span class="card-title">Audit</span>
        </template>
        <el-form :model="auditForm" label-width="100px">
          <el-form-item label="Remark">
            <el-input v-model="auditForm.remark" type="textarea" :rows="4" placeholder="Enter remark (optional)" />
          </el-form-item>
          <el-form-item>
            <el-button type="success" @click="handleAudit('ACTIVE')">Approve</el-button>
            <el-button type="danger" @click="handleAudit('REJECTED')">Reject</el-button>
            <el-button @click="$router.back()">Back</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getMerchantDetail } from '@/api/merchant'
import { formatDate } from '@/utils/format'
import request from '@/utils/request'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const merchant = ref({})
const stats = ref({})

const auditForm = reactive({
  remark: ''
})

// Fetch merchant details
const fetchMerchantDetail = async () => {
  loading.value = true
  try {
    const { data } = await getMerchantDetail(route.params.id)
    merchant.value = data || {}
    stats.value = {}
    // TODO: If stats are needed, add an API endpoint.
  } catch (error) {
    ElMessage.error('Failed to load merchant details')
  } finally {
    loading.value = false
  }
}

// Audit
const handleAudit = async (status) => {
  const actionText = status === 'ACTIVE' ? 'Approve' : 'Reject'
  try {
    await ElMessageBox.confirm(`Confirm to ${actionText.toLowerCase()} this merchant?`, 'Confirmation', {
      confirmButtonText: 'OK',
      cancelButtonText: 'Cancel',
      type: 'warning'
    })
    
    await request({
      url: `/api/admin/merchants/${route.params.id}/verify`,
      method: 'put',
      params: { 
        status
      }
    })
    
    ElMessage.success(`${actionText} succeeded`)
    router.back()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(`${actionText} failed`)
    }
  }
}

// Status tag type
const getStatusType = (status) => {
  const map = {
    PENDING: 'warning',
    ACTIVE: 'success',
    REJECTED: 'danger',
    SUSPENDED: 'info'
  }
  return map[status] || 'info'
}

// Status text
const getStatusText = (status) => {
  const map = {
    PENDING: 'Pending Review',
    ACTIVE: 'Active',
    REJECTED: 'Rejected',
    SUSPENDED: 'Suspended'
  }
  return map[status] || 'Unknown'
}

onMounted(() => {
  fetchMerchantDetail()
})
</script>

<style scoped>
.merchant-detail {
  padding: 20px;
}

.content {
  margin-top: 20px;
}

.info-card {
  margin-bottom: 20px;
}

.card-title {
  font-weight: 500;
  font-size: 16px;
}

.stats-row {
  margin-bottom: 20px;
}

.action-card {
  margin-bottom: 20px;
}
</style>
