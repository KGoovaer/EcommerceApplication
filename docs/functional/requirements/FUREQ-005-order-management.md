# FUREQ-005: Checkout and Order Management

**Functional Requirement ID:** FUREQ-005  
**Version:** 1.0  
**Derived From:** BUREQ-007-01 to BUREQ-007-06, BUREQ-008-01 to BUREQ-008-03, BUREQ-009-01 to BUREQ-009-03  
**Traced To Use Cases:** UC-007, UC-008, UC-009  
**Traced To Processes:** BP-003  

---

## Overview

The checkout flow converts a populated cart into a persisted order. The process involves shipping address capture, payment method selection, and a multi-step database operation (insert order → copy cart items → delete cart → update order details). Order history viewing and cancellation are also covered here.

---

## Functional Requirements

### FUREQ-005-01: Shipping Address Capture

**Source:** BUREQ-007-02, BUREQ-007-03  
**Description:** The system shall present a shipping address form collecting: customer name and city. The customer shall select exactly one payment method (cash on delivery or online payment). Total price is forwarded from the JSP as a request parameter.

**Implementation:**  
- Form page: `ShippingAddress.jsp`  
- Servlet: `com.servlet.ShippingAddress2` (`@WebServlet("/ShippingAddress2")`, `@MultipartConfig`)  
- `doPost()` reads: `CName` (customer name), `City`, `Total` (total price pre-computed in JSP), `CusName` ("empty" for guest)  
- Cash on delivery → redirect `confirmpayment.jsp`  
- Online payment → redirect `confirmonline.jsp`  
- Parameters forwarded as query string: `?CName=...&City=...&Total=...&CusName=...`

---

### FUREQ-005-02: Payment Route Selection

**Source:** BUREQ-007-03  
**Description:** Based on the payment method selected, the system routes to a different confirmation page. Both paths lead to the same order creation servlet.

**Implementation:**  
- `ShippingAddress2` servlet performs routing only — no DAO calls  
- Both `confirmpayment.jsp` and `confirmonline.jsp` submit to `POST /payprocess`

---

### FUREQ-005-03: Order Validation

**Source:** BUREQ-007-01, BUREQ-007-02  
**Description:** Before creating an order, the system shall validate that the city and total price parameters are non-null, and that the customer's cart is not empty.

**Implementation:**  
- Servlet: `com.servlet.payprocess` (`@WebServlet("/payprocess")`)  
- City/total check (first): if `City.equals("null") || Total.equals("null")` → redirect `paymentfail.jsp` ("Select any item first")  
- Cart check (after city/total): `DAO4.checkcart()` (guest) or `DAO4.checkcart2(N)` (customer) — both are **boolean checks only** (returns true if cart has items); no cart items are fetched or iterated  
  - → `SELECT * FROM cart WHERE Name IS NULL` (guest) or `WHERE Name=?` (customer)  
  - if false → redirect `paymentfail.jsp` ("Add items to cart first")

---

### FUREQ-005-04: Order Record Creation

**Source:** BUREQ-007-04, BUREQ-007-05  
**Description:** The system shall insert a new order record with status "Processing" as the first step in the order creation sequence.

**Implementation:**  
- DAO: `DAO4.addorders(orders o)` in `com.dao.DAO4`  
- SQL: `INSERT INTO orders (Customer_Name, City, Total_Price, Status) VALUES (?,?,?,?)`  
- `Status` hardcoded to `"Processing"` in all cases  
- Failure → redirect `paymentfail.jsp`  
- Entity: `com.entity.orders`

---

### FUREQ-005-05: Order Details Population

**Source:** BUREQ-007-04  
**Description:** After creating the order header, the system shall copy all cart items for the customer into the `order_details` table using a single bulk INSERT-SELECT statement (not a per-item loop).

**Implementation:**  
- Guest path: `DAO4.addOrder_details()`  
  - SQL: `INSERT INTO order_details(Name,bname,cname,pname,pprice,pquantity,pimage) SELECT * FROM cart WHERE Name IS NULL`  
- Customer path: `DAO4.addOrder_details2(N)` where N = cart owner identifier  
  - SQL: `INSERT INTO order_details(Name,bname,cname,pname,pprice,pquantity,pimage) SELECT * FROM cart WHERE Name = ?`  
- Inserted rows have `Date IS NULL` (populated in the subsequent update step)  
- Failure → redirect `paymentfail.jsp`

---

### FUREQ-005-06: Cart Clearance

**Source:** BUREQ-007-04  
**Description:** After all cart items are copied to order details, the system shall delete all cart items for the customer/guest.

**Implementation:**  
- DAO: `DAO4.deletecart(cname)` (customer) or equivalent null-name delete  
- SQL: `DELETE FROM cart WHERE Name=?` (or `Name IS NULL` for guest)  
- Failure → redirect `paymentfail.jsp`

---

### FUREQ-005-07: Order Details Finalisation

**Source:** BUREQ-007-04  
**Description:** After cart clearance, the system shall update the newly inserted `order_details` rows with the current date. For guest orders, the customer name (N) is also set at this point.

**Implementation:**  
- Guest path: `DAO4.updateOrder_details(od)`  
  - SQL: `UPDATE order_details SET Date=?, Name=? WHERE Date IS NULL`  
- Customer path: `DAO4.updateOrder_details2(od)`  
  - SQL: `UPDATE order_details SET Date=? WHERE Date IS NULL`  
- Success → redirect `orders.jsp`  
- Failure → redirect `paymentfail.jsp`

---

### FUREQ-005-08: View Order History

