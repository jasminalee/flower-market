<template>
  <div class="merchant-list">
    <h2 class="page-title">Merchant Management</h2>

    <!-- Search -->
    <el-card shadow="never" class="search-card">
      <el-form :inline="true">
        <el-form-item label="Store Name">
          <el-input v-model="searchForm.keyword" placeholder="Enter store name" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item label="Status">
          <el-select v-model="searchForm.status" placeholder="All" clearable style="width: 150px">
            <el-option label="Pending Review" value="PENDING" />
            <el-option label="Active" value="ACTIVE" />
            <el-option label="Rejected" value="REJECTED" />
            <el-option label="Suspended" value="SUSPENDED" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">Search</el-button>
          <el-button @click="handleReset">Reset</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- Merchants table -->
    <el-card shadow="never" class="table-card">
      <el-table :data="merchantList" v-loading="loading" style="width: 100%">
        <el-table-column label="Logo" width="80">
          <template #default="{ row }">
            <el-image :src="row.shopLogo || '/default-shop.png'" fit="cover" style="width: 50px; height: 50px; border-radius: 4px;" />
          </template>
        </el-table-column>
        <el-table-column prop="name" label="Store Name" min-width="160" />
        <el-table-column prop="name" label="Contact" width="120" />
        <el-table-column prop="phone" label="Phone" width="140" />
        <el-table-column prop="status" label="Status" width="140">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createDate" label="Registered At" width="160">
          <template #default="{ row }">
            {{ formatDate(row.createDate) }}
          </template>
        </el-table-column>
        <el-table-column label="Actions" width="340" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleView(row.merchId)">View</el-button>
            <el-button v-if="row.status === 'PENDING'" type="success" link @click="handleAudit(row.merchId, 'ACTIVE')">Approve</el-button>
            <el-button v-if="row.status === 'PENDING'" type="danger" link @click="handleAudit(row.merchId, 'REJECTED')">Reject</el-button>
            <el-button v-if="row.status === 'ACTIVE'" type="warning" link @click="handleToggleStatus(row.merchId, 'SUSPENDED')">Suspend</el-button>
            <el-button v-if="row.status === 'SUSPENDED'" type="success" link @click="handleToggleStatus(row.merchId, 'ACTIVE')">Enable</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- Pagination -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.pageSize"
          :total="pagination.total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @current-change="fetchMerchantList"
          @size-change="fetchMerchantList"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getMerchantList } from '@/api/merchant'
import { formatDate } from '@/utils/format'
import request from '@/utils/request'

const router = useRouter()
const loading = ref(false)
const merchantList = ref([])

const searchForm = reactive({
  keyword: '',
  status: ''
})

const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 0
})

// Fetch merchants
const fetchMerchantList = async () => {
  loading.value = true
  try {
    const params = {
      current: pagination.page,
      size: pagination.pageSize,
      status: searchForm.status || undefined
    }
    const { data } = await getMerchantList(params)
    // Backend returns IPage structure: records, total, size, current
    merchantList.value = data.records || []
    pagination.total = data.total || 0
  } catch (error) {
    ElMessage.error('Failed to load merchants')
  } finally {
    loading.value = false
  }
}

// Search
const handleSearch = () => {
  pagination.page = 1
  fetchMerchantList()
}

// Reset
const handleReset = () => {
  searchForm.keyword = ''
  searchForm.status = ''
  pagination.page = 1
  fetchMerchantList()
}

// View
const handleView = (id) => {
  router.push(`/admin/merchants/${id}`)
}

// Audit
const handleAudit = async (id, status) => {
  const actionText = status === 'ACTIVE' ? 'Approve' : 'Reject'
  try {
    await ElMessageBox.confirm(`Confirm to ${actionText.toLowerCase()} this merchant?`, 'Confirmation', {
      confirmButtonText: 'OK',
      cancelButtonText: 'Cancel',
      type: 'warning'
    })
    
    await request({
      url: `/api/admin/merchants/${id}/audit`,
      method: 'put',
      params: { status }  // Use params instead of data, parameters will be placed in the query string
    })
    
    ElMessage.success(`${actionText} succeeded`)
    fetchMerchantList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(`${actionText} failed`)
    }
  }
}

// Toggle status
const handleToggleStatus = async (id, status) => {
  const actionText = status === 'SUSPENDED' ? 'Suspend' : 'Enable'
  try {
    await ElMessageBox.confirm(`Confirm to ${actionText.toLowerCase()} this merchant?`, 'Confirmation', {
      confirmButtonText: 'OK',
      cancelButtonText: 'Cancel',
      type: 'warning'
    })
    
    await request({
      url: `/api/admin/merchants/${id}/audit`,
      method: 'put',
      params: { status }
    })
    
    ElMessage.success(`${actionText} succeeded`)
    fetchMerchantList()
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
  fetchMerchantList()
})
</script>

<style scoped>
.merchant-list {
  padding: 20px;
}

.page-title {
  margin: 0 0 20px;
  font-size: 24px;
  font-weight: 500;
  color: #303133;
}

.search-card {
  margin-bottom: 20px;
}

.table-card {
  margin-bottom: 20px;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>
