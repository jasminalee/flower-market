import { createRouter, createWebHistory } from 'vue-router'
import { storage } from '@/utils/storage'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/home'
    },
    // Redirect to the corresponding dashboard based on role
    {
      path: '/dashboard',
      name: 'Dashboard',
      beforeEnter: (to, from, next) => {
        const userInfo = storage.get('userInfo')
        if (!userInfo) {
          next('/login')
        } else if (userInfo.userType === 'ADMIN') {
          next('/admin/dashboard')
        } else if (userInfo.userType === 'MERCHANT') {
          next('/merchant/dashboard')
        } else {
          next('/home')
        }
      }
    },
    // ============ Customer routes ============
    {
      path: '/home',
      name: 'Home',
      component: () => import('@/views/customer/Home.vue'),
      meta: { title: 'Home' }
    },
    {
      path: '/info',
      redirect: '/profile/info'
    },
    {
      path: '/login',
      name: 'Login',
      component: () => import('@/views/auth/Login.vue'),
      meta: { title: 'Log in' }
    },
    {
      path: '/register',
      name: 'Register',
      component: () => import('@/views/auth/Register.vue'),
      meta: { title: 'Sign up' }
    },
    {
      path: '/products',
      name: 'ProductList',
      component: () => import('@/views/customer/product/ProductList.vue'),
      meta: { title: 'Products' }
    },
    {
      path: '/products/:id',
      name: 'ProductDetail',
      component: () => import('@/views/customer/product/ProductDetail.vue'),
      meta: { title: 'Product Details' }
    },
    {
      path: '/cart',
      name: 'Cart',
      component: () => import('@/views/customer/cart/Cart.vue'),
      meta: { title: 'Cart', requiresAuth: true }
    },
    {
      path: '/checkout',
      name: 'Checkout',
      component: () => import('@/views/customer/order/Checkout.vue'),
      meta: { title: 'Checkout', requiresAuth: true }
    },
    {
      path: '/profile',
      component: () => import('@/views/customer/profile/Layout.vue'),
      meta: { requiresAuth: true },
      children: [
        {
          path: '',
          redirect: 'info'
        },
        {
          path: 'info',
          name: 'ProfileInfo',
          component: () => import('@/views/customer/profile/ProfileInfo.vue'),
          meta: { title: 'Profile' }
        },
        {
          path: 'orders',
          name: 'MyOrders',
          component: () => import('@/views/customer/profile/MyOrders.vue'),
          meta: { title: 'My Orders' }
        },
        {
          path: 'orders/:id',
          name: 'OrderDetail',
          component: () => import('@/views/customer/profile/OrderDetail.vue'),
          meta: { title: 'Order Details' }
        },
        {
          path: 'orders/:id/review',
          name: 'PublishReview',
          component: () => import('@/views/customer/profile/PublishReview.vue'),
          meta: { title: 'Review Order' }
        },
        {
          path: 'favorites',
          name: 'MyFavorites',
          component: () => import('@/views/customer/profile/MyFavorites.vue'),
          meta: { title: 'My Favorites' }
        },
        {
          path: 'addresses',
          name: 'MyAddresses',
          component: () => import('@/views/customer/profile/MyAddresses.vue'),
          meta: { title: 'Addresses' }
        },
        {
          path: 'coupons',
          name: 'MyCoupons',
          component: () => import('@/views/customer/profile/MyCoupons.vue'),
          meta: { title: 'My Coupons' }
        },
        {
          path: 'checkin',
          name: 'Checkin',
          component: () => import('@/views/customer/profile/Checkin.vue'),
          meta: { title: 'Check-in' }
        },
        {
          path: 'balance',
          name: 'Balance',
          component: () => import('@/views/customer/profile/Balance.vue'),
          meta: { title: 'Balance' }
        }
      ]
    },
    {
      path: '/coupons',
      name: 'CouponCenter',
      component: () => import('@/views/customer/coupon/CouponCenter.vue'),
      meta: { title: 'Coupon Center' }
    },
    {
      path: '/knowledge',
      name: 'KnowledgeList',
      component: () => import('@/views/customer/knowledge/KnowledgeList.vue'),
      meta: { title: 'Care Tips' }
    },
    {
      path: '/knowledge/:id',
      name: 'KnowledgeDetail',
      component: () => import('@/views/customer/knowledge/KnowledgeDetail.vue'),
      meta: { title: 'Article' }
    },

    // ============ Merchant routes ============
    {
      path: '/merchant',
      component: () => import('@/views/merchant/Layout.vue'),
      meta: { requiresAuth: true, requiresMerchant: true },
      children: [
        {
          path: '',
          redirect: 'dashboard'
        },
        {
          path: 'dashboard',
          name: 'MerchantDashboard',
          component: () => import('@/views/merchant/Dashboard.vue'),
          meta: { title: 'Merchant Dashboard' }
        },
        {
          path: 'products',
          name: 'MerchantProducts',
          component: () => import('@/views/merchant/product/ProductList.vue'),
          meta: { title: 'Product Management' }
        },
        {
          path: 'products/add',
          name: 'CreateProduct',
          component: () => import('@/views/merchant/product/ProductForm.vue'),
          meta: { title: 'Create Product' }
        },
        {
          path: 'products/edit/:id',
          name: 'EditProduct',
          component: () => import('@/views/merchant/product/ProductForm.vue'),
          meta: { title: 'Edit Product' }
        },
        {
          path: 'products/:id/trackability',
          name: 'ProductTrackability',
          component: () => import('@/views/merchant/product/Trackability.vue'),
          meta: { title: 'Traceability' }
        },
        {
          path: 'orders',
          name: 'MerchantOrders',
          component: () => import('@/views/merchant/order/OrderList.vue'),
          meta: { title: 'Order Management' }
        },
        {
          path: 'orders/:id',
          name: 'MerchantOrderDetail',
          component: () => import('@/views/merchant/order/OrderDetail.vue'),
          meta: { title: 'Order Details' }
        },
        {
          path: 'coupons',
          name: 'MerchantCoupons',
          component: () => import('@/views/merchant/coupon/CouponList.vue'),
          meta: { title: 'Coupon Management' }
        },
        {
          path: 'coupons/add',
          name: 'CreateCoupon',
          component: () => import('@/views/merchant/coupon/CouponForm.vue'),
          meta: { title: 'Create Coupon' }
        },
        {
          path: 'coupons/edit/:id',
          name: 'EditCoupon',
          component: () => import('@/views/merchant/coupon/CouponForm.vue'),
          meta: { title: 'Edit Coupon' }
        },
        {
          path: 'profile',
          name: 'MerchantProfile',
          component: () => import('@/views/merchant/Profile.vue'),
          meta: { title: 'Merchant Profile' }
        }
      ]
    },

    // ============ Admin routes ============
    {
      path: '/admin',
      component: () => import('@/views/admin/Layout.vue'),
      meta: { requiresAuth: true, requiresAdmin: true },
      children: [
        {
          path: '',
          redirect: 'dashboard'
        },
        {
          path: 'dashboard',
          name: 'AdminDashboard',
          component: () => import('@/views/admin/Dashboard.vue'),
          meta: { title: 'Admin Dashboard' }
        },
        {
          path: 'customers',
          name: 'CustomerManagement',
          component: () => import('@/views/admin/customer/CustomerList.vue'),
          meta: { title: 'Customer Management' }
        },
        {
          path: 'merchants',
          name: 'MerchantManagement',
          component: () => import('@/views/admin/merchant/MerchantList.vue'),
          meta: { title: 'Merchant Management' }
        },
        {
          path: 'merchants/:id',
          name: 'MerchantDetail',
          component: () => import('@/views/admin/merchant/MerchantDetail.vue'),
          meta: { title: 'Merchant Details' }
        },
        {
          path: 'suppliers',
          name: 'SupplierManagement',
          component: () => import('@/views/admin/supplier/Index.vue'),
          meta: { title: 'Supplier' }
        },
        {
          path: 'reviews',
          name: 'ReviewManagement',
          component: () => import('@/views/admin/review/ReviewList.vue'),
          meta: { title: 'Review Management' }
        },
        {
          path: 'orders',
          name: 'AdminOrders',
          component: () => import('@/views/admin/order/OrderList.vue'),
          meta: { title: 'Order Management' }
        },
        {
          path: 'categories',
          name: 'CategoryManagement',
          component: () => import('@/views/admin/category/CategoryList.vue'),
          meta: { title: 'Category Management' }
        },
        {
          path: 'knowledge',
          name: 'KnowledgeManagement',
          component: () => import('@/views/admin/knowledge/KnowledgeList.vue'),
          meta: { title: 'Care Tips' }
        },
        {
          path: 'knowledge/add',
          name: 'CreateKnowledge',
          component: () => import('@/views/admin/knowledge/KnowledgeForm.vue'),
          meta: { title: 'Create Article' }
        },
        {
          path: 'knowledge/edit/:id',
          name: 'EditKnowledge',
          component: () => import('@/views/admin/knowledge/KnowledgeForm.vue'),
          meta: { title: 'Edit Article' }
        },
        {
          path: 'config',
          name: 'SystemConfig',
          component: () => import('@/views/admin/config/ConfigList.vue'),
          meta: { title: 'System Config' }
        },
        {
          path: 'profile',
          name: 'AdminProfile',
          component: () => import('@/views/admin/Profile.vue'),
          meta: { title: 'Profile' }
        }
      ]
    },

    // 404 page
    {
      path: '/:pathMatch(.*)*',
      name: 'NotFound',
      component: () => import('@/views/error/NotFound.vue'),
      meta: { title: 'Page Not Found' }
    }
  ],
  scrollBehavior(to, from, savedPosition) {
    if (savedPosition) {
      return savedPosition
    } else {
      return { top: 0 }
    }
  }
})

