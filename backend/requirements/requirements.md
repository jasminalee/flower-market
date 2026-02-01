# 鲜花市场后端系统 - 需求文档

## 1. 项目概述

### 1.1 项目名称
鲜花市场电商平台后端系统

### 1.2 项目描述
这是一个鲜花销售电商平台的后端系统，支持顾客购买鲜花、商家管理商品、管理员管理平台等功能。

**注意：** 本项目为课程学习项目，支付功能、短信通知等第三方服务接口均为模拟实现，不对接真实的第三方系统。

### 1.3 技术栈
- **框架**: Spring Boot 2.6.13
- **数据库**: MySQL
- **ORM**: MyBatis-Plus
- **API文档**: Knife4j (Swagger)
- **工具库**: Hutool, Lombok
- **开发语言**: Java 8

## 2. 功能模块划分

### 2.1 用户模块

#### 2.1.1 顾客 (Customers)
- 用户注册/登录
- 个人信息管理
- 地址管理
- 账户余额管理
- 会员等级管理
- 签到功能

#### 2.1.2 商家 (Merchants)
- 商家注册/登录
- 店铺信息管理
- 商品上架/下架
- 订单管理
- 商家认证（shop_logo、qualification）

#### 2.1.3 管理员 (Administrators)
- 管理员登录
- 平台管理功能
- 用户管理
- 商家审核
- 系统配置

### 2.2 产品模块

#### 2.2.1 产品管理 (Products)
- 产品CRUD操作
- 产品分类
- 产品库存管理
- 产品上下架
- 产品搜索/筛选
- 产品详情展示
- 产品溯源信息

#### 2.2.2 产品分类 (Product_Categories)
- 分类管理
- 分类层级结构

#### 2.2.3 产品收藏 (Product_Favorites)
- 添加/取消收藏
- 收藏列表查看

#### 2.2.4 产品评价 (Product_Reviews)
- 评价发布
- 评价查看
- 评价审核

#### 2.2.5 产品溯源 (Product_Trackability)
- 产品溯源信息管理
- 溯源信息查看

### 2.3 订单模块

#### 2.3.1 订单管理 (Orders)
- 订单创建
- 订单状态管理
- 订单支付
- 订单发货
- 订单完成
- 订单取消

#### 2.3.2 订单项 (Order_Items)
- 订单商品明细
- 订单金额计算

### 2.4 购物车模块 (Shopping_Cart)
- 添加商品到购物车
- 购物车商品数量修改
- 购物车商品删除
- 购物车列表查看

### 2.5 优惠券模块

#### 2.5.1 优惠券 (Coupons)
- 优惠券创建
- 优惠券类型（折扣、满减等）
- 优惠券有效期管理

#### 2.5.2 优惠券券码 (Coupon_Coupons)
- 券码生成
- 券码核销

#### 2.5.3 用户优惠券 (Customer_Coupons)
- 用户领取优惠券
- 用户优惠券列表
- 优惠券使用

### 2.6 签到模块 (Check_Ins)
- 每日签到
- 签到奖励
- 连续签到统计

### 2.7 养护知识模块 (Care_Knowledge)
- 养护知识发布
- 养护知识查看
- 养护知识分类

### 2.8 系统配置模块 (System_Configuration)
- 系统参数配置
- 系统设置管理

## 3. 数据库设计

详见 ER 图和 `schema.sql` 文件

### 3.1 核心表说明

