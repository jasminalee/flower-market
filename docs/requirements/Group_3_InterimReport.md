# 1 Introduction

## 1.1 Project Background

The floral consumption market is currently undergoing a significant transformation from seasonal purchases to daily consumption. With socioeconomic development and improvements in residents' quality of life, flowers have evolved from traditional ceremonial gifts into important elements of daily aesthetic living, serving as key components for enhancing life quality and environmental decoration. This shift in consumption patterns has led consumers to demand more comprehensive and professional flower purchasing experiences, including diversified product selection, transparent product traceability, accurate delivery services, and professional maintenance guidance.

 

On the supply side of the industry, traditional floral retail formats face substantial pressure for digital transformation. Small and medium-sized flower shops, as the mainstay of the industry, generally encounter the following development bottlenecks: physical operations are constrained by fixed business hours and limited service areas, making it difficult to expand customer bases beyond geographical restrictions; order management and inventory counting methods that rely on manual recording are inefficient and prone to high error rates; and the lack of effective digital customer relationship management tools hinders the establishment of stable membership systems and the implementation of precise marketing strategies.

 

Although existing comprehensive e-commerce platforms provide online sales channels for flower shops, their generic design architecture presents significant shortcomings: complex operational processes require higher technical skills from employees; platform commission costs squeeze already limited profit margins; and most importantly, the lack of specialized functional modules designed for the characteristics of floral products cannot meet the special needs of the flower industry in areas such as inventory management, product display, and maintenance services.

 

Based on this market situation and technological gap, this project aims to develop a vertical e-commerce platform specifically for the flower industry. The platform will reconstruct the operational model of flower shops through digital technology, creating a professional and convenient flower shopping experience for consumers while providing lightweight, user-friendly management tools for flower shops. This will effectively promote the digital transformation and upgrading of traditional floral retail businesses and support the healthy and sustainable development of the entire industry.

## 1.2 Objectives of the Project

 

This project aims to develop a SpringBoot-based flower e-commerce platform to support the digital transformation of small and medium-sized flower shops and optimize the consumer shopping experience. Specific objectives include: improving operational efficiency, enhancing user experience, driving industry digitalization, building a membership and marketing system, and ensuring system reliability and scalability.

## 1.3 Project Background & Scope

The floral industry is currently at a critical juncture of digital transformation, where traditional small and medium-sized flower shops urgently need to upgrade their operational models through specialized management systems. This project aims to develop a vertical e-commerce platform specifically designed for the floral industry, assisting flower shops in transitioning from traditional manual management to digital management, thereby enhancing their market competitiveness.

**System Scope**

This system will establish a comprehensive digital management platform covering the following core business areas:

**Intelligent Inventory and Order Management**

- Real-time monitoring and     alerts for floral product inventory
- Unified processing of orders     from multiple channels
- Inventory turnover analysis     and intelligent replenishment suggestions

**End-to-End Supply Chain Management**

- Digital Supplier     and procurement processes
- Traceability information     tracking for floral products
- Real-time monitoring of     logistics and delivery status

 

**Customer Relationship Management (CRM)**

- Membership system and points     management
- Customer preference analysis     and precision marketing
- After-sales service and     customer retention

 

**Marketing and Sales Management**

- Promotion campaigns and     coupon management
- Sales data analysis and     report generation
- Seasonal marketing strategy     support

 

**Professional Knowledge Base Management**

- Construction of floral care     knowledge base
- Product information and     traceability content management
- Professional consultation     and customer education

## 1.4 Organisation of the Report

This report is structured to include: an introduction that outlines the project background, objectives, scope, and report organization, clarifying the necessity and implementation direction of the project; the driving question, which explains how software engineering methods support the activities of florists and consumers, reflecting the technical implementation path of the project; problem findings, analyzing 15 core operational challenges faced by traditional flower shops and proposing corresponding system solutions; system requirements and architecture, detailing the functional and non-functional requirements, overall architecture, and data models of the system; problem analysis documentation, using UML models such as use case diagrams, use case descriptions, and class diagrams to model and analyze each system module; detailed design documentation, showcasing entity-relationship diagrams, data design, software/hardware architecture, and user interface design; critical evaluation, summarizing the problems encountered during project implementation, schedule adjustments, system limitations, and lessons learned; detailed project plan, describing the development process, iterative methods, and project timeline; and references, listing the books, reports, and technical documents referenced during the project development process.

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

