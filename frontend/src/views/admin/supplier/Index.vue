<template>
  <div class="supplier-container">
    <el-card class="filter-card">
      <el-form :inline="true" :model="queryParams" class="demo-form-inline">
        <el-form-item label="Supplier Name">
          <el-input v-model="queryParams.name" placeholder="Enter supplier name" clearable />
        </el-form-item>
        <el-form-item label="Status">
          <el-select v-model="queryParams.status" placeholder="Select Status" clearable style="width: 151px">
            <el-option label="Active" value="ACTIVE" />
            <el-option label="Inactive" value="INACTIVE" />
            <el-option label="Suspended" value="SUSPENDED" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="Search" @click="handleQuery">Search</el-button>
          <el-button icon="Refresh" @click="resetQuery">Reset</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="table-card">
      <template #header>
        <div class="card-header">
          <el-button type="primary" icon="Plus" @click="handleAdd">Add Supplier</el-button>
        </div>
      </template>

      <el-table v-loading="loading" :data="supplierList" border style="width: 100%">
        <el-table-column prop="id" label="ID" width="80" align="center" />
        <el-table-column prop="name" label="Supplier Name" min-width="150" show-overflow-tooltip />
        <el-table-column prop="contactPerson" label="Contact" width="120" />
        <el-table-column prop="phone" label="Phone" width="130" />
        <el-table-column prop="email" label="Email" width="180" show-overflow-tooltip />
        <el-table-column prop="rating" label="Rating" width="150" align="center">
          <template #default="scope">
            <el-rate v-model="scope.row.rating" disabled show-score text-color="#ff9900" />
          </template>
        </el-table-column>
        <el-table-column prop="status" label="Status" width="100" align="center">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusLabel(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createDate" label="Create Date" width="170" />
        <el-table-column label="Actions" min-width="260" fixed="right">
          <template #default="scope">
            <div class="operation-buttons">
              <el-button 
                type="primary" 
                plain
                size="small"
                icon="Edit" 
                @click="handleUpdate(scope.row)"
              >
                Edit
              </el-button>
              <el-button 
                :type="scope.row.status === 'ACTIVE' ? 'warning' : 'success'" 
                plain
                size="small"
                :icon="scope.row.status === 'ACTIVE' ? 'VideoPause' : 'VideoPlay'"
                @click="toggleStatus(scope.row)"
              >
                {{ scope.row.status === 'ACTIVE' ? 'Deactivate' : 'Activate' }}
              </el-button>
              <el-button 
                type="danger" 
                plain
                size="small"
                icon="Delete" 
                @click="handleDelete(scope.row)"
              >
                Delete
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          v-model:current-page="queryParams.current"
          v-model:page-size="queryParams.size"
          :page-sizes="[10, 20, 50, 100]"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          @size-change="getList"
          @current-change="getList"
        />
      </div>
    </el-card>

    <!-- Add/Edit Dialog -->
    <el-dialog :title="dialogTitle" v-model="open" width="600px" append-to-body>
      <el-form ref="supplierRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="Supplier Name" prop="name">
          <el-input v-model="form.name" placeholder="Enter supplier name" />
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="Contact" prop="contactPerson">
              <el-input v-model="form.contactPerson" placeholder="Enter contact name" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="Phone" prop="phone">
              <el-input v-model="form.phone" placeholder="Enter phone" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="Email" prop="email">
          <el-input v-model="form.email" placeholder="Enter email" />
        </el-form-item>
        <el-form-item label="Address" prop="address">
          <el-input v-model="form.address" placeholder="Enter full address" />
        </el-form-item>
        <el-form-item label="Description" prop="description">
          <el-input v-model="form.description" type="textarea" placeholder="Enter supplier description" />
        </el-form-item>
        <el-form-item label="Status" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="ACTIVE">Active</el-radio>
            <el-radio label="INACTIVE">Inactive</el-radio>
            <el-radio label="SUSPENDED">Suspended</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="cancel">Cancel</el-button>
          <el-button type="primary" @click="submitForm">Confirm</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getSupplierPage, saveSupplier, deleteSupplier, updateSupplierStatus } from '@/api/supplier'

