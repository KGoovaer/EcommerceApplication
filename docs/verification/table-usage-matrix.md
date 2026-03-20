# Table Usage Matrix — EcommerceApp

**Document ID:** TUM-001  
**Version:** 1.0  
**Phase:** Verification  
**Generated:** 2026-03-18  
**Source:** Direct analysis of `com/dao/DAO.java`, `DAO2.java`, `DAO3.java`, `DAO4.java`, `DAO5.java` and all servlet classes  

---

## Overview

This matrix maps every database table/view to the flows and DAO methods that read or write it, enabling cross-check of documentation coverage against actual source code.

---

## Table: `brand`

| Operation | SQL | DAO Method | Servlet | Flow |
|---|---|---|---|---|
| SELECT all | `SELECT * FROM brand` | `DAO.getAllbrand()` | JSP scriptlet (addproduct.jsp) | FL-004 (Add Product) |

**Documented:** ✅ DC-002, FUREQ-003  
**Notes:** Read-only at runtime. Brand IDs are hardcoded in `DAO.addproduct()` (samsung→1, sony→2, lenovo→3, acer→4, onida→5).

---

## Table: `category`

| Operation | SQL | DAO Method | Servlet | Flow |
|---|---|---|---|---|
| SELECT all | `SELECT * FROM category` | `DAO.getAllcategory()` | JSP scriptlet (addproduct.jsp, category*.jsp) | FL-004, FL-006 |

**Documented:** ✅ DC-003, FUREQ-003  
**Notes:** Read-only at runtime. Category IDs are hardcoded in `DAO.addproduct()` (laptop→1, tv→2, mobile→3, watch→4).

---

## Table: `product`

| Operation | SQL | DAO Method | Servlet | Flow |
|---|---|---|---|---|
| INSERT | `INSERT INTO product(pname,pprice,pquantity,pimage,bid,cid) VALUES(?,?,?,?,?,?)` | `DAO.addproduct(request)` | `/addproduct` | FL-004 |

**Documented:** ✅ DC-001, FUREQ-003, FF-004  
**Notes:** No UPDATE or DELETE on `product` exists in the current codebase. Product editing/deletion is not implemented.

---

## View: `viewlist` ⚠️ DATABASE VIEW (not a plain table)

| Operation | SQL | DAO Method | Servlet/JSP | Flow |
|---|---|---|---|---|
| SELECT all | `SELECT * FROM viewlist` | `DAO2.getAllviewlist()` | `viewproduct*.jsp` | FL-005 |
| SELECT by image | `SELECT * FROM viewlist WHERE Pimage = ?` | `DAO2.getSelecteditem(st)` | `selecteditem*.jsp` | FL-005 |

**Documented:** ✅ DC-010 — `viewlist` correctly documented as a SQL VIEW joining `product`, `brand`, and `category`  
**Notes:** `viewlist` is an SQL VIEW that joins `product`, `brand`, and `category`. No INSERT/UPDATE/DELETE operations exist or should exist on this object.

---

## Table: `tv`

| Operation | SQL | DAO Method | Servlet/JSP | Flow |
|---|---|---|---|---|
| SELECT all | `SELECT * FROM tv` | `DAO3.getAlltv()` | `tv*.jsp` | FL-006 |

**Documented:** ✅ DC-012  
**Notes:** Category-specific view of TV products. Read-only from application code.

---

## Table: `laptop`

| Operation | SQL | DAO Method | Servlet/JSP | Flow |
|---|---|---|---|---|
| SELECT all | `SELECT * FROM laptop` | `DAO3.getAlllaptop()` | `laptop*.jsp` | FL-006 |

**Documented:** ✅ DC-013  

---

## Table: `mobile`

| Operation | SQL | DAO Method | Servlet/JSP | Flow |
|---|---|---|---|---|
| SELECT all | `SELECT * FROM mobile` | `DAO3.getAllmobile()` | `mobile*.jsp` | FL-006 |

**Documented:** ✅ DC-011  

---

## Table: `watch`

| Operation | SQL | DAO Method | Servlet/JSP | Flow |
|---|---|---|---|---|
| SELECT all | `SELECT * FROM watch` | `DAO3.getAllwatch()` | `watch*.jsp` | FL-006 |

**Documented:** ✅ DC-014  

---

## Table: `customer`

| Operation | SQL | DAO Method | Servlet | Flow |
|---|---|---|---|---|
| SELECT all | `SELECT * FROM customer` | `DAO.getAllCustomer()` | JSP scriptlet (`managecustomers.jsp`) | FL-020 |
| SELECT by email | `SELECT * FROM customer WHERE Email_Id=?` | `DAO.getCustomer(eid)` | JSP scriptlets | FL-020 |
| SELECT (auth check) | `SELECT * FROM customer WHERE Password=? AND Email_Id=?` | `DAO2.checkcust(customer)` | `/checkcustomer` | FL-002 |
| SELECT (dup check) | `SELECT * FROM customer WHERE Name=? OR Email_Id=?` | `DAO2.checkcust2(customer)` | `/addcustomer` | FL-001 |
| INSERT | `INSERT INTO customer(Name,Password,Email_Id,Contact_No) VALUES(?,?,?,?)` | `DAO2.addcustomer(customer)` | `/addcustomer` | FL-001 |
| DELETE | `DELETE FROM customer WHERE Name=? AND Email_Id=?` | `DAO.deleteCustomer(customer)` | `/deletecustomer` | FL-020 |