# 2 Driving Question

The driving question for this project is: “How can Software Engineering techniques be used to develop software systems for supporting human activities?” In this flower market system project, Software Engineering techniques are applied in multiple aspects to effectively support the activities of both consumers and small and medium-sized flower shop operators.

Firstly, in the system design phase, object-oriented analysis and design techniques are used. By modeling the real-world entities in the flower retail industry, such as users, products, orders, into classes and objects, the system can better simulate the actual business processes. For example, the product class includes specialized attributes like bloom period and care difficulty, which are in line with the characteristics of flower products, enabling the system to accurately represent and manage flower-related information, thereby supporting flower shop operators' product management activities.

Secondly, the adoption of a frontend-backend separation architecture (using SpringBoot for the backend and Vue.js for the frontend) is a typical application of software architecture design techniques. This architecture separates the presentation layer from the business logic layer, allowing for independent development and maintenance of the frontend and backend. For consumers, the responsive design of the frontend ensures a smooth shopping experience across different devices, supporting their multi-channel shopping activities. For developers, it facilitates parallel development, improving development efficiency and supporting the collaborative development activities of the project team.

Thirdly, agile development methodology is employed, which is a key Software Engineering process technique. Through iterative development and testing, the project team can continuously collect feedback from users (flower shop operators and consumers) and make timely adjustments to the system. For example, in each iteration, new functions can be developed and tested, and based on user feedback, existing functions can be optimized. This ensures that the final system better meets the actual needs of users, effectively supporting their daily operations and shopping activities.

Additionally, software testing techniques play a crucial role in supporting human activities. Various testing methods such as unit testing, integration testing, and system testing are used to ensure the reliability and stability of the system. For flower shop operators, a stable system ensures the smooth progress of daily operations such as order processing and inventory management, avoiding losses caused by system failures. For consumers, it guarantees a trouble-free shopping experience, enhancing their trust in the platform.

Data management techniques, including the use of MySQL for relational data storage and Redis for caching, also support human activities. Efficient data storage and retrieval ensure that flower shop operators can quickly access and manage business data, such as querying inventory levels and sales statistics, to make informed business decisions. For consumers, fast data loading speeds reduce page waiting time, improving their shopping efficiency.

In summary, by applying a series of Software Engineering techniques in system design, architecture, development methodology, testing, and data management, this flower market system effectively supports the daily operations of small and medium-sized flower shops and the shopping activities of consumers, answering the driving question.

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

# 3 Problem Findings

## 3.1 Product Information Relies on Manual Recording, Prone to Errors and Difficult to Maintain

Offline flower shops commonly use manual methods to record product information. It is challenging to achieve unified and accurate digital management of details like names, prices, and attributes for items like fresh flowers, green plants, and succulents, particularly lacking specialized management for professional attributes such as blooming periods and maintenance characteristics.

**Solution: Establish a Unified Digital Product Information Management System**

Enable unified digital management of product information, support multi-dimensional categorization and professional attribute management, providing an accurate data foundation for subsequent processes like inventory and orders.

## 3.2 Inability to Establish a Membership System, Leading to Poor Customer Retention

Offline flower shops struggle to build an effective membership system, making it impossible to identify and retain user identities. This hinders any efforts in precision marketing and personalized services, resulting in customers mostly being one-time transactions.

 

**Solution: Build a User Registration and Login System**

Provide a secure identity authentication mechanism, support multi-channel login to lower the barrier, laying the groundwork for building a membership system and increasing customer lifetime value.

 



## 3.3 Users Need to Repeatedly Place Orders, Resulting in Poor Purchase Experience

Especially in scenarios like gift bouquet combinations, users are unable to temporarily save or batch process desired items, requiring repeated order submissions. This leads to cumbersome processes and low purchasing efficiency.

 

**Solution: Deploy Shopping Cart Functionality**

Support batch operations on items and real-time price calculation, temporarily save user selections, simplify the purchase process, and effectively improve purchase conversion rates and user experience.

## 3.4 Order Management is Chaotic, Prone to Errors and Loss

Order management in offline flower shops relies on manual records, often causing disorder, omissions, or loss. During peak seasons like holidays, order processing efficiency is low and errors occur frequently.

**Solution: Create a Full-Process Order Management System**

