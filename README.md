# 🗂️ Team Task Manager

A full-stack web application for managing team projects
and tasks efficiently.

## 🌐 Live Demo
👉 https://taskmanager-production-4f97.up.railway.app/index.html

## 🛠️ Tech Stack

### Backend
- Java 21
- Spring Boot 3.1.5
- Spring Security + JWT Authentication
- Spring Data JPA + Hibernate
- MySQL Database
- Maven

### Frontend
- HTML5, CSS3
- Bootstrap 5
- Vanilla JavaScript (Fetch API)

### Deployment
- Railway (Backend + Database)
- GitHub (Version Control)

---

## ✅ Features

### 🔐 Authentication
- User Signup with name, email, password
- Secure Login with JWT token
- Password encryption using BCrypt

### 📁 Project Management
- Create projects
- Creator automatically becomes Admin
- Admin can add/remove members
- Members can view assigned projects

### ✅ Task Management
- Create tasks with title, description, due date, priority
- Assign tasks to team members
- Update task status (To Do → In Progress → Done)
- Priority levels: Low, Medium, High
- Admin can delete tasks

### 📊 Dashboard
- Total tasks count
- Tasks grouped by status
- Tasks per user
- Overdue tasks count

### 🔒 Role-Based Access
- **Admin** → Full control over project, members and tasks
- **Member** → Can view and update only assigned tasks

---

## 🗄️ Database Design
