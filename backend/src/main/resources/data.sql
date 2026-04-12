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
-- 4.1 Suppliers
-- ============================================
INSERT INTO `suppliers` (`id`, `name`, `contact_person`, `phone`, `email`, `address`, `description`, `rating`, `status`) VALUES
                                                                                                                             (1, 'Kunming Highland Flower Base', 'Li Ming', '13500135001', 'liming@highlandflower.com', 'Dounan, Kunming, Yunnan', 'Largest fresh flower production base in Asia', 4.9, 'ACTIVE'),
                                                                                                                             (2, 'Ecuador Rose Garden', 'Maria Garcia', '00593-987654321', 'maria@ecuadorrose.com', 'Quito, Ecuador', 'Premium high-altitude rose exporter', 5.0, 'ACTIVE'),
                                                                                                                             (3, 'Guangdong Green Nursery', 'Chen Wei', '13500135003', 'chenwei@green-nursery.com', 'Shunde, Foshan, Guangdong', 'Specialized in indoor decorative plants', 4.7, 'ACTIVE');

-- ============================================
-- 5. Products
-- ============================================
INSERT INTO `flower_market`.`products`(`prod_id`, `merch_id`, `cat_id`, `supplier_id`, `name`, `price`, `stock`, `sales`, `main_image`, `images`, `description`, `flowering_period`, `care_difficulty`, `suitable_environment`, `floral_language`, `status`, `stock_status`, `create_date`, `update_date`) VALUES (1, 1, 3, 2, 'Red Rose Bouquet (11 stems)', 199.00, 50, 156, '/images/products/main/20260201_b8b0f1bbb5c34a8c919b52c65fb1d497.jpg', '[\"/images/products/detail/20260201_04f6c2bfb5df473eb0f994ed6ee6c5aa.jpg\"]', 'Imported Ecuadorian red roses, large blooms, vibrant color symbolizing passionate love', '7-12 days', 'MEDIUM', 'Room temp 18-25℃, avoid direct sunlight', 'Passionate love, I love you', 'ACTIVE', 'IN_STOCK', '2026-02-01 13:04:28', '2026-02-01 21:08:32');
INSERT INTO `flower_market`.`products`(`prod_id`, `merch_id`, `cat_id`, `supplier_id`, `name`, `price`, `stock`, `sales`, `main_image`, `images`, `description`, `flowering_period`, `care_difficulty`, `suitable_environment`, `floral_language`, `status`, `stock_status`, `create_date`, `update_date`) VALUES (2, 1, 3, 2, 'Champagne Rose Bouquet (19 stems)', 299.00, 30, 89, '/images/products/main/20260201_bd1af35d3e384d3aadcf9a0f48030bb6.jpg', '[\"/images/products/detail/20260201_6346aba182d140ed86f0cec86875f8b4.jpeg\"]', 'Elegant champagne roses, perfect to express gratitude and respect', '7-10 days', 'MEDIUM', 'Cool environment, fresh water every 2 days', 'I only fall in love with you', 'ACTIVE', 'IN_STOCK', '2026-02-01 13:04:28', '2026-02-01 21:06:50');
INSERT INTO `flower_market`.`products`(`prod_id`, `merch_id`, `cat_id`, `supplier_id`, `name`, `price`, `stock`, `sales`, `main_image`, `images`, `description`, `flowering_period`, `care_difficulty`, `suitable_environment`, `floral_language`, `status`, `stock_status`, `create_date`, `update_date`) VALUES (5, 2, 6, 1, 'White Peony Succulent', 29.90, 100, 234, '/images/products/main/20260201_514f1f4f3c8346d2b81b8af0c51935a7.jpg', '[\"/images/products/detail/20260201_7a8a2988e06643d89d63486d72e084be.png\"]', 'Cute white peony succulent; easy care, perfect for desks', 'Perennial', 'EASY', 'Bright light, minimal water once every 2 weeks', 'Purity and persistence', 'ACTIVE', 'IN_STOCK', '2026-02-01 13:04:28', '2026-02-01 21:16:02');
INSERT INTO `flower_market`.`products`(`prod_id`, `merch_id`, `cat_id`, `supplier_id`, `name`, `price`, `stock`, `sales`, `main_image`, `images`, `description`, `flowering_period`, `care_difficulty`, `suitable_environment`, `floral_language`, `status`, `stock_status`, `create_date`, `update_date`) VALUES (7, 2, 7, 3, 'Monstera Pot', 128.00, 45, 98, '/images/products/main/20260201_8cbf1ecc1c6842aca377920ddc3df756.jpg', '[\"/images/products/detail/20260201_a8f3ed9186314e3f95ec87c199e9bf48.png\"]', 'Scandi favorite; great air purifier, about 50cm tall', 'N/A (Foliage)', 'EASY', 'Indirect sunlight, keep soil slightly moist', 'Health and longevity', 'ACTIVE', 'IN_STOCK', '2026-02-01 13:04:28', '2026-02-01 21:45:54');
INSERT INTO `flower_market`.`products`(`prod_id`, `merch_id`, `cat_id`, `supplier_id`, `name`, `price`, `stock`, `sales`, `main_image`, `images`, `description`, `flowering_period`, `care_difficulty`, `suitable_environment`, `floral_language`, `status`, `stock_status`, `create_date`, `update_date`) VALUES (3, 1, 5, 1, 'Preserved Rose Glass Dome', 388.00, 19, 45, '/images/products/main/20260201_072b8ae13aa14c5481798f080ea9981e.jpg', '[\"/images/products/detail/20260201_5e02b3852901475185b6697e896c34e9.jpg\"]', 'Preserved rose in a delicate glass dome for lasting romance', '3-5 years', 'EASY', 'Dry environment, avoid direct sunlight and humidity', 'Eternal love', 'ACTIVE', 'IN_STOCK', '2026-02-01 13:04:28', '2026-02-01 21:12:19');
INSERT INTO `flower_market`.`products`(`prod_id`, `merch_id`, `cat_id`, `supplier_id`, `name`, `price`, `stock`, `sales`, `main_image`, `images`, `description`, `flowering_period`, `care_difficulty`, `suitable_environment`, `floral_language`, `status`, `stock_status`, `create_date`, `update_date`) VALUES (4, 1, 3, 1, 'Mixed Bouquet', 158.00, 8, 23, '/images/products/main/20260201_f8b78e83965548dbb7ca35594fbf6075.jpg', '[\"/images/products/detail/20260201_9bdf3d8f2bdc41f7b5c4f7330054567f.jpg\"]', 'Blend of roses, lilies, and baby’s breath for rich color', '5-8 days', 'MEDIUM', 'Room temperature, change water daily', 'Brilliant and colorful life', 'ACTIVE', 'LOW_STOCK', '2026-02-01 13:04:28', '2026-02-01 21:13:13');
INSERT INTO `flower_market`.`products`(`prod_id`, `merch_id`, `cat_id`, `supplier_id`, `name`, `price`, `stock`, `sales`, `main_image`, `images`, `description`, `flowering_period`, `care_difficulty`, `suitable_environment`, `floral_language`, `status`, `stock_status`, `create_date`, `update_date`) VALUES (6, 2, 6, 1, 'Succulent Combo (6 varieties)', 59.90, 80, 167, '/images/products/main/20260201_89e74a94ca4c4c96ae6c355e3ede5f8b.jpg', '[\"/images/products/detail/20260201_e63a68dadaab457a9ee9cc0ab0acceaf.png\"]', 'Six succulents with a decorative pot included', 'Perennial', 'EASY', 'Well-ventilated, bright light', 'Solid friendship', 'ACTIVE', 'IN_STOCK', '2026-02-01 13:04:28', '2026-02-01 21:16:33');
INSERT INTO `flower_market`.`products`(`prod_id`, `merch_id`, `cat_id`, `supplier_id`, `name`, `price`, `stock`, `sales`, `main_image`, `images`, `description`, `flowering_period`, `care_difficulty`, `suitable_environment`, `floral_language`, `status`, `stock_status`, `create_date`, `update_date`) VALUES (8, 2, 7, 3, 'Fiddle Leaf Fig Pot', 168.00, 25, 67, '/images/products/main/20260201_9d79df6217e2408aa522ceaac3730113.jpg', '[\"/images/products/detail/20260201_a7f9866e18dd4e1483dc76c16a345d67.png\"]', 'Large-leaf fiddle leaf fig, about 80cm, ideal for living rooms', 'N/A (Foliage)', 'MEDIUM', 'Bright indirect light, avoid overwatering', 'Grandeur and nobility', 'ACTIVE', 'IN_STOCK', '2026-02-01 13:04:28', '2026-02-01 21:43:15');
INSERT INTO `flower_market`.`products`(`prod_id`, `merch_id`, `cat_id`, `supplier_id`, `name`, `price`, `stock`, `sales`, `main_image`, `images`, `description`, `flowering_period`, `care_difficulty`, `suitable_environment`, `floral_language`, `status`, `stock_status`, `create_date`, `update_date`) VALUES (9, 2, 8, 3, 'Phalaenopsis Orchid Pot', 228.00, 15, 34, '/images/products/main/20260201_32079a3344f94057845c46c35d04c663.jpg', '[\"/images/products/detail/20260201_c04b922ed6914915af6de64a0fe788e0.png\"]', 'Premium orchid with up to 3-month bloom, gift ready', '2-3 months', 'HARD', 'Humidity 60%+, warm environment, special orchid soil', 'Nobility and elegance', 'ACTIVE', 'IN_STOCK', '2026-02-01 13:04:28', '2026-02-01 14:14:59');
INSERT INTO `flower_market`.`products`(`prod_id`, `merch_id`, `cat_id`, `supplier_id`, `name`, `price`, `stock`, `sales`, `main_image`, `images`, `description`, `flowering_period`, `care_difficulty`, `suitable_environment`, `floral_language`, `status`, `stock_status`, `create_date`, `update_date`) VALUES (10, 3, 3, 1, '520 Love Confession Bouquet', 520.00, 60, 201, '/images/products/main/20260201_5d9af596ff0d4fcd91f721c095b10da4.jpg', '[\"/images/products/detail/20260201_99fa7b9cbd1b4d86b4768741e09dae8d.png\"]', '52 red roses to say “I love you”', '7-12 days', 'MEDIUM', 'Room temperature, add nutrients to water', 'I love you forever', 'ACTIVE', 'IN_STOCK', '2026-02-01 13:04:28', '2026-02-01 21:53:30');
INSERT INTO `flower_market`.`products`(`prod_id`, `merch_id`, `cat_id`, `supplier_id`, `name`, `price`, `stock`, `sales`, `main_image`, `images`, `description`, `flowering_period`, `care_difficulty`, `suitable_environment`, `floral_language`, `status`, `stock_status`, `create_date`, `update_date`) VALUES (11, 3, 3, 1, 'Carnation Bouquet', 88.00, 120, 345, '/images/products/main/20260201_1eeff7fbcf404b138a0d2756263a3d00.jpg', '[\"/images/products/detail/20260201_f26ab7ca44e44af0812d544c4690c182.png\"]', 'Thankful carnations, a Mother’s Day bestseller', '10-14 days', 'EASY', 'Avoid high heat, keep water level consistent', 'Warmth and motherly love', 'ACTIVE', 'IN_STOCK', '2026-02-01 13:04:28', '2026-02-01 21:55:01');
INSERT INTO `flower_market`.`products`(`prod_id`, `merch_id`, `cat_id`, `supplier_id`, `name`, `price`, `stock`, `sales`, `main_image`, `images`, `description`, `flowering_period`, `care_difficulty`, `suitable_environment`, `floral_language`, `status`, `stock_status`, `create_date`, `update_date`) VALUES (12, 3, 4, 1, 'Sunflower Pot', 78.00, 50, 112, '/images/products/main/20260201_e88fd05b040a4c6f9142653d65965b70.jpg', '[\"/images/products/detail/20260201_839168bcf93b4f649ce7efdf6d520142.png\"]', 'Sunny sunflowers bringing positive energy', '1-2 weeks', 'EASY', 'Full sunlight, deep watering', 'Silent love and loyalty', 'ACTIVE', 'IN_STOCK', '2026-02-01 13:04:28', '2026-02-01 21:57:08');
INSERT INTO `flower_market`.`products`(`prod_id`, `merch_id`, `cat_id`, `supplier_id`, `name`, `price`, `stock`, `sales`, `main_image`, `images`, `description`, `flowering_period`, `care_difficulty`, `suitable_environment`, `floral_language`, `status`, `stock_status`, `create_date`, `update_date`) VALUES (13, 3, 3, 1, 'Lily Bouquet', 138.00, 0, 78, '/images/products/main/20260201_eee8133d1fa1491da608962ea001a3a1.jpg', '[\"/images/products/detail/20260201_21f38db1269b47fb86935c3109a10d0a.png\"]', 'Pure, elegant lilies suitable for many occasions', '10-15 days', 'MEDIUM', 'Remove pollen to avoid staining, fresh water', 'Purity and hundred years of harmony', 'ACTIVE', 'OUT_OF_STOCK', '2026-02-01 13:04:28', '2026-02-01 22:00:08');
INSERT INTO `flower_market`.`products`(`prod_id`, `merch_id`, `cat_id`, `supplier_id`, `name`, `price`, `stock`, `sales`, `main_image`, `images`, `description`, `flowering_period`, `care_difficulty`, `suitable_environment`, `floral_language`, `status`, `stock_status`, `create_date`, `update_date`) VALUES (14, 3, 3, 1, 'Delisted Bouquet', 99.00, 30, 12, '/images/products/main/20260201_ce6389d87a6843e2bf656af95f2137cb.jpg', '[\"/images/products/detail/20260201_292534b900164bba8cb97a06bea84fdf.png\"]', 'This product is delisted', 'N/A', 'EASY', 'N/A', 'N/A', 'ACTIVE', 'IN_STOCK', '2026-02-01 13:04:28', '2026-02-01 14:15:12');

