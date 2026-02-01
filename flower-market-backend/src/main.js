import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import enLocale from 'element-plus/dist/locale/en.mjs'

// Global styles
import '@/assets/styles/reset.css'
import '@/assets/styles/variables.css'
import '@/assets/styles/common.css'

import App from './App.vue'
import router from './router'
import pinia from './stores'

const app = createApp(App)

// Register Element Plus icons
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.use(pinia)
app.use(router)
app.use(ElementPlus, { locale: enLocale })

app.mount('#app')
