<template>
  <div class="merchant-profile">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>Merchant Profile</span>
          <el-button v-if="!isEditing" type="primary" @click="handleEdit">Edit</el-button>
          <div v-else>
            <el-button @click="handleCancel">Cancel</el-button>
            <el-button type="primary" @click="handleSave" :loading="saving">Save</el-button>
          </div>
        </div>
      </template>

      <el-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-width="120px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="Store Name" prop="name">
              <el-input v-model="formData.name" :disabled="!isEditing" placeholder="Please enter the store name" />
            </el-form-item>

            <el-form-item label="Contact" prop="contactName">
              <el-input v-model="formData.contactName" :disabled="!isEditing" placeholder="Please enter the contact name" />
            </el-form-item>

            <el-form-item label="Phone" prop="phone">
              <el-input v-model="formData.phone" :disabled="!isEditing" placeholder="Please enter the phone number" />
            </el-form-item>

            <el-form-item label="Email" prop="email">
              <el-input v-model="formData.email" :disabled="!isEditing" placeholder="Please enter the email address" />
            </el-form-item>

            <el-form-item label="Business Hours" prop="businessHours">
              <el-time-picker
                v-model="timeRange"
                is-range
                range-separator="To"
                start-placeholder="Start time"
                end-placeholder="End time"
                format="HH:mm"
                value-format="HH:mm"
                :disabled="!isEditing"
                @change="handleTimeChange"
              />
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="Store Logo">
              <el-upload
                class="logo-uploader"
                :show-file-list="false"
                action="#"
                :auto-upload="false"
                :on-change="handleLogoChange"
              >
                <img v-if="formData.logo" :src="formData.logo" class="logo-image" />
                <el-icon v-else class="logo-uploader-icon"><Plus /></el-icon>
              </el-upload>
              <div v-if="isEditing" class="upload-tip">Click to upload (Recommended size: 200x200px)</div>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="Address" prop="address">
          <el-input v-model="formData.address" :disabled="!isEditing" placeholder="Please enter the address" />
        </el-form-item>

        <el-form-item label="Description" prop="description">
          <el-input
            v-model="formData.description"
            :disabled="!isEditing"
            type="textarea"
            :rows="6"
            placeholder="Please enter a short description"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="Status">
          <el-tag :type="formData.status === 'ACTIVE' ? 'success' : 'danger'">
            {{ formData.statusText }}
          </el-tag>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getMerchantProfile, updateMerchantProfile } from '@/api/merchant'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const formRef = ref(null)
const isEditing = ref(false)
const saving = ref(false)
const timeRange = ref([])

const formData = reactive({
  merchId: null,
  name: '',
  contactName: '',
  phone: '',
  email: '',
  address: '',
  businessHours: '',
  logo: '',
  description: '',
  status: '',
  statusText: ''
})

const originalData = ref({})

const rules = {
  name: [{ required: true, message: 'Please enter the store name', trigger: 'blur' }],
  contactName: [{ required: true, message: 'Please enter the contact name', trigger: 'blur' }],
  phone: [
    { required: true, message: 'Please enter the phone number', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: 'Please enter a valid phone number', trigger: 'blur' }
  ],
  email: [
    { required: true, message: 'Please enter the email address', trigger: 'blur' },
    { type: 'email', message: 'Please enter a valid email address', trigger: 'blur' }
  ],
  address: [{ required: true, message: 'Please enter the address', trigger: 'blur' }]
}

const handleLogoChange = (file) => {
  if (!isEditing.value) {
    ElMessage.warning('Please click Edit button before uploading')
    return
  }
  const reader = new FileReader()
  reader.onload = (e) => {
    formData.logo = e.target.result
  }
  reader.readAsDataURL(file.raw)
}

const handleTimeChange = (val) => {
  if (val && val.length === 2) {
    formData.businessHours = `${val[0]}-${val[1]}`
  } else {
    formData.businessHours = ''
  }
}

const handleEdit = () => {
  isEditing.value = true
  // Create a deep copy to avoid reference issues
  originalData.value = JSON.parse(JSON.stringify(formData))
  console.log('Editing started, isEditing:', isEditing.value)
}

const handleCancel = () => {
  isEditing.value = false
  if (originalData.value) {
    Object.assign(formData, originalData.value)
    if (formData.businessHours && formData.businessHours.includes('-')) {
      timeRange.value = formData.businessHours.split('-')
    } else {
      timeRange.value = []
    }
  }
}

const handleSave = async () => {
  console.log('Attempting to save, isEditing:', isEditing.value)
  try {
    const valid = await formRef.value.validate()
    if (!valid) return
    
    saving.value = true
    const updateData = {
      merchId: formData.merchId,
      name: formData.name,
      contactName: formData.contactName,
      phone: formData.phone,
      email: formData.email,
      address: formData.address,
      businessHours: formData.businessHours,
      logo: formData.logo,
      description: formData.description
    }
    
    console.log('Sending update request:', updateData)
    await updateMerchantProfile(updateData)
    
    ElMessage.success('Saved successfully')
    isEditing.value = false
    await fetchProfile()
  } catch (error) {
    console.error('Save error:', error)
    if (error !== false) { // Not a validation error
      ElMessage.error(error.message || 'Save failed')
    }
  } finally {
    saving.value = false
  }
}

const fetchProfile = async () => {
  try {
    const merchantId = userStore.userId || userStore.user?.merchantId || 1
    console.log('Fetching profile for merchantId:', merchantId)
    const res = await getMerchantProfile(merchantId)
    if (res.data) {
      Object.assign(formData, res.data)
      formData.statusText = res.data.status === 'ACTIVE' ? 'Open' : 'Frozen'
      
      if (res.data.businessHours && res.data.businessHours.includes('-')) {
        timeRange.value = res.data.businessHours.split('-')
      } else {
        timeRange.value = []
      }
      
      console.log('Profile updated in state:', formData)
    }
  } catch (error) {
    console.error('Fetch profile error:', error)
    ElMessage.error('Failed to load data')
  }
}

onMounted(() => {
  fetchProfile()
})
</script>

<style scoped>
.merchant-profile {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo-uploader {
  display: inline-block;
}

.logo-uploader :deep(.el-upload) {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
  width: 200px;
  height: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.logo-uploader :deep(.el-upload:hover) {
  border-color: var(--el-color-primary);
}

.logo-image {
  width: 200px;
  height: 200px;
  object-fit: cover;
}

.logo-uploader-icon {
  font-size: 28px;
  color: #8c939d;
}

.upload-tip {
  margin-top: 10px;
  font-size: 12px;
  color: #909399;
}
</style>

