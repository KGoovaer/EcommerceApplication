# ID Registry — EcommerceApp Documentation

**Document ID:** IDR-001  
**Version:** 1.0  
**Phase:** Coordination  
**Purpose:** Central registry of all document IDs used across the documentation set. Ensures uniqueness and enables cross-reference validation.  

---

## Overview

This registry catalogs every identified document across all phases of the EcommerceApp documentation pipeline. IDs are guaranteed unique within each namespace. Use this registry to resolve cross-references and validate links.

---

## Execution Flow IDs (FL-*)

*Source: `docs/discovered-flows.md`*

| ID | Name | Document | Status |
|---|---|---|---|
| FL-001 | Customer Registration | `docs/discovered-flows.md#fl-001` | ✅ Documented |
| FL-002 | Customer Login | `docs/discovered-flows.md#fl-002` | ✅ Documented |
| FL-003 | Admin Login | `docs/discovered-flows.md#fl-003` | ✅ Documented |
| FL-004 | Add Product | `docs/discovered-flows.md#fl-004` | ✅ Documented |
| FL-005 | View Product Catalog | `docs/discovered-flows.md#fl-005` | ✅ Documented |
| FL-006 | Browse by Category | `docs/discovered-flows.md#fl-006` | ✅ Documented |
| FL-007 | Add to Cart (Customer) | `docs/discovered-flows.md#fl-007` | ✅ Documented |
| FL-008 | Add to Cart (Guest) | `docs/discovered-flows.md#fl-008` | ✅ Documented |
| FL-009 | Add to Cart (Admin View) | `docs/discovered-flows.md#fl-009` | ✅ Documented |
| FL-010 | Remove from Cart (Customer) | `docs/discovered-flows.md#fl-010` | ✅ Documented |
| FL-011 | Remove from Cart (Guest) | `docs/discovered-flows.md#fl-011` | ✅ Documented |
| FL-012 | Remove from Cart (Admin — Customer Cart) | `docs/discovered-flows.md#fl-012` | ✅ Documented |
| FL-013 | Remove from Cart (Admin — Guest Cart) | `docs/discovered-flows.md#fl-013` | ✅ Documented |
| FL-014 | Shipping Address Selection | `docs/discovered-flows.md#fl-014` | ✅ Documented |
| FL-015 | Payment Processing | `docs/discovered-flows.md#fl-015` | ✅ Documented |
| FL-016 | Remove Orders (Customer) | `docs/discovered-flows.md#fl-016` | ✅ Documented |
| FL-017 | Remove Orders (Admin) | `docs/discovered-flows.md#fl-017` | ✅ Documented |
| FL-018 | Contact Us Submission (Guest) | `docs/discovered-flows.md#fl-018` | ✅ Documented |
| FL-019 | Contact Us Submission (Customer) | `docs/discovered-flows.md#fl-019` | ✅ Documented |
| FL-020 | Admin Delete Customer | `docs/discovered-flows.md#fl-020` | ✅ Documented |
| FL-021 | Admin Remove Contact Us Entry | `docs/discovered-flows.md#fl-021` | ✅ Documented |
| FL-022 | Admin Manage Tables — Remove Cart Row | `docs/discovered-flows.md#fl-022` | ✅ Documented |
| FL-023 | Admin Manage Tables — Remove Order Detail Row | `docs/discovered-flows.md#fl-023` | ✅ Documented |
| FL-024 | View Orders (Customer) | `docs/discovered-flows.md#fl-024` | ✅ Documented |
| FL-025 | View Order Details | `docs/discovered-flows.md#fl-025` | ✅ Documented |
| FL-026 | Admin Home Dashboard | `docs/discovered-flows.md#fl-026` | ✅ Documented |
| FL-027 | Customer Home | `docs/discovered-flows.md#fl-027` | ✅ Documented |

---

## Component IDs (CP-*)

*Source: `docs/discovered-components.md`*

