<template>
  <div class="customer-page">
    <CustomerHeader />
    
    <div class="container">
      <div class="product-list-page">
        <!-- Filter bar -->
        <div class="filter-bar mt-lg">
          <el-card shadow="never">
            <el-form :inline="true">
              <el-form-item label="Category">
                <el-select v-model="filters.catId" placeholder="All Categories" clearable @change="handleFilter">
                  <el-option
                    v-for="cat in categories"
                    :key="cat.cateId"
                    :label="cat.name"
                    :value="cat.cateId"
                  />
                </el-select>
              </el-form-item>
              
              <el-form-item label="Price Range">
                <el-input-number v-model="filters.minPrice" placeholder="Min" :min="0" style="width: 120px" />
                <span style="margin: 0 8px">-</span>
                <el-input-number v-model="filters.maxPrice" placeholder="Max" :min="0" style="width: 120px" />
              </el-form-item>
              
              <el-form-item label="Sort">
                <el-select v-model="filters.sortBy" placeholder="Sort By" @change="handleFilter">
                  <el-option label="Default" value="" />
                  <el-option label="Price: Low to High" value="price_asc" />
                  <el-option label="Price: High to Low" value="price_desc" />
                  <el-option label="Sales: High to Low" value="sales_desc" />
                </el-select>
              </el-form-item>
              
              <el-form-item>
                <el-button type="primary" @click="handleFilter">Filter</el-button>
                <el-button @click="handleReset">Reset</el-button>
              </el-form-item>
            </el-form>
          </el-card>
        </div>
        
        <!-- Products grid -->
        <div class="products-grid mt-lg" v-loading="loading">
          <el-row :gutter="20" v-if="products.length > 0">
            <el-col :span="6" v-for="product in products" :key="product.prodId">
              <el-card :body-style="{ padding: '0px' }" shadow="hover" class="product-card">
                <img 
                  :src="product.mainImage || 'https://via.placeholder.com/300x200'" 
                  class="product-image" 
                  @click="goToDetail(product.prodId)" 
                  @error="onImageError(product)"
                  @load="onImageLoad(product)"
                  crossorigin="anonymous"
                />
                <div class="product-info">
                  <h4 class="product-name ellipsis" @click="goToDetail(product.prodId)">{{ product.name }}</h4>
                  <div class="product-merchant">Merchant: {{ product.merchantName || 'Unknown' }} (ID: {{ product.merchId || '-' }})</div>
                  <div class="product-price">
                    <span class="price">¥{{ product.price }}</span>
                    <span class="sales">Sold {{ product.sales || 0 }}</span>
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
          
          <el-empty v-else description="No products" />
        </div>
        
        <!-- Pagination -->
        <div class="pagination-wrapper mt-lg" v-if="total > 0">
          <el-pagination
            v-model:current-page="pagination.current"
            v-model:page-size="pagination.size"
            :page-sizes="[12, 24, 36, 48]"
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
import { ref, reactive, onMounted, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import CustomerHeader from '@/components/layouts/CustomerHeader.vue'
import CustomerFooter from '@/components/layouts/CustomerFooter.vue'
import { getProductList, getCategoryList, addFavorite, removeFavorite, checkFavorite } from '@/api/product'
import { useCartStore } from '@/stores/cart'
import { Star, StarFilled } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { ElMessage } from 'element-plus'

const router = useRouter()
const route = useRoute()
const cartStore = useCartStore()
const userStore = useUserStore()

const loading = ref(false)
const products = ref([])

// Track favorite status for products
const productFavorites = ref(new Map())

// Register icons
const StarIcon = Star
const StarFilledIcon = StarFilled
const categories = ref([])
const total = ref(0)

const filters = reactive({
  catId: route.query.catId || '',
  keyword: route.query.keyword || '',
  minPrice: null,
  maxPrice: null,
  sortBy: ''
})

const pagination = reactive({
  current: 1,
  size: 12
})

onMounted(async () => {
  await loadCategories()
  await loadProducts()
})

// Watch route changes
watch(() => route.query, (newQuery) => {
  filters.catId = newQuery.catId || ''
  filters.keyword = newQuery.keyword || ''
  pagination.current = 1
  loadProducts()
}, { deep: true })

const loadCategories = async () => {
  try {
    const res = await getCategoryList({ current: 1, size: 100 })
    categories.value = res.data?.records || []
  } catch (error) {
    console.error('Load categories error:', error)
  }
}

const loadProducts = async () => {
  loading.value = true
  try {
    const params = {
      current: pagination.current,
      size: pagination.size,
      status: 'ACTIVE'
    }
    
    if (filters.catId) {
      params.catId = filters.catId
    }
    
    if (filters.keyword) {
      params.keyword = filters.keyword
    }
    
    if (filters.minPrice !== null && filters.minPrice >= 0) {
      params.minPrice = filters.minPrice
    }
    
    if (filters.maxPrice !== null && filters.maxPrice >= 0) {
      params.maxPrice = filters.maxPrice
    }
    
    // Handle sorting
    if (filters.sortBy === 'price_asc') {
      params.sortBy = 'price'
      params.sortOrder = 'asc'
    } else if (filters.sortBy === 'price_desc') {
      params.sortBy = 'price'
      params.sortOrder = 'desc'
    } else if (filters.sortBy === 'sales_desc') {
      params.sortBy = 'sales'
      params.sortOrder = 'desc'
    }
    
    const res = await getProductList(params)
    console.log('Raw product list data:', res.data?.records)
    products.value = res.data?.records || []
    total.value = res.data?.total || 0
    
    // Debug product images
    products.value.forEach(product => {
      console.log('Product:', product.name, 'Main image path:', product.mainImage)
      if (!product.mainImage || product.mainImage === 'null' || product.mainImage === 'undefined') {
        console.warn('Product', product.name, 'has invalid main image path:', product.mainImage)
      } else {
        console.log('Product', product.name, 'image URL will be:', product.mainImage)
      }
    })
    
    // Load favorite status for each product if user is logged in
    if (userStore.isLoggedIn) {
      await loadFavoriteStatus()
    }
  } catch (error) {
    console.error('Load products error:', error)
    ElMessage.error('Failed to load product list')
  } finally {
    loading.value = false
  }
}

const handleFilter = () => {
  pagination.current = 1
  loadProducts()
}

const handleReset = () => {
  filters.catId = ''
  filters.minPrice = null
  filters.maxPrice = null
  filters.sortBy = ''
  pagination.current = 1
  
  // Clear URL parameters
  router.push({ path: '/products' })
}

const handleSizeChange = () => {
  loadProducts()
}

const handleCurrentChange = () => {
  loadProducts()
  window.scrollTo({ top: 0, behavior: 'smooth' })
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
    ElMessage.success('Added to cart successfully')
  } catch (error) {
    ElMessage.error(error.message || 'Failed to add')
  }
}

