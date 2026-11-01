import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import api from '../api/client';
import PageHeader from '../components/ui/PageHeader';
import ProgressBar from '../components/ui/ProgressBar';
import { EmptyState, ErrorState, LoadingState } from '../components/ui/StateViews';
import {
  BookOpen,
  CheckCircle2,
  Circle,
  Layers,
  RotateCw,
} from 'lucide-react';

export default function TopicList() {
  const [topics, setTopics] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchTopics();
  }, []);

  const fetchTopics = async () => {
    setLoading(true);
    setError('');
    try {
      const res = await api.get('/topics');
      if (res.data?.data) {
        setTopics(res.data.data);
      }
    } catch (err) {
      console.error('Failed to load topics', err);
      setError('Unable to load topics. Please check your connection.');
    } finally {
      setLoading(false);
    }
  };

  const completedCount = topics.filter((t) => t.completed).length;
  const completionPercent = topics.length
    ? Math.round((completedCount / topics.length) * 100)
    : 0;

  return (
    <div className="mx-auto max-w-7xl space-y-6 px-4 py-6 sm:px-6 sm:py-8 lg:px-8">
      <PageHeader
        eyebrow="Curriculum"
        title="Learning topics"
        description="Structured modules across all subjects, covering fundamentals, core concepts, and applied problem solving."
        actions={
          <>
            <div className="flex items-center gap-2 rounded-xl border border-slate-200 bg-white px-3.5 py-2 text-xs font-semibold text-slate-600 shadow-sm">
              <span className="h-2 w-2 rounded-full bg-primary-600" />
              {completedCount} of {topics.length} completed
            </div>
            <Link to="/quizzes" className="btn-secondary">
              <RotateCw className="h-4 w-4" aria-hidden="true" />
              Take a comprehensive quiz
            </Link>
            <Link to="/subjects" className="btn-primary">
              <Layers className="h-4 w-4" aria-hidden="true" />
              Browse available subjects
            </Link>
          </>
        }
      />

      <div className="card p-4 sm:p-5">
        <ProgressBar
          value={completionPercent}
          label="Curriculum completion"
          showValue
          tone="bg-primary-600"
        />
      </div>

      {error ? (
        <ErrorState
          title="Could not load topics"
          message={error}
          onRetry={fetchTopics}
        />
      ) : loading ? (
        <LoadingState message="Loading curriculum..." />
      ) : topics.length === 0 ? (
        <div className="card">
          <EmptyState
            icon={BookOpen}
            title="No topics available"
            message="The curriculum has not been published yet. Please check back later."
          />
        </div>
      ) : (
        <div className="grid grid-cols-1 gap-5 md:grid-cols-2">
          {topics.map((topic, index) => (
            <article
              key={topic.id}
              className="card card-hover flex flex-col justify-between p-6"
            >
              <div>
                <div className="mb-3 flex items-center justify-between gap-2">
                  <span className="badge-teal">
                    Module {String(topic.orderIndex || index + 1).padStart(2, '0')}
                  </span>
                  {topic.completed ? (
                    <span className="badge-success">
                      <CheckCircle2 className="h-3.5 w-3.5" aria-hidden="true" />
                      Completed
                    </span>
                  ) : (
                    <span className="badge-neutral">
                      <Circle className="h-3 w-3" aria-hidden="true" />
                      Not started
                    </span>
                  )}
                </div>

                {topic.subjectName && (
                  <p className="text-[11px] font-semibold uppercase tracking-wider text-primary-700">
                    {topic.subjectName}
                  </p>
                )}
                <h3 className="text-lg font-bold text-navy">{topic.name}</h3>
                <p className="mt-2 line-clamp-2 text-sm leading-relaxed text-slate-600">
                  {topic.description}
                </p>
              </div>

              <div className="mt-5 flex flex-wrap items-center justify-between gap-3 border-t border-slate-100 pt-4">
                <Link
                  to={`/topics/${topic.id}`}
                  className="inline-flex items-center gap-1.5 text-xs font-semibold text-slate-700 transition-colors hover:text-primary-700"
                >
                  <BookOpen className="h-4 w-4 text-slate-400" aria-hidden="true" />
                  Study notes & code
                </Link>
                <Link
                  to={`/quizzes/play?topicId=${topic.id}${
                    topic.subjectId ? `&subjectId=${topic.subjectId}` : ''
                  }`}
                  className="inline-flex items-center gap-1.5 rounded-lg bg-slate-100 px-3 py-1.5 text-xs font-semibold text-slate-700 transition-colors hover:bg-primary-50 hover:text-primary-700"
                >
                  <RotateCw className="h-3.5 w-3.5" aria-hidden="true" />
                  Practice quiz
                </Link>
              </div>
            </article>
          ))}
        </div>
      )}
    </div>
  );
}
