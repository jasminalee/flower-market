-- ============================================
-- Flower Market database seed data
-- Note: passwords use MD5; sample passwords match usernames unless stated
-- ============================================
-- CREATE DATABASE IF NOT EXISTS flower_market;
USE `flower_market`;


-- ============================================
-- 1. Administrators
-- Password: admin (MD5: 21232f297a57a5a743894a0e4a801fc3)
-- Password: admin123 (MD5: 0192023a7bbd73250516f069df18b500)
-- ============================================
INSERT INTO `administrators` (`admin_id`, `name`, `password`, `email`, `permission`, `status`) VALUES
(1, 'Super Admin', 'e10adc3949ba59abbe56e057f20f883e', 'admin@flowermarket.com', 'SUPER_ADMIN', 'ACTIVE'),
(2, 'Standard Admin', 'e10adc3949ba59abbe56e057f20f883e', 'admin2@flowermarket.com', 'ADMIN', 'ACTIVE');

-- ============================================
-- 2. Customers
-- Password: 123456 (MD5)
-- ============================================
INSERT INTO `customers` (`user_id`, `name`, `email`, `phone`, `password`, `balance`, `points`, `level`, `gender`, `address`, `email_verified`) VALUES
-- Password: 123456, MD5: e10adc3949ba59abbe56e057f20f883e
-- Zhang San checked in 7 times, points: 10+10+10+10+15+15+15 = 85
(1, 'Zhang San', 'zhangsan@example.com', '13800138001', 'e10adc3949ba59abbe56e057f20f883e', 1000.00, 85, 'VIP', 'Male', 'No. 88 Jianguo Rd, Chaoyang, Beijing', 1),
-- Li Si checked in 2 times, points: 10+10 = 20
(2, 'Li Si', 'lisi@example.com', '13800138002', 'e10adc3949ba59abbe56e057f20f883e', 500.50, 20, 'NORMAL', 'Female', 'No. 100 Century Ave, Pudong, Shanghai', 1),
-- Wang Wu checked in 2 times, points: 10+10 = 20
(3, 'Wang Wu', 'wangwu@example.com', '13800138003', 'e10adc3949ba59abbe56e057f20f883e', 2000.00, 20, 'SVIP', 'Male', 'No. 123 Tianhe Rd, Tianhe, Guangzhou', 1),
(4, 'Zhao Liu', 'zhaoliu@example.com', '13800138004', 'e10adc3949ba59abbe56e057f20f883e', 300.00, 0, 'NORMAL', 'Female', 'No. 66 Keji Park Rd, Nanshan, Shenzhen', 1),
-- sunqi MD5: e10adc3949ba59abbe56e057f20f883e
(5, 'Sun Qi', 'sunqi@example.com', '13800138005', 'e10adc3949ba59abbe56e057f20f883e', 150.00, 0, 'NORMAL', 'Male', 'No. 55 Renmin South Rd, Wuhou, Chengdu', 0);

-- ============================================
-- 3. Merchants
-- Passwords match merchant pinyin names (MD5)
-- ============================================
INSERT INTO `merchants` (`merch_id`, `name`, `email`, `password`, `phone`, `shop_logo`, `qualification`, `address`, `description`, `status`) VALUES
-- huadianzhuangjia MD5 example (use flower1): e10adc3949ba59abbe56e057f20f883e
(1, 'Flower Shop Expert', 'flower1@merchant.com', 'e10adc3949ba59abbe56e057f20f883e', '13900139001', '/images/shop/flower1.jpg', '/images/qualification/cert1.jpg', 'Area A, Flower Market, Chaoyang, Beijing', 'Premium custom bouquets, 10 years experience', 'ACTIVE'),
-- flower2 MD5: e10adc3949ba59abbe56e057f20f883e
(2, 'Green Plant Living', 'flower2@merchant.com', 'e10adc3949ba59abbe56e057f20f883e', '13900139002', '/images/shop/flower2.jpg', '/images/qualification/cert2.jpg', '18 Botanical Garden Rd, Xuhui, Shanghai', 'Indoor and outdoor plants to brighten your space', 'ACTIVE'),
-- flower3 MD5 simplified to 123456
(3, 'Express Flowers', 'flower3@merchant.com', 'e10adc3949ba59abbe56e057f20f883e', '13900139003', '/images/shop/flower3.jpg', '/images/qualification/cert3.jpg', '200 Huacheng Ave, Tianhe, Guangzhou', 'Citywide delivery within 2 hours', 'ACTIVE'),
-- merchant4 MD5: e10adc3949ba59abbe56e057f20f883e
(4, 'Pending Merchant', 'merchant4@merchant.com', 'e10adc3949ba59abbe56e057f20f883e', '13900139004', NULL, NULL, '99 Huaqiang North Rd, Futian, Shenzhen', 'New merchant pending review', 'PENDING'),
-- merchant5 MD5: e10adc3949ba59abbe56e057f20f883e
(5, 'Paused Merchant', 'merchant5@merchant.com', 'e10adc3949ba59abbe56e057f20f883e', '13900139005', '/images/shop/flower5.jpg', '/images/qualification/cert5.jpg', '77 Wensan Rd, Xihu, Hangzhou', 'Operations temporarily paused', 'SUSPENDED');

