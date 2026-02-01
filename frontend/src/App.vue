<script setup>
import { onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { useProductStore } from '@/stores/product'
import { useCartStore } from '@/stores/cart'

const userStore = useUserStore()
const productStore = useProductStore()
const cartStore = useCartStore()

onMounted(() => {
  // Load recent views and search history
  productStore.loadRecentViewed()
  productStore.loadSearchHistory()
  
  // Load cart when user is already logged in
  if (userStore.isLoggedIn && userStore.userId) {
    cartStore.loadCart().catch(err => {
      console.error('Failed to load cart:', err)
    })
  }
})
</script>

<template>
  <router-view />
</template>

<style>
#app {
  width: 100%;
  min-height: 100vh;
}
</style>