<template>
  <div class="customer-page">
    <CustomerHeader />
    
    <div class="container">
      <div class="knowledge-detail-page" v-loading="loading">
        <el-card class="mt-lg" v-if="knowledge">
          <!-- Article Header -->
          <div class="article-header">
            <div class="category-breadcrumb">
              <el-breadcrumb separator="/">
                  <el-breadcrumb-item :to="{ path: '/knowledge' }">Plant Care</el-breadcrumb-item>
                <el-breadcrumb-item>{{ knowledge.category }}</el-breadcrumb-item>
                <el-breadcrumb-item>{{ knowledge.title }}</el-breadcrumb-item>
              </el-breadcrumb>
            </div>
            
            <h1 class="article-title">{{ knowledge.title }}</h1>
            
            <div class="article-meta">
              <span class="author">
                <el-icon><User /></el-icon>
                {{ knowledge.author || 'Administrator' }}
              </span>
              <span class="time">
                <el-icon><Clock /></el-icon>
                {{ formatDate(knowledge.createDate) }}
              </span>
              <span class="views">
                <el-icon><View /></el-icon>
                {{ knowledge.viewCount || 0 }} views
              </span>
              <el-tag size="small" type="success">{{ knowledge.category }}</el-tag>
            </div>
          </div>
          
          <el-divider />
          
          <!-- Article Cover -->
          <div class="article-cover" v-if="knowledge.coverImage">
            <el-image 
              :src="knowledge.coverImage" 
              fit="cover"
              style="width: 100%; max-height: 400px; border-radius: 8px;"
              crossorigin="anonymous"
            />
          </div>
          
          <!-- Article keywords -->
          <div class="article-keywords" v-if="knowledge.keywords">
            <el-alert type="info" :closable="false">
              <template #title>
                <strong>Keywords:</strong>{{ knowledge.keywords }}
              </template>
            </el-alert>
          </div>
          
          <!-- Article Content -->
          <div class="article-content" v-html="knowledge.content"></div>
          
          <el-divider />
          
          <!-- Article tags -->
          <div class="article-tags" v-if="knowledge.tags && knowledge.tags.length > 0">
            <span class="tags-label">Tags:</span>
            <el-tag 
              v-for="tag in knowledge.tags" 
              :key="tag"
              size="small"
              style="margin-right: 10px;"
            >
              {{ tag }}
            </el-tag>
          </div>
          
          <!-- Action Buttons -->
          <div class="article-actions">
            <el-button @click="goBack">
              <el-icon><ArrowLeft /></el-icon>
              Back to List
            </el-button>
            <el-button type="primary" @click="handleShare">
              <el-icon><Share /></el-icon>
              Share
            </el-button>
          </div>
        </el-card>
        
          <!-- Related articles -->
        <el-card class="mt-lg" v-if="relatedKnowledge.length > 0">
          <template #header>
            <h3>Related Articles</h3>
          </template>
          
          <div class="related-articles">
            <div 
              class="related-item" 
              v-for="item in relatedKnowledge" 
              :key="item.id"
              @click="goToDetail(item.id)"
            >
                <el-image 
                :src="item.coverImage || 'https://via.placeholder.com/120x80?text=Plant+Care'" 
                fit="cover"
                style="width: 120px; height: 80px; border-radius: 4px;"
                crossorigin="anonymous"
              />
              <div class="related-info">
                <h4 class="related-title ellipsis-2">{{ item.title }}</h4>
                <div class="related-meta">
                  <span>
                    <el-icon><View /></el-icon>
                    {{ item.viewCount || 0 }}
                  </span>
                  <span>{{ formatDate(item.createDate) }}</span>
                </div>
              </div>
            </div>
          </div>
        </el-card>

        <!-- Comments Section -->
        <el-card class="mt-lg">
          <template #header>
            <div class="comments-header">
              <h3>Comments ({{ commentList.length }})</h3>
            </div>
          </template>

          <div class="comment-input-area" v-if="userStore.isLoggedIn">
            <el-input
              v-model="newComment"
              type="textarea"
              :rows="3"
              placeholder="Write a comment..."
              maxlength="500"
              show-word-limit
            />
            <div class="submit-btn">
              <el-button type="primary" @click="submitComment" :loading="submitting">Submit Comment</el-button>
            </div>
          </div>
          <div v-else class="login-prompt">
            <el-button type="primary" link @click="$router.push('/login')">Log in to comment</el-button>
          </div>

          <el-divider />

          <div class="comment-list">
            <div v-if="commentList.length === 0" class="no-comments">
              <el-empty description="No comments yet. Be the first to comment!" />
            </div>
            <div v-for="comment in commentList" :key="comment.id" class="comment-item">
              <div class="comment-user">
                <el-avatar :size="32">{{ comment.userName?.charAt(0) || 'U' }}</el-avatar>
                <span class="comment-username">{{ comment.userName }}</span>
                <span class="comment-date">{{ formatDate(comment.createDate) }}</span>
              </div>
              <div class="comment-body">
                {{ comment.content }}
              </div>
            </div>
          </div>
        </el-card>
      </div>
    </div>
    
    <CustomerFooter />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { User, Clock, View, ArrowLeft, Share } from '@element-plus/icons-vue'
