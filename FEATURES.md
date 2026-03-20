# EcommerceApplication — Features Roadmap

This document outlines 5 proposed new features for the EcommerceApplication. Each feature has been identified by analysing the current codebase and identifying gaps in functionality, security, and user experience.

---

## Feature 1: Product Search Functionality

### Description
Currently, users can only browse products by navigating category pages (TV, Laptop, Mobile, Watch) or viewing all products. There is no search capability, which makes it difficult for users to find a specific product quickly.

### Rationale
A search bar is a fundamental e-commerce UX requirement. Without it, customers must manually click through multiple category pages to locate a product, leading to a poor shopping experience and increased bounce rates.

### Proposed Implementation
- Add a search bar to `EcommerceApp/src/main/webapp/navbar.jsp` (and its customer/admin variants).
- Create `com.servlet.SearchServlet` mapped to `/search` that accepts a `q` query parameter.
- Add a `searchProducts(String keyword)` method in `com.dao.DAO2` (or a new `DAOSearch.java`) that queries the `viewlist` database view using `LIKE` clauses on `Bname`, `Cname`, `Pname`, and price columns.
- Create `EcommerceApp/src/main/webapp/search_results.jsp` to display matching products in the same card-grid layout used by existing category pages.
- Support optional filters for brand, category, and price range via additional query parameters.

### Acceptance Criteria
- [ ] A search bar is visible in the navbar on all pages (guest, customer, and admin views).
- [ ] Submitting a search query returns a results page with matching products.
- [ ] Search matches on product name, brand name, and category name (case-insensitive).
- [ ] Searching for a term with no matches shows a friendly "No products found" message.
- [ ] Optional brand, category, and price-range filters narrow the results correctly.
- [ ] The implementation uses `PreparedStatement` with parameterised `LIKE` bindings (no SQL injection risk).

---

## Feature 2: Password Hashing & Security Improvements

### Description
Passwords are currently stored in plain text in both the `customer` and `usermaster` database tables. The `customer.Password` column is defined as `varchar(20)`, which is too short for hashed values. This is a critical security vulnerability that exposes all user accounts if the database is compromised.

### Rationale
Plain-text password storage violates basic security principles. Hashing passwords with a strong algorithm (BCrypt or SHA-256) ensures that even if the database is leaked, user credentials remain protected. This is a prerequisite for any production deployment.

### Proposed Implementation
- Add a hashing utility class (`com.utility.PasswordUtil`) that wraps BCrypt (or `java.security.MessageDigest` for SHA-256).
- Update `com.servlet.AddCustomerServlet` to hash the password before inserting a new customer record.
- Update `com.servlet.CheckCustomerServlet` and `com.servlet.CheckAdminServlet` to compare the supplied password against the stored hash.
- Run a one-time migration script to hash all existing plain-text passwords in the database.
- Extend the `customer.Password` column to `varchar(255)` in the schema and migration script.
- Add server-side input validation (minimum length, disallowed characters) in both the servlet and the JSP form.

### Acceptance Criteria
- [ ] New customer registrations store a hashed password, never plain text.
- [ ] Customer login works correctly by verifying the supplied password against the stored hash.
- [ ] Admin login works correctly after passwords are migrated/hashed.
- [ ] The `customer.Password` column accepts at least 255 characters.
- [ ] A migration utility hashes existing plain-text passwords without requiring users to reset their passwords.
- [ ] Password fields enforce a minimum length of 8 characters on the server side.
- [ ] No plain-text password appears in any log output or cookie value.

---

## Feature 3: Product Reviews & Ratings System

### Description
Product pages (`selecteditem.jsp`) currently display only product details (brand, category, name, price, quantity, image). There is no mechanism for customers to leave feedback, which limits social proof and trust.

### Rationale
Reviews and ratings are a key trust signal in e-commerce. They help prospective buyers make informed decisions and give the business valuable feedback on product quality. An admin moderation view prevents abuse.

### Proposed Implementation
- Create a new `review` database table:
  ```sql
  CREATE TABLE review (
      review_id   INTEGER PRIMARY KEY AUTOINCREMENT,
      customer_name TEXT NOT NULL,
      product_image TEXT NOT NULL,
      rating      INTEGER CHECK(rating BETWEEN 1 AND 5),
      review_text TEXT,
      date        TEXT DEFAULT (date('now'))
  );
  ```
- Create `com.dao.ReviewDAO` with methods: `addReview(Review r)`, `getReviewsByProduct(String productImage)`, `deleteReview(int reviewId)`, `getAverageRating(String productImage)`.
- Create `com.entity.Review` bean with fields matching the table columns.
- Add a review submission form to `EcommerceApp/src/main/webapp/selecteditem.jsp`, visible only to logged-in customers (checked via the `cname` cookie).
- Display the average star rating and a list of individual reviews beneath the product details.
- Create `EcommerceApp/src/main/webapp/table_reviews.jsp` for admin review moderation (delete reviews).
- Create `com.servlet.ReviewServlet` to handle `POST /addreview` and `GET /deletereview` requests.