-- ============================================
-- 4. Product categories
-- ============================================
INSERT INTO `product_categories` (`cate_id`, `name`, `parent_id`, `sort_order`, `icon`, `description`) VALUES
(1, 'Fresh Flowers', 0, 1, '/images/category/fresh.png', 'Freshly cut flowers, delivered daily'),
(2, 'Green Plants', 0, 2, '/images/category/plant.png', 'Indoor and outdoor plants for clean air'),
(3, 'Bouquets', 1, 1, '/images/category/bouquet.png', 'Gift-ready bouquets'),
(4, 'Potted Flowers', 1, 2, '/images/category/potted.png', 'Potted blooms for long-lasting displays'),
(5, 'Preserved Flowers', 1, 3, '/images/category/preserved.png', 'Long-lasting preserved arrangements'),
(6, 'Succulents', 2, 1, '/images/category/succulent.png', 'Easy-care succulents'),
(7, 'Foliage Plants', 2, 2, '/images/category/foliage.png', 'Lush leafy plants'),
(8, 'Flowering Pots', 2, 3, '/images/category/flowering.png', 'Flowering plants for all seasons');

-- ============================================
-- 5. Products
-- ============================================
INSERT INTO `flower_market`.`products`(`prod_id`, `merch_id`, `cat_id`, `name`, `price`, `stock`, `sales`, `main_image`, `images`, `description`, `status`, `stock_status`, `create_date`, `update_date`) VALUES (1, 1, 3, 'Red Rose Bouquet (11 stems)', 199.00, 50, 156, '/images/products/main/20260201_b8b0f1bbb5c34a8c919b52c65fb1d497.jpg', '[\"/images/products/detail/20260201_04f6c2bfb5df473eb0f994ed6ee6c5aa.jpg\"]', 'Imported Ecuadorian red roses, large blooms, vibrant color symbolizing passionate love', 'ACTIVE', 'IN_STOCK', '2026-02-01 13:04:28', '2026-02-01 21:08:32');
INSERT INTO `flower_market`.`products`(`prod_id`, `merch_id`, `cat_id`, `name`, `price`, `stock`, `sales`, `main_image`, `images`, `description`, `status`, `stock_status`, `create_date`, `update_date`) VALUES (2, 1, 3, 'Champagne Rose Bouquet (19 stems)', 299.00, 30, 89, '/images/products/main/20260201_bd1af35d3e384d3aadcf9a0f48030bb6.jpg', '[\"/images/products/detail/20260201_6346aba182d140ed86f0cec86875f8b4.jpeg\"]', 'Elegant champagne roses, perfect to express gratitude and respect', 'ACTIVE', 'IN_STOCK', '2026-02-01 13:04:28', '2026-02-01 21:06:50');
INSERT INTO `flower_market`.`products`(`prod_id`, `merch_id`, `cat_id`, `name`, `price`, `stock`, `sales`, `main_image`, `images`, `description`, `status`, `stock_status`, `create_date`, `update_date`) VALUES (3, 1, 5, 'Preserved Rose Glass Dome', 388.00, 19, 45, '/images/products/main/20260201_072b8ae13aa14c5481798f080ea9981e.jpg', '[\"/images/products/detail/20260201_5e02b3852901475185b6697e896c34e9.jpg\"]', 'Preserved rose in a delicate glass dome for lasting romance', 'ACTIVE', 'IN_STOCK', '2026-02-01 13:04:28', '2026-02-01 21:12:19');
INSERT INTO `flower_market`.`products`(`prod_id`, `merch_id`, `cat_id`, `name`, `price`, `stock`, `sales`, `main_image`, `images`, `description`, `status`, `stock_status`, `create_date`, `update_date`) VALUES (4, 1, 3, 'Mixed Bouquet', 158.00, 8, 23, '/images/products/main/20260201_f8b78e83965548dbb7ca35594fbf6075.jpg', '[\"/images/products/detail/20260201_9bdf3d8f2bdc41f7b5c4f7330054567f.jpg\"]', 'Blend of roses, lilies, and baby’s breath for rich color', 'ACTIVE', 'LOW_STOCK', '2026-02-01 13:04:28', '2026-02-01 21:13:13');
INSERT INTO `flower_market`.`products`(`prod_id`, `merch_id`, `cat_id`, `name`, `price`, `stock`, `sales`, `main_image`, `images`, `description`, `status`, `stock_status`, `create_date`, `update_date`) VALUES (5, 2, 6, 'White Peony Succulent', 29.90, 100, 234, '/images/products/succulent_white.jpg', '[\"/images/products/succulent_white_1.jpg\"]', 'Cute white peony succulent; easy care, perfect for desks', 'ACTIVE', 'IN_STOCK', '2026-02-01 13:04:28', '2026-02-01 13:04:28');
INSERT INTO `flower_market`.`products`(`prod_id`, `merch_id`, `cat_id`, `name`, `price`, `stock`, `sales`, `main_image`, `images`, `description`, `status`, `stock_status`, `create_date`, `update_date`) VALUES (6, 2, 6, 'Succulent Combo (6 varieties)', 59.90, 80, 167, '/images/products/succulent_combo.jpg', '[\"/images/products/succulent_combo_1.jpg\", \"/images/products/succulent_combo_2.jpg\"]', 'Six succulents with a decorative pot included', 'ACTIVE', 'IN_STOCK', '2026-02-01 13:04:28', '2026-02-01 13:04:28');
INSERT INTO `flower_market`.`products`(`prod_id`, `merch_id`, `cat_id`, `name`, `price`, `stock`, `sales`, `main_image`, `images`, `description`, `status`, `stock_status`, `create_date`, `update_date`) VALUES (7, 2, 7, 'Monstera Pot', 128.00, 45, 98, '/images/products/monstera.jpg', '[\"/images/products/monstera_1.jpg\"]', 'Scandi favorite; great air purifier, about 50cm tall', 'ACTIVE', 'IN_STOCK', '2026-02-01 13:04:28', '2026-02-01 13:04:28');
INSERT INTO `flower_market`.`products`(`prod_id`, `merch_id`, `cat_id`, `name`, `price`, `stock`, `sales`, `main_image`, `images`, `description`, `status`, `stock_status`, `create_date`, `update_date`) VALUES (8, 2, 7, 'Fiddle Leaf Fig Pot', 168.00, 25, 67, '/images/products/ficus_lyrata.jpg', '[\"/images/products/ficus_lyrata_1.jpg\"]', 'Large-leaf fiddle leaf fig, about 80cm, ideal for living rooms', 'ACTIVE', 'IN_STOCK', '2026-02-01 13:04:28', '2026-02-01 13:04:28');
INSERT INTO `flower_market`.`products`(`prod_id`, `merch_id`, `cat_id`, `name`, `price`, `stock`, `sales`, `main_image`, `images`, `description`, `status`, `stock_status`, `create_date`, `update_date`) VALUES (9, 2, 8, 'Phalaenopsis Orchid Pot', 228.00, 15, 34, '/images/products/phalaenopsis.jpg', '[\"/images/products/phalaenopsis_1.jpg\"]', 'Premium orchid with up to 3-month bloom, gift ready', 'ACTIVE', 'IN_STOCK', '2026-02-01 13:04:28', '2026-02-01 13:04:28');
INSERT INTO `flower_market`.`products`(`prod_id`, `merch_id`, `cat_id`, `name`, `price`, `stock`, `sales`, `main_image`, `images`, `description`, `status`, `stock_status`, `create_date`, `update_date`) VALUES (10, 3, 3, '520 Love Confession Bouquet', 520.00, 60, 201, '/images/products/love_520.jpg', '[\"/images/products/love_520_1.jpg\", \"/images/products/love_520_2.jpg\"]', '52 red roses to say “I love you”', 'ACTIVE', 'IN_STOCK', '2026-02-01 13:04:28', '2026-02-01 13:04:28');
INSERT INTO `flower_market`.`products`(`prod_id`, `merch_id`, `cat_id`, `name`, `price`, `stock`, `sales`, `main_image`, `images`, `description`, `status`, `stock_status`, `create_date`, `update_date`) VALUES (11, 3, 3, 'Carnation Bouquet', 88.00, 120, 345, '/images/products/carnation.jpg', '[\"/images/products/carnation_1.jpg\"]', 'Thankful carnations, a Mother’s Day bestseller', 'ACTIVE', 'IN_STOCK', '2026-02-01 13:04:28', '2026-02-01 13:04:28');
INSERT INTO `flower_market`.`products`(`prod_id`, `merch_id`, `cat_id`, `name`, `price`, `stock`, `sales`, `main_image`, `images`, `description`, `status`, `stock_status`, `create_date`, `update_date`) VALUES (12, 3, 4, 'Sunflower Pot', 78.00, 50, 112, '/images/products/sunflower_pot.jpg', '[\"/images/products/sunflower_pot_1.jpg\"]', 'Sunny sunflowers bringing positive energy', 'ACTIVE', 'IN_STOCK', '2026-02-01 13:04:28', '2026-02-01 13:04:28');
INSERT INTO `flower_market`.`products`(`prod_id`, `merch_id`, `cat_id`, `name`, `price`, `stock`, `sales`, `main_image`, `images`, `description`, `status`, `stock_status`, `create_date`, `update_date`) VALUES (13, 3, 3, 'Lily Bouquet', 138.00, 0, 78, '/images/products/lily.jpg', '[\"/images/products/lily_1.jpg\"]', 'Pure, elegant lilies suitable for many occasions', 'ACTIVE', 'OUT_OF_STOCK', '2026-02-01 13:04:28', '2026-02-01 13:04:28');
INSERT INTO `flower_market`.`products`(`prod_id`, `merch_id`, `cat_id`, `name`, `price`, `stock`, `sales`, `main_image`, `images`, `description`, `status`, `stock_status`, `create_date`, `update_date`) VALUES (14, 3, 3, 'Delisted Bouquet', 99.00, 30, 12, '/images/products/inactive.jpg', '[]', 'This product is delisted', 'INACTIVE', 'IN_STOCK', '2026-02-01 13:04:28', '2026-02-01 13:04:28');

