# Business Documentation Index — EcommerceApp

**Module:** ECOMMERCE_APPLICATION  
**Phase:** Business Documentation  
**Version:** 1.0  
**Last Updated:** 2026-03-18  

---

## Overview

This index provides a complete reference to all business documentation produced for the EcommerceApp Java EE e-commerce platform. The documentation covers use cases, business processes, and a system overview derived from 27 discovered execution flows, 56 components, and 14 domain concepts.

→ [System Overview](overview/system-overview.md)

---

## Use Cases

Use cases describe observable business outcomes that the system delivers to its actors (Guest, Customer, Admin).

| ID | Use Case | Actors | Related Flows |
|---|---|---|---|
| [UC-001](use-cases/UC-001-register-customer.md) | Register as Customer | Guest | FL-001 |
| [UC-002](use-cases/UC-002-customer-login.md) | Customer Login | Customer, Guest | FL-002 |
| [UC-003](use-cases/UC-003-admin-login.md) | Admin Login | Admin | FL-003 |
| [UC-004](use-cases/UC-004-browse-catalogue.md) | Browse Product Catalogue | Guest, Customer, Admin | FL-005, FL-006, FL-026, FL-027 |
| [UC-005](use-cases/UC-005-add-to-cart.md) | Add Product to Cart | Guest, Customer | FL-007, FL-008 |
| [UC-006](use-cases/UC-006-manage-cart.md) | Manage Shopping Cart | Guest, Customer, Admin | FL-010, FL-011, FL-012, FL-013 |
| [UC-007](use-cases/UC-007-checkout-place-order.md) | Checkout and Place Order | Customer, Guest | FL-014, FL-015 |
| [UC-008](use-cases/UC-008-view-order-history.md) | View Order History | Customer | FL-024, FL-025 |
| [UC-009](use-cases/UC-009-cancel-order.md) | Cancel Order | Customer, Admin | FL-016, FL-017 |
| [UC-010](use-cases/UC-010-submit-contact-enquiry.md) | Submit Contact Enquiry | Guest, Customer | FL-018, FL-019 |
| [UC-011](use-cases/UC-011-admin-add-product.md) | Admin Add Product | Admin | FL-004 |
| [UC-012](use-cases/UC-012-admin-manage-customers.md) | Admin Manage Customers | Admin | FL-020 |
| [UC-013](use-cases/UC-013-admin-manage-enquiries.md) | Admin Manage Contact Enquiries | Admin | FL-021 |
| [UC-014](use-cases/UC-014-admin-dashboard.md) | Admin View Dashboard | Admin | FL-026 |

---

## Business Processes

Business processes describe end-to-end workflows that span multiple use cases and actor interactions.

| ID | Process | Key Use Cases | Description |
|---|---|---|---|
| [BP-001](processes/BP-001-customer-onboarding.md) | Customer Onboarding | UC-001, UC-002 | From anonymous guest to authenticated registered customer, including cart-continuation flow |
| [BP-002](processes/BP-002-product-discovery-shopping.md) | Product Discovery and Shopping | UC-004, UC-005, UC-006 | Browse catalogue by category, add items to cart, manage cart contents |
| [BP-003](processes/BP-003-checkout-order-fulfilment.md) | Checkout and Order Fulfilment | UC-007, UC-008, UC-009 | From cart to confirmed order, including post-order history and cancellation |
| [BP-004](processes/BP-004-admin-product-management.md) | Admin Product Management | UC-011, UC-014 | Admin adds products with image, brand, and category assignment |
| [BP-005](processes/BP-005-admin-data-management.md) | Admin Customer and Data Management | UC-012, UC-013 | Admin views and manages all platform data tables |
| [BP-006](processes/BP-006-contact-enquiry-handling.md) | Customer Support — Contact Enquiry Handling | UC-010, UC-013 | Users submit enquiries; admins review and remove them |

---

## Business Rules Summary

| Rule ID | Rule | Linked UC |
|---|---|---|
| BR-001 | Customer name and email must each be unique | UC-001 |
| BR-002 | Products must belong to exactly one brand and one category | UC-011 |
| BR-003 | Product images must be .jpg/.bmp/.jpeg/.png/.webp and ≤ 10 MB | UC-011 |
| BR-004 | An order can only be placed if the cart has at least one item | UC-007 |
| BR-005 | All new orders are created with status "Processing" | UC-007 |
| BR-006 | Guests may add to a shared cart; converted to order at checkout | UC-005, UC-007 |
| BR-007 | Customers may cancel their own orders (header only, not details) | UC-009 |
| BR-008 | Admin may delete customers; no cascade to cart or orders | UC-012 |
| BR-009 | Contact enquiries persist until admin removes them | UC-013 |
| BR-010 | Only one payment method per order: cash or online | UC-007 |

---

## Actor Summary

| Actor | Capabilities |
|---|---|
| **Guest** | Browse catalogue, browse by category, add to anonymous cart, manage guest cart, submit contact enquiry, register, log in |
| **Customer** | All Guest capabilities + personalised cart, checkout, view order history, cancel orders, submit enquiry while logged in |
| **Admin** | Log in, view dashboard, add products, view all customers/orders/carts/enquiries, delete customers, remove any cart/order/enquiry records |

---

## Discovery Source Reference

| Discovery Artifact | Location |
|---|---|
| Execution Flows (27) | [docs/discovered-flows.md](../discovered-flows.md) |
| Domain Concepts (14) | [docs/discovered-domain-concepts.md](../discovered-domain-concepts.md) |
| Components (56) | [docs/discovered-components.md](../discovered-components.md) |
| Documentation Plan | [docs/documentation-plan.md](../documentation-plan.md) |
