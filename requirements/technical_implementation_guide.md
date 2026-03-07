# 技术实现文档 (Technical Implementation Guide)

本文件详述鲜花电子商务平台各个功能模块的具体技术方案。

---

## 1. 鲜花专业属性模块 (Professional Attributes)
*实现日期: 2026-03-01*

### 1.1 业务背景
鲜花作为特殊品类，其花期、养护难度及适宜环境是用户决策的关键需求。

### 1.2 实现细节
*   **字段扩展**: 在 `products` 表中新增 `flowering_period`, `care_difficulty`, `suitable_environment`, `floral_language`。
*   **前端结构**:
    *   商家端通过 `ProductForm.vue` 录入相关元数据。
    *   用户端 `ProductDetail.vue` 通过彩色标签 (Tags) 展示环境要求与花期。

---

## 2. 供应商管理模块 (Supplier)
*正在进行中 (Active)*

### 2.1 模块架构
本模块旨在建立一个数字化供应商管理系统，优化采购流程，提升货源质量与透明度。采用“管理员统一录入、商家关联使用”的平台化管理模式。

### 2.2 数据库设计 (`schema.sql`)
*   **`suppliers` 供应商表**:
    *   `id`: 主键。
    *   `name`: 供应商名称（如：昆明斗南基地）。
    *   `rating`: 评分（1.0-5.0）。
    *   `status`: 合作状态 (ACTIVE, INACTIVE)。
*   **商品表关联**: `products` 表新增 `supplier_id` 外键。

### 2.3 后端分层实现
*   **Mapper**: `SupplierMapper` 基于 MyBatis-Plus。
*   **Service**: `SupplierService` 实现按名搜索与状态切换逻辑。
*   **Controller**:
    *   `AdminSupplierController`: 提供供应商信息的 CRUD 及状态控制。
    *   `MerchantSupplierController` (预留): 为商家提供查询可用供应商的接口。

### 2.4 前端界面设计
*   **管理员后台**: 维护 `e:\IdeaProjects\flower-market\frontend\src\views\admin\supplier` 下的列表与表单。
*   **商家后台集成**: 修改商品发布表单，支持从库中选择供应商。

---

## 3. 未来模块规划
*   **库存周转分析**: 引入批次管理 (FIFO) 逻辑。
*   **智能推荐系统**: 基于用户标签的协同过滤。
