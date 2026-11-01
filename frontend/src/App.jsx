import React from 'react';
import { BrowserRouter, Routes, Route, Navigate, Outlet } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import Navbar from './components/Navbar';
import Footer from './components/Footer';
import ProtectedRoute from './components/ProtectedRoute';
import AppLayout from './components/AppLayout';

// Pages
import Landing from './pages/Landing';
import Login from './pages/Login';
import Register from './pages/Register';
import Dashboard from './pages/Dashboard';
import SubjectSelection from './pages/SubjectSelection';
import SubjectDetail from './pages/SubjectDetail';
import TopicList from './pages/TopicList';
import TopicDetail from './pages/TopicDetail';
import QuizCatalog from './pages/QuizCatalog';
import QuizPlay from './pages/QuizPlay';
import QuizResult from './pages/QuizResult';
import Recommendations from './pages/Recommendations';
import Resources from './pages/Resources';
import ResourceDetail from './pages/ResourceDetail';
import ProgressAnalytics from './pages/ProgressAnalytics';
import History from './pages/History';
import Profile from './pages/Profile';

function PublicLayout() {
  return (
    <div className="flex min-h-screen flex-col bg-[#F8FAFC]">
      <Navbar />
      <main className="flex-1">
        <Outlet />
      </main>
      <Footer />
    </div>
  );
}

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          {/* Public Routes */}
          <Route element={<PublicLayout />}>
            <Route path="/" element={<Landing />} />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
          </Route>

          {/* Student Protected Routes */}
          <Route
            element={
              <ProtectedRoute>
                <AppLayout />
              </ProtectedRoute>
            }
          >
            <Route path="/dashboard" element={<Dashboard />} />
            <Route path="/subjects" element={<SubjectSelection />} />
            <Route path="/subjects/:subjectId" element={<SubjectDetail />} />
            <Route path="/topics" element={<TopicList />} />
            <Route path="/topics/:topicId" element={<TopicDetail />} />
            <Route path="/quizzes" element={<QuizCatalog />} />
            <Route path="/quizzes/play" element={<QuizPlay />} />
            <Route path="/quizzes/result/:attemptId" element={<QuizResult />} />
            <Route path="/recommendations" element={<Recommendations />} />
            <Route path="/resources" element={<Resources />} />
            <Route path="/resources/:resourceId" element={<ResourceDetail />} />
            <Route path="/analytics" element={<ProgressAnalytics />} />
            <Route path="/history" element={<History />} />
            <Route path="/profile" element={<Profile />} />
          </Route>

          {/* Catch-all redirect */}
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}