| 表名 | 说明 | 主要字段 |
|------|------|----------|
| customers | 顾客表 | user_id, name, email, phone, password, balance, level, gender, address, email_verified |
| merchants | 商家表 | merch_id, name, email, password, phone, shop_logo, qualification, address, description, status |
| administrators | 管理员表 | admin_id, name, password, email, permission, status |
| products | 产品表 | prod_id, merch_id, cat_id, name, price, stock, sales, main_image, images, description, status, stock_status |
| product_categories | 产品分类表 | cate_id, name, parent_id, sort_order, icon, description |
| orders | 订单表 | id, order_no, user_id, merch_id, order_date, total_price, discount_amount, actual_price, address, receiver_name, receiver_phone, payment_status, payment_time, payment_method, status, delivery_time, completion_time, cancel_reason, remark |
| order_items | 订单项表 | id, order_id, prod_id, name, main_image, quantity, unit_price, total_price |
| shopping_cart | 购物车表 | id, user_id, prod_id, quantity, selected |
| coupons | 优惠券表 | coupon_id, merch_id, name, type, value, min_price, total_quantity, received_quantity, start_date, end_date, status, description |
| coupon_coupons | 优惠券券码表 | id, coupon_id, code, user_id, used, used_date |
| customer_coupons | 用户优惠券表 | id, user_id, coupon_id, code, status, receive_date, used_date, order_id |
| product_favorites | 产品收藏表 | id, user_id, prod_id, fav_date |
| product_reviews | 产品评价表 | id, user_id, prod_id, order_id, rating, content, images, verified, status |
| check_ins | 签到表 | id, user_id, check_date, continuous_days, reward_points |
| care_knowledge | 养护知识表 | id, title, content, keywords, cover_image, category, author, view_count, status |
| product_trackability | 产品溯源表 | id, prod_id, origin, planting_method, picking_date, proc_date, certification, description |
| system_configuration | 系统配置表 | id, config_key, config_value, description, category |

## 4. API 接口规划

### 4.1 顾客接口
- POST `/api/customer/register` - 顾客注册
- POST `/api/customer/login` - 顾客登录
- GET `/api/customer/profile` - 获取个人信息
- PUT `/api/customer/profile` - 更新个人信息
- GET `/api/customer/balance` - 查询余额
- POST `/api/customer/checkin` - 每日签到

### 4.2 商家接口
- POST `/api/merchant/register` - 商家注册
- POST `/api/merchant/login` - 商家登录
- GET `/api/merchant/profile` - 获取商家信息
- PUT `/api/merchant/profile` - 更新商家信息
- GET `/api/merchant/products` - 商家产品列表
- GET `/api/merchant/orders` - 商家订单列表

### 4.3 产品接口
- GET `/api/products` - 产品列表（带分页、筛选）
- GET `/api/products/{id}` - 产品详情
- POST `/api/products` - 创建产品（商家）
- PUT `/api/products/{id}` - 更新产品（商家）
- DELETE `/api/products/{id}` - 删除产品（商家）
- GET `/api/products/categories` - 产品分类列表 **（需支持分页参数 current/size）**
- POST `/api/products/{id}/favorite` - 收藏产品
- DELETE `/api/products/{id}/favorite` - 取消收藏
- GET `/api/products/favorites` - 我的收藏列表
- POST `/api/products/{id}/review` - 发布评价
- GET `/api/products/{id}/reviews` - 产品评价列表

### 4.4 购物车接口
- GET `/api/cart` - 获取购物车
- POST `/api/cart/items` - 添加商品到购物车
- PUT `/api/cart/items/{id}` - 更新购物车商品数量
- DELETE `/api/cart/items/{id}` - 删除购物车商品

### 4.5 订单接口
- POST `/api/orders` - 创建订单
- GET `/api/orders` - 订单列表
- GET `/api/orders/{id}` - 订单详情
- PUT `/api/orders/{id}/pay` - 支付订单
- PUT `/api/orders/{id}/cancel` - 取消订单
- PUT `/api/orders/{id}/confirm` - 确认收货

### 4.6 优惠券接口
- GET `/api/coupons` - 可用优惠券列表
- POST `/api/coupons/{id}/receive` - 领取优惠券
- GET `/api/coupons/my` - 我的优惠券列表

### 4.7 养护知识接口
- GET `/api/care-knowledge` - 养护知识列表
- GET `/api/care-knowledge/{id}` - 养护知识详情

### 4.8 管理员接口
- POST `/api/admin/login` - 管理员登录
- GET `/api/admin/customers` - 顾客列表管理
- GET `/api/admin/merchants` - 商家列表管理
- PUT `/api/admin/merchants/{id}/verify` - 商家审核
- GET `/api/admin/orders` - 订单管理
- POST `/api/admin/care-knowledge` - 发布养护知识
- GET `/api/admin/config` - 系统配置管理

