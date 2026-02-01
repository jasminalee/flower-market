<template>
  <div class="customer-header">
    <div class="container">
      <div class="header-content flex-between">
        <router-link to="/" class="logo">
          <el-icon :size="32"><Shop /></el-icon>
          <span>Flower Market</span>
        </router-link>
        
        <div class="search-box">
          <el-input
            v-model="searchKeyword"
            placeholder="Search products"
            clearable
            @keyup.enter="handleSearch"
          >
            <template #append>
              <el-button :icon="Search" @click="handleSearch" />
            </template>
          </el-input>
        </div>
        
        <div class="header-actions flex">
          <router-link to="/cart" class="action-item">
            <el-badge :value="cartStore.cartCount" :max="99">
              <el-icon :size="24"><ShoppingCart /></el-icon>
            </el-badge>
            <span>Cart</span>
          </router-link>
          
          <template v-if="userStore.isLoggedIn">
            <el-dropdown @command="handleCommand">
              <span class="el-dropdown-link">
                <el-avatar :size="32" :src="userStore.avatar">
                  {{ userStore.realName?.charAt(0) || 'U' }}
                </el-avatar>
                <span class="ml-sm">{{ userStore.realName || userStore.username }}</span>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="profile">
                    <el-icon><User /></el-icon>
                    Profile
                  </el-dropdown-item>
                  <el-dropdown-item command="orders">
                    <el-icon><DocumentCopy /></el-icon>
                    My Orders
                  </el-dropdown-item>
                  <el-dropdown-item command="favorites">
                    <el-icon><StarFilled /></el-icon>
                    My Favorites
                  </el-dropdown-item>
                  <el-dropdown-item command="coupons">
                    <el-icon><Ticket /></el-icon>
                    My Coupons
                  </el-dropdown-item>
                  <el-dropdown-item divided command="logout">
                    <el-icon><SwitchButton /></el-icon>
                    Log out
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          <template v-else>
            <router-link to="/login" class="action-item">
              <el-button type="primary">Log in</el-button>
            </router-link>
            <router-link to="/register" class="action-item">
              <el-button>Sign up</el-button>
            </router-link>
          </template>
        </div>
      </div>
      
      <div class="nav-menu">
        <el-menu
          mode="horizontal"
          :default-active="activeMenu"
          router
        >
          <el-menu-item index="/home">Home</el-menu-item>
          <el-menu-item index="/products">Products</el-menu-item>
          <el-menu-item index="/knowledge">Care Tips</el-menu-item>
          <el-menu-item index="/coupons">Coupons</el-menu-item>
        </el-menu>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { useCartStore } from '@/stores/cart'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const cartStore = useCartStore()

const searchKeyword = ref('')
const activeMenu = computed(() => route.path)

const handleSearch = () => {
  if (!searchKeyword.value.trim()) {
    return
  }
  router.push({
    path: '/products',
    query: { keyword: searchKeyword.value }
  })
}

const handleCommand = async (command) => {
  if (command === 'logout') {
    await ElMessageBox.confirm('Are you sure you want to log out?', 'Confirm', {
      confirmButtonText: 'OK',
      cancelButtonText: 'Cancel',
      type: 'warning'
    })
    await userStore.logout()
    ElMessage.success('Logged out')
    router.push('/login')
  } else if (command === 'profile') {
    router.push('/profile')
  } else if (command === 'orders') {
    router.push('/profile/orders')
  } else if (command === 'favorites') {
    router.push('/profile/favorites')
  } else if (command === 'coupons') {
    router.push('/profile/coupons')
  }
}
</script>

<style scoped>
.customer-header {
  background: var(--color-bg-white);
  box-shadow: var(--shadow-base);
  position: sticky;
  top: 0;
  z-index: 1000;
}

.header-content {
  padding: var(--spacing-md) 0;
}

.logo {
  display: flex;
  align-items: center;
  gap: var(--spacing-sm);
  font-size: var(--font-size-xl);
  font-weight: bold;
  color: var(--color-primary);
}

.search-box {
  flex: 1;
  max-width: 600px;
  margin: 0 var(--spacing-xl);
}

.header-actions {
  gap: var(--spacing-lg);
}

.action-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--spacing-xs);
  cursor: pointer;
}

.el-dropdown-link {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.nav-menu {
  border-top: 1px solid var(--color-border-lighter);
}
</style>
