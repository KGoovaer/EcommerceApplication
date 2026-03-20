# Requirement Traceability Matrix — EcommerceApp

**Document ID:** RTM-001  
**Version:** 1.0  
**Phase:** Coordination  
**Traced Chain:** BUREQ → UC → FUREQ → FF  

---

## Overview

This matrix provides complete traceability from business requirements (BUREQ) through use cases (UC) to functional requirements (FUREQ) and technical flows (FF). Each row represents one business requirement and its complete downstream mapping.

---

## Traceability Matrix

| BUREQ ID | Business Requirement Summary | Use Case | FUREQ | Technical Flow | Business Process |
|---|---|---|---|---|---|
| BUREQ-001-01 | Prevent duplicate registrations (name or email) | [UC-001](../business/use-cases/UC-001-register-customer.md) | [FUREQ-001](../functional/requirements/FUREQ-001-customer-registration.md) | [FF-001](../functional/flows/FF-001-registration-onboarding.md) | [BP-001](../business/processes/BP-001-customer-onboarding.md) |
| BUREQ-001-02 | Collect name, password, email, contact number | [UC-001](../business/use-cases/UC-001-register-customer.md) | [FUREQ-001](../functional/requirements/FUREQ-001-customer-registration.md) | [FF-001](../functional/flows/FF-001-registration-onboarding.md) | [BP-001](../business/processes/BP-001-customer-onboarding.md) |
| BUREQ-001-03 | Post-registration redirect to login (cart context preserved) | [UC-001](../business/use-cases/UC-001-register-customer.md) | [FUREQ-001](../functional/requirements/FUREQ-001-customer-registration.md) | [FF-001](../functional/flows/FF-001-registration-onboarding.md) | [BP-001](../business/processes/BP-001-customer-onboarding.md) |
| BUREQ-001-04 | Show failure message on duplicate/error | [UC-001](../business/use-cases/UC-001-register-customer.md) | [FUREQ-001](../functional/requirements/FUREQ-001-customer-registration.md) | [FF-001](../functional/flows/FF-001-registration-onboarding.md) | [BP-001](../business/processes/BP-001-customer-onboarding.md) |
| BUREQ-002-01 | Authenticate customer via email + password | [UC-002](../business/use-cases/UC-002-customer-login.md) | [FUREQ-002](../functional/requirements/FUREQ-002-authentication.md) | [FF-001](../functional/flows/FF-001-registration-onboarding.md) | [BP-001](../business/processes/BP-001-customer-onboarding.md) |
| BUREQ-002-02 | Establish persistent session (cname cookie) | [UC-002](../business/use-cases/UC-002-customer-login.md) | [FUREQ-002](../functional/requirements/FUREQ-002-authentication.md) | [FF-001](../functional/flows/FF-001-registration-onboarding.md) | [BP-001](../business/processes/BP-001-customer-onboarding.md) |
| BUREQ-002-03 | Preserve guest cart context after login | [UC-002](../business/use-cases/UC-002-customer-login.md) | [FUREQ-002](../functional/requirements/FUREQ-002-authentication.md) | [FF-001](../functional/flows/FF-001-registration-onboarding.md) | [BP-001](../business/processes/BP-001-customer-onboarding.md) |
| BUREQ-002-04 | Show error on failed customer login | [UC-002](../business/use-cases/UC-002-customer-login.md) | [FUREQ-002](../functional/requirements/FUREQ-002-authentication.md) | [FF-001](../functional/flows/FF-001-registration-onboarding.md) | [BP-001](../business/processes/BP-001-customer-onboarding.md) |
| BUREQ-003-01 | Authenticate admin via username + password | [UC-003](../business/use-cases/UC-003-admin-login.md) | [FUREQ-002](../functional/requirements/FUREQ-002-authentication.md) | [FF-004](../functional/flows/FF-004-admin-operations-flow.md) | — |
| BUREQ-003-02 | Establish admin session (tname cookie) | [UC-003](../business/use-cases/UC-003-admin-login.md) | [FUREQ-002](../functional/requirements/FUREQ-002-authentication.md) | [FF-004](../functional/flows/FF-004-admin-operations-flow.md) | — |
| BUREQ-003-03 | Show error on failed admin login | [UC-003](../business/use-cases/UC-003-admin-login.md) | [FUREQ-002](../functional/requirements/FUREQ-002-authentication.md) | [FF-004](../functional/flows/FF-004-admin-operations-flow.md) | — |
| BUREQ-004-01 | Display all products (name, price, image, brand, category) | [UC-004](../business/use-cases/UC-004-browse-catalogue.md) | [FUREQ-003](../functional/requirements/FUREQ-003-product-catalogue.md) | [FF-002](../functional/flows/FF-002-catalogue-cart-flow.md) | [BP-002](../business/processes/BP-002-product-discovery-shopping.md) |
| BUREQ-004-02 | Filter products by category | [UC-004](../business/use-cases/UC-004-browse-catalogue.md) | [FUREQ-003](../functional/requirements/FUREQ-003-product-catalogue.md) | [FF-002](../functional/flows/FF-002-catalogue-cart-flow.md) | [BP-002](../business/processes/BP-002-product-discovery-shopping.md) |
| BUREQ-004-03 | Consistent browsing experience for all actor types | [UC-004](../business/use-cases/UC-004-browse-catalogue.md) | [FUREQ-003](../functional/requirements/FUREQ-003-product-catalogue.md) | [FF-002](../functional/flows/FF-002-catalogue-cart-flow.md) | [BP-002](../business/processes/BP-002-product-discovery-shopping.md) |
| BUREQ-004-04 | Navigate from listing to product detail | [UC-004](../business/use-cases/UC-004-browse-catalogue.md) | [FUREQ-003](../functional/requirements/FUREQ-003-product-catalogue.md) | [FF-002](../functional/flows/FF-002-catalogue-cart-flow.md) | [BP-002](../business/processes/BP-002-product-discovery-shopping.md) |
| BUREQ-005-01 | Guests may add to anonymous cart without login | [UC-005](../business/use-cases/UC-005-add-to-cart.md) | [FUREQ-004](../functional/requirements/FUREQ-004-shopping-cart.md) | [FF-002](../functional/flows/FF-002-catalogue-cart-flow.md) | [BP-002](../business/processes/BP-002-product-discovery-shopping.md) |
| BUREQ-005-02 | Registered customers add to personal cart | [UC-005](../business/use-cases/UC-005-add-to-cart.md) | [FUREQ-004](../functional/requirements/FUREQ-004-shopping-cart.md) | [FF-002](../functional/flows/FF-002-catalogue-cart-flow.md) | [BP-002](../business/processes/BP-002-product-discovery-shopping.md) |
| BUREQ-005-03 | Duplicate product increments quantity, not new row | [UC-005](../business/use-cases/UC-005-add-to-cart.md) | [FUREQ-004](../functional/requirements/FUREQ-004-shopping-cart.md) | [FF-002](../functional/flows/FF-002-catalogue-cart-flow.md) | [BP-002](../business/processes/BP-002-product-discovery-shopping.md) |
| BUREQ-005-04 | Cart stores name, brand, category, price, quantity, image | [UC-005](../business/use-cases/UC-005-add-to-cart.md) | [FUREQ-004](../functional/requirements/FUREQ-004-shopping-cart.md) | [FF-002](../functional/flows/FF-002-catalogue-cart-flow.md) | [BP-002](../business/processes/BP-002-product-discovery-shopping.md) |
| BUREQ-006-01 | Display cart with all item details | [UC-006](../business/use-cases/UC-006-manage-cart.md) | [FUREQ-004](../functional/requirements/FUREQ-004-shopping-cart.md) | [FF-002](../functional/flows/FF-002-catalogue-cart-flow.md) | [BP-002](../business/processes/BP-002-product-discovery-shopping.md) |
| BUREQ-006-02 | Users may remove any cart item | [UC-006](../business/use-cases/UC-006-manage-cart.md) | [FUREQ-004](../functional/requirements/FUREQ-004-shopping-cart.md) | [FF-002](../functional/flows/FF-002-catalogue-cart-flow.md) | [BP-002](../business/processes/BP-002-product-discovery-shopping.md) |
| BUREQ-006-03 | Removal updates cart display immediately | [UC-006](../business/use-cases/UC-006-manage-cart.md) | [FUREQ-004](../functional/requirements/FUREQ-004-shopping-cart.md) | [FF-002](../functional/flows/FF-002-catalogue-cart-flow.md) | [BP-002](../business/processes/BP-002-product-discovery-shopping.md) |
| BUREQ-006-04 | Admin may remove any cart item from any cart | [UC-006](../business/use-cases/UC-006-manage-cart.md) | [FUREQ-004](../functional/requirements/FUREQ-004-shopping-cart.md) | [FF-002](../functional/flows/FF-002-catalogue-cart-flow.md) | [BP-004](../business/processes/BP-004-admin-product-management.md) |
| BUREQ-007-01 | Checkout blocked on empty cart | [UC-007](../business/use-cases/UC-007-checkout-place-order.md) | [FUREQ-005](../functional/requirements/FUREQ-005-order-management.md) | [FF-003](../functional/flows/FF-003-checkout-order-flow.md) | [BP-003](../business/processes/BP-003-checkout-order-fulfilment.md) |
| BUREQ-007-02 | Customer must provide city (minimum shipping location) | [UC-007](../business/use-cases/UC-007-checkout-place-order.md) | [FUREQ-005](../functional/requirements/FUREQ-005-order-management.md) | [FF-003](../functional/flows/FF-003-checkout-order-flow.md) | [BP-003](../business/processes/BP-003-checkout-order-fulfilment.md) |
| BUREQ-007-03 | Exactly one payment method: cash or online | [UC-007](../business/use-cases/UC-007-checkout-place-order.md) | [FUREQ-005](../functional/requirements/FUREQ-005-order-management.md) | [FF-003](../functional/flows/FF-003-checkout-order-flow.md) | [BP-003](../business/processes/BP-003-checkout-order-fulfilment.md) |
| BUREQ-007-04 | Cart items transferred to order; cart cleared | [UC-007](../business/use-cases/UC-007-checkout-place-order.md) | [FUREQ-005](../functional/requirements/FUREQ-005-order-management.md) | [FF-003](../functional/flows/FF-003-checkout-order-flow.md) | [BP-003](../business/processes/BP-003-checkout-order-fulfilment.md) |
| BUREQ-007-05 | New orders initialised with status "Processing" | [UC-007](../business/use-cases/UC-007-checkout-place-order.md) | [FUREQ-005](../functional/requirements/FUREQ-005-order-management.md) | [FF-003](../functional/flows/FF-003-checkout-order-flow.md) | [BP-003](../business/processes/BP-003-checkout-order-fulfilment.md) |
| BUREQ-007-06 | Inform customer on any order creation failure | [UC-007](../business/use-cases/UC-007-checkout-place-order.md) | [FUREQ-005](../functional/requirements/FUREQ-005-order-management.md) | [FF-003](../functional/flows/FF-003-checkout-order-flow.md) | [BP-003](../business/processes/BP-003-checkout-order-fulfilment.md) |
| BUREQ-008-01 | Display all customer's own orders | [UC-008](../business/use-cases/UC-008-view-order-history.md) | [FUREQ-005](../functional/requirements/FUREQ-005-order-management.md) | [FF-003](../functional/flows/FF-003-checkout-order-flow.md) | [BP-003](../business/processes/BP-003-checkout-order-fulfilment.md) |
| BUREQ-008-02 | Each order shows: ID, city, date, total, status | [UC-008](../business/use-cases/UC-008-view-order-history.md) | [FUREQ-005](../functional/requirements/FUREQ-005-order-management.md) | [FF-003](../functional/flows/FF-003-checkout-order-flow.md) | [BP-003](../business/processes/BP-003-checkout-order-fulfilment.md) |
| BUREQ-008-03 | Customer can view individual order line items | [UC-008](../business/use-cases/UC-008-view-order-history.md) | [FUREQ-005](../functional/requirements/FUREQ-005-order-management.md) | [FF-003](../functional/flows/FF-003-checkout-order-flow.md) | [BP-003](../business/processes/BP-003-checkout-order-fulfilment.md) |
| BUREQ-009-01 | Customer can cancel own orders | [UC-009](../business/use-cases/UC-009-cancel-order.md) | [FUREQ-005](../functional/requirements/FUREQ-005-order-management.md) | [FF-003](../functional/flows/FF-003-checkout-order-flow.md) | [BP-003](../business/processes/BP-003-checkout-order-fulfilment.md) |
| BUREQ-009-02 | Admin can remove any order | [UC-009](../business/use-cases/UC-009-cancel-order.md) | [FUREQ-005](../functional/requirements/FUREQ-005-order-management.md) | [FF-003](../functional/flows/FF-003-checkout-order-flow.md) | [BP-003](../business/processes/BP-003-checkout-order-fulfilment.md) |
| BUREQ-009-03 | Orders page refreshes after cancellation | [UC-009](../business/use-cases/UC-009-cancel-order.md) | [FUREQ-005](../functional/requirements/FUREQ-005-order-management.md) | [FF-003](../functional/flows/FF-003-checkout-order-flow.md) | [BP-003](../business/processes/BP-003-checkout-order-fulfilment.md) |
| BUREQ-010-01 | Both guests and customers may submit enquiries | [UC-010](../business/use-cases/UC-010-submit-contact-enquiry.md) | [FUREQ-006](../functional/requirements/FUREQ-006-contact-admin-ops.md) | [FF-004](../functional/flows/FF-004-admin-operations-flow.md) | [BP-006](../business/processes/BP-006-contact-enquiry-handling.md) |
| BUREQ-010-02 | Enquiry captures name, email, phone, message | [UC-010](../business/use-cases/UC-010-submit-contact-enquiry.md) | [FUREQ-006](../functional/requirements/FUREQ-006-contact-admin-ops.md) | [FF-004](../functional/flows/FF-004-admin-operations-flow.md) | [BP-006](../business/processes/BP-006-contact-enquiry-handling.md) |
| BUREQ-010-03 | Confirm submission outcome to user | [UC-010](../business/use-cases/UC-010-submit-contact-enquiry.md) | [FUREQ-006](../functional/requirements/FUREQ-006-contact-admin-ops.md) | [FF-004](../functional/flows/FF-004-admin-operations-flow.md) | [BP-006](../business/processes/BP-006-contact-enquiry-handling.md) |
| BUREQ-011-01 | Product images: .jpg/.bmp/.jpeg/.png/.webp ≤ 10 MB | [UC-011](../business/use-cases/UC-011-admin-add-product.md) | [FUREQ-003](../functional/requirements/FUREQ-003-product-catalogue.md) | [FF-004](../functional/flows/FF-004-admin-operations-flow.md) | [BP-004](../business/processes/BP-004-admin-product-management.md) |
| BUREQ-011-02 | Products assigned to exactly one brand and one category | [UC-011](../business/use-cases/UC-011-admin-add-product.md) | [FUREQ-003](../functional/requirements/FUREQ-003-product-catalogue.md) | [FF-004](../functional/flows/FF-004-admin-operations-flow.md) | [BP-004](../business/processes/BP-004-admin-product-management.md) |
| BUREQ-011-03 | Admin notified of product add success/failure | [UC-011](../business/use-cases/UC-011-admin-add-product.md) | [FUREQ-003](../functional/requirements/FUREQ-003-product-catalogue.md) | [FF-004](../functional/flows/FF-004-admin-operations-flow.md) | [BP-004](../business/processes/BP-004-admin-product-management.md) |
| BUREQ-011-04 | Only authenticated admins may add products | [UC-011](../business/use-cases/UC-011-admin-add-product.md) | [FUREQ-003](../functional/requirements/FUREQ-003-product-catalogue.md) | [FF-004](../functional/flows/FF-004-admin-operations-flow.md) | [BP-004](../business/processes/BP-004-admin-product-management.md) |
| BUREQ-012-01 | Admin views all customers with name, email, contact | [UC-012](../business/use-cases/UC-012-admin-manage-customers.md) | [FUREQ-006](../functional/requirements/FUREQ-006-contact-admin-ops.md) | [FF-004](../functional/flows/FF-004-admin-operations-flow.md) | [BP-005](../business/processes/BP-005-admin-data-management.md) |
| BUREQ-012-02 | Admin can permanently delete a customer | [UC-012](../business/use-cases/UC-012-admin-manage-customers.md) | [FUREQ-006](../functional/requirements/FUREQ-006-contact-admin-ops.md) | [FF-004](../functional/flows/FF-004-admin-operations-flow.md) | [BP-005](../business/processes/BP-005-admin-data-management.md) |
| BUREQ-012-03 | Customer list reflects deletion | [UC-012](../business/use-cases/UC-012-admin-manage-customers.md) | [FUREQ-006](../functional/requirements/FUREQ-006-contact-admin-ops.md) | [FF-004](../functional/flows/FF-004-admin-operations-flow.md) | [BP-005](../business/processes/BP-005-admin-data-management.md) |
| BUREQ-013-01 | Admin views all enquiries with details | [UC-013](../business/use-cases/UC-013-admin-manage-enquiries.md) | [FUREQ-006](../functional/requirements/FUREQ-006-contact-admin-ops.md) | [FF-004](../functional/flows/FF-004-admin-operations-flow.md) | [BP-005](../business/processes/BP-005-admin-data-management.md) |
| BUREQ-013-02 | Admin can delete any enquiry | [UC-013](../business/use-cases/UC-013-admin-manage-enquiries.md) | [FUREQ-006](../functional/requirements/FUREQ-006-contact-admin-ops.md) | [FF-004](../functional/flows/FF-004-admin-operations-flow.md) | [BP-005](../business/processes/BP-005-admin-data-management.md) |
| BUREQ-013-03 | Enquiry list reflects deletion | [UC-013](../business/use-cases/UC-013-admin-manage-enquiries.md) | [FUREQ-006](../functional/requirements/FUREQ-006-contact-admin-ops.md) | [FF-004](../functional/flows/FF-004-admin-operations-flow.md) | [BP-005](../business/processes/BP-005-admin-data-management.md) |
| BUREQ-014-01 | Dashboard displays visual product overview | [UC-014](../business/use-cases/UC-014-admin-dashboard.md) | [FUREQ-006](../functional/requirements/FUREQ-006-contact-admin-ops.md) | [FF-004](../functional/flows/FF-004-admin-operations-flow.md) | [BP-004](../business/processes/BP-004-admin-product-management.md) |
| BUREQ-014-02 | Dashboard shows products from all 4 categories | [UC-014](../business/use-cases/UC-014-admin-dashboard.md) | [FUREQ-006](../functional/requirements/FUREQ-006-contact-admin-ops.md) | [FF-004](../functional/flows/FF-004-admin-operations-flow.md) | [BP-004](../business/processes/BP-004-admin-product-management.md) |

