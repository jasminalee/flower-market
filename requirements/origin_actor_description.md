# 1 Documentation for Problem Analysis

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

 

## 1.2 Use case description

| **Use case name:**                     | Browse Product                                               |
| -------------------------------------- | ------------------------------------------------------------ |
| **Use case ID:**                       | UC-101                                                       |
| **Primary actor:**                     | Customer                                                     |
| **Secondary actor(s):**                | None                                                         |
| **Brief description:**                 | The customer browses product information  (including details, prices, images, care knowledge, and user reviews) on the  flower e-commerce platform to understand products and support purchase  decisions. |
| **Preconditions:**                     | 1. The customer has accessed the flower  e-commerce platform (logged in or in guest mode).  2. The platform has completed product  information upload and display configuration. |
| **Flow of events:**                    | 1. Customer accesses the platform's  product browsing page (e.g., homepage recommendations, category pages).  2. System displays product lists based on  the access path (e.g., recommended, categorized products).  3. Customer selects a specific product to  view detailed information.  4. System presents comprehensive product  details: name, price, SKU, images, blooming period, maintenance requirements,  traceability information, and user reviews.  5. Customer can switch to other products  or exit the browsing page. |
| **Postconditions:**                    | 1. The customer successfully views the  desired product information.  2. System optionally records customer  browsing behavior for subsequent personalized recommendations. |
| **Alternative  flows and exceptions:** | - If the product is delisted/out of  stock, the system displays a "delisted" or "out of stock"  prompt on the product card/detail page.- If the network is interrupted, the  system shows a network error prompt and supports page reloading. |
| **Non-behavior requirements:**         | - Product images and information must  load within 3 seconds for smooth browsing.- The system should support product  filtering (price, category, freshness) and sorting (popularity, latest  arrival) to facilitate screening. |
| **Assumptions:**                       | 1. The customer’s device (computer,  mobile phone) supports platform access (compatible browser/APP version).  2. The platform’s product database is  updated in real time with complete information. |
| **Issue:**                             |                                                              |
| **Source:**                            |                                                              |

 

 

 

 

| **Use  case name:**                    | Configure Product Parameters                                 |
| -------------------------------------- | ------------------------------------------------------------ |
| **Use  case ID:**                      | UC-102                                                       |
| **Primary  actor:**                    | Administrator                                                |
| **Secondary  actor(s):**               | None                                                         |
| **Brief  description:**                | The administrator configures global  product-related parameters (product attribute templates, price range limits,  stock alert thresholds, traceability information fields) on the flower  e-commerce platform to standardize product management. |
| **Preconditions:**                     | 1. The administrator has logged in to the  backend management system and has "product parameter configuration"  permission.  2. The system has initialized the basic  parameter configuration module. |
| **Flow  of events:**                   | 1. Administrator navigates to the  "product parameter configuration" module in the backend.  2. System displays current product  parameter settings   3. Administrator modifies parameters  (e.g., adds "flower preservation period" as a mandatory attribute,  adjusts low-stock alert from 5 to 10)  .4. Administrator submits the modified  configuration for confirmation.  5. System verifies parameter validity and  saves the configuration.  6. System prompts "configuration  saved successfully" and updates the parameter application scope . |
| **Postconditions:**                    | 1. Product parameters are updated  successfully and take effect globally.  2. The system records the administrator’s  parameter modification in the operation log. |
| **Alternative  flows and exceptions:** | - If invalid parameters are input, the  system rejects submission and prompts "Please enter a positive number  for the stock alert threshold."- If configuration fails to save due to  database error, the system shows "Save failed, please try again  later" and retains original settings. |
| **Non-behavior  requirements:**        | - All parameter modification operations  (operator, time, before/after values) must be recorded for audit.- The system  should provide a "parameter reset" function to restore default  settings for misconfiguration. |
| **Assumptions:**                       | 1. The administrator understands  parameter meanings and impacts to avoid incorrect configuration.  2. The backend database runs normally to  support parameter storage and reading. |
| **Issue:**                             |                                                              |
| **Source:**                            |                                                              |

 

| **Use  case name:**                    | Classification of Management Systems                         |
| -------------------------------------- | ------------------------------------------------------------ |
| **Use  case ID:**                      | UC-103                                                       |
| **Primary  actor:**                    | Administrator                                                |
| **Secondary  actor(s):**               | None                                                         |
| **Brief  description:**                | The administrator manages the system’s  functional classification and permission classification, including defining  backend module classifications and  classifying user roles by permission scope to standardize system operation  and access control. |
| **Preconditions:**                     | 1. The administrator has logged in to the  backend management system and has "system classification  management" permission.  2. The system has a built-in basic  classification framework. |
| **Flow  of events:**                   | 1. Administrator enters the "system  classification management" module.  2. Functional module classification:  a. Administrator views the current module  structure.b. Administrator adds, deletes, or adjusts module hierarchy (e.g.,  adds "traceability management" as a sub-module of "product  management"). c. System saves adjustments and updates the backend menu  display.  3. User role classification:  a. Administrator views existing roles and  their permission scopes (e.g., "merchant" manages listings but not  global parameters). b. Administrator modifies role permissions or adds new  roles.c. System verifies permission logic and saves settings.  4.System prompts "classification  management updated successfully." |
| **Postconditions:**                    | 1. Functional module and user role  classifications are updated and take effect.2. The system records the  administrator’s adjustment operations in the system log. |
| **Alternative  flows and exceptions:** | - If deleting a core module, the system  rejects and prompts "Cannot delete core modules; adjust hierarchy  instead."- If assigning conflicting permissions, the system highlights  conflicts and requires adjustment before saving. |
| **Non-behavior  requirements:**        | - After role classification modification,  existing user accounts under the role automatically inherit updated  permissions without reconfiguration.- The system should provide a visual  editing interface to improve efficiency. |
| **Assumptions:**                       | 1. The administrator is familiar with the  system’s functional architecture and permission logic to avoid irrational  adjustments.  2. The system’s permission control engine  supports dynamic role permission updates. |
| **Issue:**                             |                                                              |
| **Source:**                            |                                                              |

 

| **Use  case name:**                    | Maintain Product Information                                 |
| -------------------------------------- | ------------------------------------------------------------ |
| **Use  case ID:**                      | UC-104                                                       |
| **Primary  actor:**                    | Merchant                                                     |
| **Secondary  actor(s):**               | None                                                         |
| **Brief  description:**                | The merchant updates and maintains  product details (prices, images, maintenance instructions, SKU information)  on the flower e-commerce platform to ensure accurate and up-to-date product  information for customers. |
| **Preconditions:**                     | 1. The merchant has logged in to the  merchant management system and has "product information  maintenance" permission.2. The platform has initialized the product  information management module (with default attribute templates). |
| **Flow  of events:**                   | 1. Merchant navigates to the  "product information management" module and selects a product to  edit.2. Merchant updates product details (e.g., adjusts rose price from 50 to  55, uploads new images, modifies maintenance instructions).3. System verifies  information validity (e.g., positive price, image size compliance).4.  Merchant submits the updated information for confirmation.5. System saves the  updates and synchronizes the revised product details to the front-end product  page.6. System prompts "product information maintained  successfully." |
| **Postconditions:**                    | 1. Product information is updated  accurately and displayed correctly on the front end.2. The system records the  merchant’s maintenance operation in the merchant operation log. |
| **Alternative  flows and exceptions:** | - If the merchant inputs incomplete  mandatory information (e.g., missing product price), the system prompts  "Please complete all mandatory fields (price, product name, main image)  before submission."- If image upload fails due to format error, the  system shows "Unsupported image format; please use JPG/PNG files"  and allows re-upload. |
| **Non-behavior  requirements:**        | - The system must retain historical  versions of product information (operator, modification time, before/after  content) for traceability.- The system should support batch maintenance of  product information (e.g., updating prices for multiple products in bulk) to  improve efficiency. |
| **Assumptions:**                       | 1. The merchant provides true and  accurate product information to avoid misleading customers.2. The system’s  front-end and back-end data are synchronized in real time to ensure  consistency. |
| **Issue:**                             |                                                              |
| **Source:**                            |                                                              |

 

 

| **Use  case name:**                    | Manage Product Listing/Delisting                             |
| -------------------------------------- | ------------------------------------------------------------ |
| **Use  case ID:**                      | UC-105                                                       |
| **Primary  actor:**                    | Merchant                                                     |
| **Secondary  actor(s):**               | None                                                         |
| **Brief  description:**                | The merchant controls the online status  of products (listing new products, delisting out-of-stock/obsolete products)  on the flower e-commerce platform to ensure the front-end product list  matches actual supply. |
| **Preconditions:**                     | 1. The merchant has logged in to the  merchant management system and has "product listing/delisting  management" permission.  2. The product to be managed has  completed information maintenance. |
| **Flow  of events:**                   | 1. Merchant enters the "product  status management" module and views the current listing status of  products.  2. Product listing:  a. Merchant selects a non-listed product  and clicks "list." b.  Merchant sets the listing time. c. System confirms the listing settings and  updates the product status to "to be listed" or "listed".  3. Product delisting:  a. Merchant selects a listed product and  clicks "delist."  b.  Merchant enters the delisting reason.   c. System updates the product status to "delisted" and  removes it from the front-end product list.  4. System prompts "product  listing/delisting operation completed successfully." |
| **Postconditions:**                    | 1. Product listing/delisting status is  updated correctly and synchronized to the front end.  2. The system records the operation in the log. |
| **Alternative  flows and exceptions:** | - If listing a product with incomplete  information, the system prompts "Product information is incomplete;  complete it before listing."- If delisting a product with pending  orders, the system warns "This product has 3 pending orders; confirm  delisting?" and proceeds only after merchant confirmation. |
| **Non-behavior  requirements:**        | - The system must ensure delisted  products are no longer searchable/displayed to customers but retain  historical data for future reference.- The system should send a reminder 24  hours before a scheduled listing to confirm the merchant’s intent. |
| **Assumptions:**                       | 1. The merchant’s listing/delisting  decisions align with actual inventory and supply status.  2. The system’s front-end product search  and display modules respond in real time to status changes. |
| **Issue:**                             |                                                              |
| **Source:**                            |                                                              |

 

| **Use  case name:**                    | Manage Supplier Information                                  |
| -------------------------------------- | ------------------------------------------------------------ |
| **Use  case ID:**                      | UC-106                                                       |
| **Primary  actor:**                    | Merchant                                                     |
| **Secondary  actor(s):**               | Administrator (for supplier qualification  review, optional) |
| **Brief  description:**                | The merchant adds, edits, and reviews  supplier information on the platform to maintain a clear and reliable  supplier database for procurement. |
| **Preconditions:**                     | 1. The merchant has logged in to the  merchant management system and has "supplier information  management" permission.  2. The platform has initialized the  Supplier module. |
| **Flow  of events:**                   | 1. Merchant accesses the "supplier  management" module and selects an operation.  2. Add supplier:  a. Merchant fills in supplier details:  name, contact person, phone number, email, qualification certificates, supply  scope. b. Merchant submits the information.c. System stores the information  and marks the status as "pending review"or "active".  3. Edit supplier: a. Merchant selects an  existing supplier and updates details. b. System verifies the updated  information and saves it.  4. View supplier:a. Merchant selects a  supplier to view detailed information, including historical cooperation  records.  5. If administrator review is required:  After the administrator approves the supplier, the system updates the status  to "active" and notifies the merchant.  6. System prompts "supplier  information managed successfully." |
| **Postconditions:**                    | 1. Supplier information is added/updated  correctly and stored in the database.2. The system records the merchant’s  operation and review status in the log. |
| **Alternative  flows and exceptions:** | - If the merchant uploads invalid  qualification certificates, the system prompts "The uploaded certificate  has expired; please upload a valid one."- If the administrator rejects  the supplier application, the system notifies the merchant with the rejection  reason and allows re-submission after  revision. |
| **Non-behavior  requirements:**        | - The system must encrypt sensitive  supplier information to ensure data  security.- The system should support supplier classification to facilitate quick retrieval. |
| **Assumptions:**                       | 1. The merchant provides true and valid  supplier information to avoid procurement risks.  2. The administrator completes  qualification reviews within 1-2 business days. |
| **Issue:**                             |                                                              |
| **Source:**                            |                                                              |

 

