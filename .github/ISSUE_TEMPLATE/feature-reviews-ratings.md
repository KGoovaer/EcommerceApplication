---
name: "Feature: Product Reviews & Ratings System"
about: Allow customers to leave star ratings and text reviews on product pages
title: "feat: add product reviews and ratings system"
labels: enhancement
---

## Description

Product pages (`selecteditem.jsp`) currently display only product details (brand, category, name, price, quantity, image). There is no mechanism for customers to leave feedback or rate a product.

## Motivation / Problem Solved

Reviews and star ratings are a key trust signal in e-commerce. They help prospective buyers make informed purchase decisions, provide the business with direct product feedback, and increase time-on-page engagement. An admin moderation view prevents abuse of the system.

## Proposed Implementation

**Database — new table:**
```sql
CREATE TABLE review (
    review_id     INTEGER PRIMARY KEY AUTOINCREMENT,
    customer_name TEXT    NOT NULL,
    product_image TEXT    NOT NULL,
    rating        INTEGER NOT NULL CHECK(rating BETWEEN 1 AND 5),
    review_text   TEXT,
    date          TEXT    DEFAULT (date('now'))
);
```

**New files:**
- `EcommerceApp/src/main/java/com/entity/Review.java` — bean with fields: `reviewId`, `customerName`, `productImage`, `rating`, `reviewText`, `date`.
- `EcommerceApp/src/main/java/com/dao/ReviewDAO.java` — methods: `addReview(Review r)`, `getReviewsByProduct(String productImage)`, `deleteReview(int reviewId)`, `getAverageRating(String productImage)`. Follows the same `DBConnect.getConn()` construction pattern as existing DAOs.
- `EcommerceApp/src/main/java/com/servlet/ReviewServlet.java` — mapped to `/review`; handles `POST` (add review) and `GET /deletereview` (admin delete).
- `EcommerceApp/src/main/webapp/table_reviews.jsp` — admin page listing all reviews with a delete action, following the table layout of `table_orders.jsp`.

**Modified files:**
- `EcommerceApp/src/main/webapp/selecteditem.jsp` — add a review submission form (visible only when the `cname` cookie is set) and a reviews list section beneath the product details. Display the average star rating.

## Acceptance Criteria

- [ ] A logged-in customer can submit a rating (1–5 stars) and optional text review on any product page.
- [ ] Guest users can read reviews but cannot submit one; a "Log in to write a review" prompt is shown instead.
- [ ] The average star rating is displayed as a visual indicator (e.g., filled/empty stars) on the product page.
- [ ] All reviews for the product are listed with reviewer name, star rating, date, and review text.
- [ ] An admin can view all reviews in `table_reviews.jsp` and delete any individual review.
- [ ] A customer cannot submit more than one review per product; the servlet rejects duplicate submissions.
- [ ] The `rating` value is validated to be between 1 and 5 on the server side.
- [ ] SQL queries in `ReviewDAO` use `PreparedStatement` — no raw string concatenation.
