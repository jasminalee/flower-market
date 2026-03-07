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

            <el-form-item label="Supplier" prop="supplierId">
              <el-select v-model="formData.supplierId" placeholder="Select supplier" clearable filterable>
                <el-option 
                  v-for="item in suppliers"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>

            <el-form-item label="Flowering Period" prop="floweringPeriod">
              <el-input v-model="formData.floweringPeriod" placeholder="e.g., 7-10 days" />
            </el-form-item>

            <el-form-item label="Care Difficulty" prop="careDifficulty">
              <el-select v-model="formData.careDifficulty" placeholder="Select difficulty">
                <el-option label="Easy" value="EASY" />
                <el-option label="Medium" value="MEDIUM" />
                <el-option label="Hard" value="HARD" />
              </el-select>
            </el-form-item>

            <el-form-item label="Status" prop="status">
              <el-radio-group v-model="formData.status">
                <el-radio label="ACTIVE">On Sale</el-radio>
                <el-radio label="INACTIVE">Off Sale</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="Suitable Env" prop="suitableEnvironment">
              <el-input 
                v-model="formData.suitableEnvironment" 
                type="textarea" 
                :rows="3" 
                placeholder="Temperature, light, humidity requirements" 
              />
            </el-form-item>

            <el-form-item label="Floral Language" prop="floralLanguage">
              <el-input v-model="formData.floralLanguage" placeholder="Flower meaning" />
            </el-form-item>

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
import { getMerchantProduct, createProduct, updateProduct, uploadProductImage } from '@/api/merchant'
import { getActiveSuppliers } from '@/api/supplier'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { getAllCategories } from '@/api/product'

const router = useRouter()
const route = useRoute()
const formRef = ref(null)
const saving = ref(false)

const userStore = useUserStore()

// Suppliers
const suppliers = ref([])
const loadSuppliers = async () => {
  try {
    const { data } = await getActiveSuppliers()
    suppliers.value = data || []
  } catch (error) {
    console.error('Failed to load suppliers:', error)
  }
}

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
  supplierId: null,
  image: '',
  description: '',
  floweringPeriod: '',
  careDifficulty: 'MEDIUM',
  suitableEnvironment: '',
  floralLanguage: '',
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

// Convert image URL to displayable format
const getImageUrl = (imagePath) => {
  if (!imagePath) return ''
  if (imagePath.startsWith('http') || imagePath.startsWith('data:image')) {
    return imagePath
  }
  return imagePath
}

// Handle detail images - upload immediately to backend
const handleDetailImageChange = async (file, fileList) => {
  console.log('handleDetailImageChange called', { file, fileListLength: fileList.length })
  
  // Check if this is a new file upload (raw property exists)
  if (file && file.raw) {
    try {
      // Upload to backend immediately
      const res = await uploadProductImage(file.raw, 'detail')
      
      console.log('Upload response:', res)
      
      // Backend returns the image path directly in res.data
      const imagePath = res.data
      
      if (!imagePath) {
        throw new Error('Invalid response: missing image path')
      }
      
      console.log('Image uploaded successfully, path:', imagePath)
      
      // Update the newly added file's URL - find by matching raw file
      const updatedList = fileList.map((f) => {
        if (f.raw === file.raw) {
          return {
            ...f,
            url: imagePath,
            name: imagePath.split('/').pop()
          }
        }
        return f
      })
      
      // Update the ref with the new list
      detailImages.value = updatedList
      
      const allUrls = detailImages.value.map(img => img.url)
      console.log('DetailImages for submission:', allUrls)
      
      ElMessage.success('Image uploaded successfully')
    } catch (error) {
      console.error('Image upload failed:', error)
      ElMessage.error('Image upload failed: ' + (error.message || 'Unknown error'))
      // Remove the failed upload (should be the last one)
      detailImages.value = fileList.slice(0, -1)
    }
  } else {
    // For file removal or list updates - just sync with fileList
    console.log('Syncing fileList without new upload, length:', fileList.length)
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
    const catId = categoryCodeToId.value[formData.category]
    if (!catId && formData.category && isNaN(formData.category)) {
      console.warn('Selected category not found in category mapping:', formData.category)
      ElMessage.warning('Selected category may not be valid, please select a valid category')
    }
    
    const productData = {
      ...formData,
      catId: catId || formData.category,
      merchId: userStore.userId,
    }
    
    delete productData.category
    
    // Only include newly uploaded images (base64 data for main, URLs for detail images)
    const isNewMainImage = formData.image?.startsWith('data:image')
    if (isNewMainImage) {
      productData.image = formData.image
    }
    
    // Detail images - collect all URLs (both existing and newly uploaded)
    if (detailImages.value && detailImages.value.length > 0) {
      // Extract all image URLs (both existing and new)
      const imageUrls = detailImages.value
        .filter(img => img && img.url) // Ensure valid entries
        .map(img => {
          console.log('Including image:', { url: img.url, isExisting: img.isExisting })
          return img.url
        })
      
      console.log('All detail images for submission:', imageUrls)
      
      if (imageUrls.length > 0) {
        // Send as both formats for compatibility
        // images: JSON array (frontend format)
        // detailImages: JSON array (alternative name)
        productData.images = imageUrls
        productData.detailImages = imageUrls
      }
    }
    
    console.log('Final product data being sent:', productData)
    
    if (isEdit.value) {
      await updateProduct(productId.value, productData)
      ElMessage.success('Product updated successfully')
    } else {
      await createProduct(productData)
      ElMessage.success('Product created successfully')
    }
    
    router.push('/merchant/products')
  } catch (error) {
    console.error('Submit error:', error)
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
    const productData = {
      ...res.data,
      category: categoryIdToCode.value[res.data.catId] || res.data.catId
    }
    
    if (!categoryIdToCode.value[res.data.catId] && !categories.value.some(cat => cat.cateId === res.data.catId)) {
      console.warn('Product category not found in loaded categories:', res.data.catId)
    }
    
    // Handle main image
    if (res.data.mainImage) {
      productData.image = res.data.mainImage
    }
    
    Object.assign(formData, productData)
    
    // Handle detail images - images field is a JSON string from backend
    // Parse existing URLs and convert them to el-upload compatible format
    if (res.data.images) {
      try {
        const imageUrls = typeof res.data.images === 'string' 
          ? JSON.parse(res.data.images) 
          : Array.isArray(res.data.images) ? res.data.images : []
        
        console.log('Loaded existing detail images:', imageUrls)
        
        // Convert URL strings to el-upload file objects
        // These are existing images, so they don't have raw files
        detailImages.value = imageUrls.map((url, index) => ({
          uid: `${Date.now()}-${index}`, // Unique ID for Vue key binding
          url: url, // The URL from backend
          name: url.split('/').pop() || `image-${index}`, // Extract filename
          isExisting: true // Mark as existing image (not newly uploaded)
        }))
        
        console.log('Initialized detailImages with existing files:', detailImages.value)
      } catch (error) {
        console.error('Failed to parse images:', error)
      }
    } else if (res.data.detailImages?.length > 0) {
      // Fallback for detailImages field (backward compatibility)
      detailImages.value = res.data.detailImages.map((url, index) => ({
        uid: `${Date.now()}-${index}`,
        url: url,
        name: url.split('/').pop() || `image-${index}`,
        isExisting: true
      }))
    }
  } catch (error) {
    ElMessage.error('Failed to load data')
  }
}

onMounted(async () => {
  // Load categories and suppliers first
  await Promise.all([
    loadCategories(),
    loadSuppliers()
  ])
  
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