Provide complete order status tracking and automated processing, support special requirements like scheduled flower delivery, improving processing efficiency and user trust.

## 3.5 Inventory Relies on Manual Counting, Inefficient and Error-Prone

Offline flower shops use manual methods for inventory counting, which is inefficient and prone to errors. This often leads to stockouts of popular flowers or overstocking of perishable goods, resulting in losses.

**Solution: Establish an Intelligent Inventory Management Module**

Implement real-time inventory monitoring and warning mechanisms, support batch management and intelligent replenishment suggestions, ensuring data accuracy and reducing inventory risks.

## 3.6 Severe Customer Churn and Low Repurchase Rate

Offline flower shops lack effective customer retention methods, making it difficult to re-engage users after transactions, leading to severe customer churn and a low repurchase rate.

 

**Solution: Introduce a Membership Points and Tier System**

Enhance user stickiness and loyalty through points incentives and membership tiers, support points redemption, creating a virtuous cycle of the membership ecosystem.

## 3.7 New Customers Lack Reference for Decision-Making, Leading to Difficult Conversion

Especially for visual products like flowers, new users struggle with purchase decisions in the absence of user reviews, making it difficult to build trust.

**Solution: Build a Product Review and Display System** 

Display genuine user reviews and images, collect user feedback, build a bridge of trust among users, lower the decision-making barrier, and improve conversion rates.

## 3.8 Operational Decisions Lack Data Support, Relying on Experience

Shop operators cannot grasp key business metrics like sales, order volume, and popular products in real-time, making it difficult to perform scientific data analysis and strategy optimization.

**Solution: Provide a Data Statistics and Analysis Dashboard**

Monitor key business indicators in real-time, provide multi-dimensional sales data analysis, and support data-driven operational decisions and refined management.

## 3.9 Promotional Activities are Inefficient with Poor Results

Offline flower shops have single and inefficient promotional methods, struggling to effectively stimulate consumption, increase average order value, and boost sales volume, especially during holiday marketing.

**Solution: Establish a Flexible and Diverse Coupon System**

Stimulate consumption by issuing various types of coupons, achieve precision marketing, improve promotional campaign effectiveness, and enhance market competitiveness.

## 3.10 Supplier Information is Chaotic, Leading to High Procurement Costs

Flower shops have chaotic supplier information management and inefficient procurement processes, lacking an evaluation system for supplier quality and performance, which affects source quality and costs.

**Solution: Build a Digital Supplier System**

Optimize the procurement process, establish a supplier evaluation system, support price comparison and performance management, ensure source quality, and reduce procurement costs.

## 3.11 Consumers Lack Maintenance Knowledge, Affecting Experience

After purchasing flowers, consumers lack professional care guidance, leading to quick deterioration of flowers, poor experience, and potential after-sales issues, epitomizing the "easy to buy, hard to maintain" problem.

**Solution: Create a Professional Flower Care Knowledge Base**

Provide professional maintenance guidance for different scenarios, reduce after-sales problems, enhance the platform's professional value and user stickiness, and create a differentiated competitive advantage.

## 3.12 Users Cannot Save Desired Products, Hampering Repurchase

Users cannot effectively collect or record products they are interested in, especially in scenarios with periodic gifting needs, which is not conducive to facilitating repeat purchases.

**Solution: Add Product Favorites and Notification Features**

Allow users to manage products of interest, support price drop alerts, provide a data basis for personalized recommendations, increase sales opportunities, and enhance user activity.

## 3.13 Product Quality Information is Not Transparent, Making Trust Difficult to Build

Consumers lack understanding of quality information such as the origin and cultivation process of flower products, affecting purchase confidence and making it difficult to build brand trust.

**Solution: Display Product Traceability Information**

Visually display traceability information like product origin and cultivation process, enhance product credibility and brand transparency, and create a differentiated competitive advantage.

## 3.14 Users Have Difficulty Choosing, High Decision-Making Cost

Faced with a wide variety of flower products, users can easily struggle with choice, especially for emotionally-driven products like flowers, reducing browsing and purchasing efficiency.

**Solution: Introduce Intelligent Popular Recommendation Features**

Provide smart recommendations based on user behavior, seasons, and holidays, increase product exposure, help users discover more products of interest, and improve sales conversion and experience.

## 3.15 Lack of User Interaction, Difficulty Cultivating Loyalty