| **Use  case name:**                    | Execute Procurement Process                                  |
| -------------------------------------- | ------------------------------------------------------------ |
| **Use  case ID:**                      | UC-107                                                       |
| **Primary  actor:**                    | Merchant                                                     |
| **Secondary  actor(s):**               | Supplier (for order confirmation,  delivery), Administrator (optional, for procurement oversight) |
| **Brief  description:**                | The merchant creates purchase orders,  tracks order status, confirms goods receipt, and updates inventory through  the flower e-commerce platform to ensure timely and accurate procurement of  flower products, maintaining smooth supply for sales. |
| **Preconditions:**                     | 1. The merchant has logged in to the  merchant management system and holds the "procurement process  execution" permission.  2. The selected supplier is in  "active" status (qualified and available for cooperation).  3. The platform’s procurement module is  connected to the inventory module to support automatic inventory updates. |
| **Flow  of events:**                   | 1. Merchant navigates to the  "Procurement Management" module in the merchant system and clicks  "Create Purchase Order."  2. Merchant fills in order details:  selected supplier, product type (e.g., 100 red roses, 50 lily bouquets),  quantity, unit price, expected delivery date, and delivery address.  3. The system automatically calculates  the total order amount (quantity × unit price) and displays it to the  merchant for confirmation.  4. After confirming the details, the  merchant submits the purchase order.  5. The system sends the order  notification to the supplier via the platform’s internal message system and  email (with an order attachment in PDF format), and marks the order status as  "Pending Confirmation."  6. The supplier logs in to the supplier  portal, views the pending order, and chooses to "Confirm Order" or  "Reject Order."  - If the  supplier confirms: The system updates the order status to "Order  Confirmed" and notifies the merchant via message.  - If the supplier rejects: The system  updates the status to "Order Rejected," records the rejection  reason (filled in by the supplier), and notifies the merchant.  7. After confirming the order, the  supplier arranges delivery and updates the delivery information (e.g.,  logistics company name, tracking number) in the system.  8. When the merchant receives the goods,  they check the actual goods against the purchase order (verifying quantity,  product quality, and freshness).  9. If goods match the order:  a. Merchant clicks "Confirm  Receipt" in the system and fills in the actual receipt time.  b. The system updates the order status to "Completed"  and automatically increases the corresponding product inventory in the  merchant’s inventory system.  c. The  system generates a receipt confirmation document and stores it in the order  record.  10. If goods have discrepancies (e.g.,  missing quantity, damaged goods):  a.  Merchant selects "Record Discrepancy" in the system, describes the  issue (e.g., "Received 95 red roses instead of 100," "5 lilies  are wilted"), and uploads photos of the discrepancies as evidence.  b. The system updates the order status to  "Discrepancy Pending" and notifies the supplier of the issue.  c. Merchant and supplier negotiate a  solution (e.g., supplier sends supplementary goods, offers a price  discount).  d. After reaching an  agreement, the merchant updates the processing result in the system (e.g.,  "Supplementary goods received"), and the system adjusts the  inventory accordingly and updates the order status to "Completed."  11. After the order is completed, the  system archives the entire order record (including all status changes,  messages, and documents) for future reference. |
| **Postconditions:**                    | 1. The procurement order is processed  (completed, rejected, or resolved for discrepancies) and fully recorded in  the system.  2. Product inventory is updated  accurately based on actual receipt (if applicable).  3. All interaction records (order  confirmations, delivery updates, discrepancy communications) are stored in  the order’s history log.  4. The system adds the procurement data  to the merchant’s monthly procurement report (e.g., total procurement volume,  average order value). |
| **Alternative  flows and exceptions:** | - Exception 1: Delayed order confirmation  by supplier If the supplier does not  confirm or reject the order within 24 hours, the system automatically sends a  reminder to the supplier (via message and email) and notifies the merchant of  the delay.- Exception 2: Delayed delivery beyond expected date If the delivery date exceeds the expected  date set in the order, the system marks the order as "Delivery  Delayed" and sends a reminder to the supplier to update the delivery  progress.- Exception 3: Damaged goods beyond acceptable range If more than 10% of the goods are damaged  (exceeding the platform’s acceptable freshness/damage standard), the system  supports the merchant to initiate a "Return Request." After the  supplier confirms the return, the system updates the order status to  "Return Processing" and decreases the inventory (if already added)  until the return is completed.- Exception 4: System error during order  submission If the system fails to  submit the order due to a database error or network issue, the system  displays an error prompt ("Failed to submit order. Please try again  later") and retains the merchant’s filled-in order details to avoid  re-entry. |
| **Non-behavior  requirements:**        | - Response time: The system must update  the order status and send notifications within 10 seconds after the supplier  confirms the order or the merchant records a discrepancy.- Data security: All  procurement-related data (order details, supplier information, logistics  data) must be encrypted to prevent unauthorized access.- Reporting function:  The system should generate monthly procurement reports for the merchant,  including order quantity, total procurement amount, supplier performance  (e.g., on-time delivery rate), and discrepancy rate.- Traceability: Every  operation in the procurement process (e.g., order submission, status update,  discrepancy recording) must be linked to the operator’s account and timestamp  for audit purposes.- Compatibility: The system’s procurement module must  support integration with mainstream logistics companies’ APIs to  automatically pull real-time logistics tracking information. |
| **Assumptions:**                       | 1. The supplier checks the platform’s  order notifications regularly to avoid delayed order confirmation.  2. The merchant inspects the received  goods within 24 hours of delivery to ensure timely recording of  discrepancies.  3. The platform’s inventory module is  functioning normally and can accurately receive inventory adjustment signals  from the procurement module.  4. The logistics company provides valid  tracking information that can be verified through the platform. |
| **Issue:**                             |                                                              |
| **Source:**                            |                                                              |

 

| **Use  case name:**                    | View Inventory Status                                        |
| -------------------------------------- | ------------------------------------------------------------ |
| **Use  case ID:**                      | UC-201                                                       |
| **Primary  actor:**                    | Customer                                                     |
| **Secondary  actor(s):**               | None                                                         |
| **Brief  description:**                | The customer checks the real-time  inventory status of flower products on the e-commerce platform, including  available stock quantity, stock location, and whether the product supports  immediate shipment, to make informed purchase decisions. |
| **Preconditions:**                     | 1. The customer has accessed the detail  page of a specific flower product on the platform;  2. The platform’s inventory system is  connected to the front-end product page and can synchronize real-time stock  data. |
| **Flow  of events:**                   | 1. The customer navigates to the product  detail page of the flower they intend to purchase;   2. The system automatically retrieves  real-time inventory data of the product from the inventory module;   3. The system displays the inventory  status on the product detail page, including:- Available stock quantity;-  Stock location;- Shipment prompt;   4. If the product has multiple variants,  the system displays the inventory status corresponding to each variant when  the customer switches variants;   5. The customer views the inventory  information and proceeds to purchase or exit the page. |
| **Postconditions:**                    | 1. The customer successfully obtains the  real-time inventory status of the target product;   2. The system records the customer’s  inventory view behavior. |
| **Alternative  flows and exceptions:** | - If the product is out of stock, the  system displays "Out of stock" and optionally shows the expected  restock time;   - If the inventory data fails to load due  to system error, the system prompts "Failed to load inventory  information. Please try again later" and provides a "Refresh"  button. |
| **Non-behavior  requirements:**        | - The inventory status displayed on the  front end must be consistent with the back-end inventory system, with a data  synchronization delay of no more than 1 minute; -The inventory information  must be clearly visible to avoid  customer misjudgment. |
| **Assumptions:**                       | 1. The platform’s inventory system is  running normally and can provide accurate real-time stock data; 2. The  product’s inventory is managed by variants   to ensure variant-specific inventory display. |
| **Issue:**                             |                                                              |
| **Source:**                            |                                                              |

 

| **Use  case name:**                    | Configure Inventory Parameters                               |
| -------------------------------------- | ------------------------------------------------------------ |
| **Use  case ID:**                      | UC-202                                                       |
| **Primary  actor:**                    | Administrator                                                |
| **Secondary  actor(s):**               | None                                                         |
| **Brief  description:**                | The administrator configures  global inventory-related parameters on the platform, including inventory  counting cycles, stock data synchronization frequency, inventory variance  tolerance, and default inventory units, to standardize inventory management  processes. |
| **Preconditions:**                     | 1. The administrator has logged in to the  backend management system and has the "Inventory Parameter  Configuration" permission;   2. The platform’s inventory module has  been initialized and supports parameter adjustment. |
| **Flow  of events:**                   | 1. The administrator navigates to the  "Inventory Parameter Management" module in the backend system;   2. The system displays the current  configuration of inventory parameters (e.g., "Inventory counting cycle:  Monthly," "Data synchronization frequency: Every 30 seconds");    3. The administrator modifies parameters  as needed:  - Adjusts the inventory  counting cycle (e.g., changes from "Monthly" to  "Biweekly");  - Sets the  inventory variance tolerance (e.g., "Allowed variance ≤ 3%");  - Updates default inventory units (e.g.,  adds "box" as a new unit for bulk products);   4. The administrator submits the modified  parameter configuration;   5. The system verifies the validity of  the parameters (e.g., checks if the variance tolerance is a positive number)  and saves the configuration;   6. The system prompts "Inventory  parameters configured successfully" and applies the new parameters to  the entire inventory system. |
| **Postconditions:**                    | 1. The inventory parameters are updated  successfully and take effect globally;   2. The system records the administrator’s  parameter modification operation in the backend log (including operator,  modification time, and before/after values). |
| **Alternative  flows and exceptions:** | - If the administrator inputs an invalid  parameter (e.g., negative variance tolerance), the system rejects the  submission and prompts "Please enter a positive number for variance  tolerance"; - If the parameter  save fails due to database error, the system displays "Failed to save  parameters. Please try again later" and retains the original parameter  settings. |
| **Non-behavior  requirements:**        | - The system must back up the previous  parameter configuration before updating, allowing rollback if new parameters  cause issues;   - All parameter modification operations  must be traceable for audit purposes;   - The system should send a notification  to relevant warehouse managers when key parameters (e.g., counting cycle) are  modified. |
| **Assumptions:**                       | 1. The administrator understands the  impact of each inventory parameter on daily operations to avoid unreasonable  configurations;   2. The backend inventory system can adapt  to the new parameter settings without restarting. |
| **Issue:**                             |                                                              |
| **Source:**                            |                                                              |

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