-- ============================================
-- 6. Product traceability
-- ============================================
INSERT INTO `product_trackability` (`prod_id`, `origin`, `planting_method`, `picking_date`, `proc_date`, `certification`, `description`) VALUES
                                                                                                                                             (1, 'Ecuador', 'Greenhouse cultivation', '2025-12-10', '2025-12-11', 'ISO9001 quality certification', 'High-altitude 2800m roses with long vase life and vivid color'),
                                                                                                                                             (2, 'Ecuador', 'Greenhouse cultivation', '2025-12-09', '2025-12-10', 'ISO9001 quality certification', 'Selected champagne rose variety with elegant tone'),
                                                                                                                                             (3, 'Japan', 'Greenhouse cooling', '2025-12-15', '2025-12-17', 'JFA Standard', 'Preserved rose using advanced dehydration technology'),
                                                                                                                                             (4, 'Kunming, China', 'Modern Smart Greenhouse', '2026-01-05', '2026-01-06', 'Green Food Certification', 'A colorful blend of premium seasonal blossoms'),
                                                                                                                                             (5, 'Kunming, Yunnan', 'Open field cultivation', '2025-11-20', '2025-11-21', 'Organic certification', 'Yunnan highland succulents, premium quality'),
                                                                                                                                             (6, 'Kunming, Yunnan', 'Highland Greenhouse', '2025-11-22', '2025-11-23', 'Organic certification', 'Diverse succulent collection for enthusiasts'),
                                                                                                                                             (7, 'Foshan, Guangdong', 'Greenhouse cultivation', '2025-10-15', '2025-10-16', 'Green plant certification', 'Grown in professional nursery with guaranteed quality'),
                                                                                                                                             (8, 'Netherlands', 'Professional Nursery', '2025-09-20', '2025-09-25', 'NPS Quality Seal', 'Acclimatized indoor tree with lush green foliage'),
                                                                                                                                             (9, 'Taiwan', 'Temperature Controlled Lab', '2025-11-30', '2025-12-01', 'TGA Agricultural Standard', 'Prized orchid variety with extended blooming period'),
                                                                                                                                             (10, 'Quito, Ecuador', 'Premium Rose Base', '2026-02-10', '2026-02-11', 'Rose Origin Protection', 'Large-headed luxury red roses for confessions'),
                                                                                                                                             (11, 'Shandong, China', 'Base Cultivation', '2026-02-12', '2026-02-13', 'GAP Certification', 'Fresh carnations for gratitude expressions'),
                                                                                                                                             (12, 'Zhejiang, China', 'Farm field', '2026-02-14', '2026-02-15', 'Safe Farm Cert', 'Sunny sunflowers grown with sustainable methods');

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
(1, 1, 'Red Rose Bouquet (11 stems)', '/images/products/main/20260201_b8b0f1bbb5c34a8c919b52c65fb1d497.jpg', 2, 199.00, 398.00),
-- Items for order 2
(2, 7, 'Monstera Pot', '/images/products/main/20260201_bd1af35d3e384d3aadcf9a0f48030bb6.jpg', 1, 128.00, 128.00),
-- Items for order 3
(3, 10, '520 Love Confession Bouquet', '/images/products/main/20260201_072b8ae13aa14c5481798f080ea9981e.jpg', 1, 520.00, 520.00),
-- Items for order 4
(4, 1, 'Red Rose Bouquet (11 stems)', '/images/products/main/20260201_b8b0f1bbb5c34a8c919b52c65fb1d497.jpg', 1, 199.00, 199.00),
(4, 2, 'Champagne Rose Bouquet (19 stems)', '/images/products/main/20260201_514f1f4f3c8346d2b81b8af0c51935a7.jpg', 1, 299.00, 299.00),
(4, 5, 'White Peony Succulent', '/images/products/main/20260201_89e74a94ca4c4c96ae6c355e3ede5f8b.jpg', 2, 29.90, 59.80),
-- Items for order 5
(5, 8, 'Fiddle Leaf Fig Pot', '/images/products/main/20260201_8cbf1ecc1c6842aca377920ddc3df756.jpg', 1, 168.00, 168.00),
(5, 6, 'Succulent Combo (6 varieties)', '/images/products/main/20260201_9d79df6217e2408aa522ceaac3730113.jpg', 1, 59.90, 59.90),
-- Items for order 6
(6, 11, 'Carnation Bouquet', '/images/products/main/20260201_32079a3344f94057845c46c35d04c663.jpg', 1, 88.00, 88.00);

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
                                                                                                                                         (1, 1, 1, 5, 'Flowers were very fresh, packaging was great, delivery on time, my girlfriend loved it!', '[]', 1, 'APPROVED', '2025-12-11 17:00:00'),
                                                                                                                                         (3, 1, 4, 5, 'Huge rose blooms, bright color, great value', '[]', 1, 'APPROVED', '2025-12-13 11:00:00'),
                                                                                                                                         (2, 1, 1, 5, 'The best red roses I have ever purchased. Stunning!', '[]', 1, 'APPROVED', '2026-01-10 10:00:00'),
                                                                                                                                         (1, 2, 4, 5, 'Elegant color, wife was very happy with them.', '[]', 1, 'APPROVED', '2026-01-12 15:30:00'),
                                                                                                                                         (3, 2, 4, 4, 'Champagne roses are beautiful, just a bit pricey', '[]', 1, 'APPROVED', '2025-12-13 11:05:00'),
                                                                                                                                         (2, 3, 3, 5, 'Lasts so much longer than fresh flowers, looks real too.', '[]', 1, 'APPROVED', '2026-01-15 09:00:00'),
                                                                                                                                         (1, 5, 1, 5, 'Cute little succulent, perfect for my office desk.', '[]', 1, 'APPROVED', '2026-02-05 14:00:00'),
                                                                                                                                         (4, 7, 2, 4, 'Healthy plant, air feels fresher already.', '[]', 1, 'APPROVED', '2026-01-20 11:00:00'),
                                                                                                                                         (1, 11, 6, 3, 'Carnations were average and a little wilted', '[]', 1, 'PENDING', '2025-12-10 10:00:00');