-- ============================================
-- 6. Product traceability
-- ============================================
INSERT INTO `product_trackability` (`prod_id`, `origin`, `planting_method`, `picking_date`, `proc_date`, `certification`, `description`) VALUES
(1, 'Ecuador', 'Greenhouse cultivation', '2025-12-10', '2025-12-11', 'ISO9001 quality certification', 'High-altitude 2800m roses with long vase life and vivid color'),
(2, 'Ecuador', 'Greenhouse cultivation', '2025-12-09', '2025-12-10', 'ISO9001 quality certification', 'Selected champagne rose variety with elegant tone'),
(5, 'Kunming, Yunnan', 'Open field cultivation', '2025-11-20', '2025-11-21', 'Organic certification', 'Yunnan highland succulents, premium quality'),
(7, 'Foshan, Guangdong', 'Greenhouse cultivation', '2025-10-15', '2025-10-16', 'Green plant certification', 'Grown in professional nursery with guaranteed quality');

-- ============================================
-- 7. Shopping cart
-- ============================================
INSERT INTO `shopping_cart` (`user_id`, `prod_id`, `quantity`, `selected`) VALUES
(1, 1, 2, 1),
(1, 5, 3, 1),
(2, 7, 1, 1),
(2, 10, 1, 0),
(3, 3, 1, 1);