| ID | Component Name | Type | Package | Status |
|---|---|---|---|---|
| CP-001 | ShippingAddress2 | Servlet | `com.servlet.ShippingAddress2` | ✅ Documented |
| CP-002 | addContactus | Servlet | `com.servlet.addContactus` | ✅ Documented |
| CP-003 | addContactusc | Servlet | `com.servlet.addContactusc` | ✅ Documented |
| CP-004 | addcustomer | Servlet | `com.servlet.addcustomer` | ✅ Documented |
| CP-005 | addproduct | Servlet | `com.servlet.addproduct` | ✅ Documented |
| CP-006 | addtocart | Servlet | `com.servlet.addtocart` | ✅ Documented |
| CP-007 | addtocartnull | Servlet | `com.servlet.addtocartnull` | ✅ Documented |
| CP-008 | addtocartnulla | Servlet | `com.servlet.addtocartnulla` | ✅ Documented |
| CP-009 | removecarta | Servlet | `com.servlet.removecarta` | ✅ Documented |
| CP-010 | removecartnulla | Servlet | `com.servlet.removecartnulla` | ✅ Documented |
| CP-011 | checkadmin | Servlet | `com.servlet.checkadmin` | ✅ Documented |
| CP-012 | checkcustomer | Servlet | `com.servlet.checkcustomer` | ✅ Documented |
| CP-013 | deletecustomer | Servlet | `com.servlet.deletecustomer` | ✅ Documented |
| CP-014 | DAO | DAO Class | `com.dao.DAO` | ✅ Documented |
| CP-015 | DAO2 | DAO Class | `com.dao.DAO2` | ✅ Documented |
| CP-016 | payprocess | Servlet | `com.servlet.payprocess` | ✅ Documented |
| CP-017 | removecart | Servlet | `com.servlet.removecart` | ✅ Documented |
| CP-018 | removecartnull | Servlet | `com.servlet.removecartnull` | ✅ Documented |
| CP-019 | removeorders | Servlet | `com.servlet.removeorders` | ✅ Documented |
| CP-020 | remove_orders | Servlet | `com.servlet.remove_orders` | ✅ Documented |
| CP-021 | remove_contactus | Servlet | `com.servlet.remove_contactus` | ✅ Documented |
| CP-022 | removetable_cart | Servlet | `com.servlet.removetable_cart` | ✅ Documented |
| CP-023 | removetable_order_details | Servlet | `com.servlet.removetable_order_details` | ✅ Documented |

---

## Domain Concept IDs (DC-*)

*Source: `docs/discovered-domain-concepts.md`, `docs/domain/domain-concepts-catalog.md`*

| ID | Concept | Entity Class | DB Table | Status |
|---|---|---|---|---|
| DC-001 | Product | `com.entity.Product` | `product` | ✅ Documented |
| DC-002 | Brand | `com.entity.brand` | `brand` | ✅ Documented |
| DC-003 | Category | `com.entity.category` | `category` | ✅ Documented |
| DC-004 | Customer | `com.entity.customer` | `customer` | ✅ Documented |
| DC-005 | UserMaster (Admin) | `com.entity.usermaster` | `usermaster` | ✅ Documented |
| DC-006 | Cart | `com.entity.cart` | `cart` | ✅ Documented |
| DC-007 | Orders | `com.entity.orders` | `orders` | ✅ Documented |
| DC-008 | OrderDetails | `com.entity.orderdetails` | `order_details` | ✅ Documented |
| DC-009 | ContactUs | `com.entity.contactus` | `Contactus` | ✅ Documented |
| DC-010 | ViewList | `com.entity.viewlist` | `viewlist` | ✅ Documented |
| DC-011 | Mobile | `com.entity.mobile` | `mobile` | ✅ Documented |
| DC-012 | TV | `com.entity.tv` | `tv` | ✅ Documented |
| DC-013 | Laptop | `com.entity.laptop` | `laptop` | ✅ Documented |
| DC-014 | Watch | `com.entity.watch` | `watch` | ✅ Documented |

---

## Use Case IDs (UC-*)

*Source: `docs/business/use-cases/`*

