<template>
  <el-container class="merchant-layout">
    <!-- Top navigation -->
    <el-header class="merchant-header">
      <div class="header-left">
        <span class="logo">🏪</span>
        <span class="title">Merchant Portal</span>
      </div>
      <div class="header-right">
        <el-button type="primary" link @click="goToHome" class="home-btn">
          <el-icon><HomeFilled /></el-icon>
          Home
        </el-button>
        <el-dropdown @command="handleCommand" class="user-dropdown">
          <div class="user-info">
            <el-avatar :size="32" class="avatar">
              {{ merchantName.charAt(0) }}
            </el-avatar>
            <span class="username">{{ merchantName }}</span>
            <el-icon class="el-icon--right"><ArrowDown /></el-icon>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item disabled>
                <div class="user-detail">
                  <div>{{ merchantName }}</div>
                  <div class="user-email">Merchant Account</div>
                </div>
              </el-dropdown-item>
              <el-dropdown-item command="profile">
                <el-icon><User /></el-icon>
                Profile
              </el-dropdown-item>
              <el-dropdown-item command="logout" divided>
                <el-icon><SwitchButton /></el-icon>
                Logout
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </el-header>

    <el-container class="main-container">
      <!-- Sidebar -->
      <el-aside width="200px" class="merchant-aside">
        <el-menu :default-active="$route.path" router>
          <el-menu-item index="/merchant/dashboard">
            <el-icon><DataAnalysis /></el-icon>
            <span>Dashboard</span>
          </el-menu-item>
          <el-menu-item index="/merchant/products">
            <el-icon><Goods /></el-icon>
            <span>Products</span>
          </el-menu-item>
          <el-menu-item index="/merchant/orders">
            <el-icon><Document /></el-icon>
            <span>Orders</span>
          </el-menu-item>
          <el-menu-item index="/merchant/coupons">
            <el-icon><Ticket /></el-icon>
            <span>Coupons</span>
          </el-menu-item>
          <el-menu-item index="/merchant/profile">
            <el-icon><Shop /></el-icon>
            <span>Profile</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <!-- Main content area -->
      <el-main class="merchant-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  HomeFilled,
  ArrowDown,
  SwitchButton,
  User,
  DataAnalysis,
  Goods,
  Document,
  Ticket,
  Shop
} from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()

const merchantName = computed(() => userStore.username || 'Merchant')

const goToHome = () => {
  router.push('/')
}

const handleCommand = async (command) => {
  if (command === 'logout') {
      try {
      await ElMessageBox.confirm('Are you sure you want to log out?', 'Confirm', {
        confirmButtonText: 'Yes',
        cancelButtonText: 'No',
        type: 'warning'
      })
      
      userStore.logout()
      ElMessage.success('Logged out')
      router.push('/home')
    } catch {
      // User cancelled
    }
  } else if (command === 'profile') {
    router.push('/merchant/profile')
  }
}
</script>

<style scoped>
.merchant-layout {
  min-height: 100vh;
  background: #f5f7fa;
}

/* Top navigation bar */
.merchant-header {
  background: #fff;
  border-bottom: 1px solid #e4e7ed;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  height: 60px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 10px;
}

.logo {
  font-size: 24px;
}

.title {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 20px;
}

.home-btn {
  font-size: 14px;
}

.user-dropdown {
  cursor: pointer;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 5px 10px;
  border-radius: 4px;
  transition: background-color 0.3s;
}

.user-info:hover {
  background-color: #f5f7fa;
}

.avatar {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
  color: #fff;
  font-weight: 600;
}

.username {
  font-size: 14px;
  color: #303133;
}

.user-detail {
  padding: 5px 0;
}

.user-email {
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
}

/* Main container */
.main-container {
  height: calc(100vh - 60px);
}

/* Sidebar */
.merchant-aside {
  background: #fff;
  border-right: 1px solid #e4e7ed;
  overflow-y: auto;
}

.merchant-aside .el-menu {
  border-right: none;
}

/* Main content area */
.merchant-main {
  background: #f5f7fa;
  overflow-y: auto;
}
</style>
