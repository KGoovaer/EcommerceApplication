# System Overview — EcommerceApp

**Document ID:** SO-001  
**Version:** 1.1  
**Phase:** Coordination (second pass, post-verification remediation)  
**Traced To:** DIAG-001, INT-001, INT-002, All FUREQs  

---

## Purpose

EcommerceApp is an online electronic retail platform built with Java EE (Servlet 3.0 + JSP) deployed on Apache Tomcat 8+. It supports three actor types — **Guest**, **Customer**, and **Admin** — across the full e-commerce lifecycle: product discovery, cart management, checkout, order tracking, and administrative operations.

---

## C4 Context Diagram

```mermaid
C4Context
    title System Context — EcommerceApp

    Person(guest, "Guest", "Anonymous visitor. Browses products, adds to shared cart, submits enquiries.")
    Person(customer, "Customer", "Registered shopper. Personal cart, checkout, order history, enquiries.")
    Person(admin, "Administrator", "Internal staff. Product management, customer/order data, enquiry management.")

    System(webapp, "EcommerceApp", "Java EE web application providing e-commerce functionality — browsing, cart, checkout, admin operations — deployed on Apache Tomcat 8+.")
    System_Ext(sqlite, "SQLite Database", "File-based relational database (mydatabase.db) storing all application data via JDBC.")
    System_Ext(filesystem, "File System", "Local Tomcat server storage for product images uploaded by admins.")

    Rel(guest, webapp, "Browses products, adds to guest cart, submits enquiries", "HTTP")
    Rel(customer, webapp, "Registers, logs in, shops, checks out, views and cancels orders", "HTTP")
    Rel(admin, webapp, "Manages products, customers, orders, and enquiries", "HTTP")
    Rel(webapp, sqlite, "Reads and writes all persistent data", "JDBC / SQLite")
    Rel(webapp, filesystem, "Stores and serves product images", "File I/O")
```

---

## C4 Container Diagram

```mermaid
C4Container
    title Container Diagram — EcommerceApp

    Person(guest, "Guest")
    Person(customer, "Customer")
    Person(admin, "Administrator")

    Container(jsps, "JSP Pages", "Java Server Pages", "Presentation layer. 65 JSPs using <%@ include %> for shared navbars. Tripling pattern: guest / customer / admin variants.")
    Container(servlets, "Servlet Layer", "Java Servlet 3.0 (@WebServlet)", "21 HTTP servlets handling all business logic. URL-mapped via annotations. No web.xml routing.")
    Container(daos, "DAO Layer", "Plain JDBC", "5 DAO classes (DAO–DAO5) providing all database operations via PreparedStatement.")
    Container(utility, "Utility", "Java", "MyUtilities.UploadFile() — validates and writes product image files.")
    Container(dbconn, "DBConnect", "Java (Static Singleton)", "Static Connection singleton providing JDBC connection to SQLite. Not thread-safe.")

    ContainerDb(sqlite, "SQLite Database", "SQLite via xerial JDBC", "14 tables: customer, product, brand, category, cart, orders, order_details, Contactus, viewlist, mobile, tv, laptop, watch, usermaster.")
    ContainerDb(filesystem, "File System", "Local Disk", "Product images stored in Tomcat's images/ directory.")

    Rel(guest, jsps, "HTTP requests", "HTTP")
    Rel(customer, jsps, "HTTP requests", "HTTP")
    Rel(admin, jsps, "HTTP requests", "HTTP")
    Rel(jsps, servlets, "Form submissions / GET links", "HTTP")
    Rel(servlets, daos, "Instantiates DAOx(DBConnect.getConn())", "Method calls")
    Rel(servlets, utility, "File upload delegation", "Method calls")
    Rel(daos, dbconn, "Acquires connection", "Method calls")
    Rel(utility, filesystem, "Writes image files", "File I/O")
    Rel(dbconn, sqlite, "JDBC connection", "JDBC / SQLite")
```

---

## Component Flow Diagram

```mermaid
flowchart TD
    subgraph Browser["Browser"]
        JSP["JSP Presentation Layer\n65 JSPs\n(navbar.jsp / customer_navbar.jsp / admin_navbar.jsp)"]
    end

    subgraph Tomcat["Apache Tomcat 8+"]
        subgraph Servlets["Servlet Layer (com.servlet.*)"]
            AUTH["Authentication\naddcustomer · checkcustomer · checkadmin"]
            CATALOG["Catalogue\n(JSP-direct — no servlet wrapper)"]
            CART["Cart Management\naddtocart · addtocartnull\nremovecart · removecartnull"]
            ORDER["Order Processing\nShippingAddress2 · payprocess\nremoveorders · remove_orders"]
            ADMIN["Admin Operations\naddproduct · deletecustomer\nremove_contactus · removetable_*"]
            CONTACT["Contact Us\naddContactus · addContactusc"]
        end

        subgraph DAOs["DAO Layer (com.dao.*)"]
            DAO["DAO — Product & Admin ops"]
            DAO2["DAO2 — Auth & Customer"]
            DAO3["DAO3 — Cart ops"]
            DAO4["DAO4 — Order ops"]
            DAO5["DAO5 — Contact Us"]
        end

        UTIL["MyUtilities\n(com.utility)"]
        DBCONN["DBConnect\n(Static singleton Connection)"]
    end

    subgraph External["External Resources"]
        SQLITE[("SQLite\nmydatabase.db")]
        FS["File System\n/images/"]
    end

    Browser <-->|HTTP| Tomcat
    AUTH --> DAO2
    CART --> DAO3
    ORDER --> DAO4
    ADMIN --> DAO
    ADMIN --> DAO2
    ADMIN --> UTIL
    CONTACT --> DAO5
    DAO --> DBCONN
    DAO2 --> DBCONN
    DAO3 --> DBCONN
    DAO4 --> DBCONN
    DAO5 --> DBCONN
    DBCONN --> SQLITE
    UTIL --> FS
```

