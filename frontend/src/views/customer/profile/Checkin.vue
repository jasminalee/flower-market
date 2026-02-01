<template>
  <div class="checkin-page">
    <el-card>
      <template #header>
        <div class="card-header">
          <h2>Check-in Center</h2>
          <div class="header-stats">
            <el-tag type="success" size="large">
              Consecutive {{ stats.continuousDays }} days
            </el-tag>
            <el-tag type="warning" size="large">
              Total {{ stats.totalDays }} days
            </el-tag>
          </div>
        </div>
      </template>

      <div class="checkin-content">
        <!-- Check-in button area -->
        <div class="checkin-action">
          <div class="checkin-button-wrapper">
            <el-button
              type="primary"
              size="large"
              round
              :disabled="todayChecked || checking"
              :loading="checking"
              @click="handleCheckin"
              class="checkin-btn"
            >
              <span v-if="!todayChecked">
                <el-icon><Select /></el-icon>
                {{ checking ? 'Checking in...' : 'Check In Now' }}
              </span>
              <span v-else>
                <el-icon><CircleCheck /></el-icon>
                Already checked in
              </span>
            </el-button>
          </div>
          
          <div class="checkin-reward">
            <p class="reward-text">Check in to earn points</p>
            <p class="reward-rule">Check in 7 consecutive days to earn extra rewards!</p>
          </div>
        </div>

        <!-- Check-in Calendar -->
        <el-divider />
        
        <div class="checkin-calendar">
            <div class="calendar-header">
            <el-button 
              :icon="ArrowLeft" 
              circle 
              @click="changeMonth(-1)"
            />
            <h3>{{ currentYearMonth }}</h3>
            <el-button 
              :icon="ArrowRight" 
              circle 
              @click="changeMonth(1)"
              :disabled="isCurrentMonth"
            />
          </div>

          <div class="calendar-body">
            <div class="calendar-weekdays">
              <div class="weekday" v-for="day in weekdays" :key="day">{{ day }}</div>
            </div>
            
            <div class="calendar-days">
              <div
                v-for="(day, index) in calendarDays"
                :key="index"
                class="calendar-day"
                :class="{
                  'other-month': !day.isCurrentMonth,
                  'today': day.isToday,
                  'checked': day.isChecked
                }"
              >
                <span class="day-number">{{ day.day }}</span>
                <el-icon v-if="day.isChecked" class="check-icon"><CircleCheck /></el-icon>
              </div>
            </div>
          </div>
        </div>

        <!-- Check-in history -->
        <el-divider />
        
        <div class="checkin-history">
          <h3>Check-in History</h3>
          <el-table
            :data="historyList"
            v-loading="historyLoading"
            max-height="300"
          >
            <el-table-column prop="checkDate" label="Date" width="150">
              <template #default="{ row }">
                {{ formatDate(row.checkDate) }}
              </template>
            </el-table-column>
            <el-table-column prop="continuousDays" label="Consecutive Days" width="100">
              <template #default="{ row }">
                {{ row.continuousDays }} days
              </template>
            </el-table-column>
            <el-table-column prop="rewardPoints" label="Points Earned" width="100">
              <template #default="{ row }">
                <el-tag type="success">+{{ row.rewardPoints }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="Checked At">
              <template #default="{ row }">
                {{ formatDateTime(row.createTime) }}
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Select, CircleCheck, ArrowLeft, ArrowRight } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { checkin, getCheckinHistory, getCheckinStats } from '@/api/checkin'
import { formatDate } from '@/utils/format'

const userStore = useUserStore()

const checking = ref(false)
const todayChecked = ref(false)
const historyLoading = ref(false)

const stats = ref({
  continuousDays: 0,
  totalDays: 0,
  lastCheckDate: null
})

const historyList = ref([])
const checkinDates = ref([]) // check-in date collection

const currentDate = ref(new Date())
const weekdays = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat']

const currentYearMonth = computed(() => {
  const year = currentDate.value.getFullYear()
  const month = currentDate.value.getMonth() + 1
  return `${year}-${String(month).padStart(2, '0')}`
})

const isCurrentMonth = computed(() => {
  const now = new Date()
  return currentDate.value.getFullYear() === now.getFullYear() &&
         currentDate.value.getMonth() === now.getMonth()
})

