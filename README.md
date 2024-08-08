# Notes App

## Introduction

This project is a web application that allows users to create, edit, delete, archive/unarchive notes, and tag and filter them by categories. The application is divided into a frontend built with Vue.js and a backend built with Spring Boot.

## Table of Contents

- [Introduction](#introduction)
- [Technologies](#technologies)
- [Requirements](#requirements)
- [Installation](#installation)
- [Author](#author)

## Technologies

### Frontend
- Vue.js 3.4.29
- Tailwind CSS 3.4.4

### Backend
- Spring Boot 3.3.1
- MySQL 8

## Requirements

- Node.js (version 18.17 or higher)
- npm (version 8.15 or higher)
- Maven (version 3.8.6 or higher)
- MySQL (version 8.0 or higher)

## Installation

### 1. Clone the Repository
   ```bash
   git clone https://github.com/Seba-Aguero/full-stack-notes-app.git
   cd full-stack-notes-app
  ```

### 2. Set Up the Database:
  - Open MySQL command line or a MySQL client.
  - Run the following command to create the database:
      ```sql
      CREATE DATABASE IF NOT EXISTS `full-stack-notes-app`;
      ```
  - Update the database configuration in the `application.properties` file located in `backend/src/main/resources/`.

### 3. Install Backend Dependencies:
  ```bash
  cd backend
  mvn clean install
  ```

### 4. Start the Backend Server:
  ```bash
  mvn spring-boot:run
  ```

### 5. Open a New Terminal and Install Frontend Dependencies:
  ```bash
  cd frontend
  npm install
  ```

### 6. Start the Frontend Server:
  ```bash
  npm run dev
  ```

### 7. Access the Application:
  - Open your browser and go to http://localhost:5173 to start using the application.

## Author

Sebastián Agüero