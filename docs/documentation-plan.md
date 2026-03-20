# Documentation Plan — EcommerceApplication

## Project Overview

| Field | Value |
|---|---|
| **Module** | ECOMMERCE_APPLICATION |
| **Language** | Java (J2EE — Servlet 3.0 + JSP) |
| **Repository** | KGoovaer/EcommerceApplication |
| **Build Tool** | Maven (WAR) |
| **Database** | SQLite (via `org.xerial:sqlite-jdbc`) |
| **Server** | Apache Tomcat 8+ |
| **Auth** | Cookie-based (`cname` = customer email, `tname` = admin username) |
| **State File** | `docs/ecommerce-state.json` |

## Architecture Summary

```
Browser
  │
  ├── JSPs (src/main/webapp/*.jsp)   — Presentation layer + direct DAO scriptlets
  │     ├── navbar.jsp               — Guest navigation
  │     ├── customer_navbar.jsp      — Customer navigation
  │     └── admin_navbar.jsp         — Admin navigation
  │
  └── Servlets (com.servlet.*, 21 classes, @WebServlet annotations)
        └── DAO Layer (com.dao.DAO – DAO5, 5 classes, plain JDBC)
              └── DBConnect.getConn() → SQLite
```

### JSP Tripling Pattern

Most pages follow a tripling convention:
- `[page].jsp` — guest/anonymous view
- `[page]a.jsp` — admin view
- `[page]c.jsp` — logged-in customer view

### Database Tables

| Table | Purpose |
|---|---|
| `brand` | Product brands (Samsung, Sony, Lenovo, Acer, Onida) |
| `category` | Product categories (laptop, tv, mobile, watch) |
| `product` | Product master with image, price, quantity, brand, category |
| `viewlist` | Denormalized read view joining product + brand + category |
| `tv` | TV-specific product view |
| `laptop` | Laptop-specific product view |
| `mobile` | Mobile-specific product view |
| `watch` | Watch-specific product view |
| `customer` | Customer accounts (Name, Password, Email_Id, Contact_No) |
| `usermaster` | Admin accounts (Name, Password) |
| `cart` | Shopping cart (Name=NULL for guest, Name=email for customer) |
| `orders` | Order header (Customer_Name, Customer_City, Date, Total_Price, Status) |
| `order_details` | Order line items copied from cart at checkout |
| `Contactus` | Contact us messages |

---

## Business Domains

| # | Domain | Key Servlets | Key JSPs | DAOs |
|---|---|---|---|---|
| 1 | **Authentication** | `checkadmin`, `checkcustomer` | `adminlogin`, `customerlogin`, `validatelogina`, `validateloginc` | DAO2 |
| 2 | **Customer Management** | `addcustomer`, `deletecustomer` | `customer_reg`, `managecustomers`, `cupass`, `cufail` | DAO, DAO2 |
| 3 | **Product Catalog** | `addproduct` | `viewproduct`, `selecteditem`, `mobile`, `tv`, `laptop`, `watch`, `category` | DAO, DAO2, DAO3 |
| 4 | **Shopping Cart** | `addtocart`, `addtocartnull`, `addtocartnulla`, `removecart`, `removecarta`, `removecartnull`, `removecartnulla` | `cart`, `carta`, `cartnull`, `cartnulla`, `cartnullqty` | DAO2, DAO3, DAO4 |
| 5 | **Order Management & Payment** | `ShippingAddress2`, `payprocess`, `remove_orders`, `removeorders` | `ShippingAddress`, `confirmpayment`, `confirmonline`, `orders`, `orderdetails`, `paymentfail` | DAO3, DAO4 |
| 6 | **Admin Operations** | `removetable_cart`, `removetable_order_details` | `managetables`, `table_cart`, `table_orders`, `table_order_details`, `table_contactus`, `adminhome` | DAO4, DAO5 |
| 7 | **Contact Us** | `addContactus`, `addContactusc`, `remove_contactus` | `contactus`, `contactusc` | DAO5 |

---

## Phase Plan

