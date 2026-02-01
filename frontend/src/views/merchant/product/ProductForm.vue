<template>
  <div class="product-form">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>{{ isEdit ? 'Edit Product' : 'Add Product' }}</span>
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
            <el-form-item label="Product Name" prop="name">
              <el-input v-model="formData.name" placeholder="Enter product name" />
            </el-form-item>

            <el-form-item label="Category" prop="category">
              <el-select v-model="formData.category" placeholder="Select category">
                <el-option 
                  v-for="category in categories"
                  :key="category.cateId"
                  :label="category.name"
                  :value="category.code || category.cateId"
                />
              </el-select>
            </el-form-item>

            <el-form-item label="Price" prop="price">
              <el-input-number v-model="formData.price" :min="0" :precision="2" :step="1" />
              <span style="margin-left: 10px">CNY</span>
            </el-form-item>

            <el-form-item label="Stock" prop="stock">
              <el-input-number v-model="formData.stock" :min="0" :step="1" />
            </el-form-item>

            <el-form-item label="Status" prop="status">
              <el-radio-group v-model="formData.status">
                <el-radio label="ACTIVE">On Sale</el-radio>
                <el-radio label="INACTIVE">Off Sale</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="Main Image" prop="image">
              <el-upload
                class="image-uploader"
                :show-file-list="false"
                action="#"
                :auto-upload="false"
                :on-change="handleImageChange"
              >
                <img v-if="formData.image" :src="getImageUrl(formData.image)" class="product-image" />
                <el-icon v-else class="uploader-icon"><Plus /></el-icon>
              </el-upload>
              <div class="upload-tip">Recommended size: 800x800px</div>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="Description" prop="description">
          <el-input
            v-model="formData.description"
            type="textarea"
            :rows="8"
            placeholder="Enter product description"
            maxlength="2000"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="Detail Images">
          <el-upload
            class="detail-uploader"
            action="#"
            :auto-upload="false"
            list-type="picture-card"
            :file-list="detailImages"
            :on-change="handleDetailImageChange"
            :on-remove="handleDetailImageRemove"
            :class="detailImages.length > 0 ? 'has-images' : ''"
          >
            <el-icon><Plus /></el-icon>
          </el-upload>
          <div class="upload-tip">You can upload multiple detail images</div>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSubmit" :loading="saving">Save</el-button>
          <el-button @click="handleCancel">Cancel</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { getMerchantProduct, createProduct, updateProduct } from '@/api/merchant'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { getAllCategories } from '@/api/product'

const router = useRouter()
const route = useRoute()
const formRef = ref(null)
const saving = ref(false)

const userStore = useUserStore()

// Category mapping
const categories = ref([])
const categoryCodeToId = ref({})
const categoryIdToCode = ref({})

const isEdit = ref(false)
const productId = ref(null)

const formData = reactive({
  name: '',
  category: '',
  price: 0,
  stock: 0,
  image: '',
  description: '',
  status: 'ACTIVE'
})

const detailImages = ref([])

const rules = {
  name: [{ required: true, message: 'Please enter product name', trigger: 'blur' }],
  category: [{ required: true, message: 'Please select a category', trigger: 'change' }],
  price: [{ required: true, message: 'Please enter product price', trigger: 'blur' }],
  stock: [{ required: true, message: 'Please enter stock quantity', trigger: 'blur' }],
  image: [{ 
    validator: (rule, value, callback) => {
      // In edit mode, image is optional (already exists in DB)
      // In create mode, image is required
      if (isEdit.value) {
        callback()
      } else if (!value) {
        callback(new Error('Please upload a main product image'))
      } else {
        callback()
      }
    }, 
    trigger: 'change' 
  }]
}

const handleImageChange = (file) => {
  const reader = new FileReader()
  reader.onload = (e) => {
    formData.image = e.target.result
  }
  reader.readAsDataURL(file.raw)
}

const getImageUrl = (imagePath) => {
  if (!imagePath) return ''
  // If it's already an absolute URL or base64 data, return as is
  if (imagePath.startsWith('http') || imagePath.startsWith('data:image')) {
    return imagePath
  }
  // For relative paths (like /products/main/...), return as is since Vite proxy handles it
  return imagePath
}

const handleDetailImageChange = (file, fileList) => {
  // Only process new uploads (raw files), not existing images
  if (file.raw) {
    const reader = new FileReader()
    reader.onload = (e) => {
      // Add the new image to the list
      detailImages.value = [...fileList]
    }
    reader.readAsDataURL(file.raw)
  } else {
    // For existing images (not raw files), just update the list
    detailImages.value = fileList
  }
}

const handleDetailImageRemove = (file, fileList) => {
  detailImages.value = fileList
}

