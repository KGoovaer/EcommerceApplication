# Cross-Domain Dependencies — EcommerceApp

**Document ID:** CDD-001  
**Version:** 1.0  
**Phase:** Verification  
**Generated:** 2026-03-18  

---

## Overview

This document maps bidirectional dependencies between the seven business domains of EcommerceApp. Each dependency is verified against actual source code and cross-checked against existing documentation.

---

## Domain Inventory

| ID | Domain | Primary Tables | Primary Docs |
|---|---|---|---|
| D1 | Authentication | `customer`, `usermaster` | FUREQ-002, UC-002, UC-003 |
| D2 | CustomerManagement | `customer` | FUREQ-006, UC-012 |
| D3 | ProductCatalog | `product`, `brand`, `category`, `viewlist`, `tv`, `laptop`, `mobile`, `watch` | FUREQ-003, UC-004, UC-011 |
| D4 | ShoppingCart | `cart` | FUREQ-004, UC-005, UC-006 |
| D5 | OrderManagement | `orders`, `order_details` | FUREQ-005, UC-007–009 |
| D6 | AdminOperations | All tables (admin reads) | FUREQ-006, UC-011–014 |
| D7 | ContactUs | `Contactus` | FUREQ-006, UC-010 |

---

## Dependency Map

### D1 → D4: Authentication drives Cart Identity

**Direction:** Authentication → ShoppingCart  
**Description:** The `cname` cookie value (customer email) becomes the `Name` field discriminator in the `cart` table. Unauthenticated users produce `Name IS NULL` cart rows.  
**Evidence:**  
- `addtocart` servlet reads `request.getParameter("N")` (the cname cookie value) and passes it to `DAO3.addtocartnull(cart)` as `cart.Name`
- `addtocartnull` servlet sets `Name = null` explicitly for guest rows
**Documented:** ✅ FL-007, FL-008, FUREQ-004, DC-006  

---

### D4 → D1: Cart-to-Login Bridge (Guest Checkout Flow)

**Direction:** ShoppingCart → Authentication  
**Description:** When a guest attempts checkout (`checkcustomer` redirect path), the `Total` and `CusName` parameters are preserved in the redirect URL so that post-login checkout can resume.  
**Evidence:**  
- `checkcustomer` servlet: `if(CusName.equals("empty")) response.sendRedirect("ShippingAddress.jsp?Total=...&CusName=...")`
- `addcustomer` servlet: passes `Total6` and `CusName6` through registration redirect
**Documented:** ✅ FL-002, FL-001, FUREQ-002 (BUREQ-002-03, BUREQ-001-03)  

---

### D3 → D4: Product Catalog drives Cart Content

**Direction:** ProductCatalog → ShoppingCart  
**Description:** Cart items are populated from the `viewlist` view. The `selecteditem*.jsp` pages pass product fields (bname, cname, pname, pprice, pimage) as GET parameters to the add-to-cart servlets.  
**Evidence:**  
- `addtocart` reads: `id`(bname), `ie`(cname), `ig`(pname), `ih`(pprice), `ii`(pquantity), `ij`(pimage) from request parameters
- `cart` table stores: bname, cname, pname, pprice, pquantity, pimage — exact mirror of viewlist columns
**Documented:** ✅ FL-005, FL-007, FL-008, FF-002  

---

### D4 → D5: Cart drives Order Creation

**Direction:** ShoppingCart → OrderManagement  
**Description:** Order line items are created via bulk INSERT-SELECT from `cart` to `order_details`. The cart is then cleared. Total price flows from JSP parameter (computed on client).  
**Evidence:**  
- `DAO4.addOrder_details()`: `INSERT INTO order_details(...) SELECT * FROM cart WHERE Name IS NULL`  
- `DAO4.addOrder_details2(st)`: `INSERT INTO order_details(...) SELECT * FROM cart WHERE Name=?`  
- `DAO4.deletecart()` / `deletecart2(st)`: clears cart after order creation
**Documented:** ⚠️ High gap — FF-003 documents this incorrectly as line-by-line INSERT (see gap report)  

---

### D5 ↛ D3: No Reverse Stock Update

