# Production Deployment Guide

This guide covers deploying the frontend, backend, and PostgreSQL database using free or low-cost hosting platforms.

---

## 1. Hosting Architecture Overview

```mermaid
flowchart LR
    Browser["Student Browser"]
    Vercel["Frontend on Vercel\n(React 18 + Vite)"]
    Render["Backend on Render.com\n(Java 21 Spring Boot Docker)"]
    Neon["Cloud Database on Neon.tech / Supabase\n(PostgreSQL 16)"]

    Browser -->|"Loads Web UI"| Vercel
    Browser -->|"REST API (HTTPS + JWT)"| Render
    Render -->|"SQL over SSL"| Neon
```

| Component | Platform | Free / Low-Cost Tier Details |
| :--- | :--- | :--- |
| **Frontend** | **Vercel** or **Netlify** | Unlimited deployments, automatic SSL, global CDN |
| **Backend API** | **Render.com** | Free Web Service tier (Docker or Native Java) |
| **Database** | **Neon.tech** or **Supabase** | Free Serverless PostgreSQL instance with connection pooling |

---

## 2. Step 1: Hosted PostgreSQL Database Setup (e.g. Neon.tech or Supabase)

1. Sign up at [neon.tech](https://neon.tech) or [supabase.com](https://supabase.com).
2. Create a new project named `knowledge-gap-system`.
3. Copy your PostgreSQL Connection URI. It looks like:
   `postgresql://username:password@ep-xyz.us-east-1.aws.neon.tech/neondb?sslmode=require`
4. Split this into:
   - `SPRING_DATASOURCE_URL`: `jdbc:postgresql://ep-xyz.us-east-1.aws.neon.tech/neondb?sslmode=require`
   - `SPRING_DATASOURCE_USERNAME`: `username`
   - `SPRING_DATASOURCE_PASSWORD`: `password`

---

## 3. Step 2: Backend API Deployment (Render.com)

1. Push your repository to GitHub.
2. Sign in to [Render.com](https://render.com) and click **New > Web Service**.
3. Select your GitHub repository.
4. Set the Root Directory to: `backend`.
5. Select **Docker** as the Runtime (uses `backend/Dockerfile`), or configure Maven:
   - **Build Command:** `mvn clean package -DskipTests`
   - **Start Command:** `java -jar target/knowledge-gap-learning-system-1.0.0.jar`
6. Add the following **Environment Variables**:
   - `SPRING_PROFILES_ACTIVE`: `prod`
   - `SPRING_DATASOURCE_URL`: *(Your Neon/Supabase JDBC URL)*
   - `SPRING_DATASOURCE_USERNAME`: *(Your DB Username)*
   - `SPRING_DATASOURCE_PASSWORD`: *(Your DB Password)*
   - `JWT_SECRET`: *(A 256-bit secret string, e.g. `404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970`)*
   - `CORS_ALLOWED_ORIGINS`: `https://your-frontend-domain.vercel.app`
7. Click **Deploy Web Service**.
8. Note your public backend URL: e.g. `https://edugap-backend.onrender.com`.

---

## 4. Step 3: Frontend Deployment (Vercel)

1. Sign in to [Vercel.com](https://vercel.com) and click **Add New > Project**.
2. Select your repository.
3. Configure project settings:
   - **Root Directory:** `frontend`
   - **Framework Preset:** `Vite`
   - **Build Command:** `npm run build`
   - **Output Directory:** `dist`
4. In **Environment Variables**, add:
   - `VITE_API_URL`: `https://edugap-backend.onrender.com/api`
5. Click **Deploy**.
6. Vercel automatically deploys your responsive frontend with SPA routing rewrites supported by `frontend/vercel.json`.

---

## 5. Pushing to GitHub (Step-by-Step)

From the project root directory:

```bash
git init
git add .
git commit -m "Initial commit: Intelligent Knowledge Gap Detection System"
git branch -M main
git remote add origin https://github.com/<your-username>/knowledge-gap-learning-system.git
git push -u origin main
```