import CustomerHeader from '@/components/layouts/CustomerHeader.vue'
import CustomerFooter from '@/components/layouts/CustomerFooter.vue'
import { getKnowledgeDetail, getKnowledgeList } from '@/api/knowledge'
import request from '@/utils/request'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'
import { formatDate } from '@/utils/format'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const loading = ref(false)
const knowledge = ref(null)
const relatedKnowledge = ref([])
const commentList = ref([])
const newComment = ref('')
const submitting = ref(false)

onMounted(() => {
  loadKnowledgeDetail()
  loadComments()
})

const loadKnowledgeDetail = async () => {
  loading.value = true
  try {
    const id = route.params.id
    const res = await getKnowledgeDetail(id)
    knowledge.value = res.data
    
    // Load related articles
    if (knowledge.value) {
      loadRelatedKnowledge()
    }
  } catch (error) {
    console.error('Load knowledge detail error:', error)
    ElMessage.error('Failed to load article details')
  } finally {
    loading.value = false
  }
}

const loadComments = async () => {
  try {
    const res = await request.get(`/api/knowledge-comments/${route.params.id}`)
    commentList.value = res.data.records || []
  } catch (error) {
    console.error('Load comments error:', error)
  }
}

const submitComment = async () => {
  if (!newComment.value.trim()) {
    ElMessage.warning('Please enter a comment.')
    return
  }
  
  submitting.value = true
  try {
    await request.post('/api/knowledge-comments', {
      knowledgeId: route.params.id,
      userId: userStore.userInfo?.userId || userStore.userId,
      userName: userStore.userInfo?.username || userStore.username,
      content: newComment.value
    })
    ElMessage.success('Comment submitted!')
    newComment.value = ''
    loadComments()
  } catch (error) {
    console.error('Submit comment error:', error)
    ElMessage.error('Failed to submit comment.')
  } finally {
    submitting.value = false
  }
}

const loadRelatedKnowledge = async () => {
  try {
    const params = {
      current: 1,
      size: 4,
      category: knowledge.value.category
    }
    
    const res = await getKnowledgeList(params)
    relatedKnowledge.value = (res.data?.records || [])
      .filter(item => item.knowledgeId !== knowledge.value.knowledgeId)
      .slice(0, 3)
  } catch (error) {
    console.error('Load related articles error:', error)
  }
}

