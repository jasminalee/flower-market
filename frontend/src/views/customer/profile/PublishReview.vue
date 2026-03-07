<template>
  <div class="publish-review">
    <el-card>
      <template #header>
        <div class="header">
          <span>Publish Review</span>
          <el-button @click="$router.back()">Back</el-button>
        </div>
      </template>

      <div v-if="loading" class="loading-state">
        <el-skeleton :rows="5" animated />
      </div>

      <div v-else-if="order" class="order-info">
        <h3>Order Items</h3>
        <div v-for="item in order.items" :key="item.id" class="product-item">
          <div class="product-header">
            <el-image :src="item.mainImage" class="prod-img" />
            <span class="prod-name">{{ item.name }}</span>
          </div>

          <el-form :model="reviewForms[item.prodId]" label-width="100px" class="review-form">
            <el-form-item label="Rating" required>
              <el-rate v-model="reviewForms[item.prodId].rating" />
            </el-form-item>
            <el-form-item label="Review" required>
              <el-input
                v-model="reviewForms[item.prodId].content"
                type="textarea"
                rows="3"
                placeholder="Share your experience with this flower..."
              />
            </el-form-item>
          </el-form>
        </div>

        <div class="submit-area">
          <el-button type="primary" size="large" @click="submitAll" :loading="submitting">
            Submit All Reviews
          </el-button>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import request from '@/utils/request'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const loading = ref(true)
const submitting = ref(false)
const order = ref(null)
const reviewForms = ref({})

const fetchOrderDetail = async () => {
  try {
    const { data } = await request.get(`/api/orders/${route.params.id}`)
    order.value = data
    // Initialize forms for each product
    data.items.forEach(item => {
      reviewForms.value[item.prodId] = {
        rating: 5,
        content: ''
      }
    })
  } catch (error) {
    ElMessage.error('Failed to load order info')
  } finally {
    loading.value = false
  }
}

const submitAll = async () => {
  submitting.value = true
  try {
    const promises = Object.keys(reviewForms.value).map(prodId => {
      const form = reviewForms.value[prodId]
      if (!form.content.trim()) return Promise.resolve()
      
      return request.post(`/api/products/${prodId}/review`, {
        orderId: parseInt(route.params.id),
        rating: form.rating,
        content: form.content
      })
    })

    await Promise.all(promises)
    ElMessage.success('Reviews submitted successfully!')
    router.replace('/profile/orders')
  } catch (error) {
    ElMessage.error('Failed to submit reviews')
  } finally {
    submitting.value = false
  }
}

onMounted(fetchOrderDetail)
</script>

<style scoped>
.publish-review {
  padding: 20px;
}
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.product-item {
  border-bottom: 1px solid #eee;
  padding: 20px 0;
}
.product-header {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}
.prod-img {
  width: 60px;
  height: 60px;
  border-radius: 4px;
  margin-right: 15px;
}
.prod-name {
  font-weight: bold;
  font-size: 16px;
}
.review-form {
  max-width: 600px;
}
.submit-area {
  margin-top: 30px;
  text-align: center;
}
</style>
