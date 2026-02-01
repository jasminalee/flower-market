<template>
  <div class="customer-page">
    <CustomerHeader />
    
    <div class="container">
      <div class="product-detail-page" v-loading="loading">
        <el-card class="mt-lg" v-if="product">
          <el-row :gutter="30">
            <!-- Product images -->
            <el-col :span="10">
              <div class="product-images">
                <el-image 
                  :src="currentImage" 
                  :preview-src-list="imageList"
                  fit="cover"
                  class="main-image"
                />
                <div class="image-list" v-if="imageList.length > 1">
                  <div 
                    v-for="(img, index) in imageList" 
                    :key="index"
                    class="image-item"
                    :class="{ active: currentImage === img }"
                    @click="currentImage = img"
                  >
                    <el-image :src="img" fit="cover" />
                  </div>
                </div>
              </div>
            </el-col>
            
            <!-- Product information -->
            <el-col :span="14">
              <div class="product-info">
                <h1 class="product-title">{{ product.name }}</h1>
                <div class="product-subtitle">{{ product.subtitle }}</div>
                
                <div class="price-section">
                  <div class="price-info">
                      <span class="label">Price:</span>
                      <span class="price">¥{{ product.price }}</span>
                      <span class="original-price" v-if="product.originalPrice">¥{{ product.originalPrice }}</span>
                    </div>
                    <div class="sales-info">
                      <span>Sold {{ product.sales || 0 }}</span>
                    </div>
                </div>
                
                <el-divider />
                
                <div class="product-attrs">
                  <div class="attr-item">
                    <span class="label">Category:</span>
                    <span>{{ product.categoryName || 'N/A' }}</span>
                  </div>
                  <div class="attr-item">
                    <span class="label">Merchant:</span>
                    <span>{{ product.merchantName || 'Unknown Merchant' }} (ID: {{ product.merchId || '-' }})</span>
                  </div>
                  <div class="attr-item">
                    <span class="label">Stock:</span>
                    <span>{{ product.stock || 0 }}</span>
                  </div>
                  <div class="attr-item" v-if="product.origin">
                    <span class="label">Origin:</span>
                    <span>{{ product.origin }}</span>
                  </div>
                </div>
                
                <el-divider />
                
                <div class="quantity-section">
                  <span class="label">Quantity:</span>
                  <el-input-number 
                    v-model="quantity" 
                    :min="1" 
                    :max="product.stock || 999"
                  />
                </div>
                
                <div class="action-buttons">
                  <el-button 
                    type="primary" 
                    size="large"
                    :icon="ShoppingCart"
                    @click="handleAddToCart"
                  >
                    Add to Cart
                  </el-button>
                  <el-button 
                    size="large"
                    :icon="isFavorite ? StarFilled : Star"
                    @click="handleFavorite"
                  >
                    {{ isFavorite ? 'Favorited' : 'Favorite' }}
                  </el-button>
                </div>
              </div>
            </el-col>
          </el-row>
        </el-card>
        
        <!-- Product details tab -->
        <el-card class="mt-lg" v-if="product">
          <el-tabs v-model="activeTab">
            <el-tab-pane label="Product Details" name="detail">
              <div class="detail-content" v-html="product.description || 'No details available'"></div>
            </el-tab-pane>
            
            <el-tab-pane label="Traceability" name="traceability">
              <div class="traceability-content">
                <el-timeline v-if="product.traceInfo && product.traceInfo.length > 0">
                  <el-timeline-item
                    v-for="(item, index) in product.traceInfo"
                    :key="index"
                    :timestamp="item.time"
                    placement="top"
                  >
                    <el-card>
                      <h4>{{ item.stage }}</h4>
                      <p>{{ item.description }}</p>
                    </el-card>
                  </el-timeline-item>
                </el-timeline>
                <el-empty v-else description="No traceability info" />
              </div>
            </el-tab-pane>
            
            <el-tab-pane label="Reviews" name="reviews">
              <div class="reviews-content">
                <div class="reviews-header">
                  <h3>User Reviews ({{ reviews.length }})</h3>
                </div>
                
                <div class="review-list" v-if="reviews.length > 0">
                  <div class="review-item" v-for="review in reviews" :key="review.id">
                    <div class="review-header">
                      <el-avatar :size="40">{{ review.userName?.charAt(0) || 'U' }}</el-avatar>
                      <div class="review-user-info">
                        <div class="username">{{ review.userName || 'Anonymous' }}</div>
                        <el-rate v-model="review.rating" disabled size="small" />
                      </div>
                      <div class="review-time">{{ review.createDate }}</div>
                    </div>
                    <div class="review-content">{{ review.content }}</div>
                    <div class="review-images" v-if="review.images && review.images.length > 0">
                      <el-image
                        v-for="(img, index) in review.images"
                        :key="index"
                        :src="img"
                        :preview-src-list="review.images"
                        fit="cover"
                        style="width: 80px; height: 80px; margin-right: 8px;"
                      />
                    </div>
                  </div>
                </div>
                <el-empty v-else description="No reviews" />
              </div>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </div>
    </div>
    
    <CustomerFooter />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import CustomerHeader from '@/components/layouts/CustomerHeader.vue'
import CustomerFooter from '@/components/layouts/CustomerFooter.vue'
import { getProductDetail, addFavorite, removeFavorite, checkFavorite } from '@/api/product'
import { useCartStore } from '@/stores/cart'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'
import { ShoppingCart, Star, StarFilled } from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const cartStore = useCartStore()
const userStore = useUserStore()

const loading = ref(false)
const product = ref(null)
const reviews = ref([])
const quantity = ref(1)
const activeTab = ref('detail')
const currentImage = ref('')
const isFavorite = ref(false)

