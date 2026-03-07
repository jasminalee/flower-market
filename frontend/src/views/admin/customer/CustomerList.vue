<template>
  <div class="customer-list">
    <div class="page-header">
      <h2>Customer Management</h2>
    </div>

    <!-- Search and Filter -->
    <el-card class="search-card">
      <el-form :inline="true">
        <el-form-item label="Keyword">
          <el-input
            v-model="searchForm.keyword"
            placeholder="Search by username, email, phone"
            clearable
            style="width: 250px"
            @keyup.enter="handleSearch"
          />
        </el-form-item>
        <el-form-item label="Membership Level">
          <el-select v-model="searchForm.level" placeholder="All" clearable style="width: 120px">
            <el-option label="All" value="" />
            <el-option label="Regular Member" value="NORMAL" />
            <el-option label="VIP" value="VIP" />
            <el-option label="SVIP" value="SVIP" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">Search</el-button>
          <el-button @click="handleReset">Reset</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- User List -->
    <el-card class="table-card">
      <el-table :data="customerList" v-loading="loading" style="width: 100%">
        <el-table-column type="index" label="#" width="60" />
        <el-table-column prop="name" label="Username" width="150" />
        <el-table-column prop="email" label="Email" width="200" />
        <el-table-column prop="phone" label="Phone Number" width="130" />
        <el-table-column prop="level" label="Membership Level" width="100">
          <template #default="{ row }">
            <el-tag :type="getLevelType(row.level)">{{ getLevelText(row.level) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="balance" label="Account Balance" width="120">
          <template #default="{ row }">
            ¥{{ row.balance || 0 }}
          </template>
        </el-table-column>
        <el-table-column prop="emailVerified" label="Email Verification" width="100">
          <template #default="{ row }">
            <el-tag :type="row.emailVerified ? 'success' : 'info'">
              {{ row.emailVerified ? 'Verified' : 'Not Verified' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createDate" label="Registration Time" width="160">
          <template #default="{ row }">
            {{ formatDate(row.createDate) }}
          </template>
        </el-table-column>
        <el-table-column label="Action" width="180" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link icon="View" @click="handleViewDetail(row)">
              View Details
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- Pagination -->
      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="pagination.current"
          v-model:page-size="pagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- Customer details dialog -->
    <el-dialog
      v-model="detailVisible"
      title="Customer Details"
      width="700px"
      destroy-on-close
    >
      <div v-loading="detailLoading">
        <el-descriptions :column="2" border v-if="currentCustomer">
          <el-descriptions-item label="Customer ID">{{ currentCustomer.userId }}</el-descriptions-item>
          <el-descriptions-item label="Username">{{ currentCustomer.name }}</el-descriptions-item>
          <el-descriptions-item label="Email">{{ currentCustomer.email }}</el-descriptions-item>
          <el-descriptions-item label="Phone Number">{{ currentCustomer.phone || 'Not Set' }}</el-descriptions-item>
          <el-descriptions-item label="Gender">{{ currentCustomer.gender || 'Not Set' }}</el-descriptions-item>
          <el-descriptions-item label="Membership Level">
            <el-tag :type="getLevelType(currentCustomer.level)">{{ getLevelText(currentCustomer.level) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="Account Balance">¥{{ currentCustomer.balance || 0 }}</el-descriptions-item>
          <el-descriptions-item label="Email Verification">
            <el-tag :type="currentCustomer.emailVerified ? 'success' : 'info'">
              {{ currentCustomer.emailVerified ? 'Verified' : 'Not Verified' }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="Default Address" :span="2">
            {{ currentCustomer.address || 'Not Set' }}
          </el-descriptions-item>
          <el-descriptions-item label="Registration Time">{{ formatDate(currentCustomer.createDate) }}</el-descriptions-item>
          <el-descriptions-item label="Update Time">{{ formatDate(currentCustomer.updateDate) }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <template #footer>
        <el-button @click="detailVisible = false">Close</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { View } from '@element-plus/icons-vue'
import { formatDate } from '@/utils/format'
import request from '@/utils/request'

const loading = ref(false)
const customerList = ref([])
const total = ref(0)
const detailVisible = ref(false)
const detailLoading = ref(false)
const currentCustomer = ref(null)

const searchForm = reactive({
  keyword: '',
  level: ''
})

const pagination = reactive({
  current: 1,
  size: 10
})

onMounted(() => {
  loadCustomerList()
})

const loadCustomerList = async () => {
  loading.value = true
  try {
    const params = {
      current: pagination.current,
      size: pagination.size
    }

    if (searchForm.keyword) {
      params.keyword = searchForm.keyword
    }

    if (searchForm.level) {
      params.level = searchForm.level
    }

    const res = await request({
      url: '/api/admin/customers',
      method: 'get',
      params
    })

    customerList.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch (error) {
    console.error('Load customer list error:', error)
    ElMessage.error('Failed to load customer list')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.current = 1
  loadCustomerList()
}

// Reset search form
const handleReset = () => {
  searchForm.keyword = ''
  searchForm.level = ''
  pagination.current = 1
  loadCustomerList()
}

const handleSizeChange = () => {
  pagination.current = 1
  loadCustomerList()
}

const handleCurrentChange = () => {
  loadCustomerList()
}

const handleViewDetail = async (row) => {
  detailVisible.value = true
  detailLoading.value = true
  
  try {
    const res = await request({
      url: `/api/admin/customers/${row.userId}`,
      method: 'get'
    })
    currentCustomer.value = res.data
  } catch (error) {
    console.error('Load customer detail error:', error)
    ElMessage.error('Failed to load customer details')
    detailVisible.value = false
  } finally {
    detailLoading.value = false
  }
}

const getLevelType = (level) => {
  const types = {
    'NORMAL': '',
    'VIP': 'warning',
    'SVIP': 'danger'
  }
  return types[level] || ''
}

const getLevelText = (level) => {
  const texts = {
    'NORMAL': 'Regular',
    'VIP': 'VIP',
    'SVIP': 'SVIP'
  }
  return texts[level] || level
}
</script>

<style scoped>
.customer-list {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  font-size: 24px;
  font-weight: 500;
}

.search-card {
  margin-bottom: 20px;
}

.table-card {
  margin-bottom: 20px;
}

.pagination-wrapper {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