-- ============================================
-- 8. Orders
-- ============================================
INSERT INTO `orders` (`id`, `order_no`, `user_id`, `merch_id`, `order_date`, `total_price`, `discount_amount`, `actual_price`, `address`, `receiver_name`, `receiver_phone`, `payment_status`, `payment_time`, `payment_method`, `status`, `delivery_time`, `completion_time`, `remark`) VALUES
(1, 'ORD202512130001', 1, 1, '2025-12-10 10:30:00', 398.00, 50.00, 348.00, 'No. 88 Jianguo Rd, Chaoyang, Beijing', 'Zhang San', '13800138001', 'PAID', '2025-12-10 10:32:00', 'ALIPAY', 'COMPLETED', '2025-12-10 14:00:00', '2025-12-11 16:00:00', 'Please deliver before noon'),
(2, 'ORD202512130002', 1, 2, '2025-12-11 15:20:00', 128.00, 0.00, 128.00, 'No. 88 Jianguo Rd, Chaoyang, Beijing', 'Zhang San', '13800138001', 'PAID', '2025-12-11 15:22:00', 'WECHAT', 'SHIPPED', '2025-12-11 18:00:00', NULL, NULL),
(3, 'ORD202512130003', 2, 3, '2025-12-12 09:15:00', 520.00, 0.00, 520.00, 'No. 100 Century Ave, Pudong, Shanghai', 'Li Si', '13800138002', 'PAID', '2025-12-12 09:17:00', 'BALANCE', 'PROCESSING', NULL, NULL, 'Please call ahead before delivery'),
(4, 'ORD202512130004', 3, 1, '2025-12-12 16:45:00', 597.00, 100.00, 497.00, 'No. 123 Tianhe Rd, Tianhe, Guangzhou', 'Wang Wu', '13800138003', 'PAID', '2025-12-12 16:50:00', 'ALIPAY', 'COMPLETED', '2025-12-12 20:00:00', '2025-12-13 10:00:00', NULL),
(5, 'ORD202512130005', 4, 2, '2025-12-13 08:30:00', 256.00, 0.00, 256.00, 'No. 66 Keji Park Rd, Nanshan, Shenzhen', 'Zhao Liu', '13800138004', 'UNPAID', NULL, NULL, 'PENDING', NULL, NULL, NULL),
(6, 'ORD202512130006', 1, 3, '2025-12-09 14:20:00', 88.00, 0.00, 88.00, 'No. 88 Jianguo Rd, Chaoyang, Beijing', 'Zhang San', '13800138001', 'PAID', '2025-12-09 14:22:00', 'WECHAT', 'CANCELLED', NULL, NULL, 'Changed plans, no longer needed');