-- ============================================
-- 12. Coupons
-- ============================================
INSERT INTO `coupons` (`coupon_id`, `merch_id`, `name`, `type`, `value`, `min_price`, `total_quantity`, `received_quantity`, `start_date`, `end_date`, `status`, `description`) VALUES
                                                                                                                                                                                    (1, NULL, 'New User Coupon', 'FIXED_AMOUNT', 50.00, 200.00, 1000, 234, '2025-12-01 00:00:00', '2026-12-31 23:59:59', 'ACTIVE', 'First order: save 50'),
                                                                                                                                                                                    (2, NULL, 'Christmas Full Reduction', 'FULL_REDUCTION', 100.00, 500.00, 500, 123, '2025-12-20 00:00:00', '2026-12-26 23:59:59', 'ACTIVE', 'Holiday deal: spend 500 save 100'),
                                                                                                                                                                                    (3, 1, 'Flower Shop 10% Off', 'DISCOUNT', 0.90, 100.00, 200, 89, '2025-12-01 00:00:00', '2026-12-31 23:59:59', 'ACTIVE', 'Exclusive 10% off at Flower Shop Expert'),
                                                                                                                                                                                    (4, 2, 'Green Plant 20% Off', 'DISCOUNT', 0.80, 150.00, 150, 67, '2025-12-01 00:00:00', '2026-12-31 23:59:59', 'ACTIVE', '20% off everything at Green Plant Living'),
                                                                                                                                                                                    (5, NULL, 'Expired Coupon', 'FIXED_AMOUNT', 30.00, 100.00, 100, 100, '2025-11-01 00:00:00', '2025-11-30 23:59:59', 'EXPIRED', 'November-only coupon');

-- ============================================
-- 13. Customer coupons
-- ============================================
INSERT INTO `customer_coupons` (`user_id`, `coupon_id`, `code`, `status`, `receive_date`, `used_date`, `order_id`) VALUES
                                                                                                                       (1, 1, 'NEW50-USER1-001', 'USED', '2025-12-10 09:00:00', '2025-12-10 10:30:00', 1),
                                                                                                                       (1, 2, 'XMAS100-USER1-002', 'UNUSED', '2025-12-12 10:00:00', NULL, NULL),
                                                                                                                       (1, 3, 'FLOWER90-USER1-003', 'UNUSED', '2026-03-01 10:00:00', NULL, NULL),
                                                                                                                       (1, 4, 'GREEN80-USER1-004', 'UNUSED', '2026-03-01 10:00:00', NULL, NULL),
                                                                                                                       (2, 1, 'NEW50-USER2-001', 'UNUSED', '2025-12-11 14:00:00', NULL, NULL),
                                                                                                                       (2, 2, 'XMAS100-USER2-002', 'UNUSED', '2026-03-01 11:00:00', NULL, NULL),
                                                                                                                       (2, 3, 'FLOWER90-USER2-003', 'UNUSED', '2026-03-01 11:00:00', NULL, NULL),
                                                                                                                       (2, 4, 'GREEN80-USER2-004', 'UNUSED', '2026-03-01 11:00:00', NULL, NULL),
                                                                                                                       (3, 1, 'NEW50-USER3-003', 'UNUSED', '2026-03-01 12:00:00', NULL, NULL),
                                                                                                                       (3, 2, 'XMAS100-USER3-001', 'USED', '2025-12-12 15:00:00', '2025-12-12 16:45:00', 4),
                                                                                                                       (3, 3, 'FLOWER90-USER3-002', 'UNUSED', '2025-12-11 11:00:00', NULL, NULL),
                                                                                                                       (3, 4, 'GREEN80-USER3-004', 'UNUSED', '2026-03-01 12:00:00', NULL, NULL),
                                                                                                                       (4, 1, 'NEW50-USER4-001', 'UNUSED', '2026-03-01 13:00:00', NULL, NULL),
                                                                                                                       (4, 2, 'XMAS100-USER4-002', 'UNUSED', '2026-03-01 13:00:00', NULL, NULL),
                                                                                                                       (4, 3, 'FLOWER90-USER4-003', 'UNUSED', '2026-03-01 13:00:00', NULL, NULL),
                                                                                                                       (4, 4, 'GREEN80-USER4-004', 'UNUSED', '2025-12-10 16:00:00', NULL, NULL),
                                                                                                                       (5, 1, 'NEW50-USER5-001', 'UNUSED', '2026-03-01 14:00:00', NULL, NULL),
                                                                                                                       (5, 2, 'XMAS100-USER5-002', 'UNUSED', '2026-03-01 14:00:00', NULL, NULL),
                                                                                                                       (5, 3, 'FLOWER90-USER5-003', 'UNUSED', '2026-03-01 14:00:00', NULL, NULL),
                                                                                                                       (5, 4, 'GREEN80-USER5-004', 'UNUSED', '2026-03-01 14:00:00', NULL, NULL);

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
-- 17. Additional Administrators (cover ACTIVE/INACTIVE status)
-- Password: 123456 (MD5: e10adc3949ba59abbe56e057f20f883e)
-- ============================================
INSERT INTO `administrators` (`admin_id`, `name`, `password`, `email`, `permission`, `status`) VALUES
(3, 'Inactive Admin', 'e10adc3949ba59abbe56e057f20f883e', 'inactive@flowermarket.com', 'ADMIN', 'INACTIVE'),
(4, 'Content Manager', 'e10adc3949ba59abbe56e057f20f883e', 'content@flowermarket.com', 'ADMIN', 'ACTIVE'),
(5, 'Order Manager', 'e10adc3949ba59abbe56e057f20f883e', 'order@flowermarket.com', 'ADMIN', 'ACTIVE');

-- ============================================
-- 18. Additional Suppliers (cover ACTIVE/INACTIVE/SUSPENDED status)
-- ============================================
INSERT INTO `suppliers` (`id`, `name`, `contact_person`, `phone`, `email`, `address`, `description`, `rating`, `status`) VALUES
(4, 'Yunnan Lily Garden', 'Wang Fang', '13500135004', 'wangfang@lilygarden.com', 'Kunming, Yunnan', 'Premium lily producer', 4.8, 'ACTIVE'),
(5, 'Dutch Tulip Farm', 'Jan Van', '0031-678901234', 'jan@dutchtulip.com', 'Amsterdam, Netherlands', 'Authentic Dutch tulips', 4.9, 'ACTIVE'),
(6, 'Inactive Supplier', 'Test Person', '13500135006', 'inactive@supplier.com', 'Test Address', 'This supplier is inactive', 3.5, 'INACTIVE'),
(7, 'Suspended Supplier', 'Bad Actor', '13500135007', 'suspended@supplier.com', 'Unknown', 'Suspended due to quality issues', 2.0, 'SUSPENDED');

-- ============================================
-- 19. Additional Merchants (cover all statuses)
-- ============================================
INSERT INTO `merchants` (`merch_id`, `name`, `email`, `password`, `phone`, `shop_logo`, `qualification`, `address`, `description`, `status`) VALUES
(6, 'Rose Garden Studio', 'flower6@merchant.com', 'e10adc3949ba59abbe56e057f20f883e', '13900139006', '/images/shop/flower6.jpg', '/images/qualification/cert6.jpg', '55 Flower Rd, Xihu, Hangzhou', 'Specializing in premium roses', 'ACTIVE'),
(7, 'Orchid Paradise', 'flower7@merchant.com', 'e10adc3949ba59abbe56e057f20f883e', '13900139007', '/images/shop/flower7.jpg', '/images/qualification/cert7.jpg', '88 Orchid Lane, Nanshan, Shenzhen', 'Exotic orchids from around the world', 'ACTIVE'),
(8, 'Wedding Florist', 'flower8@merchant.com', 'e10adc3949ba59abbe56e057f20f883e', '13900139008', '/images/shop/flower8.jpg', '/images/qualification/cert8.jpg', 'Wedding Plaza, Chaoyang, Beijing', 'Wedding bouquets and arrangements', 'ACTIVE'),
(9, 'Potted Plant World', 'flower9@merchant.com', 'e10adc3949ba59abbe56e057f20f883e', '13900139009', '/images/shop/flower9.jpg', '/images/qualification/cert9.jpg', '99 Plant Rd, Pudong, Shanghai', 'All kinds of potted plants', 'ACTIVE'),
(10, 'Seasonal Blooms', 'flower10@merchant.com', 'e10adc3949ba59abbe56e057f20f883e', '13900139010', '/images/shop/flower10.jpg', '/images/qualification/cert10.jpg', '12 Bloom Ave, Tianhe, Guangzhou', 'Fresh seasonal flowers', 'ACTIVE'),
(11, 'Pending Florist A', 'pending1@merchant.com', 'e10adc3949ba59abbe56e057f20f883e', '13900139011', NULL, NULL, '100 Pending Rd, Futian, Shenzhen', 'New florist waiting approval', 'PENDING'),
(12, 'Pending Florist B', 'pending2@merchant.com', 'e10adc3949ba59abbe56e057f20f883e', '13900139012', NULL, '/images/qualification/cert12.jpg', '200 New St, Xihu, Hangzhou', 'Another pending merchant', 'PENDING'),
(13, 'Suspended Florist', 'suspended@merchant.com', 'e10adc3949ba59abbe56e057f20f883e', '13900139013', '/images/shop/flower13.jpg', '/images/qualification/cert13.jpg', '300 Old Ave, Chaoyang, Beijing', 'Suspended due to violations', 'SUSPENDED'),
(14, 'Rejected Florist', 'rejected@merchant.com', 'e10adc3949ba59abbe56e057f20f883e', '13900139014', NULL, NULL, '400 Rejected Ln, Pudong, Shanghai', 'Application was rejected', 'REJECTED'),
(15, 'Dried Flowers Art', 'flower15@merchant.com', 'e10adc3949ba59abbe56e057f20f883e', '13900139015', '/images/shop/flower15.jpg', '/images/qualification/cert15.jpg', '50 Art St, Nanshan, Shenzhen', 'Beautiful dried flower arrangements', 'ACTIVE');

