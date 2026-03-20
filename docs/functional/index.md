# Functional Documentation Index

**Module:** ECOMMERCE_APPLICATION  
**Version:** 1.0  
**Generated:** 2026-03-18  
**Phase:** Technical  

---

## Overview

This index covers all functional and technical documentation derived from the business requirements phase. Each document maps directly to business use cases (UC) and requirements (BUREQ), providing full traceability from business intent to implementation.

---

## Functional Requirements

| ID | Document | Description | Traced To |
|---|---|---|---|
| FUREQ-001 | [Customer Registration](requirements/FUREQ-001-customer-registration.md) | Input capture, uniqueness validation, account persistence, cart continuation | UC-001, BP-001 |
| FUREQ-002 | [Authentication](requirements/FUREQ-002-authentication.md) | Customer login (cname cookie), admin login (tname cookie), error handling | UC-002, UC-003, BP-001 |
| FUREQ-003 | [Product Catalogue](requirements/FUREQ-003-product-catalogue.md) | Browse all products, category filtering, product detail, admin add product with image upload | UC-004, UC-011, BP-002, BP-004 |
| FUREQ-004 | [Shopping Cart](requirements/FUREQ-004-shopping-cart.md) | Add to cart (customer/guest), duplicate detection, quantity increment, remove items, admin cart management | UC-005, UC-006, BP-002 |
| FUREQ-005 | [Order Management](requirements/FUREQ-005-order-management.md) | Shipping address, payment routing, 4-step order creation, view history, view details, order cancellation | UC-007, UC-008, UC-009, BP-003 |
| FUREQ-006 | [Contact Us & Admin Ops](requirements/FUREQ-006-contact-admin-ops.md) | Contact enquiry submission, admin dashboard, customer management, enquiry management, data tables | UC-010, UC-012, UC-013, UC-014, BP-005, BP-006 |

---

## Non-Functional Requirements

| ID | Document | Category | Key Concerns |
|---|---|---|---|
| NFUREQ-001 | [Security](requirements/NFUREQ-001-security.md) | Security | Cookie auth, plaintext passwords, SQL injection prevention, file upload validation, CSRF |
| NFUREQ-002 | [Performance & Data Integrity](requirements/NFUREQ-002-performance-integrity.md) | Performance / Integrity | Static DB connection (not thread-safe), no transactions, order-detail soft link, file system dependency |

---

## Functional Flows

| ID | Document | Description | Servlets Covered |
|---|---|---|---|
| FF-001 | [Registration & Onboarding](flows/FF-001-registration-onboarding.md) | End-to-end: guest registration → login → optional cart continuation | `addcustomer`, `checkcustomer` |
| FF-002 | [Catalogue & Cart Management](flows/FF-002-catalogue-cart-flow.md) | Browse products, JSP tripling pattern, add to cart, remove from cart | `addtocart`, `addtocartnull`, `removecart`, `removecartnull` |
| FF-003 | [Checkout & Order Processing](flows/FF-003-checkout-order-flow.md) | Shipping address routing, 4-step order creation, view history, cancellation | `ShippingAddress2`, `payprocess`, `removeorders`, `remove_orders` |
| FF-004 | [Admin Operations](flows/FF-004-admin-operations-flow.md) | Admin login, dashboard, add product, manage customers, enquiries, data tables | `checkadmin`, `addproduct`, `deletecustomer`, `remove_contactus`, `removetable_*` |

---

## Integration Documentation

| ID | Document | Type | Technology |
|---|---|---|---|
| INT-001 | [SQLite Database](integration/INT-001-database.md) | Relational Database | SQLite via `org.xerial:sqlite-jdbc`, static `DBConnect` singleton |
| INT-002 | [File System — Image Uploads](integration/INT-002-file-system.md) | Local File System | `@MultipartConfig`, `MyUtilities.UploadFile()` |

---

## Architecture Diagrams

| ID | Document | Diagram Types |
|---|---|---|
| DIAG-001 | [System Architecture](diagrams/DIAG-001-system-architecture.md) | C4 Context, Component Architecture, Servlet URL Map |

---

## BUREQ → FUREQ Traceability Matrix

| BUREQ ID | Use Case | FUREQ |
|---|---|---|
| BUREQ-001-01 to 04 | UC-001 | FUREQ-001 |
| BUREQ-002-01 to 04 | UC-002 | FUREQ-002 |
| BUREQ-003-01 to 03 | UC-003 | FUREQ-002 |
| BUREQ-004-01 to 04 | UC-004 | FUREQ-003 |
| BUREQ-005-01 to 04 | UC-005 | FUREQ-004 |
| BUREQ-006-01 to 04 | UC-006 | FUREQ-004 |
| BUREQ-007-01 to 06 | UC-007 | FUREQ-005 |
| BUREQ-008-01 to 03 | UC-008 | FUREQ-005 |
| BUREQ-009-01 to 03 | UC-009 | FUREQ-005 |
| BUREQ-010-01 to 03 | UC-010 | FUREQ-006 |
| BUREQ-011-01 to 04 | UC-011 | FUREQ-003 |
| BUREQ-012-01 to 03 | UC-012 | FUREQ-006 |
| BUREQ-013-01 to 03 | UC-013 | FUREQ-006 |
| BUREQ-014-01 to 02 | UC-014 | FUREQ-006 |

---

## Key Design Decisions

| Decision | Description | Risk |
|---|---|---|
| Cookie-only auth | Sessions managed via browser cookies only (no `HttpSession`) | HIGH — forgeable, no HttpOnly/Secure |
| Static DB connection | Single shared `Connection` in `DBConnect` | HIGH — not thread-safe |
| No DB transactions | Order creation is 4 sequential operations without transaction | HIGH — partial failures possible |
| Image as surrogate key | `Pro_image` filename used as product identifier in cart/order | MEDIUM — fragile coupling |
| JSP tripling pattern | Each page has 3 variants for guest/customer/admin roles | LOW — maintainability concern |
| Plaintext passwords | Passwords stored and compared without hashing | CRITICAL — security risk |