-- ============================================
-- 9. Order items
-- ============================================
INSERT INTO `order_items` (`order_id`, `prod_id`, `name`, `main_image`, `quantity`, `unit_price`, `total_price`) VALUES
-- Items for order 1
(1, 1, 'Red Rose Bouquet (11 stems)', '/images/products/rose_red_11.jpg', 2, 199.00, 398.00),
-- Items for order 2
(2, 7, 'Monstera Pot', '/images/products/monstera.jpg', 1, 128.00, 128.00),
-- Items for order 3
(3, 10, '520 Love Confession Bouquet', '/images/products/love_520.jpg', 1, 520.00, 520.00),
-- Items for order 4
(4, 1, 'Red Rose Bouquet (11 stems)', '/images/products/rose_red_11.jpg', 1, 199.00, 199.00),
(4, 2, 'Champagne Rose Bouquet (19 stems)', '/images/products/rose_champagne_19.jpg', 1, 299.00, 299.00),
(4, 5, 'White Peony Succulent', '/images/products/succulent_white.jpg', 2, 29.90, 59.80),
-- Items for order 5
(5, 8, 'Fiddle Leaf Fig Pot', '/images/products/ficus_lyrata.jpg', 1, 168.00, 168.00),
(5, 6, 'Succulent Combo (6 varieties)', '/images/products/succulent_combo.jpg', 1, 59.90, 59.90),
-- Items for order 6
(6, 11, 'Carnation Bouquet', '/images/products/carnation.jpg', 1, 88.00, 88.00);

-- ============================================
-- 10. Product favorites
-- ============================================
INSERT INTO `product_favorites` (`user_id`, `prod_id`, `fav_date`) VALUES
(1, 3, '2025-12-08 10:20:00'),
(1, 7, '2025-12-09 15:30:00'),
(1, 10, '2025-12-10 09:15:00'),
(2, 1, '2025-12-07 14:20:00'),
(2, 5, '2025-12-11 11:30:00'),
(3, 2, '2025-12-06 16:45:00'),
(3, 9, '2025-12-12 08:20:00'),
(4, 11, '2025-12-10 13:10:00');

