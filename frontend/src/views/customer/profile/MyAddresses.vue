<template>
  <div class="my-addresses">
    <el-card>
      <template #header>
        <div class="card-header">
          <h2>Shipping Address</h2>
          <el-button type="primary" @click="isEditing = true" v-if="!isEditing">
            <el-icon><Edit /></el-icon>
            Edit Address
          </el-button>
        </div>
      </template>

      <div v-loading="loading" class="address-content">
        <el-empty v-if="!loading && !userInfo.address && !isEditing" description="No shipping address yet — click Edit to add one" />
        
        <!-- Display mode -->
        <div v-if="!isEditing && userInfo.address" class="address-display">
          <el-descriptions :column="1" border>
            <el-descriptions-item label="Address">
              <el-icon><Location /></el-icon>
              {{ userInfo.address }}
            </el-descriptions-item>
          </el-descriptions>
        </div>

        <!-- Edit mode -->
        <el-form 
          v-if="isEditing"
          ref="formRef" 
          :model="form" 
          :rules="rules" 
          label-width="100px"
        >
          <el-form-item label="Address" prop="address">
            <el-input 
              v-model="form.address" 
              type="textarea"
              :rows="3"
              placeholder="Enter full shipping address"
              maxlength="255"
              show-word-limit
            />
          </el-form-item>
          
          <el-form-item>
            <el-button type="primary" @click="handleSave">Save</el-button>
            <el-button @click="handleCancel">Cancel</el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Edit, Location } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const formRef = ref(null)
const loading = ref(false)
const isEditing = ref(false)

const userInfo = computed(() => userStore.userInfo || {})

const form = reactive({
  address: ''
})

const rules = {
  address: [
    { required: true, message: 'Please enter shipping address', trigger: 'blur' },
    { min: 5, max: 255, message: 'Length must be between 5 and 255 characters', trigger: 'blur' }
  ]
}

onMounted(() => {
  loadUserInfo()
})

const loadUserInfo = () => {
  if (userInfo.value.address) {
    form.address = userInfo.value.address
  }
}

const handleSave = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        // Call update user info API
        const updateData = {
          userId: userStore.userId,
          address: form.address
        }
        
        await userStore.updateUserInfo(updateData)
        
        ElMessage.success('Saved successfully')
        isEditing.value = false
      } catch (error) {
        console.error('Save address error:', error)
        ElMessage.error(error.message || 'Save failed')
      } finally {
        loading.value = false
      }
    }
  })
}

const handleCancel = () => {
  form.address = userInfo.value.address || ''
  isEditing.value = false
}
</script>

<style scoped>
.my-addresses {
  padding: 20px;
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

.address-content {
  min-height: 300px;
}

.address-display {
  padding: 20px;
}

.address-display .el-icon {
  margin-right: 8px;
}
</style>
