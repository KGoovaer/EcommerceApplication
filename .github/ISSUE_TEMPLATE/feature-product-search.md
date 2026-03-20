---
name: "Feature: Product Search Functionality"
about: Add a keyword search bar so customers can find products without browsing categories
title: "feat: add product search bar with keyword and filter support"
labels: enhancement
---

## Description

Currently, users can only browse products by navigating through category pages (TV, Laptop, Mobile, Watch) or viewing all products. There is no search functionality, which makes it difficult to locate a specific item quickly.

## Motivation / Problem Solved

A search bar is a fundamental e-commerce UX requirement. Without it, customers must manually click through multiple category pages to find a product, resulting in a poor shopping experience and higher bounce rates.

## Proposed Implementation

The following files in the codebase will need to be created or modified:

**New files:**
- `EcommerceApp/src/main/java/com/servlet/SearchServlet.java` — mapped to `/search`; reads the `q`, `brand`, `category`, `minPrice`, and `maxPrice` query parameters and forwards results to `search_results.jsp`.
- `EcommerceApp/src/main/webapp/search_results.jsp` — displays matching products in the existing card-grid layout used by category pages (e.g., `mobile.jsp`).
- `EcommerceApp/src/main/java/com/dao/DAOSearch.java` *(optional)* — dedicated DAO for search queries, or alternatively add a `searchProducts()` method directly to `com.dao.DAO2`.

**Modified files:**
- `EcommerceApp/src/main/webapp/navbar.jsp` — add a search `<form>` with a text input that `GET`s `/search?q=...`.
- `EcommerceApp/src/main/webapp/customer_navbar.jsp` — same search bar for logged-in customer view.
- `EcommerceApp/src/main/webapp/admin_navbar.jsp` — same search bar for admin view.

**Database:**
No schema changes required. Queries will target the existing `viewlist` view using parameterised `LIKE` clauses on `Bname`, `Cname`, and `Pname` columns.

```sql
SELECT * FROM viewlist
WHERE (Bname LIKE ? OR Cname LIKE ? OR Pname LIKE ?)
  AND (? IS NULL OR Bname = ?)
  AND (? IS NULL OR Cname = ?)
  AND (Pprice BETWEEN ? AND ?)
```

## Acceptance Criteria

- [ ] A search bar is visible in the navbar on all pages (guest, customer, and admin views).
- [ ] Submitting a search query navigates to a results page with matching products.
- [ ] Search matches on product name, brand name, and category name (case-insensitive).
- [ ] Searching for a term with no matches displays a friendly "No products found" message.
- [ ] Optional brand, category, and price-range query parameters correctly narrow the results.
- [ ] The implementation uses `PreparedStatement` with parameterised `LIKE` bindings — no raw string concatenation.
- [ ] The search bar submits via `GET` so results pages are bookmarkable/shareable.
