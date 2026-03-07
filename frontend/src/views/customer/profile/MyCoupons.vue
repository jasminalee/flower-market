<template>
  <div class="my-coupons">
    <el-card>
      <template #header>
        <div class="card-header">
          <h2>My Coupons</h2>
          <el-button type="primary" link @click="goToCouponCenter">
            Coupon Center
          </el-button>
        </div>
      </template>

      <el-tabs v-model="activeTab" @tab-change="handleTabChange">
        <el-tab-pane label="Unused" name="unused">
          <div v-loading="loading" class="coupon-list">
            <el-empty v-if="!loading && filteredCoupons.length === 0" description="No unused coupons" />
            
            <el-row :gutter="20" v-else>
              <el-col :span="8" v-for="coupon in filteredCoupons" :key="coupon.id">
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
                      <h4 class="coupon-name">{{ coupon.couponName || coupon.name }}</h4>
                      <p class="coupon-desc">{{ coupon.description }}</p>
                      <div class="coupon-time">
                        <el-icon><Clock /></el-icon>
                        {{ formatDate(coupon.startDate) }} - {{ formatDate(coupon.endDate) }}
                      </div>
                    </div>
                    
                    <el-button
                      type="danger"
                      size="small"
                      @click="goToProducts"
                      class="use-btn"
                    >
                      Use Now
                    </el-button>
                  </div>
                </div>
              </el-col>
            </el-row>
          </div>
        </el-tab-pane>

        <el-tab-pane label="Used" name="used">
          <div v-loading="loading" class="coupon-list">
            <el-empty v-if="!loading && filteredCoupons.length === 0" description="No used coupons" />
            
            <el-row :gutter="20" v-else>
              <el-col :span="8" v-for="coupon in filteredCoupons" :key="coupon.id">
                <div class="coupon-card disabled" :class="getCouponTypeClass(coupon.type)">
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
                      <h4 class="coupon-name">{{ coupon.couponName || coupon.name }}</h4>
                      <p class="coupon-desc">{{ coupon.description }}</p>
                      <div class="coupon-time">
                        <el-icon><Clock /></el-icon>
                        Used At: {{ formatDate(coupon.usedDate) }}
                      </div>
                    </div>
                    
                    <el-tag type="info" size="small">Used</el-tag>
                  </div>
                </div>
              </el-col>
            </el-row>
          </div>
        </el-tab-pane>

        <el-tab-pane label="Expired" name="expired">
          <div v-loading="loading" class="coupon-list">
            <el-empty v-if="!loading && filteredCoupons.length === 0" description="No expired coupons" />
            
            <el-row :gutter="20" v-else>
              <el-col :span="8" v-for="coupon in filteredCoupons" :key="coupon.id">
                <div class="coupon-card disabled" :class="getCouponTypeClass(coupon.type)">
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
                      <h4 class="coupon-name">{{ coupon.couponName || coupon.name }}</h4>
                      <p class="coupon-desc">{{ coupon.description }}</p>
                      <div class="coupon-time">
                          <el-icon><Clock /></el-icon>
                          Expires At: {{ formatDate(coupon.endDate) }}
                        </div>
                    </div>
                    
                    <el-tag type="danger" size="small">Expired</el-tag>
                  </div>
                </div>
              </el-col>
            </el-row>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Clock } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { getCustomerCouponList } from '@/api/coupon'
import { formatDate } from '@/utils/format'

const router = useRouter()
const userStore = useUserStore()

const loading = ref(false)
const activeTab = ref('unused')
const couponList = ref([])

const filteredCoupons = computed(() => {
  const now = new Date()
  
  switch (activeTab.value) {
    case 'unused':
      return couponList.value.filter(c => 
        c.status === 'UNUSED' && new Date(c.endDate) > now
      )
    case 'used':
      return couponList.value.filter(c => c.status === 'USED')
    case 'expired':
      return couponList.value.filter(c => 
        c.status === 'UNUSED' && new Date(c.endDate) <= now
      )
    default:
      return []
  }
})

onMounted(() => {
  loadMyCoupons()
})

const loadMyCoupons = async () => {
  loading.value = true
  try {
    const res = await getCustomerCouponList({
      userId: userStore.userInfo?.userId || userStore.userId,
      current: 1,
      size: 100
    })
    console.log('My Coupons Data:', res.data)
    // 根据响应结构尝试获取数据：可能是数组 res.data，也可能是分页对象 res.data.records
    couponList.value = Array.isArray(res.data) ? res.data : (res.data?.records || [])
  } catch (error) {
    console.error('Load my coupons error:', error)
    ElMessage.error(error.message || 'Failed to load coupons')
  } finally {
    loading.value = false
  }
}

const handleTabChange = () => {
  // Can reload data when tab changes
}

const getCouponTypeClass = (type) => {
  const classMap = {
    'FULL_REDUCTION': 'type-full-reduction',
    'DISCOUNT': 'type-discount',
    'CASH': 'type-cash'
  }
  return classMap[type] || 'type-default'
}

const goToProducts = () => {
  router.push('/products')
}

const goToCouponCenter = () => {
  router.push('/coupons')
}
</script>

<style scoped>
.my-coupons {
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

.coupon-list {
  min-height: 400px;
  padding-top: 20px;
}

.coupon-card {
  display: flex;
  height: 160px;
  margin-bottom: 20px;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  transition: all 0.3s;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

.coupon-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.15);
}

.coupon-card.disabled {
  opacity: 0.6;
  filter: grayscale(50%);
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
  flex: 0 0 120px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  color: white;
  padding: 16px;
}

.coupon-amount {
  display: flex;
  align-items: baseline;
  margin-bottom: 8px;
}

.amount-symbol {
  font-size: 18px;
  font-weight: bold;
}

.amount-value {
  font-size: 40px;
  font-weight: bold;
  line-height: 1;
}

.coupon-condition {
  font-size: 11px;
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
  padding: 16px;
  background: white;
}

.coupon-info {
  flex: 1;
}

.coupon-name {
  margin: 0 0 6px 0;
  font-size: 15px;
  font-weight: bold;
  color: #303133;
}

.coupon-desc {
  margin: 0 0 10px 0;
  font-size: 12px;
  color: #909399;
  line-height: 1.4;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
}

.coupon-time {
  display: fle