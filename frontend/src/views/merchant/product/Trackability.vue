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
        <el-table-column prop="origin" label="Origin" width="150" />
        <el-table-column prop="plantingMethod" label="Planting Method" width="150" />
        <el-table-column prop="pickingDate" label="Picking Date" width="120" />
        <el-table-column prop="procDate" label="Processing Date" width="120" />
        <el-table-column prop="certification" label="Certification" width="120" />
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
        label-width="120px"
      >
        <el-form-item label="Origin" prop="origin">
          <el-input v-model="formData.origin" placeholder="e.g. Kunming, Yunnan" />
        </el-form-item>
        <el-form-item label="Planting Method" prop="plantingMethod">
          <el-input v-model="formData.plantingMethod" placeholder="e.g. Greenhouse, Organic" />
        </el-form-item>
        <el-form-item label="Picking Date" prop="pickingDate">
          <el-date-picker
            v-model="formData.pickingDate"
            type="date"
            placeholder="Select date"
            style="width: 100%"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="Processing Date" prop="procDate">
          <el-date-picker
            v-model="formData.procDate"
            type="date"
            placeholder="Select date"
            style="width: 100%"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="Certification" prop="certification">
          <el-input v-model="formData.certification" placeholder="e.g. ISO9001, Green Food" />
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
import { useUserStore } from '@/stores/user'
import {
  getMerchantProducts,
  getProductTrackability,
  saveOrUpdateTrackability,
  deleteTrackability
} from '@/api/merchant'
import { ElMessage, ElMessageBox } from 'element-plus'

const route = useRoute()
const userStore = useUserStore()
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
  prodId: null,
  origin: '',
  plantingMethod: '',
  pickingDate: '',
  procDate: '',
  certification: '',
  description: ''
})

const rules = {
  prodId: [{ required: true, message: 'Please select a product', trigger: 'change' }],
  origin: [{ required: true, message: 'Please enter the origin', trigger: 'blur' }]
}

const fetchProducts = async () => {
  try {
    const merchId = userStore.userId
    const res = await getMerchantProducts({ 
      current: 1, 
      size: 100, 
      merchId 
    })
    // Backend returns IPage for merchant products
    products.value = (res.data.records || []).map(item => ({
      ...item,
      id: item.id ?? item.prodId ?? item.productId
    }))
    
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
    // The backend returns a single ProductTrackability object or null
    // Make sure we handle potential error codes or empty data
    if (res.code === 200 && res.data) {
      trackabilityList.value = [res.data]
    } else {
      trackabilityList.value = []
    }
  } catch (error) {
    // If it's just "not found", we shouldn't show an error message as the merchant might just not have created one yet
    trackabilityList.value = []
    console.log('No trackability record found')
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  isEdit.value = false
  Object.assign(formData, {
    id: null,
    prodId: selectedProductId.value,
    origin: '',
    plantingMethod: '',
    pickingDate: '',
    procDate: '',
    certification: '',
    description: ''
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  Object.assign(formData, row)
  dialogVisible.value = true
}

const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  saving.value = true
  try {
    const data = {
      ...formData,
      prodId: selectedProductId.value
    }

    await saveOrUpdateTrackability(data)
    ElMessage.success(isEdit.value ? 'Updated successfully' : 'Added successfully')
    
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
    
    await deleteTrackability(selectedProductId.value)
    ElMessage.success('Deleted successfully')
    // Clear the lists and form immediately
    trackabilityList.value = []
    Object.assign(formData, {
      id: null,
      prodId: selectedProductId.value,
      origin: '',
      plantingMethod: '',
      pickingDate: '',
      procDate: '',
      certification: '',
      description: ''
    })
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