**Documented:** ✅ DC-004, FUREQ-001, FUREQ-002, FF-001, FF-004  
**Notes:** No server-side auth check on `/deletecustomer` servlet. Documented in `NFUREQ-001-06`.

---

## Table: `usermaster`

| Operation | SQL | DAO Method | Servlet | Flow |
|---|---|---|---|---|
| SELECT (auth check) | `SELECT * FROM usermaster WHERE Name=? AND Password=?` | `DAO2.checkadmin(usermaster)` | `/checkadmin` | FL-003 |

**Documented:** ✅ DC-005, FUREQ-002, FF-004  
**Notes:** Admin credentials are stored in plaintext (documented risk in NFUREQ-001-02).

---

## Table: `cart`

| Operation | SQL | DAO Method | Servlet | Flow |
|---|---|---|---|---|
| SELECT all | `SELECT * FROM cart` | `DAO5.getAllcart()` | `table_cart.jsp` (scriptlet) | FL-022 |
| SELECT (guest) | `SELECT * FROM cart WHERE Name IS NULL` | `DAO2.getSelectedcart()` | `cartnull*.jsp` | FL-008 |
| SELECT by customer | `SELECT * FROM cart WHERE Name=?` | `DAO2.getcart(ct)` | `cart.jsp`, `carta.jsp` | FL-007 |
| SELECT check (guest) | `SELECT * FROM cart WHERE Name IS NULL AND bname=? AND cname=? AND pname=? AND pprice=? AND pimage=?` | `DAO2.checkaddtocartnull(cart)` | `/addtocartnull`, `/addtocartnulla` | FL-008, FL-009 |
| SELECT check (customer) | `SELECT * FROM cart WHERE Name=? AND bname=? AND cname=? AND pname=? AND pprice=? AND pimage=?` | `DAO3.checkaddtocartnull(cart)` | `/addtocart` | FL-007 |
| SELECT check (guest checkout) | `SELECT * FROM cart WHERE Name IS NULL` (boolean) | `DAO4.checkcart()` | `/payprocess` | FL-015 |
| SELECT check (customer checkout) | `SELECT * FROM cart WHERE Name=?` (boolean) | `DAO4.checkcart2(nm)` | `/payprocess` | FL-015 |
| INSERT (guest) | `INSERT INTO cart (bname,cname,pname,pprice,pquantity,pimage) VALUES(?,?,?,?,?,?)` | `DAO2.addtocartnull(cart)` | `/addtocartnull`, `/addtocartnulla` | FL-008, FL-009 |
| INSERT (customer) | `INSERT INTO cart VALUES(?,?,?,?,?,?,?)` | `DAO3.addtocartnull(cart)` | `/addtocart` | FL-007 |
| UPDATE quantity (guest) | `UPDATE cart SET pquantity=(pquantity+1) WHERE Name IS NULL AND …` | `DAO2.updateaddtocartnull(cart)` | `/addtocartnull`, `/addtocartnulla` | FL-008, FL-009 |
| UPDATE quantity (customer) | `UPDATE cart SET pquantity=(pquantity+1) WHERE Name=? AND …` | `DAO3.updateaddtocartnull(cart)` | `/addtocart` | FL-007 |
| DELETE (guest, by image) | `DELETE FROM cart WHERE Name IS NULL AND pimage=?` | `DAO2.removecartnull(cart)` | `/removecartnull`, `/removecartnulla`, `/removetable_cart` | FL-011, FL-013, FL-022 |
| DELETE (customer, by image) | `DELETE FROM cart WHERE Name=? AND pimage=?` | `DAO2.removecart(cart)` | `/removecart`, `/removecarta`, `/removetable_cart` | FL-010, FL-012, FL-022 |
| DELETE (all guest, checkout) | `DELETE FROM cart WHERE Name IS NULL` | `DAO4.deletecart()` | `/payprocess` | FL-015 |
| DELETE (all customer, checkout) | `DELETE FROM cart WHERE Name=?` | `DAO4.deletecart2(st)` | `/payprocess` | FL-015 |

**Documented:** ✅ DC-006, FUREQ-004, FF-002  
**Notes:** Guest cart rows use `Name IS NULL` as the discriminator. This is a single shared anonymous cart for all unauthenticated users — a thread-safety concern with the static `Connection`.

---

## Table: `orders`

