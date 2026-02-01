import { defineStore } from 'pinia'
import * as cartApi from '@/api/cart'
import * as productApi from '@/api/product'
import { useUserStore } from './user'

export const useCartStore = defineStore('cart', {
  state: () => ({
    cartItems: [],
    selectedItems: [], // selected product IDs
    loading: false
  }),
  
  getters: {
    // Cart item count
    cartCount: (state) => state.cartItems.length,
    
    // Selected item count
    selectedCount: (state) => state.selectedItems.length,
    
    // Total price of selected items
    selectedTotalPrice: (state) => {
      return state.cartItems
        .filter(item => state.selectedItems.includes(item.id))
        .reduce((total, item) => total + (item.prodPrice || 0) * item.quantity, 0)
    },
    
    // Whether all selected
    isAllSelected: (state) => {
      if (state.cartItems.length === 0) return false
      return state.cartItems.length === state.selectedItems.length
    }
  },
  
  actions: {
    // Load cart
    async loadCart() {
      const userStore = useUserStore()
      if (!userStore.userId) {
        this.cartItems = []
        return
      }
      
      this.loading = true
      try {
        const res = await cartApi.getCartList(userStore.userId)
        this.cartItems = res.data || []
      } catch (error) {
        console.error('Load cart error:', error)
        this.cartItems = []
        // Swallow the error to avoid UI noise
      } finally {
        this.loading = false
      }
    },
    
    // Add to cart
    async addToCart(item) {
      const userStore = useUserStore()
      if (!userStore.userId) {
        throw new Error('Please login first')
      }
      
      const res = await cartApi.addToCart({
        userId: userStore.userId,
        prodId: item.prodId || item.id,
        quantity: item.quantity || 1
      })
      
      // Reload cart after adding
      await this.loadCart()
      return res
    },
    
    // Update quantity
    async updateQuantity(id, quantity) {
      await cartApi.updateCartItem(id, quantity)
      await this.loadCart()
    },
    
    // Remove item
    async removeItem(id) {
      await cartApi.removeCartItem(id)
      await this.loadCart()
      
      // Remove from selected list
      this.selectedItems = this.selectedItems.filter(itemId => itemId !== id)
    },
    
    // Batch remove
    async batchRemove(ids) {
      await cartApi.batchRemoveCartItems(ids)
      await this.loadCart()
      
      // Remove from selected list
      this.selectedItems = this.selectedItems.filter(id => !ids.includes(id))
    },
    
    // Clear cart
    async clearCart() {
      const userStore = useUserStore()
      await cartApi.clearCart(userStore.userId)
      this.cartItems = []
      this.selectedItems = []
    },
    
    // Toggle select one
    toggleSelect(id) {
      const index = this.selectedItems.indexOf(id)
      if (index > -1) {
        this.selectedItems.splice(index, 1)
      } else {
        this.selectedItems.push(id)
      }
    },
    
    // Toggle select all
    toggleSelectAll() {
      if (this.isAllSelected) {
        this.selectedItems = []
      } else {
        this.selectedItems = this.cartItems.map(item => item.id)
      }
    },
    
    // Get selected items
    getSelectedItems() {
      return this.cartItems.filter(item => 
        this.selectedItems.includes(item.id)
      )
    }
  }
})
