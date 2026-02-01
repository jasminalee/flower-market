<template>
  <div class="customer-page">
    <CustomerHeader />
    
    <div class="container">
      <div class="cart-page">
        <el-card class="mt-lg">
          <template #header>
            <div class="card-header">
              <h2>Shopping Cart</h2>
              <el-button 
                text 
                type="danger" 
                @click="handleClearCart"
                v-if="cartStore.cartItems.length > 0"
              >
                Clear Cart
              </el-button>
            </div>
          </template>
          
          <div v-loading="cartStore.loading">
            <el-table
              :data="cartStore.cartItems"
              v-if="cartStore.cartItems.length > 0"
              @selection-change="handleSelectionChange"
            >
              <el-table-column type="selection" width="55" />
              
              <el-table-column label="Product" width="400">
                <template #default="{ row }">
                  <div class="product-info">
                    <el-image 
                      :src="row.prodImage || 'https://via.placeholder.com/80x80'" 
                      fit="cover"
                      style="width: 80px; height: 80px; border-radius: 4px;"
                    />
                    <div class="product-details">
                      <div class="product-name">{{ row.prodName }}</div>
                      <div class="product-merchant">Merchant: {{ row.merchantName || 'Unknown' }} (ID: {{ row.merchId || '-' }})</div>
                      <div class="product-desc">{{ row.prodDesc }}</div>
                    </div>
                  </div>
                </template>
              </el-table-column>
              
              <el-table-column label="Unit Price" prop="prodPrice" width="150">
                <template #default="{ row }">
                  <span class="price">¥{{ row.prodPrice }}</span>
                </template>
              </el-table-column>
              
              <el-table-column label="Quantity" width="200">
                <template #default="{ row }">
                  <el-input-number
                    :model-value="row.quantity"
                    :min="1"
                    :max="row.prodStock || 999"
                    @change="(val) => handleQuantityChange(row, val)"
                  />
                </template>
              </el-table-column>
              
              <el-table-column label="Subtotal" width="150">
                <template #default="{ row }">
                  <span class="subtotal">¥{{ (row.prodPrice * row.quantity).toFixed(2) }}</span>
                </template>
              </el-table-column>
              
              <el-table-column label="Actions" width="100">
                <template #default="{ row }">
                  <el-button 
                    text 
                    type="danger" 
                    @click="handleRemoveItem(row)"
                  >
                    Remove
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
            
            <el-empty v-else description="Your cart is empty" />
          </div>
          
          <div class="cart-footer" v-if="cartStore.cartItems.length > 0">
            <div class="footer-left">
              <el-checkbox 
                v-model="isAllSelected" 
                @change="handleSelectAll"
              >
                Select All
              </el-checkbox>
                <el-button 
                text 
                type="danger" 
                @click="handleBatchDelete"
                :disabled="selectedItems.length === 0"
              >
                Delete Selected
              </el-button>
            </div>
            
            <div class="footer-right">
                <div class="total-info">
                <span>Selected items: {{ selectedItems.length }}</span>
                <span class="total-label">Total:</span>
                <span class="total-price">¥{{ totalPrice.toFixed(2) }}</span>
              </div>
              <el-button 
                type="primary" 
                size="large"
                @click="handleCheckout"
                :disabled="selectedItems.length === 0"
              >
                Checkout
              </el-button>
            </div>
          </div>
        </el-card>
      </div>
    </div>
    
    <CustomerFooter />
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import CustomerHeader from '@/components/layouts/CustomerHeader.vue'
import CustomerFooter from '@/components/layouts/CustomerFooter.vue'
import { useCartStore } from '@/stores/cart'
import { useUserStore } from '@/stores/user'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const cartStore = useCartStore()
const userStore = useUserStore()

const selectedItems = ref([])

const isAllSelected = computed({
  get() {
    return selectedItems.value.length === cartStore.cartItems.length && cartStore.cartItems.length > 0
  },
  set(val) {
    if (val) {
      selectedItems.value = cartStore.cartItems.map(item => item.id)
    } else {
      selectedItems.value = []
    }
  }
})