There is a lack of daily, lightweight interaction mechanisms between the flower shop and its users, resulting in low visit frequency and difficulty in cultivating and maintaining loyalty.

**Solution: Establish a Membership Check-in Points System**

Cultivate user habits through simple mechanisms like earning points for daily check-ins, increase platform visit frequency, collect behavioral data, and strengthen user interaction and loyalty.

 

 

# 4 System Requirements and Architecture

## 4.1 Overall Architecture of the Proposed System

The proposed flower market system adopts a B/S (Browser/Server) architecture with frontend-backend separation to enhance scalability and maintainability.

### 4.1.1 Frontend Architecture

The frontend uses Vue.js along with the Element-UI component library for development, which provides a rich set of UI components and supports responsive design. The frontend is divided into three main modules: user side, merchant side, and administrator side. Each module has independent page routes and state management. Vue Router is used for page routing management, enabling smooth page switching and navigation. Vuex is used for state management, ensuring consistent data sharing among different components. The frontend communicates with the backend through RESTful APIs, sending requests and receiving responses to achieve data interaction. Additionally, the frontend uses Axios for HTTP request handling, implementing functions such as request interception and response interception to handle request headers, authentication, and error responses uniformly.

 

### 4.1.2 Backend Architecture

The backend is built using the Spring Boot framework, which simplifies the development and deployment of Java applications. The backend follows a layered architecture, including the presentation layer, business logic layer, data access layer, and domain model layer.

•   Presentation Layer: This layer is responsible for receiving frontend requests and returning responses. It uses Spring MVC to implement RESTful APIs, handling request parameters, data validation, and response formatting. JWT (JSON Web Token) is used for user authentication and authorization, ensuring the security of API access.

•   Business Logic Layer: This layer contains the core business logic of the system, such as product management, order processing, and inventory control. It is implemented through service classes, which call methods from the data access layer to operate on the database and implement business rules. Transaction management is used to ensure the consistency of business operations, such as ensuring that inventory is reduced simultaneously when an order is created.

•   Data Access Layer: This layer is responsible for interacting with the database. It uses MyBatis as the ORM (Object-Relational Mapping) framework to map database tables to Java objects, simplifying database operations. The data access layer provides methods for adding, deleting, modifying, and querying data, which are called by the business logic layer.

•   Domain Model Layer: This layer defines the entity classes corresponding to the database tables, such as User, Product, Order, and Inventory. These entity classes contain the attributes of the corresponding entities and provide getter and setter methods for data access.

 

### 4.1.3 Data Storage Architecture

The system uses MySQL as the relational database for storing core business data, such as user information, product data, order records, and inventory data. MySQL provides reliable data storage and efficient query capabilities, supporting complex SQL operations and transaction management. To improve the system's performance, especially the access speed of popular product data, Redis is used as the caching database. Redis caches frequently accessed data, such as popular product information and user session data, reducing the number of database accesses and improving the system's response speed.

 

### 4.1.4 Deployment and Security Architecture

Nginx is used as a static resource server and reverse proxy server. As a static resource server, it stores and serves static resources such as frontend pages, images, and videos, reducing the load on the backend server. As a reverse proxy server, it distributes client requests to multiple backend servers, achieving load balancing and improving the system's concurrency handling capability. Additionally, Nginx provides security features such as HTTPS encryption, preventing data leakage during transmission. The system also implements security measures such as input validation, SQL injection prevention, and XSS (Cross-Site Scripting) attack prevention to ensure the security and stability of the system.

## 4.2 Scope of the Proposed System

The flower trading platform based on SpringBoot will focus on meeting the core needs of small and medium-sized flower shops and consumers in the flower retail industry. The specific scope includes:

Merchant side: The system will cover commodity management (including management of exclusive attributes such as flowering period and maintenance difficulty), inventory management (real-time monitoring, batch management, inventory warning), order management (order status tracking, automatic processing, scheduled delivery), customer management (membership system, points management, user tags), and data statistics (sales statistics, hot commodity analysis, operation dashboard), helping merchants realize digital management of daily operations, improve efficiency and reduce costs.

Consumer side: The system will provide commodity browsing and searching (multi-dimensional classification, precise retrieval), commodity detail viewing (including traceability information, maintenance guidelines), shopping cart (batch operations, real-time pricing), order placement and tracking (scheduled delivery, logistics inquiry), membership benefits (points accumulation and redemption), commodity evaluation (rating, picture sharing), and personalized recommendations (based on user preferences and purchase history), enhancing consumers' shopping experience and providing transparent information and convenient services.

