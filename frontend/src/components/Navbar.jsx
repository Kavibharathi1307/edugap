import React from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import {
  ChevronRight,
  GraduationCap,
  LogOut,
  Menu,
  PlayCircle,
} from 'lucide-react';

const pageTitles = {
  '/dashboard': 'Dashboard',
  '/topics': 'Learning Topics',
  '/quizzes': 'Assessment Center',
  '/quizzes/play': 'Assessment in Progress',
  '/recommendations': 'Recommendations',
  '/resources': 'Learning Resources',
  '/analytics': 'Learning Analytics',
  '/history': 'Assessment History',
  '/profile': 'Student Profile',
};

export default function Navbar({ onMenuClick }) {
  const { user, logout, isAuthenticated } = useAuth();
  const location = useLocation();
  const navigate = useNavigate();

  const title =
    pageTitles[location.pathname] ||
    (location.pathname.startsWith('/topics/')
      ? 'Topic Details'
      : location.pathname.startsWith('/resources/')
      ? 'Resource Details'
      : location.pathname.startsWith('/quizzes/result/')
      ? 'Assessment Result'
      : 'EduGap');

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <header className="sticky top-0 z-30 border-b border-slate-200 bg-white">
      <div className="flex h-16 items-center justify-between gap-3 px-4 sm:px-6 lg:px-8">
        <div className="flex min-w-0 items-center gap-3">
          {isAuthenticated ? (
            <button
              type="button"
              onClick={onMenuClick}
              className="rounded-lg p-2 text-slate-600 hover:bg-slate-100 hover:text-navy lg:hidden"
              aria-label="Open navigation menu"
            >
              <Menu className="h-5 w-5" />
            </button>
          ) : (
            <Link to="/" className="flex items-center gap-2.5">
              <span className="flex h-9 w-9 items-center justify-center rounded-xl bg-primary-600 text-white">
                <GraduationCap className="h-5 w-5" aria-hidden="true" />
              </span>
              <span>
                <span className="block text-lg font-bold leading-tight text-navy">EduGap</span>
                <span className="block text-[10px] font-medium uppercase tracking-wider text-slate-400">
                  Learning System
                </span>
              </span>
            </Link>
          )}

          {isAuthenticated && (
            <div className="min-w-0">
              <div className="flex items-center gap-1.5 text-[11px] font-medium text-slate-400">
                <span>Student workspace</span>
                <ChevronRight className="h-3 w-3" aria-hidden="true" />
              </div>
              <h1 className="truncate text-sm font-semibold text-navy">{title}</h1>
            </div>
          )}
        </div>

        <div className="flex items-center gap-2 sm:gap-3">
          {isAuthenticated ? (
            <>
              <Link to="/quizzes" className="btn-primary hidden sm:inline-flex">
                <PlayCircle className="h-4 w-4" aria-hidden="true" />
                Take Quiz
              </Link>

              <Link
                to="/profile"
                className="flex items-center gap-2 rounded-xl p-1.5 pr-2 hover:bg-slate-100"
                aria-label="Open student profile"
              >
                <span className="flex h-8 w-8 items-center justify-center rounded-full bg-primary-100 text-xs font-bold text-primary-800">
                  {user?.fullName?.charAt(0) || 'S'}
                </span>
                <span className="hidden max-w-[130px] truncate text-sm font-medium text-slate-700 md:block">
                  {user?.fullName || 'Student'}
                </span>
              </Link>

              <button
                type="button"
                onClick={handleLogout}
                title="Sign out"
                className="hidden rounded-lg p-2 text-slate-500 hover:bg-red-50 hover:text-danger sm:inline-flex"
                aria-label="Sign out"
              >
                <LogOut className="h-4 w-4" />
              </button>
            </>
          ) : (
            <>
              <Link to="/login" className="btn-ghost !px-2 sm:!px-3">
                Sign In
              </Link>
              <Link to="/register" className="btn-primary">
                Get Started
              </Link>
            </>
          )}
        </div>
      </div>
    </header>
  );
}
