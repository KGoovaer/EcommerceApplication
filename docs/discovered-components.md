# Discovered Components

**Application:** EcommerceApp — Java EE (Servlet 3.0 + JSP + SQLite + Maven WAR)  
**Analysis Date:** Auto-generated from source code discovery  
**Sources:** `com/servlet/`, `com/dao/`, `com/entity/`, `com/conn/`, `com/utility/`, `src/main/webapp/`

---

## Table of Contents

- [Servlets](#servlets)
- [DAO Classes](#dao-classes)
- [Entity Classes](#entity-classes)
- [Key JSPs](#key-jsps)
- [Infrastructure / Utility](#infrastructure--utility)
- [Cross-Domain Table Usage Matrix](#cross-domain-table-usage-matrix)

---

## Servlets

### CP-001: ShippingAddress2

| Field | Value |
|---|---|
| **Component ID** | CP-001 |
| **Name** | ShippingAddress2 |
| **Type** | Servlet |
| **Package** | `com.servlet.ShippingAddress2` |
| **URL Mapping** | `POST /ShippingAddress2` |
| **Annotation** | `@WebServlet("/ShippingAddress2")`, `@MultipartConfig` |

**Responsibility:** Routes the shipping address form submission to the appropriate payment confirmation page based on the selected payment method (cash or online).

**Methods Used:** `doPost()`  
**Dependencies:** None (no DAO calls)  
**DB Tables Accessed:** None  
**Related Flows:** FL-014  

---

### CP-002: addContactus

| Field | Value |
|---|---|
| **Component ID** | CP-002 |
| **Name** | addContactus |
| **Type** | Servlet |
| **Package** | `com.servlet.addContactus` |
| **URL Mapping** | `POST /addContactus` |
| **Annotation** | `@WebServlet("/addContactus")`, `@MultipartConfig` |

**Responsibility:** Receives and persists guest Contact Us form submissions.

**Methods Used:** `doPost()`  
**Dependencies:** `DAO5`, `DBConnect`, `contactus` entity  
**DB Tables Accessed:** `Contactus` (INSERT)  
**Related Flows:** FL-018  

---

### CP-003: addContactusc

| Field | Value |
|---|---|
| **Component ID** | CP-003 |
| **Name** | addContactusc |
| **Type** | Servlet |
| **Package** | `com.servlet.addContactusc` |
| **URL Mapping** | `POST /addContactusc` |
| **Annotation** | `@WebServlet("/addContactusc")`, `@MultipartConfig` |

**Responsibility:** Receives and persists Contact Us form submissions from authenticated customers. Redirects to customer-navbar success/fail pages.

**Methods Used:** `doPost()`  
**Dependencies:** `DAO5`, `DBConnect`, `contactus` entity  
**DB Tables Accessed:** `Contactus` (INSERT)  
**Related Flows:** FL-019  

---

### CP-004: addcustomer

| Field | Value |
|---|---|
| **Component ID** | CP-004 |
| **Name** | addcustomer |
| **Type** | Servlet |
| **Package** | `com.servlet.addcustomer` |
| **URL Mapping** | `POST /addcustomer` |
| **Annotation** | `@WebServlet("/addcustomer")`, `@MultipartConfig` |

**Responsibility:** Handles new customer registration. Validates uniqueness of Name and Email_Id before inserting into `customer` table. Sets a flash cookie on success.

**Methods Used:** `doPost()`  
**Dependencies:** `DAO2`, `DBConnect`, `customer` entity  
**DB Tables Accessed:** `customer` (SELECT for duplicate check, INSERT for registration)  
**Related Flows:** FL-001  

---

### CP-005: addproduct

| Field | Value |
|---|---|
| **Component ID** | CP-005 |
| **Name** | addproduct |
| **Type** | Servlet |
| **Package** | `com.servlet.addproduct` |
| **URL Mapping** | `POST /addproduct` |
| **Annotation** | `@WebServlet("/addproduct")`, `@MultipartConfig` |

**Responsibility:** Handles multipart product creation form from admin. Delegates all logic (file upload, parsing, DB insert) to `DAO.addproduct()`.

**Methods Used:** `doPost()`  
**Dependencies:** `DAO`, `DBConnect`  
**DB Tables Accessed:** `product` (INSERT)  
**Related Flows:** FL-004  

---

### CP-006: addtocart

| Field | Value |
|---|---|
| **Component ID** | CP-006 |
| **Name** | addtocart |
| **Type** | Servlet |
| **Package** | `com.servlet.addtocart` |
| **URL Mapping** | `GET /addtocart` |
| **Annotation** | `@WebServlet("/addtocart")`, `@MultipartConfig` |

**Responsibility:** Adds a product to an authenticated customer's cart. Checks for existing item and increments quantity or inserts new row. Sets flash cookie `cart`.

**Methods Used:** `doGet()`  
**Dependencies:** `DAO3`, `DBConnect`, `cart` entity  
**DB Tables Accessed:** `cart` (SELECT check, UPDATE or INSERT)  
**Related Flows:** FL-007  

---

### CP-007: addtocartnull

| Field | Value |
|---|---|
| **Component ID** | CP-007 |
| **Name** | addtocartnull |
| **Type** | Servlet |
| **Package** | `com.servlet.addtocartnull` |
| **URL Mapping** | `GET /addtocartnull` |
| **Annotation** | `@WebServlet("/addtocartnull")`, `@MultipartConfig` |

**Responsibility:** Adds a product to the guest (anonymous) cart. Cart rows have `Name = NULL`. Checks for existing guest item and increments or inserts.

**Methods Used:** `doGet()`  
**Dependencies:** `DAO2`, `DBConnect`, `cart` entity  
**DB Tables Accessed:** `cart` (SELECT check, UPDATE or INSERT with NULL Name)  
**Related Flows:** FL-008  

---

### CP-008: addtocartnulla

| Field | Value |
|---|---|
| **Component ID** | CP-008 |
| **Name** | addtocartnulla |
| **Type** | Servlet |
| **Package** | `com.servlet.addtocartnulla` |
| **URL Mapping** | `GET /addtocartnulla` |
| **Annotation** | `@WebServlet("/addtocartnulla")`, `@MultipartConfig` |

**Responsibility:** Identical to `addtocartnull` but targets admin-navbar JSP views (`cartnulla.jsp`, `selecteditema.jsp`).

**Methods Used:** `doGet()`  
**Dependencies:** `DAO2`, `DBConnect`, `cart` entity  
**DB Tables Accessed:** `cart` (SELECT check, UPDATE or INSERT with NULL Name)  
**Related Flows:** FL-009  

---

### CP-009: checkadmin

| Field | Value |
|---|---|
| **Component ID** | CP-009 |
| **Name** | checkadmin |
| **Type** | Servlet |
| **Package** | `com.servlet.checkadmin` |
| **URL Mapping** | `POST /checkadmin` |
| **Annotation** | `@WebServlet("/checkadmin")`, `@MultipartConfig` |

**Responsibility:** Validates admin credentials against `usermaster` table. On success, sets persistent `tname` cookie (maxAge=9999) and redirects to admin dashboard.

**Methods Used:** `doPost()`  
**Dependencies:** `DAO2`, `DBConnect`, `usermaster` entity  
**DB Tables Accessed:** `usermaster` (SELECT)  
**Related Flows:** FL-003  

---

### CP-010: checkcustomer

| Field | Value |
|---|---|
| **Component ID** | CP-010 |
| **Name** | checkcustomer |
| **Type** | Servlet |
| **Package** | `com.servlet.checkcustomer` |
| **URL Mapping** | `POST /checkcustomer` |
| **Annotation** | `@WebServlet("/checkcustomer")`, `@MultipartConfig` |

**Responsibility:** Validates customer credentials. On success, sets persistent `cname` cookie (Email_Id, maxAge=9999). Supports cart-continuation flow by checking `CusName` parameter to determine redirect target.

**Methods Used:** `doPost()`  
**Dependencies:** `DAO2`, `DBConnect`, `customer` entity  
**DB Tables Accessed:** `customer` (SELECT)  
**Related Flows:** FL-002  

---

### CP-011: deletecustomer

| Field | Value |
|---|---|
| **Component ID** | CP-011 |
| **Name** | deletecustomer |
| **Type** | Servlet |
| **Package** | `com.servlet.deletecustomer` |
| **URL Mapping** | `GET /deletecustomer` |
| **Annotation** | `@WebServlet("/deletecustomer")` |

**Responsibility:** Admin-facing servlet to delete a customer by Name and Email_Id.

**Methods Used:** `doGet()`  
**Dependencies:** `DAO`, `DBConnect`, `customer` entity  
**DB Tables Accessed:** `customer` (DELETE)  
**Related Flows:** FL-020  

---

### CP-012: payprocess

| Field | Value |
|---|---|
| **Component ID** | CP-012 |
| **Name** | payprocess |
| **Type** | Servlet |
| **Package** | `com.servlet.payprocess` |
| **URL Mapping** | `POST /payprocess` |
| **Annotation** | `@WebServlet("/payprocess")`, `@MultipartConfig` |

**Responsibility:** Core checkout processing servlet. Validates cart, creates order in `orders`, copies cart to `order_details`, clears cart, updates date/name on order_details. Handles both guest (NULL Name) and authenticated customer flows.

**Methods Used:** `doPost()`  
**Dependencies:** `DAO4`, `DBConnect`, `orders` entity, `order_details` entity, `cart` entity  
**DB Tables Accessed:** `cart` (SELECT, INSERT-SELECT, DELETE), `orders` (INSERT), `order_details` (INSERT, UPDATE)  
**Related Flows:** FL-015  

---

### CP-013: remove_contactus

| Field | Value |
|---|---|
| **Component ID** | CP-013 |
| **Name** | remove_contactus |
| **Type** | Servlet |
| **Package** | `com.servlet.remove_contactus` |
| **URL Mapping** | `GET /remove_contactus` |
| **Annotation** | `@WebServlet("/remove_contactus")`, `@MultipartConfig` |

**Responsibility:** Admin servlet to delete a Contact Us entry by ID.

**Methods Used:** `doGet()`  
**Dependencies:** `DAO5`, `DBConnect`, `contactus` entity  
**DB Tables Accessed:** `Contactus` (DELETE)  
**Related Flows:** FL-021  

---

### CP-014: remove_orders

| Field | Value |
|---|---|
| **Component ID** | CP-014 |
| **Name** | remove_orders |
| **Type** | Servlet |
| **Package** | `com.servlet.remove_orders` |
| **URL Mapping** | `GET /remove_orders` |
| **Annotation** | `@WebServlet("/remove_orders")`, `@MultipartConfig` |

**Responsibility:** Admin servlet to delete an order from `orders` table by Order_Id. Redirects to admin `table_orders.jsp`.

**Methods Used:** `doGet()`  
**Dependencies:** `DAO2`, `DBConnect`, `orders` entity  
**DB Tables Accessed:** `orders` (DELETE)  
**Related Flows:** FL-017  

---

### CP-015: removecart

| Field | Value |
|---|---|
| **Component ID** | CP-015 |
| **Name** | removecart |
| **Type** | Servlet |
| **Package** | `com.servlet.removecart` |
| **URL Mapping** | `GET /removecart` |
| **Annotation** | `@WebServlet("/removecart")`, `@MultipartConfig` |

**Responsibility:** Customer-facing cart item removal. Deletes by Name (customer email) + pimage from `cart`. Redirects to `cart.jsp`.

**Methods Used:** `doGet()`  
**Dependencies:** `DAO2`, `DBConnect`, `cart` entity  
**DB Tables Accessed:** `cart` (DELETE)  
**Related Flows:** FL-010  

---

### CP-016: removecarta

| Field | Value |
|---|---|
| **Component ID** | CP-016 |
| **Name** | removecarta |
| **Type** | Servlet |
| **Package** | `com.servlet.removecarta` |
| **URL Mapping** | `GET /removecarta` |
| **Annotation** | `@WebServlet("/removecarta")`, `@MultipartConfig` |

**Responsibility:** Admin-view cart item removal for a specific customer's cart. Redirects to `carta.jsp?custname=<id>` after removal.

**Methods Used:** `doGet()`  
**Dependencies:** `DAO2`, `DBConnect`, `cart` entity  
**DB Tables Accessed:** `cart` (DELETE by Name + pimage)  
**Related Flows:** FL-012  

---

### CP-017: removecartnull

| Field | Value |
|---|---|
| **Component ID** | CP-017 |
| **Name** | removecartnull |
| **Type** | Servlet |
| **Package** | `com.servlet.removecartnull` |
| **URL Mapping** | `GET /removecartnull` |
| **Annotation** | `@WebServlet("/removecartnull")`, `@MultipartConfig` |

**Responsibility:** Guest cart item removal. Deletes by `Name IS NULL` + pimage from `cart`. Redirects to `cartnull.jsp`.

**Methods Used:** `doGet()`  
**Dependencies:** `DAO2`, `DBConnect`, `cart` entity  
**DB Tables Accessed:** `cart` (DELETE, Name IS NULL)  
**Related Flows:** FL-011  

---

### CP-018: removecartnulla

| Field | Value |
|---|---|
| **Component ID** | CP-018 |
| **Name** | removecartnulla |
| **Type** | Servlet |
| **Package** | `com.servlet.removecartnulla` |
| **URL Mapping** | `GET /removecartnulla` |
| **Annotation** | `@WebServlet("/removecartnulla")`, `@MultipartConfig` |

**Responsibility:** Admin-view guest cart item removal. Same logic as `removecartnull` but redirects to `cartnulla.jsp`.

**Methods Used:** `doGet()`  
**Dependencies:** `DAO2`, `DBConnect`, `cart` entity  
**DB Tables Accessed:** `cart` (DELETE, Name IS NULL)  
**Related Flows:** FL-013  

---

### CP-019: removeorders

| Field | Value |
|---|---|
| **Component ID** | CP-019 |
| **Name** | removeorders |
| **Type** | Servlet |
| **Package** | `com.servlet.removeorders` |
| **URL Mapping** | `GET /removeorders` |
| **Annotation** | `@WebServlet("/removeorders")`, `@MultipartConfig` |

**Responsibility:** Customer-facing order removal. Deletes order from `orders` by Order_Id. Redirects to `orders.jsp`.

**Methods Used:** `doGet()`  
**Dependencies:** `DAO2`, `DBConnect`, `orders` entity  
**DB Tables Accessed:** `orders` (DELETE)  
**Related Flows:** FL-016  

---

### CP-020: removetable_cart

| Field | Value |
|---|---|
| **Component ID** | CP-020 |
| **Name** | removetable_cart |
| **Type** | Servlet |
| **Package** | `com.servlet.removetable_cart` |
| **URL Mapping** | `GET /removetable_cart` |
| **Annotation** | `@WebServlet("/removetable_cart")`, `@MultipartConfig` |

**Responsibility:** Admin table management — removes a single cart row. Detects whether the row is a guest row (`id == "null"`) or a named customer row and calls the appropriate DAO method.

**Methods Used:** `doGet()`  
**Dependencies:** `DAO2`, `DBConnect`, `cart` entity  
**DB Tables Accessed:** `cart` (DELETE, either NULL Name or named)  
**Related Flows:** FL-022  

---

### CP-021: removetable_order_details

| Field | Value |
|---|---|
| **Component ID** | CP-021 |
| **Name** | removetable_order_details |
| **Type** | Servlet |
| **Package** | `com.servlet.removetable_order_details` |
| **URL Mapping** | `GET /removetable_order_details` |
| **Annotation** | `@WebServlet("/removetable_order_details")`, `@MultipartConfig` |

**Responsibility:** Admin table management — removes a single order_details row by Date + pimage composite key.

**Methods Used:** `doGet()`  
**Dependencies:** `DAO5`, `DBConnect`, `order_details` entity  
**DB Tables Accessed:** `order_details` (DELETE)  
**Related Flows:** FL-023  

---

## DAO Classes

### CP-022: DAO

| Field | Value |
|---|---|
| **Component ID** | CP-022 |
| **Name** | DAO |
| **Type** | DAO |
| **Package** | `com.dao.DAO` |

**Responsibility:** Data access for product management, customer management, and brand/category lookup.

**Constructor:** `DAO(Connection conn)`

**Methods:**

| Method | SQL | DB Tables | Purpose |
|---|---|---|---|
| `getAllbrand()` | `SELECT * FROM brand` | brand | Fetch all brands |
| `getAllcategory()` | `SELECT * FROM category` | category | Fetch all categories |
| `addproduct(HttpServletRequest)` | `INSERT INTO product(...)` | product | Parse multipart form, upload file, insert product |
| `getAllCustomer()` | `SELECT * FROM customer` | customer | Admin: list all customers |
| `deleteCustomer(customer)` | `DELETE FROM customer WHERE Name=? AND Email_Id=?` | customer | Delete customer by name + email |
| `getCustomer(String eid)` | `SELECT * FROM customer WHERE Email_Id=?` | customer | Fetch customer by email |

**Dependencies:** `DBConnect`, `MyUtilities`, `customer`, `brand`, `category`, `Product` entities, `commons-fileupload`

---

### CP-023: DAO2

| Field | Value |
|---|---|
| **Component ID** | CP-023 |
| **Name** | DAO2 |
| **Type** | DAO |
| **Package** | `com.dao.DAO2` |

**Responsibility:** Data access for authentication, customer registration, product detail view, guest/customer cart management, and order management.

**Constructor:** `DAO2(Connection conn)`

**Methods:**

| Method | SQL | DB Tables | Purpose |
|---|---|---|---|
| `getAllviewlist()` | `SELECT * FROM viewlist` | viewlist | Fetch all products (view) |
| `checkcust(customer)` | `SELECT * FROM customer WHERE Password=? AND Email_Id=?` | customer | Customer login check |
| `checkadmin(usermaster)` | `SELECT * FROM usermaster WHERE Name=? AND Password=?` | usermaster | Admin login check |
| `addcustomer(customer)` | `INSERT INTO customer(Name,Password,Email_Id,Contact_No) VALUES(?,?,?,?)` | customer | Register new customer |
| `getSelecteditem(String st)` | `SELECT * FROM viewlist WHERE Pimage=?` | viewlist | Fetch single product by image name |
| `checkaddtocartnull(cart)` | `SELECT * FROM cart WHERE Name IS NULL AND bname=? AND cname=? AND pname=? AND pprice=? AND pimage=?` | cart | Check if guest has item in cart |
| `updateaddtocartnull(cart)` | `UPDATE cart SET pquantity=(pquantity+1) WHERE Name IS NULL AND ...` | cart | Increment guest cart item quantity |
| `addtocartnull(cart)` | `INSERT INTO cart (bname,cname,pname,pprice,pquantity,pimage) VALUES(?,?,?,?,?,?)` | cart | Add new item to guest cart (no Name) |
| `getSelectedcart()` | `SELECT * FROM cart WHERE Name IS NULL` | cart | Fetch all guest cart items |
| `getcart(String ct)` | `SELECT * FROM cart WHERE Name=?` | cart | Fetch customer cart items |
| `removecartnull(cart)` | `DELETE FROM cart WHERE Name IS NULL AND pimage=?` | cart | Remove guest cart item by pimage |
| `removecart(cart)` | `DELETE FROM cart WHERE Name=? AND pimage=?` | cart | Remove customer cart item |
| `checkcust2(customer)` | `SELECT * FROM customer WHERE Name=? OR Email_Id=?` | customer | Duplicate check for registration |
| `removeorders(orders)` | `DELETE FROM orders WHERE Order_Id=?` | orders | Delete order by ID |

**Dependencies:** `DBConnect`, `customer`, `cart`, `viewlist`, `usermaster`, `orders` entities

---

### CP-024: DAO3

| Field | Value |
|---|---|
| **Component ID** | CP-024 |
| **Name** | DAO3 |
| **Type** | DAO |
| **Package** | `com.dao.DAO3` |

**Responsibility:** Data access for category-specific product views, authenticated customer cart management, and order/order-details retrieval.

**Constructor:** `DAO3(Connection conn)`

**Methods:**

| Method | SQL | DB Tables | Purpose |
|---|---|---|---|
| `getAlltv()` | `SELECT * FROM tv` | tv | Fetch all TV products |
| `getAlllaptop()` | `SELECT * FROM laptop` | laptop | Fetch all laptop products |
| `getAllmobile()` | `SELECT * FROM mobile` | mobile | Fetch all mobile products |
| `getAllwatch()` | `SELECT * FROM watch` | watch | Fetch all watch products |
| `checkaddtocartnull(cart)` | `SELECT * FROM cart WHERE Name=? AND bname=? AND cname=? AND pname=? AND pprice=? AND pimage=?` | cart | Check if authenticated customer has item in cart |
| `updateaddtocartnull(cart)` | `UPDATE cart SET pquantity=(pquantity+1) WHERE Name=? AND ...` | cart | Increment authenticated customer cart item quantity |
| `addtocartnull(cart)` | `INSERT INTO cart VALUES(?,?,?,?,?,?,?)` | cart | Add new item to authenticated customer cart |
| `getOrders(String customerName)` | `SELECT * FROM orders WHERE Customer_Name=?` | orders | Fetch orders for a customer |
| `getOrdersbydate(String date)` | `SELECT * FROM orders WHERE Date=?` | orders | Fetch order summary by date |
| `getOrderdetails(String date)` | `SELECT * FROM Order_details WHERE Date=?` | order_details | Fetch order line items by date |

**Dependencies:** `DBConnect`, `cart`, `tv`, `laptop`, `mobile`, `watch`, `orders`, `order_details` entities

---

### CP-025: DAO4

| Field | Value |
|---|---|
| **Component ID** | CP-025 |
| **Name** | DAO4 |
| **Type** | DAO |
| **Package** | `com.dao.DAO4` |

**Responsibility:** Data access for the full checkout sequence: cart validation, order insertion, order_details copy from cart, cart cleanup, and date/name update on order_details.

**Constructor:** `DAO4(Connection conn)`

**Methods:**

| Method | SQL | DB Tables | Purpose |
|---|---|---|---|
| `checkcart()` | `SELECT * FROM cart WHERE Name IS NULL` | cart | Check if guest cart has items |
| `checkcart2(String nm)` | `SELECT * FROM cart WHERE Name=?` | cart | Check if customer cart has items |
| `addOrders(orders)` | `INSERT INTO orders(Customer_Name,Customer_City,Date,Total_Price,Status) VALUES(?,?,?,?,?)` | orders | Create order record |
| `addOrder_details()` | `INSERT INTO order_details(...) SELECT * FROM cart WHERE Name IS NULL` | order_details, cart | Copy guest cart to order_details |
| `addOrder_details2(String st)` | `INSERT INTO order_details(...) SELECT * FROM cart WHERE Name=?` | order_details, cart | Copy customer cart to order_details |
| `deletecart()` | `DELETE FROM cart WHERE Name IS NULL` | cart | Clear guest cart after checkout |
| `deletecart2(String st)` | `DELETE FROM cart WHERE Name=?` | cart | Clear customer cart after checkout |
| `updateOrder_details(order_details)` | `UPDATE order_details SET Date=?, Name=? WHERE Date IS NULL` | order_details | Set date + name on guest order_details |
| `updateOrder_details2(order_details)` | `UPDATE order_details SET Date=? WHERE Date IS NULL` | order_details | Set date on customer order_details |

**Dependencies:** `DBConnect`, `cart`, `orders`, `order_details` entities

---

### CP-026: DAO5

| Field | Value |
|---|---|
| **Component ID** | CP-026 |
| **Name** | DAO5 |
| **Type** | DAO |
| **Package** | `com.dao.DAO5` |

**Responsibility:** Data access for admin table-management views (all cart, all orders, all order_details, all contactus) and contact-us CRUD.

**Constructor:** `DAO5(Connection conn)`

**Methods:**

| Method | SQL | DB Tables | Purpose |
|---|---|---|---|
| `getAllcart()` | `SELECT * FROM cart` | cart | Admin: fetch all cart rows |
| `getAllorders()` | `SELECT * FROM orders` | orders | Admin: fetch all orders |
| `getAllorder_details()` | `SELECT * FROM order_details` | order_details | Admin: fetch all order detail rows |
| `removeorder_details(order_details)` | `DELETE FROM order_details WHERE Date=? AND pimage=?` | order_details | Delete order detail row by date + pimage |
| `addContactus(contactus)` | `INSERT INTO Contactus(Name,Email_Id,Contact_No,Message) VALUES(?,?,?,?)` | Contactus | Insert contact us submission |
| `getcontactus()` | `SELECT * FROM Contactus` | Contactus | Admin: fetch all contact us entries |
| `removecont(contactus)` | `DELETE FROM Contactus WHERE id=?` | Contactus | Delete contact us entry by ID |

**Dependencies:** `DBConnect`, `cart`, `orders`, `order_details`, `contactus` entities

---

## Entity Classes

### CP-027: Product

| Field | Value |
|---|---|
| **Component ID** | CP-027 |
| **Type** | Entity |
| **Package** | `com.entity.Product` |
| **Fields** | `pid` (int), `pname` (String), `pprice` (int), `pquantity` (int), `pimage` (String), `bid` (int), `cid` (int) |
| **Used By** | DAO (addproduct), addproduct servlet |

---

### CP-028: brand

| Field | Value |
|---|---|
| **Component ID** | CP-028 |
| **Type** | Entity |
| **Package** | `com.entity.brand` |
| **Fields** | `bid` (int), `bname` (String) |
| **Used By** | DAO (getAllbrand) |

---

### CP-029: cart

| Field | Value |
|---|---|
| **Component ID** | CP-029 |
| **Type** | Entity |
| **Package** | `com.entity.cart` |
| **Fields** | `Name` (String), `bname` (String), `cname` (String), `pname` (String), `pprice` (int), `pquantity` (int), `pimage` (String) |
| **Used By** | DAO2, DAO3, DAO4, DAO5; addtocart, addtocartnull, addtocartnulla, removecart, removecarta, removecartnull, removecartnulla, removetable_cart, payprocess servlets |

---

### CP-030: category

| Field | Value |
|---|---|
| **Component ID** | CP-030 |
| **Type** | Entity |
| **Package** | `com.entity.category` |
| **Fields** | `cid` (int), `cname` (String) |
| **Used By** | DAO (getAllcategory) |

---

### CP-031: contactus

| Field | Value |
|---|---|
| **Component ID** | CP-031 |
| **Type** | Entity |
| **Package** | `com.entity.contactus` |
| **Fields** | `id` (int), `Name` (String), `Email_Id` (String), `Contact_No` (int), `Message` (String) |
| **Used By** | DAO5; addContactus, addContactusc, remove_contactus servlets |

---

### CP-032: customer

| Field | Value |
|---|---|
| **Component ID** | CP-032 |
| **Type** | Entity |
| **Package** | `com.entity.customer` |
| **Fields** | `Name` (String), `Password` (String), `Email_Id` (String), `Contact_No` (int) |
| **Used By** | DAO, DAO2; addcustomer, checkcustomer, deletecustomer servlets; managecustomers.jsp |

---

### CP-033: laptop

| Field | Value |
|---|---|
| **Component ID** | CP-033 |
| **Type** | Entity |
| **Package** | `com.entity.laptop` |
| **Fields** | `bname` (String), `cname` (String), `pname` (String), `pprice` (int), `pquantity` (int), `pimage` (String) |
| **Used By** | DAO3 (getAlllaptop); laptop.jsp, laptopa.jsp, laptopc.jsp |

---

### CP-034: mobile

| Field | Value |
|---|---|
| **Component ID** | CP-034 |
| **Type** | Entity |
| **Package** | `com.entity.mobile` |
| **Fields** | `bname` (String), `cname` (String), `pname` (String), `pprice` (int), `pquantity` (int), `pimage` (String) |
| **Used By** | DAO3 (getAllmobile); mobile.jsp, mobilea.jsp, mobilec.jsp |

---

### CP-035: order_details

| Field | Value |
|---|---|
| **Component ID** | CP-035 |
| **Type** | Entity |
| **Package** | `com.entity.order_details` |
| **Fields** | `Date` (String), `Name` (String), `bname` (String), `cname` (String), `pname` (String), `pprice` (int), `pquantity` (int), `pimage` (String) |
| **Used By** | DAO3, DAO4, DAO5; payprocess, removetable_order_details servlets; orderdetails.jsp |

---

### CP-036: orders

| Field | Value |
|---|---|
| **Component ID** | CP-036 |
| **Type** | Entity |
| **Package** | `com.entity.orders` |
| **Fields** | `Order_Id` (int), `Customer_Name` (String), `Customer_City` (String), `Date` (String), `Total_Price` (int), `Status` (String) |
| **Used By** | DAO2, DAO3, DAO4, DAO5; payprocess, removeorders, remove_orders servlets; orders.jsp, orderdetails.jsp |

---

### CP-037: tv

| Field | Value |
|---|---|
| **Component ID** | CP-037 |
| **Type** | Entity |
| **Package** | `com.entity.tv` |
| **Fields** | `bname` (String), `cname` (String), `pname` (String), `pprice` (int), `pquantity` (int), `pimage` (String) |
| **Used By** | DAO3 (getAlltv); tv.jsp, tva.jsp, tvc.jsp |

---

### CP-038: usermaster

| Field | Value |
|---|---|
| **Component ID** | CP-038 |
| **Type** | Entity |
| **Package** | `com.entity.usermaster` |
| **Fields** | `Name` (String), `Password` (String) |
| **Used By** | DAO2 (checkadmin); checkadmin servlet |

---

### CP-039: viewlist

| Field | Value |
|---|---|
| **Component ID** | CP-039 |
| **Type** | Entity |
| **Package** | `com.entity.viewlist` |
| **Fields** | `bname` (String), `cname` (String), `pname` (String), `pprice` (int), `pquantity` (int), `pimage` (String) |
| **Used By** | DAO2 (getAllviewlist, getSelecteditem); viewproduct.jsp, viewproducta.jsp, viewproductc.jsp, adminhome.jsp, customerhome.jsp, selecteditem.jsp, selecteditema.jsp, selecteditemc.jsp |

---

### CP-040: watch

| Field | Value |
|---|---|
| **Component ID** | CP-040 |
| **Type** | Entity |
| **Package** | `com.entity.watch` |
| **Fields** | `bname` (String), `cname` (String), `pname` (String), `pprice` (int), `pquantity` (int), `pimage` (String) |
| **Used By** | DAO3 (getAllwatch); watch.jsp, watcha.jsp, watchc.jsp |

---

## Key JSPs

### CP-041: adminhome.jsp

| Field | Value |
|---|---|
| **Component ID** | CP-041 |
| **Type** | JSP |
| **Path** | `src/main/webapp/adminhome.jsp` |

**Responsibility:** Admin dashboard landing page. Displays an auto-advancing image slideshow and a featured-products horizontal scroll panel.  
**DAO Calls:** `DAO2.getAllviewlist()` (SELECT * FROM viewlist)  
**Navbar:** `admin_navbar.jsp`  
**Related Flows:** FL-026  

---

### CP-042: customerhome.jsp

| Field | Value |
|---|---|
| **Component ID** | CP-042 |
| **Type** | JSP |
| **Path** | `src/main/webapp/customerhome.jsp` |

**Responsibility:** Customer landing page after login. Same layout as adminhome.jsp with customer navbar and product links pointing to `selecteditemc.jsp`.  
**DAO Calls:** `DAO2.getAllviewlist()` (SELECT * FROM viewlist)  
**Navbar:** `customer_navbar.jsp`  
**Related Flows:** FL-027  

---

### CP-043: viewproduct.jsp / viewproducta.jsp / viewproductc.jsp

| Field | Value |
|---|---|
| **Component ID** | CP-043 |
| **Type** | JSP (tripled) |
| **Path** | `src/main/webapp/viewproduct*.jsp` |

**Responsibility:** Full product catalog listing. Queries `viewlist` and renders a responsive grid of product thumbnails with links to detail pages. Three variants: guest (navbar.jsp), admin (admin_navbar.jsp), customer (customer_navbar.jsp).  
**DAO Calls:** `DAO2.getAllviewlist()`  
**Related Flows:** FL-005  

---

### CP-044: mobile.jsp / mobilea.jsp / mobilec.jsp (and tv, laptop, watch variants)

| Field | Value |
|---|---|
| **Component ID** | CP-044 |
| **Type** | JSP (tripled × 4 categories = 12 files) |
| **Path** | `src/main/webapp/{mobile,tv,laptop,watch}{,a,c}.jsp` |

**Responsibility:** Category-filtered product listings. Each category JSP queries the corresponding category view and renders product thumbnails.  
**DAO Calls:** `DAO3.getAllmobile()` / `getAlltv()` / `getAlllaptop()` / `getAllwatch()`  
**Related Flows:** FL-006  

---

### CP-045: selecteditem.jsp / selecteditema.jsp / selecteditemc.jsp

| Field | Value |
|---|---|
| **Component ID** | CP-045 |
| **Type** | JSP (tripled) |
| **Path** | `src/main/webapp/selecteditem*.jsp` |

**Responsibility:** Product detail page. Fetches a single product by image filename from `viewlist`. Shows product attributes and an "Add to Cart" link.  
**DAO Calls:** `DAO2.getSelecteditem(pimage)`  
**Add-to-cart targets:** `/addtocartnull` (guest), `/addtocart` (customer), `/addtocartnulla` (admin view)  
**Related Flows:** FL-007, FL-008, FL-009  

---

### CP-046: cart.jsp / cartnull.jsp / carta.jsp / cartnulla.jsp

| Field | Value |
|---|---|
| **Component ID** | CP-046 |
| **Type** | JSP (4 variants) |
| **Path** | `src/main/webapp/cart*.jsp` / `src/main/webapp/carta.jsp` |

**Responsibility:** Shopping cart display. Shows cart items with totals and remove links. Links to ShippingAddress.jsp for checkout.  
**DAO Calls:** `DAO2.getcart(email)` (cart.jsp), `DAO2.getSelectedcart()` (cartnull.jsp), `DAO2.getcart(custname)` (carta.jsp), `DAO2.getSelectedcart()` (cartnulla.jsp)  
**Related Flows:** FL-007, FL-008, FL-010, FL-011  

---

### CP-047: orders.jsp

| Field | Value |
|---|---|
| **Component ID** | CP-047 |
| **Type** | JSP |
| **Path** | `src/main/webapp/orders.jsp` |

**Responsibility:** Displays all orders for the logged-in customer (reads `cname` cookie for customer email). Provides links to order details and order removal.  
**DAO Calls:** `DAO3.getOrders(customerEmail)`  
**Related Flows:** FL-024  

---

### CP-048: orderdetails.jsp

| Field | Value |
|---|---|
| **Component ID** | CP-048 |
| **Type** | JSP |
| **Path** | `src/main/webapp/orderdetails.jsp` |

**Responsibility:** Displays line-item details for a specific order identified by Date query parameter. Shows order summary and all purchased items.  
**DAO Calls:** `DAO3.getOrdersbydate(date)`, `DAO3.getOrderdetails(date)`  
**Related Flows:** FL-025  

---

### CP-049: ShippingAddress.jsp

| Field | Value |
|---|---|
| **Component ID** | CP-049 |
| **Type** | JSP |
| **Path** | `src/main/webapp/ShippingAddress.jsp` |

**Responsibility:** Shipping address data entry form with payment method selection (Cash on Delivery / Online Payment). Reads customer name from `cname` cookie. Posts to `ShippingAddress2` servlet.  
**DAO Calls:** None  
**Related Flows:** FL-014  

---

### CP-050: managecustomers.jsp

| Field | Value |
|---|---|
| **Component ID** | CP-050 |
| **Type** | JSP |
| **Path** | `src/main/webapp/managecustomers.jsp` |

**Responsibility:** Admin page listing all customers with options to view customer's cart (`carta.jsp`) or delete the customer (`/deletecustomer`).  
**DAO Calls:** `DAO.getAllCustomer()`  
**Related Flows:** FL-020  

---

### CP-051: managetables.jsp

| Field | Value |
|---|---|
| **Component ID** | CP-051 |
| **Type** | JSP |
| **Path** | `src/main/webapp/managetables.jsp` |

**Responsibility:** Admin navigation hub with links to four raw table views: Table Cart, Table Orders, Table Order Details, Table Contact Us.  
**DAO Calls:** None  
**Related Flows:** FL-022, FL-023  

---

### CP-052: table_cart.jsp / table_orders.jsp / table_order_details.jsp / table_contactus.jsp

| Field | Value |
|---|---|
| **Component ID** | CP-052 |
| **Type** | JSP (4 files) |
| **Path** | `src/main/webapp/table_*.jsp` |

**Responsibility:** Raw admin table views showing all rows in `cart`, `orders`, `order_details`, and `Contactus` respectively, with per-row delete actions.  
**DAO Calls:** `DAO5.getAllcart()`, `DAO5.getAllorders()`, `DAO5.getAllorder_details()`, `DAO5.getcontactus()`  
**Related Flows:** FL-021, FL-022, FL-023  

---

### CP-053: addproduct.jsp

| Field | Value |
|---|---|
| **Component ID** | CP-053 |
| **Type** | JSP |
| **Path** | `src/main/webapp/addproduct.jsp` |

**Responsibility:** Admin form for creating a new product. Accepts pname, pprice, pquantity, brand name, category name, and product image file. Posts to `/addproduct`.  
**DAO Calls:** `DAO.getAllbrand()`, `DAO.getAllcategory()` (for dropdown population)  
**Related Flows:** FL-004  

---

### CP-054: Navbar JSPs

| Field | Value |
|---|---|
| **Component ID** | CP-054 |
| **Type** | JSP (include fragments) |
| **Path** | `navbar.jsp`, `customer_navbar.jsp`, `admin_navbar.jsp` |

**Responsibility:** Navigation bars included via `<%@ include %>` in every page variant. Guest navbar links to login, registration, products, categories. Customer navbar adds cart and orders links. Admin navbar adds product management, customer management, table management.  
**DAO Calls:** None  
**Cookie Checks:** navbar.jsp checks `cname` cookie; customer_navbar.jsp reads `cname`; admin_navbar.jsp reads `tname`  

---

## Infrastructure / Utility

### CP-055: DBConnect

| Field | Value |
|---|---|
| **Component ID** | CP-055 |
| **Name** | DBConnect |
| **Type** | Config / Infrastructure |
| **Package** | `com.conn.DBConnect` |

**Responsibility:** Provides a `static` SQLite JDBC connection via `DBConnect.getConn()`. Reads the DB path from `SQLITE_DB_PATH` environment variable; falls back to `"mydatabase.db"`.

**Key Behavior:**
- Loads both MySQL driver (`com.mysql.cj.jdbc.Driver`) and SQLite driver (`org.sqlite.JDBC`) — MySQL driver is legacy dead code; only SQLite is connected.
- Returns a static `Connection` field — NOT thread-safe; shared across all concurrent requests.
- A new connection is opened on every `getConn()` call (overwrites the static field).

**Used By:** All DAO classes and several JSPs that call DAOs directly.  
**Related Flows:** All flows that access the database.  

---

### CP-056: MyUtilities

| Field | Value |
|---|---|
| **Component ID** | CP-056 |
| **Name** | MyUtilities |
| **Type** | Utility |
| **Package** | `com.utility.MyUtilities` |

**Responsibility:** File upload validation and persistence. Single method `UploadFile()` validates extension and file size, then writes the file to a target directory on the server filesystem.

**Method:**

| Method | Signature | Behavior |
|---|---|---|
| `UploadFile(FileItem, String destinationPath, ArrayList<String> ext)` | Returns filename or `""` | Validates extension is in allowed list; validates size ≤ 10 MB; writes to `destinationPath + originalFilename`. Returns empty string if validation fails. |

**Security Notes:**
- Uses original filename as-is (no sanitization) — path traversal risk if filename contains `../`.
- No uniqueness guarantee — uploaded files overwrite existing files with the same name.
- Returns `""` (empty string) on invalid upload, not the string `"Problem with upload"` — the DAO check `pimage.equals("Problem with upload")` will evaluate to `false` for the empty return, which incorrectly proceeds with empty pimage.

**Used By:** `DAO.addproduct()`  
**Related Flows:** FL-004  

---

## Cross-Domain Table Usage Matrix

The following matrix shows which Servlets and DAOs access which database tables (R=Read, W=Write, D=Delete).

| Component | `product` | `brand` | `category` | `customer` | `usermaster` | `cart` | `orders` | `order_details` | `Contactus` | `viewlist` | `mobile` | `tv` | `laptop` | `watch` |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| **DAO.addproduct** | W | | | | | | | | | | | | | |
| **DAO.getAllbrand** | | R | | | | | | | | | | | | |
| **DAO.getAllcategory** | | | R | | | | | | | | | | | |
| **DAO.getAllCustomer** | | | | R | | | | | | | | | | |
| **DAO.deleteCustomer** | | | | D | | | | | | | | | | |
| **DAO.getCustomer** | | | | R | | | | | | | | | | |
| **DAO2.checkcust** | | | | R | | | | | | | | | | |
| **DAO2.checkadmin** | | | | | R | | | | | | | | | |
| **DAO2.checkcust2** | | | | R | | | | | | | | | | |
| **DAO2.addcustomer** | | | | W | | | | | | | | | | |
| **DAO2.getAllviewlist** | | | | | | | | | | R | | | | |
| **DAO2.getSelecteditem** | | | | | | | | | | R | | | | |
| **DAO2.checkaddtocartnull** | | | | | | R | | | | | | | | |
| **DAO2.updateaddtocartnull** | | | | | | W | | | | | | | | |
| **DAO2.addtocartnull** | | | | | | W | | | | | | | | |
| **DAO2.getSelectedcart** | | | | | | R | | | | | | | | |
| **DAO2.getcart** | | | | | | R | | | | | | | | |
| **DAO2.removecartnull** | | | | | | D | | | | | | | | |
| **DAO2.removecart** | | | | | | D | | | | | | | | |
| **DAO2.removeorders** | | | | | | | D | | | | | | | |
| **DAO3.getAlltv** | | | | | | | | | | | | R | | |
| **DAO3.getAlllaptop** | | | | | | | | | | | | | R | |
| **DAO3.getAllmobile** | | | | | | | | | | | R | | | |
| **DAO3.getAllwatch** | | | | | | | | | | | | | | R |
| **DAO3.checkaddtocartnull** | | | | | | R | | | | | | | | |
| **DAO3.updateaddtocartnull** | | | | | | W | | | | | | | | |
| **DAO3.addtocartnull** | | | | | | W | | | | | | | | |
| **DAO3.getOrders** | | | | | | | R | | | | | | | |
| **DAO3.getOrdersbydate** | | | | | | | R | | | | | | | |
| **DAO3.getOrderdetails** | | | | | | | | R | | | | | | |
| **DAO4.checkcart** | | | | | | R | | | | | | | | |
| **DAO4.checkcart2** | | | | | | R | | | | | | | | |
| **DAO4.addOrders** | | | | | | | W | | | | | | | |
| **DAO4.addOrder_details** | | | | | | R | | W | | | | | | |
| **DAO4.addOrder_details2** | | | | | | R | | W | | | | | | |
| **DAO4.deletecart** | | | | | | D | | | | | | | | |
| **DAO4.deletecart2** | | | | | | D | | | | | | | | |
| **DAO4.updateOrder_details** | | | | | | | | W | | | | | | |
| **DAO4.updateOrder_details2** | | | | | | | | W | | | | | | |
| **DAO5.getAllcart** | | | | | | R | | | | | | | | |
| **DAO5.getAllorders** | | | | | | | R | | | | | | | |
| **DAO5.getAllorder_details** | | | | | | | | R | | | | | | |
| **DAO5.removeorder_details** | | | | | | | | D | | | | | | |
| **DAO5.addContactus** | | | | | | | | | W | | | | | |
| **DAO5.getcontactus** | | | | | | | | | R | | | | | |
| **DAO5.removecont** | | | | | | | | | D | | | | | |
| **addproduct servlet** | W | | | | | | | | | | | | | |
| **addcustomer servlet** | | | | R+W | | | | | | | | | | |
| **checkcustomer servlet** | | | | R | | | | | | | | | | |
| **checkadmin servlet** | | | | | R | | | | | | | | | |
| **deletecustomer servlet** | | | | D | | | | | | | | | | |
| **addtocart servlet** | | | | | | R+W | | | | | | | | |
| **addtocartnull servlet** | | | | | | R+W | | | | | | | | |
| **removecart servlet** | | | | | | D | | | | | | | | |
| **removecartnull servlet** | | | | | | D | | | | | | | | |
| **payprocess servlet** | | | | | | R+D | W | W | | | | | | |
| **removeorders servlet** | | | | | | | D | | | | | | | |
| **remove_orders servlet** | | | | | | | D | | | | | | | |
| **addContactus servlet** | | | | | | | | | W | | | | | |
| **addContactusc servlet** | | | | | | | | | W | | | | | |
| **remove_contactus servlet** | | | | | | | | | D | | | | | |
| **removetable_cart servlet** | | | | | | D | | | | | | | | |
| **removetable_order_details servlet** | | | | | | | | D | | | | | | |