const calendarDays = computed(() => {
  const year = currentDate.value.getFullYear()
  const month = currentDate.value.getMonth()
  
  // First day of the month
  const firstDay = new Date(year, month, 1)
  const firstDayWeek = firstDay.getDay()
  
  // Last day of the month
  const lastDay = new Date(year, month + 1, 0)
  const lastDate = lastDay.getDate()
  
  const days = []
  
  // Dates from the previous month to fill in
  const prevMonthLastDay = new Date(year, month, 0).getDate()
  for (let i = firstDayWeek - 1; i >= 0; i--) {
    days.push({
      day: prevMonthLastDay - i,
      isCurrentMonth: false,
      isToday: false,
      isChecked: false
    })
  }
  
  // Current month dates
  const today = new Date()
  for (let i = 1; i <= lastDate; i++) {
    const dateStr = `${year}-${String(month + 1).padStart(2, '0')}-${String(i).padStart(2, '0')}`
    days.push({
      day: i,
      isCurrentMonth: true,
      isToday: year === today.getFullYear() && 
               month === today.getMonth() && 
               i === today.getDate(),
      isChecked: checkinDates.value.includes(dateStr)
    })
  }
  
  // Dates from the next month to fill in
  const remainingDays = 42 - days.length // 6 rows x 7 columns
  for (let i = 1; i <= remainingDays; i++) {
    days.push({
      day: i,
      isCurrentMonth: false,
      isToday: false,
      isChecked: false
    })
  }
  
  return days
})

onMounted(() => {
  loadCheckinData()
})

const loadCheckinData = async () => {
  await Promise.all([
    loadCheckinStats(),
    loadCheckinHistory()
  ])
  
  // Check if today is already checked in
  checkTodayCheckin()
}

const loadCheckinStats = async () => {
  try {
    const res = await getCheckinStats(userStore.userId)
    if (res.data) {
      stats.value = res.data
    }
  } catch (error) {
    console.error('Load checkin stats error:', error)
  }
}

const loadCheckinHistory = async () => {
  historyLoading.value = true
  try {
    const res = await getCheckinHistory(userStore.userId)
    historyList.value = res.data || []
    
    // Extract check-in dates
    checkinDates.value = historyList.value.map(item => {
      const date = new Date(item.checkDate)
      return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
    })
  } catch (error) {
    console.error('Load checkin history error:', error)
  } finally {
    historyLoading.value = false
  }
}

const checkTodayCheckin = () => {
  const today = new Date()
  const todayStr = `${today.getFullYear()}-${String(today.getMonth() + 1).padStart(2, '0')}-${String(today.getDate()).padStart(2, '0')}`
  todayChecked.value = checkinDates.value.includes(todayStr)
}

const handleCheckin = async () => {
  checking.value = true
  try {
    const res = await checkin(userStore.userId)
    
    if (res.code === 200) {
      const reward = res.data?.rewardPoints || 10
      ElMessage.success(`Check-in successful! Earned ${reward} points`)
      
      todayChecked.value = true
      await loadCheckinData()
      
      // Update user points
      if (userStore.userInfo) {
        userStore.setUserInfo({
          ...userStore.userInfo,
          points: (userStore.userInfo.points || 0) + reward
        })
      }
    }
  } catch (error) {
    console.error('Checkin error:', error)
    ElMessage.error(error.message || 'Check-in failed')
  } finally {
    checking.value = false
  }
}

const changeMonth = (offset) => {
  const newDate = new Date(currentDate.value)
  newDate.setMonth(newDate.getMonth() + offset)
  currentDate.value = newDate
}

const formatDateTime = (dateTime) => {
  if (!dateTime) return ''
  const date = new Date(dateTime)
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}:${String(date.getSeconds()).padStart(2, '0')}`
}
</script>

<style scoped>
.checkin-page {
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

.header-stats {
  display: flex;
  gap: 12px;
}

.checkin-content {
  padding: 20px 0;
}

.checkin-action {
  text-align: center;
  padding: 40px 0;
}

.checkin-button-wrapper {
  margin-bottom: 20px;
}

.checkin-btn {
  width: 200px;
  height: 60px;
  font-size: 18px;
  font-weight: bold;
}

.checkin-reward {
  color: #909399;
}

.reward-text {
  margin: 8px 0;
  font-size: 14px;
}

.reward-rule {
  margin: 4px 0;
  font-size: 12px;
  color: #f56c6c;
}

.checkin-calendar {
  padding: 20px;
}

.calendar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.calendar-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 500;
}

.calendar-weekdays {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 8px;
  margin-bottom: 8px;
}

.weekday {
  text-align: center;
  font-size: 14px;
  font-weight: bold;
  color: #606266;
  padding: 8px 0;
}

.calendar-days {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 8px;
}

.calendar-day {
  aspect-ratio: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
  position: relative;
}

.calendar-day.other-month {
  color: #dcdfe6;
}

.calendar-day.today {
  background-color: #ecf5ff;
  border: 2px solid #409eff;
}

.calendar-day.checked {
  background-color: #f0f9ff;
  color: #409eff;
}

.calendar-day.checked.today {
  background-color: #409eff;
  color: white;
}

.day-number {
  font-size: 14px;
  font-weight: 500;
}

.check-icon {
  font-size: 20px;
  color: #67c23a;
  position: absolute;
  top: 2px;
  right: 2px;
}

.calendar-day.checked.today .check-icon {
  color: white;
}

.checkin-history {
  padding: 20px;
}

.checkin-history h3 {
  margin: 0 0 16px 0;
  font-size: 16px;
  font-weight: 500;
}
</style>