### Phase 1: Discovery (5 Batches + Consolidation)

**Agent**: `discovery`  
**Skill**: `java-analysis`  
**Strategy**: Run discovery per domain batch. Each batch targets 7 tasks: entry points, execution flows, data access query analysis, component inventory, domain concepts, dependency mapping, and cross-domain table usage.

---

#### Batch 1 — Authentication & Customer Management

**Scope**:
- Servlets: `checkadmin`, `checkcustomer`, `addcustomer`, `deletecustomer`
- DAOs: `DAO2.checkadmin()`, `DAO2.checkcust()`, `DAO2.checkcust2()`, `DAO2.addcustomer()`, `DAO.deleteCustomer()`, `DAO.getAllCustomer()`, `DAO.getCustomer()`
- JSPs: `adminlogin`, `customerlogin`, `validatelogina`, `validateloginc`, `customer_reg`, `managecustomers`, `cupass`, `cupassc`, `cufail`, `cufailc`, `passc`

**Output**: `docs/discovery/batch-1-auth-customer-flows.md`, `batch-1-auth-customer-components.md`, `batch-1-auth-customer-domain-concepts.md`

**Special attention**:
- Cookie creation logic: `cname` (customer email), `tname` (admin username), `maxAge=9999`
- Flash message cookies (`maxAge=10`) used for pass/fail feedback between redirects
- `checkcust2()` — duplicate detection logic: `Name=? OR Email_Id=?` — document both conditions
- `deleteCustomer()` — dual-key delete: `Name=? AND Email_Id=?`
- Admin auth: `usermaster` table with `Name=? AND Password=?`
- Customer auth: `customer` table with `Password=? AND Email_Id=?`
- No `HttpSession` is used — entirely cookie-based
- Cross-domain: `customer` table also used by Cart (Batch 3) and Order (Batch 4)

---

#### Batch 2 — Product Catalog & Admin Product Management

**Scope**:
- Servlets: `addproduct`
- DAOs: `DAO.addproduct()`, `DAO.getAllbrand()`, `DAO.getAllcategory()`, `DAO2.getAllviewlist()`, `DAO2.getSelecteditem()`, `DAO3.getAlltv()`, `DAO3.getAlllaptop()`, `DAO3.getAllmobile()`, `DAO3.getAllwatch()`
- JSPs: `addproduct`, `viewproduct`, `viewproducta`, `viewproductc`, `selecteditem`, `selecteditema`, `selecteditemc`, `mobile`, `mobilea`, `mobilec`, `tv`, `tva`, `tvc`, `laptop`, `laptopa`, `laptopc`, `watch`, `watcha`, `watchc`, `category`, `categorya`, `categoryc`

**Output**: `docs/discovery/batch-2-product-catalog-flows.md`, `batch-2-product-catalog-components.md`, `batch-2-product-catalog-domain-concepts.md`

**Special attention**:
- `addproduct()`: brand-to-bid mapping (samsung=1, sony=2, lenovo=3, acer=4, onida=5), category-to-cid mapping (laptop=1, tv=2, mobile=3, watch=4) — these are hardcoded business rules
- Image upload validation: `MyUtilities.UploadFile()` with allowed extensions `.jpg,.bmp,.jpeg,.png,.webp`
- `getSelecteditem()`: filters `viewlist WHERE Pimage = ?` — item identity is image filename
- `viewlist` is a denormalized view — document its composition
- Category-specific DB views: `tv`, `laptop`, `mobile`, `watch` tables — understand if views or tables
- `UPLOAD_PATH` environment variable controls image storage path
- Cross-domain: `product`/`viewlist` tables read by Cart and Order flows

---

#### Batch 3 — Shopping Cart

