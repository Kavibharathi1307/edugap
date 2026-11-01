import React, { useEffect, useState } from 'react';
import { Link, useParams } from 'react-router-dom';
import api from '../api/client';
import PageHeader from '../components/ui/PageHeader';
import ProgressBar from '../components/ui/ProgressBar';
import StatCard from '../components/ui/StatCard';
import { EmptyState, ErrorState, LoadingState } from '../components/ui/StateViews';
import {
  ArrowLeft,
  ArrowRight,
  Award,
  BookOpen,
  Calculator,
  CheckCircle2,
  CheckSquare,
  Coffee,
  Database,
  GitBranch,
  Layers,
  Network,
  PlayCircle,
  Terminal,
  TrendingUp,
} from 'lucide-react';

const subjectIcons = {
  coffee: Coffee,
  terminal: Terminal,
  account_tree: GitBranch,
  storage: Database,
  lan: Network,
  calculate: Calculator,
};

function SubjectIcon({ icon, className = 'h-6 w-6' }) {
  const Icon = subjectIcons[icon] || BookOpen;
  return <Icon className={className} aria-hidden="true" />;
}

function formatDate(value) {
  if (!value) return '—';
  return new Date(value).toLocaleString(undefined, {
    dateStyle: 'medium',
    timeStyle: 'short',
  });
}