| Operation | SQL | DAO Method | Servlet/JSP | Flow |
|---|---|---|---|---|
| SELECT all | `SELECT * FROM orders` | `DAO5.getAllorders()` | `table_orders.jsp` (scriptlet) | FL-026 |
| SELECT by customer | `SELECT * FROM orders WHERE Customer_Name=?` | `DAO3.getOrders(o)` | `orders.jsp` (scriptlet) | FL-024 |
| SELECT by date | `SELECT * FROM orders WHERE Date=?` | `DAO3.getOrdersbydate(d)` | `orders.jsp` (scriptlet) | FL-025 |
| INSERT | `INSERT INTO orders(Customer_Name,Customer_City,Date,Total_Price,Status) VALUES(?,?,?,?,?)` | `DAO4.addOrders(orders)` | `/payprocess` | FL-015 |
| DELETE (customer) | `DELETE FROM orders WHERE Order_Id=?` | `DAO2.removeorders(orders)` | `/removeorders` | FL-016 |
| DELETE (admin) | `DELETE FROM orders WHERE Order_Id=?` | `DAO2.removeorders(orders)` | `/remove_orders` | FL-017 |

**Documented:** ✅ DC-007, FUREQ-005, FF-003  
**Notes:** `Status` is always set to `"Processing"` on INSERT. No UPDATE to Status exists anywhere — order "cancellation" is a hard DELETE, not a status transition. Documented in `DC-007` and `FUREQ-005`.

---

## Table: `order_details`

| Operation | SQL | DAO Method | Servlet/JSP | Flow |
|---|---|---|---|---|
| SELECT all | `SELECT * FROM order_details` | `DAO5.getAllorder_details()` | `table_order_details.jsp` | FL-026 |
| SELECT by date | `SELECT * FROM Order_details WHERE Date=?` | `DAO3.getOrderdetails(d)` | `orderdetails.jsp` (scriptlet) | FL-025 |
| INSERT (bulk, guest) | `INSERT INTO order_details(Name,bname,cname,pname,pprice,pquantity,pimage) SELECT * FROM cart WHERE Name IS NULL` | `DAO4.addOrder_details()` | `/payprocess` | FL-015 |
| INSERT (bulk, customer) | `INSERT INTO order_details(Name,bname,cname,pname,pprice,pquantity,pimage) SELECT * FROM cart WHERE Name=?` | `DAO4.addOrder_details2(st)` | `/payprocess` | FL-015 |
| UPDATE (guest, set date+name) | `UPDATE order_details SET Date=?, Name=? WHERE Date IS NULL` | `DAO4.updateOrder_details(od)` | `/payprocess` | FL-015 |
| UPDATE (customer, set date) | `UPDATE order_details SET Date=? WHERE Date IS NULL` | `DAO4.updateOrder_details2(od)` | `/payprocess` | FL-015 |
| DELETE (admin) | `DELETE FROM order_details WHERE Date=? AND pimage=?` | `DAO5.removeorder_details(od)` | `/removetable_order_details` | FL-023 |

**Documented:** ✅ DC-008, FUREQ-005, FF-003 — bulk INSERT-SELECT from cart and `WHERE Date IS NULL` UPDATE condition correctly documented  
**Notes:** Rows start with `Date IS NULL` immediately after INSERT, then `Date` and `Name` are populated in a separate UPDATE. `order_details` rows are NOT deleted when a corresponding `orders` row is cancelled/deleted.

---

## Table: `Contactus`

| Operation | SQL | DAO Method | Servlet | Flow |
|---|---|---|---|---|
| SELECT all | `SELECT * FROM Contactus` | `DAO5.getcontactus()` | `table_contactus.jsp` (scriptlet) | FL-021 |
| INSERT | `INSERT INTO Contactus(Name,Email_Id,Contact_No,Message) VALUES(?,?,?,?)` | `DAO5.addContactus(contactus)` | `/addContactus`, `/addContactusc` | FL-018, FL-019 |
| DELETE | `DELETE FROM Contactus WHERE id=?` | `DAO5.removecont(contactus)` | `/remove_contactus` | FL-021 |

**Documented:** ✅ DC-009, FUREQ-006, FF-004  

---

## Coverage Summary

| Table/View | Total Operations | Documented | Gaps |
|---|---|---|---|
| `brand` | 1 | ✅ | — |
| `category` | 1 | ✅ | — |
| `product` | 1 | ✅ | No edit/delete documented (correct: not implemented) |
| `viewlist` | 2 | ✅ | Correctly documented as SQL VIEW (DC-010 updated) |
| `tv` | 1 | ✅ | — |
| `laptop` | 1 | ✅ | — |
| `mobile` | 1 | ✅ | — |
| `watch` | 1 | ✅ | — |
| `customer` | 6 | ✅ | Admin servlet auth gap documented in NFUREQ-001-06 |
| `usermaster` | 1 | ✅ | — |
| `cart` | 16 | ✅ | — |
| `orders` | 6 | ✅ | Status/cancellation model documented in DC-007 and FUREQ-005 |
| `order_details` | 7 | ✅ | Bulk INSERT-SELECT and WHERE Date IS NULL correctly documented in FF-003 |
| `Contactus` | 3 | ✅ | — |
