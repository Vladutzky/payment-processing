# Payment Processing System

A robust and scalable payment processing system built using **Spring Boot** and documented with **Swagger**.

---

## 📈 Business Requirements

1. Customers can be added, removed, and updated.
2. Merchants can be added, removed, and updated.
3. Payment methods can be added, removed, and updated.
4. Once a payment is added, a transaction and an invoice are also created.
5. Merchants can view their total revenue.
6. Customers can view their total spending.
7. Customers can see the percentage of each payment method they have used.
8. Customers can refund a payment.
9. The top-spending customer can be identified.
10. The top-performing merchants can be listed.
11. The system provides complete API documentation using Swagger.

---

## 🎯 MVP - Core Features

1. **Customer Management** – Full CRUD operations for `Customer`.
2. **Merchant Management** – Full CRUD operations for `Merchant`.
3. **Payment Method Management** – Full CRUD operations for `PaymentMethod`.
4. **Payment Management** – Full CRUD operations for `Payment`, including refund functionality.
5. **Invoice Management** – Full CRUD operations for `Invoice`.
6. **Transaction Management** – Full CRUD operations for `Transaction`.
7. **Revenue and Spending Analytics**:
   - Total revenue for merchants.
   - Total spending for customers.
   - Payment method usage percentage for customers.
8. **Top Rankings**:
   - Top-spending customer.
   - Top-performing merchants.
9. **API Documentation (Swagger)** – Complete documentation for all available endpoints.

---

## 📦 Entities and Relationships

The Payment Processing System includes the following entities and their relationships:

1. **Customer**
    - `id` (Long, Primary Key)
    - `name` (String)
    - `email` (String)
    - **Relationships:**
      - `Customer` ↔️ `Invoice` – One-to-Many (a customer can have multiple invoices).
      - `Customer` ↔️ `Transaction` – One-to-Many (a customer can have multiple transactions).
      - `Customer` ↔️ `Payment` – One-to-Many (a customer can make multiple payments).

2. **Merchant**
   - `id` (Long, Primary Key)
   - `merchantName` (String)
   - `merchantEmail` (String)
   - **Relationships:**
     - `Merchant` ↔️ `Payment` – One-to-Many (a merchant can receive multiple payments).
     - `Merchant` ↔️ `Transaction` – One-to-Many (a merchant can be involved in multiple transactions).
     
3. **Invoice**
   - `id` (Long, Primary Key)
   - `totalAmount` (Double)
   - `issuedAt` (LocalDateTime)
   - **Relationships:**
     - `Invoice` ↔️ `Customer` – Many-to-One (an invoice is associated with one customer).
     - `Invoice` ↔️ `Payment` – One-to-Many (an invoice can have multiple payments).
     - `Invoice` ↔️ `Transaction` – One-to-Many (a transaction is linked to one invoice).
     
4. **Payment**
   - `id` (Long, Primary Key)
   - `payerName` (String)
   - `amount` (Double)
   - `paymentDate` (LocalDateTime)
   - `refunded` (Boolean)
   - **Relationships:**
     - `Payment` ↔️ `Invoice` – Many-to-One (a payment is linked to one invoice).
     - `Payment` ↔️ `Merchant` – Many-to-One (a payment is made to one merchant).
     - `Payment` ↔️ `PaymentMethod` – Many-to-One (a payment is made using one payment method).
     - `Payment` ↔️ `Customer` – Many-to-One (a payment is made by one customer).
     - `Payment` ↔️ `Transaction` – One-to-One (each payment has an associated transaction).
     
5. **PaymentMethod**
   - `id` (Long, Primary Key)
   - `methodName` (String)
   - **Relationships:**
     - `PaymentMethod` ↔️ `Payment` – One-to-Many (a payment method can be used in multiple payments).
     
6. **Transaction**
   - `id` (Long, Primary Key)
   - `transactionAmount` (Double)
   - `transactionDate` (LocalDateTime)
   - **Relationships:**
     - `Transaction` ↔️ `Customer` – Many-to-One (a transaction is linked to one customer).
     - `Transaction` ↔️ `Invoice` – Many-to-One (a transaction is linked to one invoice).
     - `Transaction` ↔️ `Merchant` – Many-to-One (a transaction is linked to one merchant).
     - `Transaction` ↔️ `Payment` – One-to-One (a transaction is associated with one payment).

---

## 📊 Entity Relationship Diagram (ERD)

```plaintext
[Customer] 1 --- * [Invoice] 1 --- * [Payment] 1 --- 1 [Transaction]
       |                  |                        |
       |                  |                        |
       *                  *                        1
  [Transaction]      [PaymentMethod]         [Merchant]
