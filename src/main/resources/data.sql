-- ============================================
-- 鲜花市场数据库初始化测试数据
-- 注意：密码使用MD5加密，密码与用户名一致
-- ============================================

USE `flower_market`;

-- ============================================
-- 清空现有数据（按照外键依赖顺序）
-- ============================================
SET FOREIGN_KEY_CHECKS = 0;

-- 按正确顺序清空表数据
TRUNCATE TABLE `customer_coupons`;
TRUNCATE TABLE `coupon_coupons`;
TRUNCATE TABLE `check_ins`;
TRUNCATE TABLE `order_items`;
TRUNCATE TABLE `orders`;
TRUNCATE TABLE `shopping_cart`;
TRUNCATE TABLE `product_reviews`;
TRUNCATE TABLE `product_favorites`;
TRUNCATE TABLE `product_trackability`;
TRUNCATE TABLE `products`;
TRUNCATE TABLE `product_categories`;
TRUNCATE TABLE `care_knowledge`;
TRUNCATE TABLE `coupons`;
TRUNCATE TABLE `merchants`;
TRUNCATE TABLE `customers`;
TRUNCATE TABLE `administrators`;
TRUNCATE TABLE `system_configuration`;

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================
-- 1. 插入管理员数据
-- 密码：admin（MD5: 21232f297a57a5a743894a0e4a801fc3）
-- 密码：admin123（MD5: 0192023a7bbd73250516f069df18b500）
-- ============================================
INSERT INTO `administrators` (`admin_id`, `name`, `password`, `email`, `permission`, `status`) VALUES
(1, '超级管理员', 'e10adc3949ba59abbe56e057f20f883e', 'admin@flowermarket.com', 'SUPER_ADMIN', 'ACTIVE'),
(2, '普通管理员', 'e10adc3949ba59abbe56e057f20f883e', 'admin2@flowermarket.com', 'ADMIN', 'ACTIVE');

-- ============================================
-- 2. 插入顾客数据
-- 密码：123456（MD5加密）
-- ============================================
INSERT INTO `customers` (`user_id`, `name`, `email`, `phone`, `password`, `balance`, `points`, `level`, `gender`, `address`, `email_verified`) VALUES
-- 密码: 123456, MD5: e10adc3949ba59abbe56e057f20f883e
-- 张三签到7次，积分：10+10+10+10+15+15+15=85
(1, '张三', 'zhangsan@example.com', '13800138001', 'e10adc3949ba59abbe56e057f20f883e', 1000.00, 85, 'VIP', '男', '北京市朝阳区建国路88号', 1),
-- 密码: 123456, MD5: e10adc3949ba59abbe56e057f20f883e
-- 李四签到2次，积分：10+10=20
(2, '李四', 'lisi@example.com', '13800138002', 'e10adc3949ba59abbe56e057f20f883e', 500.50, 20, 'NORMAL', '女', '上海市浦东新区世纪大道100号', 1),
-- 密码: 123456, MD5: e10adc3949ba59abbe56e057f20f883e
-- 王五签到2次，积分：10+10=20
(3, '王五', 'wangwu@example.com', '13800138003', 'e10adc3949ba59abbe56e057f20f883e', 2000.00, 20, 'SVIP', '男', '广州市天河区天河路123号', 1),
-- 密码: 123456, MD5: e10adc3949ba59abbe56e057f20f883e
(4, '赵六', 'zhaoliu@example.com', '13800138004', 'e10adc3949ba59abbe56e057f20f883e', 300.00, 0, 'NORMAL', '女', '深圳市南山区科技园路66号', 1),
-- sunqi 的MD5: e10adc3949ba59abbe56e057f20f883e
(5, '孙七', 'sunqi@example.com', '13800138005', 'e10adc3949ba59abbe56e057f20f883e', 150.00, 0, 'NORMAL', '男', '成都市武侯区人民南路55号', 0);

