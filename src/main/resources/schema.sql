-- ============================================
-- 鲜花市场数据库初始化脚本
-- ============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS `flower_market` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `flower_market`;

-- ============================================
-- 1. 顾客表 (customers)
-- ============================================
DROP TABLE IF EXISTS `customers`;
CREATE TABLE `customers` (
  `user_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `name` VARCHAR(50) NOT NULL COMMENT '用户名',
  `email` VARCHAR(100) NOT NULL COMMENT '邮箱',
  `phone` VARCHAR(20) COMMENT '手机号',
  `password` VARCHAR(255) NOT NULL COMMENT '密码（加密）',
  `balance` DECIMAL(10, 2) DEFAULT 0.00 COMMENT '账户余额',
  `level` VARCHAR(20) DEFAULT 'NORMAL' COMMENT '会员等级：NORMAL-普通，VIP-VIP，SVIP-超级VIP',
  `gender` VARCHAR(10) COMMENT '性别',
  `address` VARCHAR(255) COMMENT '默认地址',
  `email_verified` TINYINT(1) DEFAULT 0 COMMENT '邮箱是否验证：0-未验证，1-已验证',
  `create_date` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `uk_email` (`email`),
  UNIQUE KEY `uk_phone` (`phone`),
  KEY `idx_level` (`level`),
  KEY `idx_create_date` (`create_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='顾客表';

-- ============================================
-- 2. 商家表 (merchants)
-- ============================================
DROP TABLE IF EXISTS `merchants`;
CREATE TABLE `merchants` (
  `merch_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '商家ID',
  `name` VARCHAR(100) NOT NULL COMMENT '商家名称',
  `email` VARCHAR(100) NOT NULL COMMENT '商家邮箱',
  `password` VARCHAR(255) NOT NULL COMMENT '密码（加密）',
  `phone` VARCHAR(20) NOT NULL COMMENT '商家电话',
  `shop_logo` VARCHAR(255) COMMENT '店铺logo',
  `qualification` VARCHAR(255) COMMENT '商家资质证明',
  `address` VARCHAR(255) COMMENT '商家地址',
  `description` TEXT COMMENT '店铺描述',
  `status` VARCHAR(20) DEFAULT 'PENDING' COMMENT '商家状态：PENDING-待审核，ACTIVE-正常，SUSPENDED-暂停，REJECTED-已拒绝',
  `create_date` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`merch_id`),
  UNIQUE KEY `uk_email` (`email`),
  UNIQUE KEY `uk_phone` (`phone`),
  KEY `idx_status` (`status`),
  KEY `idx_create_date` (`create_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商家表';

-- ============================================
-- 3. 管理员表 (administrators)
-- ============================================
DROP TABLE IF EXISTS `administrators`;
CREATE TABLE `administrators` (
  `admin_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
  `name` VARCHAR(50) NOT NULL COMMENT '管理员姓名',
  `password` VARCHAR(255) NOT NULL COMMENT '密码（加密）',
  `email` VARCHAR(100) NOT NULL COMMENT '邮箱',
  `permission` VARCHAR(50) DEFAULT 'ADMIN' COMMENT '权限等级：SUPER_ADMIN-超级管理员，ADMIN-管理员',
  `status` VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE-正常，INACTIVE-禁用',
  `create_date` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`admin_id`),
  UNIQUE KEY `uk_email` (`email`),
  KEY `idx_permission` (`permission`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='管理员表';

-- ============================================
-- 4. 产品分类表 (product_categories)
-- ============================================
DROP TABLE IF EXISTS `product_categories`;
CREATE TABLE `product_categories` (
  `cate_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `name` VARCHAR(50) NOT NULL COMMENT '分类名称',
  `parent_id` BIGINT DEFAULT 0 COMMENT '父分类ID，0表示顶级分类',
  `sort_order` INT DEFAULT 0 COMMENT '排序顺序',
  `icon` VARCHAR(255) COMMENT '分类图标',
  `description` VARCHAR(255) COMMENT '分类描述',
  `create_date` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`cate_id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='产品分类表';

-- ============================================
-- 5. 产品表 (products)
-- ============================================
DROP TABLE IF EXISTS `products`;
CREATE TABLE `products` (
  `prod_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '产品ID',
  `merch_id` BIGINT NOT NULL COMMENT '商家ID',
  `cat_id` BIGINT NOT NULL COMMENT '分类ID',
  `name` VARCHAR(100) NOT NULL COMMENT '产品名称',
  `price` DECIMAL(10, 2) NOT NULL COMMENT '产品价格',
  `stock` INT NOT NULL DEFAULT 0 COMMENT '库存数量',
  `sales` INT DEFAULT 0 COMMENT '销量',
  `main_image` VARCHAR(255) COMMENT '产品主图',
  `images` TEXT COMMENT '产品图片集（JSON数组）',
  `description` TEXT COMMENT '产品描述',
  `status` VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '产品状态：ACTIVE-上架，INACTIVE-下架，DELETED-已删除',
  `stock_status` VARCHAR(20) DEFAULT 'IN_STOCK' COMMENT '库存状态：IN_STOCK-有货，LOW_STOCK-库存不足，OUT_OF_STOCK-缺货',
  `create_date` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`prod_id`),
  KEY `idx_merch_id` (`merch_id`),
  KEY `idx_cat_id` (`cat_id`),
  KEY `idx_status` (`status`),
  KEY `idx_price` (`price`),
  KEY `idx_sales` (`sales`),
  KEY `idx_create_date` (`create_date`),
  CONSTRAINT `fk_products_merchant` FOREIGN KEY (`merch_id`) REFERENCES `merchants` (`merch_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_products_category` FOREIGN KEY (`cat_id`) REFERENCES `product_categories` (`cate_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='产品表';

-- ============================================
-- 6. 产品收藏表 (product_favorites)
-- ============================================
DROP TABLE IF EXISTS `product_favorites`;
CREATE TABLE `product_favorites` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `prod_id` BIGINT NOT NULL COMMENT '产品ID',
  `fav_date` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_prod` (`user_id`, `prod_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_prod_id` (`prod_id`),
  KEY `idx_fav_date` (`fav_date`),
  CONSTRAINT `fk_favorites_user` FOREIGN KEY (`user_id`) REFERENCES `customers` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_favorites_product` FOREIGN KEY (`prod_id`) REFERENCES `products` (`prod_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='产品收藏表';

-- ============================================
-- 7. 产品评价表 (product_reviews)
-- ============================================
DROP TABLE IF EXISTS `product_reviews`;
CREATE TABLE `product_reviews` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '评价ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `prod_id` BIGINT NOT NULL COMMENT '产品ID',
  `order_id` BIGINT COMMENT '订单ID',
  `rating` INT NOT NULL COMMENT '评分：1-5星',
  `content` TEXT COMMENT '评价内容',
  `images` TEXT COMMENT '评价图片（JSON数组）',
  `verified` TINYINT(1) DEFAULT 0 COMMENT '是否已购买验证：0-未验证，1-已验证',
  `status` VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态：PENDING-待审核，APPROVED-已通过，REJECTED-已拒绝',
  `create_date` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_prod_id` (`prod_id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_rating` (`rating`),
  KEY `idx_create_date` (`create_date`),
  CONSTRAINT `fk_reviews_user` FOREIGN KEY (`user_id`) REFERENCES `customers` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_reviews_product` FOREIGN KEY (`prod_id`) REFERENCES `products` (`prod_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='产品评价表';

-- ============================================
-- 8. 产品溯源表 (product_trackability)
-- ============================================
DROP TABLE IF EXISTS `product_trackability`;
CREATE TABLE `product_trackability` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '溯源ID',
  `prod_id` BIGINT NOT NULL COMMENT '产品ID',
  `origin` VARCHAR(100) COMMENT '产地',
  `planting_method` VARCHAR(100) COMMENT '种植方式',
  `picking_date` DATE COMMENT '采摘日期',
  `proc_date` DATE COMMENT '加工日期',
  `certification` VARCHAR(255) COMMENT '认证信息',
  `description` TEXT COMMENT '溯源描述',
  `create_date` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_prod_id` (`prod_id`),
  CONSTRAINT `fk_trackability_product` FOREIGN KEY (`prod_id`) REFERENCES `products` (`prod_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='产品溯源表';

-- ============================================
-- 9. 订单表 (orders)
-- ============================================
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` VARCHAR(50) NOT NULL COMMENT '订单号',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `merch_id` BIGINT NOT NULL COMMENT '商家ID',
  `order_date` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
  `total_price` DECIMAL(10, 2) NOT NULL COMMENT '订单总价',
  `discount_amount` DECIMAL(10, 2) DEFAULT 0.00 COMMENT '优惠金额',
  `actual_price` DECIMAL(10, 2) NOT NULL COMMENT '实付金额',
  `address` VARCHAR(255) NOT NULL COMMENT '收货地址',
  `receiver_name` VARCHAR(50) NOT NULL COMMENT '收货人姓名',
  `receiver_phone` VARCHAR(20) NOT NULL COMMENT '收货人电话',
  `payment_status` VARCHAR(20) DEFAULT 'UNPAID' COMMENT '支付状态：UNPAID-未支付，PAID-已支付，REFUNDED-已退款',
  `payment_time` DATETIME COMMENT '支付时间',
  `payment_method` VARCHAR(20) COMMENT '支付方式：ALIPAY-支付宝，WECHAT-微信，BALANCE-余额',
  `status` VARCHAR(20) DEFAULT 'PENDING' COMMENT '订单状态：PENDING-待支付，PROCESSING-处理中，SHIPPED-已发货，COMPLETED-已完成，CANCELLED-已取消',
  `delivery_time` DATETIME COMMENT '发货时间',
  `completion_time` DATETIME COMMENT '完成时间',
  `cancel_reason` VARCHAR(255) COMMENT '取消原因',
  `remark` VARCHAR(255) COMMENT '订单备注',
  `create_date` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_merch_id` (`merch_id`),
  KEY `idx_payment_status` (`payment_status`),
  KEY `idx_status` (`status`),
  KEY `idx_order_date` (`order_date`),
  CONSTRAINT `fk_orders_user` FOREIGN KEY (`user_id`) REFERENCES `customers` (`user_id`),
  CONSTRAINT `fk_orders_merchant` FOREIGN KEY (`merch_id`) REFERENCES `merchants` (`merch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单表';

-- ============================================
-- 10. 订单项表 (order_items)
-- ============================================
DROP TABLE IF EXISTS `order_items`;
CREATE TABLE `order_items` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '订单项ID',
  `order_id` BIGINT NOT NULL COMMENT '订单ID',
  `prod_id` BIGINT NOT NULL COMMENT '产品ID',
  `name` VARCHAR(100) NOT NULL COMMENT '产品名称',
  `main_image` VARCHAR(255) COMMENT '产品图片',
  `quantity` INT NOT NULL COMMENT '购买数量',
  `unit_price` DECIMAL(10, 2) NOT NULL COMMENT '单价',
  `total_price` DECIMAL(10, 2) NOT NULL COMMENT '小计',
  `create_date` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_prod_id` (`prod_id`),
  CONSTRAINT `fk_order_items_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_order_items_product` FOREIGN KEY (`prod_id`) REFERENCES `products` (`prod_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单项表';

-- ============================================
-- 11. 购物车表 (shopping_cart)
-- ============================================
DROP TABLE IF EXISTS `shopping_cart`;
CREATE TABLE `shopping_cart` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '购物车ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `prod_id` BIGINT NOT NULL COMMENT '产品ID',
  `quantity` INT NOT NULL DEFAULT 1 COMMENT '商品数量',
  `selected` TINYINT(1) DEFAULT 1 COMMENT '是否选中：0-未选中，1-已选中',
  `create_date` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `update_date` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_prod` (`user_id`, `prod_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_prod_id` (`prod_id`),
  CONSTRAINT `fk_cart_user` FOREIGN KEY (`user_id`) REFERENCES `customers` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_cart_product` FOREIGN KEY (`prod_id`) REFERENCES `products` (`prod_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='购物车表';

-- ============================================
-- 12. 优惠券表 (coupons)
-- ============================================
DROP TABLE IF EXISTS `coupons`;
CREATE TABLE `coupons` (
  `coupon_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '优惠券ID',
  `merch_id` BIGINT COMMENT '商家ID，NULL表示平台优惠券',
  `name` VARCHAR(100) NOT NULL COMMENT '优惠券名称',
  `type` VARCHAR(20) NOT NULL COMMENT '优惠券类型：DISCOUNT-折扣券，FULL_REDUCTION-满减券，FIXED_AMOUNT-固定金额券',
  `value` DECIMAL(10, 2) NOT NULL COMMENT '优惠值（折扣比例或金额）',
  `min_price` DECIMAL(10, 2) DEFAULT 0.00 COMMENT '最低消费金额',
  `total_quantity` INT NOT NULL COMMENT '发放总数量',
  `received_quantity` INT DEFAULT 0 COMMENT '已领取数量',
  `start_date` DATETIME NOT NULL COMMENT '有效期开始',
  `end_date` DATETIME NOT NULL COMMENT '有效期结束',
  `status` VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE-激活，INACTIVE-未激活，EXPIRED-已过期',
  `description` VARCHAR(255) COMMENT '优惠券描述',
  `create_date` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`coupon_id`),
  KEY `idx_merch_id` (`merch_id`),
  KEY `idx_type` (`type`),
  KEY `idx_status` (`status`),
  KEY `idx_date_range` (`start_date`, `end_date`),
  CONSTRAINT `fk_coupons_merchant` FOREIGN KEY (`merch_id`) REFERENCES `merchants` (`merch_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='优惠券表';

-- ============================================
-- 13. 优惠券券码表 (coupon_coupons)
-- ============================================
DROP TABLE IF EXISTS `coupon_coupons`;
CREATE TABLE `coupon_coupons` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `coupon_id` BIGINT NOT NULL COMMENT '优惠券ID',
  `code` VARCHAR(50) NOT NULL COMMENT '券码',
  `user_id` BIGINT COMMENT '使用用户ID',
  `used` TINYINT(1) DEFAULT 0 COMMENT '是否已使用：0-未使用，1-已使用',
  `used_date` DATETIME COMMENT '使用时间',
  `create_date` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`),
  KEY `idx_coupon_id` (`coupon_id`),
  KEY `idx_user_id` (`user_id`),
  CONSTRAINT `fk_coupon_codes_coupon` FOREIGN KEY (`coupon_id`) REFERENCES `coupons` (`coupon_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_coupon_codes_user` FOREIGN KEY (`user_id`) REFERENCES `customers` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='优惠券券码表';

-- ============================================
-- 14. 用户优惠券表 (customer_coupons)
-- ============================================
DROP TABLE IF EXISTS `customer_coupons`;
CREATE TABLE `customer_coupons` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `coupon_id` BIGINT NOT NULL COMMENT '优惠券ID',
  `code` VARCHAR(50) COMMENT '券码',
  `status` VARCHAR(20) DEFAULT 'UNUSED' COMMENT '状态：UNUSED-未使用，USED-已使用，EXPIRED-已过期',
  `receive_date` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '领取时间',
  `used_date` DATETIME COMMENT '使用时间',
  `order_id` BIGINT COMMENT '使用的订单ID',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_coupon_id` (`coupon_id`),
  KEY `idx_status` (`status`),
  CONSTRAINT `fk_customer_coupons_user` FOREIGN KEY (`user_id`) REFERENCES `customers` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_customer_coupons_coupon` FOREIGN KEY (`coupon_id`) REFERENCES `coupons` (`coupon_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_customer_coupons_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户优惠券表';

-- ============================================
-- 15. 签到表 (check_ins)
-- ============================================
DROP TABLE IF EXISTS `check_ins`;
CREATE TABLE `check_ins` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '签到ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `check_date` DATE NOT NULL COMMENT '签到日期',
  `continuous_days` INT DEFAULT 1 COMMENT '连续签到天数',
  `reward_points` INT DEFAULT 0 COMMENT '奖励积分',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '签到时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_date` (`user_id`, `check_date`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_check_date` (`check_date`),
  CONSTRAINT `fk_checkins_user` FOREIGN KEY (`user_id`) REFERENCES `customers` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='签到表';

-- ============================================
-- 16. 养护知识表 (care_knowledge)
-- ============================================
DROP TABLE IF EXISTS `care_knowledge`;
CREATE TABLE `care_knowledge` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '知识ID',
  `title` VARCHAR(100) NOT NULL COMMENT '标题',
  `content` TEXT NOT NULL COMMENT '内容',
  `keywords` VARCHAR(255) COMMENT '关键词',
  `cover_image` VARCHAR(255) COMMENT '封面图片',
  `category` VARCHAR(50) COMMENT '分类',
  `author` VARCHAR(50) COMMENT '作者',
  `view_count` INT DEFAULT 0 COMMENT '浏览次数',
  `status` VARCHAR(20) DEFAULT 'PUBLISHED' COMMENT '状态：DRAFT-草稿，PUBLISHED-已发布',
  `create_date` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_category` (`category`),
  KEY `idx_status` (`status`),
  KEY `idx_create_date` (`create_date`),
  FULLTEXT KEY `ft_keywords` (`keywords`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='养护知识表';

-- ============================================
-- 17. 系统配置表 (system_configuration)
-- ============================================
DROP TABLE IF EXISTS `system_configuration`;
CREATE TABLE `system_configuration` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `config_key` VARCHAR(100) NOT NULL COMMENT '配置键',
  `config_value` TEXT NOT NULL COMMENT '配置值',
  `description` VARCHAR(255) COMMENT '配置描述',
  `category` VARCHAR(50) COMMENT '配置分类',
  `create_date` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_key` (`config_key`),
  KEY `idx_category` (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='系统配置表';

-- ============================================
-- 插入初始数据
-- ============================================

-- 插入管理员
INSERT INTO `administrators` (`name`, `password`, `email`, `permission`) VALUES
('超级管理员', '$2a$10$5F6E5L8SAMPLE.HASH.HERE', 'admin@flowermarket.com', 'SUPER_ADMIN'),
('普通管理员', '$2a$10$5F6E5L8SAMPLE.HASH.HERE', 'admin2@flowermarket.com', 'ADMIN');

-- 插入产品分类
INSERT INTO `product_categories` (`name`, `parent_id`, `sort_order`) VALUES
('鲜花', 0, 1),
('绿植', 0, 2),
('花束', 1, 1),
('盆栽', 1, 2),
('永生花', 1, 3),
('多肉植物', 2, 1),
('观叶植物', 2, 2);

-- 插入系统配置
INSERT INTO `system_configuration` (`config_key`, `config_value`, `description`, `category`) VALUES
('site_name', '鲜花市场', '网站名称', 'basic'),
('site_logo', '/images/logo.png', '网站Logo', 'basic'),
('check_in_points', '10', '每日签到奖励积分', 'reward'),
('continuous_check_in_bonus', '5', '连续签到额外奖励积分', 'reward'),
('free_shipping_amount', '99.00', '免运费金额', 'shipping'),
('default_shipping_fee', '10.00', '默认运费', 'shipping');

-- ============================================
-- 创建视图（可选）
-- ============================================

-- 产品统计视图
CREATE OR REPLACE VIEW `v_product_statistics` AS
SELECT 
    p.prod_id,
    p.name,
    p.price,
    p.stock,
    p.sales,
    COUNT(DISTINCT pf.id) AS favorite_count,
    COUNT(DISTINCT pr.id) AS review_count,
    AVG(pr.rating) AS avg_rating
FROM products p
LEFT JOIN product_favorites pf ON p.prod_id = pf.prod_id
LEFT JOIN product_reviews pr ON p.prod_id = pr.prod_id AND pr.status = 'APPROVED'
GROUP BY p.prod_id;

-- 商家销售统计视图
CREATE OR REPLACE VIEW `v_merchant_sales_statistics` AS
SELECT 
    m.merch_id,
    m.name AS merchant_name,
    COUNT(DISTINCT o.id) AS total_orders,
    SUM(o.actual_price) AS total_sales,
    COUNT(DISTINCT p.prod_id) AS total_products
FROM merchants m
LEFT JOIN orders o ON m.merch_id = o.merch_id AND o.payment_status = 'PAID'
LEFT JOIN products p ON m.merch_id = p.merch_id AND p.status = 'ACTIVE'
GROUP BY m.merch_id;

-- ============================================
-- 创建索引优化（额外的复合索引）
-- ============================================

-- 订单查询优化
CREATE INDEX idx_orders_user_status ON orders(user_id, status);
CREATE INDEX idx_orders_merch_status ON orders(merch_id, status);

-- 产品查询优化
CREATE INDEX idx_products_cat_status ON products(cat_id, status);
CREATE INDEX idx_products_status_sales ON products(status, sales DESC);

-- ============================================
-- 完成
-- ============================================