-- ============================================
-- 11. Product reviews
-- ============================================
INSERT INTO `product_reviews` (`user_id`, `prod_id`, `order_id`, `rating`, `content`, `images`, `verified`, `status`, `create_date`) VALUES
(1, 1, 1, 5, 'Flowers were very fresh, packaging was great, delivery on time, my girlfriend loved it!', '["/images/reviews/review1_1.jpg", "/images/reviews/review1_2.jpg"]', 1, 'APPROVED', '2025-12-11 17:00:00'),
(3, 1, 4, 5, 'Huge rose blooms, bright color, great value', '[]', 1, 'APPROVED', '2025-12-13 11:00:00'),
(3, 2, 4, 4, 'Champagne roses are beautiful, just a bit pricey', '["/images/reviews/review3_1.jpg"]', 1, 'APPROVED', '2025-12-13 11:05:00'),
(1, 11, 6, 3, 'Carnations were average and a little wilted', '[]', 1, 'PENDING', '2025-12-10 10:00:00');

-- ============================================
-- 12. Coupons
-- ============================================
INSERT INTO `coupons` (`coupon_id`, `merch_id`, `name`, `type`, `value`, `min_price`, `total_quantity`, `received_quantity`, `start_date`, `end_date`, `status`, `description`) VALUES
(1, NULL, 'New User Coupon', 'FIXED_AMOUNT', 50.00, 200.00, 1000, 234, '2025-12-01 00:00:00', '2025-12-31 23:59:59', 'ACTIVE', 'First order: save 50'),
(2, NULL, 'Christmas Full Reduction', 'FULL_REDUCTION', 100.00, 500.00, 500, 123, '2025-12-20 00:00:00', '2025-12-26 23:59:59', 'ACTIVE', 'Holiday deal: spend 500 save 100'),
(3, 1, 'Flower Shop 10% Off', 'DISCOUNT', 0.90, 100.00, 200, 89, '2025-12-01 00:00:00', '2025-12-31 23:59:59', 'ACTIVE', 'Exclusive 10% off at Flower Shop Expert'),
(4, 2, 'Green Plant 20% Off', 'DISCOUNT', 0.80, 150.00, 150, 67, '2025-12-01 00:00:00', '2025-12-31 23:59:59', 'ACTIVE', '20% off everything at Green Plant Living'),
(5, NULL, 'Expired Coupon', 'FIXED_AMOUNT', 30.00, 100.00, 100, 100, '2025-11-01 00:00:00', '2025-11-30 23:59:59', 'EXPIRED', 'November-only coupon');

-- ============================================
-- 13. Customer coupons
-- ============================================
INSERT INTO `customer_coupons` (`user_id`, `coupon_id`, `code`, `status`, `receive_date`, `used_date`, `order_id`) VALUES
(1, 1, 'NEW50-USER1-001', 'USED', '2025-12-10 09:00:00', '2025-12-10 10:30:00', 1),
(1, 2, 'XMAS100-USER1-002', 'UNUSED', '2025-12-12 10:00:00', NULL, NULL),
(2, 1, 'NEW50-USER2-001', 'UNUSED', '2025-12-11 14:00:00', NULL, NULL),
(3, 2, 'XMAS100-USER3-001', 'USED', '2025-12-12 15:00:00', '2025-12-12 16:45:00', 4),
(3, 3, 'FLOWER90-USER3-002', 'UNUSED', '2025-12-11 11:00:00', NULL, NULL),
(4, 4, 'GREEN80-USER4-001', 'UNUSED', '2025-12-10 16:00:00', NULL, NULL);

-- ============================================
-- 14. Check-ins
-- ============================================
INSERT INTO `check_ins` (`user_id`, `check_date`, `continuous_days`, `reward_points`) VALUES
(1, '2025-12-07', 1, 10),
(1, '2025-12-08', 2, 10),
(1, '2025-12-09', 3, 10),
(1, '2025-12-10', 4, 10),
(1, '2025-12-11', 5, 15),
(1, '2025-12-12', 6, 15),
(1, '2025-12-13', 7, 15),
(2, '2025-12-12', 1, 10),
(2, '2025-12-13', 2, 10),
(3, '2025-12-10', 1, 10),
(3, '2025-12-13', 1, 10);

