<template>
  <div class="config-list">
    <h2 class="page-title">System Configuration</h2>

    <!-- Configuration group tabs -->
    <el-tabs v-model="activeCategory" @tab-change="handleCategoryChange" class="tabs">
      <el-tab-pane label="Basic Settings" name="basic" />
      <el-tab-pane label="Contact Info" name="contact" />
      <el-tab-pane label="Points Reward" name="reward" />
      <el-tab-pane label="Shipping" name="shipping" />
      <el-tab-pane label="Order Settings" name="order" />
      <el-tab-pane label="Review Settings" name="review" />
      <el-tab-pane label="Shopping Cart" name="cart" />
      <el-tab-pane label="Upload Settings" name="upload" />
    </el-tabs>

    <!-- Configuration table -->
    <el-card shadow="never" class="table-card">
      <el-table :data="configList" v-loading="loading" style="width: 100%">
        <el-table-column prop="configKey" label="Config Key" width="250" />
        <el-table-column prop="configValue" label="Config Value" min-width="200" show-overflow-tooltip />
        <el-table-column prop="description" label="Description" min-width="250" />
        <el-table-column label="Action" width="120" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">Edit</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- Edit configuration dialog -->
    <el-dialog v-model="dialogVisible" title="Edit Configuration" width="600px">
      <el-form :model="formData" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="Config Key">
          <el-input v-model="formData.configKey" disabled />
        </el-form-item>
        <el-form-item label="Config Value" prop="configValue">
          <el-input v-model="formData.configValue" placeholder="Please enter config value" />
        </el-form-item>
        <el-form-item label="Description">
          <el-input v-model="formData.description" type="textarea" :rows="3" disabled />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="handleSave" :loading="submitting">Save</el-button>
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
  padding: 20px;
}

.page-title {
  margin: 0 0 20px;
  font-size: 24px;
  font-weight: 500;
  color: #303133;
}

.tabs {
  margin-bottom: 20px;
}

.table-card {
  margin-bottom: 20px;
}
</style>