The system does not include complex supply chain management required for large-scale flower wholesale (such as global procurement, multi-level distribution), nor does it involve high-end customized floral services that require offline interaction and integration with professional design software, because the core of this project is to provide a lightweight and universal solution for small and medium-sized flower shops.



## 4.3 Functional Requirements

### 4.3.1 Product Supply Management

**Product Information Management**

- Product Archive Maintenance     (SKU/Price/Image & Description Details)
- Product Category Management     (Category/Tags/Attributes)
- Product Status Management     (Listing/Delisting/Stock)

**Supplier**

- Supplier Files     (Qualifications/Contracts/Ratings)
- Procurement Management     (Planning/Orders/Acceptance)
- Supplier Evaluation     (Quality/Delivery Time/Service)

**Traceability Management**

- Traceability Information     Collection (Origin/Batch/Process)
- Traceability Information     Display (QR Code/Detail Page)

### 4.3.2 Inventory Management

**Inventory Monitoring**

- Real-time Inventory Inquiry     (Multi-warehouse/Real-time Data)
- Inventory Alerts (Upper     & Lower Limits/Expiry Alerts)
- Inventory Analysis (Turnover     Rate/Aging Analysis)

**Warehouse Operations**

- Inbound Management (Purchase     Inbound/Return Inbound)
- Outbound Management (Sales     Outbound/Transfer Outbound)
- Stocktake Management     (Regular Stocktake/Discrepancy Handling)

**Batch Management**

- Batch Tracking (Production     Date/Shelf Life)
- FIFO (First-In, First-Out)     Management
- Batch Query & Statistics.

 

### 4.3.3 Sales Order Management

**Order Processing**

- Order Creation     (Multi-channel Access)
- Order Review (Automated     Workflow)
- Status Tracking (Real-time     Status Updates)

**Customer Service**

- After-sales Service (Returns     & Exchanges/Refunds)
- Complaint Handling     (Recording/Tracking/Follow-up)
- Customer Care     (Follow-up/Satisfaction)

**Order Analysis**

- Sales Statistics     (Multi-dimensional Data Analysis)
- Order Analysis (Conversion     Rate/Average Order Value)
- Trend Forecasting (Sales     Forecasting)

 

### 4.3.4 Membership & Marketing Management

**Membership System**

- Membership Tiers (Tier     Rules/Benefits)
- Points Management     (Earning/Usage/Redemption)
- Member Services (Dedicated     Support/Privileges)

**Marketing Activities**

- Coupon Management     (Creation/Distribution/Redemption)
- Promotional Activities     (Spend-Based Discounts/Percentage Discounts/Bundles)
- Activity Analysis     (Performance Tracking/Optimization)

**Precision Marketing**

- User Profiles (Behavior     Analysis/Tagging)
- Personalized Recommendations     (Products/Content)
- Marketing Outreach     (Multi-channel Push)

### 4.3.5 Content Operations Management

**Knowledge Base Management**

- Care Knowledge (Professional     Content Library)
- Content Categorization     (Multi-dimensional Classification)
- Content Updates (Regular     Maintenance)

**Review Management**

- Review Collection     (Product/Service Reviews)
- Review Moderation (Content     Quality Control)
- Review Analysis (Issue     Identification)

**Intelligent Q&A**

- FAQ Library (Standard     Q&A)
- Intelligent Customer Service     (Auto-Response)
- Q&A Optimization     (Continuous Improvement)

 

### 4.3.6 Logistics & Delivery Management



**Delivery Management**

- Delivery Areas     (Coverage/Timeframe Settings)
- Shipping Fee Templates (Rule     Configuration)
- Delivery Dispatch (Route     Optimization)

**Logistics Tracking**

- Track & Trace (Real-time     Location)
- Status Updates (Automatic     Synchronization)
- Exception Alerts (Issue     Notifications)

**Shipping Management**

- Order Processing (Batch     Shipping)
- Shipping Label Printing     (Electronic Labels)
- Shipping Confirmation     (Status Update)

### 4.3.7 Data Analysis & Reporting Management

**Business Data Analysis**