-- ============================================
-- 3. 插入商家数据
-- 密码与商家名称拼音一致（MD5加密）
-- ============================================
INSERT INTO `merchants` (`merch_id`, `name`, `email`, `password`, `phone`, `shop_logo`, `qualification`, `address`, `description`, `status`) VALUES
-- huadianzhuangjia 的MD5: 5e5d3c8d4c8e8f0e7c9a8b5c6d7e8f9a（示例，实际使用 flower1）
-- flower1 的MD5: e10adc3949ba59abbe56e057f20f883e
(1, '花店专家', 'flower1@merchant.com', 'e10adc3949ba59abbe56e057f20f883e', '13900139001', '/images/shop/flower1.jpg', '/images/qualification/cert1.jpg', '北京市朝阳区花卉市场A区1号', '专注高端鲜花定制，10年经验', 'ACTIVE'),
-- flower2 的MD5: e10adc3949ba59abbe56e057f20f883e
(2, '绿植生活馆', 'flower2@merchant.com', 'e10adc3949ba59abbe56e057f20f883e', '13900139002', '/images/shop/flower2.jpg', '/images/qualification/cert2.jpg', '上海市徐汇区植物园路18号', '各类绿植盆栽，呵护您的绿色生活', 'ACTIVE'),
-- flower3 的MD5: cf2b8b4c9f4f3e2e1c8b7d6e5f4e3d2c
-- 使用简化的MD5: e10adc3949ba59abbe56e057f20f883e (123456)
(3, '鲜花速递', 'flower3@merchant.com', 'e10adc3949ba59abbe56e057f20f883e', '13900139003', '/images/shop/flower3.jpg', '/images/qualification/cert3.jpg', '广州市天河区花城大道200号', '全城配送，2小时送达', 'ACTIVE'),
-- merchant4 的MD5: e10adc3949ba59abbe56e057f20f883e
(4, '待审核商家', 'merchant4@merchant.com', 'e10adc3949ba59abbe56e057f20f883e', '13900139004', NULL, NULL, '深圳市福田区华强北路99号', '新注册商家，等待审核', 'PENDING'),
-- merchant5 的MD5: e10adc3949ba59abbe56e057f20f883e
(5, '暂停营业商家', 'merchant5@merchant.com', 'e10adc3949ba59abbe56e057f20f883e', '13900139005', '/images/shop/flower5.jpg', '/images/qualification/cert5.jpg', '杭州市西湖区文三路77号', '因故暂停营业', 'SUSPENDED');

-- ============================================
-- 4. 插入产品分类数据
-- ============================================
INSERT INTO `product_categories` (`cate_id`, `name`, `parent_id`, `sort_order`, `icon`, `description`) VALUES
(1, '鲜花', 0, 1, '/images/category/fresh.png', '新鲜鲜花，每日配送'),
(2, '绿植', 0, 2, '/images/category/plant.png', '室内外绿植，净化空气'),
(3, '花束', 1, 1, '/images/category/bouquet.png', '精美花束，送礼首选'),
(4, '盆栽鲜花', 1, 2, '/images/category/potted.png', '鲜花盆栽，持久观赏'),
(5, '永生花', 1, 3, '/images/category/preserved.png', '永不凋谢的美丽'),
(6, '多肉植物', 2, 1, '/images/category/succulent.png', '萌萌多肉，易于养护'),
(7, '观叶植物', 2, 2, '/images/category/foliage.png', '绿叶葱茏，生机盎然'),
(8, '花卉盆栽', 2, 3, '/images/category/flowering.png', '开花植物，四季芬芳');

-- ============================================
-- 5. 插入产品数据
-- ============================================
INSERT INTO `products` (`prod_id`, `merch_id`, `cat_id`, `name`, `price`, `stock`, `sales`, `main_image`, `images`, `description`, `status`, `stock_status`) VALUES
-- 商家1的产品
(1, 1, 3, '红玫瑰花束（11支）', 199.00, 50, 156, '/images/products/rose_red_11.jpg', '["\/images\/products\/rose_red_11_1.jpg", "\/images\/products\/rose_red_11_2.jpg"]', '精选厄瓜多尔进口红玫瑰，花朵硕大，颜色鲜艳，象征热烈的爱', 'ACTIVE', 'IN_STOCK'),
(2, 1, 3, '香槟玫瑰花束（19支）', 299.00, 30, 89, '/images/products/rose_champagne_19.jpg', '["\/images\/products\/rose_champagne_19_1.jpg"]', '香槟色玫瑰，优雅高贵，适合表达感激与尊重', 'ACTIVE', 'IN_STOCK'),
(3, 1, 5, '永生花玻璃罩', 388.00, 20, 45, '/images/products/preserved_dome.jpg', '["\/images\/products\/preserved_dome_1.jpg", "\/images\/products\/preserved_dome_2.jpg"]', '永不凋谢的玫瑰，配以精致玻璃罩，浪漫永恒', 'ACTIVE', 'IN_STOCK'),
(4, 1, 3, '混搭花束', 158.00, 8, 23, '/images/products/mixed_bouquet.jpg', '["\/images\/products\/mixed_bouquet_1.jpg"]', '玫瑰、百合、满天星混搭，色彩丰富', 'ACTIVE', 'LOW_STOCK'),

