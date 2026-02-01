<template>
  <div class="customer-home">
    <CustomerHeader />
    
    <div class="container">
      <!-- Hero-themed SVG Carousel -->
      <el-carousel height="400px" class="mt-lg">
        <el-carousel-item v-for="(item, index) in banners" :key="item.id">
          <div class="hero-carousel-item" :class="`carousel-theme-${index % 3 + 1}`">
            <div class="svg-background">
              <svg viewBox="0 0 1200 400" xmlns="http://www.w3.org/2000/svg" class="hero-svg">
                <!-- Decorative background shapes -->
                <circle cx="100" cy="100" r="80" :fill="getThemeColor(index, 'bg')" opacity="0.1" />
                <circle cx="1100" cy="300" r="120" :fill="getThemeColor(index, 'accent')" opacity="0.1" />
                
                <!-- Main decorative elements based on theme -->
                <g :data-theme="index">
                  <!-- Blue theme - flowers/blooms (index 0) -->
                  <g v-show="index === 0">
                    <path d="M600,180 C610,160 630,160 640,180 C630,200 610,200 600,180 Z" fill="#409EFF" opacity="0.6" />
                    <path d="M580,200 C590,180 610,180 620,200 C610,220 590,220 580,200 Z" fill="#409EFF" opacity="0.6" />
                    <path d="M620,200 C630,180 650,180 660,200 C650,220 630,220 620,200 Z" fill="#409EFF" opacity="0.6" />
                    <path d="M600,220 C610,200 630,200 640,220 C630,240 610,240 600,220 Z" fill="#409EFF" opacity="0.6" />
                    <circle cx="600" cy="200" r="10" fill="#217CE8" />
                  </g>
                  <!-- Green theme - leaves/plants (index 1) -->
                  <g v-show="index === 1">
                    <path d="M500,250 C520,220 550,230 560,260 C540,280 510,270 500,250 Z" fill="#67C23A" opacity="0.7" />
                    <path d="M650,200 C670,170 700,180 710,210 C690,230 660,220 650,200 Z" fill="#67C23A" opacity="0.7" />
                    <path d="M580,300 C600,270 630,280 640,310 C620,330 590,320 580,300 Z" fill="#67C23A" opacity="0.7" />
                    <line x1="600" y1="350" x2="600" y2="150" stroke="#529B2E" stroke-width="3" />
                  </g>
                  <!-- Pink theme - floral elements (index 2) -->
                  <g v-show="index !== 0 && index !== 1">
                    <circle cx="480" cy="130" r="15" fill="#F7CAC9" opacity="0.7" />
                    <circle cx="500" cy="110" r="12" fill="#F7CAC9" opacity="0.7" />
                    <circle cx="520" cy="130" r="10" fill="#F7CAC9" opacity="0.7" />
                    <circle cx="500" cy="150" r="12" fill="#F7CAC9" opacity="0.7" />
                    <circle cx="580" cy="150" r="18" fill="#F7CAC9" opacity="0.7" />
                    <circle cx="600" cy="130" r="15" fill="#F7CAC9" opacity="0.7" />
                    <circle cx="620" cy="150" r="12" fill="#F7CAC9" opacity="0.7" />
                    <circle cx="600" cy="170" r="15" fill="#F7CAC9" opacity="0.7" />
                    <circle cx="580" cy="150" r="8" fill="#D6A8A6" />
                  </g>
                </g>
                
                <!-- Central decorative element that changes with theme -->
                <g :class="`theme-${index % 3 + 1}`">
                  <circle cx="600" cy="200" r="60" :fill="getThemeColor(index, 'primary')" opacity="0.1" />
                  <circle cx="600" cy="200" r="40" :fill="getThemeColor(index, 'primary')" opacity="0.2" />
                  <circle cx="600" cy="200" r="20" :fill="getThemeColor(index, 'primary')" opacity="0.3" />
                </g>
              </svg>
            </div>
            <div class="carousel-content">
              <h3 class="carousel-title">{{ item.title }}</h3>
              <p class="carousel-subtitle">{{ item.subtitle }}</p>
              <el-button type="primary" class="cta-button">{{ item.ctaText }}</el-button>
            </div>
          </div>
        </el-carousel-item>
      </el-carousel>
      
      <!-- Category navigation -->
      <div class="category-nav mt-xl">
        <h2 class="section-title">Product Categories</h2>
        <el-row :gutter="20">
          <el-col :span="4" v-for="cat in categories" :key="cat.cateId">
            <div class="category-card" @click="goToProducts(cat.cateId)">
              <CategoryIcons :icon-type="getCategoryIcon(cat.cateId, cat.name)" :size="48" :color="getThemeColor(cat.cateId % 3, 'primary')" />
              <p>{{ cat.name }}</p>
            </div>
          </el-col>
        </el-row>
      </div>
      
      <!-- Hot Products -->
      <div class="hot-products mt-xl">
        <h2 class="section-title">Popular Products</h2>
        <el-row :gutter="40">
          <el-col :span="6" v-for="product in hotProducts" :key="product.prodId">
            <el-card :body-style="{ padding: '0px' }" shadow="hover" class="product-card">
              <img :src="product.mainImage || 'https://via.placeholder.com/300x200'" class="product-image" @click="goToDetail(product.prodId)" />
              <div class="product-info">
                <h4 class="product-name ellipsis">{{ product.name }}</h4>
                <div class="product-merchant">Merchant: {{ product.merchantName || 'Unknown' }} (ID: {{ product.merchId || '-' }})</div>
                <div class="product-price">
                  <span class="price">¥{{ product.price }}</span>
                </div>
                <div class="product-actions">
                  <el-button 
                    size="small"
                    :icon="productFavorites.get(product.prodId) ? StarFilledIcon : StarIcon"
                    @click="toggleFavorite(product)"
                  >
                    {{ productFavorites.get(product.prodId) ? 'Favorited' : 'Favorite' }}
                  </el-button>
                  <el-button type="primary" size="small" @click="addToCart(product)">
                    Add to Cart
                  </el-button>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </div>
    
    <CustomerFooter />
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import CustomerHeader from '@/components/layouts/CustomerHeader.vue'
import CustomerFooter from '@/components/layouts/CustomerFooter.vue'
import { getProductList, getCategoryList, addFavorite, removeFavorite, checkFavorite } from '@/api/product'
import { useCartStore } from '@/stores/cart'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'
import { Star, StarFilled } from '@element-plus/icons-vue'
import CategoryIcons from '@/components/icons/CategoryIcons.vue'