### Acceptance Criteria
- [ ] A logged-in customer can submit a rating (1–5 stars) and optional text review on any product page.
- [ ] Guest users see reviews but cannot submit one (a prompt to log in is shown instead).
- [ ] The average rating is displayed as a star graphic on the product page.
- [ ] All reviews for a product are listed with the reviewer's name and date.
- [ ] An admin can view all reviews in `table_reviews.jsp` and delete inappropriate ones.
- [ ] A customer cannot submit more than one review per product (enforced by the servlet).
- [ ] The `rating` field is validated to be between 1 and 5 on the server side.

---

## Feature 4: Customer Order History & Tracking Dashboard

### Description
The application has `orders` and `order_details` tables with a `Status` field, but customers have no way to view their past orders or check current status. Only admins can view orders through `table_orders.jsp`. This means customers must contact the business for any order information.

### Rationale
Providing customers with self-service order tracking reduces support overhead, improves satisfaction, and builds trust. Allowing cancellation of pending orders further reduces manual admin intervention.

### Proposed Implementation
- Create `EcommerceApp/src/main/webapp/customer_orders.jsp` that lists all orders for the logged-in customer (identified by the `cname` cookie value mapped to `Customer_Name`).
- Add `getOrdersByCustomer(String customerName)` and `getOrderDetailsByOrderId(int orderId)` methods to `com.dao.DAO3` (or a new `DAOOrders.java`).
- Display each order with: Order ID, date, status badge (Pending / Shipped / Delivered / Cancelled), item list, and total price.
- Add a "Cancel Order" button that is enabled only when `Status = 'Pending'`; the servlet updates the status to `'Cancelled'`.
- Add a "My Orders" link to `EcommerceApp/src/main/webapp/customer_navbar.jsp`.
- Create `com.servlet.CustomerOrdersServlet` mapped to `/customerorders` to handle `GET` (view) and `POST` (cancel) requests.

### Acceptance Criteria
- [ ] A logged-in customer can navigate to "My Orders" from the navbar.
- [ ] The page lists all past and current orders for that customer, sorted by date descending.
- [ ] Each order shows status with a colour-coded badge (e.g., yellow = Pending, blue = Shipped, green = Delivered, red = Cancelled).
- [ ] Order line items (product name, quantity, price) are displayed in an expandable or inline detail section.
- [ ] A "Cancel Order" button appears only for orders with `Status = 'Pending'`.
- [ ] Cancelling an order updates `Status` to `'Cancelled'` in the database and refreshes the page.
- [ ] Unauthenticated users are redirected to the login page when accessing `/customerorders`.

---

## Feature 5: Wishlist / Save for Later

### Description
The only product interaction available to customers is "Add to Cart." There is no way to save products for later consideration without committing to purchase, which is a common e-commerce expectation.

### Rationale
A wishlist reduces friction by letting customers curate a personalised list of items they intend to buy later. It also enables re-engagement (e.g., "items in your wishlist are on sale") and improves average order value over time.

### Proposed Implementation
- Create a new `wishlist` database table:
  ```sql
  CREATE TABLE wishlist (
      id           INTEGER PRIMARY KEY AUTOINCREMENT,
      customer_name TEXT NOT NULL,
      bname        TEXT,
      cname        TEXT,
      pname        TEXT,
      pprice       REAL,
      pimage       TEXT,
      date_added   TEXT DEFAULT (date('now'))
  );
  ```
- Create `com.dao.WishlistDAO` with methods: `addToWishlist(Wishlist w)`, `removeFromWishlist(int id)`, `getWishlistByCustomer(String customerName)`, `isInWishlist(String customerName, String pimage)`.
- Create `com.entity.Wishlist` bean with fields matching the table columns.
- Add a "♡ Add to Wishlist" button on `EcommerceApp/src/main/webapp/selecteditem.jsp` next to the existing "Add to Cart" button.
- Create `EcommerceApp/src/main/webapp/wishlist.jsp` showing the customer's saved items with "Move to Cart" and "Remove" actions.
- Add a wishlist icon with item count badge to `EcommerceApp/src/main/webapp/customer_navbar.jsp`, mirroring the existing cart badge pattern.
- Create `com.servlet.WishlistServlet` mapped to `/wishlist` to handle add, remove, and list operations.

### Acceptance Criteria
- [ ] A logged-in customer can add any product to their wishlist from the product details page.
- [ ] The "♡ Add to Wishlist" button changes state (e.g., filled heart) when the product is already in the wishlist.
- [ ] The customer navbar shows a wishlist icon with the current item count.
- [ ] The `wishlist.jsp` page lists all saved products with image, name, price, and action buttons.
- [ ] "Move to Cart" adds the item to the shopping cart and removes it from the wishlist in one action.
- [ ] "Remove" deletes the item from the wishlist.
- [ ] Guest users are prompted to log in when attempting to use wishlist functionality.
- [ ] Duplicate entries (same product for the same customer) are prevented by the servlet.