// Route guard
router.beforeEach((to, from, next) => {
  // Print route navigation logs
  console.log('='.repeat(60))
  console.log('🚀 Navigation:')
  console.log('  From:', from.path, from.name ? `(${from.name})` : '')
  console.log('  To:  ', to.path, to.name ? `(${to.name})` : '')
  console.log('  Title:', to.meta?.title || 'Not set')
  if (Object.keys(to.params).length > 0) {
    console.log('  Params:', to.params)
  }
  if (Object.keys(to.query).length > 0) {
    console.log('  Query:', to.query)
  }
  console.log('='.repeat(60))
  
  // Set document title
  document.title = to.meta.title ? `${to.meta.title} - Flower Market` : 'Flower Market'
  
  // Read user info
  const userInfo = storage.get('userInfo')
  const token = storage.get('token')
  
  // Routes requiring authentication
  if (to.meta.requiresAuth) {
    if (!token || !userInfo) {
      console.log('⚠️  Authentication required. Redirecting to login...')
      next({
        path: '/login',
        query: { redirect: to.fullPath }
      })
      return
    }
  }
  
  // Routes requiring merchant role
  if (to.meta.requiresMerchant) {
    if (!userInfo || userInfo.userType !== 'MERCHANT') {
      console.log('❌ Permission denied: merchant role required')
      next('/')
      return
    }
  }
  
  // Routes requiring admin role
  if (to.meta.requiresAdmin) {
    if (!userInfo || userInfo.userType !== 'ADMIN') {
      console.log('❌ Permission denied: admin role required')
      next('/')
      return
    }
  }
  
  console.log('✅ Navigation allowed')
  next()
})

export default router
