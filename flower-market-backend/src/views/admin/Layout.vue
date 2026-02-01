<template>
  <el-container class="admin-layout">
    <!-- Top Navigation Bar -->
    <el-header class="admin-header">
      <div class="header-left">
        <span class="logo">🌸</span>
        <span class="title">Flower Market Admin</span>
      </div>
      <div class="header-right">
        <el-button type="primary" link @click="goToHome" class="home-btn">
          <el-icon><HomeFilled /></el-icon>
          Back to Home
        </el-button>
        <el-dropdown @command="handleCommand" class="user-dropdown">
          <div class="user-info">
            <el-avatar :size="32" class="avatar">
              {{ adminName.charAt(0) }}
            </el-avatar>
            <span class="username">{{ adminName }}</span>
            <el-icon class="el-icon--right"><ArrowDown /></el-icon>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item disabled>
                <div class="user-detail">
                  <div>{{ adminName }}</div>
                  <div class="user-email">{{ adminEmail }}</div>
                </div>
              </el-dropdown-item>
              <el-dropdown-item command="profile">
                <el-icon><User /></el-icon>
                Profile
              </el-dropdown-item>
              <el-dropdown-item divided command="logout">
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
      <el-aside width="200px" class="admin-aside">
        <el-menu :default-active="$route.path" router>
          <el-menu-item index="/admin/dashboard">
            <el-icon><DataBoard /></el-icon>
            <span>Admin Dashboard</span>
          </el-menu-item>
          <el-menu-item index="/admin/customers">
            <el-icon><User /></el-icon>
            <span>Customers</span>
          </el-menu-item>
          <el-menu-item index="/admin/merchants">
            <el-icon><Shop /></el-icon>
            <span>Merchants</span>
          </el-menu-item>
          <el-menu-item index="/admin/reviews">
            <el-icon><ChatDotRound /></el-icon>
            <span>Reviews</span>
          </el-menu-item>
          <el-menu-item index="/admin/orders">
            <el-icon><Document /></el-icon>
            <span>Orders</span>
          </el-menu-item>
          <el-menu-item index="/admin/categories">
            <el-icon><Menu /></el-icon>
            <span>Categories</span>
          </el-menu-item>
          <el-menu-item index="/admin/knowledge">
            <el-icon><Reading /></el-icon>
            <span>Plant Care</span>
          </el-menu-item>
          <el-menu-item index="/admin/config">
            <el-icon><Setting /></el-icon>
            <span>System Settings</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <!-- Main Content Area -->
      <el-main class="admin-main">
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
  DataBoard,
  User,
  Shop,
  ChatDotRound,
  Document,
  Reading,
  Setting,
  Menu
} from '@element-plus/icons-vue'

const router = useRouter()
const userStore = useUserStore()

const adminName = computed(() => userStore.username || 'Administrator')
const adminEmail = computed(() => userStore.email || '')

// Go to home page
const goToHome = () => {
  router.push('/')
}

// Dropdown menu command handler
const handleCommand = async (command) => {
  if (command === 'profile') {
    router.push('/admin/profile')
  } else if (command === 'logout') {
    try {
      await ElMessageBox.confirm('Are you sure you want to log out?', 'Confirmation', {
        confirmButtonText: 'Confirm',
        cancelButtonText: 'Cancel',
        type: 'warning'
      })
      
      userStore.logout()
      ElMessage.success('Logout successful')
      router.push('/home')
    } catch {
      // User cancelled
    }
  }
}
</script>

<style scoped>
.admin-layout {
  min-height: 100vh;
  background: #f5f7fa;
}

/* Top Navigation Bar */
.admin-header {
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
  max-width: 100px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
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
.admin-aside {
  background: #fff;
  border-right: 1px solid #e4e7ed;
  overflow-y: auto;
}

.admin-aside .el-menu {
  border-right: none;
}

.admin-aside .el-menu-item {
  height: 50px;
  line-height: 50px;
}

/* Main Content Area */
.admin-main {
  overflow-y: auto;
}
</style>