const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  saving.value = true
  try {
    // Prepare product data for form submission
    // Map category code to catId for backend compatibility
    const catId = categoryCodeToId.value[formData.category]
    if (!catId && formData.category && isNaN(formData.category)) {
      console.warn('Selected category not found in category mapping:', formData.category)
      ElMessage.warning('Selected category may not be valid, please select a valid category')
    }
    
    // Prepare product data for submission
    const productData = {
      ...formData,
      catId: catId || formData.category,  // Map category code to catId
      merchId: userStore.userId,
    }
    
    // Remove the original category field to avoid conflicts
    delete productData.category
    
    // Only include images if they are new uploads (base64 data), not existing paths
    const isNewMainImage = formData.image && formData.image.startsWith('data:image');
    const hasNewDetailImages = detailImages.value.some(img => img.url && img.url.startsWith('data:image'));
    
    if (isNewMainImage) {
      productData.image = formData.image; // Will be converted to file in API
    }
    
    if (hasNewDetailImages) {
      productData.detailImages = detailImages.value
        .filter(img => img.url && img.url.startsWith('data:image')) // Only new uploads
        .map(img => img.url);
    }
    
    if (isEdit.value) {
      // For editing, if no new images were uploaded, don't send image fields to preserve existing images
      await updateProduct(productId.value, productData)
      ElMessage.success('Saved successfully')
    } else {
      await createProduct(productData)
      ElMessage.success('Added successfully')
    }
    
    router.push('/merchant/products')
  } catch (error) {
    ElMessage.error(error.message || 'Operation failed')
  } finally {
    saving.value = false
  }
}

const handleCancel = () => {
  router.back()
}

const loadCategories = async () => {
  try {
    const res = await getAllCategories()
    categories.value = res.data || []
    
    // Build category maps
    const codeToId = {}
    const idToCode = {}
    
    categories.value.forEach(cat => {
      // Assuming the backend has fields like code (FLOWER, etc.) and cateId (numeric ID)
      if (cat.code) {
        codeToId[cat.code] = cat.cateId
        idToCode[cat.cateId] = cat.code
      } else {
        // If no code field, use name as fallback
        codeToId[cat.name.toUpperCase().replace(/\s+/g, '_')] = cat.cateId
        idToCode[cat.cateId] = cat.name.toUpperCase().replace(/\s+/g, '_')
      }
    })
    
    categoryCodeToId.value = codeToId
    categoryIdToCode.value = idToCode
  } catch (error) {
    console.error('Failed to load categories:', error)
    ElMessage.error('Failed to load categories: ' + (error.message || 'Unknown error'))
  }
}

const fetchProduct = async () => {
  try {
    const res = await getMerchantProduct(productId.value)
    // Map catId to category code for form compatibility
    const productData = {
      ...res.data,
      category: categoryIdToCode.value[res.data.catId] || res.data.catId  // Map catId back to category code
    }
    
    // If the category code isn't in our mapping, try to find it by checking if the catId exists in our categories
    if (!categoryIdToCode.value[res.data.catId] && !categories.value.some(cat => cat.cateId === res.data.catId)) {
      // If we don't have the category loaded, try to load it
      console.warn('Product category not found in loaded categories:', res.data.catId)
    }
    
    // Handle main image - keep relative path for display
    if (res.data.mainImage) {
      // Store the original mainImage path for display
      formData.image = res.data.mainImage
    }
    
    Object.assign(formData, productData)
    
    // Handle detail images - images field is a JSON string from backend
    if (res.data.images) {
      try {
        let imageUrls = []
        if (typeof res.data.images === 'string') {
          imageUrls = JSON.parse(res.data.images)
        } else {
          imageUrls = Array.isArray(res.data.images) ? res.data.images : []
        }
        
        detailImages.value = imageUrls.map((url, index) => ({
          uid: index,
          url: url  // Keep relative path for display
        }))
      } catch (error) {
        console.error('Failed to parse images:', error)
      }
    }
    
    // Fallback for detailImages field if it exists (for backward compatibility)
    if (res.data.detailImages && detailImages.value.length === 0) {
      detailImages.value = res.data.detailImages.map((url, index) => ({
        uid: index,
        url: url  // Keep relative path for display
      }))
    }
  } catch (error) {
    ElMessage.error('Failed to load data')
  }
}

onMounted(async () => {
  // Load categories first
  await loadCategories()
  
  const id = route.params.id
  if (id && id !== 'add') {
    isEdit.value = true
    productId.value = id
    await fetchProduct()
  }
})
</script>

<style scoped>
.product-form {
  padding: 20px;
}

.card-header {
  font-size: 18px;
  font-weight: 500;
}

.image-uploader :deep(.el-upload) {
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

.image-uploader :deep(.el-upload:hover) {
  border-color: var(--el-color-primary);
}

.product-image {
  width: 200px;
  height: 200px;
  object-fit: cover;
}

.uploader-icon {
  font-size: 28px;
  color: #8c939d;
}

.upload-tip {
  margin-top: 10px;
  font-size: 12px;
  color: #909399;
}

.detail-uploader {
  width: 100%;
}

.detail-uploader.has-images {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.detail-uploader.has-images :deep(.el-upload) {
  width: auto;
  height: auto;
}

.detail-uploader.has-images :deep(.el-upload-list__item) {
  width: 120px;
  height: 120px;
}
</style>
