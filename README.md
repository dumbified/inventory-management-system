## Inventory Management System

[![Java](https://img.shields.io/badge/Java-ED8B00?logo=openjdk&logoColor=white)](https://www.java.com/)
[![Swing](https://img.shields.io/badge/GUI-Swing-blue)](https://docs.oracle.com/javase/8/docs/technotes/guides/swing/)
[![MySQL](https://img.shields.io/badge/Database-MySQL-4479A1?logo=mysql&logoColor=white)](https://www.mysql.com/)

A simple Java desktop application (Swing) for managing products, inventory, and sales with a user‑friendly GUI.  
Created for an Object‑Oriented Programming (OOP) assignment.

### Features

- **Product management**: Add, update, delete, and view products
- **Inventory reports**: Generate basic inventory summaries
- **Sales management**: Record sales and view sales history
- **Low stock alerts**: Highlight products that are running low

### Prerequisites

- **Java JDK** (version 8+ recommended)
- **MySQL Server**
- **MySQL JDBC connector JAR** (`mysql-connector-j`), added to your project’s classpath / reference libraries

### Database config.

This app expects a MySQL database with your own configurations (see `src/database/DatabaseConnection.java`)

### Compilation

From the project root:

1. **Compile**
   - `cd src`
   - `javac Main.java`
2. **Run**
   - `java Main`


> Make sure the MySQL connector JAR is on the classpath; otherwise, the database connection will fail.

### Default Credentials

- **username**: `admin`
- **password**: `123456`