-- ============================================
-- 20. Additional Customers (45 new customers, ID 6-50)
-- ============================================
INSERT INTO `customers` (`user_id`, `name`, `email`, `phone`, `password`, `balance`, `points`, `level`, `gender`, `address`, `email_verified`) VALUES
-- VIP Customers (6-15)
(6, 'Chen Wei', 'chenwei@example.com', '13800138006', 'e10adc3949ba59abbe56e057f20f883e', 800.00, 120, 'VIP', 'Male', 'No. 1 Nanjing Rd, Shanghai', 1),
(7, 'Liu Yang', 'liuyang@example.com', '13800138007', 'e10adc3949ba59abbe56e057f20f883e', 650.00, 95, 'VIP', 'Female', 'No. 2 Zhongshan Ave, Guangzhou', 1),
(8, 'Wu Jing', 'wujing@example.com', '13800138008', 'e10adc3949ba59abbe56e057f20f883e', 900.00, 150, 'VIP', 'Female', 'No. 3 Renmin Rd, Chengdu', 1),
(9, 'Zheng Min', 'zhengmin@example.com', '13800138009', 'e10adc3949ba59abbe56e057f20f883e', 750.00, 88, 'VIP', 'Male', 'No. 4 Jiefang Rd, Wuhan', 1),
(10, 'Huang Lei', 'huanglei@example.com', '13800138010', 'e10adc3949ba59abbe56e057f20f883e', 500.00, 75, 'VIP', 'Male', 'No. 5 Changjiang Rd, Nanjing', 1),
(11, 'Lin Xiao', 'linxiao@example.com', '13800138011', 'e10adc3949ba59abbe56e057f20f883e', 600.00, 110, 'VIP', 'Female', 'No. 6 Huanghe Ave, Zhengzhou', 1),
(12, 'He Jun', 'hejun@example.com', '13800138012', 'e10adc3949ba59abbe56e057f20f883e', 850.00, 130, 'VIP', 'Male', 'No. 7 Heping Rd, Tianjin', 1),
(13, 'Gao Fei', 'gaofei@example.com', '13800138013', 'e10adc3949ba59abbe56e057f20f883e', 700.00, 92, 'VIP', 'Female', 'No. 8 Jianshe Rd, Xian, China', 1),
(14, 'Luo Ming', 'luoming@example.com', '13800138014', 'e10adc3949ba59abbe56e057f20f883e', 550.00, 68, 'VIP', 'Male', 'No. 9 Wenhua Rd, Changsha', 1),
(15, 'Xie Lan', 'xielan@example.com', '13800138015', 'e10adc3949ba59abbe56e057f20f883e', 680.00, 105, 'VIP', 'Female', 'No. 10 Keji Rd, Shenzhen', 1),
-- SVIP Customers (16-20)
(16, 'Tang Hua', 'tanghua@example.com', '13800138016', 'e10adc3949ba59abbe56e057f20f883e', 3000.00, 500, 'SVIP', 'Male', 'No. 11 Financial St, Beijing', 1),
(17, 'Deng Wei', 'dengwei@example.com', '13800138017', 'e10adc3949ba59abbe56e057f20f883e', 2500.00, 450, 'SVIP', 'Female', 'No. 12 Lujiazui, Shanghai', 1),
(18, 'Feng Yu', 'fengyu@example.com', '13800138018', 'e10adc3949ba59abbe56e057f20f883e', 3500.00, 600, 'SVIP', 'Male', 'No. 13 Zhujiang New Town, Guangzhou', 1),
(19, 'Jiang Ning', 'jiangning@example.com', '13800138019', 'e10adc3949ba59abbe56e057f20f883e', 2800.00, 480, 'SVIP', 'Female', 'No. 14 High-tech Zone, Shenzhen', 1),
(20, 'Xu Bo', 'xubo@example.com', '13800138020', 'e10adc3949ba59abbe56e057f20f883e', 3200.00, 520, 'SVIP', 'Male', 'No. 15 CBD, Hangzhou', 1),
-- NORMAL Customers with verified email (21-35)
(21, 'Ma Li', 'mali@example.com', '13800138021', 'e10adc3949ba59abbe56e057f20f883e', 200.00, 25, 'NORMAL', 'Female', 'No. 16 Xihu Rd, Hangzhou', 1),
(22, 'Song Qiang', 'songqiang@example.com', '13800138022', 'e10adc3949ba59abbe56e057f20f883e', 180.00, 15, 'NORMAL', 'Male', 'No. 17 Qiantang Rd, Hangzhou', 1),
(23, 'Yuan Fang', 'yuanfang@example.com', '13800138023', 'e10adc3949ba59abbe56e057f20f883e', 250.00, 30, 'NORMAL', 'Female', 'No. 18 Binjiang, Hangzhou', 1),
(24, 'Cao Jie', 'caojie@example.com', '13800138024', 'e10adc3949ba59abbe56e057f20f883e', 150.00, 10, 'NORMAL', 'Male', 'No. 19 Xiaoshan, Hangzhou', 1),
(25, 'Yang Ping', 'yangping@example.com', '13800138025', 'e10adc3949ba59abbe56e057f20f883e', 300.00, 35, 'NORMAL', 'Female', 'No. 20 Yuhang, Hangzhou', 1),
(26, 'Zhou Hong', 'zhouhong@example.com', '13800138026', 'e10adc3949ba59abbe56e057f20f883e', 220.00, 20, 'NORMAL', 'Male', 'No. 21 Gongshu, Hangzhou', 1),
(27, 'Wu Mei', 'wumei@example.com', '13800138027', 'e10adc3949ba59abbe56e057f20f883e', 280.00, 28, 'NORMAL', 'Female', 'No. 22 Jianggan, Hangzhou', 1),
(28, 'Sun Lei', 'sunlei@example.com', '13800138028', 'e10adc3949ba59abbe56e057f20f883e', 170.00, 12, 'NORMAL', 'Male', 'No. 23 Shangcheng, Hangzhou', 1),
(29, 'Zhu Lan', 'zhulan@example.com', '13800138029', 'e10adc3949ba59abbe56e057f20f883e', 320.00, 40, 'NORMAL', 'Female', 'No. 24 Xiacheng, Hangzhou', 1),
(30, 'Qin Hao', 'qinhao@example.com', '13800138030', 'e10adc3949ba59abbe56e057f20f883e', 190.00, 18, 'NORMAL', 'Male', 'No. 25 Jiangbei, Hangzhou', 1),
(31, 'Yu Xin', 'yuxin@example.com', '13800138031', 'e10adc3949ba59abbe56e057f20f883e', 260.00, 32, 'NORMAL', 'Female', 'No. 26 Fuyang, Hangzhou', 1),
(32, 'Guo Wei', 'guowei@example.com', '13800138032', 'e10adc3949ba59abbe56e057f20f883e', 210.00, 22, 'NORMAL', 'Male', 'No. 27 Linan, Hangzhou', 1),
(33, 'Pan Jing', 'panjing@example.com', '13800138033', 'e10adc3949ba59abbe56e057f20f883e', 240.00, 26, 'NORMAL', 'Female', 'No. 28 Tonglu, Hangzhou', 1),
(34, 'Tian Yu', 'tianyu@example.com', '13800138034', 'e10adc3949ba59abbe56e057f20f883e', 160.00, 14, 'NORMAL', 'Male', 'No. 29 Chunan, Hangzhou', 1),
(35, 'Jia Ming', 'jiaming@example.com', '13800138035', 'e10adc3949ba59abbe56e057f20f883e', 230.00, 24, 'NORMAL', 'Female', 'No. 30 Jiande, Hangzhou', 1),
-- NORMAL Customers without verified email (36-50)
(36, 'Han Bing', 'hanbing@example.com', '13800138036', 'e10adc3949ba59abbe56e057f20f883e', 100.00, 5, 'NORMAL', 'Male', 'No. 31 Test Rd, Beijing', 0),
(37, 'Fang Yun', 'fangyun@example.com', '13800138037', 'e10adc3949ba59abbe56e057f20f883e', 80.00, 3, 'NORMAL', 'Female', 'No. 32 Test Ave, Shanghai', 0),
(38, 'Tan Song', 'tansong@example.com', '13800138038', 'e10adc3949ba59abbe56e057f20f883e', 120.00, 8, 'NORMAL', 'Male', 'No. 33 Test St, Guangzhou', 0),
(39, 'Cui Na', 'cuina@example.com', '13800138039', 'e10adc3949ba59abbe56e057f20f883e', 90.00, 4, 'NORMAL', 'Female', 'No. 34 Test Ln, Shenzhen', 0),
(40, 'Kang Kai', 'kangkai@example.com', '13800138040', 'e10adc3949ba59abbe56e057f20f883e', 110.00, 6, 'NORMAL', 'Male', 'No. 35 Test Way, Chengdu', 0),
(41, 'Shi Jie', 'shijie@example.com', '13800138041', 'e10adc3949ba59abbe56e057f20f883e', 70.00, 2, 'NORMAL', 'Female', 'No. 36 Test Blvd, Wuhan', 0),
(42, 'Yao Qiang', 'yaoqiang@example.com', '13800138042', 'e10adc3949ba59abbe56e057f20f883e', 130.00, 9, 'NORMAL', 'Male', 'No. 37 Test Dr, Nanjing', 0),
(43, 'Tan Li', 'tanli@example.com', '13800138043', 'e10adc3949ba59abbe56e057f20f883e', 85.00, 3, 'NORMAL', 'Female', 'No. 38 Test Ct, Zhengzhou', 0),
(44, 'Long Wei', 'longwei@example.com', '13800138044', 'e10adc3949ba59abbe56e057f20f883e', 95.00, 5, 'NORMAL', 'Male', 'No. 39 Test Pl, Tianjin', 0),
(45, 'Lu Fang', 'lufang@example.com', '13800138045', 'e10adc3949ba59abbe56e057f20f883e', 75.00, 2, 'NORMAL', 'Female', 'No. 40 Test Sq, Xian, China', 0),
(46, 'Kong Ming', 'kongming@example.com', '13800138046', 'e10adc3949ba59abbe56e057f20f883e', 140.00, 11, 'NORMAL', 'Male', 'No. 41 Test Park, Changsha', 0),
(47, 'Bai Xue', 'baixue@example.com', '13800138047', 'e10adc3949ba59abbe56e057f20f883e', 60.00, 1, 'NORMAL', 'Female', 'No. 42 Test Garden, Hangzhou', 0),
(48, 'Qiu Shan', 'qiushan@example.com', '13800138048', 'e10adc3949ba59abbe56e057f20f883e', 105.00, 7, 'NORMAL', 'Male', 'No. 43 Test Hill, Suzhou', 0),
(49, 'Ye Qing', 'yeqing@example.com', '13800138049', 'e10adc3949ba59abbe56e057f20f883e', 115.00, 8, 'NORMAL', 'Female', 'No. 44 Test Lake, Kunming', 0),
(50, 'Zou Gang', 'zougang@example.com', '13800138050', 'e10adc3949ba59abbe56e057f20f883e', 125.00, 10, 'NORMAL', 'Male', 'No. 45 Test River, Dali', 0);

