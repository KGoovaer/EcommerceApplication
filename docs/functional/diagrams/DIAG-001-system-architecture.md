# System Architecture Diagram

**Diagram ID:** DIAG-001  
**Type:** C4 Context + Component  
**Traced To:** INT-001, INT-002, All FUREQs  

---

## C4 Context Diagram

```mermaid
C4Context
    title System Context — EcommerceApplication

    Person(guest, "Guest", "Anonymous visitor browsing products and submitting enquiries")
    Person(customer, "Customer", "Registered shopper placing orders and managing their cart")
    Person(admin, "Administrator", "Internal user managing products, customers, and orders")

    System(webapp, "EcommerceApplication", "Java EE web application providing e-commerce functionality — product browsing, cart, checkout, admin operations")
    System_Ext(sqlite, "SQLite Database", "File-based relational database storing all application data")
    System_Ext(filesystem, "File System", "Local Tomcat server storage for product images")

    Rel(guest, webapp, "Browses products, adds to guest cart, submits enquiries", "HTTP")
    Rel(customer, webapp, "Registers, logs in, shops, checks out, views orders", "HTTP")
    Rel(admin, webapp, "Manages products, customers, orders, enquiries", "HTTP")
    Rel(webapp, sqlite, "Reads and writes all persistent data", "JDBC / SQLite")
    Rel(webapp, filesystem, "Stores and serves product images", "File I/O")
```

---

## Component Architecture Diagram

```mermaid
flowchart TD
    subgraph Browser["Browser (HTTP Client)"]
        JSP["JSP Pages\n(Presentation Layer)"]
    end

    subgraph Tomcat["Apache Tomcat 8+"]
        subgraph Servlets["Servlet Layer\n(com.servlet.*)"]
            AUTH["Authentication\naddcustomer, checkcustomer\ncheckadmin"]
            CART["Cart Management\naddtocart, addtocartnull\nremovecart, removecartnull"]
            ORDER["Order Processing\nShippingAddress2, payprocess\nremoveorders"]
            ADMIN["Admin Operations\naddproduct, deletecustomer\nremove_contactus"]
            CONTACT["Contact Us\naddContactus, addContactusc"]
        end

        subgraph DAOs["DAO Layer\n(com.dao.*)"]
            DAO["DAO\nProduct ops"]
            DAO2["DAO2\nAuth + Customer"]
            DAO3["DAO3\nCart ops"]
            DAO4["DAO4\nOrder ops"]
            DAO5["DAO5\nContact ops"]
        end

        subgraph Util["Utility\n(com.utility.*)"]
            MYUTIL["MyUtilities\nUploadFile()"]
        end

        subgraph Conn["Connection\n(com.conn.*)"]
            DBCONN["DBConnect\nStatic singleton\nConnection"]
        end
    end

    subgraph Data["External Resources"]
        SQLITE[("SQLite\nmydatabase.db")]
        FS["File System\n/images/"]
    end

    Browser <-->|HTTP| Tomcat
    AUTH --> DAO2
    CART --> DAO3
    ORDER --> DAO4
    ADMIN --> DAO
    ADMIN --> DAO2
    ADMIN --> MYUTIL
    CONTACT --> DAO5
    DAO --> DBCONN
    DAO2 --> DBCONN
    DAO3 --> DBCONN
    DAO4 --> DBCONN
    DAO5 --> DBCONN
    DBCONN --> SQLITE
    MYUTIL --> FS
```

---

## Servlet URL Map

```mermaid
flowchart LR
    subgraph Auth["Authentication"]
        A1["/addcustomer\nPOST"]
        A2["/checkcustomer\nPOST"]
        A3["/checkadmin\nPOST"]
    end
    subgraph Cart["Cart"]
        C1["/addtocart\nPOST"]
        C2["/addtocartnull\nPOST"]
        C3["/removecart\nGET"]
        C4["/removecartnull\nGET"]
        C5["/removecarta\nGET"]
        C6["/removecartnulla\nGET"]
        C7["/removetable_cart\nGET"]
    end
    subgraph Orders["Orders"]
        O1["/ShippingAddress2\nPOST"]
        O2["/payprocess\nPOST"]
        O3["/removeorders\nGET"]
        O4["/remove_orders\nGET"]
        O5["/removetable_order_details\nGET"]
    end
    subgraph Admin["Admin"]
        AD1["/addproduct\nPOST"]
        AD2["/deletecustomer\nGET"]
        AD3["/remove_contactus\nGET"]
    end
    subgraph Contact["Contact"]
        CO1["/addContactus\nPOST"]
        CO2["/addContactusc\nPOST"]
    end
```
