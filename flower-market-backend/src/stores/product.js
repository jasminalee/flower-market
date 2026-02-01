import { defineStore } from 'pinia'

export const useProductStore = defineStore('product', {
  state: () => ({
    categories: [],
    recentViewed: [], // recently viewed products
    searchHistory: [] // search history
  }),
  
  getters: {
    // Get category tree
    categoryTree: (state) => {
      // Build category tree if needed
      return state.categories
    }
  },
  
  actions: {
    // Set categories
    setCategories(categories) {
      this.categories = categories
    },
    
    // Add recently viewed
    addRecentViewed(product) {
      const index = this.recentViewed.findIndex(p => p.prodId === product.prodId)
      if (index > -1) {
        this.recentViewed.splice(index, 1)
      }
      this.recentViewed.unshift(product)
      
      // Keep up to 20
      if (this.recentViewed.length > 20) {
        this.recentViewed.pop()
      }
      
      // Persist
      localStorage.setItem('recentViewed', JSON.stringify(this.recentViewed))
    },
    
    // Load recently viewed
    loadRecentViewed() {
      const data = localStorage.getItem('recentViewed')
      if (data) {
        this.recentViewed = JSON.parse(data)
      }
    },
    
    // Add search history
    addSearchHistory(keyword) {
      if (!keyword) return
      
      const index = this.searchHistory.indexOf(keyword)
      if (index > -1) {
        this.searchHistory.splice(index, 1)
      }
      this.searchHistory.unshift(keyword)
      
      // Keep up to 10
      if (this.searchHistory.length > 10) {
        this.searchHistory.pop()
      }
      
      // Persist
      localStorage.setItem('searchHistory', JSON.stringify(this.searchHistory))
    },
    
    // Load search history
    loadSearchHistory() {
      const data = localStorage.getItem('searchHistory')
      if (data) {
        this.searchHistory = JSON.parse(data)
      }
    },
    
    // Clear search history
    clearSearchHistory() {
      this.searchHistory = []
      localStorage.removeItem('searchHistory')
    }
  }
})
