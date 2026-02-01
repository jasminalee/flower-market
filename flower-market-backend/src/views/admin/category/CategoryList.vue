<template>
  <div class="category-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>Product Categories</span>
          <el-button type="primary" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            Add Category
          </el-button>
        </div>
      </template>

      <el-table 
        :data="categories" 
        style="width: 100%" 
        row-key="cateId" 
        default-expand-all
        :tree-props="{ children: 'children' }"
      >
        <el-table-column prop="cateId" label="Category ID" width="110" />
        <el-table-column prop="name" label="Category Name" min-width="200" />
        <el-table-column prop="sortOrder" label="Sort" width="100" />
        <el-table-column label="Icon" width="100" align="center">
          <template #default="{ row }">
            <div class="icon-container">
              <img 
                v-if="row.icon" 
                :src="row.icon" 
                alt="Category icon"
                class="category-icon"
              />
              <span v-else>-</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="Description" min-width="200" show-overflow-tooltip />
        <el-table-column prop="createDate" label="Created At" width="180" />
        <el-table-column label="Actions" width="260" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleAddChild(row)">Add Subcategory</el-button>
            <el-button type="primary" size="small" link @click="handleEdit(row)">Edit</el-button>
            <el-button type="danger" size="small" link @click="handleDelete(row)">Delete</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- Category form dialog -->
    <el-dialog 
      :title="dialogTitle" 
      v-model="dialogVisible" 
      width="600px"
      @close="handleDialogClose"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="Category Name" prop="name">
          <el-input v-model="form.name" placeholder="Enter category name"></el-input>
        </el-form-item>

        <el-form-item label="Parent Category" prop="parentId">
          <el-tree-select
            v-model="form.parentId"
            :data="categoryTree"
            :props="{ value: 'cateId', label: 'name', children: 'children' }"
            check-strictly
            placeholder="Select parent (leave empty for top-level)"
            clearable
          />
        </el-form-item>

        <el-form-item label="Sort Order" prop="sortOrder">
          <el-input-number v-model="form.sortOrder" :min="0" placeholder="Smaller numbers appear first"></el-input-number>
        </el-form-item>

        <el-form-item label="Icon" prop="icon">
          <el-input v-model="form.icon" placeholder="Enter icon path, e.g. /images/category/fresh.png"></el-input>
          <span class="input-tip">Relative path or full URL</span>
        </el-form-item>

        <el-form-item label="Description" prop="description">
          <el-input 
            v-model="form.description" 
            type="textarea" 
            :rows="3"
            placeholder="Enter description"
          ></el-input>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">Cancel</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="loading">
          OK
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import request from '@/utils/request'

const categories = ref([])
const categoryTree = ref([])
const dialogVisible = ref(false)
const loading = ref(false)
const formRef = ref()
const isEdit = ref(false)

const form = reactive({
  cateId: null,
  name: '',
  parentId: 0,
  sortOrder: 0,
  icon: '',
  description: ''
})

const rules = {
  name: [
    { required: true, message: 'Please enter category name', trigger: 'blur' },
    { min: 2, max: 50, message: 'Length must be between 2 and 50 characters', trigger: 'blur' }
  ]
}

const dialogTitle = computed(() => isEdit.value ? 'Edit Category' : 'Add Category')

const fetchCategories = async () => {
  try {
    const { code, data, message } = await request.get('/api/admin/categories')
    if (code === 200) {
      categories.value = buildTree(data || [])
      categoryTree.value = buildCategoryTree(data || [])
    } else {
      ElMessage.error(message || 'Failed to load categories')
    }
  } catch (error) {
    console.error('Failed to load categories:', error)
    ElMessage.error('Failed to load categories')
  }
}

const buildTree = (list) => {
  if (!list?.length) return []
  
  const map = new Map()
  const roots = []
  
  // Build map
  list.forEach(item => {
    map.set(item.cateId, { ...item, children: [] })
  })
  
  // Build tree structure
  list.forEach(item => {
    const node = map.get(item.cateId)
    if (!item.parentId) {
      roots.push(node)
    } else {
      const parent = map.get(item.parentId)
      parent ? parent.children.push(node) : roots.push(node)
    }
  })
  
  return roots
}

const buildCategoryTree = (list) => {
  const tree = buildTree(list)
  return tree.length ? [{ cateId: 0, name: 'Top Level', children: tree }] : []
}

const handleAdd = () => {
  isEdit.value = false
  Object.assign(form, {
    cateId: null,
    name: '',
    parentId: 0,
    sortOrder: 0,
    icon: '',
    description: ''
  })
  dialogVisible.value = true
}

const handleAddChild = (row) => {
  isEdit.value = false
  Object.assign(form, {
    cateId: null,
    name: '',
    parentId: row.cateId,
    sortOrder: 0,
    icon: '',
    description: ''
  })
  dialogVisible.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  Object.assign(form, {
    cateId: row.cateId,
    name: row.name,
    parentId: row.parentId,
    sortOrder: row.sortOrder,
    icon: row.icon || '',
    description: row.description || ''
  })
  dialogVisible.value = true
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('Deleting a category will also delete all its subcategories. Continue?', 'Confirmation', {
      confirmButtonText: 'OK',
      cancelButtonText: 'Cancel',
      type: 'warning'
    })

    const { code, message } = await request.delete(`/api/admin/categories/${row.cateId}`)
    if (code === 200) {
      ElMessage.success('Deleted successfully')
      await fetchCategories()
    } else {
      ElMessage.error(message || 'Delete failed')
    }
  } catch (error) {
    if (error !== 'cancel') {
      console.error('Delete failed:', error)
      ElMessage.error('Delete failed')
    }
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return

  try {
    const valid = await formRef.value.validate()
    if (!valid) return

    loading.value = true
    const data = {
      name: form.name,
      parentId: form.parentId || 0,
      sortOrder: form.sortOrder,
      icon: form.icon,
      description: form.description
    }

    const url = isEdit.value ? `/api/admin/categories/${form.cateId}` : '/api/admin/categories'
    const method = isEdit.value ? 'put' : 'post'
    const { code, message } = await request[method](url, data)

    if (code === 200) {
      ElMessage.success(isEdit.value ? 'Updated successfully' : 'Created successfully')
      dialogVisible.value = false
      await fetchCategories()
    } else {
      ElMessage.error(message || 'Operation failed')
    }
  } catch (error) {
    console.error('Operation failed:', error)
    ElMessage.error('Operation failed')
  } finally {
    loading.value = false
  }
}

const handleDialogClose = () => {
  if (formRef.value) {
    formRef.value.resetFields()
  }
}

onMounted(() => {
  fetchCategories()
})
</script>

<style scoped>
.category-list {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.input-tip {
  display: block;
  margin-top: 5px;
  color: #909399;
  con-container {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 50px;
}

.category-icon {
  width: 40px;
  height: 40px;
  object-fit: cover;
  border-radius: 4px;
  display: block;
}

.ifont-size: 12px;
}
</style>