const router = useRouter()
const cartStore = useCartStore()
const userStore = useUserStore()

// Register icons
const StarIcon = Star
const StarFilledIcon = StarFilled

const banners = ref([
  { id: 1, title: 'Spring New Arrivals', subtitle: 'Discover our fresh collection of seasonal flowers', ctaText: 'Shop Now' },
  { id: 2, title: 'Hot-Selling Flower Picks', subtitle: 'Our most popular arrangements loved by customers', ctaText: 'View Collection' },
  { id: 3, title: 'Limited Time Offers', subtitle: 'Special discounts on premium flower selections', ctaText: 'Get Deals' }
])

// Theme color mapping
const getThemeColor = (index, type) => {
  const themes = [
    { primary: '#409EFF', bg: '#409EFF', accent: '#67C23A' }, // Blue theme
    { primary: '#67C23A', bg: '#67C23A', accent: '#E6A23C' }, // Green theme
    { primary: '#F7CAC9', bg: '#F7CAC9', accent: '#F56C6C' }  // Pink theme
  ];
  return themes[index % 3][type];
};

// Get appropriate icon type based on category name
const getCategoryIcon = (cateId, cateName = '') => {
  // Convert category name to lowercase for matching
  const name = cateName.toLowerCase();
  
  // Map category names to appropriate icon types
  if (name.includes('flower') || name.includes('花')) {
    return 'petal';
  } else if (name.includes('bouquet') || name.includes('束')) {
    return 'bouquet';
  } else if (name.includes('plant') || name.includes('plant') || name.includes('盆')) {
    return 'plant';
  } else if (name.includes('gift') || name.includes('gift') || name.includes('礼')) {
    return 'gift';
  } else if (name.includes('pot') || name.includes('vase') || name.includes('花瓶')) {
    return 'pot';
  } else if (name.includes('festival') || name.includes('节日') || name.includes('celebration')) {
    return 'festival';
  } else {
    // Default icon for unknown categories
    return 'default';
  }
};

const categories = ref([])
const hotProducts = ref([])

// Track favorite status for products
const productFavorites = ref(new Map())

onMounted(async () => {
  await loadData()
})

const loadData = async () => {
  try {
    // Load categories
    const catRes = await getCategoryList({ current: 1, size: 12 })
    categories.value = catRes.data?.records || []
    
    // Load hot products
    const prodRes = await getProductList({ current: 1, size: 8, status: 'ACTIVE' })
    hotProducts.value = prodRes.data?.records || []
    
    // Load favorite status for each product if user is logged in
    if (userStore.isLoggedIn) {
      await loadFavoriteStatus()
    }
  } catch (error) {
    console.error('Load data error:', error)
  }
}

// Navigation and action handlers
const goToProducts = (categoryId) => {
  router.push({ path: '/products', query: { categoryId } })
}

const goToDetail = (prodId) => {
  router.push(`/products/${prodId}`)
}

const addToCart = async (product) => {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('Please log in first')
    router.push('/login')
    return
  }
  
  try {
    await cartStore.addToCart({
      prodId: product.prodId,
      quantity: 1
    })
    ElMessage.success('Added to cart')
  } catch (error) {
    console.error('Add to cart error:', error)
  }
}

const loadFavoriteStatus = async () => {
  for (const product of hotProducts.value) {
    try {
      const res = await checkFavorite(product.prodId, userStore.userId)
      productFavorites.value.set(product.prodId, res.data || false)
    } catch (error) {
      console.error('Check favorite error:', error)
      productFavorites.value.set(product.prodId, false)
    }
  }
}

