import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import api from '../api/client';
import { LoadingState } from '../components/ui/StateViews';
import {
  Mail,
  ShieldCheck,
  Calendar,
  Award,
  TrendingUp,
  LogOut,
} from 'lucide-react';

export default function Profile() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    loadProfile();
  }, []);

  const loadProfile = async () => {
    setLoading(true);
    setError('');
    try {
      const res = await api.get('/auth/me');
      if (res.data?.data) {
        setProfile(res.data.data);
      }
    } catch (err) {
      console.error('Failed to load profile', err);
      setError('Unable to load your profile. Please refresh the page.');
    } finally {
      setLoading(false);
    }
  };

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  if (loading) {
    return (
      <div className="mx-auto max-w-2xl px-4 sm:px-6">
        <LoadingState message="Loading student profile..." />
      </div>
    );
  }

  const displayName = profile?.fullName || user?.fullName || 'Student';
  const displayEmail = profile?.email || user?.email || '';

  return (
    <div className="mx-auto max-w-2xl space-y-6 px-4 py-6 sm:px-6 sm:py-10">
      {error && (
        <div role="alert" className="rounded-xl border border-red-200 bg-red-50 p-3.5 text-sm text-red-700">
          {error}
        </div>
      )}

      <section className="card overflow-hidden">
        <div className="h-24 bg-gradient-to-r from-[#0F172A] via-[#134E4A] to-primary-700" />

        <div className="p-6 sm:p-8">
          <div className="flex flex-col gap-4 sm:flex-row sm:items-end sm:justify-between">
            <div className="-mt-16 flex items-end gap-4">
              <div className="flex h-20 w-20 shrink-0 items-center justify-center rounded-2xl border-4 border-white bg-primary-600 text-2xl font-bold text-white shadow-card">
                {displayName.charAt(0)}
              </div>
              <div className="pb-1">
                <h1 className="text-xl font-bold text-navy">{displayName}</h1>
                <p className="flex items-center gap-1.5 text-sm text-slate-500">
                  <Mail className="h-3.5 w-3.5" aria-hidden="true" />
                  {displayEmail}
                </p>
                <span className="badge-teal mt-1.5">
                  <ShieldCheck className="h-3.5 w-3.5" aria-hidden="true" />
                  {profile?.role === 'ROLE_STUDENT' ? 'Student account' : profile?.role || 'Student'}
                </span>
              </div>
            </div>

            <button
              type="button"
              onClick={handleLogout}
              className="inline-flex items-center gap-1.5 self-start rounded-xl bg-red-50 px-4 py-2 text-xs font-semibold text-red-600 transition-colors hover:bg-red-100 sm:self-auto"
            >
              <LogOut className="h-4 w-4" aria-hidden="true" />
              Sign out
            </button>
          </div>

          {/* Stats */}
          <div className="mt-8 grid grid-cols-1 gap-4 border-t border-slate-100 pt-6 sm:grid-cols-2">
            <div className="rounded-xl border border-slate-100 bg-slate-50 p-4">
              <div className="mb-1 flex items-center gap-2 text-xs font-semibold uppercase tracking-wider text-slate-500">
                <Award className="h-4 w-4 text-primary-600" aria-hidden="true" />
                Completed tests
              </div>
              <div className="text-2xl font-bold text-navy">
                {profile?.totalAssessmentsTaken || 0}
              </div>
            </div>

            <div className="rounded-xl border border-slate-100 bg-slate-50 p-4">
              <div className="mb-1 flex items-center gap-2 text-xs font-semibold uppercase tracking-wider text-slate-500">
                <TrendingUp className="h-4 w-4 text-primary-600" aria-hidden="true" />
                Average score
              </div>
              <div className="text-2xl font-bold text-navy">{profile?.averageScore || 0}%</div>
            </div>
          </div>

          {/* Account Details */}
          <div className="mt-6 space-y-3 border-t border-slate-100 pt-5 text-xs text-slate-600">
            <div className="flex items-center justify-between gap-4">
              <span className="font-medium text-slate-400">Account ID</span>
              <span className="font-mono font-semibold text-slate-700">#{profile?.id}</span>
            </div>
            <div className="flex items-center justify-between gap-4">
              <span className="flex items-center gap-1.5 font-medium text-slate-400">
                <Calendar className="h-3.5 w-3.5" aria-hidden="true" />
                Registered date
              </span>
              <span className="font-semibold text-slate-700">
                {profile?.createdAt ? new Date(profile.createdAt).toLocaleDateString() : 'N/A'}
              </span>
            </div>
            <div className="flex items-center justify-between gap-4">
              <span className="font-medium text-slate-400">Platform role</span>
              <span className="font-semibold text-slate-700">Student (Learner)</span>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
}
