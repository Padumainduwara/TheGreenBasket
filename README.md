# 🛒 The Green Basket - Inventory Management System

The Green Basket is a Java Swing-based application designed to manage supermarket inventory efficiently. This system utilizes **Object-Oriented Programming (OOP)** principles to handle user authentication, product management, and stock monitoring.

## 🚀 Features
* **Role-Based Access Control:**
    * **Store Manager:** Full access to Manage Users (Add/Remove Assistants) and view Inventory Alerts.
    * **Sales Assistant:** Restricted access to perform product sales and updates.
* **CRUD Operations:** Add, Edit, Delete, and Update Quantity of products.
* **Data Persistence:** Uses **Java Serialization** (`.txt` files) to save and retrieve data securely without a database.
* **Input Validation:** Prevents duplicate IDs and invalid data entries.
* **Low Stock Alerts:** Automatically identifies and highlights products with low quantity.

## 🛠️ Technology Stack
* **Language:** Java (JDK 8+)
* **GUI:** Java Swing (JFrame, JTable, CardLayout)
* **Data Storage:** File Handling (ObjectOutputStream/ObjectInputStream)

## 🏗️ OOP Concepts Implemented
This project demonstrates the core pillars of OOP:
1.  **Encapsulation:** Data security is ensured using `private` variables in the `Product` class with Getters and Setters.
2.  **Inheritance:** `SalesAssistant` and `StoreManager` classes inherit common properties from the abstract `User` class.
3.  **Polymorphism:** The Dashboard interface changes dynamically based on the logged-in user's role (Manager vs. Assistant).
4.  **Abstraction:** The `User` class is abstract to prevent direct instantiation, enforcing specific role creation.

## 🔑 Login Credentials (For Testing)
Use the following default credentials to run the system:

| Role | Username | Password |
| :--- | :--- | :--- |
| **Store Manager** | `admin` | `123` |
| **Sales Assistant** | `user` | `123` |

## 📦 How to Run
1.  Clone the repository.
2.  Open the project in **NetBeans** or any Java IDE.
3.  Run the `GreenBasketGUI.java` file located in `src/greenbasket/`.
