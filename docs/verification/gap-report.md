# Gap Report — EcommerceApp Documentation Verification

**Document ID:** GAP-001  
**Version:** 1.0  
**Phase:** Verification  
**Generated:** 2026-03-18  
**Verification Scope:** All documentation artifacts against `EcommerceApp/src/main/java/` and `EcommerceApp/src/main/webapp/`  

---

## Executive Summary

| Severity | Count |
|---|---|
| 🔴 Critical | 3 |
| 🟠 High | 3 |
| 🟡 Medium | 2 |
| 🟢 Low | 3 |

**Overall Verdict: FAIL** — Critical inaccuracies in `FF-003` (Checkout and Order Processing Flow) describe DAO methods that do not exist and an UPDATE condition that is wrong. These gaps mean the technical flow documentation does not reflect the actual implementation.

---

## Critical Gaps

### GAP-C01 — FF-003: Wrong DAO4 Method Names and Non-Existent Methods

**Severity:** 🔴 Critical  
**Affected Document:** `docs/functional/flows/FF-003-checkout-order-flow.md`  
**Responsible Agent:** technical-documenter  

**Description:**  
`FF-003` describes Step 2 as calling `dao4.getorders(cname)` to retrieve cart items. This method **does not exist** in `DAO4`. The actual servlet (`payprocess.java`) uses:
- `DAO4.checkcart()` — boolean check: are there any `cart` rows where `Name IS NULL`?
- `DAO4.checkcart2(String nm)` — boolean check: are there any `cart` rows where `Name=?`?

There is no retrieval of cart items in the servlet. The `Total` price is received as a request parameter from the JSP (computed client-side in the JSP), not computed server-side.

**Actual code:**
```java
// payprocess.java (actual)
String Total = request.getParameter("Total");   // total from JSP
String ND = new String(request.getParameter("N2"));
if(CusName.equals("empty")) {
    if(dao.checkcart() == true) { ... }     // DAO4.checkcart() — boolean check only
} else {
    if(dao.checkcart2(N) == true) { ... }   // DAO4.checkcart2(N) — boolean check only
}
```

**Remediation:** Rewrite FF-003 Step 2 to document: (a) total price is passed in from JSP parameter, (b) the servlet only checks if cart is non-empty (boolean), it does not retrieve cart contents.

---

### GAP-C02 — FF-003: Wrong order_details INSERT Documented (Line-by-Line vs Bulk SELECT)

**Severity:** 🔴 Critical  
**Affected Document:** `docs/functional/flows/FF-003-checkout-order-flow.md` (Steps 6–8)  
**Responsible Agent:** technical-documenter  

**Description:**  
`FF-003` Step 6 documents that order details are inserted row-by-row via `dao4.addorderdetails()` with individual field mapping. This is **architecturally wrong**. The actual implementation uses a **bulk INSERT-SELECT from the `cart` table**:

**Actual SQL (DAO4.addOrder_details):**
```sql
INSERT INTO order_details(Name,bname,cname,pname,pprice,pquantity,pimage) 
SELECT * FROM cart WHERE Name IS NULL
```

**Actual SQL (DAO4.addOrder_details2):**
```sql
INSERT INTO order_details(Name,bname,cname,pname,pprice,pquantity,pimage) 
SELECT * FROM cart WHERE Name = ?
```

This means: (1) order_details rows inherit `Name` from cart (guest rows inherit NULL Name), (2) there is no separate iteration over cart items, (3) the initial order_details rows have `Date IS NULL`.

**Remediation:** Rewrite FF-003 Step 6 to show the bulk INSERT-SELECT pattern. Remove the per-item iteration loop. Add that guest rows are inserted with `Name=NULL` and `Date=NULL` initially.

---

### GAP-C03 — FF-003: Wrong UPDATE Condition for order_details

**Severity:** 🔴 Critical  
**Affected Document:** `docs/functional/flows/FF-003-checkout-order-flow.md` (Step 8)  
**Responsible Agent:** technical-documenter  

**Description:**  
`FF-003` Step 8 documents: `UPDATE order_details SET Date=?, Customer_Name=? WHERE Customer_Name IS NULL`.

**Actual SQL (DAO4.updateOrder_details):**
```sql
UPDATE order_details SET Date=?, Name=? WHERE Date IS NULL
```

**Actual SQL (DAO4.updateOrder_details2):**
```sql
UPDATE order_details SET Date=? WHERE Date IS NULL
```

