<template>
  <div class="profile-info">
    <!-- Member card -->
    <el-card class="member-card" :class="`member-${(formData.memberLevel || 'NORMAL').toLowerCase()}`">
      <div class="member-info">
        <div class="member-badge">
          <el-icon :size="40"><UserFilled /></el-icon>
        </div>
        <div class="member-details">
          <div class="member-name">{{ formData.username || 'User' }}</div>
          <div class="member-level">
            <el-icon><Medal /></el-icon>
            <span>{{ getMemberLevelText(formData.memberLevel) }}</span>
          </div>
        </div>
        <div class="member-stats">
          <div class="stat-item">
              <div class="stat-label">Account Balance</div>
            <div class="stat-value">¥{{ formData.balance?.toFixed(2) || '0.00' }}</div>
          </div>
          <div class="stat-item">
              <div class="stat-label">Loyalty Points</div>
            <div class="stat-value">{{ formData.points || 0 }} pts</div>
          </div>
        </div>
      </div>
    </el-card>

    <el-card>
      <template #header>
        <div class="card-header">
          <h2>Personal Information</h2>
          <el-button 
            v-if="!isEditing" 
            type="primary" 
            @click="handleEdit"
          >
            Edit
          </el-button>
          <div v-else>
            <el-button @click="handleCancel">Cancel</el-button>
            <el-button type="primary" @click="handleSave" :loading="saving">Save</el-button>
          </div>
        </div>
      </template>

      <div v-loading="loading" class="profile-content">
        <el-form
          ref="formRef"
          :model="formData"
          :rules="rules"
          label-width="120px"
          :disabled="!isEditing"
        >
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="Username" prop="username">
                <el-input v-model="formData.username" disabled />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="Phone" prop="phone">
                <el-input v-model="formData.phone" placeholder="Enter phone number" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="Email" prop="email">
                <el-input v-model="formData.email" placeholder="Enter email" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="Gender" prop="gender">
                <el-radio-group v-model="formData.gender">
                  <el-radio label="MALE">Male</el-radio>
                  <el-radio label="FEMALE">Female</el-radio>
                  <el-radio label="OTHER">Other</el-radio>
                </el-radio-group>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="Member Level">
                <el-tag :type="getMemberLevelType(formData.memberLevel)">
                  {{ getMemberLevelText(formData.memberLevel) }}
                </el-tag>
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item label="Address" prop="address">
            <el-input 
              v-model="formData.address" 
              type="textarea" 
              :rows="3"
              placeholder="Enter address"
            />
          </el-form-item>

          <el-divider />

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="Account Balance">
                <div class="balance-info">
                  <span class="balance-amount">¥{{ formData.balance?.toFixed(2) || '0.00' }}</span>
                  <el-button size="small" type="primary" link>Top Up</el-button>
                </div>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="Points">
                <div class="points-info">
                  <span class="points-amount">{{ formData.points || 0 }} pts</span>
                </div>
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item label="Registered At">
            <span>{{ formatDate(formData.createTime) }}</span>
          </el-form-item>
        </el-form>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { UserFilled, Medal } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import request from '@/utils/request'
import { formatDate } from '@/utils/format'
import { validatePhone, validateEmail } from '@/utils/validate'

const userStore = useUserStore()
const formRef = ref(null)
const loading = ref(false)
const saving = ref(false)
const isEditing = ref(false)

const formData = reactive({
  username: '',
  phone: '',
  email: '',
  gender: 'OTHER',
  address: '',
  balance: 0,
  points: 0,
  memberLevel: 'NORMAL',
  createTime: null
})

const originalData = ref({})

const rules = {
  phone: [
    { required: true, message: 'Please enter phone number', trigger: 'blur' },
    { validator: validatePhone, trigger: 'blur' }
  ],
  email: [
    { validator: validateEmail, trigger: 'blur' }
  ]
}

onMounted(() => {
  loadUserProfile()
})

