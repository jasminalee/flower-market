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
                <el-option label="Flowers" value="FLOWER" />
                <el-option label="Green Plants" value="GREEN_PLANT" />
                <el-option label="Bouquets" value="BOUQUET" />
                <el-option label="Potted" value="POTTED" />
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
                <el-radio label="ON_SALE">On Sale</el-radio>
                <el-radio label="OFF_SALE">Off Sale</el-radio>
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
                <img v-if="formData.image" :src="formData.image" class="product-image" />
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

const router = useRouter()
const route = useRoute()
const formRef = ref(null)
const saving = ref(false)

const isEdit = ref(false)
const productId = ref(null)

const formData = reactive({
  name: '',
  category: '',
  price: 0,
  stock: 0,
  image: '',
  description: '',
  status: 'ON_SALE'
})

const detailImages = ref([])

const rules = {
  name: [{ required: true, message: 'Please enter product name', trigger: 'blur' }],
  category: [{ required: true, message: 'Please select a category', trigger: 'change' }],
  price: [{ required: true, message: 'Please enter product price', trigger: 'blur' }],
  stock: [{ required: true, message: 'Please enter stock quantity', trigger: 'blur' }],
  image: [{ required: true, message: 'Please upload a main product image', trigger: 'change' }]
}

const handleImageChange = (file) => {
  const reader = new FileReader()
  reader.onload = (e) => {
    formData.image = e.target.result
  }
  reader.readAsDataURL(file.raw)
}

const handleDetailImageChange = (file, fileList) => {
  const reader = new FileReader()
  reader.onload = (e) => {
    detailImages.value = fileList.map((f, index) => {
      if (f.raw && !f.url) {
        return { ...f, url: e.target.result }
      }
      return f
    })
  }
  if (file.raw) {
    reader.readAsDataURL(file.raw)
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
    const data = {
      ...formData,
      detailImages: detailImages.value.map(img => img.url)
    }

    if (isEdit.value) {
      await updateProduct(productId.value, data)
      ElMessage.success('Saved successfully')
    } else {
      await createProduct(data)
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

const fetchProduct = async () => {
  try {
    const res = await getMerchantProduct(productId.value)
    Object.assign(formData, res.data)
    if (res.data.detailImages) {
      detailImages.value = res.data.detailImages.map((url, index) => ({
        uid: index,
        url
      }))
    }
  } catch (error) {
    ElMessage.error('Failed to load data')
  }
}

onMounted(() => {
  const id = route.params.id
  if (id && id !== 'add') {
    isEdit.value = true
    productId.value = id
    fetchProduct()
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
</style>
