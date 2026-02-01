<template>
  <div class="trackability-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>Product Traceability</span>
          <el-button @click="$router.back()">Back</el-button>
        </div>
      </template>

      <!-- Select product -->
      <el-form :inline="true">
        <el-form-item label="Product">
          <el-select v-model="selectedProductId" placeholder="Please select a product" @change="fetchTrackability" style="width: 300px">
            <el-option
              v-for="product in products"
              :key="product.id"
              :label="product.name"
              :value="product.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleAdd" :disabled="!selectedProductId">Add Record</el-button>
        </el-form-item>
      </el-form>

      <el-divider />

      <!-- Traceability record list -->
      <el-table :data="trackabilityList" v-loading="loading" style="width: 100%">
        <el-table-column prop="stage" label="Stage" width="120" />
        <el-table-column prop="time" label="Time" width="180" />
        <el-table-column prop="location" label="Location" width="150" />
        <el-table-column prop="responsible" label="Responsible" width="120" />
        <el-table-column prop="description" label="Description" min-width="200" />
        <el-table-column label="Actions" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">Edit</el-button>
            <el-button type="danger" link @click="handleDelete(row)">Delete</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- Add/Edit traceability record dialog -->
    <el-dialog
      v-model="dialogVisible"
      :title="isEdit ? 'Edit Record' : 'Add Record'"
      width="600px"
    >
      <el-form
        ref="formRef"
        :model="formData"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="Stage" prop="stage">
          <el-input v-model="formData.stage" placeholder="e.g. Planting, Harvest, Shipping" />
        </el-form-item>
        <el-form-item label="Time" prop="time">
          <el-date-picker
            v-model="formData.time"
            type="datetime"
            placeholder="Select date & time"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="Location" prop="location">
          <el-input v-model="formData.location" placeholder="Please enter the location" />
        </el-form-item>
        <el-form-item label="Responsible" prop="responsible">
          <el-input v-model="formData.responsible" placeholder="Please enter the responsible person" />
        </el-form-item>
        <el-form-item label="Description" prop="description">
          <el-input
            v-model="formData.description"
            type="textarea"
            :rows="4"
            placeholder="Please enter details"
            maxlength="500"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="saving">OK</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import {
  getMerchantProducts,
  getProductTrackability,
  createTrackability,
  updateTrackability,
  deleteTrackability
} from '@/api/merchant'
import { ElMessage, ElMessageBox } from 'element-plus'

const route = useRoute()
const formRef = ref(null)
const loading = ref(false)
const saving = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)

const products = ref([])
const selectedProductId = ref(null)
const trackabilityList = ref([])

const formData = reactive({
  id: null,
  stage: '',
  time: '',
  location: '',
  responsible: '',
  description: ''
})

const rules = {
  stage: [{ required: true, message: 'Please enter the stage', trigger: 'blur' }],
  time: [{ required: true, message: 'Please select the time', trigger: 'change' }],
  location: [{ required: true, message: 'Please enter the location', trigger: 'blur' }],
  responsible: [{ required: true, message: 'Please enter the responsible person', trigger: 'blur' }]
}

const fetchProducts = async () => {
  try {
    const res = await getMerchantProducts({ page: 0, size: 100 })
    products.value = res.data.content
    
    // If the URL includes a product ID, auto-select it
    const productId = route.params.id
    if (productId) {
      selectedProductId.value = parseInt(productId)
      fetchTrackability()
    }
  } catch (error) {
    ElMessage.error('Failed to load product list')
  }
}

const fetchTrackability = async () => {
  if (!selectedProductId.value) return
  
  loading.value = true
  try {
    const res = await getProductTrackability(selectedProductId.value)
    trackabilityList.value = res.data || []
  } catch (error) {
    ElMessage.error('Failed to load records')
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  isEdit.value = false
  formData.id = null
  formData.stage = ''
  formData.time = ''
  formData.location = ''
  formData.responsible = ''
  formData.description = ''
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  formData.id = row.id
  formData.stage = row.stage
  formData.time = row.time
  formData.location = row.location
  formData.responsible = row.responsible
  formData.description = row.description
  dialogVisible.value = true
}

const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  saving.value = true
  try {
    const data = {
      stage: formData.stage,
      time: formData.time,
      location: formData.location,
      responsible: formData.responsible,
      description: formData.description
    }

    if (isEdit.value) {
      await updateTrackability(formData.id, data)
      ElMessage.success('Updated successfully')
    } else {
      await createTrackability(selectedProductId.value, data)
      ElMessage.success('Added successfully')
    }
    
    dialogVisible.value = false
    fetchTrackability()
  } catch (error) {
    ElMessage.error(error.message || 'Operation failed')
  } finally {
    saving.value = false
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('Are you sure you want to delete this record?', 'Confirm', {
      type: 'warning'
    })
    
    await deleteTrackability(row.id)
    ElMessage.success('Deleted successfully')
    fetchTrackability()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || 'Delete failed')
    }
  }
}

onMounted(() => {
  fetchProducts()
})
</script>

<style scoped>
.trackability-page {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
