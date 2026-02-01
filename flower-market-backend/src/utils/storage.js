/**
 * LocalStorage wrapper
 */

export const storage = {
  // Get
  get(key) {
    const value = localStorage.getItem(key)
    if (value === null) return null
    
    try {
      return JSON.parse(value)
    } catch {
      return value
    }
  },
  
  // Set
  set(key, value) {
    if (typeof value === 'object') {
      value = JSON.stringify(value)
    }
    localStorage.setItem(key, value)
  },
  
  // Remove
  remove(key) {
    localStorage.removeItem(key)
  },
  
  // Clear
  clear() {
    localStorage.clear()
  }
}

export const sessionStorage = {
  // Get
  get(key) {
    const value = window.sessionStorage.getItem(key)
    if (value === null) return null
    
    try {
      return JSON.parse(value)
    } catch {
      return value
    }
  },
  
  // Set
  set(key, value) {
    if (typeof value === 'object') {
      value = JSON.stringify(value)
    }
    window.sessionStorage.setItem(key, value)
  },
  
  // Remove
  remove(key) {
    window.sessionStorage.removeItem(key)
  },
  
  // Clear
  clear() {
    window.sessionStorage.clear()
  }
}
