---
name: "Feature: Password Hashing & Security Improvements"
about: Replace plain-text password storage with secure hashing for customer and admin accounts
title: "feat: implement password hashing for customer and admin accounts"
labels: enhancement
---

## Description

Passwords are currently stored in plain text in both the `customer` and `usermaster` database tables (e.g., the seed data inserts `'admin'` as the admin password). The `customer.Password` column is defined as `varchar(20)`, which is far too short to store any hashed value. This is a critical security vulnerability.

## Motivation / Problem Solved

Plain-text password storage violates basic security principles. If the SQLite database file is ever exposed, every user account is immediately compromised. Hashing passwords with a strong algorithm ensures credentials remain protected even in the event of a database breach. This change is a prerequisite for any production deployment of the application.

## Proposed Implementation

**New files:**
- `EcommerceApp/src/main/java/com/utility/PasswordUtil.java` — a utility class exposing `hashPassword(String plainText)` and `checkPassword(String plainText, String hash)` methods using BCrypt (preferred) or `java.security.MessageDigest` (SHA-256 with salt as a lighter-weight alternative).

**Modified files:**
- `EcommerceApp/src/main/java/com/servlet/AddCustomerServlet.java` — call `PasswordUtil.hashPassword()` before passing the password to the DAO insert method.
- `EcommerceApp/src/main/java/com/servlet/CheckCustomerServlet.java` — replace the direct equality check with `PasswordUtil.checkPassword()`.
- `EcommerceApp/src/main/java/com/servlet/CheckAdminServlet.java` — same change as above for admin login.
- `EcommerceApp/src/main/java/com/dao/DAO.java` — ensure the `addCustomer` method does not truncate the password field (column must be widened first).

**Database migration:**
- Alter `customer.Password` to `varchar(255)`.
- Run a one-time migration that reads each plain-text password, hashes it, and writes the hash back.
- Update the admin seed record in `usermaster` with its hashed equivalent.

**Input validation:**
- Enforce a minimum password length of 8 characters on the server side in `AddCustomerServlet`.
- Return a meaningful error message (via a flash cookie, matching the existing pattern) when validation fails.

## Acceptance Criteria

- [ ] New customer registrations store a hashed password — no plain-text password is ever written to the database.
- [ ] Customer login works correctly by verifying the supplied password against the stored hash.
- [ ] Admin login works correctly after the admin password is migrated/hashed.
- [ ] The `customer.Password` column accepts at least 255 characters.
- [ ] A migration utility/script hashes existing plain-text passwords without requiring users to reset them.
- [ ] Password fields enforce a minimum length of 8 characters with a meaningful server-side error.
- [ ] No plain-text password appears in any log output, cookie value, or HTTP response.
- [ ] The `PasswordUtil` helper is reusable and not tightly coupled to any single servlet.