-- ============================================
-- 21. Additional Shopping Cart items
-- ============================================
INSERT INTO `shopping_cart` (`user_id`, `prod_id`, `quantity`, `selected`) VALUES
(6, 1, 1, 1),
(6, 3, 1, 1),
(7, 5, 2, 1),
(7, 7, 1, 0),
(8, 10, 1, 1),
(9, 2, 1, 1),
(9, 11, 2, 1),
(10, 6, 3, 1),
(11, 4, 1, 1),
(12, 9, 1, 1),
(13, 1, 2, 1),
(14, 7, 1, 1),
(15, 3, 1, 1),
(16, 10, 1, 1),
(16, 1, 2, 1),
(17, 2, 1, 1),
(18, 5, 5, 1),
(19, 8, 1, 1),
(20, 9, 1, 1),
(21, 11, 1, 1),
(22, 1, 1, 0),
(23, 7, 1, 1),
(24, 5, 2, 1),
(25, 6, 1, 1),
(36, 1, 1, 1);

-- ============================================
-- 24. Additional Product Favorites
-- ============================================
INSERT INTO `product_favorites` (`user_id`, `prod_id`, `fav_date`) VALUES
(6, 1, '2025-12-10 10:00:00'),
(6, 3, '2025-12-11 11:00:00'),
(6, 10, '2025-12-12 09:00:00'),
(7, 2, '2025-12-09 14:00:00'),
(7, 5, '2025-12-10 15:00:00'),
(8, 1, '2025-12-08 16:00:00'),
(8, 7, '2025-12-09 17:00:00'),
(8, 9, '2025-12-10 18:00:00'),
(9, 3, '2025-12-07 10:00:00'),
(9, 11, '2025-12-08 11:00:00'),
(10, 4, '2025-12-06 12:00:00'),
(10, 6, '2025-12-07 13:00:00'),
(11, 1, '2025-12-05 14:00:00'),
(11, 2, '2025-12-06 15:00:00'),
(12, 5, '2025-12-04 16:00:00'),
(12, 10, '2025-12-05 17:00:00'),
(13, 7, '2025-12-03 18:00:00'),
(14, 3, '2025-12-02 10:00:00'),
(15, 9, '2025-12-01 11:00:00'),
(16, 1, '2025-12-01 12:00:00'),
(16, 2, '2025-12-02 13:00:00'),
(16, 3, '2025-12-03 14:00:00'),
(17, 5, '2025-12-04 15:00:00'),
(17, 6, '2025-12-05 16:00:00'),
(18, 7, '2025-12-06 17:00:00'),
(18, 8, '2025-12-07 18:00:00'),
(19, 10, '2025-12-08 10:00:00'),
(20, 11, '2025-12-09 11:00:00'),
(21, 1, '2025-12-10 12:00:00'),
(22, 2, '2025-12-11 13:00:00');

-- ============================================
-- 25. Additional Product Reviews (cover all ratings and statuses)
-- ============================================
INSERT INTO `product_reviews` (`user_id`, `prod_id`, `order_id`, `rating`, `content`, `images`, `verified`, `status`, `create_date`) VALUES
-- 5-star reviews (APPROVED)
(6, 1, 7, 5, 'Absolutely stunning roses! My wife was over the moon. Will definitely order again!', '[]', 1, 'APPROVED', '2025-12-15 10:00:00'),
(7, 7, 8, 5, 'The monstera is so healthy and beautiful. Great packaging too!', '[]', 1, 'APPROVED', '2025-12-16 11:00:00'),
(8, 10, 9, 5, 'Perfect anniversary gift. The 520 bouquet exceeded my expectations!', '[]', 1, 'APPROVED', '2025-12-17 14:00:00'),
(16, 1, 10, 5, 'Premium quality roses for our corporate event. Highly recommend!', '[]', 1, 'APPROVED', '2025-12-18 16:00:00'),
(17, 5, 11, 5, 'Cute succulents, arrived in perfect condition', '[]', 1, 'APPROVED', '2025-12-19 10:00:00'),
(21, 11, 12, 5, 'Mom loved the carnations for her birthday!', '[]', 1, 'APPROVED', '2025-12-18 12:00:00'),
-- 4-star reviews (APPROVED)
(9, 1, 13, 4, 'Nice roses, but delivery was a bit late. Still beautiful though.', '[]', 1, 'APPROVED', '2025-12-18 09:00:00'),
(10, 7, 14, 4, 'Good plant, healthy leaves. Wish it was a bit bigger for the price.', '[]', 1, 'APPROVED', '2025-12-19 10:00:00'),
(11, 3, 17, 4, 'Beautiful preserved rose. The glass dome is elegant.', '[]', 1, 'APPROVED', '2025-12-20 11:00:00'),
(12, 2, 18, 4, 'Champagne roses are lovely, just wish they lasted longer', '[]', 1, 'APPROVED', '2025-12-20 12:00:00'),
-- 3-star reviews (APPROVED)
(13, 1, 33, 3, 'Roses were okay, some petals were slightly damaged during shipping', '[]', 1, 'APPROVED', '2025-12-19 14:00:00'),
(14, 9, 34, 3, 'Orchid is nice but smaller than expected', '[]', 1, 'APPROVED', '2025-12-19 15:00:00'),
-- 2-star reviews (APPROVED)
(15, 10, 35, 2, 'The bouquet was smaller than shown in pictures, disappointing', '[]', 1, 'APPROVED', '2025-12-18 16:00:00'),
-- 1-star reviews (APPROVED)
(18, 8, 36, 1, 'Plant arrived damaged, very disappointed', '[]', 1, 'APPROVED', '2025-12-19 09:00:00'),
-- PENDING reviews (awaiting moderation)
(19, 1, 19, 4, 'Good quality roses, fast delivery', '[]', 1, 'PENDING', '2025-12-20 10:00:00'),
(20, 10, 23, 5, 'Amazing bouquet for my proposal!', '[]', 1, 'PENDING', '2025-12-20 11:00:00'),
(23, 7, 20, 4, 'Monstera is beautiful and healthy', '[]', 1, 'PENDING', '2025-12-20 12:00:00'),
-- REJECTED reviews (inappropriate content)
(24, 1, 24, 1, 'This is spam content that should not appear', '[]', 0, 'REJECTED', '2025-12-20 13:00:00'),
(25, 5, 25, 5, 'Fake review with promotional links', '[]', 0, 'REJECTED', '2025-12-20 14:00:00'),
-- Unverified reviews (no order association)
(26, 1, NULL, 4, 'Heard great things about this shop from friends', '[]', 0, 'APPROVED', '2025-12-20 15:00:00'),
(27, 3, NULL, 5, 'Looking forward to ordering the preserved rose!', '[]', 0, 'APPROVED', '2025-12-20 16:00:00');

-- ============================================
-- 26. Additional Check-ins (simulate various patterns)
-- ============================================
INSERT INTO `check_ins` (`user_id`, `check_date`, `continuous_days`, `reward_points`) VALUES
-- User 6: 14 consecutive days (1-14)
(6, '2025-12-01', 1, 10),
(6, '2025-12-02', 2, 10),
(6, '2025-12-03', 3, 10),
(6, '2025-12-04', 4, 10),
(6, '2025-12-05', 5, 15),
(6, '2025-12-06', 6, 15),
(6, '2025-12-07', 7, 15),
(6, '2025-12-08', 8, 15),
(6, '2025-12-09', 9, 15),
(6, '2025-12-10', 10, 20),
(6, '2025-12-11', 11, 20),
(6, '2025-12-12', 12, 20),
(6, '2025-12-13', 13, 20),
(6, '2025-12-14', 14, 20),
-- User 7: 7 consecutive days
(7, '2025-12-07', 1, 10),
(7, '2025-12-08', 2, 10),
(7, '2025-12-09', 3, 10),
(7, '2025-12-10', 4, 10),
(7, '2025-12-11', 5, 15),
(7, '2025-12-12', 6, 15),
(7, '2025-12-13', 7, 15),
-- User 8: Broken streak (missed days)
(8, '2025-12-05', 1, 10),
(8, '2025-12-06', 2, 10),
(8, '2025-12-08', 1, 10),
(8, '2025-12-09', 2, 10),
(8, '2025-12-10', 3, 10),
(8, '2025-12-12', 1, 10),
-- User 9: Regular check-ins
(9, '2025-12-01', 1, 10),
(9, '2025-12-02', 2, 10),
(9, '2025-12-03', 3, 10),
(9, '2025-12-05', 1, 10),
(9, '2025-12-06', 2, 10),
(9, '2025-12-07', 3, 10),
(9, '2025-12-08', 4, 10),
(9, '2025-12-09', 5, 15),
-- User 10-20: Various patterns
(10, '2025-12-10', 1, 10),
(10, '2025-12-11', 2, 10),
(10, '2025-12-12', 3, 10),
(11, '2025-12-08', 1, 10),
(11, '2025-12-09', 2, 10),
(12, '2025-12-11', 1, 10),
(13, '2025-12-12', 1, 10),
(14, '2025-12-13', 1, 10),
(15, '2025-12-14', 1, 10),
-- SVIP users (16-20) with longer streaks
(16, '2025-12-01', 1, 10),
(16, '2025-12-02', 2, 10),
(16, '2025-12-03', 3, 10),
(16, '2025-12-04', 4, 10),
(16, '2025-12-05', 5, 15),
(16, '2025-12-06', 6, 15),
(16, '2025-12-07', 7, 15),
(16, '2025-12-08', 8, 15),
(16, '2025-12-09', 9, 15),
(16, '2025-12-10', 10, 20),
(16, '2025-12-11', 11, 20),
(16, '2025-12-12', 12, 20),
(16, '2025-12-13', 13, 20),
(16, '2025-12-14', 14, 20),
(17, '2025-12-05', 1, 10),
(17, '2025-12-06', 2, 10),
(17, '2025-12-07', 3, 10),
(17, '2025-12-08', 4, 10),
(17, '2025-12-09', 5, 15),
(17, '2025-12-10', 6, 15),
(18, '2025-12-10', 1, 10),
(18, '2025-12-11', 2, 10),
(18, '2025-12-12', 3, 10),
(18, '2025-12-13', 4, 10),
(18, '2025-12-14', 5, 15),
(19, '2025-12-12', 1, 10),
(19, '2025-12-13', 2, 10),
(19, '2025-12-14', 3, 10),
(20, '2025-12-13', 1, 10),
(20, '2025-12-14', 2, 10);