| **Use  case name:**                    | Manage Warehouse Information                                 |
| -------------------------------------- | ------------------------------------------------------------ |
| **Use  case ID:**                      | UC-203                                                       |
| **Primary  actor:**                    | Administrator                                                |
| **Secondary  actor(s):**               | Warehouse Manager (optional, for  providing warehouse details) |
| **Brief  description:**                | The administrator manages basic  information of the platform’s warehouses, including adding new warehouses,  editing existing warehouse details (e.g., address, contact person), setting  warehouse responsibility scopes (e.g., which regions the warehouse serves),  and marking warehouses as "active/inactive," to ensure orderly  warehouse operation and rational inventory allocation. |
| **Preconditions:**                     | 1. The administrator has logged in to the  backend management system and has the "Warehouse Information  Management" permission;   2. The platform’s warehouse management  module has been initialized. |
| **Flow  of events:**                   | 1. The administrator enters the  "Warehouse Management" module, which displays a list of existing  warehouses (including warehouse name, status, and responsibility scope);   2. Adding a new warehouse:  a. The administrator clicks "Add  Warehouse" and fills in details: warehouse name (e.g., "Guangzhou  Warehouse"), address, contact person, phone number, and responsibility  scope (e.g., "Serves South China region");  b. The administrator submits the  information;  c. The system verifies  the uniqueness of the warehouse name (to avoid duplicates) and saves the new  warehouse information;   3. Editing warehouse details:  a. The administrator selects an existing  warehouse and clicks "Edit";    b. The administrator updates details (e.g., changes the contact person  of "Beijing Warehouse");    c. The system saves the updated information and synchronizes it to the  inventory and order modules;   4. Setting warehouse status:  a. If a warehouse is temporarily out of  service, the administrator marks it as "Inactive";  b. The system automatically redirects  inventory allocation and order shipment tasks from the inactive warehouse to  other active warehouses;   5. The system prompts "Warehouse  information managed successfully" after each operation. |
| **Postconditions:**                    | 1. New warehouses are added, or existing  warehouse details/status are updated successfully;   2. The system synchronizes the latest  warehouse information to the inventory, order, and shipment modules;   3.  The operation log records the administrator’s warehouse management actions. |
| **Alternative  flows and exceptions:** | - If the administrator tries to add a  warehouse with a duplicate name, the system prompts "Warehouse name  already exists. Please use a different name";   - If the administrator marks the only  active warehouse as "Inactive," the system warns "This is the  only active warehouse. Marking it as inactive will affect order shipment.  Confirm?" and proceeds only after confirmation. |
| **Non-behavior  requirements:**        | - The system must map each warehouse to  the inventory module to ensure that inventory data is associated with the  correct warehouse; - The system should  provide a visual warehouse list (with filters by region/status) for efficient  management;    -  When a warehouse’s responsibility scope is modified, the system must update  the order allocation logic in real time. |
| **Assumptions:**                       | 1. The administrator has accurate  warehouse details (e.g., address, contact information) when adding/editing;   2. The platform has multiple warehouses  (or plans to add them) to support regionalized inventory management. |
| **Issue:**                             |                                                              |
| **Source:**                            |                                                              |

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

| **Use  case name:**                    | Manage Inbound/Outbound                                      |
| -------------------------------------- | ------------------------------------------------------------ |
| **Use  case ID:**                      | UC-204                                                       |
| **Primary  actor:**                    | Merchant                                                     |
| **Secondary  actor(s):**               | Warehouse Staff (for confirming goods  receipt/shipment), Supplier (for inbound goods supply) |
| **Brief  description:**                | The merchant manages the inbound and  outbound processes of flower products, including creating inbound orders (for  receiving goods from suppliers), generating outbound orders (for fulfilling  customer purchases), and confirming the completion of inbound/outbound tasks,  to ensure accurate and timely inventory updates. |
| **Preconditions:**                     | 1. The merchant has logged in to the  merchant management system and has the "Inbound/Outbound  Management" permission;   2. For inbound: The selected supplier is  active and has confirmed the supply; For outbound: There are pending customer  orders requiring shipment. |
| **Flow  of events:**                   | 1. Managing inbound:  a. The merchant navigates to the  "Inbound Management" module and clicks "Create Inbound  Order";  b. The merchant fills  in inbound details: supplier name, product type (e.g., "100 white  lilies"), quantity, expected inbound date, and target warehouse;  c. The system generates an inbound order  number and sends a notification to the supplier and target warehouse;  d. When goods arrive at the warehouse,  warehouse staff confirm receipt (check quantity/quality) and update the  inbound status to "Completed" in the system;  e. The system automatically increases the  corresponding product inventory in the target warehouse;   2. Managing outbound:  a. The merchant enters the "Outbound  Management" module, and the system displays pending outbound tasks  (generated from customer orders);  b.  The merchant reviews the outbound details (customer address, product  type/quantity) and clicks "Generate Outbound Order";  c. The system assigns the outbound task  to the corresponding warehouse (based on customer region and warehouse  inventory);  d. Warehouse staff  fulfill the shipment, update the outbound status to "Shipped," and  enter the logistics tracking number;    e. The system automatically decreases the corresponding product  inventory and sends a shipment notification to the customer;   3. The system prompts  "Inbound/Outbound task managed successfully" after each process is  completed. |
| **Postconditions:**                    | 1. Inbound/outbound orders are processed  successfully, and inventory is updated accurately;   2. The system records the entire  inbound/outbound process (including order details, status changes, and  operator) for traceability; 3. Customers receive shipment notifications (for  outbound) or suppliers receive inbound confirmations (for inbound). |
| **Alternative  flows and exceptions:** | - If inbound goods have quality issues  (e.g., wilted flowers), the merchant marks the "defective quantity"  in the system, and the system only increases inventory by the qualified  quantity;    -  If there is insufficient inventory for an outbound order, the system prompts  "Insufficient inventory for this product. Please adjust the order or  restock first";   - If the logistics tracking number is  invalid, the system rejects it and prompts "Please enter a valid  logistics tracking number." |
| **Non-behavior  requirements:**        | - The system must link inbound/outbound  orders to inventory in real time to avoid inventory discrepancies;    -  The system should support batch creation of inbound/outbound orders for  efficiency (e.g., multiple products in one order);   - Inbound/outbound order records must be  retained for at least 1 year for audit. |
| **Assumptions:**                       | 1. Warehouse staff confirm  inbound/outbound status in a timely manner (within 24 hours of goods  arrival/shipment);   2. The supplier delivers goods as per the  inbound order agreement. |
| **Issue:**                             |                                                              |
| **Source:**                            |                                                              |

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

| **Use  case name:**                    | Perform Stocktaking                                          |
| -------------------------------------- | ------------------------------------------------------------ |
| **Use  case ID:**                      | UC-205                                                       |
| **Primary  actor:**                    | Merchant                                                     |
| **Secondary  actor(s):**               | Warehouse Staff (for conducting physical  stocktaking)       |
| **Brief  description:**                | The merchant initiates and manages the  stocktaking process of flower products, including creating stocktaking tasks,  recording physical stock quantities (assisted by warehouse staff), comparing  physical stock with system stock, and reconciling inventory discrepancies, to  ensure inventory accuracy. |
| **Preconditions:**                     | 1. The merchant has logged in to the  merchant management system and has the "Stocktaking" permission;   2. The platform’s inventory module has  recorded the current system stock quantity of products. |
| **Flow  of events:**                   | 1. The merchant navigates to the  "Stocktaking Management" module and clicks "Create Stocktaking  Task";   2. The merchant sets stocktaking  details:  - Scope (e.g., "All  products in Shanghai Warehouse," "Only potted plants");  - Stocktaking time (scheduled or immediate);  - Assigned warehouse staff (for physical  counting);   3. The system generates a stocktaking  task list (including product name, system stock quantity, and a blank field  for physical quantity) and notifies the assigned warehouse staff;   4. Warehouse staff conduct physical  stocktaking, enter the actual quantity of each product in the system, and  submit the results;   5. The system automatically compares the  physical quantity with the system quantity and generates a "Stocktaking  Discrepancy Report," highlighting products with differences (e.g.,  "System: 50 roses, Physical: 48 roses");   6. The merchant reviews the discrepancy  report, verifies the reason for discrepancies (e.g., damage, loss), and  clicks "Reconcile Inventory";   7. The system updates the system stock  quantity to match the physical quantity and records the reconciliation  details;   8. The system prompts "Stocktaking  completed successfully." |
| **Postconditions:**                    | 1. The stocktaking task is completed, and  the system stock quantity is reconciled with the physical quantity;   2. The system saves the stocktaking  report and discrepancy reconciliation records for future reference;   3. Inventory accuracy is ensured, and  subsequent inbound/outbound operations use the reconciled stock data. |
| **Alternative  flows and exceptions:** | - If the physical quantity cannot be  entered on time (e.g., warehouse staff are delayed), the merchant can extend  the stocktaking task deadline in the system;    -  If the discrepancy between physical and system stock exceeds the allowed  tolerance (set by administrators), the system requires the merchant to fill  in a "Discrepancy Explanation" before allowing reconciliation;   - If the stocktaking task is canceled  (e.g., due to emergency warehouse operations), the system discards the  unsubmitted physical quantity data and marks the task as  "Canceled." |
| **Non-behavior  requirements:**        | - The system must lock the inventory of  products in the stocktaking scope during the task (preventing  inbound/outbound operations) to avoid interference with stocktaking  results; - The stocktaking report must  include detailed information (product SKU, system quantity, physical  quantity, discrepancy, reconciliation time) for traceability; - The system should support exporting the  stocktaking report in Excel/CSV format for offline analysis. |
| **Assumptions:**                       | 1. Warehouse staff conduct physical  stocktaking accurately to avoid manual counting errors;   2. The merchant verifies the discrepancy  reasons truthfully to ensure the reconciled inventory data is reliable. |
| **Issue:**                             |                                                              |
| **Source:**                            |                                                              |

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

| **Use  case name:**                    | Set Inventory Alerts                                         |
| -------------------------------------- | ------------------------------------------------------------ |
| **Use  case ID:**                      | UC-206                                                       |
| **Primary  actor:**                    | Merchant                                                     |
| **Secondary  actor(s):**               | None                                                         |
| **Brief  description:**                | The merchant sets inventory alert rules  for flower products, including low-stock alerts (e.g., notify when stock is  below 10 pieces) and overstock alerts (e.g., notify when stock exceeds 100  pieces), to receive timely notifications and take proactive actions (e.g.,  restock, promote sales) to optimize inventory levels. |
| **Preconditions:**                     | 1. The merchant has logged in to the  merchant management system and has the "Inventory Alert Setting"  permission;   2. The merchant has added target flower  products to the platform’s inventory system (with existing stock records);   3. The platform’s notification module  (message, email, SMS) is configured and functional. |
| **Flow  of events:**                   | 1. The merchant navigates to the  "Inventory Alert Management" module and views the list of products  without alert rules (or existing alert rules);   2. The merchant selects a specific flower  product (e.g., "Red Rose Bouquet") to configure alerts for;   3. The merchant sets alert parameters for  the product:  - Low-stock alert:  Defines the threshold (e.g., "Alert when stock ≤ 15 pieces") and  selects notification methods (multiple options allowed: platform in-message,  merchant email, SMS to merchant’s phone);    - Overstock alert (optional): Defines the threshold (e.g., "Alert  when stock ≥ 80 pieces") and selects notification methods;  - Alert frequency: Chooses between  "Notify once when threshold is reached" or "Notify daily until  stock is adjusted to normal range";   4. The merchant reviews the configured  rules (e.g., "Low-stock alert: ≤15 pieces, Notify via email + SMS")  and clicks "Save Alert Rules"; 5. The system verifies the validity  of the rules:  - Checks if low-stock  threshold is a positive integer (e.g., rejects "0" or negative  values);  - Ensures low-stock  threshold is less than overstock threshold (if both are set, e.g., prevents  "low-stock ≤20" and "overstock ≥15");   6.  If the rules are valid, the system saves them and associates them with the  product;   7. The system prompts "Inventory  alert rules saved successfully" and updates the product’s alert status  to "Active";   8.  The system monitors the product’s real-time stock level: When stock reaches  or crosses the set threshold, it triggers the corresponding alert and sends  notifications via the selected methods. |
| **Postconditions:**                    | 1. Inventory alert rules for the target  product are created/updated and activated;   2. The system records the alert  configuration operation (merchant, product ID, rule details, save time) in  the inventory log;   3. The system automatically monitors  stock levels and sends alerts when thresholds are triggered;   4. The merchant can view all active alert  rules in the "Inventory Alert Management" module. |
| **Alternative  flows and exceptions:** | - Invalid threshold setting: If the  merchant sets a low-stock threshold ≥ overstock threshold (e.g., "low  ≤30, over ≥25"), the system rejects the save and prompts "Low-stock  threshold must be less than overstock threshold. Please adjust";   - Unsupported notification method: If the  merchant selects an unconfigured notification method, the system prompts  "SMS notification is currently unavailable. Please select other methods  or contact administrator to enable it";     - Duplicate rule creation: If the  merchant tries to set identical rules for a product with existing active  alerts, the system prompts "This product already has the same alert  rules. Do you want to overwrite them?" and updates only if the merchant  confirms. |
| **Non-behavior  requirements:**        | - The system must trigger alerts within 1  minute of stock levels crossing the threshold (to ensure timeliness);    -  Alert notifications must include key information: product name, current stock  quantity, alert type (low/overstock), and threshold (e.g., "Alert: Red  Rose Bouquet (Stock:15) has reached low-stock threshold (≤15). Please  restock"); - The system should  support batch alert configuration (e.g., apply the same "low-stock  ≤20" rule to all potted plant products) to reduce repetitive operations;   - The system must allow the merchant to  pause/delete alert rules (e.g., "Pause Alert" for seasonal products  with temporary stock fluctuations). |
| **Assumptions:**                       | 1. The merchant sets thresholds based on  historical sales data (e.g., low-stock threshold = 3 days of average sales)  to avoid unnecessary alerts;   2. The merchant’s contact information  (email, phone for SMS) in the system is accurate (to ensure notifications are  received);   3. The platform’s stock monitoring runs  in real time. |
| **Issue:**                             |                                                              |
| **Source:**                            |                                                              |

 

