import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import api from '../api/client';
import PageHeader from '../components/ui/PageHeader';
import { EmptyState, ErrorState, LoadingState } from '../components/ui/StateViews';
import {
  BookOpen,
  ExternalLink,
  Library,
  RotateCw,
} from 'lucide-react';

const describeError = (err) => {
  const status = err?.response?.status;
  if (status === 401 || status === 403) {
    return 'Your session has expired or you do not have access. Please sign in again.';
  }
  if (status >= 500) {
    return 'The server is currently unavailable. Please try again in a moment.';
  }
  if (!status) {
    return 'Unable to reach the server. Please check your connection.';
  }
  return 'Unable to load learning resources. Please try again.';
};

export default function Resources() {
  const [resources, setResources] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchResources();
  }, []);

  const fetchResources = async () => {
    setLoading(true);
    setError('');
    try {
      const res = await api.get('/resources');
      if (res.data?.data) {
        setResources(res.data.data);
      }
    } catch (err) {
      console.error('Failed to load learning resources', err);
      setError(describeError(err));
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="mx-auto max-w-7xl space-y-6 px-4 py-6 sm:px-6 sm:py-8 lg:px-8">
      <PageHeader
        eyebrow="Resource library"
        title="Learning resources"
        description="Reference articles, code examples, and cheat sheets curated for every topic across the curriculum."
        actions={
          <div className="flex items-center gap-2 rounded-xl border border-slate-200 bg-white px-3.5 py-2 text-xs font-semibold text-slate-600 shadow-sm">
            <span className="h-2 w-2 rounded-full bg-primary-600" />
            {resources.length} resource{resources.length === 1 ? '' : 's'} available
          </div>
        }
      />

      {error ? (
        <ErrorState
          title="Could not load resources"
          message={error}
          onRetry={fetchResources}
        />
      ) : loading ? (
        <LoadingState message="Loading learning resources..." />
      ) : resources.length === 0 ? (
        <div className="card">
          <EmptyState
            icon={Library}
            title="No learning resources available yet"
            message="Resources are published alongside curriculum topics. Please check back later."
            action={
              <Link to="/topics" className="btn-secondary mt-1">
                <BookOpen className="h-4 w-4" aria-hidden="true" />
                Browse topics
              </Link>
            }
          />
        </div>
      ) : (
        <div className="grid grid-cols-1 gap-5 md:grid-cols-2 xl:grid-cols-3">
          {resources.map((res) => (
            <article key={res.id} className="card card-hover flex flex-col justify-between p-6">
              <div>
                <div className="mb-3 flex flex-wrap items-center gap-2">
                  <span className="badge-neutral">{res.contentType}</span>
                  {res.difficultyLevel && (
                    <span className="badge-teal">{res.difficultyLevel}</span>
                  )}
                </div>

                {(res.subjectName || res.topicName) && (
                  <p className="text-[11px] font-semibold uppercase tracking-wider text-primary-700">
                    {[res.subjectName, res.topicName].filter(Boolean).join(' • ')}
                  </p>
                )}
                <h3 className="mt-1 text-base font-bold text-navy">{res.title}</h3>
                {res.summary && (
                  <p className="mt-2 line-clamp-3 text-sm leading-relaxed text-slate-600">
                    {res.summary}
                  </p>
                )}
              </div>

              <div className="mt-5 flex flex-wrap items-center justify-between gap-3 border-t border-slate-100 pt-4">
                <Link
                  to={`/resources/${res.id}`}
                  className="inline-flex items-center gap-1.5 text-xs font-semibold text-slate-700 transition-colors hover:text-primary-700"
                >
                  <BookOpen className="h-4 w-4 text-slate-400" aria-hidden="true" />
                  View details
                </Link>

                {res.resourceUrl ? (
                  <a
                    href={res.resourceUrl}
                    target="_blank"
                    rel="noreferrer"
                    className="inline-flex items-center gap-1.5 rounded-lg bg-slate-100 px-3 py-1.5 text-xs font-semibold text-slate-700 transition-colors hover:bg-primary-50 hover:text-primary-700"
                  >
                    <ExternalLink className="h-3.5 w-3.5" aria-hidden="true" />
                    Open resource
                  </a>
                ) : (
                  <span className="inline-flex items-center gap-1.5 rounded-lg bg-slate-50 px-3 py-1.5 text-xs font-medium text-slate-400">
                    <RotateCw className="h-3.5 w-3.5" aria-hidden="true" />
                    Link coming soon
                  </span>
                )}
              </div>
            </article>
          ))}
        </div>
      )}
    </div>
  );
}
