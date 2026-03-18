# NFUREQ-002: Performance, Concurrency and Data Integrity

**Non-Functional Requirement ID:** NFUREQ-002  
**Version:** 1.0  
**Category:** Performance / Concurrency / Data Integrity  
**Derived From:** Architecture constraints  
**Traced To Use Cases:** All UCs (cross-cutting concern)

---

## Overview

This document describes non-functional constraints related to system performance, concurrency handling, and data integrity. Due to the use of a static SQLite connection and the absence of database transactions, there are significant limitations in all three areas.

---

## NFUREQ-002-01: Database Connection Management

**Description:** The system uses a single static `Connection` object shared across all requests.

**Implementation:**  
- `com.conn.DBConnect.getConn()` — returns a static `Connection` field  
- Each DAO is instantiated per-request with `new DAOx(DBConnect.getConn())`  
- SQLite (via `org.xerial:sqlite-jdbc`) is used as the database engine  
- `Class.forName("com.mysql.cj.jdbc.Driver")` is called in `DBConnect` as legacy code but the connection URL points to SQLite

**Constraints:**  
- The static connection is **not thread-safe** — concurrent requests share a single `Connection`  
- SQLite itself is file-based and does not support the same concurrency model as client-server RDBMS  
- Under concurrent load, database operations may produce race conditions or lock contention errors

**Risk Level:** HIGH for production multi-user load; LOW for demo/single-user usage

---

## NFUREQ-002-02: Absence of Database Transactions

**Description:** The order creation process (FL-015) performs four sequential database operations without a wrapping transaction.

**Affected Flow:** Checkout / FL-015 (`payprocess` servlet)

**Operations in sequence:**
1. `INSERT INTO orders` — create order header  
2. `INSERT INTO order_details` — one insert per cart item  
3. `DELETE FROM cart` — clear the cart  
4. `UPDATE order_details` — set date and customer name  

**Risk:**  
- A failure at step 2, 3, or 4 will leave partial records (orphaned order or order_details rows)  
- No rollback mechanism is in place  
- The system redirects to `paymentfail.jsp` on failure but does not clean up partial data

**Risk Level:** HIGH — data corruption possible during concurrent or failed checkouts

---

## NFUREQ-002-03: Response Time Expectations

**Description:** As a demo/learning application, no formal SLA or response time targets are defined.

**Observations:**  
- SQLite file I/O performance is acceptable for single-user or low-concurrency usage  
- No caching layer exists — all product catalogue queries hit the database on every page load  
- No connection pooling (e.g., DBCP, HikariCP) — each request reuses the static connection

---

## NFUREQ-002-04: Order-to-Details Referential Integrity

**Description:** Orders and their line items are linked via the `Date` field, not a formal foreign key.

**Implementation:**  
- `orders.Date` matches `order_details.Date` for the join  
- SQLite does not enforce foreign key constraints unless explicitly enabled (`PRAGMA foreign_keys = ON`)  
- Cascade deletes are not configured — cancelling an order (`DELETE FROM orders`) leaves orphaned `order_details` rows

**Risk Level:** MEDIUM — data drift over time; orphaned order_details not shown to users but occupy storage

---

## NFUREQ-002-05: File System Dependency

**Description:** Product image files are stored on the local file system of the application server.

**Implementation:**  
- Upload path is hardcoded in `DAO.java`  
- Image files must exist at the configured path for product images to render in JSPs  
- No external object storage (S3, CDN) is used

**Constraints:**  
- Not suitable for horizontally scaled deployments (multiple Tomcat instances would not share the file system)  
- Manual backup required for image files; database backup alone is insufficient

---

## Data Integrity Summary

| Concern | Status |
|---|---|
| Foreign Keys | Not enforced (SQLite, no PRAGMA) |
| Transaction Support | Not used |
| Cascade Deletes | Not configured |
| Order-Detail Linkage | Via Date field (soft link) |
| Concurrent Connection Safety | Not thread-safe |
| Image File Integrity | Filename-based, no checksums |