const toggleFavorite = async (product) => {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('Please log in first')
    router.push('/login')
    return
  }
  
  try {
    const isCurrentlyFavorited = productFavorites.value.get(product.prodId) || false
    if (isCurrentlyFavorited) {
      await removeFavorite(product.prodId, userStore.userId)
      productFavorites.value.set(product.prodId, false)
      ElMessage.success('Removed from favorites')
    } else {
      await addFavorite(product.prodId, userStore.userId)
      productFavorites.value.set(product.prodId, true)
      ElMessage.success('Added to favorites')
    }
  } catch (error) {
    ElMessage.error(error.message || 'Operation failed')
  }
}
</script>

<style scoped>
.customer-home {
  min-height: 100vh;
  background: var(--color-bg-base);
}

.hero-carousel-item {
  position: relative;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
}

.hero-carousel-item.carousel-theme-1 {
  background: linear-gradient(135deg, #e3f2fd 0%, #bbdefb 100%); /* Blue theme */
}

.hero-carousel-item.carousel-theme-2 {
  background: linear-gradient(135deg, #e8f5e9 0%, #c8e6c9 100%); /* Green theme */
}

.hero-carousel-item.carousel-theme-3 {
  background: linear-gradient(135deg, #fce4ec 0%, #f8bbd0 100%); /* Pink theme */
}

/* Specific theme adjustments */
.carousel-theme-1 .carousel-title,
.carousel-theme-1 .carousel-subtitle {
  color: #1976d2;
}

.carousel-theme-2 .carousel-title,
.carousel-theme-2 .carousel-subtitle {
  color: #388e3c;
}

.carousel-theme-3 .carousel-title,
.carousel-theme-3 .carousel-subtitle {
  color: #c2185b;
}

/* Button styling for each theme */
.carousel-theme-1 .el-button--primary {
  background-color: #409EFF;
  border-color: #409EFF;
}

.carousel-theme-2 .el-button--primary {
  background-color: #67C23A;
  border-color: #67C23A;
}

.carousel-theme-3 .el-button--primary {
  background-color: #F7CAC9;
  border-color: #F7CAC9;
  color: #303133;
}

.svg-background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1;
}

.hero-svg {
  width: 100%;
  height: 100%;
}

/* Animations for SVG elements */
@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-10px); }
}

.hero-carousel-item {
  animation: slide-in 0.8s ease-out;
}

@keyframes slide-in {
  from { opacity: 0; transform: translateX(50px); }
  to { opacity: 1; transform: translateX(0); }
}

.theme-1 g, .theme-2 g, .theme-3 g {
  animation: float 4s ease-in-out infinite;
}

.hero-carousel-item g {
  animation-duration: 6s;
}

.hero-carousel-item g:nth-child(2) {
  animation-delay: 0.5s;
}

.hero-carousel-item g:nth-child(3) {
  animation-delay: 1s;
}

.hero-carousel-item g:nth-child(4) {
  animation-delay: 1.5s;
}

.hero-carousel-item g:nth-child(5) {
  animation-delay: 2s;
}

.carousel-content {
  position: relative;
  z-index: 2;
  text-align: center;
  padding: var(--spacing-lg);
  max-width: 600px;
}

.carousel-title {
  font-size: 32px;
  font-weight: bold;
  margin-bottom: var(--spacing-md);
  color: var(--color-text-primary);
  text-shadow: 1px 1px 2px rgba(255, 255, 255, 0.5);
}

.carousel-subtitle {
  font-size: 18px;
  color: var(--color-text-secondary);
  margin-bottom: var(--spacing-lg);
  text-shadow: 1px 1px 2px rgba(255, 255, 255, 0.5);
}

.cta-button {
  font-size: 16px;
  padding: 12px 24px;
}

.section-title {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: var(--spacing-lg);
  padding-bottom: var(--spacing-sm);
  border-bottom: 2px solid var(--color-primary);
}

.category-card {
  background: white;
  padding: var(--spacing-lg);
  border-radius: var(--radius-lg);
  text-align: center;
  cursor: pointer;
  transition: all 0.3s;
  margin-bottom: 20px;
}

.category-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-light);
}

.category-card p {
  margin-top: var(--spacing-md);
  font-weight: 500;
}

.product-card {
  cursor: pointer;
  transition: all 0.3s;
  margin-bottom: var(--spacing-xl);
}

.product-card:hover {
  transform: translateY(-8px);
  box-shadow: var(--shadow-light);
}

.product-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.product-info {
  padding: var(--spacing-md);
}

.product-name {
  font-size: var(--font-size-md);
  margin-bottom: var(--spacing-sm);
}

.product-merchant {
  color: var(--color-text-secondary);
  font-size: 12px;
  margin-bottom: 6px;
}

.product-price {
  margin-bottom: var(--spacing-md);
}

.price {
  font-size: var(--font-size-xl);
  color: var(--color-danger);
  font-weight: bold;
}

.original-price {
  margin-left: var(--spacing-sm);
  text-decoration: line-through;
  color: var(--color-text-secondary);
  font-size: var(--font-size-sm);
}

.product-actions {
  display: flex;
  gap: var(--spacing-sm);
}
</style>