-- ============================================
-- 27. Additional Coupons
-- ============================================
INSERT INTO `coupons` (`coupon_id`, `merch_id`, `name`, `type`, `value`, `min_price`, `total_quantity`, `received_quantity`, `start_date`, `end_date`, `status`, `description`) VALUES
(6, 1, 'Rose Garden Special', 'DISCOUNT', 0.85, 150.00, 300, 45, '2025-12-01 00:00:00', '2026-06-30 23:59:59', 'ACTIVE', '15% off at Rose Garden Studio'),
(7, 2, 'Plant Lovers 30 Off', 'FIXED_AMOUNT', 30.00, 100.00, 500, 120, '2025-12-01 00:00:00', '2026-06-30 23:59:59', 'ACTIVE', 'Save 30 on orders over 100'),
(8, 3, 'Express Delivery Deal', 'FULL_REDUCTION', 50.00, 300.00, 200, 56, '2025-12-01 00:00:00', '2026-03-31 23:59:59', 'ACTIVE', 'Spend 300 save 50'),
(9, NULL, 'Spring Festival Bonus', 'FIXED_AMOUNT', 100.00, 500.00, 1000, 0, '2026-01-20 00:00:00', '2026-02-20 23:59:59', 'INACTIVE', 'Coming soon for Spring Festival'),
(10, 6, 'Rose Special', 'DISCOUNT', 0.80, 200.00, 150, 23, '2025-12-01 00:00:00', '2026-12-31 23:59:59', 'ACTIVE', '20% off premium roses'),
(11, 7, 'Orchid Week', 'FULL_REDUCTION', 80.00, 400.00, 100, 15, '2025-12-15 00:00:00', '2026-01-15 23:59:59', 'ACTIVE', 'Orchid special: spend 400 save 80');

-- ============================================
-- 28. Additional Customer Coupons
-- ============================================
INSERT INTO `customer_coupons` (`user_id`, `coupon_id`, `code`, `status`, `receive_date`, `used_date`, `order_id`) VALUES
-- New user coupons for additional customers
(6, 1, 'NEW50-USER6-001', 'UNUSED', '2025-12-10 10:00:00', NULL, NULL),
(6, 6, 'ROSE85-USER6-001', 'UNUSED', '2025-12-11 10:00:00', NULL, NULL),
(7, 1, 'NEW50-USER7-001', 'UNUSED', '2025-12-10 11:00:00', NULL, NULL),
(7, 7, 'PLANT30-USER7-001', 'UNUSED', '2025-12-11 11:00:00', NULL, NULL),
(8, 1, 'NEW50-USER8-001', 'UNUSED', '2025-12-10 12:00:00', NULL, NULL),
(8, 8, 'EXPRESS50-USER8-001', 'USED', '2025-12-15 12:00:00', '2025-12-15 11:00:00', 9),
(9, 1, 'NEW50-USER9-001', 'UNUSED', '2025-12-10 13:00:00', NULL, NULL),
(9, 6, 'ROSE85-USER9-001', 'UNUSED', '2025-12-11 13:00:00', NULL, NULL),
(10, 1, 'NEW50-USER10-001', 'UNUSED', '2025-12-10 14:00:00', NULL, NULL),
(10, 7, 'PLANT30-USER10-001', 'UNUSED', '2025-12-11 14:00:00', NULL, NULL),
-- SVIP customers get exclusive coupons
(16, 1, 'NEW50-USER16-001', 'USED', '2025-12-10 15:00:00', '2025-12-15 14:05:00', 10),
(16, 6, 'ROSE85-USER16-001', 'UNUSED', '2025-12-11 15:00:00', NULL, NULL),
(16, 10, 'ROSE80-USER16-001', 'UNUSED', '2025-12-12 15:00:00', NULL, NULL),
(17, 1, 'NEW50-USER17-001', 'UNUSED', '2025-12-10 16:00:00', NULL, NULL),
(17, 7, 'PLANT30-USER17-001', 'UNUSED', '2025-12-11 16:00:00', NULL, NULL),
(18, 1, 'NEW50-USER18-001', 'UNUSED', '2025-12-10 17:00:00', NULL, NULL),
(18, 8, 'EXPRESS50-USER18-001', 'UNUSED', '2025-12-11 17:00:00', NULL, NULL),
(19, 1, 'NEW50-USER19-001', 'UNUSED', '2025-12-10 18:00:00', NULL, NULL),
(20, 1, 'NEW50-USER20-001', 'UNUSED', '2025-12-10 19:00:00', NULL, NULL),
-- Some coupons for NORMAL users
(21, 1, 'NEW50-USER21-001', 'UNUSED', '2025-12-15 10:00:00', NULL, NULL),
(22, 1, 'NEW50-USER22-001', 'UNUSED', '2025-12-15 11:00:00', NULL, NULL),
(23, 1, 'NEW50-USER23-001', 'UNUSED', '2025-12-15 12:00:00', NULL, NULL),
(24, 1, 'NEW50-USER24-001', 'UNUSED', '2025-12-15 13:00:00', NULL, NULL),
(25, 1, 'NEW50-USER25-001', 'UNUSED', '2025-12-15 14:00:00', NULL, NULL),
-- Expired coupons
(6, 5, 'EXPIRED-USER6-001', 'EXPIRED', '2025-11-01 10:00:00', NULL, NULL),
(7, 5, 'EXPIRED-USER7-001', 'EXPIRED', '2025-11-01 11:00:00', NULL, NULL);

-- ============================================
-- 29. Knowledge Comments
-- ============================================
INSERT INTO `knowledge_comments` (`knowledge_id`, `user_id`, `user_name`, `content`, `status`, `create_date`) VALUES
-- Comments on article 1 (Rose care)
(1, 6, 'Chen Wei', 'Great tips! My roses lasted 2 weeks following these instructions.', 'APPROVED', '2025-12-10 10:00:00'),
(1, 7, 'Liu Yang', 'The 45-degree cut tip really works! Thank you!', 'APPROVED', '2025-12-11 11:00:00'),
(1, 8, 'Wu Jing', 'Adding preservative made a huge difference for my bouquet.', 'APPROVED', '2025-12-12 12:00:00'),
(1, 21, 'Ma Li', 'Very helpful article for beginners like me.', 'APPROVED', '2025-12-13 13:00:00'),
-- Comments on article 2 (Succulent watering)
(2, 9, 'Zheng Min', 'Finally understand why my succulents keep dying - overwatering!', 'APPROVED', '2025-12-10 14:00:00'),
(2, 10, 'Huang Lei', 'The dry then soak method saved my succulent collection.', 'APPROVED', '2025-12-11 15:00:00'),
(2, 11, 'Lin Xiao', 'Drainage holes are so important, learned this the hard way.', 'APPROVED', '2025-12-12 16:00:00'),
-- Comments on article 3 (Indoor plants)
(3, 12, 'He Jun', 'Monstera is indeed perfect for my living room!', 'APPROVED', '2025-12-10 17:00:00'),
(3, 13, 'Gao Fei', 'Snake plant is amazing for my new apartment.', 'APPROVED', '2025-12-11 18:00:00'),
(3, 22, 'Song Qiang', 'Golden pothos is so easy to care for, highly recommend.', 'APPROVED', '2025-12-12 19:00:00'),
-- Comments on article 4 (Bouquet wrapping)
(4, 14, 'Luo Ming', 'The spiral technique tutorial was super helpful!', 'APPROVED', '2025-12-10 20:00:00'),
(4, 15, 'Xie Lan', 'Now I can wrap bouquets like a pro. Thanks!', 'APPROVED', '2025-12-11 21:00:00'),
-- Comments on article 5 (Spring planting)
(5, 16, 'Tang Hua', 'Looking forward to spring planting season!', 'APPROVED', '2025-12-10 22:00:00'),
-- Pending comments
(1, 23, 'Yuan Fang', 'Question: How often should I change the water?', 'PENDING', '2025-12-14 10:00:00'),
(2, 24, 'Cao Jie', 'Can I use tap water for succulents?', 'PENDING', '2025-12-14 11:00:00'),
-- Rejected comments (spam/inappropriate)
(1, 36, 'Han Bing', 'Buy cheap flowers at www.spam.com', 'REJECTED', '2025-12-14 12:00:00'),
(3, 37, 'Fang Yun', 'Check out my flower shop!!!', 'REJECTED', '2025-12-14 13:00:00');

