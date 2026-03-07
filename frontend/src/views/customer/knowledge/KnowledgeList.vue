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
            <div class="filter-content">
              <el-form :inline="true" :model="filters" class="search-form">
                <el-form-item label="Category">
                  <el-select v-model="filters.category" placeholder="All Categories" clearable @change="handleFilter" style="width: 200px">
                    <el-option label="All Categories" value="" />
                    <el-option label="Flower Care" value="Flower Care" />
                    <el-option label="Plant Care" value="Plant Care" />
                    <el-option label="Plant Encyclopedia" value="Plant Encyclopedia" />
                    <el-option label="Floral Tutorial" value="Floral Tutorial" />
                    <el-option label="Seasonal Guide" value="Seasonal Guide" />
                  </el-select>
                </el-form-item>
                
                <el-form-item label="Search">
                  <el-input 
                    v-model="filters.keyword" 
                    placeholder="Search article titles or content" 
                    clearable
                    style="width: 280px"
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

              <div class="action-buttons">
                <el-button type="success" @click="showPostDialog = true">
                  <el-icon><Edit /></el-icon>
                  Post Article
                </el-button>
              </div>
            </div>
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
                    crossorigin="anonymous"
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

    <!-- Post Article Dialog -->
    <el-dialog 
      v-model="showPostDialog" 
      title="Share Your Plant Care Wisdom" 
      width="650px" 
      custom-class="fancy-post-dialog"
      @close="resetPostForm"
    >
      <div class="dialog-intro">
        <el-icon class="intro-icon"><Edit /></el-icon>
        <p>Help our community grow by sharing your gardening expertize and floral tips.</p>
      </div>

      <el-form :model="postForm" :rules="postRules" ref="postFormRef" label-position="top">
        <div class="form-section-header">Basic Information</div>
        <el-row :gutter="20">
          <el-col :span="14">
            <el-form-item label="Captivating Title" prop="title">
              <el-input 
                v-model="postForm.title" 
                placeholder="e.g., How to keep hydrangeas blue" 
                maxlength="100" 
                show-word-limit 
                class="fancy-input"
              />
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="Category" prop="category">
              <el-select v-model="postForm.category" placeholder="Choose a type" style="width: 100%" class="fancy-select">
                <el-option label="🌸 Flower Care" value="Flower Care" />
                <el-option label="🌿 Plant Care" value="Plant Care" />
                <el-option label="📚 Encyclopedia" value="Plant Encyclopedia" />
                <el-option label="🎬 Tutorial" value="Floral Tutorial" />
                <el-option label="📅 Seasonal" value="Seasonal Guide" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <div class="form-section-header">Content Details</div>
        <el-form-item label="Brief Summary" prop="summary">
          <el-input 
            v-model="postForm.summary" 
            type="textarea" 
            placeholder="A short introduction to entice readers..." 
            :rows="2"
            maxlength="200"
            show-word-limit
            class="fancy-input"
          />
        </el-form-item>

        <el-form-item label="Detailed Guide" prop="content">
          <el-input 
            v-model="postForm.content" 
            type="textarea" 
            placeholder="Step-by-step instructions or deep dive into the topic..." 
            :rows="8"
            class="fancy-input content-editor"
            style="width: 100%"
          />
        </el-form-item>

        <div class="form-section-header">Visuals & Meta</div>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="Cover Image" prop="coverImage">
              <el-upload
                class="fancy-uploader"
                action="/api/products/upload/image"
                :show-file-list="false"
                :on-success="handleUploadSuccess"
                :before-upload="beforeUpload"
              >
                <img v-if="postForm.coverImage" :src="postForm.coverImage" class="uploaded-image" />
                <div v-else class="upload-placeholder">
                  <el-icon class="upload-icon"><Plus /></el-icon>
                  <span>Upload Cover</span>
                </div>
              </el-upload>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Tags" prop="tags">
              <el-input v-model="postForm.tags" placeholder="e.g., rose, water, sun" class="fancy-input" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      
      <template #footer>
        <div class="fancy-footer">
          <el-button @click="showPostDialog = false" class="btn-cancel">Cancel</el-button>
          <el-button type="primary" @click="submitPost" :loading="submitting" class="btn-submit">
            Publish My Post
            <el-icon class="el-icon--right"><Promotion /></el-icon>
          </el-button>
        </div>
      </template>
    </el-dialog>
    
    <CustomerFooter />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Search, User, View, Clock, Edit, Promotion } from '@element-plus/icons-vue'
import CustomerHeader from '@/components/layouts/CustomerHeader.vue'
import CustomerFooter from '@/components/layouts/CustomerFooter.vue'
import { getKnowledgeList } from '@/api/knowledge'
import request from '@/utils/request'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'
import { formatDate } from '@/utils/format'

const router = useRouter()
const userStore = useUserStore()

const loading = ref(false)
const knowledgeList = ref([])
const total = ref(0)
const submitting = ref(false)
const showPostDialog = ref(false)
const postFormRef = ref(null)

const filters = reactive({
  category: '',
  keyword: ''
})

