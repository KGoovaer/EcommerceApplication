# Gap Report — EcommerceApp Documentation Verification

**Document ID:** GAP-001  
**Version:** 2.0 (Re-verification after remediation)  
**Phase:** Verification  
**Generated:** 2026-03-18  
**Verification Scope:** All documentation artifacts against `EcommerceApp/src/main/java/` and `EcommerceApp/src/main/webapp/`  

---

## Executive Summary

### Re-verification Result (v2.0)

| Severity | Count (v1.0) | Resolved | Remaining |
|---|---|---|---|
| 🔴 Critical | 3 | ✅ 3 | 0 |
| 🟠 High | 3 | ✅ 3 | 0 |
| 🟡 Medium | 2 | ✅ 2 | 0 |
| 🟢 Low | 3 | 0 | 3 |

**Overall Verdict: PASS** — All critical, high, and medium gaps from the initial verification have been resolved by the technical-documenter remediation pass and the subsequent coordination pass. Three low-severity items remain as accepted documentation drift.

### Previous Verdict (v1.0)
**Overall Verdict: FAIL** — Critical inaccuracies in `FF-003` (Checkout and Order Processing Flow) described DAO methods that did not exist and an UPDATE condition that was wrong.

---

## Resolved Gaps (v1.0 → v2.0)

All critical, high, and medium gaps from v1.0 have been resolved in the `doc-technical-documenter` remediation pass and subsequent `doc-coordinator` coordination pass.

### GAP-C01 — FF-003: Wrong DAO4 Method Names ✅ RESOLVED

**Severity:** 🔴 Critical → ✅ Resolved  
**Resolution:** `FF-003` now correctly documents that `payprocess` uses `DAO4.checkcart()` / `DAO4.checkcart2(N)` (boolean checks), and that `Total` is passed in as a JSP request parameter.

---

### GAP-C02 — FF-003: Wrong order_details INSERT ✅ RESOLVED

**Severity:** 🔴 Critical → ✅ Resolved  
**Resolution:** `FF-003` Step 5 now correctly shows the bulk INSERT-SELECT pattern: `INSERT INTO order_details(...) SELECT * FROM cart WHERE Name IS NULL/Name=?`. Per-item loop documentation has been removed.

---

### GAP-C03 — FF-003: Wrong UPDATE Condition ✅ RESOLVED

**Severity:** 🔴 Critical → ✅ Resolved  
**Resolution:** `FF-003` Step 7 now correctly shows `UPDATE order_details SET Date=?, Name=? WHERE Date IS NULL` (guest) and `UPDATE order_details SET Date=? WHERE Date IS NULL` (customer).

---

### GAP-H01 — FF-003: ShippingAddress2 Wrong Form Fields ✅ RESOLVED

**Severity:** 🟠 High → ✅ Resolved  
**Resolution:** `FF-003` Part 1 now correctly documents the 5 actual parameters: `CName`, `City`, `Total`, `CusName`, `cash`/`online`. Removed non-existent fields (`Address`, `State`, `Country`, `Pincode`).

---

### GAP-H02 — Admin Endpoints: No Server-Side Auth ✅ RESOLVED

**Severity:** 🟠 High → ✅ Resolved  
**Resolution:** `NFUREQ-001` now includes section `NFUREQ-001-06` explicitly listing all 8 admin endpoints that perform zero server-side cookie validation, with CRITICAL risk rating.

---

### GAP-H03 — Order Status Machine Incomplete ✅ RESOLVED

**Severity:** 🟠 High → ✅ Resolved  
**Resolution:** `FUREQ-005` and `DC-007` now explicitly document: (1) `Status` is always `"Processing"` — never updated, (2) cancellation is a hard DELETE, (3) `order_details` are orphaned on cancellation. `FUREQ-005` Known Limitations section covers all three points.

---

### GAP-M01 — `viewlist` Documented as Table ✅ RESOLVED

**Severity:** 🟡 Medium → ✅ Resolved  
**Resolution:** `DC-010` now explicitly labels `viewlist` as a **DATABASE VIEW** (not a base table) and notes it joins `product`, `brand`, and `category`.

---

### GAP-M02 — No Stock Decrement Documentation ✅ RESOLVED

**Severity:** 🟡 Medium → ✅ Resolved  
**Resolution:** `DC-001` and `FUREQ-005` both include explicit notes that `product.pquantity` is never decremented when orders are placed.

---

## Remaining Low-Severity Gaps

The following low-severity gaps remain. They do not affect documentation correctness for primary flows and are accepted for this release.

### GAP-L01 — Undocumented JSP Pages: `cartnullqty.jsp`, `passc.jsp`, `z1.jsp`, `z2.jsp`

**Severity:** 🟢 Low  
**Affected Documents:** `docs/discovered-flows.md`, `docs/index.md`  
**Responsible Agent:** discovery  

**Description:**  
Four JSP pages in `src/main/webapp/` are not referenced in any documented flow:
- `cartnullqty.jsp` — appears to be a guest cart quantity edge-case page
- `passc.jsp` — undetermined purpose (possibly password-related)
- `z1.jsp`, `z2.jsp` — undetermined purpose (possibly scratch/test pages)

**Remediation:** Minor — investigate and add a brief note in the discovery artifacts or explicitly exclude as unreachable/unused pages.

---

### GAP-L02 — `addContactusc` Servlet Not Highlighted as Separate Use Case

**Severity:** 🟢 Low  
**Affected Document:** `docs/business/use-cases/UC-010-submit-contact-enquiry.md`  
**Responsible Agent:** business-documenter  

**Description:**  
`FL-019` (Contact Us Submission — Customer) is discovered and documented at the flow level. However, `UC-010` focuses on guest enquiries. The customer-facing variant (`/addContactusc` → `cupassc.jsp`/`cufailc.jsp`) has a different redirect outcome but identical data processing. The UC does not differentiate the two actors sufficiently.

**Remediation:** Minor — add a note to UC-010 that registered customers use `/addContactusc` with outcomes redirecting to `cupassc.jsp`/`cufailc.jsp`.

---

### GAP-L03 — `DAO.addproduct()` Uses `System.getenv("UPLOAD_PATH")` — Not Documented

**Severity:** 🟢 Low  
**Affected Document:** `docs/functional/integration/INT-002-file-system.md`  
**Responsible Agent:** technical-documenter  

**Description:**  
`DAO.addproduct()` resolves the upload base path via `System.getenv("UPLOAD_PATH")` with fallback to empty string. This environment variable is not mentioned in `INT-002-file-system.md`, which only documents the hardcoded path concern from `DAO.java` (the original version).

**Remediation:** Update `INT-002` to document the `UPLOAD_PATH` environment variable as the configurable image upload path.

---

## Resolution Summary

All critical and high gaps from v1.0 have been resolved. The documentation pipeline is complete. Three low-severity gaps remain as accepted documentation drift for future improvement.
