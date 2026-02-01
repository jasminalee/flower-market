<template>
  <div class="knowledge-list">
    <div class="page-header">
      <h2 class="page-title">Knowledge Articles</h2>
      <el-button type="primary" @click="handleAdd">Add Article</el-button>
    </div>

    <!-- Search -->
    <el-card shadow="never" class="search-card">
      <el-form :inline="true">
        <el-form-item label="Title">
          <el-input v-model="searchForm.keyword" placeholder="Enter title" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item label="Category">
          <el-select v-model="searchForm.category" placeholder="All" clearable style="width: 150px">
            <el-option label="Care Tips" value="care" />
            <el-option label="Flower Knowledge" value="knowledge" />
            <el-option label="Styling Guide" value="guide" />
            <el-option label="Festival Floristry" value="festival" />
          </el-select>
        </el-form-item>
        <el-form-item label="Status">
          <el-select v-model="searchForm.status" placeholder="All" clearable style="width: 150px">
            <el-option label="Published" value="PUBLISHED" />
            <el-option label="Unpublished" value="DRAFT" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">Search</el-button>
          <el-button @click="handleReset">Reset</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- Articles table -->
    <el-card shadow="never" class="table-card">
      <el-table :data="knowledgeList" v-loading="loading" style="width: 100%">
        <el-table-column label="Cover" width="100">
          <template #default="{ row }">
            <el-image :src="row.coverImage" fit="cover" style="width: 80px; height: 60px; border-radius: 4px;" />
          </template>
        </el-table-column>
        <el-table-column prop="title" label="Title" min-width="200" show-overflow-tooltip />
        <el-table-column prop="category" label="Category" width="160">
          <template #default="{ row }">
            {{ getCategoryText(row.category) }}
          </template>
        </el-table-column>
        <el-table-column prop="author" label="Author" width="140" />
        <el-table-column prop="viewCount" label="Views" width="100" />
        <el-table-column prop="status" label="Status" width="120">
          <template #default="{ row }">
            <el-tag :type="row.status === 'PUBLISHED' ? 'success' : 'info'">
              {{ row.status === 'PUBLISHED' ? 'Published' : 'Unpublished' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createDate" label="Published At" width="160">
          <template #default="{ row }">
            {{ formatDate(row.createDate) }}
          </template>
        </el-table-column>
        <el-table-column label="Actions" width="260" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row.id)">Edit</el-button>
            <el-button v-if="row.status === 'PUBLISHED'" type="warning" link @click="handleToggleStatus(row.id, 'DRAFT')">Unpublish</el-button>
            <el-button v-else type="success" link @click="handleToggleStatus(row.id, 'PUBLISHED')">Publish</el-button>
            <el-button type="danger" link @click="handleDelete(row.id)">Delete</el-button>
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
          @current-change="fetchKnowledgeList"
          @size-change="fetchKnowledgeList"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getKnowledgeList, deleteKnowledge } from '@/api/knowledge'
import { formatDate } from '@/utils/format'
import request from '@/utils/request'

const router = useRouter()
const loading = ref(false)
const knowledgeList = ref([])

const searchForm = reactive({
  keyword: '',
  category: '',
  status: ''
})

const pagination = reactive({
  page: 1,
  pageSize: 10,
  total: 0
})

// Fetch articles
const fetchKnowledgeList = async () => {
  loading.value = true
  try {
    const params = {
      current: pagination.page,
      size: pagination.pageSize,
      keyword: searchForm.keyword || undefined,
      category: searchForm.category || undefined,
      status: searchForm.status || undefined
    }
    const { data } = await getKnowledgeList(params)
    knowledgeList.value = data.records || []
    pagination.total = data.total || 0
  } catch (error) {
    ElMessage.error('Failed to load articles')
  } finally {
    loading.value = false
  }
}

// Search
const handleSearch = () => {
  pagination.page = 1
  fetchKnowledgeList()
}

// Reset
const handleReset = () => {
  searchForm.keyword = ''
  searchForm.category = ''
  searchForm.status = ''
  pagination.page = 1
  fetchKnowledgeList()
}

// Add
const handleAdd = () => {
  router.push('/admin/knowledge/add')
}

// Edit
const handleEdit = (id) => {
  router.push(`/admin/knowledge/edit/${id}`)
}

// Toggle status
const handleToggleStatus = async (id, status) => {
  const actionText = status === 'PUBLISHED' ? 'Publish' : 'Unpublish'
  try {
    await ElMessageBox.confirm(`Confirm to ${actionText.toLowerCase()} this article?`, 'Confirmation', {
      confirmButtonText: 'OK',
      cancelButtonText: 'Cancel',
      type: 'warning'
    })
    
    await request({
      url: `/api/admin/knowledge/${id}/status`,
      method: 'put',
      params: { status }
    })
    
    ElMessage.success(`${actionText} succeeded`)
    fetchKnowledgeList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(`${actionText} failed`)
    }
  }
}

// Delete
const handleDelete = async (id) => {
  try {
    await ElMessageBox.confirm('Are you sure you want to delete this article? This action cannot be undone.', 'Confirmation', {
      confirmButtonText: 'OK',
      cancelButtonText: 'Cancel',
      type: 'warning'
    })
    
    await deleteKnowledge(id)
    ElMessage.success('Deleted successfully')
    fetchKnowledgeList()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('Delete failed')
    }
  }
}

// Category text
const getCategoryText = (category) => {
  const map = {
    care: 'Care Tips',
    knowledge: 'Flower Knowledge',
    guide: 'Styling Guide',
    festival: 'Festival Floristry'
  }
  return map[category] || category
}

onMounted(() => {
  fetchKnowledgeList()
})
</script>

<style scoped>
.knowledge-list {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-title {
  margin: 0;
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