const goToDetail = (id) => {
  router.push(`/knowledge/${id}`)
  // Reload page content
  loadKnowledgeDetail()
  // Scroll to top
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

const goBack = () => {
  router.push('/knowledge')
}

const handleShare = () => {
  if (navigator.share) {
    navigator.share({
      title: knowledge.value.title,
      text: knowledge.value.summary,
      url: window.location.href
    }).catch(err => {
      console.error('Share error:', err)
    })
  } else {
    // Copy link to clipboard
    navigator.clipboard.writeText(window.location.href)
    ElMessage.success('Link copied to clipboard')
  }
}
</script>

<style scoped>
.knowledge-detail-page {
  padding-bottom: 40px;
  max-width: 900px;
  margin: 0 auto;
}

.article-header {
  padding: 20px 0;
}

.category-breadcrumb {
  margin-bottom: 20px;
}

.article-title {
  font-size: 32px;
  font-weight: 600;
  color: #333;
  line-height: 1.4;
  margin-bottom: 20px;
}

.article-meta {
  display: flex;
  align-items: center;
  gap: 20px;
  font-size: 14px;
  color: #666;
}

.article-meta span {
  display: flex;
  align-items: center;
  gap: 5px;
}

.article-meta .el-icon {
  font-size: 16px;
}

.article-cover {
  margin: 30px 0;
}

.article-summary {
  margin: 20px 0;
}

.article-summary :deep(.el-alert__content) {
  line-height: 1.8;
}

.article-content {
  padding: 30px 0;
  font-size: 16px;
  line-height: 1.8;
  color: #333;
}

.article-content :deep(img) {
  max-width: 100%;
  height: auto;
  border-radius: 8px;
  margin: 20px 0;
}

.article-content :deep(p) {
  margin-bottom: 15px;
}

.article-content :deep(h2) {
  font-size: 24px;
  font-weight: 600;
  margin: 30px 0 15px;
  color: #333;
}

.article-content :deep(h3) {
  font-size: 20px;
  font-weight: 600;
  margin: 25px 0 12px;
  color: #333;
}

.article-content :deep(ul),
.article-content :deep(ol) {
  margin: 15px 0;
  padding-left: 25px;
}

.article-content :deep(li) {
  margin-bottom: 10px;
}

.article-content :deep(blockquote) {
  border-left: 4px solid var(--el-color-primary);
  padding: 10px 20px;
  margin: 20px 0;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.article-tags {
  margin: 20px 0;
}

.tags-label {
  font-weight: 600;
  margin-right: 10px;
  color: #666;
}

.article-actions {
  display: flex;
  gap: 10px;
  margin-top: 30px;
}

.related-articles {
  display: grid;
  gap: 20px;
}

.related-item {
  display: flex;
  gap: 15px;
  cursor: pointer;
  padding: 10px;
  border-radius: 8px;
  transition: background-color 0.3s;
}

.related-item:hover {
  background-color: #f5f7fa;
}

.related-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.related-title {
  font-size: 16px;
  font-weight: 500;
  color: #333;
  line-height: 1.4;
  margin-bottom: 8px;
}

.related-meta {
  display: flex;
  align-items: center;
  gap: 15px;
  font-size: 13px;
  color: #999;
}

.related-meta span {
  display: flex;
  align-items: center;
  gap: 4px;
}

.ellipsis-2 {
  display: -webkit-box;
  -webkit-box-orient: vertical;
  -webkit-line-clamp: 2;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* Comments Section Styles */
.comments-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
}

.comment-input-area {
  margin-bottom: 20px;
}

.submit-btn {
  margin-top: 10px;
  display: flex;
  justify-content: flex-end;
}

.login-prompt {
  text-align: center;
  padding: 20px;
  background-color: #f8f9fa;
  border-radius: 4px;
}

.comment-item {
  padding: 15px 0;
  border-bottom: 1px solid #ebeef5;
}

.comment-item:last-child {
  border-bottom: none;
}

.comment-user {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 10px;
}

.comment-username {
  font-weight: 500;
  color: #333;
}

.comment-date {
  font-size: 12px;
  color: #999;
  margin-left: auto;
}

.comment-body {
  font-size: 14px;
  line-height: 1.6;
  color: #666;
  padding-left: 42px;
}

.no-comments {
  padding: 40px 0;
}
</style>
