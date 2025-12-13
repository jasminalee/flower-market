# Actor Descriptions 分析报告

**文档版本：** v1.0  
**创建日期：** 2025-12-13  
**分析范围：** 基于ER图和DDL(schema.sql)的Actor描述合理性分析

---

## 目录

1. [Customer（顾客）角色分析](#1-customer顾客角色分析)
2. [Merchant（商家）角色分析](#2-merchant商家角色分析)
3. [Administrator（管理员）角色分析](#3-administrator管理员角色分析)
4. [System (Automatic)（系统自动化）分析](#4-system-automatic系统自动化分析)
5. [DDL不支持的功能清单](#5-ddl不支持的功能清单)
6. [修正后的完整Actor Descriptions](#6-修正后的完整actor-descriptions)
7. [总结](#7-总结)

---

## 1. Customer（顾客）角色分析

### 1.1 原始描述

**Actor name:** Customer  
**Description:** Core users who browse products, search, place orders, manage shopping carts, check order status, apply for after-sales services, track logistics, and use membership benefits on the flower e-commerce platform.

### 1.2 合理性分析

#### ✅ 已包含的功能

- 浏览产品 (browse products)
- 搜索 (search)
- 下单 (place orders)
- 管理购物车 (manage shopping carts)
- 查看订单状态 (check order status)
- 会员福利 (membership benefits)

#### ⚠️ 未明确但DDL支持的功能

- 产品收藏 (Product Favorites)
- 产品评价 (Product Reviews)
- 签到功能 (Check-ins)
- 地址管理 (Address management)
- 账户余额管理 (Balance management)
- 查看养护知识 (Care Knowledge)
- 产品溯源查询 (Product Trackability)

#### ❌ DDL不完全支持的功能

**售后服务 (After-sales services)** - ⚠️ 部分支持不足

- 订单表有status字段支持订单状态管理
- 订单表有payment_status字段包含REFUNDED状态
- **缺失：** 没有专门的售后服务表（退换货、售后申请、售后进度跟踪）

**物流跟踪 (Track logistics)** - ❌ 不支持

- 订单表只有delivery_time字段记录发货时间
- **缺失：** 没有物流信息表（物流公司、运单号、物流状态、物流轨迹）

### 1.3 DDL支持情况总结

| 功能 | DDL支持 | 相关表/字段 |
|------|---------|-------------|
| 浏览、搜索产品 | ✅ 完全支持 | products表 |
| 下单 | ✅ 完全支持 | orders, order_items表 |
| 购物车管理 | ✅ 完全支持 | shopping_cart表 |
| 订单状态查询 | ✅ 完全支持 | orders.status, orders.payment_status |
| 会员福利 | ✅ 完全支持 | customers.level, check_ins, customer_coupons |
| 产品收藏 | ✅ 完全支持 | product_favorites表 |
| 产品评价 | ✅ 完全支持 | product_reviews表 |
| 签到 | ✅ 完全支持 | check_ins表 |
| 售后服务 | ⚠️ 部分支持 | orders表有退款状态，但无售后流程表 |
| 物流跟踪 | ❌ 不支持 | 缺少物流信息表 |

### 1.4 修正后的Actor Description

**Actor name:** Customer  
**Description (修正版):** Core users who browse products, search, place orders, manage shopping carts and favorites, check order status, make payments, apply for refunds, write product reviews, participate in daily check-ins, use coupons and membership benefits, manage delivery addresses and account balance, and view care knowledge on the flower e-commerce platform.

### 1.5 修正差异说明

- ✅ **新增：** "manage favorites" - 明确产品收藏功能
- ✅ **新增：** "make payments" - 明确支付功能
- ✅ **新增：** "write product reviews" - 明确评价功能
- ✅ **新增：** "participate in daily check-ins" - 明确签到功能
- ✅ **新增：** "use coupons" - 明确优惠券使用
- ✅ **新增：** "manage delivery addresses and account balance" - 明确地址和余额管理
- ✅ **新增：** "view care knowledge" - 明确养护知识查看
- ⚠️ **修改：** "apply for after-sales services" → "apply for refunds" - 根据DDL实际支持改为退款（因为没有完整售后表）
- ❌ **删除：** "track logistics" - DDL不支持物流跟踪功能

---

## 2. Merchant（商家）角色分析

### 2.1 原始描述

**Actor name:** Merchant  
**Description:** Operators and managers of flower shops, responsible for comprehensive store operations including product information maintenance, order processing, inventory management, marketing campaign setup, membership management, and customer service.

### 2.2 合理性分析

#### ✅ 已包含的功能

- 产品信息维护 (product information maintenance)
- 订单处理 (order processing)
- 库存管理 (inventory management)

#### ⚠️ 需要调整的功能

**营销活动设置 (marketing campaign setup)** - ⚠️ 仅部分支持

- DDL支持：优惠券创建和管理（coupons表，包含merch_id字段）
- 但描述过于宽泛："marketing campaign"可能包含促销活动、限时折扣等，DDL只支持优惠券

**会员管理 (membership management)** - ❌ 不支持

- 会员等级在customers表中管理，商家没有权限管理客户会员
- 这是平台/管理员的功能，不是商家功能

**客户服务 (customer service)** - ❌ DDL不支持

- 没有客服对话表、工单表、消息表等
- 评价回复功能也没有在product_reviews表中体现

#### ✅ 未明确但DDL支持的功能

- 店铺信息管理（merchants表：shop_logo, description, address等）
- 商家认证/审核（merchants.status: PENDING/ACTIVE/SUSPENDED/REJECTED）
- 产品上下架管理（products.status）
- 产品溯源信息维护（product_trackability表）
- 发货管理（orders.delivery_time）
- 查看销售统计（v_merchant_sales_statistics视图）

### 2.3 DDL支持情况总结

| 功能 | DDL支持 | 相关表/字段 |
|------|---------|-------------|
| 产品信息维护 | ✅ 完全支持 | products表（CRUD、上下架） |
| 订单处理 | ✅ 完全支持 | orders表（状态管理、发货） |
| 库存管理 | ✅ 完全支持 | products.stock, products.stock_status |
| 店铺信息管理 | ✅ 完全支持 | merchants表 |
| 商家认证 | ✅ 完全支持 | merchants.status |
| 产品溯源维护 | ✅ 完全支持 | product_trackability表 |
| 优惠券管理 | ✅ 完全支持 | coupons表（merch_id关联） |
| 销售统计查看 | ✅ 完全支持 | v_merchant_sales_statistics视图 |
| 会员管理 | ❌ 不支持 | customers.level由平台管理 |
| 客户服务系统 | ❌ 不支持 | 缺少客服对话/工单表 |

### 2.4 修正后的Actor Description

**Actor name:** Merchant  
**Description (修正版):** Operators and managers of flower shops, responsible for comprehensive store operations including store information management, product information maintenance (create, update, publish, unpublish), product trackability information setup, inventory management, order processing and shipment, coupon creation and management, and sales statistics review. Merchants must complete qualification verification before activating their stores.

### 2.5 修正差异说明

- ✅ **新增：** "store information management" - 明确店铺信息管理
- ✅ **细化：** "product information maintenance" → 添加具体操作（create, update, publish, unpublish）
- ✅ **新增：** "product trackability information setup" - 明确产品溯源功能
- ✅ **细化：** "order processing" → "order processing and shipment" - 明确包含发货
- ✅ **修改：** "marketing campaign setup" → "coupon creation and management" - 根据DDL实际支持精确化
- ✅ **新增：** "sales statistics review" - 明确销售统计查看
- ✅ **新增：** "qualification verification" - 明确商家认证审核流程
- ❌ **删除：** "membership management" - 这是平台功能，不是商家功能
- ❌ **删除：** "customer service" - DDL不支持客服系统

---

## 3. Administrator（管理员）角色分析

### 3.1 原始描述

**Actor name:** Administrator  
**Description:** Backend system managers responsible for user permission allocation, system parameter configuration, data maintenance, system monitoring, and security assurance for technical support and system maintenance.

### 3.2 合理性分析

#### ✅ 已包含的功能

- 系统参数配置 (system parameter configuration)
- 数据维护 (data maintenance)

#### ⚠️ 需要调整的功能

**用户权限分配 (user permission allocation)** - ⚠️ 部分支持

- DDL支持：administrators表有permission字段（SUPER_ADMIN/ADMIN）
- 但仅支持管理员间的权限区分，没有对Customer和Merchant的权限管理表
- 实际上Administrator应该管理商家审核，而不是分配用户权限

**系统监控 (system monitoring)** - ❌ DDL不支持

- 没有系统日志表、操作审计表、性能监控表
- 需求文档6.3提到"重要操作日志记录"，但DDL未实现

**安全保障 (security assurance)** - ⚠️ 业务逻辑层面

- DDL本身不直接体现安全功能（如JWT、加密等）
- 这属于应用层实现，不是数据库结构体现的

#### ✅ 未明确但DDL支持的功能

- 商家审核（merchants.status管理）
- 产品评价审核（product_reviews.status: PENDING/APPROVED/REJECTED）
- 养护知识发布和管理（care_knowledge表）
- 产品分类管理（product_categories表）
- 用户管理（customers表的CRUD）
- 商家管理（merchants表的CRUD）
- 优惠券管理（平台级优惠券，coupons.merch_id为NULL的）
- 订单管理（orders表）

### 3.3 DDL支持情况总结

| 功能 | DDL支持 | 相关表/字段 |
|------|---------|-------------|
| 管理员权限管理 | ✅ 完全支持 | administrators.permission, status |
| 系统参数配置 | ✅ 完全支持 | system_configuration表 |
| 商家审核 | ✅ 完全支持 | merchants.status |
| 产品评价审核 | ✅ 完全支持 | product_reviews.status |
| 用户数据管理 | ✅ 完全支持 | customers表 |
| 商家数据管理 | ✅ 完全支持 | merchants表 |
| 养护知识管理 | ✅ 完全支持 | care_knowledge表 |
| 产品分类管理 | ✅ 完全支持 | product_categories表 |
| 平台优惠券管理 | ✅ 完全支持 | coupons表（merch_id=NULL） |
| 订单管理 | ✅ 完全支持 | orders表 |
| 用户权限分配 | ❌ 不支持 | 无RBAC权限表 |
| 系统监控 | ❌ 不支持 | 缺少日志/审计表 |
| 安全保障 | N/A | 应用层功能 |

### 3.4 修正后的Actor Description

**Actor name:** Administrator  
**Description (修正版):** Backend system managers responsible for platform data management including user account management, merchant verification and management, product review approval, product category management, care knowledge content management, platform coupon management, system parameter configuration, and order supervision. Administrators have hierarchical permissions (SUPER_ADMIN and ADMIN) for different operational scopes.

### 3.5 修正差异说明

- ✅ **新增：** "user account management" - 明确用户账户管理
- ✅ **新增：** "merchant verification and management" - 明确商家审核和管理
- ✅ **新增：** "product review approval" - 明确产品评价审核
- ✅ **新增：** "product category management" - 明确产品分类管理
- ✅ **新增：** "care knowledge content management" - 明确养护知识管理
- ✅ **新增：** "platform coupon management" - 明确平台优惠券管理
- ✅ **新增：** "order supervision" - 明确订单监管
- ✅ **新增：** "hierarchical permissions" - 明确层级权限（SUPER_ADMIN/ADMIN）
- ⚠️ **修改：** "user permission allocation" → 改为具体的管理功能（用户/商家管理），因为DDL不支持RBAC权限分配
- ❌ **删除：** "system monitoring" - DDL不支持监控（无日志/审计表）
- ❌ **删除：** "security assurance" - 这是应用层功能，不在DDL范围
- ❌ **删除：** "technical support" - 过于宽泛，不是数据库结构体现的

---

## 4. System (Automatic)（系统自动化）分析

### 4.1 原始描述

**Actor name:** System (Automatic)  
**Description:** Built-in system function modules that automatically execute tasks including inventory alerts, automatic order review, status synchronization, and data backup.

### 4.2 合理性分析

这是一个特殊的"Actor"，实际上是系统的自动化功能模块。我们需要评估DDL是否为这些自动化功能提供了必要的数据支持。

#### ⚠️ 需要详细分析的功能

**1. 库存预警 (Inventory alerts)** - ⚠️ 部分支持

DDL支持：
- products.stock - 库存数量字段 ✓
- products.stock_status - 库存状态字段（IN_STOCK, LOW_STOCK, OUT_OF_STOCK）✓
- 可以实现：通过定时任务查询库存，判断是否低于阈值

缺失：
- 没有库存预警配置表（预警阈值、预警接收人等）
- 没有预警历史记录表

**2. 自动订单审核 (Automatic order review)** - ❌ DDL不支持

订单表有状态管理（orders.status），但：
- **缺失：** 没有订单审核规则表
- **缺失：** 没有自动审核日志表
- **缺失：** 没有风控规则表
- 描述不够具体：订单审核通常指风控审核、支付验证，DDL未体现

**3. 状态同步 (Status synchronization)** - ⚠️ DDL提供基础字段

DDL支持：
- 订单状态字段（orders.status, payment_status）✓
- 商家状态字段（merchants.status）✓
- 优惠券状态字段（coupons.status, customer_coupons.status）✓
- 所有表都有update_date字段（自动更新时间戳）✓
- 可以实现：状态变更时更新相关表

缺失：
- 没有状态变更历史表（审计追踪）

**4. 数据备份 (Data backup)** - N/A 数据库/运维层面

- 这不是DDL层面的功能，是数据库管理系统或运维层面的
- DDL无法体现数据备份功能
- 应从Actor描述中移除，或改为"业务数据归档"

#### ✅ 其他可能的系统自动化功能（DDL支持）

- 优惠券过期处理：根据coupons.end_date和customer_coupons状态自动更新
- 订单自动取消：超时未支付的订单自动取消
- 产品库存状态更新：根据库存数量自动更新stock_status

### 4.3 DDL支持情况总结

| 功能 | DDL支持 | 相关表/字段 | 需要补充 |
|------|---------|-------------|----------|
| 库存预警 | ⚠️ 基础支持 | products.stock, stock_status | 预警配置表、预警记录表 |
| 自动订单审核 | ❌ 不支持 | orders表 | 审核规则表、风控规则表、审核日志表 |
| 状态同步 | ✅ 基础支持 | 各表的status字段、update_date | 状态变更历史表（可选） |
| 数据备份 | N/A | - | 运维层面功能 |
| 优惠券过期处理 | ✅ 支持 | coupons.end_date, customer_coupons.status | 可通过定时任务实现 |
| 订单自动取消 | ✅ 支持 | orders.order_date, status | 可通过定时任务实现 |

### 4.4 修正后的Actor Description

**Actor name:** System (Automatic)  
**Description (修正版):** Built-in system function modules that automatically execute scheduled tasks including inventory status updates based on stock levels, expired coupon status updates, unpaid order automatic cancellation after timeout, and order/product status synchronization. The system relies on application-layer scheduled jobs to monitor and update relevant status fields in the database.

### 4.5 修正差异说明

- ✅ **修改：** "inventory alerts" → "inventory status updates based on stock levels" - 根据DDL实际支持（可自动更新stock_status，但无预警表）
- ✅ **新增：** "expired coupon status updates" - DDL支持的自动化功能
- ✅ **新增：** "unpaid order automatic cancellation after timeout" - DDL支持的自动化功能
- ⚠️ **修改：** "status synchronization" → "order/product status synchronization" - 更具体
- ✅ **新增说明：** "relies on application-layer scheduled jobs" - 明确这些是应用层定时任务，不是数据库触发器
- ❌ **删除：** "automatic order review" - DDL不支持（无审核规则表、风控表）
- ❌ **删除：** "data backup" - 这是运维层面功能，不是业务系统Actor的职责

---

## 5. DDL不支持的功能清单

### 5.1 🔴 重大功能缺失（建议补充DDL）

#### 1. 物流跟踪系统 - Customer需要

```sql
-- 建议新增表
CREATE TABLE `logistics_info` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '物流ID',
  `order_id` BIGINT NOT NULL COMMENT '订单ID',
  `logistics_company` VARCHAR(50) COMMENT '物流公司',
  `tracking_number` VARCHAR(100) COMMENT '运单号',
  `status` VARCHAR(20) COMMENT '物流状态',
  `current_location` VARCHAR(255) COMMENT '当前位置',
  `create_date` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  CONSTRAINT `fk_logistics_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物流信息表';

CREATE TABLE `logistics_tracking` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '物流轨迹ID',
  `logistics_id` BIGINT NOT NULL COMMENT '物流ID',
  `status` VARCHAR(50) COMMENT '节点状态',
  `description` VARCHAR(255) COMMENT '描述',
  `location` VARCHAR(255) COMMENT '位置',
  `tracking_time` DATETIME COMMENT '轨迹时间',
  PRIMARY KEY (`id`),
  KEY `idx_logistics_id` (`logistics_id`),
  CONSTRAINT `fk_tracking_logistics` FOREIGN KEY (`logistics_id`) REFERENCES `logistics_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物流轨迹表';
```

#### 2. 售后服务系统 - Customer需要

```sql
-- 建议新增表
CREATE TABLE `after_sales` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '售后ID',
  `order_id` BIGINT NOT NULL COMMENT '订单ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `type` VARCHAR(20) COMMENT '售后类型：RETURN-退货, EXCHANGE-换货, REFUND-仅退款',
  `reason` VARCHAR(255) COMMENT '售后原因',
  `status` VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态：PENDING-待处理, APPROVED-已同意, REJECTED-已拒绝, COMPLETED-已完成',
  `refund_amount` DECIMAL(10, 2) COMMENT '退款金额',
  `images` TEXT COMMENT '凭证图片（JSON数组）',
  `merchant_reply` TEXT COMMENT '商家回复',
  `create_date` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_date` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_status` (`status`),
  CONSTRAINT `fk_after_sales_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `fk_after_sales_user` FOREIGN KEY (`user_id`) REFERENCES `customers` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='售后服务表';
```

#### 3. 库存预警配置 - System需要

```sql
-- 建议新增表
CREATE TABLE `inventory_alert_config` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `prod_id` BIGINT NOT NULL COMMENT '产品ID',
  `alert_threshold` INT NOT NULL COMMENT '预警阈值',
  `alert_recipient` VARCHAR(100) COMMENT '预警接收人（邮箱/手机）',
  `enabled` TINYINT(1) DEFAULT 1 COMMENT '是否启用',
  `create_date` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_prod_id` (`prod_id`),
  CONSTRAINT `fk_alert_product` FOREIGN KEY (`prod_id`) REFERENCES `products` (`prod_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存预警配置表';

CREATE TABLE `inventory_alert_log` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `prod_id` BIGINT NOT NULL COMMENT '产品ID',
  `stock_level` INT COMMENT '当时库存',
  `alert_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '预警时间',
  `handled` TINYINT(1) DEFAULT 0 COMMENT '是否已处理',
  PRIMARY KEY (`id`),
  KEY `idx_prod_id` (`prod_id`),
  KEY `idx_alert_time` (`alert_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存预警日志表';
```

#### 4. 系统操作日志 - Administrator需要

```sql
-- 建议新增表
CREATE TABLE `system_operation_log` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `operator_id` BIGINT COMMENT '操作人ID',
  `operator_type` VARCHAR(20) COMMENT '操作人类型：ADMIN, MERCHANT, CUSTOMER, SYSTEM',
  `operation` VARCHAR(100) COMMENT '操作类型',
  `target_table` VARCHAR(50) COMMENT '操作目标表',
  `target_id` BIGINT COMMENT '操作目标ID',
  `content` TEXT COMMENT '操作内容（JSON）',
  `ip_address` VARCHAR(50) COMMENT 'IP地址',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`id`),
  KEY `idx_operator` (`operator_id`, `operator_type`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_target` (`target_table`, `target_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统操作日志表';
```

### 5.2 🟡 次要功能缺失（可选）

#### 5. 客服系统 - Merchant需要（原描述中提到）

```sql
-- 如果需要客服功能，建议新增
CREATE TABLE `customer_service_conversations` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '会话ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `merch_id` BIGINT NOT NULL COMMENT '商家ID',
  `status` VARCHAR(20) DEFAULT 'OPEN' COMMENT '状态：OPEN-进行中, CLOSED-已关闭',
  `create_date` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `close_date` DATETIME,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_merch_id` (`merch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客服会话表';

CREATE TABLE `customer_service_messages` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '消息ID',
  `conversation_id` BIGINT NOT NULL COMMENT '会话ID',
  `sender_id` BIGINT NOT NULL COMMENT '发送者ID',
  `sender_type` VARCHAR(20) COMMENT '发送者类型：CUSTOMER, MERCHANT',
  `content` TEXT COMMENT '消息内容',
  `message_type` VARCHAR(20) DEFAULT 'TEXT' COMMENT '消息类型：TEXT-文本, IMAGE-图片',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_conversation_id` (`conversation_id`),
  CONSTRAINT `fk_messages_conversation` FOREIGN KEY (`conversation_id`) REFERENCES `customer_service_conversations` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客服消息表';
```

#### 6. 状态变更历史 - System & Administrator需要

```sql
-- 建议新增表（用于审计追踪）
CREATE TABLE `status_change_history` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '历史ID',
  `table_name` VARCHAR(50) COMMENT '表名',
  `record_id` BIGINT COMMENT '记录ID',
  `field_name` VARCHAR(50) COMMENT '字段名',
  `old_value` VARCHAR(100) COMMENT '旧值',
  `new_value` VARCHAR(100) COMMENT '新值',
  `operator_id` BIGINT COMMENT '操作人ID',
  `operator_type` VARCHAR(20) COMMENT '操作人类型',
  `change_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_record` (`table_name`, `record_id`),
  KEY `idx_change_time` (`change_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='状态变更历史表';
```

---

## 6. 修正后的完整Actor Descriptions

### 6.1 Customer（顾客）

**Actor name:** Customer

**Description:** Core users who browse products, search, place orders, manage shopping carts and favorites, check order status, make payments, apply for refunds, write product reviews, participate in daily check-ins, use coupons and membership benefits, manage delivery addresses and account balance, and view care knowledge on the flower e-commerce platform.

### 6.2 Merchant（商家）

**Actor name:** Merchant

**Description:** Operators and managers of flower shops, responsible for comprehensive store operations including store information management, product information maintenance (create, update, publish, unpublish), product trackability information setup, inventory management, order processing and shipment, coupon creation and management, and sales statistics review. Merchants must complete qualification verification before activating their stores.

### 6.3 Administrator（管理员）

**Actor name:** Administrator

**Description:** Backend system managers responsible for platform data management including user account management, merchant verification and management, product review approval, product category management, care knowledge content management, platform coupon management, system parameter configuration, and order supervision. Administrators have hierarchical permissions (SUPER_ADMIN and ADMIN) for different operational scopes.

### 6.4 System (Automatic)（系统自动化）

**Actor name:** System (Automatic)

**Description:** Built-in system function modules that automatically execute scheduled tasks including inventory status updates based on stock levels, expired coupon status updates, unpaid order automatic cancellation after timeout, and order/product status synchronization. The system relies on application-layer scheduled jobs to monitor and update relevant status fields in the database.

---

## 7. 总结

### 7.1 分析完成情况

✅ 所有4个Actor Descriptions已完成分析：

1. **Customer：** 修正了8项内容，删除了物流跟踪，新增了多个DDL支持的功能
2. **Merchant：** 修正了6项内容，删除了会员管理和客服功能
3. **Administrator：** 重新定义了职责范围，聚焦于平台数据管理
4. **System：** 明确了基于DDL可实现的自动化功能

### 7.2 关键发现

- 💡 **原Actor描述过于概括**，很多DDL已支持的功能未明确列出
- ⚠️ **部分描述的功能DDL不完全支持**（如物流跟踪、售后服务、客服系统）
- ✅ **DDL支持的核心业务功能**（产品、订单、购物车、优惠券等）都很完善

### 7.3 建议

根据实际业务优先级，建议补充以下4个重大功能缺失的数据表：

1. **物流跟踪系统**（logistics_info, logistics_tracking）- 提升用户体验
2. **售后服务系统**（after_sales）- 完善售后流程
3. **库存预警配置**（inventory_alert_config, inventory_alert_log）- 提升库存管理
4. **系统操作日志**（system_operation_log）- 满足审计需求

---

**文档结束**
