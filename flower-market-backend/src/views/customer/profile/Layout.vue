<template>
  <el-container class="profile-layout">
    <!-- Top navigation bar -->
    <el-header class="profile-header">
      <div class="header-left">
        <span class="logo">🌸</span>
        <span class="title">Profile Center</span>
      </div>
      <div class="header-right">
        <el-button type="primary" link @click="goToHome" class="home-btn">
          <el-icon><HomeFilled /></el-icon>
          Back to Home
        </el-button>
        <el-dropdown @command="handleCommand" class="user-dropdown">
          <div class="user-info">
            <el-avatar :size="32" class="avatar">
              {{ userName.charAt(0) }}
            </el-avatar>
            <span class="username">{{ userName }}</span>
            <el-icon class="el-icon--right"><ArrowDown /></el-icon>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item disabled>
                <div class="user-detail">
                  <div>{{ userName }}</div>
                  <div class="user-email">{{ userEmail }}</div>
                </div>
              </el-dropdown-item>
              <el-dropdown-item command="logout">
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
      <el-aside width="200px" class="profile-aside">
        <el-menu :default-active="$route.path" router>
          <el-menu-item index="/profile/info">
            <el-icon><User /></el-icon>
            <span>Personal Info</span>
          </el-menu-item>
          <el-menu-item index="/profile/orders">
            <el-icon><Document /></el-icon>
            <span>My Orders</span>
          </el-menu-item>
          <el-menu-item index="/profile/favorites">
            <el-icon><Star /></el-icon>
            <span>My Favorites</span>
          </el-menu-item>
          <el-menu-item index="/profile/addresses">
            <el-icon><Location /></el-icon>
            <span>Shipping Address</span>
          </el-menu-item>
          <el-menu-item index="/profile/coupons">
            <el-icon><Ticket /></el-icon>
            <span>My Coupons</span>
          </el-menu-item>
          <el-menu-item index="/profile/balance">
            <el-icon><Wallet /></el-icon>
            <span>Balance</span>
          </el-menu-item>
          <el-menu-item index="/profile/checkin">
            <el-icon><Calendar /></el-icon>
            <span>Check-in</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <!-- Main Content Area -->
      <el-main class="profile-main">
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
  Document,
  Star,
  Location,
  Ticket,
  Wallet,
  Calendar
} from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()

const userName = computed(() => userStore.username || 'User')
const userEmail = computed(() => userStore.email || '')

const goToHome = () => {
  router.push('/')
}

const handleCommand = async (command) => {
  if (command === 'logout') {
    try {
      await ElMessageBox.confirm('Are you sure you want to log out?', 'Confirm', {
        confirmButtonText: 'OK',
        cancelButtonText: 'Cancel',
        type: 'warning'
      })
      
      userStore.logout()
      ElMessage.success('Logged out successfully')
      router.push('/home')
    } catch {
      // User cancelled
    }
  }
}
</script>

<style scoped>
.profile-layout {
  min-height: 100vh;
  background: #f5f7fa;
}

/* Top Navigation Bar */
.profile-header {
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
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
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

/* Main Container */
.main-container {
  height: calc(100vh - 60px);
}

/* Sidebar */
.profile-aside {
  background: #fff;
  border-right: 1px solid #e4e7ed;
  overflow-y: auto;
}

.profile-aside .el-menu {
  border-right: none;
}

/* Main Content Area */
.profile-main {
  background: #f5f7fa;
  overflow-y: auto;
}
</style>