| ID | Name | Document | Actors | Status |
|---|---|---|---|---|
| UC-001 | Register as Customer | `docs/business/use-cases/UC-001-register-customer.md` | Guest | ✅ Documented |
| UC-002 | Customer Login | `docs/business/use-cases/UC-002-customer-login.md` | Customer, Guest | ✅ Documented |
| UC-003 | Admin Login | `docs/business/use-cases/UC-003-admin-login.md` | Admin | ✅ Documented |
| UC-004 | Browse Product Catalogue | `docs/business/use-cases/UC-004-browse-catalogue.md` | Guest, Customer, Admin | ✅ Documented |
| UC-005 | Add Product to Cart | `docs/business/use-cases/UC-005-add-to-cart.md` | Guest, Customer | ✅ Documented |
| UC-006 | Manage Shopping Cart | `docs/business/use-cases/UC-006-manage-cart.md` | Guest, Customer, Admin | ✅ Documented |
| UC-007 | Checkout and Place Order | `docs/business/use-cases/UC-007-checkout-place-order.md` | Customer, Guest | ✅ Documented |
| UC-008 | View Order History | `docs/business/use-cases/UC-008-view-order-history.md` | Customer | ✅ Documented |
| UC-009 | Cancel Order | `docs/business/use-cases/UC-009-cancel-order.md` | Customer, Admin | ✅ Documented |
| UC-010 | Submit Contact Enquiry | `docs/business/use-cases/UC-010-submit-contact-enquiry.md` | Guest, Customer | ✅ Documented |
| UC-011 | Admin Add Product | `docs/business/use-cases/UC-011-admin-add-product.md` | Admin | ✅ Documented |
| UC-012 | Admin Manage Customers | `docs/business/use-cases/UC-012-admin-manage-customers.md` | Admin | ✅ Documented |
| UC-013 | Admin Manage Contact Enquiries | `docs/business/use-cases/UC-013-admin-manage-enquiries.md` | Admin | ✅ Documented |
| UC-014 | Admin View Dashboard | `docs/business/use-cases/UC-014-admin-dashboard.md` | Admin | ✅ Documented |

---

## Business Requirement IDs (BUREQ-*)

*Source: `docs/business/use-cases/`*

