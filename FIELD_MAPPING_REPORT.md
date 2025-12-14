# 前后端字段映射修复报告

## 修复日期：2025-12-14

## 已完成修复

### 1. Dashboard.vue ✅
- `createTime` → `createDate`  
- 用户表字段：`name`, `email`, `phone`, `createDate`
- 商家表字段：`name`, `phone`, `createDate`
- 商家状态：`ACTIVE`, `SUSPENDED`, `PENDING`, `REJECTED`

### 2. MerchantDetail.vue ✅
- `shopLogo` (后端实际字段)
- `name` (店铺名)
- `phone` (联系电话)
- `createDate`, `updateDate`
- `qualification` (商家资质)
- 状态：`ACTIVE`, `SUSPENDED`, `PENDING`, `REJECTED`

### 3. MyOrders.vue ✅
- 订单主键：`id` (不是`orderId`)
- 订单时间：`orderDate` (下单时间)
- 订单金额：`actualPrice` (实付金额)
- 订单项主键：`id` (不是`orderItemId`)
- 订单项字段：`name`, `mainImage`, `unitPrice`, `totalPrice`

### 4. OrderDetail.vue ✅
- 订单时间字段：`orderDate`, `paymentTime`, `deliveryTime`, `completionTime`
- 收货信息：`receiverName`, `receiverPhone`, `address`
- 订单金额：`totalPrice`, `discountAmount`, `actualPrice`
- 订单项字段：`name`, `mainImage`, `unitPrice`, `totalPrice`

### 5. ProductList.vue ✅
- 分类筛选：`catId` (不是`categoryId`)
- API参数：`catId` 传递给后端

### 6. ProductDetail.vue ✅
- 评价主键：`id` (不是`reviewId`)
- 评价时间：`createDate` (不是`createTime`)
- 用户名字段：`userName` (不是`customerName`，需要后端返回)

### 7. CouponCenter.vue ✅
- 优惠券金额：`value` (不是`discountAmount`)
- 最低消费：`minPrice` (不是`minOrderAmount`)
- 有效期：`startDate`, `endDate` (不是`startTime`, `endTime`)
- 剩余数量：计算 `totalQuantity - receivedQuantity`

### 8. MyCoupons.vue ✅
- 优惠券金额：`value`
- 最低消费：`minPrice`
- 有效期：`startDate`, `endDate`
- 使用时间：`usedDate` (不是`usedTime`)

### 9. Cart.vue + cart.js ✅
- 购物车主键：`id` (不是`cartItemId`)
- 商品ID：`prodId`
- 商品详情：通过`prodId`关联查询，存储在`product`字段
- 商品信息：`product.name`, `product.mainImage`, `product.price`, `product.stock`
- 数量字段：`quantity`

### 10. MyAddresses.vue ✅
- 地址字段：`address` (直接来自 customers 表)
- 无需修改，已与后端一致

### 11. KnowledgeList.vue + KnowledgeDetail.vue ✅
- 知识主键：`id` (不是`knowledgeId`)
- 时间字段：`createDate` (不是`createTime`)
- 封面图：`coverImage`
- 摘要处理：数据库无`summary`字段，前端从`content`截取前100字符
- 关键词：`keywords` (替代原`summary`显示)

### 12. Checkin.vue ✅
- 签到主键：`id`
- 签到日期：`checkDate` (不是`checkinDate`)
- 连续天数：`continuousDays` (不是`consecutiveDays`)
- 奖励积分：`rewardPoints`
- 签到时间：`createTime` (不是`checkinTime`)

### 13. MyFavorites.vue ✅
- 收藏主键：`id`
- 商品ID：`prodId`
- 收藏时间：`favDate` (数据库字段，前端未使用)
- 商品信息通过prodId关联
- 添加到购物车方法已更新为使用正确的cartStore.addToCart

## 需要继续修复的文件

### 5. 产品相关 (ProductList.vue, ProductDetail.vue)
**数据库字段** (products表):
- 主键：`prodId`
- 字段：`name`, `price`, `stock`, `sales`, `mainImage`, `images`, `description`
- 分类：`catId` (外键指向product_categories.cate_id)
- 商家：`merchId`
- 状态：`status` (ACTIVE/INACTIVE/DELETED)
- 库存状态：`stockStatus` (IN_STOCK/LOW_STOCK/OUT_OF_STOCK)
- 时间：`createDate`, `updateDate`

**需要检查的前端字段**:
- ❌ `categoryId` → 应为 `catId`
- ❌ `prodImage` → 应为 `mainImage`
- ❌ `prodName` → 应为 `name`
- ❌ `prodPrice` → 应为 `price`

