# NFUREQ-001: Security

**Non-Functional Requirement ID:** NFUREQ-001  
**Version:** 1.0  
**Category:** Security  
**Derived From:** Architecture constraints, known system design choices  
**Traced To Use Cases:** All UCs (cross-cutting concern)

---

## Overview

This document captures the security-related non-functional characteristics of the EcommerceApplication. These are observations about the current system state, not aspirational requirements. Where limitations exist, they are flagged as known risks.

---

## Current Security Characteristics

### NFUREQ-001-01: Cookie-Based Authentication

**Description:** Authentication state is maintained exclusively through browser cookies, with no server-side session tracking.

**Implementation:**  
- `cname` cookie (customer email) and `tname` cookie (admin username) are the sole identity tokens  
- `maxAge=9999` seconds (~2.78 hours) — persistent across browser sessions  
- No `HttpOnly` flag — accessible via JavaScript (XSS vulnerability)  
- No `Secure` flag — transmitted over HTTP (network interception risk)  
- Cookies are forgeable — any value can be set by the client

**Risk Level:** HIGH — cookies are the only auth mechanism and are not protected

---

### NFUREQ-001-02: Plaintext Password Storage

**Description:** Customer and admin passwords are stored and compared in plaintext.

**Implementation:**  
- `customer.Password` column stores passwords as-is  
- `usermaster.password` column stores admin passwords as-is  
- No hashing (bcrypt, SHA, etc.) applied anywhere in the codebase

**Risk Level:** CRITICAL — database exposure reveals all credentials

---

### NFUREQ-001-03: SQL Injection Prevention

**Description:** All database queries use `PreparedStatement` with parameterised placeholders.

**Implementation:**  
- `DAO`, `DAO2`, `DAO3`, `DAO4`, `DAO5` — all use `conn.prepareStatement(sql)` with `setString()` / `setInt()` / `setDouble()` parameter binding  
- No `Statement.execute(rawSql)` patterns identified  

**Risk Level:** LOW — SQL injection is mitigated by consistent PreparedStatement use

---

### NFUREQ-001-04: File Upload Security

**Description:** Image uploads are validated by file extension and size but not by content (MIME type inspection).

**Implementation:**  
- `MyUtilities.UploadFile()` — validates extension against: `.jpg`, `.bmp`, `.jpeg`, `.png`, `.webp`  
- Maximum size: 10 MB  
- The original filename is used as-is (no sanitisation) — path traversal risk if attacker controls the filename  
- No content-based MIME type check (only extension-based)

**Risk Level:** MEDIUM — extension validation is present but insufficient alone

---

### NFUREQ-001-05: Admin Access Control

**Description:** Admin routes are gated by the presence of the `tname` cookie, checked in JSPs via scriptlets. No server-side enforcement exists at the servlet layer.

**Implementation:**  
- JSPs check `<%if(tname == null){ response.sendRedirect("adminlogin.jsp"); return; }%>`  
- Servlets themselves do not verify admin status — only JSPs gate navigation  
- Direct POST to admin servlets (e.g., `/deletecustomer`, `/addproduct`) is not authenticated at the servlet level

**Risk Level:** MEDIUM-HIGH — admin servlets accept unauthenticated direct requests

---

### NFUREQ-001-06: Unauthenticated Admin-Only Endpoints (Zero Server-Side Validation)

**Description:** Eight admin-only servlet endpoints perform no server-side cookie validation whatsoever. Any HTTP client can invoke them directly without supplying the `tname` cookie.

**Affected Endpoints:**  
| Servlet URL | Operation |
|---|---|
| `/addproduct` | Add a new product to catalogue |
| `/deletecustomer` | Delete a customer account |
| `/remove_orders` | Admin delete of any order |
| `/remove_contactus` | Delete a contact enquiry |
| `/removetable_cart` | Clear all cart records |
| `/removetable_order_details` | Clear all order_details records |
| `/removecarta` | Remove a specific admin cart entry |
| `/removecartnulla` | Remove guest cart entries |

**Risk Level:** CRITICAL (for an internet-facing deployment) — unauthenticated HTTP requests can permanently delete data or alter product catalogue.

---

### NFUREQ-001-07: No CSRF Protection

**Description:** No Cross-Site Request Forgery (CSRF) tokens are used on any form.

**Risk Level:** HIGH — authenticated actions (checkout, delete customer, add product) can be triggered by malicious cross-origin requests

---

## Security Summary Table

| Concern | Status | Risk |
|---|---|---|
| SQL Injection | Mitigated (PreparedStatement) | LOW |
| Password Storage | Plaintext | CRITICAL |
| Cookie Auth Flags | Missing HttpOnly/Secure | HIGH |
| Cookie Forgery | Not prevented | HIGH |
| CSRF Protection | Not implemented | HIGH |
| File Upload (extension) | Validated | LOW |
| File Upload (content) | Not validated | MEDIUM |
| Admin servlet auth (JSP-gated) | Not enforced at servlet layer | MEDIUM-HIGH |
| 8 admin endpoints with zero auth | No validation at all | CRITICAL |
