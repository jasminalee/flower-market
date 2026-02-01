<template>
  <div class="balance-page">
    <el-card class="balance-card">
      <template #header>
        <div class="card-header">
          <span>My Balance</span>
        </div>
      </template>

      <div class="balance-info">
        <div class="balance-amount">
          <span class="label">Current Balance:</span>
          <span class="amount">¥{{ balance }}</span>
        </div>
        <el-button type="primary" @click="showRechargeDialog">Recharge</el-button>
      </div>
    </el-card>

    <!-- Balance History -->
    <el-card class="history-card">
      <template #header>
        <div class="card-header">
          <span>Balance History</span>
        </div>
      </template>

      <el-table :data="balanceHistory" v-loading="loading" style="width: 100%">
        <el-table-column prop="createDate" label="Date" width="180"></el-table-column>
        <el-table-column label="Type" width="120">
          <template #default="{ row }">
            <el-tag :type="row.type === 'RECHARGE' ? 'success' : 'warning'">
              {{ getTypeText(row.type) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Amount" width="150">
          <template #default="{ row }">
            <span :class="row.type === 'RECHARGE' ? 'income' : 'expense'">
              {{ row.type === 'RECHARGE' ? '+' : '-' }}¥{{ row.amount }}
            </span>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="Description" min-width="200"></el-table-column>
        <el-table-column label="Balance" width="150">
          <template #default="{ row }">
            ¥{{ row.balance }}
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
        v-if="total > 0"
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :total="total"
        :page-sizes="[10, 20, 50, 100]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="fetchBalanceHistory"
        @current-change="fetchBalanceHistory"
        style="margin-top: 20px; justify-content: flex-end;"
      />
    </el-card>

    <!-- Recharge Dialog -->
    <el-dialog v-model="rechargeDialogVisible" title="Recharge" width="500px">
      <el-form :model="rechargeForm" :rules="rechargeRules" ref="rechargeFormRef" label-width="auto" class="recharge-form">
        <el-form-item label="Amount" prop="amount">
          <el-input-number
            v-model="rechargeForm.amount"
            :min="1"
            :max="10000"
            :precision="2"
            placeholder="Please enter an amount"
            style="width: 140px"
          ></el-input-number>
        </el-form-item>

        <el-form-item label="Payment Method" prop="paymentMethod" class="payment-method-item">
          <el-radio-group v-model="rechargeForm.paymentMethod">
            <el-radio label="ALIPAY">Alipay</el-radio>
            <el-radio label="WECHAT">WeChat Pay</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-alert
          title="Note: This is a course project. Recharge is simulated and no real payment will be processed."
          type="warning"
          :closable="false"
          show-icon
          style="margin-bottom: 20px;"
        />
      </el-form>

      <template #footer>
        <el-button @click="rechargeDialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="handleRecharge" :loading="recharging">
          Confirm Recharge
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { storage } from '@/utils/storage'
import request from '@/utils/request'

const loading = ref(false)
const recharging = ref(false)
const rechargeDialogVisible = ref(false)
const balance = ref(0)
const balanceHistory = ref([])
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const rechargeFormRef = ref()

const rechargeForm = reactive({
  amount: 100,
  paymentMethod: 'ALIPAY'
})

const rechargeRules = {
  amount: [
    { required: true, message: 'Please enter an amount', trigger: 'blur' },
    { type: 'number', min: 1, max: 10000, message: 'Amount must be between 1 and 10000', trigger: 'blur' }
  ],
  paymentMethod: [
    { required: true, message: 'Please select a payment method', trigger: 'change' }
  ]
}

const userInfo = computed(() => storage.get('userInfo'))

const getTypeText = (type) => {
  const typeMap = {
    'RECHARGE': 'Recharge',
    'PAYMENT': 'Payment',
    'REFUND': 'Refund'
  }
  return typeMap[type] || type
}

// Get balance
const fetchBalance = async () => {
  try {
    const response = await request.get('/api/customer/balance', {
      params: { userId: userInfo.value.userId }
    })
    if (response.code === 200) {
      balance.value = response.data
    }
  } catch (error) {
    console.error('Fetch balance failed:', error)
  }
}

// Get balance details
const fetchBalanceHistory = async () => {
  loading.value = true
  try {
    const response = await request.get('/api/customer/balance/history', {
      params: {
        userId: userInfo.value.userId,
        current: currentPage.value,
        size: pageSize.value
      }
    })
    if (response.code === 200) {
      balanceHistory.value = response.data.records
      total.value = response.data.total
    }
  } catch (error) {
    console.error('Fetch balance history failed:', error)
    ElMessage.error('Failed to load balance history')
  } finally {
    loading.value = false
  }
}

const showRechargeDialog = () => {
  rechargeDialogVisible.value = true
}

const handleRecharge = async () => {
  if (!rechargeFormRef.value) return

  await rechargeFormRef.value.validate(async (valid) => {
    if (valid) {
      recharging.value = true
      try {
        const response = await request.post('/api/customer/balance/recharge', {
          userId: userInfo.value.userId,
          amount: rechargeForm.amount,
          paymentMethod: rechargeForm.paymentMethod
        })

        if (response.code === 200) {
          ElMessage.success('Recharge successful')
          rechargeDialogVisible.value = false
          fetchBalance()
          fetchBalanceHistory()
          
          // Update locally stored user information
          if (userInfo.value) {
            userInfo.value.balance = response.data.balance
            storage.set('userInfo', userInfo.value)
          }
        } else {
          ElMessage.error(response.msg || 'Recharge failed')
        }
      } catch (error) {
        console.error('Recharge failed:', error)
        ElMessage.error('Recharge failed')
      } finally {
        recharging.value = false
      }
    }
  })
}

onMounted(() => {
  fetchBalance()
  fetchBalanceHistory()
})
</script>

<style scoped>
.balance-page {
  padding: 20px;
}

.recharge-form {
  /* 确保表单项的标签和控件在同一行 */
}

.payment-method-item {
  display: flex;
  align-items: center;
}
.payment-method-item .el-form-item__content {
  line-height: unset;
}

.balance-card {
  margin-bottom: 20px;
}

.card-header {
  font-size: 18px;
  font-weight: bold;
}

.balance-info {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px;
}

.balance-amount {
  font-size: 24px;
}

.balance-amount .label {
  color: #606266;
  margin-right: 10px;
}

.balance-amount .amount {
  color: #F56C6C;
  font-weight: bold;
  font-size: 32px;
}

.history-card {
  margin-bottom: 20px;
}

.income {
  color: #67C23A;
  font-weight: bold;
}

.expense {
  color: #F56C6C;
  font-weight: bold;
}
</style>