**Scope**:
- Servlets: `addtocart`, `addtocartnull`, `addtocartnulla`, `removecart`, `removecarta`, `removecartnull`, `removecartnulla`
- DAOs: `DAO2.checkaddtocartnull()`, `DAO2.updateaddtocartnull()`, `DAO2.addtocartnull()`, `DAO2.getSelectedcart()`, `DAO2.getcart()`, `DAO2.removecartnull()`, `DAO2.removecart()`, `DAO3.checkaddtocartnull()`, `DAO3.updateaddtocartnull()`, `DAO3.addtocartnull()`, `DAO4.checkcart()`, `DAO4.checkcart2()`
- JSPs: `cart`, `carta`, `cartnull`, `cartnulla`, `cartnullqty`

**Output**: `docs/discovery/batch-3-shopping-cart-flows.md`, `batch-3-shopping-cart-components.md`, `batch-3-shopping-cart-domain-concepts.md`

**Special attention**:
- Guest cart (`Name IS NULL`) vs customer cart (`Name = email`) — dual-path business rule
- Duplicate detection before add: `checkaddtocartnull()` with WHERE on all product fields
- Increment logic: `UPDATE cart SET pquantity = (pquantity + 1) WHERE ...` on duplicate
- `DAO2.checkaddtocartnull()` uses `Name IS NULL` while `DAO3.checkaddtocartnull()` uses `Name = ?` — document both variants
- `cartnullqty.jsp` — document what triggers quantity-exceeded state
- Remove cart: guest uses `pimage` only, customer uses `Name AND pimage`
- Cross-domain: `cart` table is consumed by Order Management (Batch 4) and Admin Operations (Batch 6)

---

#### Batch 4 — Order Management & Payment

**Scope**:
- Servlets: `ShippingAddress2`, `payprocess`, `remove_orders`, `removeorders`
- DAOs: `DAO4.addOrders()`, `DAO4.addOrder_details()`, `DAO4.addOrder_details2()`, `DAO4.deletecart()`, `DAO4.deletecart2()`, `DAO4.updateOrder_details()`, `DAO4.updateOrder_details2()`, `DAO3.getOrders()`, `DAO3.getOrdersbydate()`, `DAO3.getOrderdetails()`, `DAO2.removeorders()`
- JSPs: `ShippingAddress`, `confirmpayment`, `confirmonline`, `orders`, `orderdetails`, `paymentfail`

**Output**: `docs/discovery/batch-4-orders-payment-flows.md`, `batch-4-orders-payment-components.md`, `batch-4-orders-payment-domain-concepts.md`

**Special attention**:
- Guest checkout flow (`addOrder_details()` — INSERT from `cart WHERE Name IS NULL`) vs customer checkout flow (`addOrder_details2()` — INSERT from `cart WHERE Name = ?`)
- `updateOrder_details()` — sets `Date` and `Name` WHERE `Date IS NULL` — guest post-order date assignment
- `updateOrder_details2()` — sets only `Date` WHERE `Date IS NULL` — customer post-order date assignment
- Cart deletion after checkout: `deletecart()` (guest) vs `deletecart2(name)` (customer)
- `orders.Status` field — enumerate all status values in codebase
- `getOrders()` filters by `Customer_Name = ?`; `getOrdersbydate()` filters by `Date = ?`
- `getOrderdetails()` filters `Order_details WHERE Date = ?`
- Payment flow: `ShippingAddress2` → `payprocess` — document decision points (COD vs online)
- Cross-domain: reads from `cart`, writes to `orders` and `order_details`; cross-references `customer` for name

---

#### Batch 5 — Admin Operations & Contact Us

**Scope**:
- Servlets: `removetable_cart`, `removetable_order_details`, `addContactus`, `addContactusc`, `remove_contactus`
- DAOs: `DAO5.getAllcart()`, `DAO5.getAllorders()`, `DAO5.getAllorder_details()`, `DAO5.removeorder_details()`, `DAO5.addContactus()`, `DAO5.getcontactus()`, `DAO5.removecont()`
- JSPs: `managetables`, `table_cart`, `table_orders`, `table_order_details`, `table_contactus`, `adminhome`, `contactus`, `contactusc`

**Output**: `docs/discovery/batch-5-admin-contactus-flows.md`, `batch-5-admin-contactus-components.md`, `batch-5-admin-contactus-domain-concepts.md`

