import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import api from '../api/client';
import PageHeader from '../components/ui/PageHeader';
import ProgressBar from '../components/ui/ProgressBar';
import { EmptyState, ErrorState, LoadingState } from '../components/ui/StateViews';
import {
  ArrowRight,
  BookOpen,
  Calculator,
  CheckSquare,
  Coffee,
  Database,
  GitBranch,
  Layers,
  Network,
  Terminal,
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

export default function SubjectSelection() {
  const [subjects, setSubjects] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchSubjects();
  }, []);

  const fetchSubjects = async () => {
    setLoading(true);
    setError('');
    try {
      const res = await api.get('/subjects');
      if (res.data?.data) {
        setSubjects(res.data.data);
      }
    } catch (err) {
      console.error('Failed to load subjects', err);
      setError('Unable to load the subject catalogue. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const totalTopics = subjects.reduce((sum, subject) => sum + (subject.topicCount || 0), 0);
  const assessedTopics = subjects.reduce(
    (sum, subject) => sum + (subject.assessedTopicCount || 0),
    0
  );
  const totalAttempts = subjects.reduce((sum, subject) => sum + (subject.attemptCount || 0), 0);

  return (
    <div className="mx-auto max-w-7xl space-y-6 px-4 py-6 sm:px-6 sm:py-8 lg:px-8">
      <PageHeader
        eyebrow="Subjects"
        title="Choose a learning track"
        description="Select a subject to review its curriculum, diagnose knowledge gaps, and receive targeted learning recommendations."
        actions={
          <div className="flex items-center gap-2 rounded-xl border border-slate-200 bg-white px-3.5 py-2 text-xs font-semibold text-slate-600 shadow-sm">
            <Layers className="h-4 w-4 text-primary-600" aria-hidden="true" />
            {subjects.length} subjects &bull; {totalTopics} topics
          </div>
        }
      />

      <div className="grid grid-cols-1 gap-4 sm:grid-cols-3">
        <div className="card p-5">
          <p className="text-xs font-semibold uppercase tracking-wider text-slate-500">
            Curriculum topics
          </p>
          <p className="mt-1 text-2xl font-bold text-navy">{totalTopics}</p>
        </div>
        <div className="card p-5">
          <p className="text-xs font-semibold uppercase tracking-wider text-slate-500">
            Topics assessed
          </p>
          <p className="mt-1 text-2xl font-bold text-navy">
            {assessedTopics}
            <span className="ml-1 text-sm font-normal text-slate-400">/ {totalTopics}</span>
          </p>
        </div>
        <div className="card p-5">
          <p className="text-xs font-semibold uppercase tracking-wider text-slate-500">
            Assessments taken
          </p>
          <p className="mt-1 text-2xl font-bold text-navy">{totalAttempts}</p>
        </div>
      </div>

      {error ? (
        <ErrorState title="Could not load subjects" message={error} onRetry={fetchSubjects} />
      ) : loading ? (
        <LoadingState message="Loading subject catalogue..." />
      ) : subjects.length === 0 ? (
        <div className="card">
          <EmptyState
            icon={Layers}
            title="No subjects available"
            message="Subjects will appear here once the curriculum is published."
          />
        </div>
      ) : (
        <div className="grid grid-cols-1 gap-5 md:grid-cols-2 xl:grid-cols-3">
          {subjects.map((subject) => {
            const assessedPercent = subject.topicCount
              ? Math.round((subject.assessedTopicCount / subject.topicCount) * 100)
              : 0;

            return (
              <article
                key={subject.id}
                className="card card-hover flex flex-col justify-between p-6"
              >
                <div>
                  <div className="mb-4 flex items-start justify-between gap-3">
                    <span className="flex h-12 w-12 items-center justify-center rounded-2xl bg-primary-100 text-primary-700">
                      <SubjectIcon icon={subject.icon} />
                    </span>
                    <span className="badge-teal">{subject.code}</span>
                  </div>

                  <h2 className="text-lg font-bold text-navy">{subject.name}</h2>
                  <p className="mt-2 line-clamp-3 text-sm leading-relaxed text-slate-600">
                    {subject.description}
                  </p>

                  <dl className="mt-4 grid grid-cols-3 gap-2 rounded-xl bg-slate-50 p-3 text-center">
                    <div>
                      <dt className="text-[10px] font-semibold uppercase tracking-wider text-slate-400">
                        Topics
                      </dt>
                      <dd className="text-sm font-bold text-navy">{subject.topicCount}</dd>
                    </div>
                    <div>
                      <dt className="text-[10px] font-semibold uppercase tracking-wider text-slate-400">
                        Assessed
                      </dt>
                      <dd className="text-sm font-bold text-navy">{subject.assessedTopicCount}</dd>
                    </div>
                    <div>
                      <dt className="text-[10px] font-semibold uppercase tracking-wider text-slate-400">
                        Latest
                      </dt>
                      <dd className="text-sm font-bold text-navy">
                        {subject.latestScore != null ? `${subject.latestScore}%` : '—'}
                      </dd>
                    </div>
                  </dl>

                  <ProgressBar
                    className="mt-4"
                    value={assessedPercent}
                    label="Topics assessed"
                    showValue
                    tone="bg-primary-600"
                  />
                </div>

                <div className="mt-5 flex flex-wrap items-center gap-2 border-t border-slate-100 pt-4">
                  <Link
                    to={`/subjects/${subject.id}`}
                    className="btn-secondary flex-1 !px-3 !py-2 !text-xs"
                  >
                    View subject
                    <ArrowRight className="h-3.5 w-3.5" aria-hidden="true" />
                  </Link>
                  <Link
                    to={`/quizzes/play?subjectId=${subject.id}`}
                    className="btn-primary flex-1 !px-3 !py-2 !text-xs"
                  >
                    <CheckSquare className="h-3.5 w-3.5" aria-hidden="true" />
                    Diagnostic quiz
                  </Link>
                </div>
              </article>
            );
          })}
        </div>
      )}
    </div>
  );
}