export default function SubjectDetail() {
  const { subjectId } = useParams();
  const [subject, setSubject] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchSubject();
  }, [subjectId]);

  const fetchSubject = async () => {
    setLoading(true);
    setError('');
    try {
      const res = await api.get(`/subjects/${subjectId}`);
      if (res.data?.data) {
        setSubject(res.data.data);
      }
    } catch (err) {
      console.error('Failed to load subject details', err);
      setError('Unable to load this subject. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
        <LoadingState message="Loading subject curriculum..." />
      </div>
    );
  }

  if (error || !subject) {
    return (
      <div className="mx-auto max-w-7xl px-4 py-6 sm:px-6 lg:px-8">
        <ErrorState
          title="Could not load subject"
          message={error || 'This subject could not be found.'}
          onRetry={fetchSubject}
        />
      </div>
    );
  }

  const topics = subject.topics || [];
  const recentAttempts = subject.recentAttempts || [];
  const assessedPercent = subject.topicCount
    ? Math.round((subject.assessedTopicCount / subject.topicCount) * 100)
    : 0;

  return (
    <div className="mx-auto max-w-7xl space-y-6 px-4 py-6 sm:px-6 sm:py-8 lg:px-8">
      <PageHeader
        eyebrow={subject.code}
        title={subject.name}
        description={subject.description}
        actions={
          <>
            <Link to="/subjects" className="btn-secondary !text-xs">
              <ArrowLeft className="h-4 w-4" aria-hidden="true" />
              All subjects
            </Link>
            <Link
              to={`/quizzes/play?subjectId=${subject.id}`}
              className="btn-primary !text-xs"
            >
              <PlayCircle className="h-4 w-4" aria-hidden="true" />
              Start subject assessment
            </Link>
          </>
        }
      />

      <section className="grid grid-cols-2 gap-4 sm:gap-5 lg:grid-cols-4">
        <StatCard
          label="Topics"
          value={subject.topicCount}
          hint="Modules in this subject"
          icon={Layers}
          tone="teal"
        />
        <StatCard
          label="Assessed"
          value={`${subject.assessedTopicCount} / ${subject.topicCount}`}
          hint="Topics with quiz data"
          icon={CheckCircle2}
          tone="green"
        />
        <StatCard
          label="Assessments"
          value={subject.attemptCount}
          hint="Completed attempts"
          icon={Award}
          tone="amber"
        />
        <StatCard
          label="Average score"
          value={`${subject.averageScore || 0}%`}
          hint={
            subject.latestScore != null
              ? `Latest score ${subject.latestScore}%`
              : 'No assessment yet'
          }
          icon={TrendingUp}
          tone="teal"
        />
      </section>

      <section className="card flex flex-col gap-5 p-6 sm:flex-row sm:items-center sm:justify-between">
        <div className="flex items-center gap-4">
          <span className="flex h-14 w-14 shrink-0 items-center justify-center rounded-2xl bg-primary-100 text-primary-700">
            <SubjectIcon icon={subject.icon} className="h-7 w-7" />
          </span>
          <div>
            <p className="text-sm font-bold text-navy">Subject coverage</p>
            <p className="mt-0.5 text-xs text-slate-500">
              {subject.assessedTopicCount} of {subject.topicCount} topics assessed
              {subject.lastAttemptAt ? ` • last attempt ${formatDate(subject.lastAttemptAt)}` : ''}
            </p>
          </div>
        </div>
        <ProgressBar
          className="w-full sm:max-w-xs"
          value={assessedPercent}
          label="Assessed topics"
          showValue
          tone="bg-primary-600"
        />
      </section>

      <section className="space-y-4">
        <div className="flex flex-wrap items-end justify-between gap-3">
          <div>
            <h2 className="text-lg font-bold text-navy">Curriculum topics</h2>
            <p className="mt-1 text-xs text-slate-500">
              Study the notes, take a focused quiz, or start the full subject assessment.
            </p>
          </div>
          <Link
            to={`/quizzes?subjectId=${subject.id}`}
            className="text-xs font-semibold text-primary-700 hover:text-primary-800 hover:underline"
          >
            View assessment catalogue
          </Link>
        </div>

        {topics.length === 0 ? (
          <div className="card">
            <EmptyState
              icon={BookOpen}
              title="No topics available"
              message="Topics for this subject will appear once the curriculum is published."
            />
          </div>
        ) : (
          <div className="grid grid-cols-1 gap-5 md:grid-cols-2">
            {topics.map((topic) => (
              <article
                key={topic.id}
                className="card card-hover flex flex-col justify-between p-6"
              >
                <div>
                  <div className="mb-3 flex flex-wrap items-center justify-between gap-2">
                    <span className="badge-teal">
                      Module {String(topic.orderIndex || 1).padStart(2, '0')}
                    </span>
                    <span
                      className="badge"
                      style={{
                        backgroundColor: `${topic.badgeColor}15`,
                        color: topic.badgeColor,
                      }}
                    >
                      {topic.gapLabel}
                    </span>
                  </div>

                  <h3 className="text-lg font-bold text-navy">{topic.name}</h3>
                  <p className="mt-2 line-clamp-2 text-sm leading-relaxed text-slate-600">
                    {topic.description}
                  </p>

                  <div className="mt-4 flex flex-wrap items-center gap-x-4 gap-y-1 text-xs text-slate-500">
                    <span>{topic.questionCount} questions</span>
                    <span>
                      {topic.assessed
                        ? `${Math.round(topic.accuracyPercentage)}% accuracy`
                        : 'Not assessed'}
                    </span>
                    {topic.completed && (
                      <span className="inline-flex items-center gap-1 text-green-700">
                        <CheckCircle2 className="h-3.5 w-3.5" aria-hidden="true" />
                        Completed
                      </span>
                    )}
                  </div>
                </div>

                <div className="mt-5 flex flex-wrap items-center justify-between gap-3 border-t border-slate-100 pt-4">
                  <Link
                    to={`/topics/${topic.id}`}
                    className="inline-flex items-center gap-1.5 text-xs font-semibold text-slate-700 transition-colors hover:text-primary-700"
                  >
                    <BookOpen className="h-4 w-4 text-slate-400" aria-hidden="true" />
                    Study notes
                  </Link>
                  <Link
                    to={`/quizzes/play?topicId=${topic.id}&subjectId=${subject.id}`}
                    className="inline-flex items-center gap-1.5 rounded-lg bg-slate-100 px-3 py-1.5 text-xs font-semibold text-slate-700 transition-colors hover:bg-primary-50 hover:text-primary-700"
                  >
                    <CheckSquare className="h-3.5 w-3.5" aria-hidden="true" />
                    Practice quiz
                  </Link>
                </div>
              </article>
            ))}
          </div>
        )}
      </section>

      {recentAttempts.length > 0 && (
        <section className="space-y-3">
          <h2 className="text-lg font-bold text-navy">Recent attempts</h2>
          <div className="card divide-y divide-slate-100">
            {recentAttempts.map((attempt) => (
              <div
                key={attempt.attemptId}
                className="flex flex-wrap items-center justify-between gap-3 p-4 sm:px-6"
              >
                <div>
                  <p className="text-sm font-semibold text-navy">
                    {attempt.correctAnswers} / {attempt.totalQuestions} correct
                  </p>
                  <p className="mt-0.5 text-xs text-slate-500">
                    {formatDate(attempt.completedAt)}
                  </p>
                </div>
                <div className="flex items-center gap-3">
                  <span className="text-lg font-bold text-navy">
                    {Math.round(attempt.scorePercentage)}%
                  </span>
                  <Link
                    to={`/quizzes/result/${attempt.attemptId}`}
                    className="inline-flex items-center gap-1 text-xs font-semibold text-primary-700 hover:text-primary-800"
                  >
                    Review
                    <ArrowRight className="h-3.5 w-3.5" aria-hidden="true" />
                  </Link>
                </div>
              </div>
            ))}
          </div>
        </section>
      )}
    </div>
  );
}
