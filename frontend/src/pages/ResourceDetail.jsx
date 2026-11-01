import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import api from '../api/client';
import PageHeader from '../components/ui/PageHeader';
import { ErrorState, LoadingState } from '../components/ui/StateViews';
import {
  AlertTriangle,
  ArrowLeft,
  BookOpen,
  ExternalLink,
  Library,
} from 'lucide-react';

export default function ResourceDetail() {
  const { resourceId } = useParams();
  const [resource, setResource] = useState(null);
  const [loading, setLoading] = useState(true);
  const [notFound, setNotFound] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchResource();
  }, [resourceId]);

  const fetchResource = async () => {
    setLoading(true);
    setError('');
    setNotFound(false);
    try {
      const res = await api.get(`/resources/${resourceId}`);
      if (res.data?.data) {
        setResource(res.data.data);
      } else {
        setNotFound(true);
      }
    } catch (err) {
      const status = err?.response?.status;
      console.error('Failed to load learning resource', err);
      if (status === 404) {
        setNotFound(true);
      } else if (status === 401 || status === 403) {
        setError('Your session has expired or you do not have access. Please sign in again.');
      } else if (status >= 500) {
        setError('The server is currently unavailable. Please try again in a moment.');
      } else if (!status) {
        setError('Unable to reach the server. Please check your connection.');
      } else {
        setError('Unable to load this learning resource. Please try again.');
      }
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="mx-auto max-w-4xl px-4 sm:px-6 lg:px-8">
        <LoadingState message="Loading learning resource..." />
      </div>
    );
  }

  if (notFound) {
    return (
      <div className="mx-auto max-w-4xl px-4 py-16 sm:px-6 lg:px-8">
        <div className="card mx-auto max-w-md p-8 text-center">
          <AlertTriangle className="mx-auto mb-3 h-8 w-8 text-warning" aria-hidden="true" />
          <h2 className="text-base font-bold text-navy">Resource not found</h2>
          <p className="mt-1 text-sm text-slate-500">
            The learning resource you are looking for does not exist or is no longer available.
          </p>
          <Link to="/resources" className="btn-secondary mt-5">
            <ArrowLeft className="h-4 w-4" aria-hidden="true" />
            Back to resources
          </Link>
        </div>
      </div>
    );
  }

  if (!resource) {
    return (
      <div className="mx-auto max-w-4xl space-y-4 px-4 py-6 sm:px-6 sm:py-8 lg:px-8">
        <Link
          to="/resources"
          className="inline-flex items-center gap-1.5 text-xs font-semibold text-slate-600 transition-colors hover:text-navy"
        >
          <ArrowLeft className="h-4 w-4" aria-hidden="true" />
          Back to resources
        </Link>
        <ErrorState
          title="Could not load resource"
          message={error || 'Unable to load this learning resource.'}
          onRetry={fetchResource}
        />
      </div>
    );
  }

  return (
    <div className="mx-auto max-w-4xl space-y-6 px-4 py-6 sm:px-6 sm:py-8 lg:px-8">
      <Link
        to="/resources"
        className="inline-flex items-center gap-1.5 text-xs font-semibold text-slate-600 transition-colors hover:text-navy"
      >
        <ArrowLeft className="h-4 w-4" aria-hidden="true" />
        Back to resources
      </Link>

      <PageHeader
        eyebrow={resource.subjectName || 'Learning resource'}
        title={resource.title}
        description={resource.summary || undefined}
        actions={
          resource.resourceUrl ? (
            <a
              href={resource.resourceUrl}
              target="_blank"
              rel="noreferrer"
              className="btn-primary"
            >
              <ExternalLink className="h-4 w-4" aria-hidden="true" />
              Open resource
            </a>
          ) : (
            <span className="inline-flex items-center gap-1.5 rounded-xl border border-slate-200 bg-slate-50 px-3.5 py-2 text-xs font-semibold text-slate-500">
              No external link available
            </span>
          )
        }
      />

      <section className="card space-y-5 p-6 sm:p-8">
        <div className="flex flex-wrap items-center gap-2">
          <span className="badge-neutral">{resource.contentType}</span>
          {resource.difficultyLevel && (
            <span className="badge-teal">{resource.difficultyLevel}</span>
          )}
        </div>

        {resource.topicName && (
          <div className="rounded-xl border border-slate-200 bg-slate-50 p-4">
            <h3 className="mb-2 text-xs font-bold uppercase tracking-wider text-slate-500">
              Part of topic
            </h3>
            <div className="flex flex-wrap items-center justify-between gap-3">
              <div>
                {resource.subjectName && (
                  <p className="text-[11px] font-semibold uppercase tracking-wider text-primary-700">
                    {resource.subjectName}
                  </p>
                )}
                <p className="text-sm font-semibold text-navy">{resource.topicName}</p>
              </div>
              {resource.topicId && (
                <Link
                  to={`/topics/${resource.topicId}`}
                  className="inline-flex items-center gap-1.5 rounded-lg bg-white px-3 py-1.5 text-xs font-semibold text-slate-700 shadow-sm transition-colors hover:bg-primary-50 hover:text-primary-700"
                >
                  <BookOpen className="h-3.5 w-3.5" aria-hidden="true" />
                  View topic notes
                </Link>
              )}
            </div>
          </div>
        )}

        {!resource.resourceUrl && (
          <p className="rounded-xl border border-amber-200 bg-amber-50 p-4 text-sm leading-relaxed text-amber-800">
            This resource does not have an external link yet. Review the topic notes and take the
            topic quiz while the link is being added.
          </p>
        )}
      </section>
    </div>
  );
}