const pagination = reactive({
  current: 1,
  size: 9
})

const postForm = reactive({
  title: 'How to Care for My New Bouquet',
  category: 'Flower Care',
  summary: 'Keep your fresh cut flowers vibrant for longer with these simple daily routines.',
  content: '### Preparation\n1. Use a clean vase filled with lukewarm water.\n2. Cut the stems at a 45-degree angle.\n\n### Daily Maintenance\n* Change the water every 2 days.\n* Remove any leaves below the water line.\n* Keep away from direct sunlight and fruit.',
  coverImage: '/uploads/images/knowledge/sample-flower.jpg',
  tags: 'fresh flowers, vase life, floral care'
})

const postRules = {
  title: [{ required: true, message: 'Please enter a catchy title', trigger: 'blur' }],
  category: [{ required: true, message: 'Please select a category', trigger: 'change' }],
  content: [{ required: true, message: 'Content cannot be empty', trigger: 'blur' }]
}

onMounted(() => {
  loadKnowledgeList()
})

const resetPostForm = () => {
  if (postFormRef.value) postFormRef.value.resetFields()
  postForm.tags = 'fresh flowers, vase life, floral care'
}

const handleUploadSuccess = (response) => {
  if (response.code === 200) {
    postForm.coverImage = response.data
    ElMessage.success('Image uploaded successfully')
  } else {
    ElMessage.error(response.message || 'Upload failed')
  }
}

const beforeUpload = (file) => {
  const isJPGorPNG = file.type === 'image/jpeg' || file.type === 'image/png'
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isJPGorPNG) {
    ElMessage.error('Avatar picture must be JPG or PNG format!')
  }
  if (!isLt2M) {
    ElMessage.error('Avatar picture size can not exceed 2MB!')
  }
  return isJPGorPNG && isLt2M
}

const submitPost = async () => {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('You must be logged in to post an article.')
    router.push('/login')
    return
  }

  await postFormRef.value.validate(async (valid) => {
    if (valid) {
      submitting.value = true
      try {
        await request.post('/api/care-knowledge', {
          ...postForm,
          author: userStore.userInfo?.username || userStore.username || 'Anonymous User',
          status: 'PUBLISHED'
        })
        ElMessage.success('Post published! Your knowledge will help others.')
        showPostDialog.value = false
        loadKnowledgeList()
      } catch (error) {
        console.error('Submit post error:', error)
        ElMessage.error('Oops! Failed to publish your post. Please try again.')
      } finally {
        submitting.value = false
      }
    }
  })
}

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

.filter-content {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  flex-wrap: wrap;
  gap: 20px;
}

.search-form {
  display: flex;
  flex-wrap: wrap;
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

/* Fancy Dialog Styles */
.fancy-post-dialog :deep(.el-dialog__header) {
  margin-right: 0;
  padding: 24px 24px 16px;
  background: linear-gradient(135deg, #f0f7f4 0%, #ffffff 100%);
  border-bottom: 1px solid #eef2f1;
}

.fancy-post-dialog :deep(.el-dialog__title) {
  font-weight: 700;
  color: #2c3e50;
  font-size: 20px;
}

.dialog-intro {
  display: flex;
  align-items: center;
  gap: 12px;
  background: #fdf6ec;
  padding: 12px 16px;
  border-radius: 8px;
  margin-bottom: 24px;
  color: #a6732e;
  font-size: 14px;
  border: 1px solid #f9ebd8;
}

.intro-icon {
  font-size: 18px;
}

.form-section-header {
  font-size: 13px;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  color: #909399;
  margin: 20px 0 12px;
  display: flex;
  align-items: center;
}

.form-section-header::after {
  content: "";
  flex: 1;
  height: 1px;
  background: #ebeef5;
  margin-left: 10px;
}

.fancy-input :deep(.el-input__wrapper),
.fancy-input :deep(.el-textarea__inner),
.fancy-select :deep(.el-input__wrapper) {
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.02) !important;
  border: 1px solid #e4e7ed;
  transition: all 0.3s ease;
}

.content-editor :deep(.el-textarea__inner) {
  padding: 12px;
  line-height: 1.6;
}

.fancy-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding-top: 10px;
}

.btn-submit {
  padding: 10px 24px;
  font-weight: 600;
  border-radius: 8px;
  transition: all 0.3s;
}

.btn-submit:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
}

.btn-cancel {
  border-radius: 8px;
}

/* Uploader Styles */
.fancy-uploader :deep(.el-upload) {
  border: 1px dashed #d9d9d9;
  border-radius: 8px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 120px;
  height: 120px;
  display: flex;
  justify-content: center;
  align-items: center;
  background: #fafafa;
  transition: all 0.3s;
}

.fancy-uploader :deep(.el-upload:hover) {
  border-color: var(--el-color-primary);
  background: #f0f7ff;
}

.uploaded-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.upload-placeholder {
  display: flex;
  flex-direction: column;
  align-items: center;
  color: #8c939d;
  font-size: 13px;
}

.upload-icon {
  font-size: 24px;
  margin-bottom: 8px;
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