**Special attention**:
- `DAO5.getAllcart()` — full cart table read (all users), admin view
- `DAO5.getAllorders()` — all orders, no filter — admin-only data exposure
- `removeorder_details()` — deletes by `Date AND pimage` composite key
- `removecont()` — deletes by `id` (auto-increment PK in Contactus)
- `addContactus` vs `addContactusc` — guest vs customer paths for Contact Us submission
- Admin home (`adminhome.jsp`) — entry point for admin dashboard
- Cross-domain: reads `cart`, `orders`, `order_details`, `Contactus` — all tables

---

#### Consolidation Step (after all batches)

**Tasks**:
1. Merge all batch outputs into a unified component inventory
2. Build cross-domain table matrix (each table → which batches read/write it)
3. Flag undocumented business rules and gaps for verification phase

**Output**: `docs/discovery/cross-domain-table-matrix.md`, `docs/discovery/consolidation-gaps.md`

---

### Phase 2: Business Documenter

**Agent**: `business-documenter`  
**Tasks**:
1. Define actors (Guest, Customer, Admin)
2. Create use cases per domain (UC_AUTH_001–UC_AUTH_002, UC_CUST_001–UC_CUST_003, UC_PROD_001–UC_PROD_005, UC_CART_001–UC_CART_004, UC_ORDER_001–UC_ORDER_003, UC_ADMIN_001–UC_ADMIN_005, UC_CONT_001–UC_CONT_002)
3. Write business requirements (BUREQs) per domain
4. Create BPMN process diagrams for key flows (checkout, cart management, authentication)
5. Write business overview

**Output**: `docs/business/index.md`, `docs/business/overview/`, `docs/business/use-cases/`, `docs/business/processes/`

---

### Phase 3: Technical / Functional Documenter

**Agent**: `technical-documenter`  
**Tasks**:
1. Derive functional requirements (FUREQs) from BUREQs
2. Document non-functional requirements (NFUREQs: security, session management, file upload limits)
3. Create technical flow diagrams (servlet request/response chains)
4. Document API endpoints (servlet URL mappings and parameter contracts)
5. Document data schemas (entity field definitions, DB table structures)
6. Document validation rules (login, registration, product upload, cart operations)

**Output**: `docs/functional/index.md`, `docs/functional/requirements/`, `docs/functional/flows/`, `docs/functional/integration/`

---

### Phase 4: Doc Coordinator

**Agent**: `doc-coordinator`  
**Tasks**:
1. Validate directory structure and create missing indexes
2. Create master landing page (`docs/index.md`)
3. Create system overview (`docs/system-overview.md`)
4. Build traceability matrix (BUREQ ↔ FUREQ ↔ Flow ↔ Servlet ↔ DAO)
5. Create domain concepts catalog and ubiquitous language glossary

**Output**: `docs/index.md`, `docs/system-overview.md`, `docs/domain/`, `docs/traceability/`

---

### Phase 5: Verification

**Agent**: `verification`  
**Tasks**:

| ID | Task | Description |
|---|---|---|
| V-1 | Build table usage matrix | Map every DB table to all servlets/DAOs/JSPs that read or write it |
| V-2 | Repository query deep analysis | Compare every WHERE clause in all DAO methods against documented business rules |
| V-3 | Validation completeness check | Enumerate all validations (login, registration, product, cart, order) with true/false outcomes |
| V-4 | Cross-domain dependency verification | Verify `cart` → `orders`/`order_details` flow is bidirectionally linked in docs |
| V-5 | Entity state machine verification | Map `orders.Status` and any other status fields through all transitions |
| V-6 | Generate consolidated gap report | Classify gaps as Critical/High/Medium/Low with remediation prompts |

**Output**: `docs/verification/gap-report.md`, `docs/verification/table-usage-matrix.md`, `docs/verification/cross-domain-dependencies.md`

---

### Phase 6: Remediation (Conditional)