Three errors in the documentation:
1. The WHERE condition is `Date IS NULL` — not `Customer_Name IS NULL`
2. The column name is `Name` — not `Customer_Name`
3. For customer checkout (`updateOrder_details2`), only `Date` is updated (customer Name is already set from the bulk INSERT); guest checkout (`updateOrder_details`) updates both `Date` and `Name`

**Remediation:** Rewrite FF-003 Step 8 to show both the guest and authenticated customer paths with their correct SQL, WHERE conditions, and column names.

---

## High Gaps

### GAP-H01 — FF-003: ShippingAddress2 Form Fields Documented Incorrectly

**Severity:** 🟠 High  
**Affected Document:** `docs/functional/flows/FF-003-checkout-order-flow.md` (Part 1)  
**Responsible Agent:** technical-documenter  

**Description:**  
`FF-003` Part 1 documents that `ShippingAddress2` captures: `Name, Address, City, State, Country, Pincode`. The actual servlet reads only:
- `CName` — customer name
- `City` — shipping city
- `Total` — cart total (passed from upstream)
- `CusName` — guest vs customer discriminator
- `cash` / `online` — payment method radio button

The documented form fields (`Address, State, Country, Pincode`) do not exist in the code.

**Remediation:** Rewrite FF-003 Part 1 to show the actual 5 parameters read by `ShippingAddress2.doPost()`.

---

### GAP-H02 — Missing Documentation: No Server-Side Auth on Admin Endpoints

**Severity:** 🟠 High  
**Affected Documents:** `docs/functional/requirements/NFUREQ-001-security.md`, `docs/functional/requirements/FUREQ-006-contact-admin-ops.md`  
**Responsible Agent:** technical-documenter  

**Description:**  
The following admin-only servlets perform **zero server-side validation** of the `tname` (admin) cookie. Any unauthenticated HTTP request can invoke them directly:

| Servlet | Endpoint | Operation |
|---|---|---|
| `addproduct.java` | `POST /addproduct` | INSERT into `product` |
| `deletecustomer.java` | `GET /deletecustomer` | DELETE from `customer` |
| `remove_orders.java` | `GET /remove_orders` | DELETE from `orders` |
| `remove_contactus.java` | `GET /remove_contactus` | DELETE from `Contactus` |
| `removetable_cart.java` | `GET /removetable_cart` | DELETE from `cart` |
| `removetable_order_details.java` | `GET /removetable_order_details` | DELETE from `order_details` |
| `removecarta.java` | `GET /removecarta` | DELETE from `cart` (customer) |
| `removecartnulla.java` | `GET /removecartnulla` | DELETE from `cart` (guest) |

`NFUREQ-001` notes that cookies are forgeable but does not explicitly state that these admin endpoints have NO cookie validation in the servlet layer. This is a critical missing security observation.

**Remediation:** Add a new security risk item to `NFUREQ-001` explicitly listing these endpoints as lacking server-side auth enforcement.

---

### GAP-H03 — Missing Documentation: Order Status Machine Incomplete / Cancel = DELETE

**Severity:** 🟠 High  
**Affected Documents:** `docs/functional/requirements/FUREQ-005-order-management.md`, `docs/domain/domain-concepts-catalog.md` (DC-007)  
**Responsible Agent:** technical-documenter  

**Description:**  
The `orders.Status` field is set to `"Processing"` on creation and **never updated**. There is no code anywhere in the application that transitions `Status` to any other value (e.g., "Shipped", "Delivered", "Cancelled"). Order "cancellation" (UC-009) actually **deletes the `orders` row** rather than updating Status.

Additionally, when an `orders` row is deleted, the associated `order_details` rows are **not deleted** (orphan records remain).

Neither DC-007 (Orders domain concept) nor FUREQ-005 explicitly documents:
1. That `"Processing"` is the only status value ever set
2. That cancellation is implemented as a hard DELETE (not a status change)
3. That `order_details` are orphaned after order cancellation

**Remediation:** Update DC-007 and FUREQ-005 to document the actual single-state status machine and the DELETE-based cancellation with orphan order_details consequence.

---

## Medium Gaps

### GAP-M01 — `viewlist` Documented as Table, Is Actually a Database VIEW

**Severity:** 🟡 Medium  
**Affected Document:** `docs/domain/domain-concepts-catalog.md` (DC-010)  
**Responsible Agent:** doc-coordinator or technical-documenter  