const totalPrice = computed(() => {
  return cartStore.cartItems
    .filter(item => selectedItems.value.includes(item.id))
    .reduce((total, item) => total + (item.prodPrice || 0) * item.quantity, 0)
})

onMounted(async () => {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('Please log in first')
    router.push('/login')
    return
  }
  
  await cartStore.loadCart()
})

const handleSelectionChange = (selection) => {
  selectedItems.value = selection.map(item => item.id)
}

const handleSelectAll = (val) => {
  isAllSelected.value = val
}

const handleQuantityChange = async (row, newQuantity) => {
  try {
    await cartStore.updateQuantity(row.id, newQuantity)
    ElMessage.success('Quantity updated')
  } catch (error) {
    ElMessage.error(error.message || 'Update failed')
  }
}

const handleRemoveItem = async (row) => {
  try {
    await ElMessageBox.confirm('Are you sure you want to remove this item?', 'Confirm', {
      confirmButtonText: 'OK',
      cancelButtonText: 'Cancel',
      type: 'warning'
    })
    
    await cartStore.removeItem(row.id)
    selectedItems.value = selectedItems.value.filter(id => id !== row.id)
    ElMessage.success('Removed')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || 'Remove failed')
    }
  }
}

const handleBatchDelete = async () => {
  try {
    await ElMessageBox.confirm(`Are you sure you want to remove the selected ${selectedItems.value.length} items?`, 'Confirm', {
      confirmButtonText: 'OK',
      cancelButtonText: 'Cancel',
      type: 'warning'
    })
    
    await cartStore.batchRemove(selectedItems.value)
    selectedItems.value = []
    ElMessage.success('Removed')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || 'Remove failed')
    }
  }
}

const handleClearCart = async () => {
  try {
    await ElMessageBox.confirm('Are you sure you want to clear the cart?', 'Confirm', {
      confirmButtonText: 'OK',
      cancelButtonText: 'Cancel',
      type: 'warning'
    })
    
    await cartStore.clearCart()
    selectedItems.value = []
    ElMessage.success('Cart cleared')
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || 'Clear failed')
    }
  }
}

const handleCheckout = () => {
  if (selectedItems.value.length === 0) {
    ElMessage.warning('Please select items to checkout')
    return
  }
  
  // Pass selected product IDs to checkout page
  router.push({
    path: '/checkout',
    query: { items: selectedItems.value.join(',') }
  })
}
</script>

<style scoped>
.customer-page {
  min-height: 100vh;
  background: var(--color-bg-base);
}

.cart-page {
  padding-bottom: var(--spacing-xl);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h2 {
  margin: 0;
  font-size: 20px;
  font-weight: 600;
}

.product-info {
  display: flex;
  gap: var(--spacing-md);
}

.product-details {
  flex: 1;
}

.product-name {
  font-weight: 500;
  margin-bottom: var(--spacing-xs);
  color: var(--color-text-primary);
}

.product-desc {
  font-size: 12px;
  color: var(--color-text-secondary);
}

.product-merchant {
  color: var(--color-text-secondary);
  font-size: 12px;
}

.price {
  color: var(--color-danger);
  font-weight: 500;
}

.subtotal {
  color: var(--color-danger);
  font-size: 16px;
  font-weight: 600;
}

.cart-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: var(--spacing-xl);
  padding-top: var(--spacing-lg);
  border-top: 1px solid var(--color-border);
}

.footer-left {
  display: flex;
  align-items: center;
  gap: var(--spacing-lg);
}

.footer-right {
  display: flex;
  align-items: center;
  gap: var(--spacing-xl);
}

.total-info {
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
  font-size: 14px;
}

.total-label {
  font-weight: 500;
}

.total-price {
  color: var(--color-danger);
  font-size: 24px;
  font-weight: 600;
}

:deep(.el-table) {
  font-size: 14px;
}
</style>
