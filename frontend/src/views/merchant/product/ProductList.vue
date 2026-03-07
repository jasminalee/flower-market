<template>
  <div class="product-list-container">
    <div class="page-header">
      <h2 class="page-title">Product Management</h2>
      <div class="header-actions">
        <el-button type="primary" @click="handleAdd" class="add-btn">
          <el-icon><Plus /></el-icon>
          Add New Product
        </el-button>
      </div>
    </div>

    <!-- Search filters -->
    <el-card shadow="never" class="search-card">
      <el-form :inline="true" :model="searchForm" class="filter-form">
        <el-form-item label="Product Name">
          <el-input v-model="searchForm.name" placeholder="Search by name..." clearable @keyup.enter="handleSearch" style="width: 220px" />
        </el-form-item>
        <el-form-item label="Category">
          <el-select v-model="searchForm.catId" placeholder="All Categories" clearable style="width: 180px">
            <el-option 
              v-for="cat in categories" 
              :key="cat.cateId" 
              :label="cat.name" 
              :value="cat.cateId" 
            />
          </el-select>
        </el-form-item>
        <el-form-item label="Status">
          <el-select v-model="searchForm.status" placeholder="All Status" clearable style="width: 150px">
            <el-option label="On Sale" value="ACTIVE" />
            <el-option label="Off Sale" value="INACTIVE" />
          </el-select>
        </el-form-item>
        <el-form-item class="form-actions">
          <el-button type="primary" @click="handleSearch">Search</el-button>
          <el-button @click="handleReset">Reset</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- Product table -->
    <el-card shadow="never" class="table-card">
      <el-table :data="tableData" v-loading="loading" style="width: 100%">
        <el-table-column label="Product" min-width="250">
          <template #default="{ row }">
            <div class="product-info-cell">
              <el-image
                :src="row.image"
                fit="cover"
                class="product-thumb"
                :preview-src-list="[row.image]"
                :preview-teleported="true"
                crossorigin="anonymous"
              >
                <template #error>
                  <div class="image-error-slot">
                    <el-icon><Picture /></el-icon>
                  </div>
                </template>
              </el-image>
              <div class="product-details">
                <div class="product-name">{{ row.name }}</div>
                <div class="product-cat-tag">{{ row.categoryText }}</div>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="price" label="Price" width="120">
          <template #default="{ row }">
            <span class="price-value">¥{{ row.price }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="stock" label="Stock" width="100" align="center">
          <template #default="{ row }">
            <span :class="['stock-count', { 'low-stock': row.stock < 10 }]">{{ row.stock }}</span>
          </template>
        </el-table-column>
        <el-table-column label="Status" width="120" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 'ACTIVE' ? 'success' : 'info'" effect="light">
              {{ row.statusText }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Actions" width="320" fixed="right" align="center">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">Edit</el-button>
            <el-button
              :type="row.status === 'ACTIVE' ? 'warning' : 'success'"
              link
              @click="handleToggleStatus(row)"
            >
              {{ row.status === 'ACTIVE' ? 'Take Off' : 'Put On' }}
            </el-button>
            <el-button type="info" link @click="handleTrackability(row)">Trace</el-button>
            <el-divider direction="vertical" />
            <el-button type="danger" link @click="handleDelete(row)">Delete</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- Pagination -->
      <div class="pagination-footer">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.size"
          :total="pagination.total"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="fetchProducts"
          @current-change="fetchProducts"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getMerchantProducts, updateProductStatus, deleteProduct } from '@/api/merchant'
import { getAllCategories } from '@/api/product'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Edit, Delete, Top, Bottom, Search } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()
const loading = ref(false)

const searchForm = reactive({
  name: '',
  catId: null,
  status: ''
})

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

const tableData = ref([])
const categories = ref([])
const categoryMap = ref({})

