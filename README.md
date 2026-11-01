# Intelligent Knowledge Gap Detection and Personalized Learning System

An enterprise-grade, student-centered educational platform designed for college mini projects. The system objectively assesses student performance through topic-categorized assessments, detects fine-grained knowledge gaps using a deterministic Java-based analysis engine, and delivers personalized remedial learning materials, practice quizzes, and interactive Chart.js growth tracking.

---

## 🌟 Key Features

1. **Secure Authentication & Role Management:** Student registration and login secured with Spring Security, BCrypt password hashing, and stateless JWT tokens.
2. **Core Java Topic Hub:** 8 foundational Java topics with in-depth explanations, code examples, common mistakes, and practice questions:
   - Java Fundamentals
   - Variables and Data Types
   - Conditional Statements
   - Loops
   - Arrays
   - Strings
   - Methods
   - Object-Oriented Programming (OOP)
3. **Cheat-Resistant Quiz Engine:** Questions are fetched without revealing correct answers to the browser. Answer choices are verified and scored strictly on the server.
4. **Intelligent Knowledge Gap Detection Engine:** Deterministic pure Java algorithm classifying topic understanding into:
   - **High-Priority Knowledge Gap:** $< 40\%$ accuracy
   - **Needs Improvement:** $40\% - 69\%$ accuracy
   - **Good Understanding:** $70\% - 84\%$ accuracy
   - **Strong Understanding:** $\ge 85\%$ accuracy
   - **Not Assessed:** Unattempted topics handled safely without false gaps
5. **Personalized Learning Recommendation Engine:** Dynamically generates remedial study activities and targeted quizzes mapped directly to detected weaknesses, with transparent explanations.
6. **Progress Tracking & Delta Analysis:** Compares consecutive assessment attempts ($\Delta = \text{Current} - \text{Previous}$) to highlight learning gains or detect knowledge decay.
7. **Interactive Visual Dashboard:** Dynamic radar charts for topic mastery and line charts for historical performance built with Chart.js.
8. **Study Material Completion Tracking:** Students can study topic notes and mark them completed to track self-study progress.

---

## 🏗️ Technology Architecture

- **Frontend:** React 18, Vite, JavaScript, Tailwind CSS, React Router v6, Axios, Chart.js & react-chartjs-2, Lucide Icons.
- **Backend:** Java 21, Spring Boot 3.3.x, Spring Web, Spring Data JPA, Spring Security 6, JJWT (JSON Web Token), Jakarta Bean Validation, Maven.
- **Database:** PostgreSQL 16 (with connection pooling, constraints, and relational schemas).
- **Testing:** JUnit 5, Mockito, Spring Boot Test, Vite Build Verification.

---

## 📁 Repository Structure

```text
knowledge-gap-learning-system/
├── PROJECT_PLAN.md                  <-- Master implementation checklist & status
├── README.md                        <-- High-level project documentation
├── docs/                            <-- In-depth engineering specifications
│   ├── ARCHITECTURE.md              <-- System design & data flow
│   ├── DATABASE_SCHEMA.md           <-- PostgreSQL ER diagram & table definitions
│   ├── API_DOCUMENTATION.md         <-- REST API endpoint contracts
│   ├── SETUP.md                     <-- Local development guide
│   ├── TESTING.md                   <-- Test execution guide & results
│   └── DEPLOYMENT.md                <-- Production deployment instructions
├── backend/                         <-- Spring Boot 3 Java backend
└── frontend/                        <-- React 18 + Vite frontend
```

---

## 🚀 Quick Start Guide

### Prerequisites
- **Java 21** or later (`java -version`)
- **Maven 3.9+** (`mvn -version`)
- **Node.js 18+** & npm (`node -v`, `npm -v`)
- **PostgreSQL 16** (`psql -U postgres`)

### 1. Database Setup
Ensure PostgreSQL is running, then create the database:
```sql
CREATE DATABASE knowledge_gap_db;
```

### 2. Backend Setup
```bash
cd backend
mvn clean install
mvn spring-boot:run
```
The backend API starts on `http://localhost:8080`.

### 3. Frontend Setup
```bash
cd frontend
npm install
npm run dev
```
The client application runs at `http://localhost:5173`.
