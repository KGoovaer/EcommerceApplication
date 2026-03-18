# INT-001: SQLite Database Integration

**Integration ID:** INT-001  
**Version:** 1.0  
**Type:** Relational Database  
**Technology:** SQLite via `org.xerial:sqlite-jdbc`  
**Traced To:** NFUREQ-002, All FUREQs  

---

## Overview

The EcommerceApplication integrates with a local SQLite database file (`mydatabase.db`) as its sole persistent data store. The database connection is managed through a static singleton pattern in `com.conn.DBConnect`.

---

## Connection Configuration

### `com.conn.DBConnect`

```java
// File: EcommerceApp/src/main/java/com/conn/DBConnect.java
public class DBConnect {
    static Connection conn = null;

    public static Connection getConn() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");  // Legacy — not used
            if (conn == null) {
                conn = DriverManager.getConnection(
                    "jdbc:sqlite:/path/to/mydatabase.db"
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}
```

**Key Notes:**
- `Class.forName("com.mysql.cj.jdbc.Driver")` is present as legacy code — the actual connection is SQLite
- Connection path (`/path/to/mydatabase.db`) is hardcoded and must be updated per deployment
- The connection is a static singleton — **not thread-safe**
- Connection is never closed during application lifecycle

---

## Database Schema

### Tables

| Table | Primary Key | Description |
|---|---|---|
| `brand` | `Brand_Id` | Product brand lookup (Samsung, Sony, Lenovo, Acer, Onida) |
| `category` | `Cat_Id` | Product category lookup (Laptop, TV, Mobile, Watch) |
| `product` | `Pro_Id` | Product catalogue |
| `customer` | (Name + Email_Id) | Registered customer accounts |
| `usermaster` | (name) | Admin accounts |
| `cart` | (Name + Pro_image) | Shopping cart (Name=NULL for guest) |
| `orders` | `Order_Id` | Order headers |
| `order_details` | `OD_Id` | Order line items (linked to orders via Date) |
| `Contactus` | `Contact_Id` | Contact enquiries |

### Category Views (Read-Only)

| View | Description |
|---|---|
| `viewlist` | All products with brand and category names (used for home/dashboard) |
| `mobile` | Products where Cat_name = 'Mobile' |
| `tv` | Products where Cat_name = 'TV' |
| `laptop` | Products where Cat_name = 'Laptop' |
| `watch` | Products where Cat_name = 'Watch' |

---

## Entity-Relationship Diagram

```mermaid
classDiagram
    class brand {
        +int Brand_Id PK
        +String Brand_name
    }
    class category {
        +int Cat_Id PK
        +String Cat_name
    }
    class product {
        +int Pro_Id PK
        +String Pro_name
        +double Price
        +int Qty
        +String Pro_image
        +int Brand_Id FK
        +int Cat_Id FK
    }
    class customer {
        +String Name PK
        +String Password
        +String Email_Id
        +String Contact_No
    }
    class usermaster {
        +String name PK
        +String password
    }
    class cart {
        +String Name nullable
        +String Brand_name
        +String Cat_name
        +String Pro_name
        +double Price
        +int Qty
        +String Pro_image
    }
    class orders {
        +int Order_Id PK
        +String Customer_Name
        +String City
        +String Date
        +double Total_Price
        +String Status
    }
    class order_details {
        +int OD_Id PK
        +String Brand_name
        +String Cat_name
        +String Pro_name
        +double Price
        +int Qty
        +String Pro_image
        +String Date
        +String Customer_Name
    }
    class Contactus {
        +int Contact_Id PK
        +String Name
        +String Email_Id
        +String Contact_No
        +String Message
    }

    product "*" --> "1" brand : Brand_Id (no enforced FK)
    product "*" --> "1" category : Cat_Id (no enforced FK)
    cart "*" ..> "0..1" customer : Name (soft ref)
    orders "*" ..> "1" customer : Customer_Name (soft ref)
    order_details "*" ..> "1" orders : Date (soft ref)
```

---

## DAO Layer Architecture