| **Use  case name:**                    | Manage Product Batches                                       |
| -------------------------------------- | ------------------------------------------------------------ |
| **Use  case ID:**                      | 207                                                          |
| **Primary  actor:**                    | Merchant                                                     |
| **Secondary  actor(s):**               | Supplier (provides batch-related  information), Warehouse Staff (records batch storage location), Administrator  (optional, for batch quality audit) |
| **Brief  description:**                | The merchant manages batch information of  flower products (which have short shelf lives and require traceability),  including creating batch records (linking to inbound orders), tracking  batch-specific details (production date, expiration date, supplier), updating  batch status (in-stock, expired, sold out), and conducting batch-specific  stock checks, to ensure product freshness and traceability of quality issues. |
| **Preconditions:**                     | 1. The merchant has logged in to the  merchant management system and has the "Product Batch Management"  permission;   2. The merchant has completed an inbound  order for the flower product (with goods received and confirmed by warehouse  staff);   3. The supplier has provided  batch-related documents (e.g., production date, quality certificate) for the  inbound goods;   4. The platform’s batch management module  supports linking batches to products and inbound orders. |
| **Flow  of events:**                   | 1. Create Product Batch:  a. The merchant navigates to the  "Product Batch Management" module and clicks "Create  Batch" ;  b. The merchant  associates the batch with a specific inbound order (e.g., "Inbound Order  No. PO-20241001") and selects the corresponding product (e.g.,  "White Lily Bunch");  c.  The merchant fills in batch details as required:    - Batch number (auto-generated by  system, e.g., "B20241001-LILY-001" or manually entered with unique  verification);    - Production date  (from supplier’s certificate, e.g., "2024-10-01");    - Expiration/freshness period (e.g.,  "Valid until 2024-10-08");     - Supplier batch reference (optional, e.g., supplier’s internal batch  code "SUP-LILY-589");    -  Storage location (provided by warehouse staff, e.g., "Warehouse A, Shelf  3, Zone B");    - Quantity  (auto-filled from inbound order, e.g., "50 bunches," editable if  there are quality separations);   2. The merchant uploads supporting  documents (optional, e.g., supplier’s quality certificate PDF) and clicks  "Submit Batch Record"; 3. The system verifies the batch  information:  - Ensures batch number  is unique (no duplicates in the system);    - Checks if expiration date is after production date (rejects  "expiration before production");    - Confirms quantity does not exceed the inbound order quantity;   4. If valid, the system saves the batch  record and links it to the product and inbound order;   5.  Update Batch Status:  a. When the  batch is sold out (stock = 0), the system automatically updates its status to  "Sold Out";  b. When the  batch exceeds the expiration date, the system automatically updates its  status to "Expired" and prompts the merchant to "Remove  expired batch from inventory";    c. If the batch has quality issues (e.g., wilted lilies), the merchant  manually updates its status to "Quarantined" and records the reason  (e.g., "Quality defect: 10 bunches wilted");   6. Batch Traceability: When a customer  reports a quality issue (e.g., "Received expired lily"), the  merchant searches for the product’s batch record by order number (or batch  number) to trace production date, supplier, and storage history for issue  resolution. |
| **Postconditions:**                    | 1. Product batch records are  created/updated and stored in the system (linked to products, inbound  orders);   2. Batch status is accurately maintained  (In-Stock, Sold Out, Expired, Quarantined) based on stock and time/quality  changes;   3. The system retains batch records for  at least 1 year after expiration (for quality traceability and compliance);   4. The merchant can query batch details  (e.g., "All batches of White Lily Bunch in 2024") and export batch  reports. |
| **Alternative  flows and exceptions:** | - Duplicate batch number: If the merchant  manually enters a batch number that already exists, the system prompts  "Batch number B20241001-LILY-001 already exists. Please use a different  number or enable auto-generation";     - Expired batch inbound: If the merchant  tries to create a batch with an expiration date that is already past (e.g.,  production date 2024-09-01, expiration 2024-09-07, current date 2024-10-01),  the system warns "This batch has expired. Confirm creating the  record?" and requires manual confirmation before saving;    -  Batch quantity adjustment: If warehouse staff find a discrepancy between  inbound quantity and actual batch quantity (e.g., inbound order 50 bunches,  actual 48 due to damage), the merchant edits the batch quantity to 48 and  records "Damaged 2 bunches" in the batch notes; the system updates  the product’s total inventory accordingly. |
| **Non-behavior  requirements:**        | - The system must support batch-level  inventory tracking (i.e., can view stock quantity of each individual batch,  not just total product stock);   - Batch records must include traceable  fields: creator (merchant), creation time, inbound order number, supplier ID,  and all status change logs (e.g., "Status changed to Expired on  2024-10-08 by System");   - The system should send a reminder to  the merchant 2 days before a batch expires (e.g., "Reminder: Batch  B20241001-LILY-001 will expire on 2024-10-08. Please promote sales or prepare  for disposal"); - The system must  allow filtering batches by status (e.g., "Show all Expired  batches") and export batch data in CSV/Excel format for compliance  audits. |
| **Assumptions:**                       | 1. The merchant accurately records batch  details (production date, expiration) based on supplier-provided documents  (to avoid false traceability);   2. Warehouse staff correctly report batch  storage locations and any quality issues (to ensure batch status accuracy);  3. Flower products are managed by batches (critical for perishable goods) to  meet quality and safety requirements. |
| **Issue:**                             |                                                              |
| **Source:**                            |                                                              |

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 



| **Use case name:**                    | Place Order                                                  |
| ------------------------------------- | ------------------------------------------------------------ |
| **Use case ID:**                      | UC-301                                                       |
| **Primary actor:**                    | Customer                                                     |
| **Secondary actor(s):**               | Merchant, System                                             |
| **Brief description:**                | The customer selects  products, confirms order information, completes payment, and creates a formal  purchase order in the system. |
| **Preconditions:**                    | 1.The customer has successfully logged into the system.  2.The customer's shopping cart contains at least one  product.  3.Sufficient product inventory is available.  4.The customer's shipping address information is complete  and valid. |
| **Flow of events:**                   | 1. The customer goes  to the shopping cart page and clicks the "Place Order Now" button..  2. The system displays the order  confirmation page, including the product list, price details, shipping  address, etc.  3. The customer  confirms the order information is correct and clicks "Submit  Order".  4. The system  verifies inventory availability and reserves the stock..  5. The system  redirects to the payment page.  6. The customer completes  the payment process.  7. The system updates  the order status to "Pending Shipment".  8. The system sends  an order confirmation notification to the customer. |
| **Postconditions:**                   | 1. The order is  successfully created in the system.  2. The corresponding  product inventory is reserved.  3. The customer receives an order confirmation  notification. |
| **Alternative flows and exceptions:** | - If inventory is  insufficient, the system alerts the customer and cancels the order placement.  - If payment fails,  the order status remains "Pending Payment". |
| **Non-behavior requirements:**        | - The system should complete order creation within 3  seconds.  - Order data must be backed up in real time. |
| **Assumptions:**                      | The delivery address provided by the customer is within  the delivery range. |
| **Issue:**                            |                                                              |
| **Source:**                           |                                                              |

 

 

 

 

 

 

 

| **Use case name:**                    | Check Order Status                                           |
| ------------------------------------- | ------------------------------------------------------------ |
| **Use case ID:**                      | UC-302                                                       |
| **Primary actor:**                    | Customer                                                     |
| **Secondary actor(s):**               | System                                                       |
| **Brief description:**                | The customer checks  the current processing status and delivery progress of placed orders. |
| **Preconditions:**                    | 1. The customer has successfully logged into the system.  2. The customer has historical order records. |
| **Flow of events:**                   | 1. The customer  navigates to the "My Orders" page.   2. The system displays the order list,  including order number, order time, and order status.   3. The customer  clicks on a specific order to view details.  4. The system  displays complete order status information, including delivery progress and  estimated delivery time.  5. The customer can  view logistics tracking information. |
| **Postconditions:**                   | 1. The customer  obtains the current status information of the order.  2. The system records  the query log. |
| **Alternative flows and exceptions:** |                                                              |
| **Non-behavior requirements:**        | - Order status query  response time should not exceed 2 seconds..  - The system should  ensure real-time order status information. |
| **Assumptions:**                      | Order status  information is completely recorded in the system. |
| **Issue:**                            |                                                              |
| **Source:**                           |                                                              |

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

| **Use case name:**                    | Apply for After-sales Service                                |
| ------------------------------------- | ------------------------------------------------------------ |
| **Use case ID:**                      | UC-303                                                       |
| **Primary actor:**                    | Customer                                                     |
| **Secondary**  **actor(s):**          | Merchant, System                                             |
| **Brief description:**                | The customer applies  for after-sales services such as returns, exchanges, or refunds for received  products. |
| **Preconditions:**                    | 1. The customer has successfully logged into the system.   2. The order status is "Completed" or  "Received".  3. The product is within the after-sales service  period.  4. The product meets after-sales policy requirements. |
| **Flow of events:**                   | 1. The customer goes to the order details  page.   2. Clicks the "Apply for  After-sales" button.   3. Selects the type  of after-sales service (return, exchange, refund).  4. Fills in the  reason for after-sales and problem description. 5. Uploads relevant evidence  photos.  6. Submits the after-sales service application.  7.The system generates an  after-sales service ticket and notifies the merchant. |
| **Postconditions:**                   | 1. The after-sales  service ticket is successfully created.  2. The merchant  receives the after-sales application notification.  3. The order enters the after-sales processing  workflow. |
| **Alternative flows and exceptions:** |                                                              |
| **Non-behavior requirements:**        | - After-sales  application submission response time should not exceed 3 seconds.  - Support image  upload. |
| **Assumptions:**                      | The customer can  provide a clear description of product issues. |
| **Issue:**                            |                                                              |
| **Source:**                           |                                                              |

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 



