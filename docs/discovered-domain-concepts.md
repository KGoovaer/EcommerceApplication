# Discovered Domain Concepts

**Application:** EcommerceApp — Java EE (Servlet 3.0 + JSP + SQLite + Maven WAR)  
**Analysis Date:** Auto-generated from source code discovery  
**Source:** `EcommerceApp/src/main/java/com/entity/`

---

## Table of Contents

- [DC-001: Product](#dc-001-product)
- [DC-002: Brand](#dc-002-brand)
- [DC-003: Category](#dc-003-category)
- [DC-004: Customer](#dc-004-customer)
- [DC-005: UserMaster (Admin)](#dc-005-usermaster-admin)
- [DC-006: Cart](#dc-006-cart)
- [DC-007: Orders](#dc-007-orders)
- [DC-008: OrderDetails](#dc-008-orderdetails)
- [DC-009: ContactUs](#dc-009-contactus)
- [DC-010: ViewList](#dc-010-viewlist)
- [DC-011: Mobile](#dc-011-mobile)
- [DC-012: TV](#dc-012-tv)
- [DC-013: Laptop](#dc-013-laptop)
- [DC-014: Watch](#dc-014-watch)

---

## DC-001: Product

| Field | Value |
|---|---|
| **Concept ID** | DC-001 |
| **Name** | Product |
| **Entity Class** | `com.entity.Product` |
| **Related DB Table** | `product` |
| **Related Flows** | FL-004 (Add Product), FL-005 (View Catalog), FL-006 (Browse by Category) |

### Description
The central sellable item in the e-commerce system. Products have a price, a quantity in stock, an image file reference, and are classified by both a brand and a category. Admin users create products; customers and guests browse and purchase them.

### Attributes

| Field | Type | Description |
|---|---|---|
| `pid` | `int` | Auto-generated primary key for the product |
| `pname` | `String` | Product display name |
| `pprice` | `int` | Price in Indian Rupees (₹) |
| `pquantity` | `int` | Available stock quantity |
| `pimage` | `String` | Image filename (stored in `images/` directory on server) |
| `bid` | `int` | Foreign key → `brand.bid` |
| `cid` | `int` | Foreign key → `category.cid` |

### Business Rules
1. A product must belong to exactly one brand and one category.
2. Brand is mapped from name to ID with a fixed hardcoded list: samsung→1, sony→2, lenovo→3, acer→4, onida→5.
3. Category is mapped from name to ID: laptop→1, tv→2, mobile→3, watch→4.
4. Image file must be `.jpg`, `.bmp`, `.jpeg`, `.png`, or `.webp` and ≤ 10 MB.
5. The image filename (as uploaded) serves as a surrogate key in several queries (e.g., `viewlist.Pimage`, `cart.pimage`).
6. Only admin users may add products; no update or delete endpoint exists for products.

---

## DC-002: Brand

| Field | Value |
|---|---|
| **Concept ID** | DC-002 |
| **Name** | Brand |
| **Entity Class** | `com.entity.brand` |
| **Related DB Table** | `brand` |
| **Related Flows** | FL-004 (Add Product — brand selection), FL-005 / FL-006 (catalog display via viewlist) |

### Description
Represents the manufacturer or label associated with a product. Used as a display attribute in product listings and for product filtering. Brands are pre-seeded in the database; there is no admin UI to manage brands dynamically.

### Attributes

| Field | Type | Description |
|---|---|---|
| `bid` | `int` | Primary key (1–5, hardcoded mapping in DAO.addproduct) |
| `bname` | `String` | Brand name (samsung, sony, lenovo, acer, onida) |

### Business Rules
1. Brand–to–ID mapping is hardcoded in `DAO.addproduct()`: samsung=1, sony=2, lenovo=3, acer=4, onida=5.
2. Adding a product with a brand name not in this list results in `bid = 0` (invalid foreign key silently accepted).
3. `DAO.getAllbrand()` retrieves brands but is not currently wired to any active servlet/JSP flow.

---

## DC-003: Category

| Field | Value |
|---|---|
| **Concept ID** | DC-003 |
| **Name** | Category |
| **Entity Class** | `com.entity.category` |
| **Related DB Table** | `category` |
| **Related Flows** | FL-004 (Add Product — category selection), FL-006 (Browse by Category) |

### Description
Classifies products into one of four product types: laptop, tv, mobile, watch. Each category corresponds to a dedicated DB view (or table) that surfaces only products of that type. Navigation menus link directly to category-specific JSPs.

### Attributes

| Field | Type | Description |
|---|---|---|
| `cid` | `int` | Primary key (1=laptop, 2=tv, 3=mobile, 4=watch) |
| `cname` | `String` | Category name |

### Business Rules
1. Category–to–ID mapping is hardcoded: laptop=1, tv=2, mobile=3, watch=4.
2. Each category has its own DB read view (`mobile`, `tv`, `laptop`, `watch`) and a corresponding set of three JSPs (guest/admin/customer).
3. `DAO.getAllcategory()` retrieves categories but is not currently wired to any active UI flow.

---

## DC-004: Customer

| Field | Value |
|---|---|
| **Concept ID** | DC-004 |
| **Name** | Customer |
| **Entity Class** | `com.entity.customer` |
| **Related DB Table** | `customer` |
| **Related Flows** | FL-001 (Registration), FL-002 (Login), FL-007 (Add to Cart), FL-010 (Remove Cart), FL-015 (Payment), FL-016 (Remove Order), FL-019 (Contact Us), FL-020 (Admin Delete Customer), FL-024 (View Orders) |

### Description
Registered end-user who can browse products, manage a personal shopping cart, place orders, view order history, and submit contact inquiries. Identity is maintained via a persistent browser cookie (`cname` = customer email) with no server-side session.

### Attributes

| Field | Type | Description |
|---|---|---|
| `Name` | `String` | Display name and cart identifier |
| `Password` | `String` | Plaintext password (no hashing — known security issue) |
| `Email_Id` | `String` | Email address; used as login credential and as `cname` cookie value |
| `Contact_No` | `int` | Phone number |

### Business Rules
1. Name and Email_Id must be unique; `checkcust2()` blocks registration if either already exists.
2. Login requires matching both Email_Id and Password (plaintext comparison).
3. `cname` cookie (maxAge=9999) stores Email_Id as session token; there is no logout mechanism.
4. Customer's email (`cname`) is the `Name` field in `cart` and `Customer_Name` in `orders`.
5. Deleting a customer does not cascade to their cart or order records.

---

## DC-005: UserMaster (Admin)

| Field | Value |
|---|---|
| **Concept ID** | DC-005 |
| **Name** | UserMaster (Admin) |
| **Entity Class** | `com.entity.usermaster` |
| **Related DB Table** | `usermaster` |
| **Related Flows** | FL-003 (Admin Login), FL-004 (Add Product), FL-020 (Delete Customer), FL-021 (Remove Contact Us), FL-022 (Manage Cart Table), FL-023 (Manage Order Details Table), FL-026 (Admin Dashboard) |

### Description
Administrator account with elevated privileges including product management, customer management, and database table inspection/cleanup. Identified by a persistent `tname` cookie after login.

### Attributes

| Field | Type | Description |
|---|---|---|
| `Name` | `String` | Admin username; stored in `tname` cookie after login |
| `Password` | `String` | Plaintext password (no hashing) |

### Business Rules
1. Admin credentials are matched by Name AND Password (both required).
2. `tname` cookie (maxAge=9999) stores admin username; no logout mechanism.
3. No role-based access control exists beyond the cookie name distinction (`tname` vs `cname`).
4. Servlets do not enforce admin authentication; only JSP navbars gate admin UI links.

---

## DC-006: Cart

| Field | Value |
|---|---|
| **Concept ID** | DC-006 |
| **Name** | Cart |
| **Entity Class** | `com.entity.cart` |
| **Related DB Table** | `cart` |
| **Related Flows** | FL-007 (Add to Cart — Customer), FL-008 (Add to Cart — Guest), FL-009 (Add to Cart — Admin view), FL-010 (Remove Cart — Customer), FL-011 (Remove Cart — Guest), FL-012 (Remove Cart — Admin/Customer), FL-013 (Remove Cart — Admin/Guest), FL-015 (Payment — reads + deletes cart), FL-022 (Admin Manage Cart Table), FL-024 (View Orders indirect) |

### Description
A shopping basket holding product selections before checkout. The same `cart` table serves both authenticated customers (Name = email) and guests (Name = NULL). Each row represents one product selection with a quantity.

### Attributes

| Field | Type | Description |
|---|---|---|
| `Name` | `String` | Customer email (authenticated) or NULL (guest) |
| `bname` | `String` | Brand name (denormalized from product) |
| `cname` | `String` | Category name (denormalized from product) |
| `pname` | `String` | Product name (denormalized from product) |
| `pprice` | `int` | Unit price at time of add (denormalized) |
| `pquantity` | `int` | Quantity of this product in cart |
| `pimage` | `String` | Product image filename; used as item key for updates/deletes |

### Business Rules
1. **Guest cart is shared** — All anonymous users share a single NULL-named cart partition; concurrent guests interfere with each other.
2. Duplicate product detection uses bname + cname + pname + pprice + pimage combination; matching rows increment pquantity by 1.
3. `pimage` filename acts as the de facto item identifier for removals (no separate cart row ID).
4. Cart data is denormalized — product attributes copied at add-time; price changes after add are not reflected.
5. Cart is fully cleared (by Name or NULL) upon successful checkout.
6. No maximum cart size or stock validation occurs at cart add time.

---

## DC-007: Orders

| Field | Value |
|---|---|
| **Concept ID** | DC-007 |
| **Name** | Orders |
| **Entity Class** | `com.entity.orders` |
| **Related DB Table** | `orders` |
| **Related Flows** | FL-015 (Payment — creates order), FL-016 (Remove Order — Customer), FL-017 (Remove Order — Admin), FL-024 (View Orders), FL-025 (View Order Details) |

### Description
Represents a placed order summarizing the customer, shipping city, total amount, date, and current fulfillment status. Created atomically with order_details during the payment flow.

### Attributes

| Field | Type | Description |
|---|---|---|
| `Order_Id` | `int` | Auto-incremented primary key |
| `Customer_Name` | `String` | Customer's name (from form input CName) or email |
| `Customer_City` | `String` | City from shipping address form |
| `Date` | `String` | `java.util.Date().toString()` — locale-dependent string timestamp |
| `Total_Price` | `int` | Total order value in rupees (from cart total passed via form) |
| `Status` | `String` | Always `"Processing"` at creation; no update mechanism |

### Business Rules
1. Order status is hardcoded to `"Processing"` on creation; no workflow exists to advance status.
2. Date is stored as a Java Date toString() — not a normalized timestamp, making date sorting unreliable.
3. The Date field is the foreign key linking `orders` to `order_details` (instead of Order_Id) — fragile if two orders share the same timestamp.
4. Customer can delete their own orders (no ownership validation beyond reading cookie).
5. No inventory decrement occurs on order creation.

---

## DC-008: OrderDetails

| Field | Value |
|---|---|
| **Concept ID** | DC-008 |
| **Name** | OrderDetails |
| **Entity Class** | `com.entity.order_details` |
| **Related DB Table** | `order_details` (also queried as `Order_details`) |
| **Related Flows** | FL-015 (Payment — creates via INSERT-SELECT from cart), FL-023 (Admin Remove Detail Row), FL-025 (View Order Details) |

### Description
Line-item detail records for an order, created by copying cart contents at checkout. Each row represents one product within an order, with full product attributes denormalized.

### Attributes

| Field | Type | Description |
|---|---|---|
| `Date` | `String` | Order date (links back to `orders.Date`); initially NULL, updated after INSERT |
| `Name` | `String` | Customer name/email (guest: NULL initially, then updated) |
| `bname` | `String` | Brand name (copied from cart) |
| `cname` | `String` | Category name (copied from cart) |
| `pname` | `String` | Product name (copied from cart) |
| `pprice` | `int` | Unit price at time of order (copied from cart) |
| `pquantity` | `int` | Quantity ordered (copied from cart) |
| `pimage` | `String` | Product image filename; used as row identifier for admin deletion |

### Business Rules
1. Populated by INSERT-SELECT from `cart` at checkout; not inserted row-by-row.
2. Date and Name are initially NULL for the inserted rows and then updated in a separate UPDATE statement — two-step process with gap for partial failure.
3. Guest order details have Name updated via `updateOrder_details()` which sets Date AND Name simultaneously.
4. Deleting a parent `orders` row does NOT cascade to `order_details` rows.
5. Admin can delete individual detail rows by (Date + pimage) composite key.

---

## DC-009: ContactUs

| Field | Value |
|---|---|
| **Concept ID** | DC-009 |
| **Name** | ContactUs |
| **Entity Class** | `com.entity.contactus` |
| **Related DB Table** | `Contactus` (note capital C) |
| **Related Flows** | FL-018 (Guest Contact Us), FL-019 (Customer Contact Us), FL-021 (Admin Remove Entry) |

### Description
Stores customer/visitor contact form submissions for admin review. Both guests and authenticated customers can submit inquiries.

### Attributes

| Field | Type | Description |
|---|---|---|
| `id` | `int` | Auto-incremented primary key |
| `Name` | `String` | Submitter's name (free text, not validated against customer table) |
| `Email_Id` | `String` | Submitter's email |
| `Contact_No` | `int` | Submitter's phone number |
| `Message` | `String` | Free-text inquiry message |

### Business Rules
1. No server-side validation on message content or length.
2. Contact submissions from guests and customers both insert into the same `Contactus` table.
3. Admin can view all entries via `table_contactus.jsp` and delete by `id`.
4. No acknowledgement email is sent; success is confirmed only by redirect to `cupass.jsp` / `cupassc.jsp`.

---

## DC-010: ViewList

| Field | Value |
|---|---|
| **Concept ID** | DC-010 |
| **Name** | ViewList |
| **Entity Class** | `com.entity.viewlist` |
| **Related DB Table** | `viewlist` (likely a SQLite VIEW joining product + brand + category) |
| **Related Flows** | FL-005 (View Product Catalog), FL-026 (Admin Home), FL-027 (Customer Home), FL-007/FL-008 (selecteditem via getSelecteditem) |

### Description
A read-only denormalized view of the product catalog that pre-joins product data with brand and category names. Used for all product listing and product detail pages. The `Pimage` field serves as the primary lookup key for individual item retrieval.

### Attributes

| Field | Type | Description |
|---|---|---|
| `bname` | `String` | Brand name (joined from brand table) |
| `cname` | `String` | Category name (joined from category table) |
| `pname` | `String` | Product name |
| `pprice` | `int` | Product price |
| `pquantity` | `int` | Available quantity |
| `pimage` | `String` | Image filename (acts as surrogate key for item lookup) |

### Business Rules
1. `viewlist` has no primary key in the entity class; `pimage` is used as the lookup key in `getSelecteditem()`.
2. All product display pages (catalog, home, detail) query `viewlist` rather than `product` directly.
3. No admin-facing writable interface exists for `viewlist`; it is maintained automatically as a DB view.

---

## DC-011: Mobile

| Field | Value |
|---|---|
| **Concept ID** | DC-011 |
| **Name** | Mobile |
| **Entity Class** | `com.entity.mobile` |
| **Related DB Table** | `mobile` |
| **Related Flows** | FL-006 (Browse by Category — mobile) |

### Description
A category-specific read view (or table) containing only mobile phone products. Has the same attribute structure as ViewList but filtered to mobile category. Used by `mobile.jsp`, `mobilea.jsp`, `mobilec.jsp`.

### Attributes

| Field | Type | Description |
|---|---|---|
| `bname` | `String` | Brand name |
| `cname` | `String` | Category name (always "mobile") |
| `pname` | `String` | Product name |
| `pprice` | `int` | Price |
| `pquantity` | `int` | Quantity |
| `pimage` | `String` | Image filename |

### Business Rules
1. Queried read-only via `DAO3.getAllmobile()`.
2. Links to `selecteditem.jsp?Pn=<pimage>` for detail view.

---

## DC-012: TV

| Field | Value |
|---|---|
| **Concept ID** | DC-012 |
| **Name** | TV |
| **Entity Class** | `com.entity.tv` |
| **Related DB Table** | `tv` |
| **Related Flows** | FL-006 (Browse by Category — tv) |

### Description
Category-specific read view for television products. Same structure as Mobile. Used by `tv.jsp`, `tva.jsp`, `tvc.jsp`.

### Attributes

| Field | Type | Description |
|---|---|---|
| `bname` | `String` | Brand name |
| `cname` | `String` | Category name (always "tv") |
| `pname` | `String` | Product name |
| `pprice` | `int` | Price |
| `pquantity` | `int` | Quantity |
| `pimage` | `String` | Image filename |

### Business Rules
1. Queried read-only via `DAO3.getAlltv()`.

---

## DC-013: Laptop

| Field | Value |
|---|---|
| **Concept ID** | DC-013 |
| **Name** | Laptop |
| **Entity Class** | `com.entity.laptop` |
| **Related DB Table** | `laptop` |
| **Related Flows** | FL-006 (Browse by Category — laptop) |

### Description
Category-specific read view for laptop products. Same structure as Mobile. Used by `laptop.jsp`, `laptopa.jsp`, `laptopc.jsp`.

### Attributes

| Field | Type | Description |
|---|---|---|
| `bname` | `String` | Brand name |
| `cname` | `String` | Category name (always "laptop") |
| `pname` | `String` | Product name |
| `pprice` | `int` | Price |
| `pquantity` | `int` | Quantity |
| `pimage` | `String` | Image filename |

### Business Rules
1. Queried read-only via `DAO3.getAlllaptop()`.

---

## DC-014: Watch

| Field | Value |
|---|---|
| **Concept ID** | DC-014 |
| **Name** | Watch |
| **Entity Class** | `com.entity.watch` |
| **Related DB Table** | `watch` |
| **Related Flows** | FL-006 (Browse by Category — watch) |

### Description
Category-specific read view for smartwatch/watch products. Same structure as Mobile. Used by `watch.jsp`, `watcha.jsp`, `watchc.jsp`.

### Attributes

| Field | Type | Description |
|---|---|---|
| `bname` | `String` | Brand name |
| `cname` | `String` | Category name (always "watch") |
| `pname` | `String` | Product name |
| `pprice` | `int` | Price |
| `pquantity` | `int` | Quantity |
| `pimage` | `String` | Image filename |

### Business Rules
1. Queried read-only via `DAO3.getAllwatch()`.

---

## Domain Concept Relationship Summary

```
brand (DC-002) ─────────┐
                        ├──→ product (DC-001) ──→ viewlist (DC-010) ──→ mobile/tv/laptop/watch (DC-011–014)
category (DC-003) ──────┘
                                                       ↓
                                                cart (DC-006) ──→ order_details (DC-008)
                                                                         ↑
customer (DC-004) ──→ cart.Name                orders (DC-007) ──────────┘
usermaster (DC-005) ──→ admin access
contactus (DC-009) ──→ standalone inquiry records
```