## 5. 业务流程

### 5.1 用户购买流程
1. 用户浏览商品
2. 添加商品到购物车
3. 查看购物车
4. 创建订单（选择收货地址、应用优惠券）
5. 支付订单
6. 商家发货
7. 用户确认收货
8. 用户评价商品

### 5.2 商家销售流程
1. 商家注册并等待审核
2. 审核通过后上架商品
3. 接收订单
4. 处理订单并发货
5. 查看销售统计

### 5.3 签到奖励流程
1. 用户每日签到
2. 获得积分/优惠券奖励
3. 连续签到额外奖励

## 6. 非功能性需求

### 6.1 性能要求
- API 响应时间 < 500ms
- 支持并发用户数 > 1000

### 6.2 安全要求
- 密码加密存储（BCrypt）
- JWT Token 认证（课程项目简化实现）
- 接口权限控制
- SQL 注入防护（MyBatis-Plus 参数化查询）
- XSS 攻击防护

### 6.3 数据要求
- 数据库备份机制（MySQL 数据库层面）
- 数据完整性约束（外键、唯一索引、非空约束）
- 重要字段时间戳记录（create_date, update_date）

**注意：** 系统操作日志记录功能暂不实现（可作为后续扩展）

## 7. 开发计划

### 阶段一：基础功能
- [ ] 数据库表结构设计与创建
- [ ] 实体类（Entity）创建
- [ ] Mapper 接口创建
- [ ] 用户注册登录功能
- [ ] 产品基础 CRUD

### 阶段二：核心业务
- [ ] 购物车功能
- [ ] 订单管理功能
- [ ] 支付功能
- [ ] 优惠券功能

### 阶段三：增值功能
- [ ] 产品收藏与评价
- [ ] 签到功能
- [ ] 养护知识
- [ ] 产品溯源

### 阶段四：管理功能
- [ ] 商家管理后台
- [ ] 管理员后台
- [ ] 数据统计与报表

## 8. 课程项目说明与功能范围

### 8.1 模拟实现的功能（不对接真实第三方系统）
1. **支付方式**：支持支付宝、微信、余额支付的模拟实现，仅更新订单支付状态，不对接真实支付接口
2. **短信/邮件通知**：仅在后台日志输出，不发送真实短信或邮件
3. **图片存储**：本地文件系统存储，路径存入数据库
4. **物流配送**：仅记录发货时间（delivery_time），不对接物流追踪接口

### 8.2 已明确的业务规则
1. **会员等级权益**：NORMAL（普通）、VIP、SVIP 三个等级，由管理员手动调整
2. **积分系统规则**：
   - 每日签到奖励 10 积分
   - 连续签到额外奖励 5 积分
   - 积分规则可通过 system_configuration 表配置
3. **优惠券类型**：
   - DISCOUNT：折扣券（value 为折扣比例）
   - FULL_REDUCTION：满减券（满 min_price 减 value）
   - FIXED_AMOUNT：固定金额券（直接减 value）
4. **商家入驻审核流程**：
   - 商家注册后状态为 PENDING（待审核）
   - 管理员审核通过后状态变为 ACTIVE（正常）
   - 可设置为 SUSPENDED（暂停）或 REJECTED（已拒绝）
5. **产品审核机制**：商家可自由上下架产品（status: ACTIVE/INACTIVE），暂不需要管理员审核
6. **评价审核机制**：
   - 评价状态：PENDING（待审核）、APPROVED（已通过）、REJECTED（已拒绝）
   - 由管理员进行审核

### 8.3 暂不实现的功能（可作为后续扩展）
1. 物流详细追踪（物流轨迹表）
2. 售后服务系统（退换货流程）
3. 系统操作日志记录表
4. 在线客服系统
5. 库存自动预警配置
6. 真实的第三方支付/短信/邮件接口对接

---

**版本**: v1.0  
**创建日期**: 2025-12-13  
**最后更新**: 2025-12-13
