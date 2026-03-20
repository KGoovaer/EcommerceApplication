---
name: "Feature: Customer Order History & Tracking Dashboard"
about: Let customers view their past orders and current order status without contacting admin
title: "feat: add customer order history and tracking dashboard"
labels: enhancement
---

## Description

The application already has `orders` and `order_details` tables that include a `Status` field, but customers currently have no way to view their past orders or check the status of an active order. Only administrators can access order data via `table_orders.jsp`.

## Motivation / Problem Solved

Providing self-service order tracking significantly reduces customer support overhead and improves satisfaction. Customers expect to see their order history and current status in any e-commerce application. Allowing cancellation of pending orders further removes the need for manual admin intervention.

## Proposed Implementation

**New files:**
- `EcommerceApp/src/main/java/com/servlet/CustomerOrdersServlet.java` — mapped to `/customerorders`; `GET` renders the order list, `POST` processes a cancel request. Redirects unauthenticated users (no `cname` cookie) to the login page.
- `EcommerceApp/src/main/webapp/customer_orders.jsp` — displays all orders for the logged-in customer in a table with status badges and an expandable line-item section. Follows the visual style of `table_orders.jsp`.

**Modified files:**
- `EcommerceApp/src/main/java/com/dao/DAO3.java` (or a new `DAOOrders.java`) — add `getOrdersByCustomer(String customerName)` and `cancelOrder(int orderId)` methods. The cancel method performs an `UPDATE orders SET Status='Cancelled' WHERE Order_Id=? AND Customer_Name=? AND Status='Pending'`.
- `EcommerceApp/src/main/webapp/customer_navbar.jsp` — add a "My Orders" navigation link pointing to `/customerorders`.

**Status badge colour scheme (Bootstrap classes):**

| Status    | Badge class          |
|-----------|----------------------|
| Pending   | `badge-warning`      |
| Shipped   | `badge-primary`      |
| Delivered | `badge-success`      |
| Cancelled | `badge-danger`       |

## Acceptance Criteria

- [ ] A logged-in customer can navigate to "My Orders" via the customer navbar.
- [ ] The page lists all past and current orders for that customer, sorted by date descending.
- [ ] Each order displays the Order ID, date, status badge, and a summary of line items.
- [ ] Status is colour-coded (Pending = yellow, Shipped = blue, Delivered = green, Cancelled = red).
- [ ] A "Cancel Order" button is shown only for orders with `Status = 'Pending'`.
- [ ] Clicking "Cancel Order" updates `Status` to `'Cancelled'` in the database and refreshes the list.
- [ ] Only the order owner can cancel their own order (enforced by matching `Customer_Name` in the `UPDATE` query).
- [ ] Unauthenticated users attempting to access `/customerorders` are redirected to the login page.
- [ ] SQL queries use `PreparedStatement` — no raw string concatenation.
