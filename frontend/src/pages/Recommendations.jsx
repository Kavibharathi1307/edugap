import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import api from '../api/client';
import PageHeader from '../components/ui/PageHeader';
import { EmptyState, ErrorState, LoadingState } from '../components/ui/StateViews';
import {
  BookMarked,
  ExternalLink,
  Lightbulb,
  CheckCircle2,
  BookOpen,
  Sparkles,
} from 'lucide-react';

export default function Recommendations() {
  const [recommendations, setRecommendations] = useState([]);
  const [filter, setFilter] = useState('ALL');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [actionLoading, setActionLoading] = useState(null);
  const [hasAssessmentHistory, setHasAssessmentHistory] = useState(false);

  useEffect(() => {
    fetchRecommendations();
  }, []);

  const fetchRecommendations = async () => {
    setLoading(true);
    setError('');
    try {
      const [recRes, profileRes] = await Promise.all([
        api.get('/recommendations'),
        api.get('/auth/me'),
      ]);
      if (recRes.data?.data) {
        setRecommendations(recRes.data.data);
      }
      setHasAssessmentHistory((profileRes.data?.data?.totalAssessmentsTaken || 0) > 0);
    } catch (err) {
      console.error('Failed to load recommendations', err);
      setError('Unable to load personalized recommendations. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const markCompleted = async (id) => {
    setActionLoading(id);
    try {
      await api.patch(`/recommendations/${id}/complete`);
      setRecommendations((prev) => prev.filter((r) => r.id !== id));
    } catch (err) {
      console.error('Failed to mark completed', err);
    } finally {
      setActionLoading(null);
    }
  };

  const filtered = recommendations.filter((r) => {
    if (filter === 'ALL') return true;
    return r.priority === filter;
  });

  return (
    <div className="mx-auto max-w-5xl space-y-6 px-4 py-6 sm:px-6 sm:py-8 lg:px-8">
      <PageHeader
        eyebrow="Adaptive learning engine"
        title="Personalized learning hub"
        description="Actionable learning steps dynamically mapped to your diagnosed knowledge gaps."
        actions={
          <div className="flex items-center gap-1.5 rounded-xl bg-slate-100 p-1">
            {['ALL', 'HIGH', 'MEDIUM'].map((f) => (
              <button
                key={f}
                type="button"
                onClick={() => setFilter(f)}
                aria-pressed={filter === f}
                className={`cursor-pointer rounded-lg px-3 py-1.5 text-xs font-semibold transition-all ${
                  filter === f
                    ? 'bg-white text-navy shadow-sm'
                    : 'text-slate-600 hover:text-navy'
                }`}
              >
                {f === 'ALL' ? 'All gaps' : `${f} priority`}
              </button>
            ))}
          </div>
        }
      />

      {loading ? (
        <LoadingState message="Aggregating personalized recommendations..." />
      ) : error ? (
        <ErrorState title="Could not load recommendations" message={error} onRetry={fetchRecommendations} />
      ) : filtered.length > 0 ? (
        <div className="space-y-4">
          {filtered.map((rec) => (
            <article
              key={rec.id}
              className="card card-hover flex flex-col gap-5 p-6 md:flex-row md:items-center md:justify-between"
            >
              <div className="max-w-2xl space-y-2">
                <div className="flex flex-wrap items-center gap-2">
                  <span
                    className={
                      rec.priority === 'HIGH'
                        ? 'badge-danger'
                        : rec.priority === 'MEDIUM'
                        ? 'badge-warning'
                        : 'badge-teal'
                    }
                  >
                    {rec.priority} priority
                  </span>
                  <span className="text-xs font-bold text-slate-700">{rec.topicName}</span>
                  <span className="text-[11px] font-semibold text-slate-400">
                    &bull; Action: {rec.actionType.replace('_', ' ')}
                  </span>
                </div>

                <h3 className="text-base font-bold text-navy">{rec.recommendationText}</h3>

                {rec.reason && (
                  <p className="rounded-xl border border-slate-100 bg-slate-50 p-3 text-xs leading-relaxed text-slate-500">
                    <strong className="text-slate-600">Why this is recommended: </strong>
                    {rec.reason}
                  </p>
                )}

                {rec.resourceTitle && (rec.resourceId || rec.resourceUrl) && (
                  <div className="flex items-center gap-2 rounded-xl border border-primary-100 bg-primary-50/60 px-3 py-2">
                    <BookMarked className="h-3.5 w-3.5 shrink-0 text-primary-700" aria-hidden="true" />
                    <span className="shrink-0 text-[11px] font-semibold uppercase tracking-wider text-primary-700">
                      Suggested resource
                    </span>
                    {rec.resourceId ? (
                      <Link
                        to={`/resources/${rec.resourceId}`}
                        className="truncate text-xs font-semibold text-navy hover:text-primary-700 hover:underline"
                      >
                        {rec.resourceTitle}
                      </Link>
                    ) : (
                      <a
                        href={rec.resourceUrl}
                        target="_blank"
                        rel="noreferrer"
                        className="truncate text-xs font-semibold text-navy hover:text-primary-700 hover:underline"
                      >
                        {rec.resourceTitle}
                      </a>
                    )}
                    {rec.resourceUrl && !rec.resourceId && (
                      <ExternalLink className="h-3 w-3 shrink-0 text-slate-400" aria-hidden="true" />
                    )}
                  </div>
                )}
              </div>

              <div className="flex shrink-0 items-center gap-2.5 sm:flex-col sm:items-end">
                <Link
                  to={`/topics/${rec.topicId}`}
                  className="btn-primary w-full !text-xs sm:w-auto"
                >
                  <BookOpen className="h-3.5 w-3.5" aria-hidden="true" />
                  Study module
                </Link>

                <button
                  type="button"
                  onClick={() => markCompleted(rec.id)}
                  disabled={actionLoading === rec.id}
                  className="inline-flex w-full items-center justify-center gap-1.5 rounded-xl border border-transparent bg-slate-100 px-3.5 py-2 text-xs font-semibold text-slate-600 transition-colors hover:border-green-200 hover:bg-green-50 hover:text-green-700 disabled:opacity-60 sm:w-auto"
                >
                  <CheckCircle2 className="h-3.5 w-3.5" aria-hidden="true" />
                  {actionLoading === rec.id ? 'Updating...' : 'Mark done'}
                </button>
              </div>
            </article>
          ))}
        </div>
      ) : (
        <div className="card mx-auto max-w-md">
          <EmptyState
            icon={recommendations.length === 0 ? Sparkles : Lightbulb}
            title={
              recommendations.length > 0
                ? 'No recommendations in this filter'
                : hasAssessmentHistory
                ? 'All gaps remediated!'
                : 'Discover your knowledge gaps'
            }
            message={
              recommendations.length > 0
                ? 'Try selecting a different priority filter to see more recommendations.'
                : hasAssessmentHistory
                ? 'You currently have no outstanding remedial tasks. Take another assessment to test yourself on advanced topics.'
                : 'Take a quiz to discover your knowledge gaps. Personalized recommendations will appear here once you complete an assessment.'
            }
            action={
              <Link to="/quizzes" className="btn-primary mt-1 !text-xs">
                Take assessment &rarr;
              </Link>
            }
          />
        </div>
      )}
    </div>
  );
}
