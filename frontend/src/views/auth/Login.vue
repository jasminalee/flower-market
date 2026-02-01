<template>
  <div class="auth-page">
    <div class="auth-container">
      <div class="auth-card">
        <div class="auth-header">
          <h2>{{ isRegister ? 'Sign up' : 'Log in' }}</h2>
          <el-segmented v-model="userType" :options="userTypes" size="large" />
        </div>
        
        <el-form
          ref="formRef"
          :model="formData"
          :rules="rules"
          label-width="80px"
          class="auth-form"
        >
          <template v-if="isRegister">
            <el-form-item label="Username" prop="username">
              <el-input v-model="formData.username" placeholder="Enter username (4-20 chars)" />
            </el-form-item>
            
            <el-form-item label="Name" prop="realName">
              <el-input v-model="formData.realName" placeholder="Enter your full name" />
            </el-form-item>
            
            <el-form-item label="Phone" prop="phone">
              <el-input v-model="formData.phone" placeholder="Enter phone number" />
            </el-form-item>
            
            <el-form-item label="Email" prop="email">
              <el-input v-model="formData.email" placeholder="Enter email" />
            </el-form-item>
            
            <el-form-item label="Password" prop="password">
              <el-input
                v-model="formData.password"
                type="password"
                placeholder="Enter password (4-20 chars)"
                show-password
              />
            </el-form-item>
            
            <el-form-item label="Confirm" prop="confirmPassword">
              <el-input
                v-model="formData.confirmPassword"
                type="password"
                placeholder="Re-enter password"
                show-password
              />
            </el-form-item>
            
            <template v-if="userType === 'MERCHANT'">
              <el-form-item label="Merchant Name" prop="merchantName">
                <el-input v-model="formData.merchantName" placeholder="Enter merchant name" />
              </el-form-item>
              
              <el-form-item label="Contact Address" prop="contactAddr">
                <el-input v-model="formData.contactAddr" placeholder="Enter contact address" />
              </el-form-item>
            </template>
          </template>
          
          <template v-else>
            <el-form-item label="Email" prop="email">
              <el-input v-model="formData.email" placeholder="Enter email" />
            </el-form-item>
            
            <el-form-item label="Password" prop="password">
              <el-input
                v-model="formData.password"
                type="password"
                placeholder="Enter password (4-20 chars)"
                show-password
                @keyup.enter="handleSubmit"
              />
            </el-form-item>
          </template>
          
          <el-form-item>
            <el-button
              type="primary"
              :loading="loading"
              @click="handleSubmit"
              style="width: 100%"
            >
              {{ isRegister ? 'Sign up' : 'Log in' }}
            </el-button>
          </el-form-item>
        </el-form>
        
        <div class="auth-footer">
          <template v-if="isRegister">
            Already have an account?
            <router-link to="/login">Log in</router-link>
          </template>
          <template v-else>
            New here?
            <router-link to="/register">Sign up</router-link>
          </template>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { registerCustomer, registerMerchant } from '@/api/auth'
import { ElMessage } from 'element-plus'
import { validateUsername, validatePassword, validatePhone, validateEmail, validateChineseName } from '@/utils/validate'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()

const isRegister = computed(() => route.name === 'Register')
const userType = ref('CUSTOMER')
const userTypes = [
  { label: 'Customer', value: 'CUSTOMER' },
  { label: 'Merchant', value: 'MERCHANT' },
  { label: 'Admin', value: 'ADMIN' }
]

const formRef = ref(null)
const loading = ref(false)

const formData = reactive({
  email: '',
  username: '',
  password: '',
  confirmPassword: '',
  realName: '',
  phone: '',
  merchantName: '',
  contactAddr: ''
})

const validateConfirmPassword = (rule, value, callback) => {
  if (value !== formData.password) {
    callback(new Error('Passwords do not match'))
  } else {
    callback()
  }
}

const rules = reactive({
  email: [
    { required: true, message: 'Please enter your email', trigger: 'blur' },
    { validator: validateEmail, trigger: 'blur' }
  ],
  username: [
    { required: true, message: 'Please enter a username', trigger: 'blur' },
    { validator: validateUsername, trigger: 'blur' }
  ],
  password: [
    { required: true, message: 'Please enter your password', trigger: 'blur' },
    { validator: validatePassword, trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: 'Please re-enter your password', trigger: 'blur' },
    { validator: validateConfirmPassword, trigger: 'blur' }
  ],
  realName: [
    { required: true, message: 'Please enter your full name', trigger: 'blur' },
    { validator: validateChineseName, trigger: 'blur' }
  ],
  phone: [
    { required: true, message: 'Please enter your phone number', trigger: 'blur' },
    { validator: validatePhone, trigger: 'blur' }
  ],
  email: [
    { required: true, message: 'Please enter your email', trigger: 'blur' },
    { validator: validateEmail, trigger: 'blur' }
  ],
  merchantName: [
    { required: true, message: 'Please enter the merchant name', trigger: 'blur' }
  ],
  contactAddr: [
    { required: true, message: 'Please enter the contact address', trigger: 'blur' }
  ]
})

const handleSubmit = async () => {
  await formRef.value.validate(async (valid) => {
    if (!valid) return
    
    loading.value = true
    try {
      if (isRegister.value) {
        // Sign up
        if (userType.value === 'CUSTOMER') {
          await registerCustomer(formData)
          ElMessage.success('Signed up successfully. Please log in.')
          router.push('/login')
        } else if (userType.value === 'MERCHANT') {
          await registerMerchant(formData)
          ElMessage.success('Signed up successfully. Please wait for approval.')
          router.push('/login')
        }
      } else {
        // Log in
        if (userType.value === 'CUSTOMER') {
          await userStore.customerLogin({
            email: formData.email,
            password: formData.password
          })
          ElMessage.success('Logged in successfully')
          const redirect = route.query.redirect || '/home'
          router.push(redirect)
        } else if (userType.value === 'MERCHANT') {
          await userStore.merchantLogin({
            email: formData.email,
            password: formData.password
          })
          ElMessage.success('Merchant login successful')
          router.push('/merchant/dashboard')
        } else if (userType.value === 'ADMIN') {
          await userStore.adminLogin({
            email: formData.email,
            password: formData.password
          })
          ElMessage.success('Admin login successful')
          router.push('/admin/dashboard')
        }
      }
    } catch (error) {
      console.error('Auth error:', error)
    } finally {
      loading.value = false
    }
  })
}
</script>

<style scoped>
.auth-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: var(--spacing-lg);
}

.auth-container {
  width: 100%;
  max-width: 500px;
}

.auth-card {
  background: var(--color-bg-white);
  border-radius: var(--radius-xl);
  padding: var(--spacing-xl);
  box-shadow: var(--shadow-dark);
}

.auth-header {
  text-align: center;
  margin-bottom: var(--spacing-xl);
}

.auth-header h2 {
  font-size: 28px;
  margin-bottom: var(--spacing-lg);
  color: var(--color-text-primary);
}

.auth-form {
  margin-top: var(--spacing-lg);
}

.auth-footer {
  text-align: center;
  margin-top: var(--spacing-lg);
  color: var(--color-text-secondary);
}

.auth-footer a {
  color: var(--color-primary);
  margin-left: var(--spacing-xs);
}
</style>