| ID | Summary | Use Case | Status |
|---|---|---|---|
| BUREQ-001-01 | Prevent duplicate registrations | UC-001 | ✅ Documented |
| BUREQ-001-02 | Collect registration fields | UC-001 | ✅ Documented |
| BUREQ-001-03 | Post-registration cart continuation | UC-001 | ✅ Documented |
| BUREQ-001-04 | Failure message on error | UC-001 | ✅ Documented |
| BUREQ-002-01 | Customer auth via email + password | UC-002 | ✅ Documented |
| BUREQ-002-02 | Persistent session via cname cookie | UC-002 | ✅ Documented |
| BUREQ-002-03 | Preserve cart context after login | UC-002 | ✅ Documented |
| BUREQ-002-04 | Error on failed customer login | UC-002 | ✅ Documented |
| BUREQ-003-01 | Admin auth via username + password | UC-003 | ✅ Documented |
| BUREQ-003-02 | Admin session via tname cookie | UC-003 | ✅ Documented |
| BUREQ-003-03 | Error on failed admin login | UC-003 | ✅ Documented |
| BUREQ-004-01 | Display all products | UC-004 | ✅ Documented |
| BUREQ-004-02 | Filter by category | UC-004 | ✅ Documented |
| BUREQ-004-03 | Consistent browsing for all actors | UC-004 | ✅ Documented |
| BUREQ-004-04 | Navigate to product detail | UC-004 | ✅ Documented |
| BUREQ-005-01 | Guest add to cart (no login) | UC-005 | ✅ Documented |
| BUREQ-005-02 | Customer add to personal cart | UC-005 | ✅ Documented |
| BUREQ-005-03 | Duplicate product increments quantity | UC-005 | ✅ Documented |
| BUREQ-005-04 | Cart stores product details | UC-005 | ✅ Documented |
| BUREQ-006-01 | Display cart items | UC-006 | ✅ Documented |
| BUREQ-006-02 | Remove cart items | UC-006 | ✅ Documented |
| BUREQ-006-03 | Immediate cart update after removal | UC-006 | ✅ Documented |
| BUREQ-006-04 | Admin remove any cart item | UC-006 | ✅ Documented |
| BUREQ-007-01 | Block checkout on empty cart | UC-007 | ✅ Documented |
| BUREQ-007-02 | Require shipping city | UC-007 | ✅ Documented |
| BUREQ-007-03 | Single payment method selection | UC-007 | ✅ Documented |
| BUREQ-007-04 | Transfer cart items to order | UC-007 | ✅ Documented |
| BUREQ-007-05 | New orders status = "Processing" | UC-007 | ✅ Documented |
| BUREQ-007-06 | Inform on order creation failure | UC-007 | ✅ Documented |
| BUREQ-008-01 | Display customer order list | UC-008 | ✅ Documented |
| BUREQ-008-02 | Order list shows key fields | UC-008 | ✅ Documented |
| BUREQ-008-03 | Navigate to order line items | UC-008 | ✅ Documented |
| BUREQ-009-01 | Customer cancel own order | UC-009 | ✅ Documented |
| BUREQ-009-02 | Admin cancel any order | UC-009 | ✅ Documented |
| BUREQ-009-03 | Orders page refreshes after cancel | UC-009 | ✅ Documented |
| BUREQ-010-01 | Guest and customer submit enquiry | UC-010 | ✅ Documented |
| BUREQ-010-02 | Enquiry captures name/email/phone/msg | UC-010 | ✅ Documented |
| BUREQ-010-03 | Confirm enquiry outcome | UC-010 | ✅ Documented |
| BUREQ-011-01 | Image format + size validation | UC-011 | ✅ Documented |
| BUREQ-011-02 | Product assigned brand + category | UC-011 | ✅ Documented |
| BUREQ-011-03 | Admin notified of product add result | UC-011 | ✅ Documented |
| BUREQ-011-04 | Only admin may add products | UC-011 | ✅ Documented |
| BUREQ-012-01 | Admin views all customers | UC-012 | ✅ Documented |
| BUREQ-012-02 | Admin deletes customer | UC-012 | ✅ Documented |
| BUREQ-012-03 | Customer list reflects deletion | UC-012 | ✅ Documented |
| BUREQ-013-01 | Admin views all enquiries | UC-013 | ✅ Documented |
| BUREQ-013-02 | Admin deletes enquiry | UC-013 | ✅ Documented |
| BUREQ-013-03 | Enquiry list reflects deletion | UC-013 | ✅ Documented |
| BUREQ-014-01 | Dashboard shows visual product overview | UC-014 | ✅ Documented |
| BUREQ-014-02 | Dashboard shows all 4 categories | UC-014 | ✅ Documented |

---

## Business Process IDs (BP-*)

*Source: `docs/business/processes/`*

| ID | Name | Document | Status |
|---|---|---|---|
| BP-001 | Customer Onboarding | `docs/business/processes/BP-001-customer-onboarding.md` | ✅ Documented |
| BP-002 | Product Discovery and Shopping | `docs/business/processes/BP-002-product-discovery-shopping.md` | ✅ Documented |
| BP-003 | Checkout and Order Fulfilment | `docs/business/processes/BP-003-checkout-order-fulfilment.md` | ✅ Documented |
| BP-004 | Admin Product Management | `docs/business/processes/BP-004-admin-product-management.md` | ✅ Documented |
| BP-005 | Admin Customer and Data Management | `docs/business/processes/BP-005-admin-data-management.md` | ✅ Documented |
| BP-006 | Contact Enquiry Handling | `docs/business/processes/BP-006-contact-enquiry-handling.md` | ✅ Documented |

---

## Functional Requirement IDs (FUREQ-*, NFUREQ-*)

*Source: `docs/functional/requirements/`*