- Sales Data Analysis:     Multi-dimensional analysis of sales performance and customer purchasing     behavior.
- Inventory Data Analysis:     Analyze inventory turnover rates and capital occupancy.
- Customer Behavior Analysis:     In-depth analysis of customer preferences and consumption habits.

**Intelligent Decision Support**

- Visual Data Dashboard:     Real-time display of key business indicators and data trends.
- Business Forecasting &     Analysis: Predict sales trends and inventory needs based on historical     data.
- Anomaly Alerts &     Notifications: Automatically identify business anomalies and send alert     notifications.



## 4.4 Non-functional requirements

### 4.4.1 Performance Requirements

- Response Time: Page load     time ≤ 3 seconds, critical     operation response time ≤ 2     seconds.
- Concurrency Handling:     Support at least 1,000 simultaneous online users and 500 concurrent user     operations.
- Data Processing: Response     time for large-scale data queries ≤ 5 seconds.
- System Capacity: Support at     least 100,000 product records and millions of user records.

### 4.4.2 Security Requirements

- Data Encryption: User     passwords encrypted using BCrypt, sensitive data transmitted via HTTPS.
- Access Control: Role-based     access control (RBAC) with differentiated permissions for users.
- Attack Prevention:     Protection against common cyber threats (e.g., SQL injection, XSS     attacks).
- Data Backup: Regular     automated backups with support for disaster recovery.

### 4.4.3 Reliability Requirements

- System Availability: 99.9%     uptime, with annual downtime not exceeding 8 hours.
- Fault Recovery: Service     restoration within 30 minutes of system failure.
- Data Consistency: Ensure     eventual consistency of data in distributed environments.
- Fault Tolerance: Single     points of failure do not impact overall system operation.

### 4.4.4 Usability Requirements

- User Interface: Simple and     intuitive interface with user-friendly workflows.
- design.
- Operational Efficiency:     Frequently used operations completed within 3 steps.
- Documentation: Comprehensive     online help and user guides.

### 4.4.5 Maintainability Requirements

- Code Standards: Adherence to     unified coding conventions and design patterns.
- Modular Design: Low coupling     between system modules for independent maintenance and upgrades.
- Logging: Complete operation     and system logs for tracking.
- Monitoring & Alerts:     Real-time system monitoring with automatic alerts for anomalies.

### 4.4.6 Scalability Requirements

- Architecture:     Microservices-based architecture supporting horizontal scaling.
- API Standards: Standardized     API interfaces for third-party integration.
- Plugin Mechanism: Support     for plug-in extensions of functional modules.
- Configuration Flexibility:     System parameters adjustable via configuration files.

### 4.4.7 Compatibility Requirements

- Browser Compatibility:     Support for mainstream browsers (Chrome, Firefox, Safari, Edge).
- Mobile Compatibility:     Accessible on iOS and Android systems.
- Resolution Adaptation:     Adaptive display support for various screen resolutions.
- Data Format Compatibility:     Support for import/export of common data formats.

### 4.4.8 User Experience Requirements

- Page Loading: Optimized     first-screen load time with progressive image loading.
- Interactive Feedback:     Immediate operation feedback and loading status indicators.
- Error Handling:     User-friendly error messages with suggested solutions.
- Personalized Services:     Content recommendations based on user behavior.

## 4.5 data description

### 4.5.1 user data

Basic Information: Includes username, encrypted password , phone number (uniquely verified), profile picture (stored as URL), gender (male/female), and date of birth (used for personalized birthday promotions).

Address Information: A delivery address book containing recipient name, phone number, detailed address (province/city/district/street and house number), postal code, and address tags (Home/Office/Other), with support for setting a default address.

Behavioral Data: Browsing history (product ID, view time, duration), search history (search keywords, search time), product favorites (favorited product ID, time favorited), and check-in records (check-in date, points earned).

Membership System Data: Points balance (total accumulated points), membership tier , growth value (used for tier upgrades), and coupon holdings (coupon ID, type, value, usage conditions, validity period).

Special Fields: User floral preference tags (e.g., prefers cut flowers/succulents/potted plants) and frequent recipient information .

### 4.5.2 Product data

Basic Information: Product ID (unique identifier), flower name (e.g., "red rose", "succulent combination potted plant"), flower type (fresh cut flowers / potted plants / preserved flowers / gift bouquets), price (original price, promotional price), stock quantity (current available quantity).

