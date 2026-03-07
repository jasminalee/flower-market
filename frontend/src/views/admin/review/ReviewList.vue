<template>
  <div class="review-list">
    <h2 class="page-title">Review Management</h2>

    <!-- Search filters -->
    <el-card shadow="never" class="search-card">
      <el-form :inline="true">
        <el-form-item label="Status">
          <el-select v-model="searchForm.status" placeholder="All statuses" clearable style="width: 150px">
            <el-option label="Pending" value="PENDING" />
            <el-option label="Approved" value="APPROVED" />
            <el-option label="Rejected" value="REJECTED" />
          </el-select>
        </el-form-item>
        <el-form-item label="Rating">
          <el-select v-model="searchForm.rating" placeholder="All ratings" clearable style="width: 150px">
            <el-option label="5 stars" :value="5" />
            <el-option label="4 stars" :value="4" />
            <el-option label="3 stars" :value="3" />
            <el-option label="2 stars" :value="2" />
            <el-option label="1 star" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">Search</el-button>
          <el-button @click="handleReset">Reset</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- Review table -->
    <el-card shadow="never" class="table-card">
      <el-table :data="reviewList" v-loading="loading" style="width: 100%">
        <el-table-column label="Product" width="250">
          <template #default="{ row }">
            <div class="product-info">
              <el-image :src="row.productImage || '/default-product.png'" fit="cover" class="product-image" />
              <div class="product-name">{{ getDisplayName(row.productName, row.prodId, 'Product ID') }}</div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="Customer" width="150">
          <template #default="{ row }">
            {{ getDisplayName(row.userName, row.userId, 'User ID') }}
          </template>
        </el-table-column>
        <el-table-column prop="rating" label="Rating" width="120">
          <template #default="{ row }">
            <el-rate v-model="row.rating" disabled />
          </template>
        </el-table-column>
        <el-table-column prop="content" label="Review" min-width="200" show-overflow-tooltip />
        <el-table-column label="Images" width="120">
          <template #default="{ row }">
            <template v-if="parseImages(row.images).length > 0">
              <div class="review-images">
                <el-image
                  v-for="(img, index) in parseImages(row.images).slice(0, 3)"
                  :key="index"
                  :src="img"
                  fit="cover"
                  class="review-image-thumb"
                  :preview-src-list="parseImages(row.images)"
                />
                <span v-if="parseImages(row.images).length > 3" class="more-count">
                  +{{ parseImages(row.images).length - 3 }}
                </span>
              </div>
            </template>
            <span v-else class="empty-text">-</span>
          </template>
        </el-table-column>
        <el-table-column prop="createDate" label="Time" width="160">
          <template #default="{ row }">
            {{ formatDate(row.createDate) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="Status" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Actions" width="200" fixed="right">
          <template #default="{ row }">
            <el-button 
              type="primary" 
              plain
              size="small"
              icon="View" 
              @click="handleViewDetail(row)"
            >
              Details
            </el-button>
            <template v-if="isPendingStatus(row.status)">
              <el-button 
                type="success" 
                plain
                size="small"
                icon="Check" 
                @click="handleAudit(row.id, 'APPROVED')"
              >
                Approve
              </el-button>
              <el-button 
                type="danger" 
                plain
                size="small"
                icon="Close" 
                @click="handleAudit(row.id, 'REJECTED')"
              >
                Reject
              </el-button>
            </template>
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
          @current-change="fetchReviewList"
          @size-change="fetchReviewList"
        />
      </div>
    </el-card>

    <!-- Detail dialog -->
    <el-dialog v-model="detailDialogVisible" title="Review Details" width="600px">
      <div v-if="currentReview" class="detail-content">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="Product">
            <div class="product-info">
              <el-image :src="currentReview.productImage || '/default-product.png'" fit="cover" class="detail-product-image" />
              <div>
                <div>{{ getDisplayName(currentReview.productName, currentReview.prodId, 'Product ID') }}</div>
                <div class="info-id">ID: {{ currentReview.prodId }}</div>
              </div>
            </div>
          </el-descriptions-item>
          <el-descriptions-item label="Customer">
            <div>{{ getDisplayName(currentReview.userName, currentReview.userId, 'User ID') }}</div>
            <div class="info-id">ID: {{ currentReview.userId }}</div>
          </el-descriptions-item>
          <el-descriptions-item label="Order ID">
            {{ currentReview.orderId }}
          </el-descriptions-item>
          <el-descriptions-item label="Rating">
            <el-rate v-model="currentReview.rating" disabled />
          </el-descriptions-item>
          <el-descriptions-item label="Review">
            <div class="review-content">{{ currentReview.content || 'N/A' }}</div>
          </el-descriptions-item>
          <el-descriptions-item label="Images">
            <template v-if="currentReviewImages.length > 0">
              <div class="detail-images">
                <el-image
                  v-for="(img, index) in currentReviewImages"
                  :key="index"
                  :src="img"
                  fit="cover"
                  class="detail-image"
                  :preview-src-list="currentReviewImages"
                />
              </div>
            </template>
            <span v-else class="empty-text">N/A</span>
          </el-descriptions-item>
          <el-descriptions-item label="Time">
            {{ formatDate(currentReview.createDate) }}
          </el-descriptions-item>
          <el-descriptions-item label="Status">
            <el-tag :type="getStatusType(currentReview.status)">{{ getStatusText(currentReview.status) }}</el-tag>
          </el-descriptions-item>
        </el-descriptions>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="detailDialogVisible = false">Close</el-button>
          <template v-if="currentReview && isPendingStatus(currentReview.status)">
            <el-button type="success" @click="handleAuditFromDetail('APPROVED')">Approve</el-button>
            <el-button type="danger" @click="handleAuditFromDetail('REJECTED')">Reject</el-button>
          </template>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'
import { formatDate } from '@/utils/format'

// Status config
const STATUS_CONFIG = {
  PENDING: { text: 'Pending', type: 'warning' },
  APPROVED: { text: 'Approved', type: 'success' },
  REJECTED: { text: 'Rejected', type: 'danger' }
}

const loading = ref(false)
const reviewList = ref([])

const searchForm = reactive({
  status: '',
  rating: null
})

const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 0
})

