<template>
  <div class="config-list">
    <div class="page-header">
      <h2 class="page-title">System Configuration</h2>
      <div class="header-actions">
        <el-select
          v-model="activeCategory"
          style="width: 200px"
          @change="handleCategoryChange"
          placeholder="Filter Category"
        >
          <el-option label="Basic Settings" value="basic" />
          <el-option label="Contact Info" value="contact" />
          <el-option label="Points Reward" value="reward" />
          <el-option label="Shipping" value="shipping" />
          <el-option label="Order Settings" value="order" />
          <el-option label="Review Settings" value="review" />
          <el-option label="Shopping Cart" value="cart" />
          <el-option label="Upload Settings" value="upload" />
        </el-select>
      </div>
    </div>

    <!-- Configuration table -->
    <el-card shadow="never" class="table-card">
      <el-table :data="configList" v-loading="loading" style="width: 100%">
        <el-table-column prop="configKey" label="Config Key" width="250" />
        <el-table-column prop="configValue" label="Value" min-width="200">
          <template #default="{ row }">
            <el-tag v-if="!row.configValue" type="info">Not Set</el-tag>
            <span v-else class="config-value">{{ row.configValue }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="Description" min-width="250" show-overflow-tooltip />
        <el-table-column label="Action" width="100" fixed="right" align="center">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">Edit</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- Edit configuration dialog -->
    <el-dialog v-model="dialogVisible" title="Edit Configuration" width="500px" destroy-on-close>
      <el-form :model="formData" :rules="rules" ref="formRef" label-position="top">
        <el-form-item label="Config Key">
          <el-input v-model="formData.configKey" disabled />
        </el-form-item>
        <el-form-item label="Config Value" prop="configValue">
          <el-input v-model="formData.configValue" placeholder="Please enter config value" clearable />
        </el-form-item>
        <el-form-item label="Description">
          <el-input v-model="formData.description" type="textarea" :rows="3" disabled />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="handleSave" :loading="submitting">Save Changes</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getConfigByCategory, updateConfig } from '@/api/config'

const loading = ref(false)
const submitting = ref(false)
const dialogVisible = ref(false)
const activeCategory = ref('basic')
const configList = ref([])
const formRef = ref(null)

const formData = reactive({
  id: null,
  configKey: '',
  configValue: '',
  description: ''
})

const rules = {
  configValue: [{ required: true, message: 'Please enter config value', trigger: 'blur' }]
}

// Fetch config list
const fetchConfigList = async () => {
  loading.value = true
  try {
    const { data } = await getConfigByCategory(activeCategory.value)
    configList.value = data
  } catch (error) {
    ElMessage.error('Failed to load config list')
  } finally {
    loading.value = false
  }
}

// Category change
const handleCategoryChange = () => {
  fetchConfigList()
}

// Edit configuration
const handleEdit = (row) => {
  formData.id = row.id
  formData.configKey = row.configKey
  formData.configValue = row.configValue
  formData.description = row.description
  dialogVisible.value = true
}

// Save configuration
const handleSave = async () => {
  try {
    await formRef.value.validate()
    submitting.value = true

    await updateConfig(formData.id, {
      configValue: formData.configValue
    })

    ElMessage.success('Configuration saved successfully')
    dialogVisible.value = false
    fetchConfigList()
  } catch (error) {
    if (error !== false) {
      ElMessage.error('Save failed')
    }
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  fetchConfigList()
})
</script>

<style scoped>
.config-list {
  padding: 0;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.page-title {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
  color: #1a1a1a;
}

.table-card {
  border: none;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.config-value {
  font-family: monospace;
  background: #f8fafc;
  padding: 2px 6px;
  border-radius: 4px;
  color: #409eff;
}

:deep(.el-table__header) {
  th {
    background-color: #f8fafc !important;
    color: #475569;
    font-weight: 600;
  }
}

:deep(.el-form-item__label) {
  font-weight: 600;
}
</style>