-- ============================================
-- 15. Care knowledge articles
-- ============================================
INSERT INTO `care_knowledge` (`title`, `content`, `keywords`, `cover_image`, `category`, `author`, `view_count`, `status`) VALUES
('Daily Care Tips for Roses', 'Roses are among the most popular flowers; proper care extends vase life...\n\n1. Water: use clean water and change daily\n2. Pruning: cut stems at 45 degrees to improve uptake\n3. Temperature: avoid direct sun, keep cool\n4. Add preservative: can extend vase life 3-5 days', 'rose,care,preservation,flowers', '/images/knowledge/rose_care.jpg', 'Flower Care', 'Florist Xiao Wang', 1234, 'PUBLISHED'),
('Watering Secrets for Succulents', 'Succulents are beloved for being cute and easy to grow...\n\nWatering rules:\n1. Dry then soak: water only after soil is fully dry\n2. Water thoroughly each time\n3. Seasonal changes: less in summer, control even more in winter\n4. Avoid standing water: pots must have drainage holes', 'succulent,watering,care,plants', '/images/knowledge/succulent_water.jpg', 'Plant Care', 'Garden Enthusiast', 2345, 'PUBLISHED'),
('Choosing and Placing Indoor Plants', 'Indoor plants beautify and purify the air...\n\nGood indoor picks:\n1. Monstera: shade tolerant, great for living rooms\n2. Golden pothos: strong purifier, good for bedrooms\n3. Fiddle leaf fig: Nordic style, fits studies\n4. Snake plant: absorbs formaldehyde, ideal for new homes', 'indoor,plants,placement,air quality', '/images/knowledge/indoor_plants.jpg', 'Plant Encyclopedia', 'Botanist', 3456, 'PUBLISHED'),
('Bouquet Wrapping Techniques', 'Great wrapping adds ceremony and beauty...\n\nSteps:\n1. Choose suitable wrapping paper\n2. Decide bouquet shape (round, cascade, etc.)\n3. Use spiral technique to secure stems\n4. Add decorative elements\n5. Tie a ribbon', 'bouquet,wrapping,floral,technique', '/images/knowledge/bouquet_wrap.jpg', 'Floral Tutorial', 'Florist Xiao Wang', 567, 'PUBLISHED'),
('Spring Flower Planting Guide', 'Spring is ideal for sowing; using the right methods matters...', 'spring,planting,flowers,gardening', '/images/knowledge/spring_planting.jpg', 'Seasonal Guide', 'Garden Enthusiast', 123, 'DRAFT');

-- ============================================
-- 16. System configuration
-- ============================================
INSERT INTO `system_configuration` (`config_key`, `config_value`, `description`, `category`) VALUES
('site_name', 'Flower Market', 'Site name', 'basic'),
('site_logo', '/images/logo.png', 'Site logo', 'basic'),
('site_description', 'Professional e-commerce platform for flowers and plants', 'Site description', 'basic'),
('customer_service_phone', '400-888-9999', 'Customer service phone', 'contact'),
('customer_service_email', 'service@flowermarket.com', 'Customer service email', 'contact'),
('check_in_points', '10', 'Daily check-in reward points', 'reward'),
('continuous_check_in_bonus', '5', 'Bonus points for continuous check-ins (from day 5)', 'reward'),
('free_shipping_amount', '99.00', 'Free shipping threshold', 'shipping'),
('default_shipping_fee', '10.00', 'Default shipping fee', 'shipping'),
('order_auto_cancel_minutes', '30', 'Auto-cancel unpaid orders after minutes', 'order'),
('order_auto_complete_days', '7', 'Auto-confirm receipt after shipped days', 'order'),
('review_auto_approve', 'false', 'Whether reviews auto-approve', 'review'),
('max_cart_items', '20', 'Maximum items in cart', 'cart'),
('product_image_max_size', '5', 'Max product image size (MB)', 'upload');

-- ============================================
-- Data initialization complete
-- ============================================

-- View inserted data stats
SELECT 'Customers' AS 'category', COUNT(*) AS 'count' FROM customers
UNION ALL
SELECT 'Merchants', COUNT(*) FROM merchants
UNION ALL
SELECT 'Products', COUNT(*) FROM products
UNION ALL
SELECT 'Orders', COUNT(*) FROM orders
UNION ALL
SELECT 'Reviews', COUNT(*) FROM product_reviews
UNION ALL
SELECT 'Coupons', COUNT(*) FROM coupons;