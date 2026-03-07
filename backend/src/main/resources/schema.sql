-- ============================================
-- Flower Market database initialization script
-- ============================================

-- Create database
CREATE DATABASE IF NOT EXISTS `flower_market` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `flower_market`;


  -- Disable foreign key checks to allow dropping tables cleanly
SET FOREIGN_KEY_CHECKS = 0;

-- ============================================
-- Drop tables in FK order (child to parent)
-- ============================================
DROP TABLE IF EXISTS `customer_coupons`;
DROP TABLE IF EXISTS `coupon_coupons`;
DROP TABLE IF EXISTS `check_ins`;
DROP TABLE IF EXISTS `order_items`;
DROP TABLE IF EXISTS `orders`;
DROP TABLE IF EXISTS `shopping_cart`;
DROP TABLE IF EXISTS `product_reviews`;
DROP TABLE IF EXISTS `product_favorites`;
DROP TABLE IF EXISTS `product_trackability`;
DROP TABLE IF EXISTS `products`;
DROP TABLE IF EXISTS `product_categories`;
DROP TABLE IF EXISTS `care_knowledge`;
DROP TABLE IF EXISTS `coupons`;
DROP TABLE IF EXISTS `merchants`;
DROP TABLE IF EXISTS `customers`;
DROP TABLE IF EXISTS `administrators`;
DROP TABLE IF EXISTS `system_configuration`;
DROP TABLE IF EXISTS `suppliers`;


