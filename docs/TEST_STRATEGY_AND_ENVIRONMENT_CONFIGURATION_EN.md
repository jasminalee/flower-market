# Flower E-Commerce Platform - Test Strategy and Environment Configuration Guide

**Document Version:** 1.0  
**Last Updated:** 2026-03-22  
**Project Code:** Flower Market Platform

---

## Table of Contents

1. [Test Scope and Objectives](#1-test-scope-and-objectives)
2. [Test Types](#2-test-types)
3. [Test Environment Configuration](#3-test-environment-configuration)
4. [Test Data Preparation Methods](#4-test-data-preparation-methods)
5. [Test Execution Strategy](#5-test-execution-strategy)
6. [Defect Management Process](#6-defect-management-process)
7. [Test Completion Criteria](#7-test-completion-criteria)

---

## 1. Test Scope and Objectives

### 1.1 Test Scope

#### 1.1.1 Functional Scope

Test coverage includes the following core functional modules of the flower e-commerce platform:

| Functional Module | Description | Priority |
|-----------------|-------------|----------|
| User Authentication and Authorization | User registration, login, permission control | P0 |
| Product Management | Product information, categorization, search, traceability | P0 |
| Shopping Cart Management | Add products, edit, delete, checkout | P0 |
| Order Management | Order creation, payment, fulfillment, after-sales | P0 |
| Inventory Management | Inventory monitoring, alerts, allocation, inventory check | P0 |
| Supplier Management | Supplier information maintenance, collaboration management | P1 |
| Coupon System | Coupon creation, usage, redemption | P1 |
| User Points and Check-in | Points earning, consumption, check-in rewards | P1 |
| Flower Knowledge Base | Care knowledge, flower language, information display | P2 |
| Admin Configuration | System parameters, basic data, permissions | P1 |
| Address Management | Add, edit, delete, default address settings | P0 |
| Product Reviews | Review posting, viewing, replies | P2 |

#### 1.1.2 Test Environment Scope

- **Frontend Application:** Vue.js Single Page Application (SPA)
- **Backend Application:** SpringBoot REST API Service
- **Database System:** MySQL Relational Database
- **External Integrations:** Payment Gateway, Logistics System, Third-party Services

### 1.2 Test Objectives

#### 1.2.1 Primary Objectives

| Objective | Details |
|----------|---------|
| Functional Completeness | Verify all required features are implemented correctly and business processes are error-free |
| System Stability | Ensure stable operation under normal and abnormal loads |
| User Experience | Verify good user experience for all roles (customers, merchants, administrators) |
| Data Integrity | Ensure transaction data, inventory data, order data are complete and accurate |
| System Security | Prevent data breaches, SQL injection, permission bypass and other security risks |
| Performance Compliance | API response time, page load time meet performance requirements |

#### 1.2.2 Success Criteria

- ✅ Core business flow main path test pass rate reaches **100%**
- ✅ All P0 defects fixed before release
- ✅ P1 defects fix rate ≥ 95%
- ✅ No blocking defects in critical system functions
- ✅ Average API response time < 500ms, page load time < 2s

---

## 2. Test Types

### 2.1 Functional Testing

#### 2.1.1 Test Description

Verify that each functional module of the system is correctly implemented according to business requirements.

#### 2.1.2 Test Scope and Methods

| Test Object | Test Method | Expected Coverage |
|------------|------------|------------------|
| User Authentication Flow | Black-box testing, boundary value testing | 100% business flow |
| Product Search and Filter | Equivalence class analysis, decision table testing | Main search condition combinations |
| Order Lifecycle | State machine testing, flow testing | All state transitions |
| Inventory Operations | Concurrent testing, boundary testing | Inventory change scenarios |
| Payment Process | Integration testing, exception handling testing | Normal and exception paths |
| Permission Control | Access control testing, privilege escalation testing | Operation permissions for all roles |

#### 2.1.3 Functional Test Checklist

**User Management**
- [ ] User registration (email/phone verification)
- [ ] User login (credentials, third-party login)
- [ ] Password management (change, reset)
- [ ] Update personal information
- [ ] User deactivation

**Product Management**
- [ ] Browse products by category
- [ ] Product search, sorting, filtering
- [ ] Product detail display (basic attributes, professional attributes, traceability info)
- [ ] Display flower care suggestions
- [ ] Favorite and unfavorite products
- [ ] View and add product reviews

**Shopping and Orders**
- [ ] Add products to cart
- [ ] Edit cart items (quantity, delete)
- [ ] Confirm checkout page information
- [ ] Create order, process payment
- [ ] Real-time order status updates
- [ ] Query, cancel, request refund for orders
- [ ] After-sales handling (returns, exchanges)

**Inventory and Alerts**
- [ ] Real-time inventory display
- [ ] Low inventory alerts (backend validation)
- [ ] Oversell prevention (inventory locking)
- [ ] Inventory allocation operations

**Discounts and Points**
- [ ] Create, edit, deactivate coupons
- [ ] Coupon usage validation
- [ ] Check-in to earn points
- [ ] Redeem points for consumption
- [ ] View points and history

**Supplier Management**
- [ ] Supplier information management
- [ ] Purchase order management
- [ ] Goods receipt and acceptance process

### 2.2 Integration Testing

#### 2.2.1 Test Description

Verify that data flow and collaboration between modules are correct, and interfaces with external systems are functioning properly.

#### 2.2.2 Integration Test Scenarios

| Test Scenario | Test Cases | Verification Points |
|--------------|-----------|-------------------|
| Order-Payment Integration | Create order then process payment | Order status, payment status sync |
| Order-Inventory Integration | Inventory changes before and after order | Inventory deduction, locking |
| Order-Points Integration | Points awarded after order completion | Points increase, records created |
| Points-Coupon Integration | Use coupons and points together | Discount calculation, rule validation |
| Logistics-Order Integration | Order shipment and logistics info sync | Logistics status, order progress |
| Payment-Order-Inventory Integration | Rollback on payment failure | Transaction consistency, data accuracy |

### 2.3 Performance Testing

#### 2.3.1 Test Description

Verify system performance under different load conditions, including response time, throughput, resource utilization, etc.

#### 2.3.2 Performance Test Metrics

| Metric | Target Value | Test Method | Priority |
|--------|-------------|-----------|----------|
| Average API Response Time | < 500ms | Concurrent request test | P0 |
| Page Load Time | < 2s | Browser performance monitoring | P0 |
| System Throughput | ≥ 1000 req/s | Stress testing | P1 |
| Database Query Response Time | < 100ms (P95) | Database monitoring | P1 |
| CPU Utilization | < 80% | System monitoring | P1 |
| Memory Utilization | < 80% | System monitoring | P1 |
| Concurrent Users | ≥ 5000 | Concurrent user test | P2 |

#### 2.3.3 Performance Test Scenarios

**Baseline Testing**
- Single user normal operation response time

**Load Testing**
- System performance with 100, 500, 1000, 5000 concurrent users

**Stress Testing**
- Increase concurrent users to system bottleneck, observe system behavior

**Soak Testing**
- Run under moderate load (e.g., 1000 concurrent) for 24 hours continuously

### 2.4 Security Testing

#### 2.4.1 Test Description

Identify and verify security vulnerabilities in the system, including authentication, authorization, data protection, coding issues, etc.

#### 2.4.2 Security Test Checklist

| Security Category | Test Items | Test Method | Risk Level |
|-----------------|-----------|-----------|-----------|
| Authentication & Authorization | SQL Injection | Input special characters, SQL statements | High |
| | Cross-Site Scripting | Input JS code, HTML tags | High |
| | Privilege Escalation | Direct access to restricted APIs | High |
| | Session Fixation | Check session management | Medium |
| Data Protection | Password Encryption | Verify encryption algorithms | High |
| | Data Transport Encryption (HTTPS) | Verify HTTPS usage | High |
| | Sensitive Data Exposure | Check sensitive data in API responses | High |
| Business Logic | Concurrent Order Payment | Multiple payment attempts | Medium |
| | Negative Inventory | Oversell prevention, concurrent inventory ops | Medium |
| | Duplicate Coupon Usage | Multiple use of same coupon | Medium |

#### 2.4.3 Security Testing Tools

- **Static Analysis:** SonarQube, FindBugs
- **Dynamic Scanning:** OWASP ZAP, Burp Suite
- **Penetration Testing:** Manual testing by security team
- **Dependency Check:** OWASP Dependency-Check

### 2.5 Compatibility Testing

#### 2.5.1 Browser Compatibility

| Browser | Minimum Version | Support Level |
|---------|---------------|---------------|
| Chrome | 90+ | Full Support |
| Firefox | 88+ | Full Support |
| Safari | 14+ | Full Support |
| Edge | 90+ | Full Support |
| IE | Not Supported | Not officially supported |

#### 2.5.2 Mobile Device Compatibility

| Device Type | Operating System | Screen Size | Priority |
|------------|-----------------|-----------|----------|
| Smartphone | iOS 12+, Android 6+ | 375px - 768px | P0 |
| Tablet | iOS 12+, Android 6+ | 768px - 1024px | P1 |

### 2.6 User Experience Testing

#### 2.6.1 Usability Testing

- **Ease of Operation:** Users can quickly understand and operate system features
- **Information Clarity:** Page information display is clear and logical
- **Error Messaging:** System prompts are clear and helpful when errors occur
- **Navigation Design:** Navigation structure is reasonable and easy to find features

#### 2.6.2 Accessibility Testing

- [ ] Keyboard navigation support
- [ ] Screen reader compatibility
- [ ] Text scaling support
- [ ] High contrast mode

---

## 3. Test Environment Configuration

### 3.1 Development Environment

#### 3.1.1 Development Environment Purpose

- Function development, debugging, developer unit testing
- No integration testing or system testing

#### 3.1.2 Development Environment Configuration

**System Requirements**

```
Hardware Configuration
----------------------------------------------
Processor:        Intel 8th Gen i5+ / AMD Ryzen 5+
RAM:              8GB minimum, 16GB recommended
Disk:             SSD 256GB or more
Network:          Stable network connection (≥10Mbps)
```

**Software Environment**

| Software Component | Version | Purpose |
|------------------|---------|---------|
| Java Development Kit | 1.8 (OpenJDK recommended) | Java compilation & runtime |
| Apache Maven | 3.6.3+ | Project build, dependency management |
| Node.js | 14+ | Frontend development |
| npm/yarn | 6.14+ / 1.22+ | Frontend package management |
| MySQL | 5.7.0+ or 8.0+ | Database |
| Git | 2.20+ | Version control |
| IDE | IntelliJ IDEA, VS Code | Code editing |

**Environment Configuration Steps**

```bash
# 1. Clone project code
git clone <repository-url>
cd flower-market

# 2. Backend environment setup
cd backend

# 2.1 Create Maven dependencies
mvn clean install -DskipTests

# 2.2 Configure database
# Edit application.yml, configure MySQL connection

# 2.3 Initialize database
mysql -u root -p < src/main/resources/schema.sql
mysql -u root -p < src/main/resources/data.sql

# 3. Frontend environment setup
cd ../frontend

# 3.1 Install dependencies
npm install
# or
yarn install

# 3.2 Start development server
npm run dev
# or
yarn dev

# 4. Access application
# Frontend: http://localhost:5173 (Vite default port)
# Backend API: http://localhost:8080 (Spring Boot default port)
```

### 3.2 Test Environment

#### 3.2.1 Test Environment Purpose

- Functional testing, integration testing, performance testing, security testing
- Comprehensive system testing
- STAGING pre-release environment (same configuration as production but with test data)

#### 3.2.2 Test Environment Architecture

```
                        ┌─────────────────────────────────┐
                        │     Load Balancer (Nginx)       │
                        └──────────────┬──────────────────┘
                                       │
                    ┌──────────────────┼──────────────────┐
                    │                  │                  │
            ┌───────▼─────────┐ ┌─────▼────────┐ ┌──────▼────────┐
            │  Backend Pod 1  │ │Backend Pod 2 │ │Backend Pod 3  │
            │ (Spring Boot)   │ │(Spring Boot) │ │(Spring Boot)  │
            └────────┬────────┘ └──────┬───────┘ └───────┬───────┘
                     │                 │                 │
                     └─────────────────┼─────────────────┘
                                       │
                    ┌──────────────────┼──────────────────┐
                    │                  │                  │
            ┌───────▼──────────┐ ┌────▼──────────┐ ┌────▼──────────┐
            │  MySQL Master    │ │ MySQL Slave 1 │ │ MySQL Slave 2 │
            └──────────────────┘ └───────────────┘ └───────────────┘
                    │
            ┌───────▼──────────┐
            │  Redis Cache     │
            └──────────────────┘
```

#### 3.2.3 Test Environment Configuration Requirements

**Network Configuration**

```yaml
# Nginx Load Balancer Configuration Example
upstream backend {
    least_conn;
    server backend-1.test.internal:8080 max_fails=3 fail_timeout=30s;
    server backend-2.test.internal:8080 max_fails=3 fail_timeout=30s;
    server backend-3.test.internal:8080 max_fails=3 fail_timeout=30s;
}

server {
    listen 80;
    server_name api.test.internal;
    
    location / {
        proxy_pass http://backend;
        proxy_connect_timeout 5s;
        proxy_send_timeout 10s;
        proxy_read_timeout 10s;
    }
}
```

**Database Configuration**

```yaml
# MySQL Master-Slave Configuration Example
spring:
  datasource:
    master:
      url: jdbc:mysql://mysql-master.test.internal:3306/flower_market?useSSL=false&serverTimezone=Asia/Shanghai
      username: root
      password: test_password
      driver-class-name: com.mysql.cj.jdbc.Driver
    slave:
      url: jdbc:mysql://mysql-slave-1.test.internal:3306/flower_market?useSSL=false&serverTimezone=Asia/Shanghai
      username: read_user
      password: read_password
      driver-class-name: com.mysql.cj.jdbc.Driver
```

**Cache Configuration**

```yaml
# Redis Cache Configuration Example
spring:
  redis:
    host: redis.test.internal
    port: 6379
    password: test_redis_password
    database: 0
    timeout: 2000ms
    jedis:
      pool:
        max-active: 100
        max-idle: 50
        min-idle: 10
```

**Monitoring Configuration**

```yaml
# Application Monitoring Configuration
management:
  endpoints:
    web:
      exposure:
        include: health,metrics,prometheus
  metrics:
    export:
      prometheus:
        enabled: true
  endpoint:
    health:
      show-details: always
```

### 3.3 Performance Test Environment

#### 3.3.1 Performance Test Environment Configuration

**Isolation Requirements**

```
Performance test environment must be isolated from other test environments

Configuration:
- Separate network subnet (VLAN)
- Separate MySQL database instance
- Disable all external network requests
- Dedicated hardware resources (not shared with other applications)
```

**Testing Tools**

| Tool | Purpose | Installation |
|-----|---------|--------------|
| Apache JMeter | Performance & stress testing | Download ZIP package |
| Locust | Distributed load testing | pip install locust |
| Prometheus | Metric collection, monitoring | Docker / Binary |
| Grafana | Monitoring visualization | Docker / Binary |

### 3.4 Production Environment

#### 3.4.1 Production Environment Architecture (Reference)

```
                     ┌──────────────────────────────┐
                     │    CDN (Content Delivery)    │
                     └──────────────┬───────────────┘
                                    │
                     ┌──────────────▼───────────────┐
                     │    WAF (Web Application Fw)  │
                     └──────────────┬───────────────┘
                                    │
                     ┌──────────────▼───────────────┐
                     │    Load Balancer (Nginx)     │
                     └──────────────┬───────────────┘
                                    │
            ┌───────────────┬───────┴────────┬──────────────┐
            │               │                │              │
    ┌───────▼──────┐ ┌─────▼──────┐ ┌──────▼────────┐ ┌───▼────────┐
    │Backend Pod 1 │ │Backend Pod 2│ │Backend Pod 3 │ │Backend Pod N│
    │(SpringBoot)  │ │(SpringBoot) │ │(SpringBoot)  │ │(SpringBoot) │
    └───────┬──────┘ └──────┬──────┘ └───────┬──────┘ └───┬────────┘
            │               │              │             │
            └───────────────┼──────────────┼─────────────┘
                            │
            ┌───────────────┼──────────────────────────┐
            │               │                          │
    ┌───────▼──────┐ ┌─────▼──────┐ ┌────────────────▼──┐
    │MySQL Master  │ │MySQL Slave 1│ │ MySQL Slave N    │
    │(Binlog)      │ │(Read-only)  │ │ (Read-only)      │
    └──────────────┘ └────────────┘ └──────────────────┘
            │
    ┌───────▼──────┐
    │ Redis Cluster │
    │ (High Avail)  │
    └──────────────┘
```

#### 3.4.2 Production Environment Configuration Principles

- ✅ **High Availability:** Critical components deployed with redundancy, no single point of failure
- ✅ **Data Security:** Data encrypted at rest and in transit, regular backups
- ✅ **Performance Stability:** Only deploy fully tested versions
- ✅ **Disaster Recovery:** Disaster recovery plan in place, regular drills
- ✅ **Monitoring & Alerting:** 24/7 system monitoring, real-time alerts

---

## 4. Test Data Preparation Methods

### 4.1 Overall Test Data Strategy

#### 4.1.1 Data Preparation Principles

```
Principle 1: Realism
----------------------------------------------
Test data should be as close as possible to production data characteristics

Examples:
- Real product information (name, price, category)
- Order state transitions following business rules
- Real geographic location information


Principle 2: Isolation
----------------------------------------------
Test data must be completely isolated from production data

Requirements:
- Separate database accounts and databases
- Test data marked with unique identifier (e.g., _TEST_ suffix)
- Regular cleanup of test data


Principle 3: Completeness
----------------------------------------------
Test data covers all major business scenarios

Scenarios:
- Normal flow (Happy Path)
- Exception flow (Exception Path)
- Boundary conditions
- Cross-module scenarios


Principle 4: Traceability
----------------------------------------------
Test data source and change history is traceable

Implementation:
- Record data version and update date
- Keep test data change log
- Backup important test datasets
```

### 4.2 Test Data Preparation Methods

#### 4.2.1 Initial Data Preparation

**Method 1: Database Scripts**

```sql
-- Initialize product data
INSERT INTO product (id, name, category_id, price, stock, status) VALUES
(1, 'Red Rose Grade A', 1, 99.99, 100, 'ACTIVE'),
(2, 'White Rose Grade A', 1, 99.99, 150, 'ACTIVE'),
(3, 'Sunflower Mix', 2, 79.99, 200, 'ACTIVE'),
(4, 'Tulip Combo', 3, 129.99, 50, 'ACTIVE'),
(5, 'Carnation Mother\'s Day', 4, 49.99, 0, 'ACTIVE');

-- Initialize category data
INSERT INTO category (id, name, description) VALUES
(1, 'Roses', 'Various rose flowers'),
(2, 'Sunflowers', 'Sunflower related products'),
(3, 'Tulips', 'Tulip series products'),
(4, 'Carnations', 'Mother\'s Day specials');

-- Initialize user data
INSERT INTO user (id, username, email, phone, password_hash, role, status) VALUES
(1, 'customer_test_001', 'customer1@test.com', '13800000001', SHA2('password123', 256), 'CUSTOMER', 'ACTIVE'),
(2, 'merchant_test_001', 'merchant1@test.com', '13800000002', SHA2('password123', 256), 'MERCHANT', 'ACTIVE'),
(3, 'admin_test_001', 'admin@test.com', '13800000003', SHA2('password123', 256), 'ADMIN', 'ACTIVE');

-- Initialize delivery addresses
INSERT INTO address (id, user_id, recipient_name, phone, province, city, district, detail_address, is_default) VALUES
(1, 1, 'John Doe', '13800000001', 'Beijing', 'Beijing', 'Chaoyang', '1 Jianguo Road', true),
(2, 1, 'John Doe', '13800000001', 'Shanghai', 'Shanghai', 'Pudong', '100 Century Ave', false);

-- Initialize coupons
INSERT INTO coupon (id, code, name, discount_amount, discount_percent, min_amount, max_usage, used_count, status, end_date) VALUES
(1, 'WELCOME10', 'New User Coupon', 10, null, 50, 1000, 0, 'ACTIVE', '2026-12-31'),
(2, 'SPRING20', 'Spring Promotion', 20, null, 100, 500, 0, 'ACTIVE', '2026-06-30');
```

**Method 2: Test Data Factory (Utility Methods)**

```java
// Java Test Data Factory Example
public class TestDataFactory {
    
    private static final Random random = new Random();
    
    /**
     * Create test user
     */
    public static User createTestUser(UserRole role) {
        User user = new User();
        user.setUsername("user_" + System.currentTimeMillis());
        user.setEmail(user.getUsername() + "@test.com");
        user.setPhone("138" + String.format("%08d", random.nextInt(100000000)));
        user.setRole(role);
        user.setStatus(UserStatus.ACTIVE);
        return user;
    }
    
    /**
     * Create test product
     */
    public static Product createTestProduct(Long categoryId) {
        Product product = new Product();
        product.setCategoryId(categoryId);
        product.setName("Test Product_" + System.currentTimeMillis());
        product.setPrice(new BigDecimal(Math.random() * 500 + 10)); // 10-510
        product.setStock(random.nextInt(1000) + 1);
        product.setStatus(ProductStatus.ACTIVE);
        return product;
    }
    
    /**
     * Create test order
     */
    public static Order createTestOrder(Long userId, List<Product> products) {
        Order order = new Order();
        order.setUserId(userId);
        order.setOrderNo("TEST" + System.currentTimeMillis());
        order.setStatus(OrderStatus.PENDING_PAYMENT);
        order.setTotalAmount(products.stream()
            .map(Product::getPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add));
        return order;
    }
}
```

#### 4.2.2 Test Scenario Data Preparation

**Scenario 1: Normal Shopping Flow**

```
Preconditions:
1. System contains at least 10 valid products
2. Each product inventory ≥ 100
3. Coupons exist (new user discount, full-reduction discount)

Data Setup:
- Create test user (new user, no purchase history)
- Prepare products with multiple colors/specifications
- Set up 3 delivery addresses
```

**Scenario 2: Insufficient Inventory Handling**

```
Preconditions:
1. Create products with low inventory (5-10 pieces)
2. Create products with zero inventory

Data Setup:
- Set product status to show "low inventory"
- Prepare oversell scenario test cases
```

**Scenario 3: Multi-user Concurrent Purchase**

```
Preconditions:
1. Same product inventory: 10 pieces
2. Create 15 test users

Data Setup:
- 15 users add to cart simultaneously for 10 pieces
- Verify only 10 successful orders
```

### 4.3 Test Data Management Tools

#### 4.3.1 Database Reset Tools

```bash
#!/bin/bash
# Database Reset Script

# Stop application
systemctl stop flower-market-backend

# Export production data backup (optional)
mysqldump -u root -p production_db > /backup/prod_$(date +%Y%m%d_%H%M%S).sql

# Drop test database
mysql -u root -p -e "DROP DATABASE IF EXISTS flower_market_test;"

# Create new test database
mysql -u root -p -e "CREATE DATABASE flower_market_test CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;"

# Initialize database schema
mysql -u root -p flower_market_test < src/main/resources/schema.sql

# Initialize test data
mysql -u root -p flower_market_test < src/main/resources/data.sql

# Start application
systemctl start flower-market-backend

echo "Database reset completed"
```

#### 4.3.2 Test Data Version Control

```yaml
# test-data-versions.yaml
versions:
  v1.0:
    description: "Initial test data set"
    date: "2026-01-01"
    files:
      - schema.sql
      - seed-products.sql
      - seed-users.sql
      - seed-orders.sql
    changes: "First release"
    
  v1.1:
    description: "Add performance test data"
    date: "2026-02-15"
    files:
      - schema.sql
      - seed-products.sql
      - seed-users.sql (extended to 10,000 users)
      - seed-orders.sql (extended to 50,000 orders)
    changes: "Support performance testing"
    
  v1.2:
    description: "Add edge case data"
    date: "2026-03-01"
    files:
      - seed-edge-cases.sql
    changes: "Test boundary conditions"
```

### 4.4 Sensitive Data Handling

#### 4.4.1 Data Masking Rules

```
User Phone Masking
Original: 13800001234
Masked:   138****1234

User Email Masking
Original: customer@example.com
Masked:   c***@example.com

Payment Card Masking
Original: 6222 0210 0000 1234
Masked:   6222 02** **** 1234

ID Number Masking
Original: 110101199003071234
Masked:   110101****071234
```

#### 4.4.2 Data Masking Tools

```java
/**
 * Data Masking Utility Class
 */
public class DataMaskingUtil {
    
    /**
     * Mask phone number
     */
    public static String maskPhone(String phone) {
        if (phone == null || phone.length() < 6) return phone;
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }
    
    /**
     * Mask email
     */
    public static String maskEmail(String email) {
        int atIndex = email.indexOf("@");
        if (atIndex <= 1) return email;
        String prefix = email.substring(0, 1);
        String domain = email.substring(atIndex);
        return prefix + "***" + domain;
    }
    
    /**
     * Mask ID number
     */
    public static String maskIdCard(String idCard) {
        if (idCard == null || idCard.length() < 8) return idCard;
        return idCard.substring(0, 6) + "****" + idCard.substring(14);
    }
}
```

---

## 5. Test Execution Strategy

### 5.1 Test Phase Planning

#### 5.1.1 Test Phase Division

```
                        Project Lifecycle
                              │
                ┌─────────────┼─────────────┐
                │             │             │
        Development    │     Testing      │      Release
            Phase        │     Phase       │      Phase
                │             │             │
                │     ┌───────┼───────┐    │
                │     │       │       │    │
                │  Unit Test Functional Integrated
                │             │
        ┌───────▼───────┐    │    ┌──────▼────────┐
        │                │    │    │                │
    P1:Dev Complete  P2:Func   P3: System   P4:Production
     Phase 1         Phase 2    Phase 3      Phase 4
                     │
            ┌─────────┼─────────┐
            │         │         │
        Performance Security Compatibility
        Phase       Phase     Phase
```

#### 5.1.2 Detailed Test Phase Description

**Phase 1: P1 - Unit Testing (Development Phase)**

```
Timeline:           Within 2 days of development completion
Executed by:        Developers
Coverage:           Code coverage ≥ 80%
Tools:              JUnit, Mockito

Checklist:
- [ ] Write unit test cases
- [ ] Run unit tests
- [ ] Code coverage analysis (SonarQube)
- [ ] Fix test failures
- [ ] Submit code review
```

**Phase 2: P2 - Functional Testing (Test Cycle 1)**

```
Timeline:           3-7 days after development
Executed by:        Test Team
Coverage:           100% functional requirement coverage
Tools:              TestLink, Jira, Postman

Checklist:
- [ ] Prepare test environment & data
- [ ] Write test cases
- [ ] Execute functional tests
- [ ] Log defects and improvement suggestions
- [ ] Defect verification & regression
- [ ] Generate functional test report

Expected Results:
- P0 defect fix rate: 100%
- P1 defect fix rate: ≥ 80%
```

**Phase 3: P3 - Integration/System/Performance/Security Testing (Test Cycle 2)**

```
Timeline:           3-5 days after functional testing
Executed by:        Senior QA, Performance & Security Teams
Coverage:           End-to-end flows, system metrics, security issues

Parallel Execution:

┌─ Integration ─┬─ Performance ─┬─ Security ─┐
│               │               │            │
│ Order-Payment │ Baseline Test │ Auth/Authz │
│ Stock Sync    │ Load Test     │ SQL Inject │
│ Point Award   │ Stress Test   │ XSS        │
│               │ Soak Test     │ Privilege  │
└───────────────┴───────────────┴────────────┘

Expected Results:
- Critical flow integration test pass rate: 100%
- Average API response time < 500ms
- No high-risk security vulnerabilities
```

**Phase 4: P4 - Canary/Production Validation (Pre-release)**

```
Timeline:           1-2 days after system testing
Executed by:        Test Team, Product Team, Operations
Objective:          Verify production readiness

Checklist:
- [ ] Verify canary deployment
- [ ] Real user test with 1% traffic
- [ ] Monitor KPIs (no anomalies in 5 mins)
- [ ] Gradual traffic upgrade (1% → 10% → 50% → 100%)
- [ ] Collect user feedback

Expected Results:
- No production-specific defects
- System availability ≥ 99.5%
```

### 5.2 Test Cycle Planning

#### 5.2.1 Standard Test Cycle (Example: One Module)

```
Test Cycle: 2-3 weeks before module launch

Week 1 - Development & Unit Testing
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Mon - Wed:  Developers complete code + unit testing
Thu - Fri:  Code review & merge to main branch

Week 2 - Functional Testing
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Mon - Tue:  Setup test environment and data
Wed - Thu:  Execute functional tests, log defects
Fri:        Defect verification & regression

Week 3 - System/Performance/Security Testing
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
Mon - Wed:  Parallel system, performance, security testing
Thu:        Test report generation & risk assessment
Fri:        Management review & release decision
```

#### 5.2.2 Fast Iteration Cycle

```
For small daily iterations (Bug fixes, minor features)

Cycle: 3-5 days

Day 1: Development + unit testing
       
Day 2-3: Functional testing + defect fixes
         
Day 4: Integration + regression + release prep
       
Day 5: Release + production monitoring
```

### 5.3 Team Composition and Responsibility Matrix

#### 5.3.1 Test Team Composition

```
Flower E-Commerce Platform - Test Team Structure
================================================

┌──────────────────────────────────────┐
│       QA Manager                     │
│  (Manage planning, progress, quality)│
└──────────────────┬──────────────────┘
                   │
        ┌──────────┼───────────┐
        │          │           │
    Functional Performance  Automation
    Test Lead   Test Lead    Lead
        │          │           │
        │          │           │
   ┌────▼───┐ ┌────▼───┐ ┌────▼───┐
   │  QA 1  │ │  QA 4  │ │  QA 7  │
   │  QA 2  │ │  QA 5  │ │  QA 8  │
   │  QA 3  │ │  QA 6  │ │  QA 9  │
   └────────┘ └────────┘ └────────┘
```

#### 5.3.2 Responsibility Matrix

| Task | QA Manager | Functional QA | Performance QA | Automation QA | Dev |
|------|-----------|--------------|---|---|---|
| Test Plan | **A** | C | C | I | R |
| Write Test Cases | R | **A** | - | C | - |
| Setup Test Env | R | **A** | **A** | - | - |
| Execute Functional Tests | I | **A** | - | - | - |
| Execute Perf Tests | I | - | **A** | C | - |
| Execute Security Tests | I | - | **A** | C | - |
| Build Automation Framework | I | - | - | **A** | R |
| Defect Tracking | R | **A** | - | R | - |
| Generate Report | **A** | R | R | - | - |
| Production Validation | R | **A** | - | I | - |

**Legend:**
- **A** = Accountable - Final responsibility
- **R** = Responsible - Executor
- **C** = Consulted - Opinion
- **I** = Informed - Notification

### 5.4 Test Execution Guidelines

#### 5.4.1 Test Case Execution Flow

```
1. Prepare Environment
   ├─ Clear test data
   ├─ Initialize test data
   └─ Confirm system ready (no errors)

2. Execute Test Steps
   ├─ Execute steps sequentially
   ├─ Record actual results
   ├─ Compare expected vs actual
   └─ Screenshot (if failed)

3. Record Results
   ├─ PASS      - Meets expected result
   ├─ FAIL      - Doesn't meet expected, report defect
   ├─ BLOCKED   - Blocked by other defect
   └─ SKIP      - Not applicable, environment issue, etc.

4. Report Defect (if Failed)
   ├─ Create Jira ticket
   ├─ Fill details (title, description, repro steps, screenshots)
   ├─ Prioritize
   ├─ Assign to developer
   └─ Set related test case
```

#### 5.4.2 Defect Reproduction & Verification Checklist

```
Reproduce Defect
- [ ] Environment: Dev/Test/Staging/Production
- [ ] Browser: Chrome/Firefox/Safari/Edge
- [ ] OS: Windows/Mac/Linux
- [ ] Network: WiFi/Mobile
- [ ] Repro steps are clear & reproducible
- [ ] Reproducible consistently: Yes/Intermittently
- [ ] Impact: Single feature/Multiple features

Verify Defect
- [ ] Confirm defect is fixed
- [ ] Verify fix doesn't introduce new defects
- [ ] Related functionality regression passed
- [ ] Other user scenarios verified
```

---

## 6. Defect Management Process

### 6.1 Defect Classification and Priority

#### 6.1.1 Severity Classification

| Severity | Impact | Example | Avg Fix Time |
|----------|--------|---------|------------|
| P0 - Blocker | System crash, critical functions unusable | Cannot login, payment failure, data loss | < 2 hours |
| P1 - Critical | Major function unavailable, reduces availability | Order status not updating, wrong inventory | < 4 hours |
| P2 - Major | Feature works but has defects, affects UX | Slow page load, text formatting issues | < 24 hours |
| P3 - Minor | Minor defects, minimal impact on usage | Wrong button color, typos in messages | < 48 hours |
| P4 - Trivial | Enhancement suggestion, UX optimization | UI beautification, performance improvement tips | To be defined |

#### 6.1.2 Defect Classification Matrix

```
          Impact Scope
              │ All Users │ Some Users │ Single User
              │           │            │
    ┌─────────┼───────────┼────────────┐
    │ Complete│   P0    │   P1     │  P2
致 │ Unable  │ Blocker │ Critical │ Major
影 │ to Work │         │          │
响 ├─────────┼───────────┼────────────┤
程 │ Partial │   P1    │   P2     │  P3
度 │ Failure │ Critical│ Major    │ Minor
   │         │         │          │
   ├─────────┼───────────┼────────────┤
   │ Minor   │   P2    │   P3     │  P4
   │ Defect  │ Major   │ Minor    │ Trivial
   └─────────┴───────────┴────────────┘
```

### 6.2 Defect Management Process

#### 6.2.1 Defect Lifecycle

```
State Transition Diagram

        ┌─────────────┐
        │   NEW       │  (Created)
        │      ▼      │
        └─────────────┘
             │
             │ (reviewed by QA lead)
             ▼
        ┌─────────────┐
        │  OPEN       │  (Verified, pending fix)
        │ (Verified)  │
        └─────────────┘
             │
             │ (assigned to developer)
             ▼
        ┌─────────────┐
        │ ASSIGNED    │  (Assigned)
        │ (In Dev)    │
        └─────────────┘
             │
             │ (developer submits fix)
             ▼
        ┌──────────────┐
        │ IN_PROGRESS  │  (Fixing)
        │ (Verifying)  │
        └──────────────┘
             │
             │ (fix complete, submitted for review)
             ▼
        ┌──────────────┐
        │ RESOLVED     │  (Resolved, pending regression)
        │(Reviewed)    │
        └──────────────┘
             │
   ┌─────────┴──────────┐
   │                    │
(Regr Pass)      (Regr Fail)
   │                    │
   ▼                    ▼
┌──────────┐      ┌──────────┐
│ VERIFIED │      │ REOPENED │  (Needs re-fix)
│(Verified)│      │Reopen)   │
│Closed    │      └──────────┘
└──────────┘             │
   │                     │
   │        (reassigned)  │
   │                     └──▶ IN_PROGRESS
   │
   ▼
┌──────────┐
│ CLOSED   │  (Closed)
│(Closed)  │
└──────────┘
```

#### 6.2.2 Detailed Defect Process

**Step 1: Defect Identification & Recording**

```
When:           During test execution or production feedback

Who:            QA, users, operations

What:           Document defect information

Defect Report Template
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

Defect ID:          Auto-generated
Title:              Concise (max 20 chars)
Priority:           P0/P1/P2/P3/P4
Severity:           Blocker/Critical/Major/Minor/Trivial
Module:             (Product Management, Order Management, etc.)
Found in:           Dev/Test/Staging/Production
Affected Version:   1.0.0
Found Date:         YYYY-MM-DD

Description:
Brief description of defect

Steps to Reproduce:
1. ...
2. ...
3. ...

Expected Result:
System should...

Actual Result:
System actually...

Attachments:
- Screenshot
- Error log
- Video recording

Notes:
Additional comments
```

**Step 2: Defect Triage**

```
When:           Within 2 hours of report

Participants:   QA Lead, Project Manager, Tech Lead

Review:
- [ ] Confirm defect is valid
- [ ] Re-classify priority
- [ ] Determine responsible team
- [ ] Assess fix complexity
- [ ] Check for duplicates

Output:         Jira ticket updated to OPEN status
```

**Step 3: Defect Assignment & Fix**

```
When:           Immediately after triage

Who:            Tech Lead / Team Lead

Assignment Rules:
- P0: Immediately to senior dev
- P1: To module owner
- P2+: To regular dev

Fix Work:
- [ ] Reproduce locally
- [ ] Root cause analysis
- [ ] Implement fix
- [ ] Write/update unit tests
- [ ] Submit code review
- [ ] Merge to test branch
```

**Step 4: Defect Verification**

```
When:           Within 2 hours of dev marking RESOLVED

Who:            Original reporter / related QA

Verification Steps:
1. Re-execute repro steps
2. Confirm defect is fixed
3. Execute related regression tests
4. Check for new issues

Result:
- PASS   → Update to VERIFIED (closed)
- FAIL   → Update to REOPENED
- BLOCKED→ Update to BLOCKED
```

**Step 5: Defect Monitoring & Closure**

```
Ongoing:
- Monitor all open defects
- Daily status sync
- Track high-priority defect fixes

Closure Criteria:
- [ ] Defect fixed
- [ ] Regression test passed
- [ ] Version released or merged to main
- [ ] Production validation passed
```

### 6.3 Defect Statistics & Analysis

#### 6.3.1 Defect Statistics Metrics

```
Key Metrics
━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

1. Defect Discovery Rate
   Definition: Average defects discovered per day
   Formula: Total discovered ÷ Test days
   
   Example:
   Suppose 150 defects found, test cycle 15 days
   
   Discovery rate = 150 ÷ 15 = 10 defects/day
   
   Assessment:
   - Rising rate: Development quality concern
   - Declining rate: Defect space gradually filled

2. Defect Fix Rate
   Definition: Percentage of fixed vs discovered defects
   Formula: (Fixed ÷ Discovered) × 100%
   
   Example:
   Fixed: 120, Discovered: 150
   
   Fix rate = (120 ÷ 150) × 100% = 80%

3. Defect Trend Analysis
   
   Defect Discovery & Fix Trend
   
   Defects
   │        ┌─────────────┐
   │        │ Discovery  │
   │      ╱ │             │
   │    ╱   │             │
   │  ╱     └─────────────┘
   │╱
   │     ╱─────┐        ┌┐
   │    ╱       └──┐  ┌──┘│ Fix
   │   ╱           └──┘    │
   │  ╱                    └─────
   │╱
   └─────────────────────────  Time
   
   Interpretation:
   - When fix curve approaches discovery curve, inventory decreases
   - When discovery rate drops near zero, test adequacy is stable

4. Defect Distribution
   
   By Priority
   P0: 5%  (7/150)
   P1: 15% (23/150)
   P2: 40% (60/150)
   P3: 30% (45/150)
   P4: 10% (15/150)

   By Module
   Product Management:  30% (45/150)
   Order Management:    25% (37/150)
   Inventory Management:20% (30/150)
   User Authentication: 10% (15/150)
   Others:              15% (23/150)
```

#### 6.3.2 Defect Quality Report

```markdown
# Test Cycle I - Defect Summary Report
## Period: 2026-03-01 to 2026-03-15

### Executive Summary

Discovery total: 150 defects total: 7 P0, 23 P1.

As of report date, 120 fixed (80% fix rate), 30 open.

### Defect Statistics

| Priority | Discovered | Fixed | Open | Fix Rate |
|----------|-----------|-------|------|----------|
| P0 | 7 | 7 | 0 | 100% |
| P1 | 23 | 18 | 5 | 78% |
| P2 | 60 | 55 | 5 | 92% |
| P3 | 45 | 40 | 5 | 89% |
| P4 | 15 | 0 | 15 | 0% |
| **Total** | **150** | **120** | **30** | **80%** |

### Key Findings

1. **High Priority Defects**
   - All P0 defects fixed
   - P1 fix rate 78%, need acceleration
   
2. **Defect Distribution**
   - Order management has most defects (40%)
   - Inventory management second (25%)
   
3. **Defect Trend**
   - Week 1: avg 12 defects/day
   - Week 2: avg 8 defects/day
   - Discovery rate down 33%, indicating improving test adequacy

### Recommendations

1. Accelerate P1 fixes to reach 95%+ by Friday
2. Conduct deep regression testing on order management
3. Continue test execution, expect discovery rate below 3/day by weekend
```

---

## 7. Test Completion Criteria

### 7.1 Functional Testing Completion Criteria

#### 7.1.1 Prerequisites

```
✅ Code Review Passed
   - All code changes reviewed by tech lead

✅ Unit Tests Passed
   - Unit test pass rate: 100%
   - Code coverage ≥ 80%

✅ Test Environment Ready
   - Latest code deployed to test environment
   - Database initialized
   - External dependencies available

✅ Test Cases Ready
   - All features have corresponding test cases
   - Test cases reviewed
```

#### 7.1.2 Functional Test Completion Criteria

```
Defect Criteria:
┌────────────────────────────────────────┐
│ P0 Defects: ZERO
│ • No system crashes
│ • No data loss
│ • No blocked business processes
└────────────────────────────────────────┘

┌────────────────────────────────────────┐
│ P1 Defects: Fix Rate ≥ 95%
│ • Critical function defects fixed
│ • Allow max 1-2 P1s planned for later
└────────────────────────────────────────┘

┌────────────────────────────────────────┐
│ P2+ Defects: Fix Rate ≥ 70%
│ • Handle flexibly based on priority
└────────────────────────────────────────┘

Test Case Coverage:
┌────────────────────────────────────────┐
│ Requirement Coverage:     ≥ 100%
│ Business Flow Coverage:   ≥ 95%
│ Boundary Condition:       ≥ 90%
│ Exception Handling:       ≥ 85%
└────────────────────────────────────────┘

Test Case Execution:
┌────────────────────────────────────────┐
│ Pass Rate:             ≥ 95%
│ No blocking failures
│ All failures have defect tickets
└────────────────────────────────────────┘
```

### 7.2 Integration Testing Completion Criteria

```
Core Process Integration
┌────────────────────────────────────────┐
│ Order-Payment-Inventory Flow    ✅ PASS
│ ├─ Order → Payment → Stock → Confirm
│
│ Order-Points Flow                ✅ PASS
│ ├─ Order Complete → Award Points → Balance
│
│ Inventory-Alert Flow             ✅ PASS
│ ├─ Stock Deduct → Low Check → Alert
│
│ Coupon-Order-Payment Flow        ✅ PASS
│ ├─ Apply Coupon → Recalc → Payment
└────────────────────────────────────────┘

Data Consistency
┌────────────────────────────────────────┐
│ ✅ Order data consistent with payment
│ ✅ Inventory data consistent with orders
│ ✅ User balance consistent with orders
│ ✅ Points data consistent with logs
└────────────────────────────────────────┘
```

### 7.3 Performance Testing Completion Criteria

```
Performance Metrics Achieved

Tier 1: MUST ACHIEVE
┌────────────────────────────────────────┐
│ ✅ API avg response time < 500ms
│ ✅ P95 response time < 1000ms
│ ✅ API error rate < 1%
│ ✅ Page load time < 2s
│ ✅ System availability ≥ 99.5%
└────────────────────────────────────────┘

Tier 2: SHOULD ACHIEVE
┌────────────────────────────────────────┐
│ ✅ CPU usage < 80% (peak load)
│ ✅ Memory usage < 80% (peak load)
│ ✅ System throughput ≥ 1000 req/s
│ ✅ No connection pool overflow
│ ✅ No memory leak (24h running)
└────────────────────────────────────────┘

Load Test Scenarios
┌────────────────────────────────────────┐
│ ✅ 100 concurrent users - stable
│ ✅ 500 concurrent users - sustainable
│ ✅ 1000 concurrent users - within spec
│ ✅ 5000 concurrent users - identify bottleneck
│ ✅ 24h soak test - no critical anomalies
└────────────────────────────────────────┘
```

### 7.4 Security Testing Completion Criteria

```
Security Test Completion

High Risk Vulnerabilities
┌────────────────────────────────────────┐
│ ❌ Zero Tolerance
│ • SQL Injection: 0
│ • XSS: 0
│ • Authentication bypass: 0
│ • Privilege escalation: 0
│ • Remote code execution: 0
│ • Sensitive data exposure: 0
└────────────────────────────────────────┘

Medium Risk Vulnerabilities
┌────────────────────────────────────────┐
│ Fix Rate ≥ 95%
│ • Session management issues
│ • Insecure password storage
│ • Missing rate limiting
│ • Insufficient logging
└────────────────────────────────────────┘

Security Controls Validation
┌────────────────────────────────────────┐
│ ✅ HTTPS transport encryption
│ ✅ Password encrypted storage (irreversible)
│ ✅ JWT token expiration set appropriately
│ ✅ Input validation in place
│ ✅ Permission control logic correct
│ ✅ Audit logging complete
│ ✅ Dependencies free of known vulnerabilities
└────────────────────────────────────────┘
```

### 7.5 Compatibility Testing Completion Criteria

```
Browser Compatibility
┌────────────────────────────────────────┐
│ ✅ Chrome 90+         - Full Support
│ ✅ Firefox 88+        - Full Support
│ ✅ Safari 14+         - Full Support
│ ✅ Edge 90+           - Full Support
│
│ Compatibility Standard:
│ • Page layout correct
│ • Interactions work
│ • Styling correct
│ • Performance consistent
└────────────────────────────────────────┘

Mobile Device Compatibility
┌────────────────────────────────────────┐
│ ✅ iOS 12+ Smartphones
│ ✅ Android 6+ Smartphones
│ ✅ iPad / Android Tablets
│
│ Compatibility Standard:
│ • Responsive layout works
│ • Touch interactions smooth
│ • No crashes
│ • Text readable
└────────────────────────────────────────┘
```

### 7.6 Release Readiness Checklist

#### 7.6.1 Final Sign-Off Checklist

```markdown
Release Readiness Checklist
Version: 1.0.0
Release Date: 2026-XX-XX

Functional Testing
━━━━━━━━━━━━━━━━━━━━━━━━━━━━
- [ ] All new features implemented
- [ ] Requirement coverage ≥ 100%
- [ ] Test case pass rate ≥ 95%
- [ ] P0 defect fix rate 100%
- [ ] P1 defect fix rate ≥ 95%

Performance Testing
━━━━━━━━━━━━━━━━━━━━━━━━━━━━
- [ ] API avg response time < 500ms
- [ ] Page load time < 2s
- [ ] System supports ≥ 1000 req/s
- [ ] 24h soak test passed

Security Testing
━━━━━━━━━━━━━━━━━━━━━━━━━━━━
- [ ] No P0 security vulnerabilities
- [ ] No SQL injection vulnerabilities
- [ ] No XSS vulnerabilities
- [ ] Permission control correct
- [ ] Sensitive data encrypted

Compatibility Testing
━━━━━━━━━━━━━━━━━━━━━━━━━━━━
- [ ] Compatible with major browsers
- [ ] Mobile and tablet adaptation complete
- [ ] Responsive design verified

Deployment & Documentation
━━━━━━━━━━━━━━━━━━━━━━━━━━━━
- [ ] Deployment scripts ready
- [ ] Database upgrade scripts verified
- [ ] User documentation updated
- [ ] API documentation updated
- [ ] Operations manual ready
- [ ] Canary release plan prepared

Team Readiness
━━━━━━━━━━━━━━━━━━━━━━━━━━━━
- [ ] Release team trained
- [ ] Support team on-call
- [ ] Emergency plan ready
- [ ] Rollback plan verified

Sign-off
━━━━━━━━━━━━━━━━━━━━━━━━━━━━
QA Manager:        ________________  Date: ________
Project Manager:   ________________  Date: ________
Tech Lead:         ________________  Date: ________
Product Owner:     ________________  Date: ________
```

---

## Appendices

### Appendix A: Quick Start Guide

**Quick Development Environment Setup (5 mins)**

```bash
# 1. Clone and navigate
git clone https://github.com/your-org/flower-market.git
cd flower-market

# 2. Quick start backend (requires JDK 1.8+, Maven)
cd backend
mvn spring-boot:run

# 3. In new terminal: Quick start frontend (requires Node.js 14+)
cd frontend
npm install && npm run dev

# 4. Access application
# Frontend: http://localhost:5173
# Backend API: http://localhost:8080
# API Docs: http://localhost:8080/doc.html (Knife4j)
```

**Quick Test Environment Setup (15 mins)**

One-command startup with Docker Compose (Recommended)

```yaml
# docker-compose.yml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: test_password
      MYSQL_DATABASE: flower_market_test
    ports:
      - "3306:3306"
    volumes:
      - ./backend/src/main/resources/schema.sql:/docker-entrypoint-initdb.d/1-schema.sql
      - ./backend/src/main/resources/data.sql:/docker-entrypoint-initdb.d/2-data.sql
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost"]
      timeout: 20s
      retries: 10

  redis:
    image: redis:6-alpine
    ports:
      - "6379:6379"
    healthcheck:
      test: ["CMD", "redis-cli", "ping"]
      timeout: 10s
      retries: 5

  backend:
    build:
      context: ./backend
      dockerfile: Dockerfile
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/flower_market_test
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: test_password
      SPRING_REDIS_HOST: redis
    ports:
      - "8080:8080"
    depends_on:
      mysql:
        condition: service_healthy
      redis:
        condition: service_healthy

# Startup command
# docker-compose up -d
```

### Appendix B: FAQ

**Q1: How to isolate test data from production data?**

```
A: Use the following strategies:

1. Physical Isolation
   • Completely separate database and server
   
2. Logical Isolation
   • Mark test data with prefix (_TEST_)
   • Regular cleanup of test data
   
3. Access Control
   • Use different DB accounts
   • Restrict test environment access
```

**Q2: How to simulate real user behavior in performance testing?**

```
A: Use user behavior scripts

1. Analyze real user behavior
   • User login frequency
   • Browse frequency
   • Purchase conversion path
   
2. Create behavior scripts
   • 30% users just browse
   • 15% users browse then add to cart
   • 5% users complete payment
   
3. Configure in JMeter/Locust
   • Random delays for user think time
   • Gradual concurrency ramp-up
```

**Q3: How to quickly identify and fix defects?**

```
A: Establish rapid defect diagnosis

1. Detailed defect reports
   • Clear repro steps
   • Include screenshots & logs
   • Complete environment info
   
2. Automated log collection
   • Application logs
   • DB slow query logs
   • Performance metrics
   
3. Fast fix process
   • P0 defects: fix within 2 hours
   • Reserve regression test time
```

---

## Conclusion

This test strategy document provides a **comprehensive and systematic testing framework** for the flower e-commerce platform.

By strictly following this strategy, we can ensure:

✅ **Product Quality** - Features complete, logic clear, good UX

✅ **System Stability** - Performance met, load balanced, highly available

✅ **Data Security** - Vulnerability-free, strict permission control, data protection

✅ **Continuous Improvement** - Scientific defect management, deep data analysis, continuous learning

---

**Document Maintenance**

| Version | Date | By | Changes |
|---------|------|----|----|
| 1.0 | 2026-03-22 | QA Team | Initial release |

---

**Related Documents**

- [Comprehensive Requirements](comprehensive_requirements.md) - Detailed project requirements
- [Technical Implementation Guide](technical_implementation_guide.md) - Technical implementation details
- [API Documentation](http://api.docs.internal/) - API interface documentation
- [Deployment Guide](deployment.md) - System deployment guide

---

**License**

This document is for internal use only. Unauthorized distribution or modification is prohibited.

© 2026 Flower Market Platform Team. All rights reserved.
