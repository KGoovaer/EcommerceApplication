---
name: "Feature: Wishlist / Save for Later"
about: Allow customers to save products to a wishlist and move them to cart when ready
title: "feat: add wishlist / save for later functionality"
labels: enhancement
---

## Description

The only product interaction available to logged-in customers is "Add to Cart." There is no way to bookmark or save a product for later consideration without immediately committing to purchase.

## Motivation / Problem Solved

A wishlist reduces friction by letting customers build a personalised list of items they plan to buy in the future. It is a standard e-commerce feature that improves return visits, supports gift-list use cases, and can increase average order value when customers return to purchase saved items.

## Proposed Implementation

**Database — new table:**
```sql
CREATE TABLE wishlist (
    id            INTEGER PRIMARY KEY AUTOINCREMENT,
    customer_name TEXT    NOT NULL,
    bname         TEXT,
    cname         TEXT,
    pname         TEXT,
    pprice        REAL,
    pimage        TEXT,
    date_added    TEXT    DEFAULT (date('now'))
);
```

**New files:**
- `EcommerceApp/src/main/java/com/entity/Wishlist.java` — bean with fields: `id`, `customerName`, `bname`, `cname`, `pname`, `pprice`, `pimage`, `dateAdded`.
- `EcommerceApp/src/main/java/com/dao/WishlistDAO.java` — methods: `addToWishlist(Wishlist w)`, `removeFromWishlist(int id, String customerName)`, `getWishlistByCustomer(String customerName)`, `isInWishlist(String customerName, String pimage)`. Follows the same `DBConnect.getConn()` construction pattern as existing DAOs.
- `EcommerceApp/src/main/java/com/servlet/WishlistServlet.java` — mapped to `/wishlist`; handles `GET` (view list), `POST /wishlist?action=add` (add item), `POST /wishlist?action=remove` (remove item), and `POST /wishlist?action=movetocart` (move item to cart by also inserting into the cart table).
- `EcommerceApp/src/main/webapp/wishlist.jsp` — displays the customer's saved products in a card layout matching `selecteditem.jsp`, with "Move to Cart" and "Remove" buttons per item.

**Modified files:**
- `EcommerceApp/src/main/webapp/selecteditem.jsp` — add a "♡ Add to Wishlist" button next to the existing "Add to Cart" button. When the product is already in the wishlist (check via `WishlistDAO.isInWishlist()`), render a filled heart ("♥") instead and link it to the wishlist page.
- `EcommerceApp/src/main/webapp/customer_navbar.jsp` — add a wishlist icon (e.g., `♡`) with an item-count badge, following the same pattern as the existing cart count badge.

## Acceptance Criteria

- [ ] A logged-in customer can add any product to their wishlist from the product details page (`selecteditem.jsp`).
- [ ] The "♡ Add to Wishlist" button changes appearance (filled heart / different label) when the product is already in the wishlist.
- [ ] The customer navbar shows a wishlist icon with the current item count.
- [ ] `wishlist.jsp` lists all saved products with image, name, price, and action buttons.
- [ ] "Move to Cart" adds the item to the shopping cart and simultaneously removes it from the wishlist.
- [ ] "Remove" deletes the item from the wishlist without adding it to the cart.
- [ ] Guest users attempting to use wishlist features are redirected to the login page.
- [ ] Duplicate entries (same product for the same customer) are prevented; the servlet returns a meaningful flash-cookie message.
- [ ] SQL queries in `WishlistDAO` use `PreparedStatement` — no raw string concatenation.
- [ ] The `removeFromWishlist` method verifies ownership (`customer_name` matches) before deleting, preventing users from removing each other's items.