const imageList = computed(() => {
  if (!product.value) return []
  const images = [product.value.mainImage]
  if (product.value.images) {
    images.push(...product.value.images.split(',').filter(img => img))
  }
  return images.filter(img => img)
})

onMounted(async () => {
  await loadProductDetail()
  if (userStore.isLoggedIn) {
    await checkFavoriteStatus()
  }
})

const loadProductDetail = async () => {
  loading.value = true
  try {
    const prodId = route.params.id
    const res = await getProductDetail(prodId)
    product.value = res.data
    currentImage.value = res.data.mainImage || 'https://via.placeholder.com/600x600'
    
    // Load reviews (mock data, actual implementation should call review API)
    reviews.value = res.data.reviews || []
  } catch (error) {
    console.error('Load product detail error:', error)
    ElMessage.error('Failed to load product details')
  } finally {
    loading.value = false
  }
}

const checkFavoriteStatus = async () => {
  try {
    const res = await checkFavorite(route.params.id, userStore.userId)
    isFavorite.value = res.data || false
  } catch (error) {
    console.error('Check favorite error:', error)
  }
}

const handleAddToCart = async () => {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('Please log in first')
    router.push('/login')
    return
  }
  
  if (!product.value.stock || product.value.stock < quantity.value) {
    ElMessage.warning('Insufficient stock')
    return
  }
  
  try {
    await cartStore.addToCart({
      prodId: product.value.prodId,
      quantity: quantity.value
    })
    ElMessage.success('Added to cart')
  } catch (error) {
    ElMessage.error(error.message || 'Add failed')
  }
}

const handleFavorite = async () => {
    if (!userStore.isLoggedIn) {
    ElMessage.warning('Please log in first')
    router.push('/login')
    return
  }
  
  try {
    if (isFavorite.value) {
      await removeFavorite(product.value.prodId, userStore.userId)
      ElMessage.success('Removed from favorites')
      isFavorite.value = false
    } else {
      await addFavorite(product.value.prodId, userStore.userId)
      ElMessage.success('Added to favorites')
      isFavorite.value = true
    }
  } catch (error) {
    ElMessage.error(error.message || 'Operation failed')
  }
}
</script>

<style scoped>
.customer-page {
  min-height: 100vh;
  background: var(--color-bg-base);
}

.product-detail-page {
  padding-bottom: var(--spacing-xl);
}

/* Product images */
.product-images {
  position: sticky;
  top: 80px;
}

.main-image {
  width: 100%;
  height: 500px;
  border-radius: var(--radius-md);
}

.image-list {
  display: flex;
  gap: var(--spacing-sm);
  margin-top: var(--spacing-md);
}

.image-item {
  width: 80px;
  height: 80px;
  cursor: pointer;
  border: 2px solid transparent;
  border-radius: var(--radius-sm);
  overflow: hidden;
  transition: all 0.3s;
}

.image-item:hover,
.image-item.active {
  border-color: var(--color-primary);
}

.image-item .el-image {
  width: 100%;
  height: 100%;
}

/* Product information */
.product-info {
  padding: var(--spacing-md) 0;
}

.product-title {
  font-size: 28px;
  font-weight: 600;
  margin-bottom: var(--spacing-sm);
  color: var(--color-text-primary);
}

.product-subtitle {
  font-size: 14px;
  color: var(--color-text-secondary);
  margin-bottom: var(--spacing-lg);
}

.price-section {
  background: #fff8f0;
  padding: var(--spacing-lg);
  border-radius: var(--radius-md);
  margin-bottom: var(--spacing-lg);
}

.price-info {
  display: flex;
  align-items: baseline;
  gap: var(--spacing-sm);
  margin-bottom: var(--spacing-sm);
}

.price {
  font-size: 32px;
  font-weight: 600;
  color: var(--color-danger);
}

.original-price {
  font-size: 16px;
  color: var(--color-text-secondary);
  text-decoration: line-through;
}

.sales-info {
  color: var(--color-text-secondary);
  font-size: 14px;
}

.product-attrs {
  margin: var(--spacing-lg) 0;
}

.attr-item {
  margin-bottom: var(--spacing-md);
  font-size: 14px;
}

.label {
  color: var(--color-text-secondary);
  margin-right: var(--spacing-sm);
}

.quantity-section {
  display: flex;
  align-items: center;
  margin-bottom: var(--spacing-xl);
}

.action-buttons {
  display: flex;
  gap: var(--spacing-md);
}

.action-buttons .el-button {
  flex: 1;
}

/* Detail content */
.detail-content {
  padding: var(--spacing-lg);
  line-height: 1.8;
  color: var(--color-text-primary);
}

/* Traceability info */
.traceability-content {
  padding: var(--spacing-lg);
}

/* Review list */
.reviews-content {
  padding: var(--spacing-lg);
}

.reviews-header {
  margin-bottom: var(--spacing-lg);
}

.review-list {
  display: flex;
  flex-direction: column;
  gap: var(--spacing-lg);
}

.review-item {
  padding: var(--spacing-lg);
  background: var(--color-bg-base);
  border-radius: var(--radius-md);
}

.review-header {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
  margin-bottom: var(--spacing-md);
}

.review-user-info {
  flex: 1;
}

.username {
  font-weight: 500;
  margin-bottom: var(--spacing-xs);
}

.review-time {
  color: var(--color-text-secondary);
  font-size: 12px;
}

.review-content {
  line-height: 1.6;
  color: var(--color-text-primary);
  margin-bottom: var(--spacing-md);
}

.review-images {
  display: flex;
  gap: var(--spacing-sm);
}
</style>