-- ============================================
-- 30. Additional Care Knowledge Articles
-- ============================================
INSERT INTO `care_knowledge` (`title`, `content`, `keywords`, `cover_image`, `category`, `author`, `view_count`, `status`) VALUES
('How to Choose Fresh Flowers', 'When buying fresh flowers, look for these signs of quality:\n\n1. Petals should be firm and vibrant\n2. Stems should be green and sturdy\n3. Avoid flowers with brown edges\n4. Check the water in the bucket - it should be clean', 'fresh flowers,quality,buying guide', '/images/knowledge/choose_fresh.jpg', 'Flower Care', 'Florist Xiao Wang', 890, 'PUBLISHED'),
('Winter Plant Care Guide', 'Winter brings unique challenges for plant care:\n\n1. Reduce watering frequency\n2. Move plants away from cold windows\n3. Increase humidity with pebble trays\n4. Avoid fertilizing during dormancy', 'winter,care,plants,seasonal', '/images/knowledge/winter_care.jpg', 'Seasonal Guide', 'Garden Enthusiast', 756, 'PUBLISHED'),
('DIY Flower Arrangement', 'Create stunning arrangements at home:\n\n1. Choose a theme or color palette\n2. Start with greenery as a base\n3. Add focal flowers (larger blooms)\n4. Fill in with accent flowers\n5. Finish with delicate filler flowers', 'DIY,arrangement,flowers,tutorial', '/images/knowledge/diy_arrangement.jpg', 'Floral Tutorial', 'Botanist', 1234, 'PUBLISHED'),
('Common Plant Diseases', 'Learn to identify and treat common plant problems:\n\n1. Root rot: Yellow leaves, mushy stems\n2. Powdery mildew: White dusty coating\n3. Spider mites: Tiny webs on leaves\n4. Aphids: Clusters on new growth', 'diseases,pests,plant health,treatment', '/images/knowledge/diseases.jpg', 'Plant Care', 'Botanist', 567, 'PUBLISHED'),
('Best Flowers for Gifting', 'Choose the perfect flowers for every occasion:\n\n- Roses: Love and romance\n- Lilies: Sympathy and elegance\n- Sunflowers: Joy and friendship\n- Carnations: Gratitude and motherhood\n- Orchids: Luxury and refinement', 'gifts,occasions,flowers,meaning', '/images/knowledge/gifting.jpg', 'Flower Encyclopedia', 'Florist Xiao Wang', 2345, 'PUBLISHED'),
('Summer Flower Care', 'Hot weather tips for keeping flowers fresh:\n\n1. Water more frequently\n2. Provide afternoon shade\n3. Mulch to retain moisture\n4. Deadhead spent blooms', 'summer,heat,care,watering', '/images/knowledge/summer_care.jpg', 'Seasonal Guide', 'Garden Enthusiast', 456, 'DRAFT');

-- ============================================
-- 21. Recent Orders for Sales Trend (Last 7 Days: 2026-04-06 to 2026-04-12)
-- These orders ensure Sales Trend chart displays data
-- ============================================
INSERT INTO `orders` (`id`, `order_no`, `user_id`, `merch_id`, `order_date`, `total_price`, `discount_amount`, `actual_price`, `address`, `receiver_name`, `receiver_phone`, `payment_status`, `payment_time`, `payment_method`, `status`, `delivery_time`, `completion_time`, `cancel_reason`, `remark`) VALUES
-- 2026-04-06 orders
(7, 'ORD202604060001', 1, 1, '2026-04-06 09:00:00', 398.00, 0.00, 398.00, 'No. 88 Jianguo Rd, Chaoyang, Beijing', 'Zhang San', '13800138001', 'PAID', '2026-04-06 09:05:00', 'ALIPAY', 'COMPLETED', '2026-04-06 14:00:00', '2026-04-08 10:00:00', NULL, NULL),
(8, 'ORD202604060002', 6, 2, '2026-04-06 11:00:00', 256.80, 0.00, 256.80, 'No. 1 Nanjing Rd, Shanghai', 'Chen Wei', '13800138006', 'PAID', '2026-04-06 11:05:00', 'WECHAT', 'COMPLETED', '2026-04-06 15:00:00', '2026-04-08 11:00:00', NULL, NULL),
(9, 'ORD202604060003', 16, 3, '2026-04-06 14:00:00', 520.00, 0.00, 520.00, 'No. 11 Financial St, Beijing', 'Tang Hua', '13800138016', 'PAID', '2026-04-06 14:05:00', 'BALANCE', 'COMPLETED', '2026-04-06 18:00:00', '2026-04-08 14:00:00', NULL, 'VIP order'),
-- 2026-04-07 orders
(10, 'ORD202604070001', 7, 1, '2026-04-07 10:00:00', 199.00, 0.00, 199.00, 'No. 2 Zhongshan Ave, Guangzhou', 'Liu Yang', '13800138007', 'PAID', '2026-04-07 10:05:00', 'ALIPAY', 'COMPLETED', '2026-04-07 15:00:00', '2026-04-09 10:00:00', NULL, NULL),
(11, 'ORD202604070002', 17, 2, '2026-04-07 13:00:00', 387.70, 0.00, 387.70, 'No. 12 Lujiazui, Shanghai', 'Deng Wei', '13800138017', 'PAID', '2026-04-07 13:05:00', 'WECHAT', 'SHIPPED', '2026-04-07 17:00:00', NULL, NULL, NULL),
(12, 'ORD202604070003', 21, 3, '2026-04-07 16:00:00', 88.00, 0.00, 88.00, 'No. 16 Xihu Rd, Hangzhou', 'Ma Li', '13800138021', 'PAID', '2026-04-07 16:05:00', 'ALIPAY', 'COMPLETED', '2026-04-07 19:00:00', '2026-04-09 11:00:00', NULL, NULL),
-- 2026-04-08 orders
(13, 'ORD202604080001', 8, 1, '2026-04-08 09:30:00', 497.00, 50.00, 447.00, 'No. 3 Renmin Rd, Chengdu', 'Wu Jing', '13800138008', 'PAID', '2026-04-08 09:35:00', 'ALIPAY', 'COMPLETED', '2026-04-08 14:00:00', '2026-04-10 10:00:00', NULL, 'Anniversary gift'),
(14, 'ORD202604080002', 18, 2, '2026-04-08 11:00:00', 658.00, 0.00, 658.00, 'No. 13 Zhujiang New Town, Guangzhou', 'Feng Yu', '13800138018', 'PAID', '2026-04-08 11:05:00', 'BALANCE', 'SHIPPED', '2026-04-08 16:00:00', NULL, NULL, NULL),
(15, 'ORD202604080003', 22, 3, '2026-04-08 14:30:00', 158.00, 0.00, 158.00, 'No. 17 Qiantang Rd, Hangzhou', 'Song Qiang', '13800138022', 'PAID', '2026-04-08 14:35:00', 'WECHAT', 'PROCESSING', NULL, NULL, NULL, NULL),
(16, 'ORD202604080004', 9, 1, '2026-04-08 17:00:00', 299.00, 0.00, 299.00, 'No. 4 Jiefang Rd, Wuhan', 'Zheng Min', '13800138009', 'PAID', '2026-04-08 17:05:00', 'ALIPAY', 'COMPLETED', '2026-04-08 20:00:00', '2026-04-10 14:00:00', NULL, NULL),
-- 2026-04-09 orders
(17, 'ORD202604090001', 10, 2, '2026-04-09 10:00:00', 326.80, 0.00, 326.80, 'No. 5 Changjiang Rd, Nanjing', 'Huang Lei', '13800138010', 'PAID', '2026-04-09 10:05:00', 'WECHAT', 'SHIPPED', '2026-04-09 15:00:00', NULL, NULL, NULL),
(18, 'ORD202604090002', 19, 3, '2026-04-09 12:00:00', 786.00, 100.00, 686.00, 'No. 14 High-tech Zone, Shenzhen', 'Jiang Ning', '13800138019', 'PAID', '2026-04-09 12:05:00', 'BALANCE', 'PROCESSING', NULL, NULL, NULL, 'Large corporate order'),
(19, 'ORD202604090003', 23, 1, '2026-04-09 15:00:00', 128.00, 0.00, 128.00, 'No. 18 Binjiang, Hangzhou', 'Yuan Fang', '13800138023', 'PAID', '2026-04-09 15:05:00', 'ALIPAY', 'COMPLETED', '2026-04-09 18:00:00', '2026-04-11 10:00:00', NULL, NULL),
(20, 'ORD202604090004', 11, 2, '2026-04-09 18:00:00', 447.80, 0.00, 447.80, 'No. 6 Huanghe Ave, Zhengzhou', 'Lin Xiao', '13800138011', 'PAID', '2026-04-09 18:05:00', 'WECHAT', 'PROCESSING', NULL, NULL, NULL, NULL),
-- 2026-04-10 orders
(21, 'ORD202604100001', 12, 3, '2026-04-10 09:00:00', 520.00, 0.00, 520.00, 'No. 7 Heping Rd, Tianjin', 'He Jun', '13800138012', 'PAID', '2026-04-10 09:05:00', 'ALIPAY', 'SHIPPED', '2026-04-10 14:00:00', NULL, NULL, NULL),
(22, 'ORD202604100002', 20, 1, '2026-04-10 11:00:00', 388.00, 50.00, 338.00, 'No. 15 CBD, Hangzhou', 'Xu Bo', '13800138020', 'PAID', '2026-04-10 11:05:00', 'BALANCE', 'PROCESSING', NULL, NULL, NULL, NULL),
(23, 'ORD202604100003', 24, 2, '2026-04-10 14:00:00', 228.00, 0.00, 228.00, 'No. 19 Xiaoshan, Hangzhou', 'Cao Jie', '13800138024', 'PAID', '2026-04-10 14:05:00', 'WECHAT', 'COMPLETED', '2026-04-10 18:00:00', '2026-04-12 09:00:00', NULL, NULL),
(24, 'ORD202604100004', 13, 3, '2026-04-10 16:00:00', 89.90, 0.00, 89.90, 'No. 8 Jianshe Rd, Xian, China', 'Gao Fei', '13800138013', 'PAID', '2026-04-10 16:05:00', 'ALIPAY', 'SHIPPED', '2026-04-10 19:00:00', NULL, NULL, NULL),
-- 2026-04-11 orders
(25, 'ORD202604110001', 14, 1, '2026-04-11 09:00:00', 398.00, 0.00, 398.00, 'No. 9 Wenhua Rd, Changsha', 'Luo Ming', '13800138014', 'PAID', '2026-04-11 09:05:00', 'WECHAT', 'PROCESSING', NULL, NULL, NULL, NULL),
(26, 'ORD202604110002', 25, 2, '2026-04-11 11:30:00', 168.00, 0.00, 168.00, 'No. 20 Yuhang, Hangzhou', 'Yang Ping', '13800138025', 'PAID', '2026-04-11 11:35:00', 'ALIPAY', 'SHIPPED', '2026-04-11 16:00:00', NULL, NULL, NULL),
(27, 'ORD202604110003', 26, 3, '2026-04-11 14:00:00', 520.00, 0.00, 520.00, 'No. 21 Gongshu, Hangzhou', 'Zhou Hong', '13800138026', 'PAID', '2026-04-11 14:05:00', 'BALANCE', 'PROCESSING', NULL, NULL, NULL, NULL),
(28, 'ORD202604110004', 15, 1, '2026-04-11 16:30:00', 299.00, 0.00, 299.00, 'No. 10 Keji Rd, Shenzhen', 'Xie Lan', '13800138015', 'PAID', '2026-04-11 16:35:00', 'ALIPAY', 'SHIPPED', '2026-04-11 19:00:00', NULL, NULL, NULL),
-- 2026-04-12 orders (today)
(29, 'ORD202604120001', 1, 2, '2026-04-12 08:00:00', 256.80, 0.00, 256.80, 'No. 88 Jianguo Rd, Chaoyang, Beijing', 'Zhang San', '13800138001', 'PAID', '2026-04-12 08:05:00', 'WECHAT', 'PROCESSING', NULL, NULL, NULL, 'Morning order'),
(30, 'ORD202604120002', 27, 3, '2026-04-12 10:00:00', 88.00, 0.00, 88.00, 'No. 22 Jianggan, Hangzhou', 'Wu Mei', '13800138027', 'PAID', '2026-04-12 10:05:00', 'ALIPAY', 'PENDING', NULL, NULL, NULL, NULL),
(31, 'ORD202604120003', 28, 1, '2026-04-12 11:00:00', 199.00, 0.00, 199.00, 'No. 23 Shangcheng, Hangzhou', 'Sun Lei', '13800138028', 'UNPAID', NULL, NULL, 'PENDING', NULL, NULL, NULL, 'New order'),
(32, 'ORD202604120004', 29, 2, '2026-04-12 13:00:00', 398.00, 0.00, 398.00, 'No. 24 Xiacheng, Hangzhou', 'Zhu Lan', '13800138029', 'PAID', '2026-04-12 13:05:00', 'WECHAT', 'PROCESSING', NULL, NULL, NULL, NULL),
(33, 'ORD202604120005', 30, 3, '2026-04-12 15:00:00', 520.00, 0.00, 520.00, 'No. 25 Jiangbei, Hangzhou', 'Qin Hao', '13800138030', 'UNPAID', NULL, NULL, 'PENDING', NULL, NULL, NULL, 'Afternoon order');

