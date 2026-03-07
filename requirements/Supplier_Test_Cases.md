# Supplier Module - Test Cases (供应商管理模块测试用例)

## 1. Test Overview (测试概览)
This module allows administrators to manage flower suppliers and merchants to associate products with these suppliers.
本模块允许管理员管理鲜花供应商，并允许商家将商品与这些供应商关联。

---

## 2. Test Steps - English (英文测试步骤)

### Step 1: Admin - Data Source Maintenance
*   **Path**: Admin Dashboard -> **Supplier** (Van icon).
*   **Action**: Click "Add Supplier".
*   **Data**: Enter name (e.g., "Kunming Rose Factory"), contact, phone, and set status to "Active".
*   **Expectation**: Success message appears, and the supplier shows up in the list with a 5.0 rating.

### Step 2: Admin - Status Toggle
*   **Path**: Supplier list.
*   **Action**: Click the "Deactivate" button for a supplier.
*   **Expectation**: Confirmation dialog appears. After confirming, the status tag changes to "Inactive" (Red).

### Step 3: Merchant - Product Association
*   **Path**: Merchant Dashboard -> **Product Management** -> **Add Product**.
*   **Action**: Scroll to the "Supplier" dropdown.
*   **Expectation**: The "Kunming Rose Factory" created in Step 1 should be visible and selectable.

---

## 3. 测试步骤 - 中文 (Chinese Test Steps)

### 第一步：管理员 - 数据维护
*   **路径**：管理员面板 -> **供应商管理** (货车图标)。
*   **操作**：点击“新增供应商”。
*   **数据**：输入名称（如：昆明玫瑰工厂）、联系人、电话，并将状态设为“启用”。
*   **预期结果**：弹出成功提示，列表显示该供应商，初始评分为 5.0。

### 第二步：管理员 - 状态切换
*   **路径**：供应商列表。
*   **操作**：点击某个供应商的“禁用”按钮。
*   **预期结果**：确认对话框弹出，点击确认后，状态标签变为红色“禁用”。

### 第三步：商家端 - 商品关联
*   **路径**：商家面板 -> **商品管理** -> **新增商品**。
*   **操作**：找到“供应商”下拉选择框。
*   **预期结果**：在步骤一中创建的“昆明玫瑰工厂”应出现在列表中供选择。

---

## 4. API Verification (API 验证)
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| GET | `/api/admin/suppliers/page` | Pagination & search |
| POST | `/api/admin/suppliers/save` | Create/Update supplier |
| DELETE| `/api/admin/suppliers/{id}` | Remove supplier |
| POST | `/api/admin/suppliers/status` | Update ACTIVE/INACTIVE |
