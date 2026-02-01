<template>
  <div class="my-favorites">
    <el-card>
      <template #header>
        <div class="card-header">
          <h2>My Favorites</h2>
          <div class="header-actions">
            <el-button 
              v-if="selectedIds.length > 0" 
              type="danger" 
              @click="handleBatchDelete"
            >
              Batch Unfavorite ({{ selectedIds.length }})
            </el-button>
            <el-button @click="handleSelectAll">
              {{ isAllSelected ? 'Unselect All' : 'Select All' }}
            </el-button>
          </div>
        </div>
      </template>

      <div v-loading="loading" class="favorites-content">
        <el-empty v-if="!loading && favoriteList.length === 0" description="No favorites yet" />
        
        <el-row :gutter="20" v-else>
          <el-col :span="6" v-for="product in favoriteList" :key="product.prodId">
            <el-card 
              :body-style="{ padding: '0px' }" 
              shadow="hover" 
              class="product-card"
              :class="{ 'is-selected': selectedIds.includes(product.prodId) }"
            >
              <div class="product-selection">
                <el-checkbox 
                  :model-value="selectedIds.includes(product.prodId)"
                  @change="handleSelect(product.prodId)"
                />
              </div>
              
              <img 
                :src="product.mainImage || 'https://via.placeholder.com/300x200'" 
                class="product-image" 
                @click="goToDetail(product.prodId)" 
              />
              
              <div class="product-info">
                <h4 class="product-name" @click="goToDetail(product.prodId)">
                  {{ product.name }}
                </h4>
                <div class="product-merchant">Merchant: {{ product.merchantName || 'Unknown' }} (ID: {{ product.merchId || '-' }})</div>
                <div class="product-price">
                  <span class="price">¥{{ product.price }}</span>

                  <span class="original-price" v-if="product.originalPrice">
                    ¥{{ product.originalPrice }}
                  </span>
                </div>
                <div class="product-actions">
                  <el-button 
                    type="danger" 
                    size="small" 
                    @click="handleDelete(product.prodId)"
                  >
                    Remove
                  </el-button>
                  <el-button 
                    type="primary" 
                    size="small" 
                    @click="addToCart(product)"
                  >
                    Add to Cart
                  </el-button>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { useCartStore } from '@/stores/cart'
import request from '@/utils/request'

const router = useRouter()
const userStore = useUserStore()
const cartStore = useCartStore()

const loading = ref(false)
const favoriteList = ref([])
const selectedIds = ref([])

const isAllSelected = computed(() => {
  return favoriteList.value.length > 0 && 
         selectedIds.value.length === favoriteList.value.length
})

onMounted(() => {
  loadFavorites()
})

const loadFavorites = async () => {
  loading.value = true
  try {
    const res = await request({
      url: '/api/products/favorites',
      method: 'get',
      params: {
        userId: userStore.userId
      }
    })
    favoriteList.value = res.data || []
    } catch (error) {
    console.error('Load favorites error:', error)
    ElMessage.error(error.message || 'Failed to load favorites')
  } finally {
    loading.value = false
  }
}

const goToDetail = (prodId) => {
  router.push(`/products/${prodId}`)
}

const handleSelect = (prodId) => {
  const index = selectedIds.value.indexOf(prodId)
  if (index > -1) {
    selectedIds.value.splice(index, 1)
  } else {
    selectedIds.value.push(prodId)
  }
}

const handleSelectAll = () => {
  if (isAllSelected.value) {
    selectedIds.value = []
  } else {
    selectedIds.value = favoriteList.value.map(item => item.prodId)
  }
}

const handleDelete = async (prodId) => {
  try {
    await ElMessageBox.confirm('Are you sure you want to remove this item from favorites?', 'Confirmation', {
      type: 'warning'
    })
    
    await request({
      url: `/api/products/${prodId}/favorite`,
      method: 'delete',
      data: { userId: userStore.userId }
    })
    
    ElMessage.success('Removed from favorites')
    loadFavorites()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Delete favorite error:', error)
      ElMessage.error(error.message || 'Operation failed')
    }
  }
}

const handleBatchDelete = async () => {
  try {
    await ElMessageBox.confirm(
      `Are you sure you want to remove ${selectedIds.value.length} selected items from favorites?`, 
      'Confirmation', 
      {
        type: 'warning'
      }
    )
    
    // Batch delete
    await Promise.all(
      selectedIds.value.map(prodId => 
        request({
          url: `/api/products/${prodId}/favorite`,
          method: 'delete',
          data: { userId: userStore.userId }
        })
      )
    )
    
    ElMessage.success('Batch removal successful')
    selectedIds.value = []
    loadFavorites()
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Batch delete favorites error:', error)
      ElMessage.error(error.message || 'Operation failed')
    }
  }
}

const addToCart = async (product) => {
  try {
    await cartStore.addToCart({
      prodId: product.prodId,
      quantity: 1
    })
    ElMessage.success('Added to cart')
  } catch (error) {
    console.error('Add to cart error:', error)
    ElMessage.error(error.message || 'Add failed')
  }
}
</script>

<style scoped>
.my-favorites {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h2 {
  margin: 0;
  font-size: 20px;
  font-weight: 500;
}

.header-actions {
  display: flex;
  gap: 12px;
}

.favorites-content {
  min-height: 400px;
}

.product-card {
  margin-bottom: 20px;
  cursor: pointer;
  transition: all 0.3s;
  position: relative;
}

.product-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.product-card.is-selected {
  border: 2px solid #409eff;
}

.product-selection {
  position: absolute;
  top: 8px;
  left: 8px;
  z-index: 10;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 4px;
  padding: 4px;
}

.product-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
  cursor: pointer;
}

.product-info {
  padding: 12px;
}

.product-name {
  margin: 0 0 8px 0;
  font-size: 14px;
  font-weight: 500;
  color: #303133;
  cursor: pointer;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.product-name:hover {
  color: #409eff;
}

.product-price {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;
}

.price {
  font-size: 20px;
  font-weight: bold;
  color: #f56c6c;

}

.original-price {
  font-size: 14px;
  color: #909399;
  text-decoration: line-through;
}

.product-actions {
  display: flex;
  gap: 8px;
}

.product-actions .el-button {
  flex: 1;
}
</style>