| **Use case name:**                     | Process  Order Review                                        |
| -------------------------------------- | ------------------------------------------------------------ |
| **Use case ID:**                       | UC-304                                                       |
| **Primary actor:**                     | Merchant                                                     |
| **Secondary**  **actor(s):**           | System                                                       |
| **Brief description:**                 | The merchant reviews customer-submitted orders to confirm  the completeness and validity of order information. |
| **Preconditions:**                     | 1. The  merchant has logged into the management system.  2. The merchant has order review  permissions.   3. There are pending review orders in  the system. |
| **Flow of events:**                    | 1. The  merchant enters the order management page.   2. Views  the list of orders pending review.   3. Clicks  on a specific order to view detailed information.   4. Reviews order content (products, address, price,  etc.).  5. Confirms inventory availability.   6. Approves or rejects the order.   7.The system updates the order status and notifies the customer. |
| **Postconditions:**                    | 1. The  order review status is updated.  2. The  customer receives the review result notification.   3. Review records are completely saved. |
| **Alternative flows and  exceptions:** |                                                              |
| **Non-behavior  requirements:**        | - The order review interface should support batch  operations.  - Review operations require secondary confirmation. |
| **Assumptions:**                       | System inventory information is accurate and reliable.  Customer order information is authentic and valid. |
| **Issue:**                             |                                                              |
| **Source:**                            |                                                              |



 

 

 

 

 

 

 

 

 

 

 



| **Use case name:**                     | Handle  After-sales Requests                                 |
| -------------------------------------- | ------------------------------------------------------------ |
| **Use case ID:**                       | UC-305                                                       |
| **Primary actor:**                     | Merchant                                                     |
| **Secondary**  **actor(s):**           | Customer, System                                             |
| **Brief description:**                 | The merchant processes after-sales service applications  initiated by customers, including review, confirmation, and execution. |
| **Preconditions:**                     | 1.The merchant has logged into the management system.  2.The merchant has after-sales processing permissions.  3.There are pending after-sales service tickets in the system. |
| **Flow of events:**                    | 1.The merchant  enters the after-sales service management page.  2.Views the list of  pending after-sales applications.  3.Reviews the  after-sales application content and evidence.  4.Communicates with  the customer to confirm specific details.  5.Makes a handling  decision (approve/reject).  6.Arranges for  return receipt or refund operation.  7.Updates the  after-sales service ticket status.  8.The system  notifies the customer of the handling result. |
| **Postconditions:**                    | 1.The after-sales  service ticket status is updated.  2.The customer  receives the handling result notification.  3.If a refund is  required, the financial process is initiated.  4.After-sales  processing records are completely saved. |
| **Alternative flows and  exceptions:** |                                                              |
| **Non-behavior  requirements:**        | - Support real-time tracking of after-sales progress.  - After-sales processing time limits should be  configurable. |
| **Assumptions:**                       | Merchant after-sales personnel have professional  processing capabilities.  Customers cooperate in providing necessary after-sales  information. |
| **Issue:**                             |                                                              |
| **Source:**                            |                                                              |

 



 

 

 

 

 

 

 

 

 

 

 

| **Use case name:**                    | Configure Order Rules                                        |
| ------------------------------------- | ------------------------------------------------------------ |
| **Use case ID:**                      | UC-306                                                       |
| **Primary actor:**                    | Administrator                                                |
| **Secondary**  **actor(s):**          |                                                              |
| **Brief description:**                | The administrator  sets business rules and parameter configurations related to orders. |
| **Preconditions:**                    | 1.The administrator has  logged into the system management backend.  2.The administrator has  system configuration permissions.  3.The system is in a  configurable state. |
| **Flow of events:**                   | 1.The administrator enters  the system configuration page.  2.Selects the order rules  configuration module.  3.Sets order-related  parameters:  4.Saves the configuration  information.  5.The system verifies  configuration validity and applies the new rules. |
| **Postconditions:**                   | 1.The order rules  configuration is updated and takes effect.  2.The system processes  orders according to the new rules.  3.Configuration change  records are saved. |
| **Alternative flows and exceptions:** |                                                              |
| **Non-behavior requirements:**        | - The configuration  interface should be intuitive and easy to use.  - The system should  provide configuration verification functions. |
| **Assumptions:**                      | The administrator  understands the meaning of business rules.  Configuration changes  undergo sufficient testing.  The system can  correctly handle configuration updates. |
| **Issue:**                            |                                                              |
| **Source:**                           |                                                              |

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

| **Use case name:**                    | Manage Payment Parameters                                    |
| ------------------------------------- | ------------------------------------------------------------ |
| **Use case ID:**                      | UC-307                                                       |
| **Primary actor:**                    | Administrator                                                |
| **Secondary**  **actor(s):**          |                                                              |
| **Brief description:**                | The administrator  configures payment-related parameters and payment method settings. |
| **Preconditions:**                    | 1.The administrator has  logged into the system management backend.  2.The administrator has  payment management permissions.  3.Payment service provider  interfaces are available. |
| **Flow of events:**                   | 1.The administrator enters  the payment management page.  2.Configures  payment-related parameters:  3.Tests payment channel  connectivity.  4.Saves the payment  configuration.  5.The system applies the  new payment parameters. |
| **Postconditions:**                   | 1.The payment parameter  configuration is updated.  2.The payment system  operates according to the new parameters.  3.Payment configuration  records are saved. |
| **Alternative flows and exceptions:** |                                                              |
| **Non-behavior requirements:**        | - Payment parameter  configuration should take effect in real-time.   - The system should  provide payment security detection. |
| **Assumptions:**                      | The system network  environment is secure and reliable.      |
| **Issue:**                            |                                                              |
| **Source:**                           |                                                              |

 

 

 

 

 

 

 

 

 

 

 

 

| **Use case name:**                    | View Member Information                                      |
| ------------------------------------- | ------------------------------------------------------------ |
| **Use case ID:**                      | UC-401                                                       |
| **Primary actor:**                    | Customer                                                     |
| **Secondary actor(s):**               | System                                                       |
| **Brief description:**                | The customer views  membership-related information such as their membership level, available  points balance, growth value, etc. |
| **Preconditions:**                    | 1.The customer has successfully logged into the system.  2.The customer is registered as a member.  3.The membership system is operating normally. |
| **Flow of events:**                   | 1.The customer navigates  to the "My Membership" page.  2.The system displays  basic membership information, including:  l Current membership level  l Available points balance  l Growth value progress  l Membership validity period  3.The customer can view  details of membership benefits.  4.The customer can view  the points transaction history.  5.The system updates the  displayed member information in real-time. |
| **Postconditions:**                   | 1.The customer  successfully retrieves the membership information.  2.The system logs the  membership information query. |
| **Alternative flows and exceptions:** |                                                              |
| **Non-behavior requirements:**        | - Response time for  membership information queries should not exceed 2 seconds.  - Clear presentation  of membership benefits should be supported. |
| **Assumptions:**                      | The system can  correctly calculate membership levels.       |
| **Issue:**                            |                                                              |
| **Source:**                           |                                                              |

 

 

 

 

 

 

 

 

 

 

 

 

| **Use case name:**                    | Use Discount Benefit                                         |
| ------------------------------------- | ------------------------------------------------------------ |
| **Use case ID:**                      | UC-402                                                       |
| **Primary actor:**                    | Customer                                                     |
| **Secondary actor(s):**               | System                                                       |
| **Brief description:**                | The customer uses  member-exclusive discount offers and privilege benefits during shopping. |
| **Preconditions:**                    | 1.The customer has successfully  logged into the system.  2.The customer has a valid membership  status.  3.The customer has available  membership benefits.  4.The product(s) support member  discounts. |
| **Flow of events:**                   | 1.The customer selects the option to use the member discount on the  checkout page.  2.The system verifies the membership eligibility and discount  benefits.  3.The system automatically calculates the member discount amount.  4.The customer confirms the use of the discount.  5.The system applies the discount and updates the order total.  6.The discount usage is recorded. |
| **Postconditions:**                   | 1. The member  discount is successfully applied to the order.  2. A record of the  discount usage is saved. |
| **Alternative flows and exceptions:** |                                                              |
| **Non-behavior requirements:**        | - Discount  calculation should complete within 1 second.  - Discount rules  should be clear and transparent. |
| **Assumptions:**                      | The discount  calculation logic is correct.  The customer  understands the discount usage rules. |
| **Issue:**                            |                                                              |
| **Source:**                           |                                                              |

 

 

 

 

 

 

 

 

 

 

 

 

 

 

| **Use case name:**                    | Participate in Marketing Activity                            |
| ------------------------------------- | ------------------------------------------------------------ |
| **Use case ID:**                      | UC-403                                                       |
| **Primary actor:**                    | Customer                                                     |
| **Secondary actor(s):**               | System                                                       |
| **Brief description:**                | The customer  participates in various marketing activities launched by the system |
| **Preconditions:**                    | 1.The customer has successfully  logged into the system.  2.There are ongoing marketing  activities.  3.The customer meets the activity  participation conditions.  4.The activity is within its validity  period. |
| **Flow of events:**                   | 1.The customer navigates to the marketing activities page.  2.The customer browses the list of available activities.  3.The customer selects an activity of interest to participate in.  4.The customer completes the required action for the activity.  5.The system verifies the participation eligibility.  6.The system distributes the activity rewards (points, coupons, etc.).  7.The activity participation record is updated. |
| **Postconditions:**                   | 1.The customer successfully participates in the marketing activity.  2.The activity rewards have been distributed.  3.The activity participation record is saved. |
| **Alternative flows and exceptions:** |                                                              |
| **Non-behavior requirements:**        | - Activity rules  should be clear and easy to understand.  - Reward distribution  should be timely and accurate |
| **Assumptions:**                      | Marketing activities  are reasonably designed.  Customers participate  in activities voluntarily. |
| **Issue:**                            |                                                              |
| **Source:**                           |                                                              |

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

| **Use case name:**                    | Manage Member Profile                                        |
| ------------------------------------- | ------------------------------------------------------------ |
| **Use case ID:**                      | UC-404                                                       |
| **Primary actor:**                    | Customer                                                     |
| **Secondary actor(s):**               | System                                                       |
| **Brief description:**                | The merchant  maintains and manages member basic information, level adjustments, points  operations, etc. |
| **Preconditions:**                    | 1.The merchant has logged into the  management system.  2.The merchant has member management  permissions.  3.The membership system is operating  normally. |
| **Flow of events:**                   | 1.The merchant navigates to the member management page.  2.The merchant queries or searches for a specific member.  3.The merchant views the member's detailed information.  4.The merchant performs management operations:  l Adjust membership  level  l Manually add or  subtract points  l Update member  status  l Modify member  information  5.The system verifies the operation permissions.  6.The system saves the change record. |
| **Postconditions:**                   | 1. The member profile update is completed.  2. The operation record is saved. |
| **Alternative flows and exceptions:** |                                                              |
| **Non-behavior requirements:**        | - Operations require  secondary confirmation.  - Change records must  be complete. |
| **Assumptions:**                      | Reasons for member  changes are sufficient and reasonable.  System permission  controls are effective. |
| **Issue:**                            |                                                              |
| **Source:**                           |                                                              |

 

 

 

 

 

 

 

 

 

 

 

 

