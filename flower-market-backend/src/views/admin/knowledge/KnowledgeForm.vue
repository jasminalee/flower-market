<template>
  <div class="knowledge-form">
    <el-page-header @back="$router.back()" :content="isEditMode ? 'Edit Article' : 'Add Article'" />

    <el-card shadow="never" class="form-card">
      <el-form :model="formData" :rules="rules" ref="formRef" label-width="100px" v-loading="loading">
        <el-form-item label="Title" prop="title">
          <el-input v-model="formData.title" placeholder="Please enter the article title" maxlength="100" show-word-limit />
        </el-form-item>

        <el-form-item label="Category" prop="category">
          <el-select v-model="formData.category" placeholder="Please select a category" style="width: 200px">
            <el-option label="Care Tips" value="care" />
            <el-option label="Flower Knowledge" value="knowledge" />
            <el-option label="Styling Guide" value="guide" />
            <el-option label="Festival Arrangements" value="festival" />
          </el-select>
        </el-form-item>

        <el-form-item label="Cover Image" prop="coverImage">
          <el-input v-model="formData.coverImage" placeholder="Please enter cover image URL" />
          <el-image v-if="formData.coverImage" :src="formData.coverImage" fit="cover" style="width: 200px; height: 150px; margin-top: 10px; border-radius: 4px;" />
        </el-form-item>

        <el-form-item label="Summary" prop="summary">
          <el-input v-model="formData.summary" type="textarea" :rows="3" placeholder="Please enter a short summary" maxlength="200" show-word-limit />
        </el-form-item>

        <el-form-item label="Content" prop="content">
          <el-input v-model="formData.content" type="textarea" :rows="15" placeholder="Please enter the article content (Markdown supported)" />
        </el-form-item>

        <el-form-item label="Tags">
          <el-tag
            v-for="tag in formData.tags"
            :key="tag"
            closable
            @close="handleRemoveTag(tag)"
            style="margin-right: 10px;"
          >
            {{ tag }}
          </el-tag>
          <el-input
            v-if="tagInputVisible"
            ref="tagInputRef"
            v-model="tagInputValue"
            size="small"
            style="width: 100px;"
            @keyup.enter="handleAddTag"
            @blur="handleAddTag"
          />
          <el-button v-else size="small" @click="showTagInput">+ Add Tag</el-button>
        </el-form-item>

        <el-form-item label="Author">
          <el-input v-model="formData.author" placeholder="Please enter author name" style="width: 200px" />
        </el-form-item>

        <el-form-item label="Publish Status">
          <el-radio-group v-model="formData.status">
            <el-radio label="PUBLISHED">Publish Now</el-radio>
            <el-radio label="DRAFT">Save Draft</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSubmit" :loading="submitting">Save</el-button>
          <el-button @click="$router.back()">Cancel</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getKnowledgeDetail, createKnowledge, updateKnowledge } from '@/api/knowledge'

const route = useRoute()
const router = useRouter()
const formRef = ref(null)
const tagInputRef = ref(null)
const loading = ref(false)
const submitting = ref(false)
const tagInputVisible = ref(false)
const tagInputValue = ref('')

const isEditMode = ref(false)

const formData = reactive({
  title: '',
  category: '',
  coverImage: '',
  summary: '',
  content: '',
  tags: [],
  author: 'Admin',
  status: 'PUBLISHED'
})

const rules = {
  title: [{ required: true, message: 'Please enter the article title', trigger: 'blur' }],
  category: [{ required: true, message: 'Please select a category', trigger: 'change' }],
  coverImage: [{ required: true, message: 'Please enter a cover image', trigger: 'blur' }],
  summary: [{ required: true, message: 'Please enter a summary', trigger: 'blur' }],
  content: [{ required: true, message: 'Please enter the article content', trigger: 'blur' }]
}

// Get article details (edit mode)
const fetchKnowledgeDetail = async () => {
  loading.value = true
  try {
    const { data } = await getKnowledgeDetail(route.params.id)
    Object.assign(formData, data)
    formData.tags = data.tags ? data.tags.split(',') : []
  } catch (error) {
    ElMessage.error('Failed to load article details')
  } finally {
    loading.value = false
  }
}

// Submit form
const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    submitting.value = true

    const submitData = {
      ...formData,
      tags: formData.tags.join(',')
    }

    if (isEditMode.value) {
      await updateKnowledge(route.params.id, submitData)
      ElMessage.success('Update successful')
    } else {
      await createKnowledge(submitData)
      ElMessage.success('Create successful')
    }

    router.back()
  } catch (error) {
    if (error !== false) {
      ElMessage.error('Save failed')
    }
  } finally {
    submitting.value = false
  }
}

// Show tag input
const showTagInput = () => {
  tagInputVisible.value = true
  nextTick(() => {
    tagInputRef.value.focus()
  })
}

// Add tag
const handleAddTag = () => {
  if (tagInputValue.value) {
    if (!formData.tags.includes(tagInputValue.value)) {
      formData.tags.push(tagInputValue.value)
    }
    tagInputValue.value = ''
  }
  tagInputVisible.value = false
}

// Remove tag
const handleRemoveTag = (tag) => {
  formData.tags = formData.tags.filter(t => t !== tag)
}

onMounted(() => {
  isEditMode.value = route.path.includes('/edit/')
  if (isEditMode.value && route.params.id) {
    fetchKnowledgeDetail()
  }
})
</script>

<style scoped>
.knowledge-form {
  padding: 20px;
}

.form-card {
  margin-top: 20px;
  max-width: 900px;
}
</style>