-- ============================================
-- 1. Customers table (customers)
-- ============================================
DROP TABLE IF EXISTS `customers`;
CREATE TABLE `customers` (
  `user_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'User ID',
  `name` VARCHAR(50) NOT NULL COMMENT 'Username',
  `email` VARCHAR(100) NOT NULL COMMENT 'Email',
  `phone` VARCHAR(20) COMMENT 'Phone number',
  `password` VARCHAR(255) NOT NULL COMMENT 'Password (encrypted)',
  `balance` DECIMAL(10, 2) DEFAULT 0.00 COMMENT 'Account balance',
  `points` INT DEFAULT 0 COMMENT 'Total reward points',
  `level` VARCHAR(20) DEFAULT 'NORMAL' COMMENT 'Membership level: NORMAL, VIP, SVIP',
  `gender` VARCHAR(10) COMMENT 'Gender',
  `address` VARCHAR(255) COMMENT 'Default address',
  `email_verified` TINYINT(1) DEFAULT 0 COMMENT 'Email verified: 0-no, 1-yes',
  `create_date` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Created at',
  `update_date` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Updated at',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `uk_email` (`email`),
  UNIQUE KEY `uk_phone` (`phone`),
  KEY `idx_level` (`level`),
  KEY `idx_create_date` (`create_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Customers table';

-- ============================================
-- 2. Merchants table (merchants)
-- ============================================
DROP TABLE IF EXISTS `merchants`;
CREATE TABLE `merchants` (
  `merch_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Merchant ID',
  `name` VARCHAR(100) NOT NULL COMMENT 'Merchant name',
  `email` VARCHAR(100) NOT NULL COMMENT 'Merchant email',
  `password` VARCHAR(255) NOT NULL COMMENT 'Password (encrypted)',
  `phone` VARCHAR(20) NOT NULL COMMENT 'Merchant phone',
  `shop_logo` VARCHAR(255) COMMENT 'Shop logo',
  `qualification` VARCHAR(255) COMMENT 'Merchant qualification proof',
  `address` VARCHAR(255) COMMENT 'Merchant address',
  `description` TEXT COMMENT 'Shop description',
  `status` VARCHAR(20) DEFAULT 'PENDING' COMMENT 'Merchant status: PENDING-review, ACTIVE-active, SUSPENDED-paused, REJECTED-rejected',
  `create_date` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Created at',
  `update_date` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Updated at',
  PRIMARY KEY (`merch_id`),
  UNIQUE KEY `uk_email` (`email`),
  UNIQUE KEY `uk_phone` (`phone`),
  KEY `idx_status` (`status`),
  KEY `idx_create_date` (`create_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Merchants table';

-- ============================================
-- 3. Administrators table (administrators)
-- ============================================
DROP TABLE IF EXISTS `administrators`;
CREATE TABLE `administrators` (
  `admin_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Administrator ID',
  `name` VARCHAR(50) NOT NULL COMMENT 'Administrator name',
  `password` VARCHAR(255) NOT NULL COMMENT 'Password (encrypted)',
  `email` VARCHAR(100) NOT NULL COMMENT 'Email',
  `permission` VARCHAR(50) DEFAULT 'ADMIN' COMMENT 'Permission level: SUPER_ADMIN or ADMIN',
  `status` VARCHAR(20) DEFAULT 'ACTIVE' COMMENT 'Status: ACTIVE-enabled, INACTIVE-disabled',
  `create_date` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Created at',
  `update_date` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Updated at',
  PRIMARY KEY (`admin_id`),
  UNIQUE KEY `uk_email` (`email`),
  KEY `idx_permission` (`permission`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Administrators table';

-- ============================================
-- 4. Product categories table (product_categories)
-- ============================================
DROP TABLE IF EXISTS `product_categories`;
CREATE TABLE `product_categories` (
  `cate_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Category ID',
  `name` VARCHAR(50) NOT NULL COMMENT 'Category name',
  `parent_id` BIGINT DEFAULT 0 COMMENT 'Parent category ID; 0 = top level',
  `sort_order` INT DEFAULT 0 COMMENT 'Sort order',
  `icon` VARCHAR(255) COMMENT 'Category icon',
  `description` VARCHAR(255) COMMENT 'Category description',
  `create_date` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Created at',
  `update_date` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Updated at',
  PRIMARY KEY (`cate_id`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Product categories table';

-- ============================================
-- 5. Products table (products)
-- ============================================
DROP TABLE IF EXISTS `products`;
CREATE TABLE `products` (
  `prod_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Product ID',
  `merch_id` BIGINT NOT NULL COMMENT 'Merchant ID',
  `cat_id` BIGINT NOT NULL COMMENT 'Category ID',
  `supplier_id` BIGINT COMMENT 'Supplier ID',
  `name` VARCHAR(100) NOT NULL COMMENT 'Product name',
  `price` DECIMAL(10, 2) NOT NULL COMMENT 'Product price',
  `stock` INT NOT NULL DEFAULT 0 COMMENT 'Stock quantity',
  `sales` INT DEFAULT 0 COMMENT 'Sales volume',
  `main_image` VARCHAR(255) COMMENT 'Primary product image',
  `images` TEXT COMMENT 'Product image set (JSON array)',
  `description` TEXT COMMENT 'Product description',
  `flowering_period` VARCHAR(100) COMMENT 'Flowering period (e.g., 7-10 days)',
  `care_difficulty` VARCHAR(20) DEFAULT 'MEDIUM' COMMENT 'Care difficulty: EASY, MEDIUM, HARD',
  `suitable_environment` TEXT COMMENT 'Suitable environment details (temp, light, humidity)',
  `floral_language` VARCHAR(255) COMMENT 'Flower meaning/language',
  `status` VARCHAR(20) DEFAULT 'ACTIVE' COMMENT 'Product status: ACTIVE-listed, INACTIVE-unlisted, DELETED-removed',
  `stock_status` VARCHAR(20) DEFAULT 'IN_STOCK' COMMENT 'Stock status: IN_STOCK, LOW_STOCK, OUT_OF_STOCK',
  `create_date` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Created at',
  `update_date` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Updated at',
  PRIMARY KEY (`prod_id`),
  KEY `idx_merch_id` (`merch_id`),
  KEY `idx_cat_id` (`cat_id`),
  KEY `idx_status` (`status`),
  KEY `idx_price` (`price`),
  KEY `idx_sales` (`sales`),
  KEY `idx_create_date` (`create_date`),
  KEY `idx_supplier_id` (`supplier_id`),
  CONSTRAINT `fk_products_merchant` FOREIGN KEY (`merch_id`) REFERENCES `merchants` (`merch_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_products_category` FOREIGN KEY (`cat_id`) REFERENCES `product_categories` (`cate_id`),
  CONSTRAINT `fk_products_supplier` FOREIGN KEY (`supplier_id`) REFERENCES `suppliers` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Products table';

-- ============================================
-- 6. Product favorites table (product_favorites)
-- ============================================
DROP TABLE IF EXISTS `product_favorites`;
CREATE TABLE `product_favorites` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Favorite ID',
  `user_id` BIGINT NOT NULL COMMENT 'User ID',
  `prod_id` BIGINT NOT NULL COMMENT 'Product ID',
  `fav_date` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Favorite time',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_prod` (`user_id`, `prod_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_prod_id` (`prod_id`),
  KEY `idx_fav_date` (`fav_date`),
  CONSTRAINT `fk_favorites_user` FOREIGN KEY (`user_id`) REFERENCES `customers` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_favorites_product` FOREIGN KEY (`prod_id`) REFERENCES `products` (`prod_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Product favorites table';

-- ============================================
-- 7. Product reviews table (product_reviews)
-- ============================================
DROP TABLE IF EXISTS `product_reviews`;
CREATE TABLE `product_reviews` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Review ID',
  `user_id` BIGINT NOT NULL COMMENT 'User ID',
  `prod_id` BIGINT NOT NULL COMMENT 'Product ID',
  `order_id` BIGINT COMMENT 'Order ID',
  `rating` INT NOT NULL COMMENT 'Rating: 1-5 stars',
  `content` TEXT COMMENT 'Review content',
  `images` TEXT COMMENT 'Review images (JSON array)',
  `verified` TINYINT(1) DEFAULT 0 COMMENT 'Purchase verified: 0-no, 1-yes',
  `status` VARCHAR(20) DEFAULT 'PENDING' COMMENT 'Status: PENDING-review, APPROVED-approved, REJECTED-rejected',
  `create_date` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Created at',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_prod_id` (`prod_id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_rating` (`rating`),
  KEY `idx_create_date` (`create_date`),
  CONSTRAINT `fk_reviews_user` FOREIGN KEY (`user_id`) REFERENCES `customers` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_reviews_product` FOREIGN KEY (`prod_id`) REFERENCES `products` (`prod_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Product reviews table';

-- ============================================
-- 8. Product traceability table (product_trackability)
-- ============================================
DROP TABLE IF EXISTS `product_trackability`;
CREATE TABLE `product_trackability` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Traceability ID',
  `prod_id` BIGINT NOT NULL COMMENT 'Product ID',
  `origin` VARCHAR(100) COMMENT 'Origin',
  `planting_method` VARCHAR(100) COMMENT 'Planting method',
  `picking_date` DATE COMMENT 'Picking date',
  `proc_date` DATE COMMENT 'Processing date',
  `certification` VARCHAR(255) COMMENT 'Certification info',
  `description` TEXT COMMENT 'Traceability description',
  `create_date` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Created at',
  `update_date` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Updated at',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_prod_id` (`prod_id`),
  CONSTRAINT `fk_trackability_product` FOREIGN KEY (`prod_id`) REFERENCES `products` (`prod_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Product traceability table';

-- ============================================
-- 9. Orders table (orders)
-- ============================================
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Order ID',
  `order_no` VARCHAR(50) NOT NULL COMMENT 'Order number',
  `user_id` BIGINT NOT NULL COMMENT 'User ID',
  `merch_id` BIGINT NOT NULL COMMENT 'Merchant ID',
  `order_date` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Order time',
  `total_price` DECIMAL(10, 2) NOT NULL COMMENT 'Order total',
  `discount_amount` DECIMAL(10, 2) DEFAULT 0.00 COMMENT 'Discount amount',
  `actual_price` DECIMAL(10, 2) NOT NULL COMMENT 'Amount paid',
  `address` VARCHAR(255) NOT NULL COMMENT 'Shipping address',
  `receiver_name` VARCHAR(50) NOT NULL COMMENT 'Receiver name',
  `receiver_phone` VARCHAR(20) NOT NULL COMMENT 'Receiver phone',
  `payment_status` VARCHAR(20) DEFAULT 'UNPAID' COMMENT 'Payment status: UNPAID, PAID, REFUNDED',
  `payment_time` DATETIME COMMENT 'Payment time',
  `payment_method` VARCHAR(20) COMMENT 'Payment method: ALIPAY, WECHAT, BALANCE',
  `status` VARCHAR(20) DEFAULT 'PENDING' COMMENT 'Order status: PENDING, PROCESSING, SHIPPED, COMPLETED, CANCELLED',
  `delivery_time` DATETIME COMMENT 'Delivery time',
  `completion_time` DATETIME COMMENT 'Completion time',
  `cancel_reason` VARCHAR(255) COMMENT 'Cancellation reason',
  `remark` VARCHAR(255) COMMENT 'Order remark',
  `create_date` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Created at',
  `update_date` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Updated at',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_merch_id` (`merch_id`),
  KEY `idx_payment_status` (`payment_status`),
  KEY `idx_status` (`status`),
  KEY `idx_order_date` (`order_date`),
  CONSTRAINT `fk_orders_user` FOREIGN KEY (`user_id`) REFERENCES `customers` (`user_id`),
  CONSTRAINT `fk_orders_merchant` FOREIGN KEY (`merch_id`) REFERENCES `merchants` (`merch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Orders table';

-- ============================================
-- 10. Order items table (order_items)
-- ============================================
DROP TABLE IF EXISTS `order_items`;
CREATE TABLE `order_items` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Order item ID',
  `order_id` BIGINT NOT NULL COMMENT 'Order ID',
  `prod_id` BIGINT NOT NULL COMMENT 'Product ID',
  `name` VARCHAR(100) NOT NULL COMMENT 'Product name',
  `main_image` VARCHAR(255) COMMENT 'Product image',
  `quantity` INT NOT NULL COMMENT 'Quantity',
  `unit_price` DECIMAL(10, 2) NOT NULL COMMENT 'Unit price',
  `total_price` DECIMAL(10, 2) NOT NULL COMMENT 'Subtotal',
  `create_date` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Created at',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_prod_id` (`prod_id`),
  CONSTRAINT `fk_order_items_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_order_items_product` FOREIGN KEY (`prod_id`) REFERENCES `products` (`prod_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Order items table';

-- ============================================
-- 11. Shopping cart table (shopping_cart)
-- ============================================
DROP TABLE IF EXISTS `shopping_cart`;
CREATE TABLE `shopping_cart` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Cart ID',
  `user_id` BIGINT NOT NULL COMMENT 'User ID',
  `prod_id` BIGINT NOT NULL COMMENT 'Product ID',
  `quantity` INT NOT NULL DEFAULT 1 COMMENT 'Quantity',
  `selected` TINYINT(1) DEFAULT 1 COMMENT 'Selected: 0-no, 1-yes',
  `create_date` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Added at',
  `update_date` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Updated at',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_prod` (`user_id`, `prod_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_prod_id` (`prod_id`),
  CONSTRAINT `fk_cart_user` FOREIGN KEY (`user_id`) REFERENCES `customers` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_cart_product` FOREIGN KEY (`prod_id`) REFERENCES `products` (`prod_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Shopping cart table';

-- ============================================
-- 12. Coupons table (coupons)
-- ============================================
DROP TABLE IF EXISTS `coupons`;
CREATE TABLE `coupons` (
  `coupon_id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Coupon ID',
  `merch_id` BIGINT COMMENT 'Merchant ID; NULL indicates platform coupon',
  `name` VARCHAR(100) NOT NULL COMMENT 'Coupon name',
  `type` VARCHAR(20) NOT NULL COMMENT 'Coupon type: DISCOUNT, FULL_REDUCTION, FIXED_AMOUNT',
  `value` DECIMAL(10, 2) NOT NULL COMMENT 'Discount value (percentage or amount)',
  `min_price` DECIMAL(10, 2) DEFAULT 0.00 COMMENT 'Minimum spend',
  `total_quantity` INT NOT NULL COMMENT 'Total quantity issued',
  `received_quantity` INT DEFAULT 0 COMMENT 'Quantity received',
  `start_date` DATETIME NOT NULL COMMENT 'Valid from',
  `end_date` DATETIME NOT NULL COMMENT 'Valid to',
  `status` VARCHAR(20) DEFAULT 'ACTIVE' COMMENT 'Status: ACTIVE, INACTIVE, EXPIRED',
  `description` VARCHAR(255) COMMENT 'Coupon description',
  `create_date` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Created at',
  `update_date` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Updated at',
  PRIMARY KEY (`coupon_id`),
  KEY `idx_merch_id` (`merch_id`),
  KEY `idx_type` (`type`),
  KEY `idx_status` (`status`),
  KEY `idx_date_range` (`start_date`, `end_date`),
  CONSTRAINT `fk_coupons_merchant` FOREIGN KEY (`merch_id`) REFERENCES `merchants` (`merch_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Coupons table';

-- ============================================
-- 13. Coupon codes table (coupon_coupons)
-- ============================================
DROP TABLE IF EXISTS `coupon_coupons`;
CREATE TABLE `coupon_coupons` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `coupon_id` BIGINT NOT NULL COMMENT 'Coupon ID',
  `code` VARCHAR(50) NOT NULL COMMENT 'Coupon code',
  `user_id` BIGINT COMMENT 'Redeeming user ID',
  `used` TINYINT(1) DEFAULT 0 COMMENT 'Used: 0-no, 1-yes',
  `used_date` DATETIME COMMENT 'Used at',
  `create_date` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Created at',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`),
  KEY `idx_coupon_id` (`coupon_id`),
  KEY `idx_user_id` (`user_id`),
  CONSTRAINT `fk_coupon_codes_coupon` FOREIGN KEY (`coupon_id`) REFERENCES `coupons` (`coupon_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_coupon_codes_user` FOREIGN KEY (`user_id`) REFERENCES `customers` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Coupon codes table';

-- ============================================
-- 14. Customer coupons table (customer_coupons)
-- ============================================
DROP TABLE IF EXISTS `customer_coupons`;
CREATE TABLE `customer_coupons` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `user_id` BIGINT NOT NULL COMMENT 'User ID',
  `coupon_id` BIGINT NOT NULL COMMENT 'Coupon ID',
  `code` VARCHAR(50) COMMENT 'Coupon code',
  `status` VARCHAR(20) DEFAULT 'UNUSED' COMMENT 'Status: UNUSED, USED, EXPIRED',
  `receive_date` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Received at',
  `used_date` DATETIME COMMENT 'Used at',
  `order_id` BIGINT COMMENT 'Order ID where used',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_coupon_id` (`coupon_id`),
  KEY `idx_status` (`status`),
  CONSTRAINT `fk_customer_coupons_user` FOREIGN KEY (`user_id`) REFERENCES `customers` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_customer_coupons_coupon` FOREIGN KEY (`coupon_id`) REFERENCES `coupons` (`coupon_id`) ON DELETE CASCADE,
  CONSTRAINT `fk_customer_coupons_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Customer coupons table';

-- ============================================
-- 15. Check-in table (check_ins)
-- ============================================
DROP TABLE IF EXISTS `check_ins`;
CREATE TABLE `check_ins` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Check-in ID',
  `user_id` BIGINT NOT NULL COMMENT 'User ID',
  `check_date` DATE NOT NULL COMMENT 'Check-in date',
  `continuous_days` INT DEFAULT 1 COMMENT 'Consecutive check-in days',
  `reward_points` INT DEFAULT 0 COMMENT 'Reward points',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Check-in timestamp',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_date` (`user_id`, `check_date`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_check_date` (`check_date`),
  CONSTRAINT `fk_checkins_user` FOREIGN KEY (`user_id`) REFERENCES `customers` (`user_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Check-ins table';

-- ============================================
-- 16. Care knowledge table (care_knowledge)
-- ============================================
DROP TABLE IF EXISTS `care_knowledge`;
CREATE TABLE `care_knowledge` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Article ID',
  `title` VARCHAR(100) NOT NULL COMMENT 'Title',
  `content` TEXT NOT NULL COMMENT 'Content',
  `keywords` VARCHAR(255) COMMENT 'Keywords',
  `cover_image` VARCHAR(255) COMMENT 'Cover image',
  `category` VARCHAR(50) COMMENT 'Category',
  `author` VARCHAR(50) COMMENT 'Author',
  `view_count` INT DEFAULT 0 COMMENT 'View count',
  `status` VARCHAR(20) DEFAULT 'PUBLISHED' COMMENT 'Status: DRAFT or PUBLISHED',
  `create_date` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Created at',
  `update_date` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Updated at',
  PRIMARY KEY (`id`),
  KEY `idx_category` (`category`),
  KEY `idx_status` (`status`),
  KEY `idx_create_date` (`create_date`),
  FULLTEXT KEY `ft_keywords` (`keywords`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Care knowledge table';

-- ============================================
-- 17. System configuration table (system_configuration)
-- ============================================
DROP TABLE IF EXISTS `system_configuration`;
CREATE TABLE `system_configuration` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Config ID',
  `config_key` VARCHAR(100) NOT NULL COMMENT 'Config key',
  `config_value` TEXT NOT NULL COMMENT 'Config value',
  `description` VARCHAR(255) COMMENT 'Config description',
  `category` VARCHAR(50) COMMENT 'Config category',
  `create_date` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Created at',
  `update_date` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Updated at',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_key` (`config_key`),
  KEY `idx_category` (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='System configuration table';

-- ============================================
-- 18. Suppliers table (suppliers)
-- ============================================
DROP TABLE IF EXISTS `suppliers`;
CREATE TABLE `suppliers` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Supplier ID',
  `name` VARCHAR(100) NOT NULL COMMENT 'Supplier name',
  `contact_person` VARCHAR(50) COMMENT 'Contact person',
  `phone` VARCHAR(20) COMMENT 'Phone number',
  `email` VARCHAR(100) COMMENT 'Email',
  `address` VARCHAR(255) COMMENT 'Address',
  `description` TEXT COMMENT 'Description',
  `rating` DECIMAL(3, 2) DEFAULT 5.00 COMMENT 'Rating: 1.0-5.0',
  `status` VARCHAR(20) DEFAULT 'ACTIVE' COMMENT 'Status: ACTIVE, INACTIVE, SUSPENDED',
  `create_date` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT 'Created at',
  `update_date` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Updated at',
  PRIMARY KEY (`id`),
  KEY `idx_status` (`status`),
  KEY `idx_rating` (`rating`),
  KEY `idx_create_date` (`create_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Suppliers table';



-- ============================================
-- Create views (optional)
-- ============================================

-- Product statistics view
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

-- Merchant sales statistics view
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
-- Create additional compound indexes for optimization
-- ============================================

-- Order query optimization
CREATE INDEX idx_orders_user_status ON orders(user_id, status);
CREATE INDEX idx_orders_merch_status ON orders(merch_id, status);

-- Product query optimization
CREATE INDEX idx_products_cat_status ON products(cat_id, status);
CREATE INDEX idx_products_status_sales ON products(status, sales DESC);

-- ============================================
-- Done
-- ============================================