-- 商家2的产品
(5, 2, 6, '白牡丹多肉', 29.90, 100, 234, '/images/products/succulent_white.jpg', '["\/images\/products\/succulent_white_1.jpg"]', '萌萌的白牡丹多肉，易养活，办公桌必备', 'ACTIVE', 'IN_STOCK'),
(6, 2, 6, '十二卷多肉组合', 59.90, 80, 167, '/images/products/succulent_combo.jpg', '["\/images\/products\/succulent_combo_1.jpg", "\/images\/products\/succulent_combo_2.jpg"]', '6种不同多肉组合，含精美花盆', 'ACTIVE', 'IN_STOCK'),
(7, 2, 7, '龟背竹盆栽', 128.00, 45, 98, '/images/products/monstera.jpg', '["\/images\/products\/monstera_1.jpg"]', '北欧风格首选，净化空气效果好，高度约50cm', 'ACTIVE', 'IN_STOCK'),
(8, 2, 7, '琴叶榕盆栽', 168.00, 25, 67, '/images/products/ficus_lyrata.jpg', '["\/images\/products\/ficus_lyrata_1.jpg"]', '大叶琴叶榕，高度约80cm，适合客厅摆放', 'ACTIVE', 'IN_STOCK'),
(9, 2, 8, '蝴蝶兰盆栽', 228.00, 15, 34, '/images/products/phalaenopsis.jpg', '["\/images\/products\/phalaenopsis_1.jpg"]', '高档蝴蝶兰，花期长达3个月，送礼佳品', 'ACTIVE', 'IN_STOCK'),

-- 商家3的产品
(10, 3, 3, '520表白花束', 520.00, 60, 201, '/images/products/love_520.jpg', '["\/images\/products\/love_520_1.jpg", "\/images\/products\/love_520_2.jpg"]', '52支红玫瑰，我爱你的浪漫表白', 'ACTIVE', 'IN_STOCK'),
(11, 3, 3, '康乃馨花束', 88.00, 120, 345, '/images/products/carnation.jpg', '["\/images\/products\/carnation_1.jpg"]', '感恩母亲，康乃馨花束，母亲节热销', 'ACTIVE', 'IN_STOCK'),
(12, 3, 4, '向日葵盆栽', 78.00, 50, 112, '/images/products/sunflower_pot.jpg', '["\/images\/products\/sunflower_pot_1.jpg"]', '阳光向上的向日葵，带来正能量', 'ACTIVE', 'IN_STOCK'),
(13, 3, 3, '百合花束', 138.00, 0, 78, '/images/products/lily.jpg', '["\/images\/products\/lily_1.jpg"]', '纯洁高雅的百合，适合各种场合', 'ACTIVE', 'OUT_OF_STOCK'),
(14, 3, 3, '已下架花束', 99.00, 30, 12, '/images/products/inactive.jpg', '[]', '此产品已下架', 'INACTIVE', 'IN_STOCK');

-- ============================================
-- 6. 插入产品溯源信息
-- ============================================
INSERT INTO `product_trackability` (`prod_id`, `origin`, `planting_method`, `picking_date`, `proc_date`, `certification`, `description`) VALUES
(1, '厄瓜多尔', '温室大棚种植', '2025-12-10', '2025-12-11', 'ISO9001质量认证', '海拔2800米高原玫瑰，花期长、颜色艳'),
(2, '厄瓜多尔', '温室大棚种植', '2025-12-09', '2025-12-10', 'ISO9001质量认证', '精选香槟色玫瑰品种，色泽优雅'),
(5, '云南昆明', '露地种植', '2025-11-20', '2025-11-21', '有机认证', '云南高原多肉，品质优良'),
(7, '广东佛山', '温室培育', '2025-10-15', '2025-10-16', '绿色植物认证', '专业苗圃培育，品质保证');