const loadFavoriteStatus = async () => {
  for (const product of products.value) {
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

// Image event handlers for debugging
const onImageError = (product) => {
  console.error('Image failed to load for product:', product.name, 'at path:', product.mainImage)
  console.log('Full product object:', product)
  console.log('Image URL attempted:', product.mainImage)
}

const onImageLoad = (product) => {
  console.log('Image loaded successfully for product:', product.name, 'at path:', product.mainImage)
}
</script>

<style scoped>
.customer-page {
  min-height: 100vh;
  background: var(--color-bg-base);
}

.product-list-page {
  padding-bottom: var(--spacing-xl);
}

.filter-bar :deep(.el-card__body) {
  padding: var(--spacing-md);
}

.filter-bar :deep(.el-form-item) {
  margin-bottom: 0;
}

.products-grid {
  min-height: 400px;
}

.product-card {
  margin-bottom: var(--spacing-lg);
  transition: transform 0.3s;
}

.product-card:hover {
  transform: translateY(-4px);
}

.product-image {
  width: 100%;
  height: 240px;
  object-fit: cover;
  cursor: pointer;
}

.product-merchant {
  color: var(--color-text-secondary);
  font-size: 12px;
  margin-bottom: 6px;
}

.product-info {
  padding: var(--spacing-md);
}

.product-name {
  font-size: 16px;
  font-weight: 500;
  margin-bottom: var(--spacing-sm);
  cursor: pointer;
  color: var(--color-text-primary);
}

.product-name:hover {
  color: var(--color-primary);
}

.product-price {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--spacing-md);
}

.price {
  color: var(--color-danger);
  font-size: 20px;
  font-weight: 600;
}

.sales {
  color: var(--color-text-secondary);
  font-size: 12px;
}

.product-actions {
  display: flex;
  gap: var(--spacing-sm);
}

.product-actions .el-button {
  flex: 1;
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
}

.ellipsis {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style>