| **Use case name:**                    | Execute Marketing Promotion                                  |
| ------------------------------------- | ------------------------------------------------------------ |
| **Use case ID:**                      | UC-405                                                       |
| **Primary actor:**                    | Merchant                                                     |
| **Secondary actor(s):**               | System, Customer                                             |
| **Brief description:**                | The merchant plans  and executes marketing promotion activities targeted at members. |
| **Preconditions:**                    | 1.The merchant has logged into the  management system.  2.The merchant has marketing  management permissions.  3.There are clear marketing  objectives and plans. |
| **Flow of events:**                   | 1.The merchant enters the marketing promotion module.  2.The merchant selects the target member group.  3.The merchant sets the promotion content and offer plan.  4.The merchant formulates the promotion schedule.  5.The merchant executes the promotion activity.  6.The merchant monitors the promotion effectiveness.  7.The merchant adjusts the promotion strategy. |
| **Postconditions:**                   | 1. The marketing promotion activity has been  executed.  2. Promotion effectiveness data is recorded. |
| **Alternative flows and exceptions:** |                                                              |
| **Non-behavior requirements:**        | Effectiveness data  should be statistically available in real-time. |
| **Assumptions:**                      | Marketing content  complies with relevant regulations.       |
| **Issue:**                            |                                                              |
| **Source:**                           |                                                              |

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

| **Use case name:**                    | Manage Promotion Activity                                    |
| ------------------------------------- | ------------------------------------------------------------ |
| **Use case ID:**                      | UC-406                                                       |
| **Primary actor:**                    | Merchant                                                     |
| **Secondary actor(s):**               | System                                                       |
| **Brief description:**                | The merchant creates,  configures, and manages various promotion activities, including coupons,  spend-and-save activities, etc. |
| **Preconditions:**                    | 1. The merchant has logged into the management system.   2. The promotion system is functioning normally. |
| **Flow of events:**                   | 1.The merchant navigates to the promotion management page.  2.The merchant creates a new promotion activity.  3.The merchant configures the activity parameters:  l Activity name and  description  l Activity duration  and scope  l Participation  conditions and rules  l Reward settings  4.The merchant tests the activity rules.  5.The merchant publishes the promotion activity.  6.The merchant monitors the activity's operational status.  7.The merchant adjusts or ends the activity. |
| **Postconditions:**                   | 1. The promotion activity is successfully created or  updated.  2. The activity record is saved. |
| **Alternative flows and exceptions:** |                                                              |
| **Non-behavior requirements:**        | The activity creation  interface should be intuitive and easy to use. |
| **Assumptions:**                      | The system can  support the activity rules.                  |
| **Issue:**                            |                                                              |
| **Source:**                           |                                                              |

 

 

 

 

 

 

 

 

 

 

 

 

 

 

| **Use case name:**                    | Configure Membership Rules                                   |
| ------------------------------------- | ------------------------------------------------------------ |
| **Use case ID:**                      | UC-407                                                       |
| **Primary actor:**                    | Administrator                                                |
| **Secondary actor(s):**               |                                                              |
| **Brief description:**                | The administrator  configures the basic rules of the membership system, including level rules,  points rules, etc. |
| **Preconditions:**                    | 1. The administrator has logged into the system administration backend.   2. The system is in a configurable state. |
| **Flow of events:**                   | 1.The administrator navigates to the membership rules configuration  page.  2.The administrator configures membership level rules:  l Level names and  conditions  l Level benefit  settings  l Upgrade and  downgrade rules  3.The administrator configures points rules:  l Points earning  rules  l Points validity  period  l Points usage  restrictions  4.The administrator saves the configuration information.  5.The system verifies the validity of the rules. |
| **Postconditions:**                   | The configuration change record is saved.                    |
| **Alternative flows and exceptions:** |                                                              |
| **Non-behavior requirements:**        | The rules  configuration interface should be clear and understandable.  Support rule preview  and testing. |
| **Assumptions:**                      | The system can handle  rule changes smoothly.                |
| **Issue:**                            |                                                              |
| **Source:**                           |                                                              |

 

 

 

 

 

 

 

 

 

 

 

 

 

| **Use case name:**                    | Set Points Parameters                                        |
| ------------------------------------- | ------------------------------------------------------------ |
| **Use case ID:**                      | UC-408                                                       |
| **Primary actor:**                    | Administrator                                                |
| **Secondary actor(s):**               |                                                              |
| **Brief description:**                | The administrator  sets various parameters of the points system, including exchange rates,  limits, validity periods, etc. |
| **Preconditions:**                    | 1.The administrator has logged into  the system administration backend.  2.The administrator has points system  management permissions. |
| **Flow of events:**                   | 1.The administrator navigates to the points parameters configuration  page.  2.The administrator sets basic points parameters:  l Points exchange  rate  l Daily points  earning limit  l Points validity  period settings  l Points usage scope  3.The administrator configures points activity parameters.  4.The administrator saves the parameter configuration.  5.The system applies the new parameters.. |
| **Postconditions:**                   | 1.The points parameters update takes effect.  2.The parameter change record is saved. |
| **Alternative flows and exceptions:** |                                                              |
| **Non-behavior requirements:**        | Parameter  modifications must be logged.  Important parameter  changes require confirmation. |
| **Assumptions:**                      | Parameter settings  align with business requirements.        |
| **Issue:**                            |                                                              |
| **Source:**                           |                                                              |

 

 

 

 

 

 

 

 

 

 

 

 

 

| **Use case name:**                    | Browse Maintenance Knowledge                                 |
| ------------------------------------- | ------------------------------------------------------------ |
| **Use case ID:**                      | UC - 501                                                     |
| **Primary actor:**                    | Customer                                                     |
| **Secondary actor(s):**               | None                                                         |
| **Brief description:**                | The customer browses maintenance  knowledge related to products. |
| **Preconditions:**                    | The maintenance knowledge base has  relevant content available. |
| **Flow of events:**                   | 1. Customer accesses the maintenance  knowledge browsing feature in the FMS.  2. Customer navigates through or searches  for specific maintenance knowledge topics. |
| **Postconditions:**                   | Customer successfully browses the desired  maintenance knowledge. |
| **Alternative flows and exceptions:** | If the requested maintenance knowledge is  not found, the system notifies the customer. |
| **Non - behavior requirements:**      | The system should present maintenance  knowledge in a clear and organized manner. |
| **Assumptions:**                      | The customer has basic system navigation  skills.            |
| **Issue:**                            |                                                              |
| **Source:**                           |                                                              |

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 



| **Use case name:**                     | View  Product Reviews                                        |
| -------------------------------------- | ------------------------------------------------------------ |
| **Use case ID:**                       | UC  - 502                                                    |
| **Primary actor:**                     | Customer                                                     |
| **Secondary actor(s):**                | None                                                         |
| **Brief description:**                 | The  customer views reviews submitted by other users about products. |
| **Preconditions:**                     | There  are product reviews available in the FMS.             |
| **Flow of events:**                    | 1.  Customer selects the product reviews viewing option in the FMS.  2.  Customer views the reviews for the selected product(s). |
| **Postconditions:**                    | Customer  successfully views the product reviews.            |
| **Alternative flows and  exceptions:** | If  there are no reviews for a selected product, the system informs the customer. |
| **Non - behavior  requirements:**      | The  system should display product reviews accurately and in a readable format. |
| **Assumptions:**                       | The  customer knows which product's reviews they want to view. |
| **Issue:**                             |                                                              |
| **Source:**                            |                                                              |



 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

| **Use case name:**                    | Maintain Knowledge Base                                      |
| ------------------------------------- | ------------------------------------------------------------ |
| **Use case ID:**                      | UC - 503                                                     |
| **Primary actor:**                    | Merchant                                                     |
| **Secondary actor(s):**               | None                                                         |
| **Brief description:**                | The merchant maintains (adds, edits,  deletes) the content of the knowledge base in the FMS. |
| **Preconditions:**                    | The merchant has the authorization to  maintain the knowledge base. |
| **Flow of events:**                   | 1. Merchant accesses the knowledge base  maintenance interface in the FMS.  2. Merchant performs actions such as  adding new knowledge entries, editing existing ones, or deleting outdated  entries. |
| **Postconditions:**                   | The knowledge base is updated with the  merchant's maintenance actions. |
| **Alternative flows and exceptions:** | If the merchant attempts an unauthorized  maintenance action, the system denies access. |
| **Non - behavior requirements:**      | The system should have a user - friendly  interface for knowledge base maintenance. |
| **Assumptions:**                      | The merchant is trained on how to  maintain the knowledge  base. |
| **Issue:**                            |                                                              |
| **Source:**                           |                                                              |

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

| **Use case name:**                    | Manage User Reviews                                          |
| ------------------------------------- | ------------------------------------------------------------ |
| **Use case ID:**                      | UC - 504                                                     |
| **Primary actor:**                    | Merchant                                                     |
| **Secondary actor(s):**               | None                                                         |
| **Brief description:**                | The merchant manages (approves, rejects,  responds to) user reviews in the FMS. |
| **Preconditions:**                    | There are user reviews pending management  or existing reviews to be handled. |
| **Flow of events:**                   | 1. Merchant accesses the user reviews  management section in the FMS.  2. Merchant reviews user reviews and  takes appropriate actions like approving, rejecting, or responding. |
| **Postconditions:**                   | User reviews are managed as per the  merchant's actions.     |
| **Alternative flows and exceptions:** | If a review is inappropriate, the  merchant can reject and remove it; if a review needs clarification, the  merchant can respond. |
| **Non - behavior requirements:**      | The system should enable efficient  management of user reviews. |
| **Assumptions:**                      | The merchant has guidelines for managing  user reviews.      |
| **Issue:**                            |                                                              |
| **Source:**                           |                                                              |

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

| **Use case name:**                    | Configure Content Rules                                      |
| ------------------------------------- | ------------------------------------------------------------ |
| **Use case ID:**                      | UC - 505                                                     |
| **Primary actor:**                    | Administrator                                                |
| **Secondary actor(s):**               | None                                                         |
| **Brief description:**                | The administrator configures rules  related to content within the FMS. |
| **Preconditions:**                    | The administrator has the authority to  configure content rules. |
| **Flow of events:**                   | 1. Administrator accesses the content  rules configuration module in the FMS.  2. Administrator sets up or modifies  rules for content management, such as content approval workflows, content  display rules, etc. |
| **Postconditions:**                   | The system's content rules are updated  according to the administrator's configuration. |
| **Alternative flows and exceptions:** | If conflicting rules are configured, the  system notifies the administrator. |
| **Non - behavior requirements:**      | The system should validate content rules  to ensure they are logical and consistent. |
| **Assumptions:**                      | The administrator understands the  system's content management needs and rule logic. |
| **Issue:**                            |                                                              |
| **Source:**                           |                                                              |

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

| **Use case name:**                    | Check order status                                           |
| ------------------------------------- | ------------------------------------------------------------ |
| **Use case ID:**                      | UC - 601                                                     |
| **Primary actor:**                    | Customer                                                     |
| **Secondary actor(s):**               | None                                                         |
| **Brief description:**                | The customer checks the status of their  placed orders.      |
| **Preconditions:**                    | The customer has placed an order in the  FMS.                |
| **Flow of events:**                   | 1. Customer accesses the order status  checking feature in the FMS.  2. Customer views the status of their  orders. |
| **Postconditions:**                   | Customer successfully checks the order  status.              |
| **Alternative flows and exceptions:** | If there are no orders for the customer,  the system informs the customer. |
| **Non - behavior requirements:**      | The system should display order status  information accurately and in a timely manner. |
| **Assumptions:**                      | The customer knows how to access the  order status checking feature. |
| **Issue:**                            |                                                              |
| **Source:**                           |                                                              |

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

