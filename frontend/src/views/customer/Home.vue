<template>
  <div class="customer-home">
    <CustomerHeader />
    
    <div class="container">
      <!-- Banner Carousel with Frosted Glass Effect -->
      <el-carousel height="400px" class="mt-lg banner-carousel">
        <el-carousel-item v-for="item in banners" :key="item.id">
          <div class="banner-carousel-item">
            <img :src="item.image" class="banner-bg-image" />
            <div class="frosted-glass-overlay">
              <div class="carousel-content">
                <h3 class="carousel-title">{{ item.title }}</h3>
                <p class="carousel-subtitle">{{ item.subtitle }}</p>
                <el-button type="primary" class="cta-button" @click="router.push(item.route)">{{ item.ctaText }}</el-button>
              </div>
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

import banner1 from '@/assets/image/home/banner1.jpg'
import banner2 from '@/assets/image/home/banner2.jpg'
import banner3 from '@/assets/image/home/banner3.jpg'

const router = useRouter()
const cartStore = useCartStore()
const userStore = useUserStore()

// Register icons
const StarIcon = Star
const StarFilledIcon = StarFilled

const banners = ref([
  { id: 1, title: 'Spring New Arrivals', subtitle: 'Discover our fresh collection of seasonal flowers', ctaText: 'Shop Now', image: banner1, route: '/products' },
  { id: 2, title: 'Hot-Selling Flower Picks', subtitle: 'Our most popular arrangements loved by customers', ctaText: 'View Collection', image: banner2, route: '/products' },
  { id: 3, title: 'Limited Time Offers', subtitle: 'Special discounts on premium flower selections', ctaText: 'Get Deals', image: banner3, route: '/products' }
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

/* === Banner Carousel with Frosted Glass Effect === */
.banner-carousel-item {
  position: relative;
  width: 100%;
  height: 100%;
  overflow: hidden;
}

.banner-bg-image {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  object-fit: cover;
  z-index: 0;
}

.frosted-glass-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1;
  display: flex;
  justify-content: center;
  align-items: center;
  text-align: center;

  /* Frosted glass effect - light transparent */
  background-image: linear-gradient(45deg, rgba(255, 102, 65, 0.05), rgba(228, 70, 122, 0.05));
  backdrop-filter: blur(3px);
  -webkit-backdrop-filter: blur(3px);

  /* Smooth transition */
  transition: backdrop-filter 0.4s ease, -webkit-backdrop-filter 0.4s ease;
}

.banner-carousel-item:hover .frosted-glass-overlay {
  backdrop-filter: blur(0px);
  -webkit-backdrop-filter: blur(0px);
  background-image: linear-gradient(45deg, rgba(255, 102, 65, 0), rgba(228, 70, 122, 0));
}

.carousel-content {
  position: relative;
  z-index: 2;
  text-align: center;
  padding: 30px 40px;
  max-width: 600px;
  border-radius: 12px;
  background-color: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(8px);
  -webkit-backdrop-filter: blur(8px);
  border: 1px solid rgba(255, 255, 255, 0.25);
  box-shadow: 0 4px 30px rgba(0, 0, 0, 0.1);
}

.carousel-title {
  font-size: 32px;
  font-weight: bold;
  margin-bottom: 12px;
  color: #fff;
  text-shadow: 1px 1px 4px rgba(0, 0, 0, 0.5);
}

.carousel-subtitle {
  font-size: 18px;
  margin-bottom: 20px;
  color: #fff;
  text-shadow: 1px 1px 3px rgba(0, 0, 0, 0.5);
}

.cta-button {
  font-size: 16px;
  padding: 12px 24px;
  background-color: rgba(64, 158, 255, 0.6) !important;
  border-color: rgba(255, 255, 255, 0.3) !important;
  backdrop-filter: blur(4px);
  -webkit-backdrop-filter: blur(4px);
  transition: all 0.3s ease;
}

.cta-button:hover {
  background-color: rgba(64, 158, 255, 0.85) !important;
  border-color: rgba(255, 255, 255, 0.5) !important;
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