const fetchProducts = async () => {
  loading.value = true
  try {
    const res = await getMerchantProducts({
      merchId: userStore.userId,
      name: searchForm.name || undefined,
      catId: searchForm.catId || undefined,
      status: searchForm.status || undefined,
      current: pagination.page,
      size: pagination.size
    })
    // Backend returns IPage structure: records, total, size, current
    console.log('Raw product data:', res.data.records)
    tableData.value = (res.data.records || []).map(item => {
      console.log('Product catId:', item.catId, 'mapped category name:', categoryMap.value[item.catId])
      // Debug image path
      console.log('Original mainImage path:', item.mainImage)
      const imagePath = item.mainImage
      console.log('Mapped image path:', imagePath)
      // Check if the image path is valid
      let imageUrl = null
      if (!imagePath || imagePath === 'null' || imagePath === 'undefined') {
        console.warn('Product', item.name, 'has invalid image path:', imagePath)
      } else {
        // Use relative path so it goes through the Vite proxy
        imageUrl = imagePath
        console.log('Product', item.name, 'image URL will be:', imageUrl)
      }
      return {
        ...item,
        // Ensure a normalized id field is available for routing/editing
        id: item.prodId ?? item.id ?? item.productId,
        categoryText: categoryMap.value[item.catId] || `Category ${item.catId}`,
        statusText: item.status === 'ACTIVE' ? 'On Sale' : 'Off Sale',
        // Map field names for frontend display
        image: imageUrl,
        price: item.price
      }
    })
    pagination.total = res.data.total || 0
  } catch (error) {
    ElMessage.error('Failed to load data')
  } finally {
    loading.value = false
  }
}

const handleSearch = () => {
  pagination.page = 1
  fetchProducts()
}

const handleReset = () => {
  searchForm.name = ''
  searchForm.catId = null
  searchForm.status = ''
  handleSearch()
}

const handleAdd = () => {
  router.push('/merchant/products/add')
}

const handleEdit = (row) => {
  const pid = row.id ?? row.prodId ?? row.productId
  router.push(`/merchant/products/edit/${pid}`)
}

const handleTrackability = (row) => {
  router.push(`/merchant/products/${row.id}/trackability`)
}

const handleToggleStatus = async (row) => {
  const currentStatus = row.status || 'INACTIVE'
  const newStatus = currentStatus === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE'
  const actionText = newStatus === 'ACTIVE' ? 'Put On Sale' : 'Take Off Sale'

  try {
    await ElMessageBox.confirm(`Are you sure you want to ${actionText.toLowerCase()} this product?`, 'Confirmation', {
      type: 'warning'
    })

    const pid = row.prodId ?? row.id
    await updateProductStatus(pid, newStatus)
    ElMessage.success(`${actionText} successful`)
    fetchProducts()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || `${actionText} failed`)
    }
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('Are you sure you want to delete this product? This action cannot be undone.', 'Confirmation', {
      type: 'warning'
    })

    const pid = row.prodId ?? row.id
    await deleteProduct(pid)
    ElMessage.success('Deleted successfully')
    fetchProducts()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || 'Delete failed')
    }
  }
}

// Image event handlers for debugging
const onImageError = (row) => {
  console.error('Image failed to load for product:', row.name, 'at path:', row.image)
  console.log('Full product object:', row)
  console.log('Image URL attempted:', row.image || row.image)
}

const onImageLoad = (row) => {
  console.log('Image loaded successfully for product:', row.name, 'at path:', row.image)
}

// Load categories list
const loadCategories = async () => {
  try {
    const res = await getAllCategories()
    categories.value = res.data || []
    
    // Build category map (note: backend field is cateId)
    const map = {}
    categories.value.forEach(cat => {
      map[cat.cateId] = cat.name
    })
    categoryMap.value = map
    
    console.log('Loaded categories:', categories.value)
    console.log('Category map:', categoryMap.value)
  } catch (error) {
    console.error('Failed to load categories:', error)
  }
}

onMounted(async () => {
  await loadCategories()
  fetchProducts()
})
</script>

<style scoped>
.product-list-container {
  padding: 0;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  color: #1a1a1a;
  margin: 0;
}

.search-card {
  border: none;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
  margin-bottom: 20px;
}

.table-card {
  border: none;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.05);
}

.product-info-cell {
  display: flex;
  align-items: center;
  gap: 12px;
}

.product-thumb {
  width: 54px;
  height: 54px;
  border-radius: 8px;
  background-color: #f1f5f9;
  flex-shrink: 0;
}

.product-details {
  display: flex;
  flex-direction: column;
  gap: 4px;
  overflow: hidden;
}

.product-name {
  font-weight: 600;
  color: #334155;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.product-cat-tag {
  font-size: 12px;
  color: #64748b;
}

.price-value {
  font-weight: 700;
  color: #ef4444;
}

.stock-count {
  font-family: monospace;
  font-weight: 600;
}

.low-stock {
  color: #f59e0b;
}

.image-error-slot {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
  color: #cbd5e1;
  font-size: 20px;
}

.pagination-footer {
  margin-top: 24px;
  display: flex;
  justify-content: flex-end;
}

:deep(.el-table__header) {
  th {
    background-color: #f8fafc !important;
    color: #475569;
    font-weight: 600;
  }
}

:deep(.el-button--link) {
  padding: 4px 8px;
}
</style>