| **Use case name:**                    | Arrange Product Delivery                                     |
| ------------------------------------- | ------------------------------------------------------------ |
| **Use case ID:**                      | UC - 602                                                     |
| **Primary actor:**                    | Merchant                                                     |
| **Secondary actor(s):**               | None                                                         |
| **Brief description:**                | The merchant arranges for the delivery of  products to customers. |
| **Preconditions:**                    | The merchant has products ready for  delivery and customer delivery information is available. |
| **Flow of events:**                   | 1. Merchant accesses the product delivery  arrangement interface in the FMS.  2. Merchant enters or selects delivery  details and arranges for product delivery. |
| **Postconditions:**                   | Product delivery is successfully  arranged.                  |
| **Alternative flows and exceptions:** | If there are issues with delivery details  (e.g., invalid address), the system notifies the merchant. |
| **Non - behavior requirements:**      | The system should facilitate efficient  arrangement of product delivery. |
| **Assumptions:**                      | The merchant has knowledge of delivery  logistics.           |
| **Issue:**                            |                                                              |
| **Source:**                           |                                                              |

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

| **Use case name:**                    | Handle Shipping Operations                                   |
| ------------------------------------- | ------------------------------------------------------------ |
| **Use case ID:**                      | UC - 603                                                     |
| **Primary actor:**                    | Merchant                                                     |
| **Secondary actor(s):**               | None                                                         |
| **Brief description:**                | The merchant handles various shipping -  related operations (e.g., preparing shipping labels, coordinating with  carriers). |
| **Preconditions:**                    | Products are ready for shipping and  shipping requirements are defined. |
| **Flow of events:**                   | 1. Merchant accesses the shipping  operations handling module in the FMS.  2. Merchant performs shipping - related  tasks such as generating shipping documents, scheduling pickups, etc. |
| **Postconditions:**                   | Shipping operations are successfully  handled.               |
| **Alternative flows and exceptions:** | If there are problems with shipping  carrier integration, the system notifies the merchant. |
| **Non - behavior requirements:**      | The system should support smooth  execution of shipping operations. |
| **Assumptions:**                      | The merchant is familiar with shipping  processes and systems. |
| **Issue:**                            |                                                              |
| **Source:**                           |                                                              |

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

| **Use case name:**                    | Manage Delivery Orders                                       |
| ------------------------------------- | ------------------------------------------------------------ |
| **Use case ID:**                      | UC - 604                                                     |
| **Primary actor:**                    | Merchant                                                     |
| **Secondary actor(s):**               | None                                                         |
| **Brief description:**                | The merchant manages delivery orders  (e.g., tracking, updating status, resolving issues). |
| **Preconditions:**                    | There are delivery orders in the system  that need management. |
| **Flow of events:**                   | 1. Merchant accesses the delivery orders  management section in the FMS.  2. Merchant tracks delivery progress,  updates order statuses, and addresses any delivery - related issues. |
| **Postconditions:**                   | Delivery orders are effectively managed.                     |
| **Alternative flows and exceptions:** | If a delivery order is delayed or lost,  the system allows the merchant to update status and take appropriate actions. |
| **Non - behavior requirements:**      | The system should provide comprehensive  tools for managing delivery orders. |
| **Assumptions:**                      | The merchant has a process for managing  delivery orders.    |
| **Issue:**                            |                                                              |
| **Source:**                           |                                                              |

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

| **Use case name:**                    | Configure Delivery Rules                                     |
| ------------------------------------- | ------------------------------------------------------------ |
| **Use case ID:**                      | UC - 605                                                     |
| **Primary actor:**                    | Administrator                                                |
| **Secondary actor(s):**               | None                                                         |
| **Brief description:**                | The administrator configures rules  related to product delivery within the FMS. |
| **Preconditions:**                    | The administrator has the authority to  configure delivery rules. |
| **Flow of events:**                   | 1. Administrator accesses the delivery  rules configuration module in the FMS.  2. Administrator sets up or modifies  rules for delivery, such as delivery timeframes, shipping methods, etc. |
| **Postconditions:**                   | The system's delivery rules are updated  according to the administrator's configuration. |
| **Alternative flows and exceptions:** | If conflicting delivery rules are  configured, the system notifies the administrator. |
| **Non - behavior requirements:**      | The system should validate delivery rules  to ensure they are logical and consistent. |
| **Assumptions:**                      | The administrator understands the  system's delivery management needs and rule logic. |
| **Issue:**                            |                                                              |
| **Source:**                           |                                                              |

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

 

| **Use case name:**                    | View Personal Reports                                        |
| ------------------------------------- | ------------------------------------------------------------ |
| **Use case ID:**                      | UC-701                                                       |
| **Primary actor:**                    | Customer                                                     |
| **Secondary actor(s):**               | Merchant,  Administrator                                     |
| **Brief description:**                | This use case allows  users (Customers, Merchants, and Administrators) to view personalized reports  based on their roles. Customers can view their purchase history and behavior  analysis; Merchants can access sales performance and inventory turnover  reports; Administrators can monitor system-wide analytics and user activity  trends. |
| **Preconditions:**                    | 1. The user is authenticated and logged  into the system.  2. The system has collected sufficient  data for report generation. |
| **Flow of events:**                   | 1.User navigates to the “Reports” section from the  dashboard.   2.System identifies the user role (Customer, Merchant, or  Administrator).   3.System retrieves relevant data based on the user’s role  and permissions.   4.System generates and displays visual reports (charts,  tables, summaries).   5.User can filter reports by date range, category, or other  criteria.   6.User may export the report in PDF or Excel format. |
| **Postconditions:**                   | 1.The user successfully views and optionally downloads the  report.   2.The system logs the report access for auditing purposes. |
| **Alternative flows and exceptions:** | -If no data is  available for the selected period, the system displays a “No Data Available”  message.   -If the user session expires, they are redirected to the  login page.   -If the user lacks permission, an “Access Denied” message  is shown. |
| **Non-behavior requirements:**        | - Reports must be  generated within 5 seconds.  -The system must ensure data privacy and role-based access  control.  -Reports should be responsive and viewable on both desktop  and mobile devices. |
| **Assumptions:**                      | -The system has a functioning data analytics module.   -Users have appropriate permissions to access their  respective reports. |
| **Issue:**                            |                                                              |
| **Source:**                           |                                                              |

 

 

 

| **Use case name:**                    | View Business Reports                                        |
| ------------------------------------- | ------------------------------------------------------------ |
| **Use case ID:**                      | UC-702                                                       |
| **Primary actor:**                    | Merchant                                                     |
| **Secondary actor(s):**               | None                                                         |
| **Brief description:**                | This use case allows  merchants to view comprehensive business reports, including sales  performance, inventory turnover, customer behavior, and marketing  effectiveness. These reports help merchants make data-driven decisions to  optimize operations and improve profitability. |
| **Preconditions:**                    | 1.The merchant is authenticated and logged into the system.    2.The system has collected sufficient business data. |
| **Flow of events:**                   | 1.Merchant accesses the  “Business Reports” section from the dashboard.   2.System retrieves relevant  business data for the merchant’s store(s).   3.System generates visual  reports, including:      Sales performance       (daily/weekly/monthly)   Inventory turnover and       stock alerts   Customer purchase       behavior and preferences   Marketing campaign       effectiveness    4.Merchant applies filters  (e.g., date range, product category, region).   5.Reports are updated in  real-time based on selected filters.   6.Merchant can export  reports in PDF or Excel format. |
| **Postconditions:**                   | 1.Merchant successfully  views and optionally downloads business reports.   2.System logs the report  access for auditing and analytics. |
| **Alternative flows and exceptions:** | -If no data is available for the selected filters, the  system displays a “No Data Available” message.   -If the merchant’s session expires, they are redirected to  the login page.   -If there is a system error during report generation, an  error message is shown with retry options. |
| **Non-behavior requirements:**        | -Reports must load within 5 seconds.   -The system must ensure secure access and data isolation  between merchants. |
| **Assumptions:**                      | -The merchant has at least  one store with active sales and inventory data.   -The system has a  functioning analytics engine and role-based access control. |
| **Issue:**                            |                                                              |
| **Source:**                           |                                                              |

 

 

| **Use case name:**                    | Analyze Operational Data                                     |
| ------------------------------------- | ------------------------------------------------------------ |
| **Use case ID:**                      | UC-703                                                       |
| **Primary actor:**                    | Merchant                                                     |
| **Secondary actor(s):**               | None                                                         |
| **Brief description:**                | This use case enables  merchants to perform in-depth analysis of their operational data, including  sales trends, inventory turnover, customer behavior, and marketing  performance. The insights help merchants make informed decisions to optimize  business strategies and improve efficiency. |
| **Preconditions:**                    | 1.The merchant is  authenticated and logged into the system.   2.The system has collected  sufficient operational data from various modules (orders, inventory,  marketing, etc.). |
| **Flow of events:**                   | 1.Merchant accesses the  “Operational Analysis” section from the dashboard.   2.System presents a visual  dashboard with key performance indicators (KPIs).   3.Merchant selects specific  data dimensions to analyze.   4.System generates  interactive charts and tables showing:      Sales trends and seasonal       patterns   Inventory turnover rates       and stock aging   Customer purchase       frequency and preferences   Marketing campaign ROI       and engagement metrics    5.Merchant drills down into  specific data points for detailed insights.   6.Merchant may export  analysis results or save custom views for future reference. |
| **Postconditions:**                   | 1.Merchant gains actionable  insights from the operational data.   2.System logs the analysis  session for future personalization or auditing. |
| **Alternative flows and exceptions:** | -If data is incomplete or  unavailable, the system notifies the merchant and suggests alternative  filters.   -If the system encounters a  processing error, an error message is displayed with retry options.   -If the merchant lacks  permission for certain data, access is restricted with a warning message. |
| **Non-behavior requirements:**        | -Data visualizations must  load within 5 seconds.   -The system must ensure  data accuracy and real-time synchronization. |
| **Assumptions:**                      | -The merchant has  sufficient historical data for meaningful analysis. |
| **Issue:**                            |                                                              |
| **Source:**                           |                                                              |

 

 

 

| **Use case name:**                    | Configure Data Permissions                                   |
| ------------------------------------- | ------------------------------------------------------------ |
| **Use case ID:**                      | UC-704                                                       |
| **Primary actor:**                    | Administrator                                                |
| **Secondary actor(s):**               | None                                                         |
| **Brief description:**                | This use case allows  administrators to configure and manage data access permissions for different  user roles (e.g., Customer, Merchant, Staff). It ensures that users can only  access data relevant to their roles, maintaining data security and  compliance. |
| **Preconditions:**                    | 1.The administrator is  authenticated and logged into the system.   2.The system has predefined  user roles and associated data modules. |
| **Flow of events:**                   | 1.Administrator navigates  to the “Permissions Management” section.   2.System displays a list of  user roles and associated permissions.   3.Administrator selects a  role (e.g., Merchant, Customer, Designer).   4.Administrator configures  data access levels (e.g., view, edit, export) for each module:      Sales reports   Inventory data   Customer information   Marketing analytics    5.Administrator saves the  configuration.   6.System updates the access  control settings and logs the changes. |
| **Postconditions:**                   | 1.Updated data permissions  are applied to the selected user role.   2.System logs the  configuration changes for auditing. |
| **Alternative flows and exceptions:** | -If the administrator tries  to assign unauthorized permissions, the system displays a warning and blocks  the action.   -If the system fails to  save changes due to a backend error, an error message is shown with retry  options.   -If the administrator  session expires, they are redirected to the login page. |
| **Non-behavior requirements:**        | -Changes to permissions  must take effect immediately.   -The system must maintain  an audit trail of all permission changes. |
| **Assumptions:**                      | -The system has a  role-based access control framework in place.   -The administrator has full  access to all system modules and configuration tools. |
| **Issue:**                            |                                                              |
| **Source:**                           |                                                              |

 

 

 

 