**Source:** BUREQ-008-01, BUREQ-008-02  
**Description:** A logged-in customer shall be able to view all their orders as a list showing order ID, city, date, total price, and status.

**Implementation:**  
- JSP: `orders.jsp`  
- Customer identified by `cname` cookie  
- DAO: query `SELECT * FROM orders WHERE Customer_Name=?`  
- Entity: `com.entity.orders`

---

### FUREQ-005-09: View Order Line Items

**Source:** BUREQ-008-03  
**Description:** A customer shall be able to click on an order to view all line items (product name, brand, category, price, quantity, and subtotal).

**Implementation:**  
- JSP: `orderdetails.jsp?id=<date>`  
- DAO: `SELECT * FROM orders WHERE Date=?` + `SELECT * FROM order_details WHERE Date=?`  
- Note: Orders are linked to their details via the `Date` field, not a formal foreign key

---

### FUREQ-005-10: Order Cancellation — Customer

**Source:** BUREQ-009-01, BUREQ-009-03  
**Description:** A customer shall be able to remove an order by its ID. The orders page shall refresh after deletion.

**Implementation:**  
- Servlet: `com.servlet.removeorders` (`@WebServlet("/removeorders")`)  
- Parameter: `id` = `Order_Id`  
- DAO: `DAO4.removeorders(orders o)`  
- SQL: `DELETE FROM orders WHERE Order_Id=?`  
- Redirect: `orders.jsp`

---

### FUREQ-005-11: Order Cancellation — Admin

**Source:** BUREQ-009-02  
**Description:** An admin shall be able to remove any order from the admin order management view.

**Implementation:**  
- Servlet: `com.servlet.remove_orders` (`@WebServlet("/remove_orders")`)  
- DAO: `DAO4.removeorders(orders o)`  
- SQL: `DELETE FROM orders WHERE Order_Id=?`  
- Redirect: `table_orders.jsp`

---

## Order Entity Model

```mermaid
classDiagram
    class orders {
        +int Order_Id
        +String Customer_Name
        +String City
        +String Date
        +double Total_Price
        +String Status
    }
    class order_details {
        +int OD_Id
        +String Brand_name
        +String Cat_name
        +String Pro_name
        +double Price
        +int Qty
        +String Pro_image
        +String Date
        +String Customer_Name
    }
    orders "1" --> "*" order_details : Date (soft link - no FK)
```

---

## Checkout Sequence

```mermaid
sequenceDiagram
    participant Customer
    participant ShippingAddress2 as ShippingAddress2 Servlet
    participant payprocess as payprocess Servlet
    participant DAO4
    participant DB

    Customer->>ShippingAddress2: POST /ShippingAddress2 (CName, City, Total, CusName + cash/online)
    alt Cash on delivery
        ShippingAddress2-->>Customer: redirect confirmpayment.jsp?CName=...&City=...&Total=...&CusName=...
    else Online payment
        ShippingAddress2-->>Customer: redirect confirmonline.jsp?CName=...&City=...&Total=...&CusName=...
    end
    Customer->>payprocess: POST /payprocess (confirm)
    alt City or Total == "null"
        payprocess-->>Customer: redirect paymentfail.jsp (Select any item first)
    else City and Total present
        alt Guest (CusName == "empty")
            payprocess->>DAO4: checkcart() — SELECT * FROM cart WHERE Name IS NULL
        else Customer
            payprocess->>DAO4: checkcart2(N) — SELECT * FROM cart WHERE Name=?
        end
        alt Cart empty (returns false)
            payprocess-->>Customer: redirect paymentfail.jsp (Add items to cart first)
        else Cart has items
            payprocess->>DAO4: addOrders(o) — INSERT INTO orders (Customer_Name,Customer_City,Date,Total_Price,Status)
            DAO4->>DB: INSERT orders
            DB-->>DAO4: Success
            alt Guest
                payprocess->>DAO4: addOrder_details() — INSERT-SELECT WHERE Name IS NULL
            else Customer
                payprocess->>DAO4: addOrder_details2(N) — INSERT-SELECT WHERE Name=?
            end
            DAO4->>DB: INSERT INTO order_details ... SELECT * FROM cart (bulk)
            alt Guest
                payprocess->>DAO4: deletecart() — DELETE FROM cart WHERE Name IS NULL
            else Customer
                payprocess->>DAO4: deletecart2(N) — DELETE FROM cart WHERE Name=?
            end
            alt Guest
                payprocess->>DAO4: updateOrder_details(od) — UPDATE SET Date=?,Name=? WHERE Date IS NULL
            else Customer
                payprocess->>DAO4: updateOrder_details2(od) — UPDATE SET Date=? WHERE Date IS NULL
            end
            payprocess-->>Customer: redirect orders.jsp
        end
    end
```

---

## Known Limitations

- The four-step order creation is **not wrapped in a database transaction** — partial failure may leave orphaned order or order_detail records.
- Orders are linked to `order_details` via the `Date` field (soft link), not a proper foreign key — date collisions could cause incorrect joins.
- `orders.Status` is **always** `"Processing"` — no code in the application ever updates it. There are no shipped, delivered, or refunded transitions.
- Order cancellation is a **hard DELETE** from the `orders` table (`DELETE FROM orders WHERE Order_Id=?`), not a status change.
- Cancelling an order (`DELETE FROM orders`) does **not** delete associated `order_details` rows — these become orphan records with no parent order.
- `product.pquantity` (stock quantity) is **never decremented** when orders are placed. No `UPDATE` to the `product` table exists anywhere in the application; stock counts are effectively decorative.
