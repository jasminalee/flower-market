<template>
  <div class="admin-profile">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>Profile</span>
        </div>
      </template>

      <el-form :model="form" :rules="rules" ref="formRef" label-width="120px">
        <el-form-item label="Admin ID">
          <el-input v-model="form.adminId" disabled></el-input>
        </el-form-item>

        <el-form-item label="Name" prop="name">
          <el-input v-model="form.name" placeholder="Enter name"></el-input>
        </el-form-item>

        <el-form-item label="Email" prop="email">
          <el-input v-model="form.email" disabled></el-input>
        </el-form-item>

        <el-form-item label="Permission">
          <el-tag :type="form.permission === 'SUPER_ADMIN' ? 'danger' : 'primary'">
            {{ permissionText }}
          </el-tag>
        </el-form-item>

        <el-form-item label="Status">
          <el-tag :type="form.status === 'ACTIVE' ? 'success' : 'info'">
            {{ statusText }}
          </el-tag>
        </el-form-item>

        <el-form-item label="Created At">
          <el-input v-model="form.createDate" disabled></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSubmit" :loading="loading">Save</el-button>
          <el-button @click="handleChangePassword">Change Password</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- Change password dialog -->
    <el-dialog title="Change Password" v-model="passwordDialogVisible" width="500px">
      <el-form :model="passwordForm" :rules="passwordRules" ref="passwordFormRef" label-width="120px">
        <el-form-item label="Current Password" prop="oldPassword">
          <el-input v-model="passwordForm.oldPassword" type="password" placeholder="Enter current password"></el-input>
        </el-form-item>

        <el-form-item label="New Password" prop="newPassword">
          <el-input v-model="passwordForm.newPassword" type="password" placeholder="Enter new password"></el-input>
        </el-form-item>

        <el-form-item label="Confirm Password" prop="confirmPassword">
          <el-input v-model="passwordForm.confirmPassword" type="password" placeholder="Re-enter new password"></el-input>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="passwordDialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="handlePasswordSubmit" :loading="passwordLoading">OK</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { storage } from '@/utils/storage'
import request from '@/utils/request'

const formRef = ref()
const passwordFormRef = ref()
const loading = ref(false)
const passwordLoading = ref(false)
const passwordDialogVisible = ref(false)

const form = reactive({
  adminId: null,
  name: '',
  email: '',
  permission: '',
  status: '',
  createDate: ''
})

const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const rules = {
  name: [
    { required: true, message: 'Please enter your name', trigger: 'blur' }
  ]
}

const validateConfirmPassword = (rule, value, callback) => {
  if (value === '') {
    callback(new Error('Please re-enter the password'))
  } else if (value !== passwordForm.newPassword) {
    callback(new Error('Passwords do not match'))
  } else {
    callback()
  }
}

const passwordRules = {
  oldPassword: [
    { required: true, message: 'Please enter current password', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: 'Please enter new password', trigger: 'blur' },
    { min: 6, message: 'Password must be at least 6 characters', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, validator: validateConfirmPassword, trigger: 'blur' }
  ]
}

const permissionText = computed(() => {
  const map = {
    'SUPER_ADMIN': 'Super Admin',
    'ADMIN': 'Admin'
  }
  return map[form.permission] || form.permission
})

const statusText = computed(() => {
  const map = {
    'ACTIVE': 'Active',
    'INACTIVE': 'Inactive'
  }
  return map[form.status] || form.status
})

const fetchProfile = async () => {
  try {
    const userInfo = storage.get('userInfo')
    if (!userInfo || userInfo.userType !== 'ADMIN') {
      ElMessage.error('Please log in first')
      return
    }
    
    const response = await request.get('/api/admin/profile', {
      params: { adminId: userInfo.id }
    })
    if (response.code === 200) {
      Object.assign(form, response.data)
    } else {
      ElMessage.error(response.msg || 'Failed to load profile')
    }
  } catch (error) {
    console.error('Failed to load profile:', error)
    // If the backend API is not available, fall back to local storage
    const userInfo = storage.get('userInfo')
    if (userInfo && userInfo.userType === 'ADMIN') {
      form.adminId = userInfo.id
      form.name = userInfo.name
      form.email = userInfo.email
      form.permission = userInfo.permission || 'ADMIN'
      form.status = userInfo.status || 'ACTIVE'
      form.createDate = userInfo.createDate || ''
    }
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const response = await request.put('/api/admin/profile', {
          adminId: form.adminId,
          name: form.name
        })
        if (response.code === 200) {
          ElMessage.success('Saved successfully')
          // Update local storage
          const userInfo = storage.get('userInfo')
          if (userInfo) {
            userInfo.name = form.name
            storage.set('userInfo', userInfo)
          }
        } else {
          ElMessage.error(response.msg || 'Save failed')
        }
      } catch (error) {
        console.error('Save failed:', error)
        ElMessage.error('Save failed')
      } finally {
        loading.value = false
      }
    }
  })
}

const handleChangePassword = () => {
  passwordDialogVisible.value = true
  passwordForm.oldPassword = ''
  passwordForm.newPassword = ''
  passwordForm.confirmPassword = ''
  if (passwordFormRef.value) {
    passwordFormRef.value.clearValidate()
  }
}

const handlePasswordSubmit = async () => {
  if (!passwordFormRef.value) return

  await passwordFormRef.value.validate(async (valid) => {
    if (valid) {
      passwordLoading.value = true
      try {
        const response = await request.put('/api/admin/password', {
          adminId: form.adminId,
          oldPassword: passwordForm.oldPassword,
          newPassword: passwordForm.newPassword
        })
        if (response.code === 200) {
          ElMessage.success('Password changed. Please log in again.')
          passwordDialogVisible.value = false
          // Clear login info and go to login page
          storage.remove('userInfo')
          storage.remove('token')
          setTimeout(() => {
            window.location.href = '/login'
          }, 1000)
        } else {
          ElMessage.error(response.msg || 'Failed to change password')
        }
      } catch (error) {
        console.error('Failed to change password:', error)
        ElMessage.error('Failed to change password')
      } finally {
        passwordLoading.value = false
      }
    }
  })
}

onMounted(() => {
  fetchProfile()
})
</script>

<style scoped>
.admin-profile {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 18px;
  font-weight: bold;
}

.el-form {
  max-width: 600px;
}
</style>