| **Use case name:**                    | Manage System Reports                                        |
| ------------------------------------- | ------------------------------------------------------------ |
| **Use case ID:**                      | UC-705                                                       |
| **Primary actor:**                    | Administrator                                                |
| **Secondary actor(s):**               | None                                                         |
| **Brief description:**                | This use case allows  administrators to manage all system-generated reports,including configuring  report templates, scheduling automated report generation, managing access  permissions, and maintaining report archives. It ensures that reporting  across the platform is consistent, secure, and aligned with organizational  needs. |
| **Preconditions:**                    | 1.The administrator is  authenticated and logged into the system.   2.The system has access to  all relevant data sources and reporting modules. |
| **Flow of events:**                   | 1.Administrator accesses the “System Reports Management”  interface.   2.System displays a list of  all available report types .  3.Administrator performs  one or more of the following actions:      Create or edit report       templates .   Schedule automated report       generation .   Assign or revoke access       permissions for specific user roles   Archive or delete       outdated reports   Export reports for backup       or compliance purposes    4.System validates and  applies the changes.   5.System logs all  administrative actions for auditing. |
| **Postconditions:**                   | 1.System reports are  updated, scheduled, and secured according to the administrator’s  configuration.   2.All changes are logged  for traceability. |
| **Alternative flows and exceptions:** | -If the administrator  attempts to delete a report in use, the system prompts for confirmation.   -If a scheduled report  fails to generate, the system sends an alert.   -If the administrator lacks  permission for a specific report type, access is denied. |
| **Non-behavior requirements:**        | -Report management actions  must be completed within 3 seconds.   -The system must enforce  strict access control and maintain an audit trail. |
| **Assumptions:**                      | -The system has a  centralized reporting engine.   -The administrator has full  access to all reporting features and modules. |
| **Issue:**                            |                                                              |
| **Source:**                           |                                                              |

 

 

 

| **Use case name:**                    | Manage Personal Account                                      |
| ------------------------------------- | ------------------------------------------------------------ |
| **Use case ID:**                      | UC-801                                                       |
| **Primary actor:**                    | Customer                                                     |
| **Secondary actor(s):**               | None                                                         |
| **Brief description:**                | This use case allows  customers to manage their personal account information, including updating  profile details, changing passwords, managing addresses, and viewing login  history. It ensures that users can maintain control over their personal data  and account security. |
| **Preconditions:**                    | 1.The customer is  authenticated and logged into the system.   2.The system has an active  user account associated with the customer. |
| **Flow of events:**                   | 1.Customer navigates to the  “My Account” section.   2.stem displays current  account information, including:      Name, email, phone number   Delivery addresses   Password settings   Login history    3.stomer performs one or  more of the following actions:      Edit personal information       (e.g., name, contact info)   Add, edit, or delete       delivery addresses   Change password   View recent login       activity    4.stem validates the  changes and updates the account.   5.stem confirms the update  and logs the action. |
| **Postconditions:**                   | 1.stomer’s account  information is updated successfully.   2.stem logs the changes for  security and auditing. |
| **Alternative flows and exceptions:** | 1. the customer enters  invalid data (e.g., incorrect email format), the system prompts for  correction.  2. the password change  fails due to incorrect current password, an error message is shown.  3. the session expires, the  customer is redirected to the login page. |
| **Non-behavior requirements:**        | All       updates must be processed within 2 seconds.   The       interface must be mobile-friendly and accessible. |
| **Assumptions:**                      | -The customer has a valid  and active account.  -The system has role-based  access control to restrict unauthorized access. |
| **Issue:**                            |                                                              |
| **Source:**                           |                                                              |

 

 

 

| **Use case name:**                    | Manage User Permissions                                      |
| ------------------------------------- | ------------------------------------------------------------ |
| **Use case ID:**                      | UC-802                                                       |
| **Primary actor:**                    | Merchant                                                     |
| **Secondary actor(s):**               | None                                                         |
| **Brief description:**                | This use case allows  merchants to manage user permissions within their own store or organization.  Merchants can assign roles (e.g., cashier, inventory manager, designer),  define access levels for each role, and ensure that employees only access the  data and functions necessary for their responsibilities. |
| **Preconditions:**                    | 1.he merchant is  authenticated and logged into the system.   2. The merchant has permission to manage sub-user  accounts and roles. |
| **Flow of events:**                   | 1. Merchant accesses the  “User Permissions” section from the system settings.   2. System displays a list  of current users and their assigned roles.   3.Merchant performs one or  more of the following actions:      Create a new user account   Assign or modify a user’s       role (e.g., Sales Staff, Inventory Clerk)   Define or adjust       permissions for each role (e.g., view-only, edit, export)   Deactivate or delete user       accounts    4. System validates the  changes and updates the permission settings.   5. System logs all  permission changes for auditing. |
| **Postconditions:**                   | 1. User roles and  permissions are updated successfully.   2. System enforces the new  permissions immediately. |
| **Alternative flows and exceptions:** | If       the merchant tries to assign unauthorized permissions, the system blocks       the action and displays a warning.   If       a user account is deleted, the system prompts for confirmation and       reassigns any pending tasks.   If       the session expires, the merchant is redirected to the login page. |
| **Non-behavior requirements:**        | Permission       changes must take effect in real-time.    The       system must enforce role-based access control (RBAC) consistently.    All       changes must be logged with timestamps and user IDs. |
| **Assumptions:**                      | 1.The merchant has a  multi-user store management structure.   2. The system supports  customizable roles and permissions for merchants. |
| **Issue:**                            |                                                              |
| **Source:**                           |                                                              |

 

 

 

| **Use case name:**                    | View Operation Logs                                          |
| ------------------------------------- | ------------------------------------------------------------ |
| **Use case ID:**                      | UC-803                                                       |
| **Primary actor:**                    | Merchant                                                     |
| **Secondary actor(s):**               | None                                                         |
| **Brief description:**                | This use case allows  merchants to view operation logs related to their store activities, including  employee login records, order processing, inventory changes, and permission  updates. It supports internal auditing, employee monitoring, and issue  tracking. |
| **Preconditions:**                    | 1. The merchant is  authenticated and logged into the system.   2. The system has been  logging relevant operational activities. |
| **Flow of events:**                   | 1. The merchant navigates  to the “Operation Logs” section in the system.   2. The system displays a  list of logs related to the merchant’s store, including:      Employee login/logout       records   Order creation,       modification, and cancellation   Inventory adjustments   Role and permission       changes    3. The merchant filters  logs by:      Date/time range   Employee account or role   Operation type (e.g.,       login, update, delete)   Module (e.g., orders,       inventory, marketing)    4. The merchant views  detailed log entries and may export them (e.g., PDF or Excel). |
| **Postconditions:**                   | 1. The merchant  successfully reviews and analyzes the operation logs.   2. The system logs the  merchant’s access to the logs for traceability. |
| **Alternative flows and exceptions:** | - If no logs match the  filter criteria, the system displays “No Records Found.”   -If the log service is  temporarily unavailable, the system shows an error and retry option.   -If the merchant attempts  to access logs outside their permission scope, access is denied with a  warning. |
| **Non-behavior requirements:**        | -Log queries must respond  within 3 seconds.   -The system must ensure log  data integrity and prevent tampering. |
| **Assumptions:**                      | -The merchant has  permission to manage and view logs related to their store.   -The system has a  centralized logging and query mechanism. |
| **Issue:**                            |                                                              |
| **Source:**                           |                                                              |

 

 

| **Use case name:**                    | Configure Data Permissions                                   |
| ------------------------------------- | ------------------------------------------------------------ |
| **Use case ID:**                      | UC-804                                                       |
| **Primary actor:**                    | Administrator                                                |
| **Secondary actor(s):**               | None                                                         |
| **Brief description:**                | This use case allows  administrators to configure and manage data access permissions for different  user roles (e.g., Customer, Merchant, Staff). It ensures that users can only  access the data and system modules relevant to their responsibilities,  maintaining data security and compliance. |
| **Preconditions:**                    | 1.The administrator is  authenticated and logged into the system.   2.The system has predefined  user roles and permission structures. |
| **Flow of events:**                   | 1.Administrator navigates  to the “Data Permissions” configuration panel.   2.System displays a list of  user roles and their current data access settings.   3.Administrator selects a  role (e.g., Merchant, Customer, Designer).   4.Administrator performs  one or more of the following actions:      Grant or revoke access to       specific data modules (e.g., sales reports, inventory, customer data)   Define access levels       (e.g., view, edit, export)   Apply restrictions based       on data scope (e.g., store-specific, region-specific)    5.System validates and  applies the updated permissions.   6.System logs all changes  for auditing and compliance. |
| **Postconditions:**                   | 1.Updated data permissions  are enforced across the system. 2.All permission changes are recorded in the  system logs. |
| **Alternative flows and exceptions:** | If       the administrator attempts to assign invalid or conflicting permissions,       the system displays an error and prevents the action.   If       the system encounters a backend error during update, an error message is       shown with retry options.   If       the administrator’s session expires, they are redirected to the login       page. |
| **Non-behavior requirements:**        | Permission       changes must take effect immediately.   The       system must enforce role-based access control (RBAC) consistently. |
| **Assumptions:**                      | 1.The system supports  dynamic permission configuration.   2.The administrator has  full access to all system modules and permission settings. |
| **Issue:**                            |                                                              |
| **Source:**                           |                                                              |

 

 

| **Use case name:**                    | Manage Database                                              |
| ------------------------------------- | ------------------------------------------------------------ |
| **Use case ID:**                      | UC-805                                                       |
| **Primary actor:**                    | Administrator                                                |
| **Secondary actor(s):**               | None                                                         |
| **Brief description:**                | This use case allows  administrators to manage the system database, including performing backups,  restoring data, optimizing performance, and managing data integrity. It  ensures the reliability, availability, and security of the platform’s data  infrastructure. |
| **Preconditions:**                    | 1.The administrator is  authenticated and has database management privileges.  2.The system is running in  a stable state and connected to the database. |
| **Flow of events:**                   | 1.Administrator accesses  the “Database Management” panel.   2.The system displays  available database management functions, such as:      Backup database   Restore from backup   Optimize database       performance (e.g., indexing, cleanup)   Monitor database health       and usage   Manage data retention and       archival policies    3.Administrator selects a  task (e.g., initiate backup).   4.System performs the  selected operation and provides real-time status updates.   5.Upon completion, the  system logs the operation and notifies the administrator. |
| **Postconditions:**                   | 1.The selected database  operation is completed successfully.   2.The system logs the  action for auditing and recovery purposes. |
| **Alternative flows and exceptions:** | If       a backup or restore operation fails, the system displays an error       message and logs the failure.    If       the administrator attempts an unauthorized operation, access is denied.    If       the database is under high load, the system may delay or queue the       operation. |
| **Non-behavior requirements:**        | -Backup and restore  operations must not interrupt live services.   -The system must ensure  data consistency and integrity during all operations. |
| **Assumptions:**                      | -The system has a built-in  database management interface or is integrated with a database management  tool.   -The administrator has the  necessary technical knowledge and permissions to perform database operations. |
| **Issue:**                            |                                                              |
| **Source:**                           |                                                              |

 

 