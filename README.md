# Hibernate ORM Implementation - E-commerce System

This project is a Hibernate-based Java application managing an e-commerce system with entities: `Category`, `Product`, `User`, `Order`, and `OrderDetail`. It demonstrates complex entity mappings, CRUD operations, and advanced Hibernate query strategies.

## 📂 Project Structure

```text
Hibernate-ORM/
├── .gitignore
├── pom.xml                                 # Maven dependencies and build configuration
├── schema.sql                              # Raw SQL script for manual database creation
├── README.md                               # Project documentation
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/ecommerce/
    │   │       ├── Main.java               # Main entry point to demonstrate CRUD operations
    │   │       ├── dao/
    │   │       │   └── EcommerceDAO.java   # Data Access Object containing all Hibernate queries
    │   │       ├── entity/
    │   │       │   ├── Category.java       # Entity: One-To-Many with Product
    │   │       │   ├── Order.java          # Entity: Many-To-One (User), One-To-Many (OrderDetail)
    │   │       │   ├── OrderDetail.java    # Entity: Many-To-One (Order, Product)
    │   │       │   ├── Product.java        # Entity: Many-To-One (Category). Soft delete & Named Queries
    │   │       │   └── User.java           # Entity: One-To-Many (Order). BCrypt password hashing
    │   │       └── util/
    │   │           └── HibernateUtil.java  # Hibernate SessionFactory configuration
    │   └── resources/
    │       └── hibernate.cfg.xml           # Main Hibernate config (MySQL setup)
    └── test/
        ├── java/
        │   └── com/ecommerce/
        │       └── EcommerceDAOTest.java   # JUnit 5 Test Class covering full flow
        └── resources/
            └── hibernate.cfg.xml           # Test Hibernate config (H2 in-memory DB)
```

## 🚀 Features Implemented
- **Entity Mappings**: Utilized standard JPA annotations (`@Entity`, `@Table`, `@OneToMany`, `@ManyToOne`, `@JoinColumn`).
- **Cascading & Fetching**: Implemented `CascadeType.ALL` and `orphanRemoval = true` where appropriate (e.g., Orders -> OrderDetails), and `FetchType.LAZY` for optimized data fetching.
- **Database Configurations**: 
  - Production/Main runtime uses **MySQL** (`src/main/resources`).
  - Unit Tests use **H2 In-Memory Database** (`src/test/resources`) ensuring tests always pass without external DB setup.
- **Security**: User passwords are automatically hashed using **BCrypt** before database persistence.

### ⭐ Bonus Enhancements Implemented
- **Named Queries**: Defined `@NamedQuery` on `Product` to fetch products by category name.
- **Criteria Queries**: Used `CriteriaBuilder` inside `EcommerceDAO.java` to fetch all active products.
- **Soft Delete**: Added a `deleted` boolean flag on `Product`. Configured `@SQLDelete` to intercept `session.remove()` and `@Where(clause = "deleted=false")` to filter logically deleted items out of queries automatically.
- **Pagination**: Implemented `getProductsPaginated` in DAO using `setFirstResult()` and `setMaxResults()`.

## ⚙️ Setup and Execution Instructions

### Prerequisites
- Java 17+ installed.
- Maven installed (or use an IDE like IntelliJ IDEA/Eclipse which has Maven bundled).
- MySQL Server (optional if you only run tests, required for running `Main.java`).

### 1. Database Configuration (MySQL)
If you want to run `Main.java` against a real database:
1. Create a MySQL database:
   ```sql
   CREATE DATABASE IF NOT EXISTS ecommerce_db;
   ```
   *(You can optionally execute the included `schema.sql` to create tables, but Hibernate is configured with `hbm2ddl.auto=update` and will create them for you).*
2. Open `src/main/resources/hibernate.cfg.xml`.
3. Update `connection.username` and `connection.password` to match your local MySQL credentials.

### 2. Running Unit Tests (No Database Required)
The project includes a JUnit test suite (`EcommerceDAOTest.java`) configured to use an **H2 in-memory database**. This allows you to verify the mappings and CRUD logic without setting up MySQL.
- **From IDE**: Open `EcommerceDAOTest.java` and click the "Run Test" play button.
- **From CLI**: Run the following command in the project root:
  ```bash
  mvn clean test
  ```

### 3. Running the Main Demonstration
The `Main.java` file contains a procedural walkthrough inserting categories, products, a user, executing an order, and demonstrating the bonus features.
- **From IDE**: Open `Main.java` and click "Run".
- **From CLI**:
  ```bash
  mvn clean compile exec:java -Dexec.mainClass="com.ecommerce.Main"
  ```
