<template>
  <div class="product-list">
    <div class="header-actions">
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        Add Product
      </el-button>
    </div>

    <!-- Search filters -->
    <el-card class="search-card">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="Product Name">
          <el-input v-model="searchForm.name" placeholder="Enter product name" clearable />
        </el-form-item>
        <el-form-item label="Category">
          <el-select v-model="searchForm.catId" placeholder="Select category" clearable>
            <el-option 
              v-for="cat in categories" 
              :key="cat.cateId" 
              :label="cat.name" 
              :value="cat.cateId" 
            />
          </el-select>
        </el-form-item>
        <el-form-item label="Status">
          <el-select v-model="searchForm.status" placeholder="Select status" clearable>
            <el-option label="On Sale" value="ACTIVE" />
            <el-option label="Off Sale" value="INACTIVE" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">Search</el-button>
          <el-button @click="handleReset">Reset</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- Product table -->
    <el-card>
      <el-table :data="tableData" v-loading="loading" style="width: 100%">
        <el-table-column label="Product Image" width="100">
          <template #default="{ row }">
            <el-image
              :src="row.image"
              fit="cover"
              style="width: 60px; height: 60px; border-radius: 4px"
              :preview-src-list="[row.image]"
            />
          </template>
        </el-table-column>
        <el-table-column prop="name" label="Product Name" min-width="150" />
        <el-table-column prop="categoryText" label="Category" width="100" />
        <el-table-column prop="price" label="Price" width="100">
          <template #default="{ row }">
            ¥{{ row.price }}
          </template>
        </el-table-column>
        <el-table-column prop="stock" label="Stock" width="80" />
        <el-table-column label="Status" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'ACTIVE' ? 'success' : 'info'">
              {{ row.statusText }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Actions" width="280" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">Edit</el-button>
            <el-button
              :type="row.status === 'ACTIVE' ? 'warning' : 'success'"
              link
              @click="handleToggleStatus(row)"
            >
              {{ row.status === 'ACTIVE' ? 'Off Sale' : 'On Sale' }}
            </el-button>
            <el-button type="info" link @click="handleTrackability(row)">Traceability</el-button>
            <el-button type="danger" link @click="handleDelete(row)">Delete</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- Pagination -->
      <div class="pagination">
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
import { Plus } from '@element-plus/icons-vue'
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
      return {
        ...item,
        categoryText: categoryMap.value[item.catId] || `Category ${item.catId}`,
        statusText: item.status === 'ACTIVE' ? 'On Sale' : 'Off Sale',
        // Map field names for frontend display
        image: item.mainImage,
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
  router.push(`/merchant/products/edit/${row.id}`)
}

const handleTrackability = (row) => {
  router.push(`/merchant/products/${row.id}/trackability`)
}

const handleToggleStatus = async (row) => {
  const newStatus = row.status === 'ON_SALE' ? 'OFF_SALE' : 'ON_SALE'
  const actionText = newStatus === 'ON_SALE' ? 'Put On Sale' : 'Take Off Sale'

  try {
    await ElMessageBox.confirm(`Are you sure you want to ${actionText.toLowerCase()} this product?`, 'Confirmation', {
      type: 'warning'
    })

    await updateProductStatus(row.id, newStatus)
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

    await deleteProduct(row.id)
    ElMessage.success('Deleted successfully')
    fetchProducts()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || 'Delete failed')
    }
  }
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
.product-list {
  padding: 20px;
}

.header-actions {
  margin-bottom: 20px;
}

.search-card {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>