-- ============================================
-- 22. Order Items for Recent Orders
-- ============================================
INSERT INTO `order_items` (`order_id`, `prod_id`, `name`, `main_image`, `quantity`, `unit_price`, `total_price`) VALUES
-- Order 7 items
(7, 1, 'Red Rose Bouquet (11 stems)', '/images/products/main/20260201_b8b0f1bbb5c34a8c919b52c65fb1d497.jpg', 2, 199.00, 398.00),
-- Order 8 items
(8, 7, 'Monstera Pot', '/images/products/main/20260201_8cbf1ecc1c6842aca377920ddc3df756.jpg', 1, 128.00, 128.00),
(8, 5, 'White Peony Succulent', '/images/products/main/20260201_514f1f4f3c8346d2b81b8af0c51935a7.jpg', 1, 29.90, 29.90),
(8, 6, 'Succulent Combo (6 varieties)', '/images/products/main/20260201_89e74a94ca4c4c96ae6c355e3ede5f8b.jpg', 1, 59.90, 59.90),
-- Order 9 items
(9, 10, '520 Love Confession Bouquet', '/images/products/main/20260201_5d9af596ff0d4fcd91f721c095b10da4.jpg', 1, 520.00, 520.00),
-- Order 10 items
(10, 1, 'Red Rose Bouquet (11 stems)', '/images/products/main/20260201_b8b0f1bbb5c34a8c919b52c65fb1d497.jpg', 1, 199.00, 199.00),
-- Order 11 items
(11, 5, 'White Peony Succulent', '/images/products/main/20260201_514f1f4f3c8346d2b81b8af0c51935a7.jpg', 3, 29.90, 89.70),
(11, 6, 'Succulent Combo (6 varieties)', '/images/products/main/20260201_89e74a94ca4c4c96ae6c355e3ede5f8b.jpg', 5, 59.90, 299.50),
-- Order 12 items
(12, 11, 'Carnation Bouquet', '/images/products/main/20260201_1eeff7fbcf404b138a0d2756263a3d00.jpg', 1, 88.00, 88.00),
-- Order 13 items
(13, 1, 'Red Rose Bouquet (11 stems)', '/images/products/main/20260201_b8b0f1bbb5c34a8c919b52c65fb1d497.jpg', 1, 199.00, 199.00),
(13, 2, 'Champagne Rose Bouquet (19 stems)', '/images/products/main/20260201_bd1af35d3e384d3aadcf9a0f48030bb6.jpg', 1, 299.00, 299.00),
-- Order 14 items
(14, 10, '520 Love Confession Bouquet', '/images/products/main/20260201_5d9af596ff0d4fcd91f721c095b10da4.jpg', 1, 520.00, 520.00),
(14, 11, 'Carnation Bouquet', '/images/products/main/20260201_1eeff7fbcf404b138a0d2756263a3d00.jpg', 1, 88.00, 88.00),
-- Order 15 items
(15, 4, 'Mixed Bouquet', '/images/products/main/20260201_f8b78e83965548dbb7ca35594fbf6075.jpg', 1, 158.00, 158.00),
-- Order 16 items
(16, 2, 'Champagne Rose Bouquet (19 stems)', '/images/products/main/20260201_bd1af35d3e384d3aadcf9a0f48030bb6.jpg', 1, 299.00, 299.00),
-- Order 17 items
(17, 7, 'Monstera Pot', '/images/products/main/20260201_8cbf1ecc1c6842aca377920ddc3df756.jpg', 1, 128.00, 128.00),
(17, 5, 'White Peony Succulent', '/images/products/main/20260201_514f1f4f3c8346d2b81b8af0c51935a7.jpg', 2, 29.90, 59.80),
(17, 6, 'Succulent Combo (6 varieties)', '/images/products/main/20260201_89e74a94ca4c4c96ae6c355e3ede5f8b.jpg', 1, 59.90, 59.90),
-- Order 18 items
(18, 1, 'Red Rose Bouquet (11 stems)', '/images/products/main/20260201_b8b0f1bbb5c34a8c919b52c65fb1d497.jpg', 2, 199.00, 398.00),
(18, 2, 'Champagne Rose Bouquet (19 stems)', '/images/products/main/20260201_bd1af35d3e384d3aadcf9a0f48030bb6.jpg', 1, 299.00, 299.00),
(18, 3, 'Preserved Rose Glass Dome', '/images/products/main/20260201_072b8ae13aa14c5481798f080ea9981e.jpg', 1, 388.00, 388.00),
-- Order 19 items
(19, 7, 'Monstera Pot', '/images/products/main/20260201_8cbf1ecc1c6842aca377920ddc3df756.jpg', 1, 128.00, 128.00),
-- Order 20 items
(20, 3, 'Preserved Rose Glass Dome', '/images/products/main/20260201_072b8ae13aa14c5481798f080ea9981e.jpg', 1, 388.00, 388.00),
(20, 5, 'White Peony Succulent', '/images/products/main/20260201_514f1f4f3c8346d2b81b8af0c51935a7.jpg', 2, 29.90, 59.80),
-- Order 21 items
(21, 10, '520 Love Confession Bouquet', '/images/products/main/20260201_5d9af596ff0d4fcd91f721c095b10da4.jpg', 1, 520.00, 520.00),
-- Order 22 items
(22, 3, 'Preserved Rose Glass Dome', '/images/products/main/20260201_072b8ae13aa14c5481798f080ea9981e.jpg', 1, 388.00, 388.00),
-- Order 23 items
(23, 9, 'Phalaenopsis Orchid Pot', '/images/products/main/20260201_32079a3344f94057845c46c35d04c663.jpg', 1, 228.00, 228.00),
-- Order 24 items
(24, 5, 'White Peony Succulent', '/images/products/main/20260201_514f1f4f3c8346d2b81b8af0c51935a7.jpg', 3, 29.90, 89.70),
-- Order 25 items
(25, 1, 'Red Rose Bouquet (11 stems)', '/images/products/main/20260201_b8b0f1bbb5c34a8c919b52c65fb1d497.jpg', 2, 199.00, 398.00),
-- Order 26 items
(26, 8, 'Fiddle Leaf Fig Pot', '/images/products/main/20260201_9d79df6217e2408aa522ceaac3730113.jpg', 1, 168.00, 168.00),
-- Order 27 items
(27, 10, '520 Love Confession Bouquet', '/images/products/main/20260201_5d9af596ff0d4fcd91f721c095b10da4.jpg', 1, 520.00, 520.00),
-- Order 28 items
(28, 2, 'Champagne Rose Bouquet (19 stems)', '/images/products/main/20260201_bd1af35d3e384d3aadcf9a0f48030bb6.jpg', 1, 299.00, 299.00),
-- Order 29 items
(29, 7, 'Monstera Pot', '/images/products/main/20260201_8cbf1ecc1c6842aca377920ddc3df756.jpg', 1, 128.00, 128.00),
(29, 5, 'White Peony Succulent', '/images/products/main/20260201_514f1f4f3c8346d2b81b8af0c51935a7.jpg', 1, 29.90, 29.90),
(29, 6, 'Succulent Combo (6 varieties)', '/images/products/main/20260201_89e74a94ca4c4c96ae6c355e3ede5f8b.jpg', 1, 59.90, 59.90),
-- Order 30 items
(30, 11, 'Carnation Bouquet', '/images/products/main/20260201_1eeff7fbcf404b138a0d2756263a3d00.jpg', 1, 88.00, 88.00),
-- Order 31 items
(31, 1, 'Red Rose Bouquet (11 stems)', '/images/products/main/20260201_b8b0f1bbb5c34a8c919b52c65fb1d497.jpg', 1, 199.00, 199.00),
-- Order 32 items
(32, 1, 'Red Rose Bouquet (11 stems)', '/images/products/main/20260201_b8b0f1bbb5c34a8c919b52c65fb1d497.jpg', 2, 199.00, 398.00),
-- Order 33 items
(33, 10, '520 Love Confession Bouquet', '/images/products/main/20260201_5d9af596ff0d4fcd91f721c095b10da4.jpg', 1, 520.00, 520.00);

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