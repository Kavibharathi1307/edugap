import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import api from '../api/client';
import { LoadingState } from '../components/ui/StateViews';
import {
  BookOpen,
  CheckCircle2,
  AlertTriangle,
  Code2,
  ExternalLink,
  ArrowLeft,
  Copy,
  Check,
  PlayCircle,
} from 'lucide-react';

export default function TopicDetail() {
  const { topicId } = useParams();
  const [topic, setTopic] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [copied, setCopied] = useState(false);
  const [updating, setUpdating] = useState(false);

  useEffect(() => {
    fetchTopic();
  }, [topicId]);

  const fetchTopic = async () => {
    try {
      const res = await api.get(`/topics/${topicId}`);
      if (res.data?.data) {
        setTopic(res.data.data);
      }
    } catch (err) {
      console.error('Failed to load topic details', err);
      setError('Failed to load topic notes. Please refresh.');
    } finally {
      setLoading(false);
    }
  };

  const toggleCompletion = async () => {
    if (!topic) return;
    setUpdating(true);
    try {
      const res = await api.post(`/topics/${topicId}/progress`, {
        completed: !topic.completed,
      });
      if (res.data?.data) {
        setTopic(res.data.data);
      }
    } catch (err) {
      console.error('Failed to update progress', err);
    } finally {
      setUpdating(false);
    }
  };

  const copyCode = () => {
    if (topic?.codeExample) {
      navigator.clipboard.writeText(topic.codeExample);
      setCopied(true);
      setTimeout(() => setCopied(false), 2000);
    }
  };

  if (loading) {
    return (
      <div className="mx-auto max-w-4xl px-4 sm:px-6 lg:px-8">
        <LoadingState message="Loading learning materials..." />
      </div>
    );
  }

  if (!topic) {
    return (
      <div className="mx-auto max-w-4xl px-4 py-16 text-center sm:px-6 lg:px-8">
        <div className="card mx-auto max-w-md p-8">
          <AlertTriangle className="mx-auto mb-3 h-8 w-8 text-warning" aria-hidden="true" />
          <h2 className="text-base font-bold text-navy">Topic not found</h2>
          <p className="mt-1 text-sm text-slate-500">{error || 'This topic is not available.'}</p>
          <Link to="/topics" className="btn-secondary mt-5">
            <ArrowLeft className="h-4 w-4" aria-hidden="true" />
            Back to topics
          </Link>
        </div>
      </div>
    );
  }

  return (
    <div className="mx-auto max-w-4xl space-y-6 px-4 py-6 sm:px-6 sm:py-8 lg:px-8">
      <div className="flex flex-wrap items-center justify-between gap-3">
        <Link
          to="/topics"
          className="inline-flex items-center gap-1.5 text-xs font-semibold text-slate-600 transition-colors hover:text-navy"
        >
          <ArrowLeft className="h-4 w-4" aria-hidden="true" />
          Back to curriculum
        </Link>

        <div className="flex flex-wrap items-center gap-2.5">
          <button
            type="button"
            onClick={toggleCompletion}
            disabled={updating}
            className={`inline-flex items-center gap-1.5 rounded-xl px-3.5 py-2 text-xs font-semibold transition-colors ${
              topic.completed
                ? 'border border-green-200 bg-green-50 text-green-700 hover:bg-green-100'
                : 'border border-slate-200 bg-white text-slate-700 shadow-sm hover:border-primary-500 hover:text-primary-700'
            }`}
          >
            <CheckCircle2 className="h-4 w-4" aria-hidden="true" />
            {updating
              ? 'Updating...'
              : topic.completed
              ? 'Marked completed'
              : 'Mark as completed'}
          </button>

          <Link to={`/quizzes?topicId=${topic.id}`} className="btn-primary !px-3.5 !py-2 !text-xs">
            <PlayCircle className="h-4 w-4" aria-hidden="true" />
            Take topic quiz
          </Link>
        </div>
      </div>

      {error && (
        <div role="alert" className="rounded-xl border border-red-200 bg-red-50 p-3.5 text-sm text-red-700">
          {error}
        </div>
      )}

      {/* Title Header */}
      <header className="card p-6 sm:p-8">
        <span className="badge-teal mb-3">
          Module {String(topic.orderIndex || 1).padStart(2, '0')} &bull; Core Java
        </span>
        <h1 className="text-2xl font-bold tracking-tight text-navy sm:text-3xl">{topic.name}</h1>
        <p className="mt-3 text-base leading-relaxed text-slate-600">{topic.description}</p>

        {topic.learningObjectives && (
          <div className="mt-6 rounded-xl border border-slate-200 bg-slate-50 p-4">
            <h3 className="mb-2 text-xs font-bold uppercase tracking-wider text-slate-500">
              Learning objectives
            </h3>
            <div className="whitespace-pre-line text-sm font-medium leading-relaxed text-slate-700">
              {topic.learningObjectives}
            </div>
          </div>
        )}
      </header>

      {/* Concept Explanation */}
      <section className="card space-y-4 p-6 sm:p-8">
        <h2 className="flex items-center gap-2 text-lg font-bold text-navy">
          <BookOpen className="h-5 w-5 text-primary-600" aria-hidden="true" />
          Concept explanation
        </h2>
        <div className="space-y-3 text-sm leading-relaxed text-slate-700">
          <p>{topic.beginnerExplanation}</p>
        </div>
      </section>

      {/* Java Code Example */}
      {topic.codeExample && (
        <section className="overflow-hidden rounded-2xl border border-slate-800 bg-[#0F172A] shadow-card">
          <div className="flex items-center justify-between border-b border-white/10 bg-black/20 px-5 py-3">
            <div className="flex items-center gap-2 font-mono text-xs text-slate-400">
              <Code2 className="h-4 w-4 text-primary-300" aria-hidden="true" />
              Example.java
            </div>
            <button
              type="button"
              onClick={copyCode}
              className="flex items-center gap-1.5 text-xs text-slate-400 transition-colors hover:text-white"
            >
              {copied ? (
                <Check className="h-3.5 w-3.5 text-primary-300" aria-hidden="true" />
              ) : (
                <Copy className="h-3.5 w-3.5" aria-hidden="true" />
              )}
              {copied ? 'Copied!' : 'Copy code'}
            </button>
          </div>
          <pre className="overflow-x-auto p-5 font-mono text-sm leading-relaxed text-primary-200">
            <code>{topic.codeExample}</code>
          </pre>
        </section>
      )}

      {/* Common Mistakes */}
      {topic.commonMistakes && (
        <section className="rounded-2xl border border-amber-200 bg-amber-50 p-6 shadow-card">
          <h3 className="mb-2 flex items-center gap-2 text-sm font-bold text-amber-900">
            <AlertTriangle className="h-4 w-4 text-warning" aria-hidden="true" />
            Common beginner mistakes to avoid
          </h3>
          <p className="text-sm leading-relaxed text-amber-800">{topic.commonMistakes}</p>
        </section>
      )}

      {/* Recommended Resources */}
      {topic.resources && topic.resources.length > 0 && (
        <section className="card space-y-4 p-6 sm:p-8">
          <h3 className="text-base font-bold text-navy">Recommended reference resources</h3>
          <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
            {topic.resources.map((res) => (
              <a
                key={res.id}
                href={res.resourceUrl}
                target="_blank"
                rel="noreferrer"
                className="group flex flex-col justify-between rounded-xl border border-slate-200 bg-slate-50/60 p-4 transition-colors hover:border-primary-300 hover:bg-primary-50/40"
              >
                <div>
                  <div className="mb-2 flex items-center justify-between">
                    <span className="badge-neutral">{res.contentType}</span>
                    <ExternalLink
                      className="h-3.5 w-3.5 text-slate-400 transition-colors group-hover:text-primary-600"
                      aria-hidden="true"
                    />
                  </div>
                  <h4 className="text-sm font-bold text-navy transition-colors group-hover:text-primary-700">
                    {res.title}
                  </h4>
                  <p className="mt-1 text-xs leading-relaxed text-slate-500">{res.summary}</p>
                </div>
                <div className="mt-3 border-t border-slate-200/70 pt-2 text-right">
                  <span className="text-[11px] font-semibold text-primary-700 group-hover:underline">
                    Open resource &rarr;
                  </span>
                </div>
              </a>
            ))}
          </div>
        </section>
      )}
    </div>
  );
}