### 6. 产品分类 (ProductCategoryList.vue)
**数据库字段** (product_categories表):
- 主键：`cateId`
- 字段：`name`, `parentId`, `sortOrder`, `icon`, `description`
- 时间：`createDate`, `updateDate`

### 7. 优惠券相关 (CouponCenter.vue, MyCoupons.vue)
**数据库字段** (coupons表):
- 主键：`couponId`
- 字段：`name`, `type`, `value`, `minPrice`
- 数量：`totalQuantity`, `receivedQuantity`
- 时间：`startDate`, `endDate`, `createDate`, `updateDate`
- 状态：`status` (ACTIVE/INACTIVE/EXPIRED)

**需要修复**:
- ❌ `discountAmount` → `value`
- ❌ `minOrderAmount` → `minPrice`
- ❌ `startTime`/`endTime` → `startDate`/`endDate`
- ❌ `remainingQuantity` → 需计算：`totalQuantity - receivedQuantity`

### 8. 用户优惠券 (customer_coupons表)
- 主键：`id`
- 字段：`userId`, `couponId`, `code`, `status`
- 时间：`receiveDate`, `usedDate`
- 订单：`orderId`

### 9. 产品评价 (ProductReview)
**数据库字段** (product_reviews表):
- 主键：`id` (不是`reviewId`)
- 字段：`userId`, `prodId`, `orderId`, `rating`, `content`, `images`
- 验证：`verified` (0/1)
- 状态：`status` (PENDING/APPROVED/REJECTED)
- 时间：`createDate`

**需要修复**:
- ❌ `reviewId` → `id`
- ❌ `createTime` → `createDate`
- ❌ `customerName` → 需要JOIN customer表获取，后端应返回关联数据

### 10. 养护知识 (CareKnowledge)
**数据库字段** (care_knowledge表):
- 主键：`id` (不是`knowledgeId`)
- 字段：`title`, `content`, `keywords`, `coverImage`, `category`, `author`, `viewCount`
- 状态：`status`
- 时间：`createDate`, `updateDate`

**需要修复**:
- ❌ `knowledgeId` → `id`
- ❌ `createTime` → `createDate`
- ❌ `summary` → 不存在此字段，需要从content截取或后端添加

### 12. 签到 (Checkin.vue)
**数据库字段** (check_ins表):
- 主键：`id`
- 字段：`userId`, `checkDate`, `continuousDays`, `rewardPoints`

## 全局时间字段统一规则

**后端数据库统一使用**：
- `create_date` → Java实体：`createDate`
- `update_date` → Java实体：`updateDate`
- 特殊时间字段按表而定（如orders表的`orderDate`, `paymentTime`等）

**前端应统一使用**：
- `createDate`
- `updateDate`
- 避免使用：`createdTime`, `updatedTime`, `createTime`, `updateTime`

## 修复优先级

### 高优先级（影响核心功能）✅ 已完成
1. ✅ 订单相关字段（MyOrders.vue, OrderDetail.vue）
2. ✅ 产品相关字段（ProductList.vue, ProductDetail.vue）
3. ✅ 优惠券字段（CouponCenter.vue, MyCoupons.vue）
4. ✅ 购物车字段（Cart.vue, cart.js）- 已通过关联查询解决
5. ✅ 地址管理（MyAddresses.vue）- 已验证，与后端一致
6. ✅ 养护知识（KnowledgeList.vue, KnowledgeDetail.vue）

### 中优先级（影响用户体验）
7. ⚠️ 产品评价字段（ProductReview相关）- 需后端返回用户名
8. ⚠️ 产品分类字段

### 低优先级（辅助功能）✅ 已完成
9. ✅ 签到字段（Checkin.vue）- checkDate, continuousDays, createTime
10. ✅ 收藏字段（MyFavorites.vue）- 已验证，字段一致

## 后续建议

1. **后端改进**：
   - 对于需要关联数据的场景（如评价中的用户名），后端应返回VO对象包含关联数据
   - 对于需要计算的字段（如优惠券剩余数量），后端应在响应中包含计算结果

2. **前端改进**：
   - 建立统一的字段映射工具类
   - 使用TypeScript定义接口类型，确保类型安全
   - 在API层做统一的字段转换处理

3. **文档维护**：
   - 保持前后端接口文档同步
   - 更新Swagger文档确保字段名称正确
   - 建立字段命名规范文档

## 测试建议

每次修复后应测试：
1. 列表页面加载和显示
2. 详情页面数据展示
3. 表单提交和数据保存
4. 分页和筛选功能
5. 关联数据正确加载