---

## Coverage Summary

| FUREQ | Covered By BUREQs | Related UCs | Technical Flow |
|---|---|---|---|
| FUREQ-001 | BUREQ-001-01 to BUREQ-001-04 | UC-001 | FF-001 |
| FUREQ-002 | BUREQ-002-01 to BUREQ-002-04, BUREQ-003-01 to BUREQ-003-03 | UC-002, UC-003 | FF-001, FF-004 |
| FUREQ-003 | BUREQ-004-01 to BUREQ-004-04, BUREQ-011-01 to BUREQ-011-04 | UC-004, UC-011 | FF-002, FF-004 |
| FUREQ-004 | BUREQ-005-01 to BUREQ-005-04, BUREQ-006-01 to BUREQ-006-04 | UC-005, UC-006 | FF-002 |
| FUREQ-005 | BUREQ-007-01 to BUREQ-007-06, BUREQ-008-01 to BUREQ-008-03, BUREQ-009-01 to BUREQ-009-03 | UC-007, UC-008, UC-009 | FF-003 |
| FUREQ-006 | BUREQ-010-01 to BUREQ-010-03, BUREQ-012-01 to BUREQ-012-03, BUREQ-013-01 to BUREQ-013-03, BUREQ-014-01 to BUREQ-014-02 | UC-010, UC-012, UC-013, UC-014 | FF-004 |
| NFUREQ-001 | Security cross-cutting concern | All UCs | All FFs |
| NFUREQ-002 | Performance / integrity cross-cutting concern | All UCs | All FFs |

---

## Traceability Gaps

| Gap | Description | Recommendation |
|---|---|---|
| No UC for admin order cancellation | BUREQ-009-02 (admin cancel order) is traced to UC-009 but UC-009 primarily covers customer cancel. | Consider splitting UC-009 into customer and admin variants, or adding a note in UC-009 about admin scope. |
| BUREQ-006-04 cross-domain | Admin removing cart items (BUREQ-006-04) is traced to both BP-002 and BP-004; the primary owner is ambiguous. | Assign BP-004 as the primary owner since it is an admin operation. |
| No BUREQ for guest-to-order checkout | The cart-continuation / guest checkout flow (FL-014) has no dedicated BUREQ; covered implicitly by BUREQ-007. | Document as a clarification note in FUREQ-005. |

---

*Cross-reference with [ID Registry](id-registry.md) for all document IDs | [Flow-to-Component Map](flow-to-component-map.md) for implementation detail*
