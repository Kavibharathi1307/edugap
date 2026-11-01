import React, { useState } from 'react';
import { Link, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import AuthLayout from '../components/AuthLayout';
import { AlertCircle, ArrowRight, Lock, Mail, Sparkles } from 'lucide-react';

export default function Login() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const from = location.state?.from?.pathname || '/dashboard';

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      await login(email.trim(), password);
      navigate(from, { replace: true });
    } catch (err) {
      if (!err.response) {
        setError('Unable to reach the server. Please check that the backend is running and try again.');
      } else {
        setError(
          err.response.data?.message ||
            (err.response.status === 401
              ? 'Invalid email or password. Please try again.'
              : 'Login failed. Please try again.')
        );
      }
    } finally {
      setLoading(false);
    }
  };

  const fillDemoAccount = () => {
    setEmail('student@edugap.ac.in');
    setPassword('Password@123');
  };

  return (
    <AuthLayout
      title="Student sign in"
      subtitle="Access your assessment diagnostics, knowledge gap reports, and personalized recommendations."
      footer={
        <p>
          Don't have an account?{' '}
          <Link to="/register" className="font-semibold text-primary-700 hover:underline">
            Register as a new student
          </Link>
        </p>
      }
    >
      <div className="mb-6 flex flex-col gap-3 rounded-xl border border-primary-200 bg-primary-50 p-3.5 sm:flex-row sm:items-center sm:justify-between">
        <div className="text-xs text-primary-900">
          <p className="flex items-center gap-1.5 font-semibold">
            <Sparkles className="h-3.5 w-3.5 text-primary-600" aria-hidden="true" />
            Quick demo login
          </p>
          <p className="mt-0.5 font-mono text-[11px] text-primary-700">
            student@edugap.ac.in / Password@123
          </p>
        </div>
        <button
          type="button"
          onClick={fillDemoAccount}
          className="rounded-lg bg-primary-600 px-3 py-1.5 text-xs font-semibold text-white transition-colors hover:bg-primary-700"
        >
          Auto fill
        </button>
      </div>

      {error && (
        <div
          role="alert"
          className="mb-5 flex items-start gap-2.5 rounded-xl border border-red-200 bg-red-50 p-3.5 text-sm text-red-700"
        >
          <AlertCircle className="mt-0.5 h-5 w-5 shrink-0" aria-hidden="true" />
          <span>{error}</span>
        </div>
      )}

      <form className="space-y-5" onSubmit={handleSubmit}>
        <div>
          <label htmlFor="login-email" className="label-field">
            Email address
          </label>
          <div className="relative">
            <Mail
              className="pointer-events-none absolute left-3.5 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-400"
              aria-hidden="true"
            />
            <input
              id="login-email"
              type="email"
              autoComplete="email"
              required
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder="student@edugap.ac.in"
              className="input-field pl-10"
            />
          </div>
        </div>

        <div>
          <label htmlFor="login-password" className="label-field">
            Password
          </label>
          <div className="relative">
            <Lock
              className="pointer-events-none absolute left-3.5 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-400"
              aria-hidden="true"
            />
            <input
              id="login-password"
              type="password"
              autoComplete="current-password"
              required
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              placeholder="Enter your password"
              className="input-field pl-10"
            />
          </div>
        </div>

        <button type="submit" disabled={loading} className="btn-primary w-full !py-3">
          {loading ? (
            <>
              <span className="h-4 w-4 animate-spin rounded-full border-2 border-white border-t-transparent" />
              Signing in...
            </>
          ) : (
            <>
              Sign in to dashboard
              <ArrowRight className="h-4 w-4" aria-hidden="true" />
            </>
          )}
        </button>
      </form>
    </AuthLayout>
  );
}
