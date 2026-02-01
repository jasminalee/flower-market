<template>
  <div class="customer-page">
    <CustomerHeader />
    
    <div class="container">
      <el-card class="coupon-center mt-lg">
        <template #header>
          <div class="card-header">
              <h2>Coupon Center</h2>
              <el-button type="primary" link @click="goToMyCoupons">
                My Coupons
              </el-button>
            </div>
        </template>

        <div v-loading="loading" class="coupon-content">
          <el-empty v-if="!loading && couponList.length === 0" description="No coupons available" />
          
          <el-row :gutter="20" v-else>
            <el-col :span="8" v-for="coupon in couponList" :key="coupon.couponId">
              <div class="coupon-card" :class="getCouponTypeClass(coupon.type)">
                <div class="coupon-left">
                  <div class="coupon-amount">
                    <span class="amount-symbol">¥</span>
                    <span class="amount-value">{{ coupon.value }}</span>
                  </div>
                  <div class="coupon-condition">
                    Minimum spend ¥{{ coupon.minPrice }}
                  </div>
                </div>
                
                <div class="coupon-divider">
                  <div class="divider-circle top"></div>
                  <div class="divider-line"></div>
                  <div class="divider-circle bottom"></div>
                </div>
                
                <div class="coupon-right">
                  <div class="coupon-info">
                    <h4 class="coupon-name">{{ coupon.name }}</h4>
                    <p class="coupon-desc">{{ coupon.description }}</p>
                    <div class="coupon-time">
                      <el-icon><Clock /></el-icon>
                      {{ formatDate(coupon.startDate) }} - {{ formatDate(coupon.endDate) }}
                    </div>
                    <div class="coupon-stock">
                      <el-icon><Goods /></el-icon>
                      Remaining {{ (coupon.totalQuantity - coupon.receivedQuantity) || 0 }}
                    </div>
                  </div>
                  
                  <el-button
                    :type="coupon.received ? 'info' : 'danger'"
                    :disabled="coupon.received || (coupon.totalQuantity - coupon.receivedQuantity) <= 0"
                    @click="handleReceive(coupon)"
                    size="small"
                    class="receive-btn"
                  >
                    {{ coupon.received ? 'Received' : ((coupon.totalQuantity - coupon.receivedQuantity) <= 0 ? 'Sold Out' : 'Receive Now') }}
                  </el-button>
                </div>
              </div>
            </el-col>
          </el-row>
        </div>
      </el-card>
    </div>
    
    <CustomerFooter />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Clock, Goods } from '@element-plus/icons-vue'
import CustomerHeader from '@/components/layouts/CustomerHeader.vue'
import CustomerFooter from '@/components/layouts/CustomerFooter.vue'
import { useUserStore } from '@/stores/user'
import { getCouponList, claimCoupon } from '@/api/coupon'
import { formatDate } from '@/utils/format'

const router = useRouter()
const userStore = useUserStore()

const loading = ref(false)
const couponList = ref([])

onMounted(() => {
  loadCoupons()
})

const loadCoupons = async () => {
  loading.value = true
  try {
    const res = await getCouponList({
      status: 'ACTIVE',
      current: 1,
      size: 100
    })
    couponList.value = res.data?.records || []
    
    // Check user's claimed coupons
    if (userStore.isLoggedIn) {
      await checkReceivedCoupons()
    }
  } catch (error) {
    console.error('Load coupons error:', error)
    ElMessage.error(error.message || 'Failed to load coupons')
  } finally {
    loading.value = false
  }
}

const checkReceivedCoupons = async () => {
  try {
    const res = await getCouponList({
      userId: userStore.userId,
      current: 1,
      size: 1000
    })
    const receivedCouponIds = (res.data?.records || []).map(c => c.couponId)
    
    // Mark claimed coupons
    couponList.value.forEach(coupon => {
      coupon.received = receivedCouponIds.includes(coupon.couponId)
    })
  } catch (error) {
    console.error('Check received coupons error:', error)
  }
}

const handleReceive = async (coupon) => {
    if (!userStore.isLoggedIn) {
    ElMessage.warning('Please log in first')
    router.push('/login')
    return
  }
  
  try {
    await claimCoupon({
      userId: userStore.userId,
      couponId: coupon.couponId
    })
    
    ElMessage.success('Claimed successfully')
    coupon.received = true
    coupon.remainingQuantity = Math.max(0, (coupon.remainingQuantity || 0) - 1)
  } catch (error) {
    console.error('Receive coupon error:', error)
    ElMessage.error(error.message || 'Claim failed')
  }
}

const getCouponTypeClass = (type) => {
  const classMap = {
    'FULL_REDUCTION': 'type-full-reduction',
    'DISCOUNT': 'type-discount',
    'CASH': 'type-cash'
  }
  return classMap[type] || 'type-default'
}

const goToMyCoupons = () => {
  if (!userStore.isLoggedIn) {
    ElMessage.warning('Please log in first')
    router.push('/login')
    return
  }
  router.push('/profile/coupons')
}
</script>

<style scoped>
.coupon-center {
  margin-bottom: 40px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header h2 {
  margin: 0;
  font-size: 24px;
  font-weight: 500;
}

.coupon-content {
  min-height: 400px;
}

.coupon-card {
  display: flex;
  height: 180px;
  margin-bottom: 20px;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: all 0.3s;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.coupon-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.2);
}

.coupon-card.type-full-reduction {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.coupon-card.type-discount {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.coupon-card.type-cash {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.coupon-left {
  flex: 0 0 140px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  color: white;
  padding: 20px;
}

.coupon-amount {
  display: flex;
  align-items: baseline;
  margin-bottom: 8px;
}

.amount-symbol {
  font-size: 20px;
  font-weight: bold;
}

.amount-value {
  font-size: 48px;
  font-weight: bold;
  line-height: 1;
}

.coupon-condition {
  font-size: 12px;
  opacity: 0.9;
}

.coupon-divider {
  position: relative;
  width: 2px;
}

.divider-line {
  position: absolute;
  top: 12px;
  bottom: 12px;
  left: 0;
  right: 0;
  border-left: 2px dashed rgba(255, 255, 255, 0.5);
}

.divider-circle {
  position: absolute;
  width: 20px;
  height: 20px;
  background: white;
  border-radius: 50%;
  left: -9px;
}

.divider-circle.top {
  top: -10px;
}

.divider-circle.bottom {
  bottom: -10px;
}

.coupon-right {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding: 20px;
  background: white;
}

.coupon-info {
  flex: 1;
}

.coupon-name {
  margin: 0 0 8px 0;
  font-size: 16px;
  font-weight: bold;
  color: #303133;
}

.coupon-desc {
  margin: 0 0 12px 0;
  font-size: 12px;
  color: #909399;
  line-height: 1.4;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.coupon-time,
.coupon-stock {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  color: #606266;
  margin-bottom: 4px;
}

.receive-btn {
  width: 100%;
  margin-top: 8px;
}
</style>