-- ============================================
-- 7. 插入购物车数据
-- ============================================
INSERT INTO `shopping_cart` (`user_id`, `prod_id`, `quantity`, `selected`) VALUES
(1, 1, 2, 1),
(1, 5, 3, 1),
(2, 7, 1, 1),
(2, 10, 1, 0),
(3, 3, 1, 1);

-- ============================================
-- 8. 插入订单数据
-- ============================================
INSERT INTO `orders` (`id`, `order_no`, `user_id`, `merch_id`, `order_date`, `total_price`, `discount_amount`, `actual_price`, `address`, `receiver_name`, `receiver_phone`, `payment_status`, `payment_time`, `payment_method`, `status`, `delivery_time`, `completion_time`, `remark`) VALUES
(1, 'ORD202512130001', 1, 1, '2025-12-10 10:30:00', 398.00, 50.00, 348.00, '北京市朝阳区建国路88号', '张三', '13800138001', 'PAID', '2025-12-10 10:32:00', 'ALIPAY', 'COMPLETED', '2025-12-10 14:00:00', '2025-12-11 16:00:00', '请在12点前送达'),
(2, 'ORD202512130002', 1, 2, '2025-12-11 15:20:00', 128.00, 0.00, 128.00, '北京市朝阳区建国路88号', '张三', '13800138001', 'PAID', '2025-12-11 15:22:00', 'WECHAT', 'SHIPPED', '2025-12-11 18:00:00', NULL, NULL),
(3, 'ORD202512130003', 2, 3, '2025-12-12 09:15:00', 520.00, 0.00, 520.00, '上海市浦东新区世纪大道100号', '李四', '13800138002', 'PAID', '2025-12-12 09:17:00', 'BALANCE', 'PROCESSING', NULL, NULL, '送货时请提前电话联系'),
(4, 'ORD202512130004', 3, 1, '2025-12-12 16:45:00', 597.00, 100.00, 497.00, '广州市天河区天河路123号', '王五', '13800138003', 'PAID', '2025-12-12 16:50:00', 'ALIPAY', 'COMPLETED', '2025-12-12 20:00:00', '2025-12-13 10:00:00', NULL),
(5, 'ORD202512130005', 4, 2, '2025-12-13 08:30:00', 256.00, 0.00, 256.00, '深圳市南山区科技园路66号', '赵六', '13800138004', 'UNPAID', NULL, NULL, 'PENDING', NULL, NULL, NULL),
(6, 'ORD202512130006', 1, 3, '2025-12-09 14:20:00', 88.00, 0.00, 88.00, '北京市朝阳区建国路88号', '张三', '13800138001', 'PAID', '2025-12-09 14:22:00', 'WECHAT', 'CANCELLED', NULL, NULL, '临时有事，不需要了');

-- ============================================
-- 9. 插入订单项数据
-- ============================================
INSERT INTO `order_items` (`order_id`, `prod_id`, `name`, `main_image`, `quantity`, `unit_price`, `total_price`) VALUES
-- 订单1的商品
(1, 1, '红玫瑰花束（11支）', '/images/products/rose_red_11.jpg', 2, 199.00, 398.00),
-- 订单2的商品
(2, 7, '龟背竹盆栽', '/images/products/monstera.jpg', 1, 128.00, 128.00),
-- 订单3的商品
(3, 10, '520表白花束', '/images/products/love_520.jpg', 1, 520.00, 520.00),
-- 订单4的商品
(4, 1, '红玫瑰花束（11支）', '/images/products/rose_red_11.jpg', 1, 199.00, 199.00),
(4, 2, '香槟玫瑰花束（19支）', '/images/products/rose_champagne_19.jpg', 1, 299.00, 299.00),
(4, 5, '白牡丹多肉', '/images/products/succulent_white.jpg', 2, 29.90, 59.80),
-- 订单5的商品
(5, 8, '琴叶榕盆栽', '/images/products/ficus_lyrata.jpg', 1, 168.00, 168.00),
(5, 6, '十二卷多肉组合', '/images/products/succulent_combo.jpg', 1, 59.90, 59.90),
-- 订单6的商品
(6, 11, '康乃馨花束', '/images/products/carnation.jpg', 1, 88.00, 88.00);