**Triggered by**: Verification finding Critical or High severity gaps  
**Action**: Re-run Discovery Agent on specific batches using targeted prompts from gap report  
**Followed by**: Re-run Verification Agent to confirm resolution

---

## Effort Estimate

| Phase | Agent | Est. Tasks | Complexity |
|---|---|---|---|
| Planning | Planning Agent | 1 | Low |
| Discovery — 5 Batches | Discovery Agent | 35 (5×7) | High |
| Discovery — Consolidation | Discovery Agent | 3 | Medium |
| Business | Business Documenter | 5 | Medium |
| Technical | Technical Documenter | 6 | High |
| Coordination | Doc Coordinator | 5 | Medium |
| Verification | Verification Agent | 6 | High |
| Remediation (if needed) | Discovery + Verification | TBD | Variable |
| **Total (baseline)** | | **61** | |

---

## Completeness Criteria

### Per-Flow Completeness Criteria

- [ ] ALL WHERE clauses in DAO methods called by the flow are documented as business rules
- [ ] ALL validation conditions have both true AND false outcomes documented
- [ ] Cookie creation and deletion logic is fully documented (field name, value, maxAge)
- [ ] Guest vs customer dual-path logic is documented at every branch point
- [ ] Cross-domain table dependencies are linked (upstream producers, downstream consumers)
- [ ] Error/redirect paths are documented (e.g., `fail.jsp`, `failc.jsp`, `paymentfail.jsp`)
- [ ] Hardcoded business rules (brand-ID mapping, category-ID mapping) are explicitly listed

### Per-Domain Completeness Criteria

- [ ] All flows in domain meet per-flow criteria
- [ ] Cross-domain dependencies are bidirectionally linked
- [ ] Business use cases reference all relevant flows (including cross-domain inputs)
- [ ] Functional requirements cover all business rules extracted from DAO queries

### Overall System Completeness Criteria

- [ ] All 21 servlets appear in at least one documented flow
- [ ] All 14 DB tables appear in the table usage matrix
- [ ] All 5 DAO classes are covered with documented methods
- [ ] `orders.Status` state machine is fully enumerated
- [ ] Authentication bypass paths are documented (what happens with missing/invalid cookie)
- [ ] File upload path configuration (`UPLOAD_PATH` env var) is documented

---

## Output Directory Structure

```
docs/
├── ecommerce-state.json                    # State tracking file
├── documentation-plan.md                  # This plan
├── index.md                               # Master landing page
├── system-overview.md                     # Architecture overview
├── discovery/
│   ├── batch-1-auth-customer-flows.md
│   ├── batch-1-auth-customer-components.md
│   ├── batch-1-auth-customer-domain-concepts.md
│   ├── batch-2-product-catalog-flows.md
│   ├── batch-2-product-catalog-components.md
│   ├── batch-2-product-catalog-domain-concepts.md
│   ├── batch-3-shopping-cart-flows.md
│   ├── batch-3-shopping-cart-components.md
│   ├── batch-3-shopping-cart-domain-concepts.md
│   ├── batch-4-orders-payment-flows.md
│   ├── batch-4-orders-payment-components.md
│   ├── batch-4-orders-payment-domain-concepts.md
│   ├── batch-5-admin-contactus-flows.md
│   ├── batch-5-admin-contactus-components.md
│   ├── batch-5-admin-contactus-domain-concepts.md
│   ├── cross-domain-table-matrix.md
│   └── consolidation-gaps.md
├── business/
│   ├── index.md
│   ├── overview/
│   ├── use-cases/
│   └── processes/
├── functional/
│   ├── index.md
│   ├── requirements/
│   ├── flows/
│   └── integration/
├── domain/
│   ├── domain-concepts-catalog.md
│   ├── ubiquitous-language.md
│   └── bounded-contexts.md
├── verification/
│   ├── gap-report.md
│   ├── table-usage-matrix.md
│   └── cross-domain-dependencies.md
└── traceability/
    ├── requirement-matrix.md
    ├── flow-to-component-map.md
    └── id-registry.md
```