const loading = ref(false)
const total = ref(0)
const supplierList = ref([])
const open = ref(false)
const dialogTitle = ref('')
const supplierRef = ref(null)

const queryParams = reactive({
  current: 1,
  size: 10,
  name: '',
  status: ''
})

const form = reactive({
  id: undefined,
  name: '',
  contactPerson: '',
  phone: '',
  email: '',
  address: '',
  description: '',
  status: 'ACTIVE',
  rating: 5.0
})

const rules = {
  name: [{ required: true, message: 'Supplier name is required', trigger: 'blur' }],
  contactPerson: [{ required: true, message: 'Contact person is required', trigger: 'blur' }],
  phone: [{ required: true, message: 'Phone number is required', trigger: 'blur' }],
  email: [{ type: 'email', message: 'Please enter a valid email address', trigger: ['blur', 'change'] }]
}

const getList = async () => {
  loading.value = true
  try {
    const res = await getSupplierPage(queryParams)
    supplierList.value = res.data.records
    total.value = res.data.total
  } catch (error) {
    console.error('Failed to fetch supplier list:', error)
  } finally {
    loading.value = false
  }
}

const handleQuery = () => {
  queryParams.current = 1
  getList()
}

const resetQuery = () => {
  queryParams.name = ''
  queryParams.status = ''
  handleQuery()
}

const handleAdd = () => {
  Object.assign(form, {
    id: undefined,
    name: '',
    contactPerson: '',
    phone: '',
    email: '',
    address: '',
    description: '',
    status: 'ACTIVE',
    rating: 5.0
  })
  dialogTitle.value = 'Add Supplier'
  open.value = true
}

const handleUpdate = (row) => {
  Object.assign(form, row)
  dialogTitle.value = 'Edit Supplier'
  open.value = true
}

const toggleStatus = (row) => {
  const newStatus = row.status === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE'
  const text = newStatus === 'ACTIVE' ? 'activate' : 'deactivate'
  ElMessageBox.confirm(`Are you sure you want to ${text} supplier "${row.name}"?`, 'Warning', {
    confirmButtonText: 'Confirm',
    cancelButtonText: 'Cancel',
    type: 'warning'
  }).then(async () => {
    await updateSupplierStatus(row.id, newStatus)
    ElMessage.success(`${text.charAt(0).toUpperCase() + text.slice(1)} success`)
    getList()
  }).catch(() => {})
}

const handleDelete = (row) => {
  ElMessageBox.confirm(`Are you sure you want to delete supplier "${row.name}"? This action cannot be undone!`, 'Warning', {
    confirmButtonText: 'Delete',
    cancelButtonText: 'Cancel',
    type: 'error'
  }).then(async () => {
    await deleteSupplier(row.id)
    ElMessage.success('Delete success')
    getList()
  }).catch(() => {})
}

const cancel = () => {
  open.value = false
}

const submitForm = () => {
  supplierRef.value.validate(async (valid) => {
    if (valid) {
      await saveSupplier(form)
      ElMessage.success('Operation success')
      open.value = false
      getList()
    }
  })
}

const getStatusType = (status) => {
  const types = { ACTIVE: 'success', INACTIVE: 'danger', SUSPENDED: 'warning' }
  return types[status] || 'info'
}

const getStatusLabel = (status) => {
  const labels = { ACTIVE: 'Active', INACTIVE: 'Inactive', SUSPENDED: 'Suspended' }
  return labels[status] || status
}

onMounted(() => {
  getList()
})
</script>

<style scoped>
.supplier-container {
  padding: 20px;
}
.filter-card {
  margin-bottom: 20px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
.operation-buttons {
  display: flex;
  gap: 12px;
  justify-content: flex-start;
}
</style>