**Direction:** OrderManagement → ProductCatalog  
**Description:** No UPDATE to `product.pquantity` occurs when an order is placed. Stock is not decremented on purchase.  
**Evidence:** No UPDATE on `product` table exists in any DAO or servlet.  
**Documented:** ⚠️ Medium gap — Not explicitly documented as a known limitation. `product.pquantity` field exists but is never decremented on order creation.  

---

### D1 → D5: Auth identity in Order Records

**Direction:** Authentication → OrderManagement  
**Description:** `orders.Customer_Name` stores the value from `CName` request parameter (derived from cookie or form input). There is no FK relationship enforced — customer name is stored as plain text.  
**Evidence:**  
- `payprocess` reads `CName` = `request.getParameter("CName")`  
- `DAO4.addOrders(orders)` stores `Customer_Name` as passed string
**Documented:** ✅ FL-015, FUREQ-005  

---

### D5 ↛ D5: Order-to-OrderDetails Orphan Risk

**Direction:** OrderManagement internal  
**Description:** When an order is cancelled (`/removeorders` or `/remove_orders`), only the `orders` row is deleted. Associated `order_details` rows (linked by customer name and date) are NOT deleted. This creates orphan records.  
**Evidence:**  
- `DAO2.removeorders(orders)`: `DELETE FROM orders WHERE Order_Id=?`  
- No corresponding DELETE on `order_details` in either `removeorders` or `remove_orders` servlets
**Documented:** ⚠️ High gap — FF-003 mentions "Associated order_details rows are NOT deleted on cancellation" in a note, but the business and use-case layers (UC-009, BUREQ-009-01) do not document this data integrity issue.  

---

### D6 → All: Admin Cross-Domain Access Without Auth Validation

**Direction:** AdminOperations → All Domains  
**Description:** Admin-only servlets access tables across all domains but perform no server-side validation of the `tname` cookie. Any unauthenticated HTTP request can invoke these endpoints.  
**Evidence (admin-only endpoints with no cookie check):**  
- `/addproduct` → `product` table  
- `/deletecustomer` → `customer` table  
- `/remove_orders` → `orders` table  
- `/remove_contactus` → `Contactus` table  
- `/removetable_cart` → `cart` table  
- `/removetable_order_details` → `order_details` table  
- `/removecarta` → `cart` table (admin removes from customer cart)  
- `/removecartnulla` → `cart` table (admin removes from guest cart)  
**Documented:** ⚠️ High gap — NFUREQ-001 notes cookies are forgeable but does not explicitly document that admin servlet endpoints have zero server-side auth enforcement. This should be documented as a known critical security limitation in FUREQ-006 and NFUREQ-001.  

---

### D7 → D6: Contact Enquiries consumed by Admin

**Direction:** ContactUs → AdminOperations  
**Description:** Enquiries submitted via `/addContactus` (guest) or `/addContactusc` (customer) are readable and deletable only by the admin via `table_contactus.jsp` → `/remove_contactus`.  
**Evidence:**  
- `DAO5.getcontactus()`: `SELECT * FROM Contactus`  
- `DAO5.removecont(contactus)`: `DELETE FROM Contactus WHERE id=?`  
**Documented:** ✅ FL-018, FL-019, FL-021, FUREQ-006  

---

## Bidirectional Coverage Summary

| Dependency | Direction | Source Domain | Target Domain | Status |
|---|---|---|---|---|
| Auth identity → Cart Name field | → | D1 Authentication | D4 ShoppingCart | ✅ Documented |
| Guest cart → Login bridge | → | D4 ShoppingCart | D1 Authentication | ✅ Documented |
| Product fields → Cart content | → | D3 ProductCatalog | D4 ShoppingCart | ✅ Documented |
| Cart bulk copy → OrderDetails | → | D4 ShoppingCart | D5 OrderManagement | ⚠️ Critical — FF-003 inaccurate |
| Order creation → No stock update | ↛ | D5 OrderManagement | D3 ProductCatalog | ⚠️ Medium — undocumented limitation |
| Auth name → Order customer field | → | D1 Authentication | D5 OrderManagement | ✅ Documented |
| Order cancel → OrderDetails orphan | ↛ | D5 internal | D5 internal | ⚠️ High — only noted in FF-003 |
| Admin endpoints → No server auth | → | D6 AdminOperations | All | ⚠️ High — missing in NFUREQ-001 |
| Contact submissions → Admin review | → | D7 ContactUs | D6 AdminOperations | ✅ Documented |
