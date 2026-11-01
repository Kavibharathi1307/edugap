import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import api from '../api/client';
import TopicRadarChart from '../components/charts/TopicRadarChart';
import StatCard from '../components/ui/StatCard';
import { LoadingState } from '../components/ui/StateViews';
import {
  CheckCircle2,
  AlertTriangle,
  TrendingUp,
  Award,
  BookOpen,
  ArrowRight,
  Sparkles,
  BarChart3,
  Calendar,
  Lightbulb,
  Target,
  RefreshCw,
} from 'lucide-react';

export default function Dashboard() {
  const { user } = useAuth();
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchDashboard();
  }, []);

  const fetchDashboard = async () => {
    setLoading(true);
    setError('');
    try {
      const res = await api.get('/analytics/dashboard');
      if (res.data?.data) {
        setStats(res.data.data);
      }
    } catch (err) {
      console.error('Failed to load dashboard data', err);
      setError('Unable to load dashboard analytics. Please refresh or check connection.');
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
        <LoadingState message="Loading student performance analytics..." />
      </div>
    );
  }

  return (
    <div className="mx-auto max-w-7xl space-y-6 px-4 py-6 sm:px-6 sm:py-8 lg:px-8">
      {/* Welcome Banner */}
      <section className="relative overflow-hidden rounded-2xl bg-gradient-to-r from-[#0F172A] via-[#134E4A] to-primary-700 p-6 text-white shadow-card sm:p-8">
        <div
          className="pointer-events-none absolute -right-16 -top-24 h-64 w-64 rounded-full bg-primary-400/20 blur-3xl"
          aria-hidden="true"
        />
        <div
          className="pointer-events-none absolute -bottom-24 right-24 h-48 w-48 rounded-full bg-primary-300/10 blur-3xl"
          aria-hidden="true"
        />
        <div className="relative z-10 flex flex-col gap-6 lg:flex-row lg:items-end lg:justify-between">
          <div className="max-w-2xl">
            <div className="mb-3 inline-flex items-center gap-1.5 rounded-full bg-white/10 px-3 py-1 text-xs font-semibold text-primary-100 ring-1 ring-inset ring-white/15">
              <Sparkles className="h-3.5 w-3.5 text-amber-300" aria-hidden="true" />
              AI Knowledge Gap Diagnostic Engine
            </div>
            <h1 className="text-2xl font-bold tracking-tight sm:text-3xl">
              Welcome back, {user?.fullName || 'Student'}
            </h1>
            <p className="mt-2 text-sm leading-relaxed text-primary-100 sm:text-base">
            {stats?.totalAssessmentsTaken > 0
              ? `You have completed ${stats.totalAssessmentsTaken} assessment(s) with an average mastery of ${stats.averageScore}%. Review your priority gaps below.`
              : "You haven't attempted any assessments yet. Browse subjects and take a diagnostic quiz to identify your knowledge gaps."}
            </p>

            <div className="mt-6 flex flex-wrap gap-3">
              <Link
                to="/subjects"
                className="inline-flex items-center gap-2 rounded-xl bg-white px-5 py-2.5 text-sm font-semibold text-primary-700 transition-colors hover:bg-primary-50"
              >
                Browse subjects
                <ArrowRight className="h-4 w-4" aria-hidden="true" />
              </Link>
              <Link
                to="/recommendations"
                className="inline-flex items-center gap-2 rounded-xl bg-white/10 px-5 py-2.5 text-sm font-medium text-white ring-1 ring-inset ring-white/20 transition-colors hover:bg-white/15"
              >
                <Lightbulb className="h-4 w-4 text-amber-300" aria-hidden="true" />
                View Recommendations ({stats?.activeRecommendationsCount || 0})
              </Link>
            </div>
          </div>

          <div className="w-full max-w-xs rounded-2xl bg-white/10 p-4 ring-1 ring-inset ring-white/15 backdrop-blur-sm">
            <div className="flex items-center justify-between text-xs font-medium text-primary-100">
              <span>Overall mastery</span>
              <span className="font-bold text-white">{stats?.averageScore || 0}%</span>
            </div>
            <div className="mt-2 h-2 w-full overflow-hidden rounded-full bg-white/15">
              <div
                className="h-2 rounded-full bg-primary-300 transition-all duration-300"
                style={{ width: `${Math.min(100, stats?.averageScore || 0)}%` }}
              />
            </div>
            <div className="mt-3 flex items-center justify-between text-[11px] text-primary-200">
              <span>{stats?.completedTopicsCount || 0} topics completed</span>
              <span>75% target</span>
            </div>
          </div>
        </div>
      </section>

      {error && (
        <div
          role="alert"
          className="flex flex-col gap-3 rounded-xl border border-red-200 bg-red-50 p-4 text-sm text-red-700 sm:flex-row sm:items-center sm:justify-between"
        >
          <span>{error}</span>
          <button type="button" onClick={fetchDashboard} className="btn-secondary self-start !py-1.5 !text-xs">
            <RefreshCw className="h-3.5 w-3.5" aria-hidden="true" />
            Retry
          </button>
        </div>
      )}

      {/* Top Level Metric Cards */}
      <section className="grid grid-cols-2 gap-4 sm:gap-5 lg:grid-cols-4">
        <StatCard
          label="Assessments"
          value={stats?.totalAssessmentsTaken || 0}
          hint="Total quizzes completed"
          icon={Award}
          tone="teal"
        />
        <StatCard
          label="Average Score"
          value={`${stats?.averageScore || 0}%`}
          hint="Across all submissions"
          icon={TrendingUp}
          tone="green"
        />
        <StatCard
          label="Topics Mastered"
          value={
            <span>
              {stats?.completedTopicsCount || 0}
              <span className="text-sm font-normal text-slate-400"> / {stats?.totalTopicsCount || 0}</span>
            </span>
          }
          hint="Curriculum topics reviewed"
          icon={BookOpen}
          tone="teal"
        />
        <StatCard
          label="Knowledge Gaps"
          value={stats?.weakestTopics?.length || 0}
          hint="Topics needing remediation"
          icon={AlertTriangle}
          tone="red"
        />
      </section>

      {/* Main Analysis Section */}
      <section className="grid grid-cols-1 gap-6 lg:grid-cols-12">
        {/* Radar Chart */}
        <div className="card flex flex-col justify-between p-6 lg:col-span-7">
          <div className="mb-4 flex flex-wrap items-start justify-between gap-3">
            <div>
              <h2 className="flex items-center gap-2 text-base font-bold text-navy">
                <BarChart3 className="h-5 w-5 text-primary-600" aria-hidden="true" />
                Topic Mastery Radar
              </h2>
              <p className="mt-0.5 text-xs text-slate-500">
                Current mastery across topics versus the 75% proficiency benchmark
              </p>
            </div>
            <Link
              to="/analytics"
              className="text-xs font-semibold text-primary-700 hover:text-primary-800 hover:underline"
            >
              View Trend Curve
            </Link>
          </div>

          <div className="flex flex-1 items-center justify-center">
            <TopicRadarChart topicPerformances={stats?.allTopicsPerformance} />
          </div>
        </div>

        {/* Knowledge Gap Diagnosis Column */}
        <div className="space-y-6 lg:col-span-5">
          {/* Topics Requiring Remediation */}
          <div className="card p-6">
            <div className="mb-4 flex items-center justify-between">
              <h3 className="flex items-center gap-2 text-sm font-bold text-navy">
                <AlertTriangle className="h-4 w-4 text-danger" aria-hidden="true" />
                Identified Gaps
              </h3>
              <span className="badge-danger">{stats?.weakestTopics?.length || 0}</span>
            </div>

            {stats?.weakestTopics && stats.weakestTopics.length > 0 ? (
              <div className="space-y-3">
                {stats.weakestTopics.map((item) => (
                  <div
                    key={item.topicId}
                    className="rounded-xl border border-slate-200 bg-slate-50/70 p-3 transition-colors hover:border-slate-300 hover:bg-white"
                  >
                    <div className="flex items-start justify-between gap-3">
                      <div className="min-w-0">
                        <h4 className="truncate text-sm font-semibold text-navy">{item.topicName}</h4>
                        <div className="mt-1.5 flex items-center gap-2">
                          <span
                            className="rounded-full px-2 py-0.5 text-[11px] font-semibold"
                            style={{
                              backgroundColor: `${item.badgeColor || '#DC2626'}15`,
                              color: item.badgeColor || '#DC2626',
                            }}
                          >
                            {item.gapLabel}
                          </span>
                          <span className="font-mono text-xs font-medium text-slate-500">
                            {item.accuracyPercentage}%
                          </span>
                        </div>
                      </div>
                      <Link
                        to={`/topics/${item.topicId}`}
                        className="shrink-0 rounded-lg border border-slate-200 bg-white px-3 py-1.5 text-xs font-semibold text-slate-700 shadow-sm transition-colors hover:border-primary-500 hover:text-primary-700"
                      >
                        Study
                      </Link>
                    </div>
                    <div className="mt-3 h-1.5 w-full overflow-hidden rounded-full bg-slate-200">
                      <div
                        className="h-1.5 rounded-full"
                        style={{
                          width: `${Math.min(100, item.accuracyPercentage || 0)}%`,
                          backgroundColor: item.badgeColor || '#DC2626',
                        }}
                      />
                    </div>
                  </div>
                ))}
              </div>
            ) : (
              <div className="py-4 text-center">
                <CheckCircle2 className="mx-auto mb-2 h-6 w-6 text-success" aria-hidden="true" />
                <p className="text-xs leading-relaxed text-slate-500">
                  {stats?.totalAssessmentsTaken > 0
                    ? 'Outstanding! You currently have no high-priority knowledge gaps.'
                    : 'Complete a quiz to detect topic gaps.'}
                </p>
              </div>
            )}
          </div>

          {/* Strongest Topics */}
          <div className="card p-6">
            <div className="mb-4 flex items-center justify-between">
              <h3 className="flex items-center gap-2 text-sm font-bold text-navy">
                <Target className="h-4 w-4 text-success" aria-hidden="true" />
                Strongest Areas
              </h3>
              <span className="badge-success">{stats?.strongestTopics?.length || 0}</span>
            </div>

            {stats?.strongestTopics && stats.strongestTopics.length > 0 ? (
              <div className="space-y-3">
                {stats.strongestTopics.map((item) => (
                  <div key={item.topicId}>
                    <div className="mb-1 flex items-center justify-between gap-3">
                      <span className="truncate text-sm font-medium text-slate-700">{item.topicName}</span>
                      <span className="text-xs font-bold text-success">{item.accuracyPercentage}%</span>
                    </div>
                    <div className="h-1.5 w-full overflow-hidden rounded-full bg-slate-100">
                      <div
                        className="h-1.5 rounded-full bg-success"
                        style={{ width: `${Math.min(100, item.accuracyPercentage || 0)}%` }}
                      />
                    </div>
                  </div>
                ))}
              </div>
            ) : (
              <p className="py-2 text-center text-xs italic text-slate-500">
                No completed assessments recorded yet.
              </p>
            )}
          </div>
        </div>
      </section>

      {/* Recent Attempts History */}
      <section className="table-shell">
        <div className="flex flex-wrap items-center justify-between gap-3 border-b border-slate-200 px-4 py-4 sm:px-6">
          <h3 className="flex items-center gap-2 text-base font-bold text-navy">
            <Calendar className="h-5 w-5 text-primary-600" aria-hidden="true" />
            Recent Assessment History
          </h3>
          <Link
            to="/history"
            className="text-xs font-semibold text-primary-700 hover:text-primary-800 hover:underline"
          >
            View all ({stats?.totalAssessmentsTaken || 0})
          </Link>
        </div>

        {stats?.recentAttempts && stats.recentAttempts.length > 0 ? (
          <div className="overflow-x-auto">
            <table className="data-table min-w-[640px]">
              <thead>
                <tr>
                  <th>Assessment</th>
                  <th>Subject</th>
                  <th>Questions</th>
                  <th>Score</th>
                  <th>Date</th>
                  <th className="text-right">Actions</th>
                </tr>
              </thead>
              <tbody>
                {stats.recentAttempts.map((attempt) => (
                  <tr key={attempt.attemptId}>
                    <td className="font-mono font-medium text-slate-600">#{attempt.attemptId}</td>
                    <td className="font-semibold text-navy">{attempt.subjectName}</td>
                    <td className="text-slate-600">
                      {attempt.correctAnswers} / {attempt.totalQuestions}
                    </td>
                    <td>
                      <span
                        className={
                          attempt.scorePercentage >= 75
                            ? 'badge-success'
                            : attempt.scorePercentage >= 50
                            ? 'badge-warning'
                            : 'badge-danger'
                        }
                      >
                        {attempt.scorePercentage}%
                      </span>
                    </td>
                    <td className="whitespace-nowrap text-xs text-slate-500">
                      {new Date(attempt.completedAt).toLocaleDateString(undefined, {
                        month: 'short',
                        day: 'numeric',
                        hour: '2-digit',
                        minute: '2-digit',
                      })}
                    </td>
                    <td className="text-right">
                      <Link
                        to={`/quizzes/result/${attempt.attemptId}`}
                        className="text-xs font-semibold text-primary-700 hover:text-primary-800"
                      >
                        Review &rarr;
                      </Link>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        ) : (
          <div className="px-6 py-10 text-center">
            <BookOpen className="mx-auto mb-2 h-7 w-7 text-slate-300" aria-hidden="true" />
            <p className="text-sm text-slate-500">You haven't completed any quizzes yet.</p>
            <Link
              to="/quizzes"
              className="mt-3 inline-flex items-center gap-1.5 text-xs font-semibold text-primary-700 hover:underline"
            >
              Start your first diagnostic quiz &rarr;
            </Link>
          </div>
        )}
      </section>
    </div>
  );
}
