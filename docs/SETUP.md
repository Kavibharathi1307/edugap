# Local Development Setup Guide

Follow this step-by-step guide to run the application on your local machine.

---

## 1. System Requirements
- **Java Development Kit (JDK):** Version 21 or later (`java -version`)
- **Apache Maven:** Version 3.9+ (`mvn -version`)
- **Node.js:** Version 18+ and npm (`node -v`, `npm -v`)
- **PostgreSQL:** Version 14+ running locally on port `5432`

---

## 2. Database Setup

1. Start your local PostgreSQL service:
   - On Windows: Run `services.msc` and ensure `postgresql-x64-XX` is running.
   - On Mac/Linux: `sudo service postgresql start` or `brew services start postgresql`.

2. Open your terminal or `psql` console and create the database:
```sql
CREATE DATABASE knowledge_gap_db;
```

---

## 3. Backend Setup & Startup

1. Open a terminal in the `backend/` directory:
```bash
cd backend
```

2. Compile and run the Spring Boot application:
```bash
# Using Maven Wrapper
mvnw.cmd spring-boot:run     # (Windows PowerShell / CMD)
./mvnw spring-boot:run        # (Linux / macOS)
```

3. Verification:
   The backend starts on `http://localhost:8080`.
   Open `http://localhost:8080/api/health` in your browser. You should see:
   ```json
   { "success": true, "data": { "status": "UP" } }
   ```

*Note: On first startup, the database is automatically seeded with 8 Core Java modules, 32 questions with options, and a demo student account (`student@edugap.ac.in` / `Password@123`).*

---

## 4. Frontend Setup & Startup

1. Open a new terminal in the `frontend/` directory:
```bash
cd frontend
```

2. Install dependencies:
```bash
npm install
```

3. Start the Vite development server:
```bash
npm run dev
```

4. Access the web app:
   Open `http://localhost:5173` in your browser.

---

## 5. Demo Credentials
To test immediately without registering:
- **Email:** `student@edugap.ac.in`
- **Password:** `Password@123`
*(Or click the "Auto Fill" button on the Sign In page!)*
