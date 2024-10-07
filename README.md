
# CLminton Shop Management Application

## Overview

CLminton is a badminton racket shop that offers a variety of quality rackets from various brands. This project aims to develop a Java-based application to manage product transactions in the store using a MySQL database. The application features user roles (User and Admin) and supports various functionalities such as product management, cart checkout, and transaction history. The project was developed as part of the Business Application Development course in the Odd Semester 2023/2024 at ISYS6197 | ISYS6197003.

## Table of Contents

1. [Technologies Used](#technologies-used)
2. [Project Features](#project-features)
    - [Login Scene](#login-scene)
    - [Register Scene](#register-scene)
    - [Main Navigation Bar](#main-navigation-bar)
    - [Home Scene](#home-scene)
    - [Cart Scene](#cart-scene)
    - [Transaction Card Pop-up](#transaction-card-pop-up)
    - [History Scene](#history-scene)
    - [Manage Product Scene](#manage-product-scene)
    - [View History Scene](#view-history-scene)
3. [User Roles](#user-roles)
4. [How to Run the Project](#how-to-run-the-project)

## Technologies Used

- Java 11.0.18
- JavaFX 17.0.7
- MySQL Java Connection Library 8.0.24
- XAMPP 8.0.7
- Eclipse 2020.6 R

## Project Features

### Login Scene

- A login form for users to log in.
- Validates username and password.
- Directs the user to the appropriate page based on the role (User or Admin).
- Shows warning alerts if login fails.

### Register Scene

- Registration form for new users.
- Validates the following:
  - Email format (must end with '@gmail.com').
  - Username uniqueness.
  - Password requirements (minimum 6 characters).
  - Age, gender, and nationality must be provided.
- Upon successful registration, inserts the user into the database with a "User" role.

### Main Navigation Bar

- Displays different menu options based on the user role.
  - User role: Shows options for Home, Cart, and History.
  - Admin role: Shows options for Manage Products and View History.
- Allows users to log out.

### Home Scene (User Role)

- Displays all available products from the database.
- Allows users to select products, view details, and add them to the cart.
- Products with 0 stock are hidden.

### Cart Scene (User Role)

- Displays all items in the user's cart.
- Users can view, remove, and proceed to checkout.
- Cart total is calculated and shown.

### Transaction Card Pop-up

- Allows users to select courier options and insurance for checkout.
- Updates total price based on selected options.

### History Scene (User Role)

- Displays a list of past transactions for the current user.
- Shows details of each transaction.

### Manage Product Scene (Admin Role)

- Allows admins to view, add, update, or delete products from the database.
- Provides fields for updating stock and adding new products.

### View History Scene (Admin Role)

- Displays all transactions made by all users.
- Allows admins to view the details of each transaction.

## User Roles

### Admin Role:
- Username: `admin@gmail.com`
- Password: `admin1234`

### User Role:
- Username: `boodi@gmail.com`
- Password: `user1234`

## How to Run the Project

1. Clone the repository from GitHub.
2. Open the project in Eclipse.
3. Execute the provided SQL script (`create+insert.sql`) to set up the database.
4. Configure the database connection in the Java project.
5. Run the project from Eclipse.
6. Use the provided credentials to log in as either an Admin or a User.

