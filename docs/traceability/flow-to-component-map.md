# Flow-to-Component Map — EcommerceApp

**Document ID:** FCM-001  
**Version:** 1.0  
**Phase:** Coordination  
**Source:** `docs/discovered-flows.md`, `docs/discovered-components.md`  

---

## Overview

This map traces each of the 27 discovered execution flows to the implementing servlets, DAO classes, entity classes, and database tables. Use this document to locate implementation components for any business or functional requirement.

---

## Flow-to-Component Map

| Flow ID | Flow Name | Trigger | Servlet | DAO | Entity | DB Tables | Technical Flow |
|---|---|---|---|---|---|---|---|
| [FL-001](#fl-001-customer-registration) | Customer Registration | `POST /addcustomer` | `addcustomer` | `DAO2` | `customer` | `customer` | FF-001 |
| [FL-002](#fl-002-customer-login) | Customer Login | `POST /checkcustomer` | `checkcustomer` | `DAO2` | `customer` | `customer` | FF-001 |
| [FL-003](#fl-003-admin-login) | Admin Login | `POST /checkadmin` | `checkadmin` | `DAO2` | `usermaster` | `usermaster` | FF-004 |
| [FL-004](#fl-004-add-product) | Add Product | `POST /addproduct` | `addproduct` | `DAO` | `Product` | `product` | FF-004 |
| [FL-005](#fl-005-view-product-catalog) | View Product Catalog | JSP direct | — (JSP scriptlet) | `DAO` | `viewlist` | `viewlist` | FF-002 |
| [FL-006](#fl-006-browse-by-category) | Browse by Category | JSP direct | — (JSP scriptlet) | `DAO` | `mobile`/`tv`/`laptop`/`watch` | `mobile`, `tv`, `laptop`, `watch` | FF-002 |
| [FL-007](#fl-007-add-to-cart-authenticated-customer) | Add to Cart (Customer) | `GET /addtocart` | `addtocart` | `DAO3` | `cart` | `cart` | FF-002 |
| [FL-008](#fl-008-add-to-cart-guest) | Add to Cart (Guest) | `GET /addtocartnull` | `addtocartnull` | `DAO3` | `cart` | `cart` | FF-002 |
| [FL-009](#fl-009-add-to-cart-admin-view) | Add to Cart (Admin View) | `GET /addtocartnulla` | `addtocartnulla` | `DAO3` | `cart` | `cart` | FF-002 |
| [FL-010](#fl-010-remove-from-cart-customer) | Remove from Cart (Customer) | `GET /removecart` | `removecart` | `DAO3` | `cart` | `cart` | FF-002 |
| [FL-011](#fl-011-remove-from-cart-guest) | Remove from Cart (Guest) | `GET /removecartnull` | `removecartnull` | `DAO3` | `cart` | `cart` | FF-002 |
| [FL-012](#fl-012-remove-from-cart-admin-customer-cart) | Remove from Cart (Admin — Customer) | `GET /removecarta` | `removecarta` | `DAO3` | `cart` | `cart` | FF-004 |
| [FL-013](#fl-013-remove-from-cart-admin-guest-cart) | Remove from Cart (Admin — Guest) | `GET /removecartnulla` | `removecartnulla` | `DAO3` | `cart` | `cart` | FF-004 |
| [FL-014](#fl-014-shipping-address-selection) | Shipping Address Selection | `POST /ShippingAddress2` | `ShippingAddress2` | — | — | None | FF-003 |
| [FL-015](#fl-015-payment-processing) | Payment Processing | `POST /payprocess` | `payprocess` | `DAO4` | `orders`, `orderdetails` | `cart`, `orders`, `order_details` | FF-003 |
| [FL-016](#fl-016-remove-orders-customer) | Remove Orders (Customer) | `GET /removeorders` | `removeorders` | `DAO4` | `orders` | `orders` | FF-003 |
| [FL-017](#fl-017-remove-orders-admin) | Remove Orders (Admin) | `GET /remove_orders` | `remove_orders` | `DAO4` | `orders` | `orders` | FF-003 |
| [FL-018](#fl-018-contact-us-submission-guest) | Contact Us Submission (Guest) | `POST /addContactus` | `addContactus` | `DAO5` | `contactus` | `Contactus` | FF-004 |
| [FL-019](#fl-019-contact-us-submission-customer) | Contact Us Submission (Customer) | `POST /addContactusc` | `addContactusc` | `DAO5` | `contactus` | `Contactus` | FF-004 |
| [FL-020](#fl-020-admin-delete-customer) | Admin Delete Customer | `GET /deletecustomer` | `deletecustomer` | `DAO2` | `customer` | `customer` | FF-004 |
| [FL-021](#fl-021-admin-remove-contact-us-entry) | Admin Remove Contact Us Entry | `GET /remove_contactus` | `remove_contactus` | `DAO5` | `contactus` | `Contactus` | FF-004 |
| [FL-022](#fl-022-admin-remove-cart-row) | Admin Remove Cart Row | `GET /removetable_cart` | `removetable_cart` | `DAO3` | `cart` | `cart` | FF-004 |
| [FL-023](#fl-023-admin-remove-order-detail-row) | Admin Remove Order Detail Row | `GET /removetable_order_details` | `removetable_order_details` | `DAO4` | `orderdetails` | `order_details` | FF-004 |
| [FL-024](#fl-024-view-orders-customer) | View Orders (Customer) | JSP direct | — (JSP scriptlet) | `DAO4` | `orders` | `orders` | FF-003 |
| [FL-025](#fl-025-view-order-details) | View Order Details | JSP direct | — (JSP scriptlet) | `DAO4` | `orderdetails` | `order_details` | FF-003 |
| [FL-026](#fl-026-admin-home-dashboard) | Admin Home Dashboard | JSP direct | — (JSP scriptlet) | `DAO` | `mobile`, `tv`, etc. | `mobile`, `tv`, `laptop`, `watch` | FF-004 |
| [FL-027](#fl-027-customer-home) | Customer Home | JSP direct | — (JSP scriptlet) | `DAO` | `viewlist` | `viewlist` | FF-002 |

---

## Flow Detail Cards

### FL-001: Customer Registration
**Trigger:** `POST /addcustomer` | **Actor:** Guest  
**Servlet:** `com.servlet.addcustomer` (CP-004)  
**DAO:** `DAO2.checkcust2()` → `DAO2.addcustomer()`  
**Entity:** `com.entity.customer`  
**DB:** `customer` (SELECT duplicate check, INSERT)  
**Success:** Flash cookie `creg` (maxAge=10), redirect to `customerlogin.jsp`  
**Failure:** Redirect to `fail.jsp`

---

### FL-002: Customer Login
**Trigger:** `POST /checkcustomer` | **Actor:** Guest/Customer  
**Servlet:** `com.servlet.checkcustomer` (CP-012)  
**DAO:** `DAO2.checkcustomer()`  
**Entity:** `com.entity.customer`  
**DB:** `customer` (SELECT credential check)  
**Success:** Cookie `cname`=email (maxAge=9999), redirect to `index.jsp` or checkout  
**Failure:** Redirect to `fail.jsp`

---

### FL-003: Admin Login
**Trigger:** `POST /checkadmin` | **Actor:** Admin  
**Servlet:** `com.servlet.checkadmin` (CP-011)  
**DAO:** `DAO2.checkadmin()`  
**Entity:** `com.entity.usermaster`  
**DB:** `usermaster` (SELECT credential check)  
**Success:** Cookie `tname`=username (maxAge=9999), redirect to `adminhome.jsp`  
**Failure:** Redirect to `faila.jsp`

---

### FL-004: Add Product
**Trigger:** `POST /addproduct` | **Actor:** Admin (authenticated)  
**Servlet:** `com.servlet.addproduct` (CP-005) — `@MultipartConfig`  
**DAO:** `DAO.addproduct()` (handles file upload + DB insert)  
**Utility:** `MyUtilities.UploadFile()`  
**Entity:** `com.entity.Product`  
**DB:** `product` (INSERT)  
**Success:** Redirect to `addproductsuccess.jsp`  
**Failure:** Redirect to `fail.jsp`

---

### FL-005: View Product Catalog
**Trigger:** JSP page load (no dedicated servlet)  
**Actor:** Guest, Customer, Admin  
**JSPs:** `index.jsp`, `indexc.jsp`, `indexa.jsp` (tripling pattern)  
**DAO:** `DAO.getAllProducts()` / `DAO.getAllViewList()`  
**Entity:** `com.entity.viewlist`  
**DB:** `viewlist` (SELECT all rows)

---

### FL-006: Browse by Category
**Trigger:** JSP page load for category-specific page  
**Actor:** Guest, Customer, Admin  
**JSPs:** `mobile.jsp`/`mobilea.jsp`/`mobilec.jsp`, `tv.jsp`/`tva.jsp`/`tvc.jsp`, etc. (tripling pattern)  
**DAO:** Category-specific DAO methods  
**Entities:** `com.entity.mobile`, `com.entity.tv`, `com.entity.laptop`, `com.entity.watch`  
**DB:** `mobile`, `tv`, `laptop`, `watch` (SELECT all rows)

---

### FL-007: Add to Cart (Authenticated Customer)
**Trigger:** `GET /addtocart` | **Actor:** Customer  
**Servlet:** `com.servlet.addtocart` (CP-006)  
**DAO:** `DAO3.checkcart()` → `DAO3.updatecart()` or `DAO3.addtocart()`  
**Entity:** `com.entity.cart`  
**DB:** `cart` (SELECT check, UPDATE increment or INSERT)  
**Success:** Flash cookie `cart` (maxAge=10), redirect back  

---

### FL-008: Add to Cart (Guest)
**Trigger:** `GET /addtocartnull` | **Actor:** Guest  
**Servlet:** `com.servlet.addtocartnull` (CP-007)  
**DAO:** `DAO3` — same logic as FL-007, CusName="null"  
**DB:** `cart` (SELECT check, UPDATE or INSERT with null name)

---

### FL-009: Add to Cart (Admin View)
**Trigger:** `GET /addtocartnulla` | **Actor:** Admin viewing cart  
**Servlet:** `com.servlet.addtocartnulla` (CP-008)  
**DAO:** `DAO3` — inserts with NULL CusName  
**DB:** `cart`

---

### FL-010: Remove from Cart (Customer)
**Trigger:** `GET /removecart` | **Actor:** Customer  
**Servlet:** `com.servlet.removecart` (CP-017)  
**DAO:** `DAO3.removecart()`  
**DB:** `cart` (DELETE by Name + pimage)

---

### FL-011: Remove from Cart (Guest)
**Trigger:** `GET /removecartnull` | **Actor:** Guest  
**Servlet:** `com.servlet.removecartnull` (CP-018)  
**DAO:** `DAO3.removecartnull()`  
**DB:** `cart` (DELETE where Name IS NULL + pimage)

---

### FL-012: Remove from Cart (Admin — Customer Cart)
**Trigger:** `GET /removecarta` | **Actor:** Admin  
**Servlet:** `com.servlet.removecarta`  
**DAO:** `DAO3.removecart()`  
**DB:** `cart` (DELETE by Name + pimage)

---

### FL-013: Remove from Cart (Admin — Guest Cart)
**Trigger:** `GET /removecartnulla` | **Actor:** Admin  
**Servlet:** `com.servlet.removecartnulla`  
**DAO:** `DAO3.removecartnull()`  
**DB:** `cart` (DELETE where Name IS NULL + pimage)

---

### FL-014: Shipping Address Selection
**Trigger:** `POST /ShippingAddress2` | **Actor:** Customer/Guest  
**Servlet:** `com.servlet.ShippingAddress2` (CP-001) — routing only, no DAO  
**Logic:** Routes to `cashpayment.jsp` or `onlinepayment.jsp` based on `paymethod` parameter  
**DB:** None

---

### FL-015: Payment Processing
**Trigger:** `POST /payprocess` | **Actor:** Customer  
**Servlet:** `com.servlet.payprocess` (CP-016) — `@MultipartConfig`  
**DAO:** `DAO4`: insert order → select cart → insert order_details → delete cart  
**Entities:** `com.entity.orders`, `com.entity.orderdetails`  
**DB:** `cart` (SELECT + DELETE), `orders` (INSERT), `order_details` (INSERT)  
**Success:** Redirect to `ordersuccess.jsp`  
**Failure:** Redirect to `fail.jsp`

---

### FL-016: Remove Orders (Customer)
**Trigger:** `GET /removeorders` | **Actor:** Customer  
**Servlet:** `com.servlet.removeorders` (CP-019)  
**DAO:** `DAO4.removeorders()`  
**DB:** `orders` (DELETE by Order_Id)

---

### FL-017: Remove Orders (Admin)
**Trigger:** `GET /remove_orders` | **Actor:** Admin  
**Servlet:** `com.servlet.remove_orders` (CP-020)  
**DAO:** `DAO4.removeorders()`  
**DB:** `orders` (DELETE by Order_Id)

---

### FL-018: Contact Us Submission (Guest)
**Trigger:** `POST /addContactus` | **Actor:** Guest  
**Servlet:** `com.servlet.addContactus` (CP-002)  
**DAO:** `DAO5.addcontactus()`  
**Entity:** `com.entity.contactus`  
**DB:** `Contactus` (INSERT)

---

### FL-019: Contact Us Submission (Customer)
**Trigger:** `POST /addContactusc` | **Actor:** Customer  
**Servlet:** `com.servlet.addContactusc` (CP-003)  
**DAO:** `DAO5.addcontactus()`  
**Entity:** `com.entity.contactus`  
**DB:** `Contactus` (INSERT)

---

### FL-020: Admin Delete Customer
**Trigger:** `GET /deletecustomer` | **Actor:** Admin  
**Servlet:** `com.servlet.deletecustomer` (CP-013)  
**DAO:** `DAO2.deletecustomer()`  
**DB:** `customer` (DELETE by Name + Email_Id)  
**Note:** Does not cascade to cart or order records.

---

### FL-021: Admin Remove Contact Us Entry
**Trigger:** `GET /remove_contactus` | **Actor:** Admin  
**Servlet:** `com.servlet.remove_contactus` (CP-021)  
**DAO:** `DAO5.removecontactus()`  
**DB:** `Contactus` (DELETE)

---

### FL-022: Admin Remove Cart Row
**Trigger:** `GET /removetable_cart` | **Actor:** Admin  
**Servlet:** `com.servlet.removetable_cart` (CP-022)  
**DAO:** `DAO3` cart removal  
**DB:** `cart` (DELETE specific row)

---

### FL-023: Admin Remove Order Detail Row
**Trigger:** `GET /removetable_order_details` | **Actor:** Admin  
**Servlet:** `com.servlet.removetable_order_details` (CP-023)  
**DAO:** `DAO4` order detail removal  
**DB:** `order_details` (DELETE specific row)

---

### FL-024: View Orders (Customer)
**Trigger:** JSP direct (`myorders.jsp` / `myordersc.jsp`)  
**Actor:** Customer  
**DAO:** `DAO4.getorders()` (SELECT by CusName)  
**DB:** `orders` (SELECT)

---

### FL-025: View Order Details
**Trigger:** JSP direct (`orderdetails.jsp` / `orderdetailsc.jsp`)  
**Actor:** Customer  
**DAO:** `DAO4.getorderdetails()` (SELECT by oid)  
**DB:** `order_details` (SELECT)

---

### FL-026: Admin Home Dashboard
**Trigger:** JSP direct (`adminhome.jsp`)  
**Actor:** Admin  
**DAO:** `DAO` product retrieval per category  
**DB:** `mobile`, `tv`, `laptop`, `watch` (SELECT)

---

### FL-027: Customer Home
**Trigger:** JSP direct (`index.jsp` / `indexc.jsp`)  
**Actor:** Customer, Guest  
**DAO:** `DAO.getAllViewList()`  
**DB:** `viewlist` (SELECT all)

---

## DAO Responsibility Map

| DAO Class | Responsible For | Flows |
|---|---|---|
| `DAO` | Product CRUD, category browsing, viewlist | FL-004, FL-005, FL-006, FL-026, FL-027 |
| `DAO2` | Customer auth, admin auth, customer management | FL-001, FL-002, FL-003, FL-020 |
| `DAO3` | Cart operations (all actor types) | FL-007, FL-008, FL-009, FL-010, FL-011, FL-012, FL-013, FL-022 |
| `DAO4` | Order lifecycle (create, view, cancel) | FL-015, FL-016, FL-017, FL-023, FL-024, FL-025 |
| `DAO5` | Contact Us (insert, delete) | FL-018, FL-019, FL-021 |

---

*Cross-reference with [Requirement Traceability Matrix](requirement-matrix.md) and [ID Registry](id-registry.md)*