-- ============================================
-- 10. 插入产品收藏数据
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
-- 11. 插入产品评价数据
-- ============================================
INSERT INTO `product_reviews` (`user_id`, `prod_id`, `order_id`, `rating`, `content`, `images`, `verified`, `status`, `create_date`) VALUES
(1, 1, 1, 5, '花非常新鲜，包装精美，送货及时，女朋友很喜欢！', '["\/images\/reviews\/review1_1.jpg", "\/images\/reviews\/review1_2.jpg"]', 1, 'APPROVED', '2025-12-11 17:00:00'),
(3, 1, 4, 5, '玫瑰很大朵，颜色鲜艳，物超所值', '[]', 1, 'APPROVED', '2025-12-13 11:00:00'),
(3, 2, 4, 4, '香槟玫瑰很漂亮，就是价格稍微贵了点', '["\/images\/reviews\/review3_1.jpg"]', 1, 'APPROVED', '2025-12-13 11:05:00'),
(1, 11, 6, 3, '康乃馨一般般，有点蔫', '[]', 1, 'PENDING', '2025-12-10 10:00:00');

-- ============================================
-- 12. 插入优惠券数据
-- ============================================
INSERT INTO `coupons` (`coupon_id`, `merch_id`, `name`, `type`, `value`, `min_price`, `total_quantity`, `received_quantity`, `start_date`, `end_date`, `status`, `description`) VALUES
(1, NULL, '新用户专享券', 'FIXED_AMOUNT', 50.00, 200.00, 1000, 234, '2025-12-01 00:00:00', '2025-12-31 23:59:59', 'ACTIVE', '新用户首单立减50元'),
(2, NULL, '圣诞节满减券', 'FULL_REDUCTION', 100.00, 500.00, 500, 123, '2025-12-20 00:00:00', '2025-12-26 23:59:59', 'ACTIVE', '圣诞特惠，满500减100'),
(3, 1, '花店专享9折券', 'DISCOUNT', 0.90, 100.00, 200, 89, '2025-12-01 00:00:00', '2025-12-31 23:59:59', 'ACTIVE', '花店专家店铺专享9折优惠'),
(4, 2, '绿植生活馆8折券', 'DISCOUNT', 0.80, 150.00, 150, 67, '2025-12-01 00:00:00', '2025-12-31 23:59:59', 'ACTIVE', '绿植生活馆全场8折'),
(5, NULL, '已过期优惠券', 'FIXED_AMOUNT', 30.00, 100.00, 100, 100, '2025-11-01 00:00:00', '2025-11-30 23:59:59', 'EXPIRED', '11月专享优惠券');

-- ============================================
-- 13. 插入用户优惠券数据
-- ============================================
INSERT INTO `customer_coupons` (`user_id`, `coupon_id`, `code`, `status`, `receive_date`, `used_date`, `order_id`) VALUES
(1, 1, 'NEW50-USER1-001', 'USED', '2025-12-10 09:00:00', '2025-12-10 10:30:00', 1),
(1, 2, 'XMAS100-USER1-002', 'UNUSED', '2025-12-12 10:00:00', NULL, NULL),
(2, 1, 'NEW50-USER2-001', 'UNUSED', '2025-12-11 14:00:00', NULL, NULL),
(3, 2, 'XMAS100-USER3-001', 'USED', '2025-12-12 15:00:00', '2025-12-12 16:45:00', 4),
(3, 3, 'FLOWER90-USER3-002', 'UNUSED', '2025-12-11 11:00:00', NULL, NULL),
(4, 4, 'GREEN80-USER4-001', 'UNUSED', '2025-12-10 16:00:00', NULL, NULL);