// Fetch review list
const fetchReviewList = async () => {
  loading.value = true
  try {
    const params = {
      current: pagination.page,
      size: pagination.pageSize,
      status: searchForm.status || undefined,
      rating: searchForm.rating || undefined
    }
    const { data } = await request({
      url: '/api/admin/reviews',
      method: 'get',
      params
    })
    reviewList.value = data.records || []
    pagination.total = data.total || 0
  } catch (error) {
    ElMessage.error('Failed to load review list')
  } finally {
    loading.value = false
  }
}

// Search
const handleSearch = () => {
  pagination.page = 1
  fetchReviewList()
}

// Reset
const handleReset = () => {
  searchForm.status = ''
  searchForm.rating = null
  pagination.page = 1
  fetchReviewList()
}

// Detail dialog
const detailDialogVisible = ref(false)
const currentReview = ref(null)

// Compute image list for current review
const currentReviewImages = computed(() => parseImages(currentReview.value?.images))

// Pending status check
const isPendingStatus = (status) => status === 'PENDING'

const handleViewDetail = (row) => {
  currentReview.value = row
  detailDialogVisible.value = true
}

// Display name with fallback
const getDisplayName = (name, id, prefix = 'ID') => name || `${prefix}: ${id}`

// Approve / reject
const handleAudit = async (id, status) => {
  const actionLabel = status === 'APPROVED' ? 'Approve' : 'Reject'
  const actionPast = status === 'APPROVED' ? 'Approved' : 'Rejected'
  try {
    await ElMessageBox.confirm(`Confirm to ${actionLabel.toLowerCase()} this review?`, 'Confirm', {
      confirmButtonText: 'OK',
      cancelButtonText: 'Cancel',
      type: 'warning'
    })
    
    await request({
      url: `/api/products/reviews/${id}/approval`,
      method: 'put',
      params: { status }
    })
    
    ElMessage.success(`${actionPast} successfully`)
    fetchReviewList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(`${actionLabel} failed`)
    }
  }
}

// Approve/reject from detail dialog
const handleAuditFromDetail = async (status) => {
  await handleAudit(currentReview.value.id, status)
  detailDialogVisible.value = false
}

// Status helpers
const getStatusType = (status) => STATUS_CONFIG[status]?.type || 'info'
const getStatusText = (status) => STATUS_CONFIG[status]?.text || 'Unknown'

// Parse image JSON string
const parseImages = (imagesStr) => {
  if (!imagesStr) return []
  try {
    return JSON.parse(imagesStr)
  } catch (e) {
    return []
  }
}

onMounted(() => {
  fetchReviewList()
})
</script>

<style scoped>
.review-list {
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

.product-info {
  display: flex;
  align-items: center;
  gap: 10px;
}

.product-name {
  flex: 1;
  font-size: 14px;
  color: #303133;
}

.product-image {
  width: 60px;
  height: 60px;
  border-radius: 4px;
}

.detail-product-image {
  width: 80px;
  height: 80px;
  border-radius: 4px;
  margin-right: 10px;
}

.info-id {
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
}

.review-images {
  display: flex;
  align-items: center;
}

.review-image-thumb {
  width: 30px;
  height: 30px;
  margin-right: 5px;
  border-radius: 4px;
}

.more-count {
  font-size: 12px;
  color: #909399;
}

.empty-text {
  color: #909399;
}

.review-content {
  white-space: pre-wrap;
  line-height: 1.6;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.detail-content {
  max-height: 60vh;
  overflow-y: auto;
}

.detail-images {
  display: flex;
  flex-wrap: wrap;
}

.detail-image {
  width: 80px;
  height: 80px;
  margin-right: 10px;
  margin-bottom: 10px;
  border-radius: 4px;
}

.dialog-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}
</style>