Professional Attributes of Flowers: Flowering period (e.g., fresh-cut roses last 7-10 days, potted lilies are perennial), maintenance difficulty (easy / moderate / difficult), suitable environment (temperature, light, humidity requirements), flower language, origin traceability information (origin, planting base, picking time).

Multimedia data: Main product images (5-8 images), detailed images (showing details), 360° display videos, maintenance tutorial videos (URL storage).

Classification system data: Multi-level classification (e.g., "Flowers → Fresh cut flowers → Roses → Red roses"), product labels (new arrival, best-selling, holiday limited).

Product specification data: Support for multiple specifications (such as number of stems, pot diameter, packaging type), each specification corresponding to an independent price and inventory.

### 4.5.3 Order data

Basic Information: Order number (unique identifier), user ID, order amount (total price after discounts), payment status (unpaid/paid/refunded), order status (pending payment/paid/preparing/shipped/out for delivery/completed/cancelled).

Delivery Information: Desired delivery date, delivery time slot, latest shipment time.

Product Information: Product ID, purchase quantity, specifications, product snapshot (information backup at the time of order placement).

Logistics Information: Shipping address, logistics company, tracking number, delivery status.

Timestamps: Order time, payment time, shipment time, completion time.

### 4.5.4 inventory data

Real-time Data: Current sellable stock, locked stock (reserved for unpaid orders).

Batch Data: Batch number, storage time, shelf life (specific to fresh cut flowers), freshness grade (Grade A/B/C).

Inventory Logs: Change type (inbound/outbound/return/loss), quantity, timestamp, operator.

Alert Data: Safety stock threshold, alert notification records (timestamp, product ID, stock quantity).

### 4.5.5 Evaluation and interaction data

Product Review Data: Rating (1-5 stars), review content, images/videos (URLs), merchant response, review timestamp, user ID.

Interaction Data: Review likes count, product favorites records, Q&A interactions (customer questions, merchant answers).

Professional Evaluation Data: Flower freshness rating, delivery packaging rating, additional comments.

### 4.5.6 Marketing data

Coupon Information: Name, type (fixed amount / percentage discount), face value, usage conditions, validity period, issued/used/remaining quantity.

Campaign Data: Promotion name, duration, participating products, performance (number of orders, sales volume, revenue).

Points Ledger Data: Change type (points earned / points redeemed / points expired), amount, balance, timestamp.

### 4.5.7 Traceability data

Origin Information: Coordinates of the planting location, base name, picking time, records of the planting process (fertilizers, watering, pest and disease control).

Timeline data: Key nodes from planting to delivery (sowing / seedling raising / picking / shipping / delivery) along with descriptions and pictures.

### 4.5.8 Knowledge base data

Maintenance article data: Title, main text, author, applicable flowers, release time, number of reads, number of likes.

FAQ data: Question, answer, category (maintenance / delivery / after-sales), usefulness rating.

Seasonal characteristic data: Seasonal maintenance knowledge, recommended holiday flower themes.

### 4.5.9 After-sales data

Work Order Data: Request type (wilted / damaged / wrong item shipped), processing progress, user ID, order number, submission time, supporting pictures.

Processing Details Data: Refund amount, responsible person, processing time, result, user satisfaction score.

Problem Analysis Data: Problem classification, delivery issue statistics, root cause analysis (inventory backlog / logistics rough handling, etc.).

# 1 Problem Analysis Documentation

## 1.1 Actor Description

| **Actor name**: Customer                                     |
| ------------------------------------------------------------ |
| **Description**: Core users who browse products, search, place orders,  manage shopping carts, check order status, apply for after-sales services,  track logistics, and use membership benefits on the flower e-commerce  platform. |

 

| **Actor name**: Merchant                                     |
| ------------------------------------------------------------ |
| **Description**:Operators and managers of flower shops,  responsible for comprehensive store operations including product information  maintenance, order processing, inventory management, marketing campaign  setup, membership management, and customer service. |

 

| **Actor name**: Administrator                                |
| ------------------------------------------------------------ |
| **Description**: Backend system managers responsible for user permission  allocation, system parameter configuration, data maintenance, system  monitoring, and security assurance for technical support and system  maintenance. |

 

| **Actor name**: System (Automatic)                           |
| ------------------------------------------------------------ |
| **Description**: Built-in system function modules that  automatically execute tasks including inventory alerts, automatic order  review, status synchronization, and data backup. |