**Description:**  
`DC-010` and all flow/requirement documents describe `viewlist` as a database table. Based on the schema (no INSERT/UPDATE/DELETE exists anywhere in the codebase, and it returns data combining brand name, category name, product name, price, quantity, and image — exactly the join of `product` + `brand` + `category`), `viewlist` is an SQL VIEW.

**Remediation:** Update DC-010 to clarify that `viewlist` is a DATABASE VIEW (not a base table), and note the three underlying tables it joins.

---

### GAP-M02 — Missing Limitation: No Stock Decrement on Order Creation

**Severity:** 🟡 Medium  
**Affected Document:** `docs/functional/requirements/FUREQ-005-order-management.md`, DC-001  
**Responsible Agent:** technical-documenter  

**Description:**  
`product.pquantity` is populated when a product is added but is **never decremented** when that product is purchased (ordered). No UPDATE to the `product` table exists anywhere in the application. This means the displayed stock quantity never changes regardless of orders placed.

**Remediation:** Add a known limitation note to FUREQ-005 and DC-001 stating that stock quantity is not decremented on order creation.

---

## Low Gaps

### GAP-L01 — Undocumented JSP Pages: `cartnullqty.jsp`, `passc.jsp`, `z1.jsp`, `z2.jsp`

**Severity:** 🟢 Low  
**Affected Documents:** `docs/discovered-flows.md`, `docs/index.md`  
**Responsible Agent:** discovery  

**Description:**  
Four JSP pages in `src/main/webapp/` are not referenced in any documented flow:
- `cartnullqty.jsp` — appears to be a guest cart quantity edge-case page
- `passc.jsp` — undetermined purpose (possibly password-related)
- `z1.jsp`, `z2.jsp` — undetermined purpose (possibly scratch/test pages)

**Remediation:** Minor — investigate and add a brief note in the discovery artifacts or explicitly exclude as unreachable/unused pages.

---

### GAP-L02 — `addContactusc` Servlet Not Highlighted as Separate Use Case

**Severity:** 🟢 Low  
**Affected Document:** `docs/business/use-cases/UC-010-submit-contact-enquiry.md`  
**Responsible Agent:** business-documenter  

**Description:**  
`FL-019` (Contact Us Submission — Customer) is discovered and documented at the flow level. However, `UC-010` focuses on guest enquiries. The customer-facing variant (`/addContactusc` → `cupassc.jsp`/`cufailc.jsp`) has a different redirect outcome but identical data processing. The UC does not differentiate the two actors sufficiently.

**Remediation:** Minor — add a note to UC-010 that registered customers use `/addContactusc` with outcomes redirecting to `cupassc.jsp`/`cufailc.jsp`.

---

### GAP-L03 — `DAO.addproduct()` Uses `System.getenv("UPLOAD_PATH")` — Not Documented

**Severity:** 🟢 Low  
**Affected Document:** `docs/functional/integration/INT-002-file-system.md`  
**Responsible Agent:** technical-documenter  

**Description:**  
`DAO.addproduct()` resolves the upload base path via `System.getenv("UPLOAD_PATH")` with fallback to empty string. This environment variable is not mentioned in `INT-002-file-system.md`, which only documents the hardcoded path concern from `DAO.java` (the original version).

**Remediation:** Update `INT-002` to document the `UPLOAD_PATH` environment variable as the configurable image upload path.

---

## Remediation Plan

The critical and high gaps all originate from `FF-003` (technical-documenter output) and one missing security observation (also technical-documenter). The earliest agent in the chain with gaps is the **technical-documenter**.

**Dispatch:** `doc-technical-documenter`  
**Scope:** Fix the following in `docs/functional/flows/FF-003-checkout-order-flow.md`:
1. Step 2: Remove reference to non-existent `dao4.getorders()`. Document that total comes from JSP parameter and cart validation is a boolean check via `checkcart()`/`checkcart2()`
2. Steps 6–8: Replace line-by-line INSERT documentation with the actual bulk INSERT-SELECT from cart pattern; fix UPDATE condition to `WHERE Date IS NULL`; show both guest and customer paths
3. Part 1: Fix `ShippingAddress2` form fields to match actual servlet parameters (`CName`, `City`, `Total`, `CusName`, `cash`/`online`)

Additionally update:
- `NFUREQ-001-security.md`: Add explicit risk item documenting that admin-only servlet endpoints have no server-side cookie validation
- `FUREQ-005-order-management.md` and `DC-007`: Document that order status is always "Processing"; cancellation is DELETE; order_details are orphaned on cancellation
- `DC-001` and `FUREQ-005`: Note that stock quantity (`product.pquantity`) is not decremented on purchase
