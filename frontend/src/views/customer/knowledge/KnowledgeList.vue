<template>
  <div class="customer-page">
    <CustomerHeader />
    
    <div class="container">
      <div class="knowledge-list-page">
        <!-- Page header -->
        <div class="page-header mt-lg">
          <h1>Plant Care</h1>
          <p class="subtitle">Learn professional plant care tips to keep your flowers healthy and beautiful</p>
        </div>
        
        <!-- Search and filters -->
        <div class="filter-bar mt-lg">
          <el-card shadow="never">
            <el-form :inline="true">
              <el-form-item label="Category">
                <el-select v-model="filters.category" placeholder="All Categories" clearable @change="handleFilter">
                  <el-option label="All Categories" value="" />
                  <el-option label="Plant Care" value="plant-care" />
                  <el-option label="Pest & Disease" value="pest-control" />
                  <el-option label="Seasonal Care" value="seasonal-care" />
                  <el-option label="Propagation" value="propagation" />
                  <el-option label="Soil & Fertilizer" value="soil-fertilizer" />
                  <el-option label="Watering" value="watering" />
                </el-select>
              </el-form-item>
              
              <el-form-item label="Search">
                <el-input 
                  v-model="filters.keyword" 
                  placeholder="Search article titles or content" 
                  clearable
                  style="width: 250px"
                  @keyup.enter="handleFilter"
                >
                  <template #suffix>
                    <el-icon @click="handleFilter" style="cursor: pointer;">
                      <Search />
                    </el-icon>
                  </template>
                </el-input>
              </el-form-item>
              
              <el-form-item>
                <el-button type="primary" @click="handleFilter">Search</el-button>
                <el-button @click="handleReset">Reset</el-button>
              </el-form-item>
            </el-form>
          </el-card>
        </div>
        
        <!-- Articles list -->
        <div class="knowledge-list mt-lg" v-loading="loading">
          <el-row :gutter="20" v-if="knowledgeList.length > 0">
            <el-col :span="8" v-for="item in knowledgeList" :key="item.id">
              <el-card :body-style="{ padding: '0px' }" shadow="hover" class="knowledge-card">
                <div class="card-image" @click="goToDetail(item.id)">
                  <el-image 
                      :src="item.coverImage || 'https://via.placeholder.com/400x250?text=Plant+Care'" 
                    fit="cover"
                    style="width: 100%; height: 200px;"
                  />
                  <div class="category-tag">
                    <el-tag size="small">{{ item.category }}</el-tag>
                  </div>
                </div>
                <div class="card-content">
                  <h3 class="title ellipsis-2" @click="goToDetail(item.id)">
                    {{ item.title }}
                  </h3>
                  <p class="summary ellipsis-3">{{ item.content ? item.content.substring(0, 100) + '...' : '' }}</p>
                  <div class="meta-info">
                    <span class="author">
                      <el-icon><User /></el-icon>
                      {{ item.author || 'Admin' }}
                    </span>
                    <span class="views">
                      <el-icon><View /></el-icon>
                      {{ item.viewCount || 0 }}
                    </span>
                    <span class="time">
                      <el-icon><Clock /></el-icon>
                      {{ formatDate(item.createDate) }}
                    </span>
                  </div>
                </div>
              </el-card>
            </el-col>
          </el-row>
          
          <el-empty v-else description="No plant care articles available" />
        </div>
        
        <!-- Pagination -->
        <div class="pagination-wrapper mt-lg" v-if="total > 0">
          <el-pagination
            v-model:current-page="pagination.current"
            v-model:page-size="pagination.size"
            :page-sizes="[9, 18, 27, 36]"
            :total="total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
          />
        </div>
      </div>
    </div>
    
    <CustomerFooter />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Search, User, View, Clock } from '@element-plus/icons-vue'
import CustomerHeader from '@/components/layouts/CustomerHeader.vue'
import CustomerFooter from '@/components/layouts/CustomerFooter.vue'
import { getKnowledgeList } from '@/api/knowledge'
import { ElMessage } from 'element-plus'
import { formatDate } from '@/utils/format'

const router = useRouter()

const loading = ref(false)
const knowledgeList = ref([])
const total = ref(0)

const filters = reactive({
  category: '',
  keyword: ''
})

const pagination = reactive({
  current: 1,
  size: 9
})

onMounted(() => {
  loadKnowledgeList()
})

const loadKnowledgeList = async () => {
  loading.value = true
  try {
    const params = {
      current: pagination.current,
      size: pagination.size
    }
    
    if (filters.category) {
      params.category = filters.category
    }
    
    if (filters.keyword) {
      params.keyword = filters.keyword
    }
    
    const res = await getKnowledgeList(params)
    knowledgeList.value = res.data?.records || []
    total.value = res.data?.total || 0
  } catch (error) {
    console.error('Load knowledge list error:', error)
    ElMessage.error('Failed to load plant care articles')
  } finally {
    loading.value = false
  }
}

const handleFilter = () => {
  pagination.current = 1
  loadKnowledgeList()
}

const handleReset = () => {
  filters.category = ''
  filters.keyword = ''
  pagination.current = 1
  loadKnowledgeList()
}

const handleSizeChange = () => {
  pagination.current = 1
  loadKnowledgeList()
}

const handleCurrentChange = () => {
  loadKnowledgeList()
}

const goToDetail = (id) => {
  router.push(`/knowledge/${id}`)
}
</script>

<style scoped>
.knowledge-list-page {
  padding-bottom: 40px;
}

.page-header {
  text-align: center;
  padding: 30px 0;
}

.page-header h1 {
  font-size: 32px;
  font-weight: 600;
  color: #333;
  margin-bottom: 10px;
}

.page-header .subtitle {
  font-size: 16px;
  color: #666;
}

.filter-bar :deep(.el-card__body) {
  padding: 20px;
}

.knowledge-card {
  margin-bottom: 20px;
  cursor: pointer;
  transition: transform 0.3s;
}

.knowledge-card:hover {
  transform: translateY(-5px);
}

.card-image {
  position: relative;
  overflow: hidden;
}

.card-image:hover .el-image {
  transform: scale(1.05);
}

.card-image .el-image {
  transition: transform 0.3s;
}

.category-tag {
  position: absolute;
  top: 10px;
  right: 10px;
}

.card-content {
  padding: 20px;
}

.card-content .title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin-bottom: 12px;
  line-height: 1.4;
  cursor: pointer;
  transition: color 0.3s;
}

.card-content .title:hover {
  color: var(--el-color-primary);
}

.card-content .summary {
  font-size: 14px;
  color: #666;
  line-height: 1.6;
  margin-bottom: 15px;
  min-height: 60px;
}

.meta-info {
  display: flex;
  align-items: center;
  font-size: 13px;
  color: #999;
  gap: 15px;
}

.meta-info span {
  display: flex;
  align-items: center;
  gap: 4px;
}

.meta-info .el-icon {
  font-size: 14px;
}

.ellipsis-2 {
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
  text-overflow: ellipsis;
}

.ellipsis-3 {
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 3;
  overflow: hidden;
  text-overflow: ellipsis;
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  padding: 20px 0;
}
</style>
