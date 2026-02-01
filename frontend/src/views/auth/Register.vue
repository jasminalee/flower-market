<template>
  <div class="auth-page">
    <div class="auth-container">
      <div class="auth-card">
        <div class="auth-header">
          <h2>User Registration</h2>
        </div>

        <el-form
            ref="formRef"
            :model="formData"
            :rules="rules"
            label-width="80px"
            class="auth-form"
        >


          <el-form-item label="Name" prop="name">
            <el-input v-model="formData.name" placeholder="Please enter your full name"/>
          </el-form-item>

          <el-form-item label="Phone" prop="phone">
            <el-input v-model="formData.phone" placeholder="Please enter your phone number"/>
          </el-form-item>

          <el-form-item label="Email" prop="email">
            <el-input v-model="formData.email" placeholder="Please enter your email"/>
          </el-form-item>

          <el-form-item label="Password" prop="password">
            <el-input
                v-model="formData.password"
                type="password"
                placeholder="Please enter a password (4-20 characters)"
                show-password
            />
          </el-form-item>

          <el-form-item label="Confirm Password" prop="confirmPassword">
            <el-input
                v-model="formData.confirmPassword"
                type="password"
                placeholder="Please re-enter your password"
                show-password
            />
          </el-form-item>

          <el-form-item>
            <el-button
                type="primary"
                :loading="loading"
                @click="handleSubmit"
                style="width: 100%"
            >
              Sign Up
            </el-button>
          </el-form-item>
        </el-form>

        <div class="auth-footer">
          Already have an account?
          <router-link to="/login">Log In</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import {ref, reactive} from 'vue'
import {useRouter} from 'vue-router'
import {registerCustomer} from '@/api/auth'
import {ElMessage} from 'element-plus'
import {validatePassword, validatePhone, validateEmail, validateChineseName} from '@/utils/validate'

const router = useRouter()

const formRef = ref(null)
const loading = ref(false)

const formData = reactive({
  name: '',
  phone: '',
  email: '',
  password: '',
  confirmPassword: ''
})

const validateConfirmPassword = (rule, value, callback) => {
  if (value !== formData.password) {
    callback(new Error('Passwords do not match'))
  } else {
    callback()
  }
}

const rules = reactive({
  name: [
    {required: true, message: 'Please enter your full name', trigger: 'blur'},
    {validator: validateChineseName, trigger: 'blur'}
  ],
  phone: [
    {required: true, message: 'Please enter your phone number', trigger: 'blur'},
    {validator: validatePhone, trigger: 'blur'}
  ],
  email: [
    {required: true, message: 'Please enter your email', trigger: 'blur'},
    {validator: validateEmail, trigger: 'blur'}
  ],
  password: [
    {required: true, message: 'Please enter your password', trigger: 'blur'},
    {validator: validatePassword, trigger: 'blur'}
  ],
  confirmPassword: [
    {required: true, message: 'Please re-enter your password', trigger: 'blur'},
    {validator: validateConfirmPassword, trigger: 'blur'}
  ]
})

const handleSubmit = async () => {
  if (!formRef.value) return

  await formRef.value.validate(async (valid) => {
    if (!valid) return

    loading.value = true
    try {
      await registerCustomer({
        name: formData.name,
        phone: formData.phone,
        email: formData.email,
        password: formData.password
      })

      ElMessage.success('Registration successful, please log in')
      router.push('/login')
    } catch (error) {
      ElMessage.error(error.message || 'Registration failed')
    } finally {
      loading.value = false
    }
  })
}
</script>

<style scoped>
.auth-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.auth-container {
  width: 100%;
  max-width: 500px;
  padding: var(--spacing-lg);
}

.auth-card {
  background: var(--color-bg-white);
  border-radius: var(--radius-lg);
  padding: var(--spacing-xl);
  box-shadow: var(--shadow-lg);
}

.auth-header {
  text-align: center;
  margin-bottom: var(--spacing-xl);
}

.auth-header h2 {
  font-size: 28px;
  font-weight: 600;
  color: var(--color-text-primary);
  margin-bottom: var(--spacing-md);
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
  text-decoration: none;
  font-weight: 500;
}

.auth-footer a:hover {
  text-decoration: underline;
}

.customer-home {
  min-height: 100vh;
  background: var(--color-bg-base);
}

.carousel-item {
  height: 100%;
  background-size: cover;
  background-position: center;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 32px;
  font-weight: bold;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
}

.section-title {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: var(--spacing-lg);
  padding-bottom: var(--spacing-sm);
  border-bottom: 2px solid var(--color-primary);
}

.category-card {
  background: white;
  padding: var(--spacing-lg);
  border-radius: var(--radius-lg);
  text-align: center;
  cursor: pointer;
  transition: all 0.3s;
}

.category-card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-light);
}

.category-card p {
  margin-top: var(--spacing-md);
  font-weight: 500;
}

.product-card {
  cursor: pointer;
  transition: all 0.3s;
}

.product-card:hover {
  transform: translateY(-4px);
}

.product-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.product-info {
  padding: var(--spacing-md);
}

.product-name {
  font-size: var(--font-size-md);
  margin-bottom: var(--spacing-sm);
}

.product-price {
  margin-bottom: var(--spacing-md);
}

.price {
  font-size: var(--font-size-xl);
  color: var(--color-danger);
  font-weight: bold;
}

.original-price {
  margin-left: var(--spacing-sm);
  text-decoration: line-through;
  color: var(--color-text-secondary);
  font-size: var(--font-size-sm);
}

.product-actions {
  display: flex;
  gap: var(--spacing-sm);
}
</style>