---

## Domain Model Overview

```mermaid
classDiagram
    class Customer {
        +String Name
        +String Password
        +String Email_Id
        +String Contact_No
    }
    class UserMaster {
        +String Username
        +String Password
    }
    class Product {
        +int pid
        +String pname
        +int pprice
        +int pquantity
        +String pimage
        +int bid
        +int cid
    }
    class Brand {
        +int bid
        +String bname
    }
    class Category {
        +int cid
        +String cname
    }
    class Cart {
        +String CusName
        +String Pname
        +String Category
        +String Brand
        +int Price
        +int Quantity
        +String pimage
    }
    class Orders {
        +int oid
        +String CusName
        +String City
        +String Date
        +int TotalPrice
        +String Status
    }
    class OrderDetails {
        +int oid
        +String Pname
        +String Category
        +String Brand
        +int Price
        +int Quantity
    }
    class ContactUs {
        +String Name
        +String Email
        +String Phone
        +String Message
    }
    class ViewList {
        +String Pname
        +String Category
        +String Brand
        +int Price
        +String Pimage
    }

    Product --> Brand : has
    Product --> Category : belongs to
    Customer "1" --> "*" Cart : owns
    Customer "1" --> "*" Orders : places
    Orders "1" --> "*" OrderDetails : contains
    Product ..> Cart : referenced in
    Product ..> OrderDetails : referenced in
    Product ..> ViewList : aggregated in
```

---

## Deployment Architecture

```mermaid
flowchart LR
    subgraph Server["Application Server"]
        direction TB
        TC["Apache Tomcat 8+"]
        WAR["EcommerceApp-0.0.1-SNAPSHOT.war"]
        DB[("mydatabase.db\nSQLite")]
        IMG["/images/\nProduct images"]
        TC --> WAR
        WAR --- DB
        WAR --- IMG
    end

    Browser["Browser\n(HTTP client)"] -->|"HTTP :8080/EcommerceApp"| TC
```

**Deployment Notes:**
- The WAR is built via `mvn package` from the `EcommerceApp/` directory.
- Before deployment: update the hardcoded SQLite path in `DBConnect.java` and image upload path in `DAO.java`.
- Auth is cookie-based (`cname` = customer email, `tname` = admin username); cookies have `maxAge=9999` for persistence.
- Flash messages use short-lived cookies (`maxAge=10`) for redirect feedback.

---

## Servlet URL Routing

```mermaid
flowchart LR
    subgraph Auth["Authentication"]
        A1["POST /addcustomer"]
        A2["POST /checkcustomer"]
        A3["POST /checkadmin"]
    end
    subgraph Cart["Cart"]
        C1["GET /addtocart"]
        C2["GET /addtocartnull"]
        C3["GET /removecart"]
        C4["GET /removecartnull"]
    end
    subgraph Order["Order"]
        O1["POST /ShippingAddress2"]
        O2["POST /payprocess"]
        O3["GET /removeorders"]
        O4["GET /remove_orders"]
    end
    subgraph Admin["Admin"]
        AD1["POST /addproduct"]
        AD2["GET /deletecustomer"]
        AD3["GET /remove_contactus"]
        AD4["GET /removetable_cart"]
        AD5["GET /removetable_orderdetail"]
    end
    subgraph Contact["Contact Us"]
        CO1["POST /addContactus"]
        CO2["POST /addContactusc"]
    end
```

---

## Technology Stack Summary

| Layer | Technology | Notes |
|---|---|---|
| Language | Java (J2EE) | Servlet 3.0 API |
| Presentation | JSP (Scriptlets) | 65 JSPs; tripling pattern for guest/customer/admin |
| Routing | `@WebServlet` annotations | 21 servlets; no `web.xml` URL mappings |
| Data Access | Plain JDBC (`PreparedStatement`) | 5 DAO classes; no ORM |
| Database | SQLite via `org.xerial:sqlite-jdbc` | Single file DB; static connection singleton |
| Build | Apache Maven (WAR packaging) | Output: `EcommerceApp-0.0.1-SNAPSHOT.war` |
| Server | Apache Tomcat 8+ | Standard servlet container |
| Authentication | Browser cookies | `cname` (customer email), `tname` (admin name) |
| File Storage | Local file system | Product images in Tomcat `images/` directory |

---

## Known Architectural Constraints

| Constraint | Impact |
|---|---|
| Static `DBConnect` singleton | Not thread-safe; concurrent requests may corrupt DB state |
| Hardcoded brand/category mappings | New brands/categories require code changes |
| Cookie-based auth (no `HttpOnly`/`Secure`) | Susceptible to XSS-based session theft |
| Plaintext passwords in DB | No password hashing |
| No transaction management | Order creation is a multi-step non-atomic operation |
| File upload uses original filename | Path traversal risk if filename contains `../` |

---

*See also: [DIAG-001 System Architecture](functional/diagrams/DIAG-001-system-architecture.md) | [INT-001 Database](functional/integration/INT-001-database.md) | [INT-002 File System](functional/integration/INT-002-file-system.md)*