-- ============================================
-- 14. 插入签到数据
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
-- 15. 插入养护知识数据
-- ============================================
INSERT INTO `care_knowledge` (`title`, `content`, `keywords`, `cover_image`, `category`, `author`, `view_count`, `status`) VALUES
('玫瑰花的日常养护技巧', '玫瑰花是最受欢迎的鲜花之一，正确的养护可以延长花期...\n\n1. 水质要求：使用清水，每天换水\n2. 修剪：斜45度剪根，增加吸水面积\n3. 温度：避免阳光直射，保持阴凉\n4. 添加保鲜剂：可延长花期3-5天', '玫瑰,养护,保鲜,鲜花', '/images/knowledge/rose_care.jpg', '鲜花养护', '花艺师小王', 1234, 'PUBLISHED'),
('多肉植物的浇水秘诀', '多肉植物因其可爱的外形和易养活的特点深受喜爱...\n\n浇水原则：\n1. 见干见湿：土壤完全干透后再浇水\n2. 浇则浇透：每次浇水要浇透\n3. 季节差异：夏季减少浇水，冬季更要控水\n4. 避免积水：盆底必须有排水孔', '多肉,浇水,养护,绿植', '/images/knowledge/succulent_water.jpg', '绿植养护', '园艺达人', 2345, 'PUBLISHED'),
('室内绿植的选择与摆放', '在家中摆放绿植不仅能美化环境，还能净化空气...\n\n适合室内的绿植：\n1. 龟背竹：耐阴，适合客厅\n2. 绿萝：净化能力强，适合卧室\n3. 琴叶榕：北欧风格，适合书房\n4. 虎皮兰：吸收甲醛，适合新房', '室内,绿植,摆放,净化空气', '/images/knowledge/indoor_plants.jpg', '绿植百科', '植物学者', 3456, 'PUBLISHED'),
('鲜花花束的包装技巧', '精美的包装能让花束更具观赏性和仪式感...\n\n包装步骤：\n1. 选择合适的包装纸\n2. 确定花束形状（圆形、瀑布形等）\n3. 螺旋手法固定花茎\n4. 添加装饰元素\n5. 系上丝带', '花束,包装,花艺,技巧', '/images/knowledge/bouquet_wrap.jpg', '花艺教程', '花艺师小王', 567, 'PUBLISHED'),
('春季花卉种植指南', '春季是播种的好时节，掌握正确的种植方法很重要...', '春季,种植,花卉,园艺', '/images/knowledge/spring_planting.jpg', '季节指南', '园艺达人', 123, 'DRAFT');

-- ============================================
-- 16. 插入系统配置数据
-- ============================================
INSERT INTO `system_configuration` (`config_key`, `config_value`, `description`, `category`) VALUES
('site_name', '鲜花市场', '网站名称', 'basic'),
('site_logo', '/images/logo.png', '网站Logo', 'basic'),
('site_description', '专业的鲜花绿植电商平台', '网站描述', 'basic'),
('customer_service_phone', '400-888-9999', '客服电话', 'contact'),
('customer_service_email', 'service@flowermarket.com', '客服邮箱', 'contact'),
('check_in_points', '10', '每日签到奖励积分', 'reward'),
('continuous_check_in_bonus', '5', '连续签到额外奖励积分（第5天起）', 'reward'),
('free_shipping_amount', '99.00', '免运费金额', 'shipping'),
('default_shipping_fee', '10.00', '默认运费', 'shipping'),
('order_auto_cancel_minutes', '30', '未支付订单自动取消时间（分钟）', 'order'),
('order_auto_complete_days', '7', '发货后自动确认收货时间（天）', 'order'),
('review_auto_approve', 'false', '评价是否自动通过审核', 'review'),
('max_cart_items', '20', '购物车最大商品数量', 'cart'),
('product_image_max_size', '5', '产品图片最大尺寸（MB）', 'upload');

-- ============================================
-- 数据初始化完成
-- ============================================

-- 查看插入的数据统计
SELECT '顾客数量' AS '类别', COUNT(*) AS '数量' FROM customers
UNION ALL
SELECT '商家数量', COUNT(*) FROM merchants
UNION ALL
SELECT '产品数量', COUNT(*) FROM products
UNION ALL
SELECT '订单数量', COUNT(*) FROM orders
UNION ALL
SELECT '评价数量', COUNT(*) FROM product_reviews
UNION ALL
SELECT '优惠券数量', COUNT(*) FROM coupons;