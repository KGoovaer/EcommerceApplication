# Discovered Execution Flows

**Application:** EcommerceApp — Java EE (Servlet 3.0 + JSP + SQLite + Maven WAR)  
**Analysis Date:** Auto-generated from source code discovery  
**Source:** `EcommerceApp/src/main/java/com/servlet/`, `com/dao/`, `com/entity/`, JSPs

---

## Table of Contents

- [FL-001: Customer Registration](#fl-001-customer-registration)
- [FL-002: Customer Login](#fl-002-customer-login)
- [FL-003: Admin Login](#fl-003-admin-login)
- [FL-004: Add Product](#fl-004-add-product)
- [FL-005: View Product Catalog](#fl-005-view-product-catalog)
- [FL-006: Browse by Category](#fl-006-browse-by-category)
- [FL-007: Add to Cart (Authenticated Customer)](#fl-007-add-to-cart-authenticated-customer)
- [FL-008: Add to Cart (Unauthenticated — Guest)](#fl-008-add-to-cart-unauthenticated--guest)
- [FL-009: Add to Cart (Unauthenticated — Admin View)](#fl-009-add-to-cart-unauthenticated--admin-view)
- [FL-010: Remove from Cart (Customer)](#fl-010-remove-from-cart-customer)
- [FL-011: Remove from Cart (Guest)](#fl-011-remove-from-cart-guest)
- [FL-012: Remove from Cart (Admin View — Customer Cart)](#fl-012-remove-from-cart-admin-view--customer-cart)
- [FL-013: Remove from Cart (Admin View — Guest Cart)](#fl-013-remove-from-cart-admin-view--guest-cart)
- [FL-014: Shipping Address Selection](#fl-014-shipping-address-selection)
- [FL-015: Payment Processing](#fl-015-payment-processing)
- [FL-016: Remove Orders (Customer)](#fl-016-remove-orders-customer)
- [FL-017: Remove Orders (Admin)](#fl-017-remove-orders-admin)
- [FL-018: Contact Us Submission (Guest)](#fl-018-contact-us-submission-guest)
- [FL-019: Contact Us Submission (Customer)](#fl-019-contact-us-submission-customer)
- [FL-020: Admin Delete Customer](#fl-020-admin-delete-customer)
- [FL-021: Admin Remove Contact Us Entry](#fl-021-admin-remove-contact-us-entry)
- [FL-022: Admin Manage Tables — Remove Cart Row](#fl-022-admin-manage-tables--remove-cart-row)
- [FL-023: Admin Manage Tables — Remove Order Detail Row](#fl-023-admin-manage-tables--remove-order-detail-row)
- [FL-024: View Orders (Customer)](#fl-024-view-orders-customer)
- [FL-025: View Order Details](#fl-025-view-order-details)
- [FL-026: Admin Home Dashboard](#fl-026-admin-home-dashboard)
- [FL-027: Customer Home](#fl-027-customer-home)
- [SQL Query Inventory](#sql-query-inventory)

---

## FL-001: Customer Registration

| Field | Value |
|---|---|
| **Flow ID** | FL-001 |
| **Name** | Customer Registration |
| **Trigger** | `POST /addcustomer` |
| **Actor** | Guest |
| **Servlet** | `com.servlet.addcustomer` |

### Path
```
customer_reg.jsp (form)
  → POST /addcustomer
    → Trim Username, Password, Email_Id, Contact_No
    → DAO2(DBConnect.getConn())
    → DAO2.checkcust2(customer)  [SELECT * FROM customer WHERE Name=? OR Email_Id=?]
      ├── if true (duplicate exists)
      │     → redirect: fail.jsp
      └── if false (new user)
            → DAO2.addcustomer(customer)  [INSERT INTO customer VALUES (?,?,?,?)]
              ├── if > 0 (success)
              │     → Set flash cookie "creg" (maxAge=10)
              │     → redirect: customerlogin.jsp?Total=<Total>&CusName=<CusName>
              └── if 0 (fail)
                    → redirect: fail.jsp
```

### Key Decisions
1. **Duplicate check** — `checkcust2()` blocks registration if Name OR Email_Id already exists in `customer` table.
2. **Trim whitespace** — All string inputs are trimmed before processing.
3. **Cart-continuation flow** — `Total` and `CusName` parameters are passed through from the add-to-cart flow, enabling post-registration checkout.
4. **Flash cookie `creg`** — Set `maxAge=10` to signal successful registration to the login page.

### DB Tables Accessed
| Table | Operation |
|---|---|
| `customer` | SELECT (duplicate check), INSERT (registration) |

### Success Outcome
Customer inserted into `customer` table; flash cookie `creg` set; redirected to `customerlogin.jsp`.

### Error Outcome
Duplicate name/email or DB error → redirect to `fail.jsp`.

---

## FL-002: Customer Login

| Field | Value |
|---|---|
| **Flow ID** | FL-002 |
| **Name** | Customer Login |
| **Trigger** | `POST /checkcustomer` |
| **Actor** | Guest / Customer |
| **Servlet** | `com.servlet.checkcustomer` |

### Path
```
customerlogin.jsp (form)
  → POST /checkcustomer
    → Trim Email_Id, Password; read Total, CusName params
    → DAO2(DBConnect.getConn())
    → DAO2.checkcust(customer)  [SELECT * FROM customer WHERE Password=? AND Email_Id=?]
      ├── if true (credentials valid)
      │     → Set persistent cookie "cname" = Email_Id (maxAge=9999)
      │     → if CusName == "empty"
      │           → redirect: ShippingAddress.jsp?Total=<Total>&CusName=<CusName>
      │     → else
      │           → redirect: customerhome.jsp
      └── if false (invalid credentials)
            → Set flash cookie "un" = "up" (maxAge=10)
            → redirect: customerlogin.jsp?Total=<Total>&CusName=<CusName>
```

### Key Decisions
1. **Auth via cookie** — Persistent `cname` cookie (maxAge=9999) stores customer's email as session identity.
2. **Cart-continuation** — `CusName == "empty"` means guest added items to cart before logging in; login redirects directly to shipping address.
3. **Failed login flash** — `"un"` cookie with maxAge=10 signals the login page to show an error.

### DB Tables Accessed
| Table | Operation |
|---|---|
| `customer` | SELECT (credential check) |

### Success Outcome
Cookie `cname` set; redirect to `customerhome.jsp` or `ShippingAddress.jsp`.

### Error Outcome
Flash cookie `un` set; redirect back to `customerlogin.jsp`.

---

## FL-003: Admin Login

| Field | Value |
|---|---|
| **Flow ID** | FL-003 |
| **Name** | Admin Login |
| **Trigger** | `POST /checkadmin` |
| **Actor** | Guest |
| **Servlet** | `com.servlet.checkadmin` |

### Path
```
adminlogin.jsp (form)
  → POST /checkadmin
    → Trim Username, Password
    → DAO2(DBConnect.getConn())
    → DAO2.checkadmin(usermaster)  [SELECT * FROM usermaster WHERE Name=? AND Password=?]
      ├── if true
      │     → Set persistent cookie "tname" = Username (maxAge=9999)
      │     → redirect: adminhome.jsp
      └── if false
            → Set flash cookie "un" = "up" (maxAge=10)
            → redirect: adminlogin.jsp
```

### Key Decisions
1. **Admin identity stored in `tname` cookie** — No server-side session; identity is entirely cookie-based (forgeable — known security limitation).
2. **Flash cookie `un`** — Signals login failure.

### DB Tables Accessed
| Table | Operation |
|---|---|
| `usermaster` | SELECT (credential check) |

### Success Outcome
Cookie `tname` set; redirect to `adminhome.jsp`.

### Error Outcome
Flash cookie `un` set; redirect to `adminlogin.jsp`.

---

## FL-004: Add Product

| Field | Value |
|---|---|
| **Flow ID** | FL-004 |
| **Name** | Add Product (Admin) |
| **Trigger** | `POST /addproduct` (multipart/form-data) |
| **Actor** | Admin |
| **Servlet** | `com.servlet.addproduct` |

### Path
```
addproduct.jsp (admin form with file upload)
  → POST /addproduct
    → DAO(DBConnect.getConn())
    → DAO.addproduct(request)
        → Parse multipart form fields:
            pname, pprice, pquantity
            bname → bid mapping:
              samsung→1, sony→2, lenovo→3, acer→4, onida→5
            cname → cid mapping:
              laptop→1, tv→2, mobile→3, watch→4
        → Non-form field (file) → MyUtilities.UploadFile(item, path+"images/", [".jpg",".bmp",".jpeg",".png",".webp"])
            → validates extension & file size (≤10 MB)
            → writes to filesystem → returns filename
        → if pimage != "Problem with upload"
              → INSERT INTO product(pname,pprice,pquantity,pimage,bid,cid) VALUES(?,?,?,?,?,?)
              → return 1 (success)
        → else return 0 (fail)
      ├── if > 0 → redirect: passc.jsp
      └── if 0   → redirect: failc.jsp
```

### Key Decisions
1. **Brand/Category ID hardcoded mapping** — Brand and category names are mapped to IDs via if-else chains in DAO.java (not DB lookup).
2. **File upload validation** — Extension must be `.jpg`, `.bmp`, `.jpeg`, `.png`, or `.webp`; size ≤ 10 MB.
3. **Upload path from env var** — `UPLOAD_PATH` environment variable used; falls back to empty string.
4. **No auth check in servlet** — Access control relies solely on `tname` cookie presence being checked in JSP navbars; servlet itself does not verify admin identity.

### DB Tables Accessed
| Table | Operation |
|---|---|
| `product` | INSERT |

### Success Outcome
Product inserted; image file written to server filesystem; redirect to `passc.jsp`.

### Error Outcome
Invalid file type/size or DB error → redirect to `failc.jsp`.

---

## FL-005: View Product Catalog

| Field | Value |
|---|---|
| **Flow ID** | FL-005 |
| **Name** | View All Products |
| **Trigger** | `GET viewproduct.jsp` / `viewproducta.jsp` / `viewproductc.jsp` |
| **Actor** | Guest / Admin / Customer |

### Path
```
Browser → viewproduct.jsp (Guest) | viewproducta.jsp (Admin) | viewproductc.jsp (Customer)
  → JSP scriptlet: DAO2(DBConnect.getConn())
  → DAO2.getAllviewlist()  [SELECT * FROM viewlist]
  → Render product grid with image links → selecteditem.jsp?Pn=<pimage>
                                         | selecteditema.jsp (admin)
                                         | selecteditemc.jsp (customer)
```

### Key Decisions
1. **`viewlist` is a DB view** — Joins product + brand + category; JSPs query it directly without a servlet.
2. **Three page variants** — `viewproduct.jsp` (guest navbar), `viewproducta.jsp` (admin navbar), `viewproductc.jsp` (customer navbar). Same DAO call, different navbars and different `selecteditem` link targets.

### DB Tables Accessed
| Table | Operation |
|---|---|
| `viewlist` | SELECT all rows |

### Success Outcome
Product grid displayed with images and links to product detail pages.

### Error Outcome
Empty list displayed if no products; DB exception printed to console (no user-facing error page).

---

## FL-006: Browse by Category

| Field | Value |
|---|---|
| **Flow ID** | FL-006 |
| **Name** | Browse Products by Category |
| **Trigger** | `GET mobile.jsp` / `tv.jsp` / `laptop.jsp` / `watch.jsp` (and `a`/`c` variants) |
| **Actor** | Guest / Admin / Customer |

### Path
```
Browser → mobile.jsp | tv.jsp | laptop.jsp | watch.jsp
  → JSP scriptlet: DAO3(DBConnect.getConn())
  → DAO3.getAllmobile()   [SELECT * FROM mobile]
    | DAO3.getAlltv()     [SELECT * FROM tv]
    | DAO3.getAlllaptop() [SELECT * FROM laptop]
    | DAO3.getAllwatch()  [SELECT * FROM watch]
  → Render product grid with links → selecteditem.jsp?Pn=<pimage>
```

### Key Decisions
1. **Category views** — `mobile`, `tv`, `laptop`, `watch` are separate DB views (or tables) each returning only products of that category.
2. **Three page variants** — Each category page has guest/admin/customer variants (e.g., `mobile.jsp` / `mobilea.jsp` / `mobilec.jsp`).

### DB Tables Accessed
| Table | Operation |
|---|---|
| `mobile` | SELECT all rows |
| `tv` | SELECT all rows |
| `laptop` | SELECT all rows |
| `watch` | SELECT all rows |

### Success Outcome
Category-filtered product grid displayed.

### Error Outcome
Empty grid; DB exception to console.

---

## FL-007: Add to Cart (Authenticated Customer)

| Field | Value |
|---|---|
| **Flow ID** | FL-007 |
| **Name** | Add to Cart — Logged-in Customer |
| **Trigger** | `GET /addtocart?N=<email>&id=<bname>&ie=<cname>&ig=<pname>&ih=<pprice>&ii=<qty>&ij=<pimage>` |
| **Actor** | Customer |
| **Servlet** | `com.servlet.addtocart` |

### Path
```
selecteditemc.jsp (customer product detail page)
  → GET /addtocart (with query params)
    → Read params: N (customer email from cookie), id, ie, ig, ih, ii, ij
    → Build cart entity (Name=N set)
    → DAO3(DBConnect.getConn())
    → DAO3.checkaddtocartnull(c)
        [SELECT * FROM cart WHERE Name=? AND bname=? AND cname=? AND pname=? AND pprice=? AND pimage=?]
      ├── if true (item already in cart for this customer)
      │     → DAO3.updateaddtocartnull(c)
      │         [UPDATE cart SET pquantity=(pquantity+1) WHERE Name=? AND bname=? AND cname=? AND pname=? AND pprice=? AND pimage=?]
      │       ├── success → set flash cookie "cart" (maxAge=10) → redirect: cart.jsp
      │       └── fail    → redirect: selecteditemc.jsp
      └── if false (new cart item)
            → DAO3.addtocartnull(c)
                [INSERT INTO cart VALUES(?,?,?,?,?,?,?)]
              ├── success → set flash cookie "cart" (maxAge=10) → redirect: cart.jsp
              └── fail    → redirect: selecteditemc.jsp
```

### Key Decisions
1. **Name field identifies user** — Cart rows for authenticated customers have `Name = customer_email`.
2. **Quantity increment** — Duplicate detection updates quantity by +1 rather than inserting a new row.
3. **Flash cookie `cart`** — Signals `cart.jsp` to show "Product added successfully" message.
4. **No server-side auth guard** — Servlet does not validate `cname` cookie; trusts `N` parameter.

### DB Tables Accessed
| Table | Operation |
|---|---|
| `cart` | SELECT (check), UPDATE (increment) or INSERT (new item) |

### Success Outcome
Cart item inserted or quantity incremented; flash cookie set; redirect to `cart.jsp`.

### Error Outcome
DB error → redirect to `selecteditemc.jsp`.

---

## FL-008: Add to Cart (Unauthenticated — Guest)

| Field | Value |
|---|---|
| **Flow ID** | FL-008 |
| **Name** | Add to Cart — Guest |
| **Trigger** | `GET /addtocartnull?id=<bname>&ie=<cname>&ig=<pname>&ih=<pprice>&ii=<qty>&ij=<pimage>` |
| **Actor** | Guest |
| **Servlet** | `com.servlet.addtocartnull` |

### Path
```
selecteditem.jsp (guest product detail page)
  → GET /addtocartnull (with query params)
    → N = null (Name is intentionally null for guests)
    → Build cart entity (Name NOT set)
    → DAO2(DBConnect.getConn())
    → DAO2.checkaddtocartnull(c)
        [SELECT * FROM cart WHERE Name IS NULL AND bname=? AND cname=? AND pname=? AND pprice=? AND pimage=?]
      ├── if true (same item already in guest cart)
      │     → DAO2.updateaddtocartnull(c)
      │         [UPDATE cart SET pquantity=(pquantity+1) WHERE Name IS NULL AND ...]
      │       ├── success → set flash cookie "cart" → redirect: cartnull.jsp
      │       └── fail    → redirect: selecteditem.jsp
      └── if false
            → DAO2.addtocartnull(c)
                [INSERT INTO cart (bname,cname,pname,pprice,pquantity,pimage) VALUES(?,?,?,?,?,?)]
              ├── success → set flash cookie "cart" → redirect: cartnull.jsp
              └── fail    → redirect: selecteditem.jsp
```

### Key Decisions
1. **NULL Name identifies guest cart** — `Name IS NULL` is the filter for unauthenticated cart items.
2. **Note:** All guests share a single anonymous cart since Name is always NULL; multiple simultaneous guest sessions will interfere with each other.
3. **Different cart view** — Redirects to `cartnull.jsp` (guest cart page) instead of `cart.jsp`.

### DB Tables Accessed
| Table | Operation |
|---|---|
| `cart` | SELECT (check), UPDATE (increment) or INSERT (new item, no Name) |

### Success Outcome
Cart item inserted with NULL Name; redirect to `cartnull.jsp`.

### Error Outcome
DB error → redirect to `selecteditem.jsp`.

---

## FL-009: Add to Cart (Unauthenticated — Admin View)

| Field | Value |
|---|---|
| **Flow ID** | FL-009 |
| **Name** | Add to Cart — Admin Page (Unauthenticated) |
| **Trigger** | `GET /addtocartnulla` |
| **Actor** | Admin (browsing as guest context) |
| **Servlet** | `com.servlet.addtocartnulla` |

### Path
Same as FL-008 but redirects to `cartnulla.jsp` (admin-navbar cart view) and `selecteditema.jsp` on failure.

### DB Tables Accessed
| Table | Operation |
|---|---|
| `cart` | SELECT (check), UPDATE or INSERT with NULL Name |

### Success Outcome
Cart row inserted; redirect to `cartnulla.jsp`.

### Error Outcome
Redirect to `selecteditema.jsp`.

---

## FL-010: Remove from Cart (Customer)

| Field | Value |
|---|---|
| **Flow ID** | FL-010 |
| **Name** | Remove Cart Item — Authenticated Customer |
| **Trigger** | `GET /removecart?id=<Name>&ie=<pimage>` |
| **Actor** | Customer |
| **Servlet** | `com.servlet.removecart` |

### Path
```
cart.jsp → delete link → GET /removecart?id=<email>&ie=<pimage>
  → Build cart entity (Name=id, pimage=ie)
  → DAO2.removecart(c)  [DELETE FROM cart WHERE Name=? AND pimage=?]
  → redirect: cart.jsp (success or failure — same redirect)
```

### DB Tables Accessed
| Table | Operation |
|---|---|
| `cart` | DELETE (by Name + pimage) |

### Success Outcome
Cart item deleted; redirect to `cart.jsp`.

### Error Outcome
DB error printed to console; redirect to `cart.jsp` (no user-facing error).

---

## FL-011: Remove from Cart (Guest)

| Field | Value |
|---|---|
| **Flow ID** | FL-011 |
| **Name** | Remove Cart Item — Guest |
| **Trigger** | `GET /removecartnull?ie=<pimage>` |
| **Actor** | Guest |
| **Servlet** | `com.servlet.removecartnull` |

### Path
```
cartnull.jsp → delete link → GET /removecartnull?ie=<pimage>
  → DAO2.removecartnull(c)  [DELETE FROM cart WHERE Name IS NULL AND pimage=?]
  → redirect: cartnull.jsp
```

### DB Tables Accessed
| Table | Operation |
|---|---|
| `cart` | DELETE (Name IS NULL + pimage) |

### Success Outcome
Null-named cart item deleted; redirect to `cartnull.jsp`.

### Error Outcome
Redirect to `cartnull.jsp` (same for success/failure).

---

## FL-012: Remove from Cart (Admin View — Customer Cart)

| Field | Value |
|---|---|
| **Flow ID** | FL-012 |
| **Name** | Remove Cart Item — Admin View (Customer-Owned) |
| **Trigger** | `GET /removecarta?id=<Name>&ie=<pimage>` |
| **Actor** | Admin |
| **Servlet** | `com.servlet.removecarta` |

### Path
```
carta.jsp → delete link → GET /removecarta?id=<customerEmail>&ie=<pimage>
  → DAO2.removecart(c)  [DELETE FROM cart WHERE Name=? AND pimage=?]
  → redirect: carta.jsp?custname=<id>
```

### DB Tables Accessed
| Table | Operation |
|---|---|
| `cart` | DELETE (by Name + pimage) |

### Success Outcome
Customer cart item deleted; redirect back to `carta.jsp` filtered by customer name.

---

## FL-013: Remove from Cart (Admin View — Guest Cart)

| Field | Value |
|---|---|
| **Flow ID** | FL-013 |
| **Name** | Remove Cart Item — Admin View (Null/Guest Cart) |
| **Trigger** | `GET /removecartnulla?ie=<pimage>` |
| **Actor** | Admin |
| **Servlet** | `com.servlet.removecartnulla` |

### Path
```
cartnulla.jsp → delete link → GET /removecartnulla?ie=<pimage>
  → DAO2.removecartnull(c)  [DELETE FROM cart WHERE Name IS NULL AND pimage=?]
  → redirect: cartnulla.jsp
```

### DB Tables Accessed
| Table | Operation |
|---|---|
| `cart` | DELETE (Name IS NULL + pimage) |

---

## FL-014: Shipping Address Selection

| Field | Value |
|---|---|
| **Flow ID** | FL-014 |
| **Name** | Shipping Address and Payment Method Selection |
| **Trigger** | `POST /ShippingAddress2` |
| **Actor** | Customer |
| **Servlet** | `com.servlet.ShippingAddress2` |

### Path
```
ShippingAddress.jsp (form: CName, Address, City, State, Country, Pincode, payment radio)
  → POST /ShippingAddress2
    → Read: CName, City, Total, CusName, cash (radio), online (radio)
    → if cash != null
      → redirect: confirmpayment.jsp?CName=<CName>&City=<City>&Total=<Total>&CusName=<CusName>
    → if online != null
      → redirect: confirmonline.jsp?CName=<CName>&City=<City>&Total=<Total>&CusName=<CusName>
```

### Key Decisions
1. **No DB access** — Purely routes to payment confirmation pages based on payment method selection.
2. **Only City captured for orders** — Address, State, Country, Pincode are shown in form but only `City` is passed to payment flow.
3. **CusName = "empty"** signals a guest checkout flow.

### DB Tables Accessed
None.

### Success Outcome
Redirect to `confirmpayment.jsp` (cash) or `confirmonline.jsp` (online payment).

---

## FL-015: Payment Processing

| Field | Value |
|---|---|
| **Flow ID** | FL-015 |
| **Name** | Payment Processing and Order Creation |
| **Trigger** | `POST /payprocess` |
| **Actor** | Customer / Guest |
| **Servlet** | `com.servlet.payprocess` |

### Path
```
confirmpayment.jsp | confirmonline.jsp (form)
  → POST /payprocess
    → Read: CName, CusName, City, Total, N2 (customer email/name), current Date
    → Validate: City != "null" AND Total != "null"
        └── if invalid → redirect: paymentfail.jsp?msgf=Select any item first.

    → Build orders entity (status = "Processing")
    → Build order_details entity (Date, Name)
    → DAO4(DBConnect.getConn())

    → GUEST FLOW (CusName == "empty"):
        → DAO4.checkcart()      [SELECT * FROM cart WHERE Name IS NULL]
          ├── false → paymentfail.jsp?msgf=Add items to cart first(empty).
          └── true
              → DAO4.addOrders(o)
                  [INSERT INTO orders(Customer_Name,Customer_City,Date,Total_Price,Status) VALUES(?,?,?,?,?)]
                ├── fail → paymentfail.jsp
                └── success
                    → DAO4.addOrder_details()
                        [INSERT INTO order_details(...) SELECT * FROM cart WHERE Name IS NULL]
                      ├── fail → paymentfail.jsp
                      └── success
                          → DAO4.deletecart()
                              [DELETE FROM cart WHERE Name IS NULL]
                            ├── fail → paymentfail.jsp
                            └── success
                                → DAO4.updateOrder_details(od)
                                    [UPDATE order_details SET Date=?, Name=? WHERE Date IS NULL]
                                  ├── fail → paymentfail.jsp
                                  └── success → redirect: orders.jsp

    → CUSTOMER FLOW (CusName != "empty"):
        → DAO4.checkcart2(N)    [SELECT * FROM cart WHERE Name=?]
          ├── false → paymentfail.jsp?msgf=Add items to cart first.
          └── true
              → DAO4.addOrders(o) → addOrder_details2(N) → deletecart2(N) → updateOrder_details2(od)
                → redirect: orders.jsp (success) | paymentfail.jsp (any step fails)
```

### Key Decisions
1. **Sequential multi-step transaction** — Four DB operations (insert orders, copy cart to order_details, delete cart, update date/name) executed without a true transaction; partial failures produce inconsistent state.
2. **Guest vs. authenticated path** — `CusName == "empty"` triggers NULL-name cart/order flow.
3. **Order status hardcoded** — Always set to `"Processing"` at creation.
4. **Date** — `java.util.Date().toString()` (locale-dependent string, not normalized timestamp).
5. **No payment gateway** — Online payment flow uses the same processing logic; no real payment integration.

### DB Tables Accessed
| Table | Operation |
|---|---|
| `cart` | SELECT (check), INSERT-SELECT copy, DELETE |
| `orders` | INSERT |
| `order_details` | INSERT (copy from cart), UPDATE (date + name) |

### Success Outcome
Order row created; cart items copied to order_details; cart cleared; redirect to `orders.jsp`.

### Error Outcome
Redirect to `paymentfail.jsp` with a descriptive `msgf` parameter at any failed step.

---

## FL-016: Remove Orders (Customer)

| Field | Value |
|---|---|
| **Flow ID** | FL-016 |
| **Name** | Remove Order — Customer Self-Service |
| **Trigger** | `GET /removeorders?id=<Order_Id>` |
| **Actor** | Customer |
| **Servlet** | `com.servlet.removeorders` |

### Path
```
orders.jsp → delete link → GET /removeorders?id=<Order_Id>
  → Build orders entity (Order_Id = id)
  → DAO2.removeorders(o)  [DELETE FROM orders WHERE Order_Id=?]
  → redirect: orders.jsp (success or failure)
```

### Key Decisions
1. **No ownership check** — Any customer who knows an Order_Id can delete it (security gap).
2. **Order_details not cleaned up** — Deleting from `orders` does not cascade to `order_details`.

### DB Tables Accessed
| Table | Operation |
|---|---|
| `orders` | DELETE by Order_Id |

### Success Outcome
Order deleted; redirect to `orders.jsp`.

---

## FL-017: Remove Orders (Admin)

| Field | Value |
|---|---|
| **Flow ID** | FL-017 |
| **Name** | Remove Order — Admin |
| **Trigger** | `GET /remove_orders?id=<Order_Id>` |
| **Actor** | Admin |
| **Servlet** | `com.servlet.remove_orders` |

### Path
Same logic as FL-016 but redirects to `table_orders.jsp` (admin view).

### DB Tables Accessed
| Table | Operation |
|---|---|
| `orders` | DELETE by Order_Id |

---

## FL-018: Contact Us Submission (Guest)

| Field | Value |
|---|---|
| **Flow ID** | FL-018 |
| **Name** | Contact Us — Guest |
| **Trigger** | `POST /addContactus` |
| **Actor** | Guest |
| **Servlet** | `com.servlet.addContactus` |

### Path
```
contactus.jsp (form: Name, Email_Id, Contact_No, Message)
  → POST /addContactus
    → Build contactus entity
    → DAO5(DBConnect.getConn())
    → DAO5.addContactus(c)  [INSERT INTO Contactus(Name,Email_Id,Contact_No,Message) VALUES(?,?,?,?)]
      ├── > 0 → redirect: cupass.jsp
      └── 0   → redirect: cufail.jsp
```

### DB Tables Accessed
| Table | Operation |
|---|---|
| `Contactus` | INSERT |

### Success Outcome
Contact entry saved; redirect to `cupass.jsp`.

### Error Outcome
DB error → redirect to `cufail.jsp`.

---

## FL-019: Contact Us Submission (Customer)

| Field | Value |
|---|---|
| **Flow ID** | FL-019 |
| **Name** | Contact Us — Logged-in Customer |
| **Trigger** | `POST /addContactusc` |
| **Actor** | Customer |
| **Servlet** | `com.servlet.addContactusc` |

### Path
Same as FL-018 but redirects to `cupassc.jsp` (success) or `cufailc.jsp` (failure) — customer-navbar variants.

### DB Tables Accessed
| Table | Operation |
|---|---|
| `Contactus` | INSERT |

---

## FL-020: Admin Delete Customer

| Field | Value |
|---|---|
| **Flow ID** | FL-020 |
| **Name** | Delete Customer — Admin |
| **Trigger** | `GET /deletecustomer?Name=<Name>&Email_Id=<Email_Id>` |
| **Actor** | Admin |
| **Servlet** | `com.servlet.deletecustomer` |

### Path
```
managecustomers.jsp → delete link → GET /deletecustomer?Name=<Name>&Email_Id=<Email_Id>
  → Build customer entity (Name, Email_Id)
  → DAO(DBConnect.getConn())
  → DAO.deleteCustomer(c)  [DELETE FROM customer WHERE Name=? AND Email_Id=?]
  → redirect: managecustomers.jsp (both success and failure)
```

### Key Decisions
1. **No cascade** — Deleting a customer does not remove their cart items or orders.
2. **No auth guard** — Servlet does not check `tname` cookie; relies on JSP navbar-level access.

### DB Tables Accessed
| Table | Operation |
|---|---|
| `customer` | DELETE by Name + Email_Id |

### Success Outcome
Customer deleted; redirect to `managecustomers.jsp`.

---

## FL-021: Admin Remove Contact Us Entry

| Field | Value |
|---|---|
| **Flow ID** | FL-021 |
| **Name** | Remove Contact Us Entry — Admin |
| **Trigger** | `GET /remove_contactus?id=<id>` |
| **Actor** | Admin |
| **Servlet** | `com.servlet.remove_contactus` |

### Path
```
table_contactus.jsp → delete link → GET /remove_contactus?id=<id>
  → Build contactus entity (id = Integer.parseInt(id))
  → DAO5.removecont(c)  [DELETE FROM Contactus WHERE id=?]
  → redirect: table_contactus.jsp (success or failure)
```

### DB Tables Accessed
| Table | Operation |
|---|---|
| `Contactus` | DELETE by id |

---

## FL-022: Admin Manage Tables — Remove Cart Row

| Field | Value |
|---|---|
| **Flow ID** | FL-022 |
| **Name** | Remove Cart Row — Admin Table Management |
| **Trigger** | `GET /removetable_cart?id=<Name>&ie=<pimage>` |
| **Actor** | Admin |
| **Servlet** | `com.servlet.removetable_cart` |

### Path
```
table_cart.jsp → delete link → GET /removetable_cart?id=<Name>&ie=<pimage>
  → DAO2(DBConnect.getConn())
  → if id == "null"
      → DAO2.removecartnull(c)  [DELETE FROM cart WHERE Name IS NULL AND pimage=?]
    else
      → DAO2.removecart(c)      [DELETE FROM cart WHERE Name=? AND pimage=?]
  → redirect: table_cart.jsp
```

### Key Decisions
1. **Unified admin interface** — One servlet handles both guest (null Name) and customer cart rows by checking if `id == "null"`.

### DB Tables Accessed
| Table | Operation |
|---|---|
| `cart` | DELETE (NULL Name or by Name + pimage) |

---

## FL-023: Admin Manage Tables — Remove Order Detail Row

| Field | Value |
|---|---|
| **Flow ID** | FL-023 |
| **Name** | Remove Order Details Row — Admin |
| **Trigger** | `GET /removetable_order_details?id=<Date>&ie=<pimage>` |
| **Actor** | Admin |
| **Servlet** | `com.servlet.removetable_order_details` |

### Path
```
table_order_details.jsp → delete link → GET /removetable_order_details?id=<Date>&ie=<pimage>
  → Build order_details (Date=id, pimage=ie)
  → DAO5.removeorder_details(c)  [DELETE FROM order_details WHERE Date=? AND pimage=?]
  → redirect: table_order_details.jsp
```

### DB Tables Accessed
| Table | Operation |
|---|---|
| `order_details` | DELETE by Date + pimage |

---

## FL-024: View Orders (Customer)

| Field | Value |
|---|---|
| **Flow ID** | FL-024 |
| **Name** | View Customer Orders |
| **Trigger** | `GET orders.jsp` (JSP with scriptlet) |
| **Actor** | Customer |

### Path
```
orders.jsp
  → Read "cname" cookie (customer email) → N = cookie value
  → DAO3(DBConnect.getConn())
  → DAO3.getOrders(N)  [SELECT * FROM orders WHERE Customer_Name=?]
  → Render table with Order_Id, Customer_Name, Customer_City, Date, Total_Price, Status
  → Delete link → /removeorders?id=<Order_Id>
  → Detail link → orderdetails.jsp?id=<Date>
```

### DB Tables Accessed
| Table | Operation |
|---|---|
| `orders` | SELECT by Customer_Name |

### Success Outcome
Order list displayed.

---

## FL-025: View Order Details

| Field | Value |
|---|---|
| **Flow ID** | FL-025 |
| **Name** | View Order Details (Items in an Order) |
| **Trigger** | `GET orderdetails.jsp?id=<Date>` |
| **Actor** | Customer |

### Path
```
orderdetails.jsp?id=<Date>
  → DAO3(DBConnect.getConn())
  → DAO3.getOrdersbydate(d)     [SELECT * FROM orders WHERE Date=?]
  → DAO3.getOrderdetails(d)     [SELECT * FROM Order_details WHERE Date=?]
  → Render: order summary + item table with running total
```

### Key Decisions
1. **Date as order key** — Orders are linked to their details via the Date string, not Order_Id. This is fragile if two orders share the same timestamp.

### DB Tables Accessed
| Table | Operation |
|---|---|
| `orders` | SELECT by Date |
| `order_details` | SELECT by Date |

---

## FL-026: Admin Home Dashboard

| Field | Value |
|---|---|
| **Flow ID** | FL-026 |
| **Name** | Admin Home Dashboard |
| **Trigger** | `GET adminhome.jsp` |
| **Actor** | Admin |

### Path
```
adminhome.jsp
  → DAO2(DBConnect.getConn())
  → DAO2.getAllviewlist()  [SELECT * FROM viewlist]
  → Render: image slideshow (tv, laptop, mobile, watch) + featured products carousel
```

### DB Tables Accessed
| Table | Operation |
|---|---|
| `viewlist` | SELECT all rows |

---

## FL-027: Customer Home

| Field | Value |
|---|---|
| **Flow ID** | FL-027 |
| **Name** | Customer Home Page |
| **Trigger** | `GET customerhome.jsp` |
| **Actor** | Customer |

### Path
```
customerhome.jsp
  → DAO2(DBConnect.getConn())
  → DAO2.getAllviewlist()  [SELECT * FROM viewlist]
  → Render: image slideshow + featured products carousel
  → Product links → selecteditemc.jsp?Pn=<pimage>
```

### DB Tables Accessed
| Table | Operation |
|---|---|
| `viewlist` | SELECT all rows |

---

## SQL Query Inventory

All SQL queries found across DAO classes. Queries use `PreparedStatement` throughout.

| Query ID | DAO Class | Method | SQL Statement | Tables | Filter | Purpose |
|---|---|---|---|---|---|---|
| SQL-001 | DAO | `getAllbrand()` | `SELECT * FROM brand` | brand | none | Fetch all brands |
| SQL-002 | DAO | `getAllcategory()` | `SELECT * FROM category` | category | none | Fetch all categories |
| SQL-003 | DAO | `addproduct()` | `INSERT INTO product(pname,pprice,pquantity,pimage,bid,cid) VALUES(?,?,?,?,?,?)` | product | — | Insert new product |
| SQL-004 | DAO | `getAllCustomer()` | `SELECT * FROM customer` | customer | none | Fetch all customers (admin) |
| SQL-005 | DAO | `deleteCustomer()` | `DELETE FROM customer WHERE Name=? AND Email_Id=?` | customer | Name + Email_Id | Delete customer by name and email |
| SQL-006 | DAO | `getCustomer()` | `SELECT * FROM customer WHERE Email_Id=?` | customer | Email_Id | Fetch single customer by email |
| SQL-007 | DAO2 | `getAllviewlist()` | `SELECT * FROM viewlist` | viewlist | none | Fetch all products (view) |
| SQL-008 | DAO2 | `checkcust()` | `SELECT * FROM customer WHERE Password=? AND Email_Id=?` | customer | Password + Email_Id | Customer login credential check |
| SQL-009 | DAO2 | `checkadmin()` | `SELECT * FROM usermaster WHERE Name=? AND Password=?` | usermaster | Name + Password | Admin login credential check |
| SQL-010 | DAO2 | `addcustomer()` | `INSERT INTO customer(Name,Password,Email_Id,Contact_No) VALUES(?,?,?,?)` | customer | — | Register new customer |
| SQL-011 | DAO2 | `getSelecteditem()` | `SELECT * FROM viewlist WHERE Pimage=?` | viewlist | Pimage | Fetch single product by image filename |
| SQL-012 | DAO2 | `checkaddtocartnull()` | `SELECT * FROM cart WHERE Name IS NULL AND bname=? AND cname=? AND pname=? AND pprice=? AND pimage=?` | cart | Name IS NULL + product fields | Check if guest cart has item |
| SQL-013 | DAO2 | `updateaddtocartnull()` | `UPDATE cart SET pquantity=(pquantity+1) WHERE Name IS NULL AND bname=? AND cname=? AND pname=? AND pprice=? AND pimage=?` | cart | Name IS NULL + product fields | Increment guest cart item qty |
| SQL-014 | DAO2 | `addtocartnull()` | `INSERT INTO cart (bname,cname,pname,pprice,pquantity,pimage) VALUES(?,?,?,?,?,?)` | cart | — | Add new item to guest cart (no Name) |
| SQL-015 | DAO2 | `getSelectedcart()` | `SELECT * FROM cart WHERE Name IS NULL` | cart | Name IS NULL | Fetch all guest cart items |
| SQL-016 | DAO2 | `getcart()` | `SELECT * FROM cart WHERE Name=?` | cart | Name (email) | Fetch customer-specific cart items |
| SQL-017 | DAO2 | `removecartnull()` | `DELETE FROM cart WHERE Name IS NULL AND pimage=?` | cart | Name IS NULL + pimage | Remove guest cart item |
| SQL-018 | DAO2 | `removecart()` | `DELETE FROM cart WHERE Name=? AND pimage=?` | cart | Name + pimage | Remove authenticated customer cart item |
| SQL-019 | DAO2 | `checkcust2()` | `SELECT * FROM customer WHERE Name=? OR Email_Id=?` | customer | Name OR Email_Id | Check for duplicate before registration |
| SQL-020 | DAO2 | `removeorders()` | `DELETE FROM orders WHERE Order_Id=?` | orders | Order_Id | Delete order by ID |
| SQL-021 | DAO3 | `getAlltv()` | `SELECT * FROM tv` | tv | none | Fetch all TV products |
| SQL-022 | DAO3 | `getAlllaptop()` | `SELECT * FROM laptop` | laptop | none | Fetch all laptop products |
| SQL-023 | DAO3 | `getAllmobile()` | `SELECT * FROM mobile` | mobile | none | Fetch all mobile products |
| SQL-024 | DAO3 | `getAllwatch()` | `SELECT * FROM watch` | watch | none | Fetch all watch products |
| SQL-025 | DAO3 | `checkaddtocartnull()` | `SELECT * FROM cart WHERE Name=? AND bname=? AND cname=? AND pname=? AND pprice=? AND pimage=?` | cart | Name (email) + product fields | Check if authenticated customer has item in cart |
| SQL-026 | DAO3 | `updateaddtocartnull()` | `UPDATE cart SET pquantity=(pquantity+1) WHERE Name=? AND bname=? AND cname=? AND pname=? AND pprice=? AND pimage=?` | cart | Name + product fields | Increment authenticated customer cart item qty |
| SQL-027 | DAO3 | `addtocartnull()` | `INSERT INTO cart VALUES(?,?,?,?,?,?,?)` | cart | — | Add new item to authenticated customer cart |
| SQL-028 | DAO3 | `getOrders()` | `SELECT * FROM orders WHERE Customer_Name=?` | orders | Customer_Name | Fetch orders for a customer |
| SQL-029 | DAO3 | `getOrdersbydate()` | `SELECT * FROM orders WHERE Date=?` | orders | Date | Fetch order summary for a date (for detail view) |
| SQL-030 | DAO3 | `getOrderdetails()` | `SELECT * FROM Order_details WHERE Date=?` | order_details | Date | Fetch order line items by date |
| SQL-031 | DAO4 | `checkcart()` | `SELECT * FROM cart WHERE Name IS NULL` | cart | Name IS NULL | Check if guest cart has items (before checkout) |
| SQL-032 | DAO4 | `checkcart2()` | `SELECT * FROM cart WHERE Name=?` | cart | Name | Check if authenticated customer cart has items |
| SQL-033 | DAO4 | `addOrders()` | `INSERT INTO orders(Customer_Name,Customer_City,Date,Total_Price,Status) VALUES(?,?,?,?,?)` | orders | — | Create new order record |
| SQL-034 | DAO4 | `addOrder_details()` | `INSERT INTO order_details(Name,bname,cname,pname,pprice,pquantity,pimage) SELECT * FROM cart WHERE Name IS NULL` | order_details, cart | Name IS NULL | Copy guest cart items to order_details |
| SQL-035 | DAO4 | `addOrder_details2()` | `INSERT INTO order_details(Name,bname,cname,pname,pprice,pquantity,pimage) SELECT * FROM cart WHERE Name=?` | order_details, cart | Name (email) | Copy customer cart items to order_details |
| SQL-036 | DAO4 | `deletecart()` | `DELETE FROM cart WHERE Name IS NULL` | cart | Name IS NULL | Clear guest cart after checkout |
| SQL-037 | DAO4 | `deletecart2()` | `DELETE FROM cart WHERE Name=?` | cart | Name | Clear customer cart after checkout |
| SQL-038 | DAO4 | `updateOrder_details()` | `UPDATE order_details SET Date=?, Name=? WHERE Date IS NULL` | order_details | Date IS NULL | Set date and name on guest order_details after insert |
| SQL-039 | DAO4 | `updateOrder_details2()` | `UPDATE order_details SET Date=? WHERE Date IS NULL` | order_details | Date IS NULL | Set date on customer order_details after insert |
| SQL-040 | DAO5 | `getAllcart()` | `SELECT * FROM cart` | cart | none | Fetch all cart rows (admin view) |
| SQL-041 | DAO5 | `getAllorders()` | `SELECT * FROM orders` | orders | none | Fetch all orders (admin view) |
| SQL-042 | DAO5 | `getAllorder_details()` | `SELECT * FROM order_details` | order_details | none | Fetch all order detail rows (admin view) |
| SQL-043 | DAO5 | `removeorder_details()` | `DELETE FROM order_details WHERE Date=? AND pimage=?` | order_details | Date + pimage | Remove order detail row |
| SQL-044 | DAO5 | `addContactus()` | `INSERT INTO Contactus(Name,Email_Id,Contact_No,Message) VALUES(?,?,?,?)` | Contactus | — | Insert contact us form submission |
| SQL-045 | DAO5 | `getcontactus()` | `SELECT * FROM Contactus` | Contactus | none | Fetch all contact us entries (admin) |
| SQL-046 | DAO5 | `removecont()` | `DELETE FROM Contactus WHERE id=?` | Contactus | id | Delete contact us entry by ID |