| ID | Name | Document | Traced BUREQs | Status |
|---|---|---|---|---|
| FUREQ-001 | Customer Registration | `docs/functional/requirements/FUREQ-001-customer-registration.md` | BUREQ-001-01 to -04 | ✅ Documented |
| FUREQ-002 | Authentication | `docs/functional/requirements/FUREQ-002-authentication.md` | BUREQ-002-01 to -04, BUREQ-003-01 to -03 | ✅ Documented |
| FUREQ-003 | Product Catalogue | `docs/functional/requirements/FUREQ-003-product-catalogue.md` | BUREQ-004-01 to -04, BUREQ-011-01 to -04 | ✅ Documented |
| FUREQ-004 | Shopping Cart | `docs/functional/requirements/FUREQ-004-shopping-cart.md` | BUREQ-005-01 to -04, BUREQ-006-01 to -04 | ✅ Documented |
| FUREQ-005 | Order Management | `docs/functional/requirements/FUREQ-005-order-management.md` | BUREQ-007-01 to -06, BUREQ-008-01 to -03, BUREQ-009-01 to -03 | ✅ Documented |
| FUREQ-006 | Contact Us & Admin Ops | `docs/functional/requirements/FUREQ-006-contact-admin-ops.md` | BUREQ-010-01 to -03, BUREQ-012-01 to -03, BUREQ-013-01 to -03, BUREQ-014-01 to -02 | ✅ Documented |
| NFUREQ-001 | Security | `docs/functional/requirements/NFUREQ-001-security.md` | Cross-cutting | ✅ Documented |
| NFUREQ-002 | Performance & Data Integrity | `docs/functional/requirements/NFUREQ-002-performance-integrity.md` | Cross-cutting | ✅ Documented |

---

## Technical Flow IDs (FF-*)

*Source: `docs/functional/flows/`*

| ID | Name | Document | Covered FLs | Status |
|---|---|---|---|---|
| FF-001 | Registration & Onboarding | `docs/functional/flows/FF-001-registration-onboarding.md` | FL-001, FL-002 | ✅ Documented |
| FF-002 | Catalogue & Cart Management | `docs/functional/flows/FF-002-catalogue-cart-flow.md` | FL-005 to FL-013, FL-027 | ✅ Documented |
| FF-003 | Checkout & Order Processing | `docs/functional/flows/FF-003-checkout-order-flow.md` | FL-014 to FL-017, FL-024, FL-025 | ✅ Documented |
| FF-004 | Admin Operations | `docs/functional/flows/FF-004-admin-operations-flow.md` | FL-003, FL-004, FL-018 to FL-023, FL-026 | ✅ Documented |

---

## Integration IDs (INT-*)

*Source: `docs/functional/integration/`*

| ID | Name | Document | Status |
|---|---|---|---|
| INT-001 | SQLite Database | `docs/functional/integration/INT-001-database.md` | ✅ Documented |
| INT-002 | File System — Image Uploads | `docs/functional/integration/INT-002-file-system.md` | ✅ Documented |

---

## Diagram IDs (DIAG-*)

*Source: `docs/functional/diagrams/`*

| ID | Name | Document | Status |
|---|---|---|---|
| DIAG-001 | System Architecture | `docs/functional/diagrams/DIAG-001-system-architecture.md` | ✅ Documented |

---

## Coordination Artifact IDs

| ID | Name | Document | Status |
|---|---|---|---|
| SO-001 | System Overview | `docs/system-overview.md` | ✅ Documented |
| DCC-001 | Domain Concepts Catalog | `docs/domain/domain-concepts-catalog.md` | ✅ Documented |
| RTM-001 | Requirement Traceability Matrix | `docs/traceability/requirement-matrix.md` | ✅ Documented |
| FCM-001 | Flow-to-Component Map | `docs/traceability/flow-to-component-map.md` | ✅ Documented |
| IDR-001 | ID Registry | `docs/traceability/id-registry.md` | ✅ Documented |

---

## ID Uniqueness Validation

All IDs verified unique within their respective namespaces:

| Namespace | Count | Conflicts |
|---|---|---|
| FL-* (Execution Flows) | 27 | None |
| CP-* (Components) | 23 | None |
| DC-* (Domain Concepts) | 14 | None |
| UC-* (Use Cases) | 14 | None |
| BUREQ-* (Business Requirements) | 34 | None |
| BP-* (Business Processes) | 6 | None |
| FUREQ-* (Functional Requirements) | 6 | None |
| NFUREQ-* (Non-Functional Requirements) | 2 | None |
| FF-* (Technical Flows) | 4 | None |
| INT-* (Integrations) | 2 | None |
| DIAG-* (Diagrams) | 1 | None |

**Total documented artefacts: 133** across 11 namespaces. No ID conflicts detected.

---

*Validated during coordination phase. Cross-reference with [Requirement Matrix](requirement-matrix.md) and [Flow-to-Component Map](flow-to-component-map.md).*
