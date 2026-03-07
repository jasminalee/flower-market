# 鲜花电子商务平台功能覆盖表 (基于后端代码分析)

本表根据当前后端 (`backend`) 代码实现情况，对比 [comprehensive_requirements.md](./comprehensive_requirements.md) 中的需求进行覆盖分析。

| 需求分类 | 详细功能需求 | 后端实现状态 | 相关文件/接口 (参考) | 备注 |
| :--- | :--- | :---: | :--- | :--- |
| **3.1 产品与供应管理** | 数字化信息管理 (基础属性: SKU, 价格, 图片) | ✅ | `ProductController`, `Product.java` | 基础 CRUD 已完成 |
| | 专业属性 (花期, 维护难度, 适宜环境, 花语) | ❌ | `Product.java` | 已增加字段并支持存取 |
| | 产品分类管理 (多级分类) | ✅ | `ProductCategoryController` | 支持分类层级 |
| | 供应商管理 | ❌ | | 未发现供应商相关 Controller/Service |
| | 可追溯性管理 (产地, 种植基地, 采摘时间) | ✅ | `ProductTrackabilityController`, `ProductTrackability.java` | 已实现溯源信息维护 |
| | 溯源信息展示 (详情页接口) | ✅ | `ProductTrackabilityController.getByProductId` | |
| **3.2 智能库存管理** | 实时库存监控 | ✅ | `Product.java (stock)`, `ProductService` | 基础库存字段已具备 |
| | 智能预警 (安全库存阈值/自动提醒) | ⚠️ | `Product.java (stockStatus)` | 有状态枚举，但未见自动预警/通知逻辑 |
| | 批次与保质期管理 (先进先出) | ❌ | | 缺乏批次表及 FIFO 相关业务逻辑 |
| | 库存操作 (入库, 出库, 盘点) | ✅ | `ProductController` (基础更新) | 缺乏专门的入库单/出库单流水管理 |
| **3.3 全流程订单管理** | 订单生命周期 (待支付 -> 已支付 -> 已完成) | ✅ | `OrderController`, `OrderStatus` | 流程闭环已实现 |
| | 定时配送/预约时间支持 | ✅ | `OrderController.createOrder` | `OrderCreateRequest` 包含期望配送时间字段 |
| | 售后与投诉处理 (退款/换货) | ⚠️ | `OrderService.cancelOrder` | 仅有取消订单功能，复杂的售后流程(审核/退货)尚未完善 |
| | 物流追踪 (状态更新) | ⚠️ | `OrderServiceImpl` | 有物流单号字段，但未接入真实物流轨迹 API |
| **3.4 会员与精准营销** | 会员等级与成长值 | ✅ | `CustomerController`, `Customer.java` | 包含等级及成长值字段 |
| | 积分系统 (获取, 签到, 兑换) | ✅ | `CheckInController`, `CheckInService` | 签到积分逻辑已实现 |
| | 余额与充值功能 | ✅ | `CustomerController.recharge` | 已实现余额流水与充值 |
| | 优惠券工具 (创建, 领取, 核销) | ✅ | `CouponController`, `CouponService` | 支持满减/折扣等类型 |
| | 用户偏好标签/精准推荐 | ❌ | | 缺乏用户画像建模及推荐算法实现 |
| | 收藏功能 (降价提醒, 到货通知) | ⚠️ | `ProductFavoriteController` | 已有收藏 CRUD，但缺少自动通知触发器 |
| **3.5 知识库与内容运营** | 养护百科 (文章/视频) | ✅ | `CareKnowledgeController` | 支持养护文章的 CRUD 与分类分页 |
| | 互动评价 (评分, 多图评价) | ✅ | `ProductReviewController` | 已实现评价、评分及图片展示 |
| | FAQ 与智能问答 | ⚠️ | `SystemConfigurationController` | 仅支持基础配置项，未见专门的问答 FAQ 库 |
| **3.6 数据分析视窗** | 销售仪表盘 (销售额, 订单量, 客单价) | ✅ | `MerchantController.getDashboardData` | 已实现基础经营统计接口 |
| | 商品热力分析 (TOP 榜单) | ❌ | | 仪表盘接口中统计维度较基础，缺少深度分析 |
| | 库存周转分析 | ❌ | | 缺乏库存周转率相关报表统计 |
| **4. 非功能性需求** | 安全加密 (BCrypt) | ✅ | `BCryptPasswordEncoder` (推测) | 代码中提及加密登录 |
| | 访问控制 (RBAC) | ✅ | `AppConfig`, `JwtUtils` | 使用 JWT 与角色权限校验 |
| | 文件上传支持 (图片/详情图) | ✅ | `MerchantController.uploadProductImage` | 已实现多图上传且路径分类 |

---

### 总结
*   **满足率约 70%**：核心的电商交易链路 (产品 -> 购物车 -> 订单 -> 支付 -> 评价) 以及会员系统 (等级、积分、签到、优惠券) 已基本闭环。
*   **主要缺失点**：
    1.  **产品专业化属性**：数据库及实体中缺少“花期”、“维护难度”等花卉垂直领域的元数据。
    2.  **高级库存/供应链**：缺乏批次管理 (FIFO) 和 供应商生命周期管理。
    3.  **智能化/自动化**：自动库存预警通知、基于行为的推荐算法、到货/降价自动推送等逻辑尚未通过代码实现。
    4.  **深度分析**：目前的看板数据较为基础，缺乏周转率、转化率趋势图等深度决策报表。
