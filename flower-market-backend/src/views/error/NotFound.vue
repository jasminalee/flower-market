<template>
  <div class="error-page">
    <el-result
      icon="error"
      title="404"
      sub-title="Sorry, the page you are looking for does not exist"
    >
      <template #extra>
        <el-button type="primary" @click="$router.push('/')">
          Back to Home
        </el-button>
      </template>
    </el-result>
  </div>
</template>

<style scoped>
.error-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--color-bg-base);
}
</style>