const loadUserProfile = async () => {
  loading.value = true
  try {
    const userId = userStore.userId
    const res = await request({
      url: `/api/customer/profile/${userId}`,
      method: 'get'
    })
    
    if (res.data) {
      Object.assign(formData, res.data)
      originalData.value = { ...res.data }
    }
  } catch (error) {
    console.error('Load profile error:', error)
    ElMessage.error(error.message || 'Failed to load user info')
  } finally {
    loading.value = false
  }
}

const handleEdit = () => {
  isEditing.value = true
}

const handleCancel = () => {
  Object.assign(formData, originalData.value)
  isEditing.value = false
}

const handleSave = async () => {
  try {
    await formRef.value.validate()
    
    saving.value = true
    const res = await request({
      url: '/api/customer/profile',
      method: 'put',
      data: {
        userId: userStore.userId,
        phone: formData.phone,
        email: formData.email,
        gender: formData.gender,
        address: formData.address
      }
    })
    
    if (res.code === 200) {
      ElMessage.success('Saved successfully')
      originalData.value = { ...formData }
      isEditing.value = false
      
      // Update user info in the store
      userStore.setUserInfo({
        ...userStore.userInfo,
        phone: formData.phone,
        email: formData.email
      })
    }
  } catch (error) {
    console.error('Save profile error:', error)
    if (error !== 'validation failed') {
      ElMessage.error(error.message || 'Save failed')
    }
  } finally {
    saving.value = false
  }
}

const getMemberLevelText = (level) => {
  const levelMap = {
    NORMAL: 'Normal',
    VIP: 'VIP',
    SVIP: 'Super VIP',
    BRONZE: 'Bronze',
    SILVER: 'Silver',
    GOLD: 'Gold',
    PLATINUM: 'Platinum',
    DIAMOND: 'Diamond'
  }
  return levelMap[level] || 'Normal'
}

const getMemberLevelType = (level) => {
  const typeMap = {
    NORMAL: 'info',
    VIP: 'warning',
    SVIP: 'danger',
    BRONZE: 'info',
    SILVER: '',
    GOLD: 'warning',
    PLATINUM: 'success',
    DIAMOND: 'danger'
  }
  return typeMap[level] || 'info'
}
</script>

<style scoped>
.profile-info {
  padding: 20px;
}

/* Member card */
.member-card {
  margin-bottom: 20px;
  border: none;
}

.member-card.member-bronze {
  background: linear-gradient(135deg, #cd7f32 0%, #8b5a2b 100%);
}

.member-card.member-silver {
  background: linear-gradient(135deg, #c0c0c0 0%, #808080 100%);
}

.member-card.member-gold {
  background: linear-gradient(135deg, #ffd700 0%, #ff8c00 100%);
}

.member-card.member-platinum {
  background: linear-gradient(135deg, #e5e4e2 0%, #98989d 100%);
}

.member-card.member-diamond {
  background: linear-gradient(135deg, #b9f2ff 0%, #4a90e2 100%);
}

.member-card :deep(.el-card__body) {
  padding: 30px;
}

.member-info {
  display: flex;
  align-items: center;
  color: white;
}

.member-badge {
  width: 80px;
  height: 80px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20px;
}

.member-details {
  flex: 1;
}

.member-name {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 8px;
}

.member-level {
  display: flex;
  align-items: center;
  gap: 5px;
  font-size: 16px;
  opacity: 0.9;
}

.member-stats {
  display: flex;
  gap: 40px;
}

.stat-item {
  text-align: center;
}

.stat-label {
  font-size: 14px;
  opacity: 0.8;
  margin-bottom: 5px;
}

.stat-value {
  font-size: 20px;
  font-weight: bold;
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

.profile-content {
  min-height: 400px;
}

.balance-info,
.points-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.balance-amount {
  font-size: 24px;
  font-weight: bold;
  color: #f56c6c;
}

.points-amount {
  font-size: 18px;
  font-weight: bold;
  color: #409eff;
}
</style>