```mermaid
classDiagram
    class DBConnect {
        +static Connection conn
        +static getConn() Connection
    }
    class DAO {
        +Connection conn
        +addproduct(product) int
        +getViewlist() List~viewlist~
    }
    class DAO2 {
        +Connection conn
        +addcustomer(customer) int
        +checkcust2(customer) boolean
        +checkcustomer(customer) customer
        +checkadmin(usermaster) usermaster
        +getcustomer() List~customer~
        +deletecustomer(customer) int
    }
    class DAO3 {
        +Connection conn
        +addtocart(cart) int
        +addtocartnull(cart) int
        +checkcart(cart) boolean
        +checkcart2(cart) boolean
        +addqty(cart) int
        +addqty2(cart) int
        +removecart(cart) int
        +removecartnull(cart) int
        +getCart(String) List~cart~
    }
    class DAO4 {
        +Connection conn
        +addorders(orders) int
        +addorderdetails(order_details) int
        +deletecart(String) int
        +updateorderdetails(orders) int
        +removeorders(orders) int
        +getorders(String) List~cart~
    }
    class DAO5 {
        +Connection conn
        +addcontactus(contactus) int
        +getcontactus() List~contactus~
        +removecontactus(contactus) int
    }

    DAO --> DBConnect : getConn()
    DAO2 --> DBConnect : getConn()
    DAO3 --> DBConnect : getConn()
    DAO4 --> DBConnect : getConn()
    DAO5 --> DBConnect : getConn()
```

---

## Query Inventory

| Operation | Table | SQL Pattern | DAO |
|---|---|---|---|
| Register customer | `customer` | `INSERT INTO customer VALUES (?,?,?,?)` | `DAO2.addcustomer()` |
| Check duplicate | `customer` | `SELECT * WHERE Name=? OR Email_Id=?` | `DAO2.checkcust2()` |
| Login customer | `customer` | `SELECT * WHERE Email_Id=? AND Password=?` | `DAO2.checkcustomer()` |
| Login admin | `usermaster` | `SELECT * WHERE name=? AND password=?` | `DAO2.checkadmin()` |
| Add product | `product` | `INSERT INTO product VALUES (?,?,?,?,?,?)` | `DAO.addproduct()` |
| Check cart dup (customer) | `cart` | `SELECT * WHERE Name=? AND Pro_image=? ...` | `DAO3.checkcart()` |
| Check cart dup (guest) | `cart` | `SELECT * WHERE Name IS NULL AND Pro_image=? ...` | `DAO3.checkcart2()` |
| Add to cart (customer) | `cart` | `INSERT INTO cart VALUES (?,?,?,?,?,1,?)` | `DAO3.addtocart()` |
| Add to cart (guest) | `cart` | `INSERT INTO cart VALUES (NULL,?,?,?,?,1,?)` | `DAO3.addtocartnull()` |
| Increment qty (customer) | `cart` | `UPDATE cart SET Qty=Qty+1 WHERE Name=? AND Pro_image=?` | `DAO3.addqty()` |
| Delete cart item (customer) | `cart` | `DELETE FROM cart WHERE Name=? AND Pro_image=?` | `DAO3.removecart()` |
| Create order | `orders` | `INSERT INTO orders (...) VALUES (?,?,?,?)` | `DAO4.addorders()` |
| Add order details | `order_details` | `INSERT INTO order_details (...) VALUES (?,?,?,?,?,?)` | `DAO4.addorderdetails()` |
| Clear cart | `cart` | `DELETE FROM cart WHERE Name=?` | `DAO4.deletecart()` |
| Update order details | `order_details` | `UPDATE order_details SET Date=?, Customer_Name=? ...` | `DAO4.updateorderdetails()` |
| Cancel order | `orders` | `DELETE FROM orders WHERE Order_Id=?` | `DAO4.removeorders()` |
| Submit enquiry | `Contactus` | `INSERT INTO Contactus VALUES (?,?,?,?)` | `DAO5.addcontactus()` |
| Delete customer | `customer` | `DELETE FROM customer WHERE Name=? AND Email_Id=?` | `DAO2.deletecustomer()` |
