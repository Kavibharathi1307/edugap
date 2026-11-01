import React, { useState, useEffect } from 'react';
import { Link, useSearchParams } from 'react-router-dom';
import api from '../api/client';
import PageHeader from '../components/ui/PageHeader';
import { EmptyState, ErrorState } from '../components/ui/StateViews';
import {
  CheckSquare,
  ArrowRight,
  Clock,
  Award,
  Zap,
  BookOpen,
} from 'lucide-react';

export default function QuizCatalog() {
  const [searchParams] = useSearchParams();
  const selectedTopicParam = searchParams.get('topicId');
  const subjectId = searchParams.get('subjectId');

  const [topics, setTopics] = useState([]);
  const [subject, setSubject] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    loadCatalogue();
  }, [subjectId]);

  const loadCatalogue = async () => {
    setLoading(true);
    setError('');
    try {
      const requests = [
        api.get(`/topics${subjectId ? `?subjectId=${subjectId}` : ''}`),
        subjectId ? api.get(`/subjects/${subjectId}`) : Promise.resolve(null),
      ];
      const [topicsRes, subjectRes] = await Promise.all(requests);
      if (topicsRes.data?.data) {
        setTopics(topicsRes.data.data);
      }
      if (subjectRes?.data?.data) {
        setSubject(subjectRes.data.data);
      }
    } catch (err) {
      console.error('Failed to load assessment catalogue', err);
      setError('Unable to load the assessment catalogue. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const comprehensiveLink = subjectId ? `/quizzes/play?subjectId=${subjectId}` : '/subjects';
  const comprehensiveTitle = subjectId
    ? `Comprehensive ${subject?.name || 'subject'} assessment`
    : 'Choose a subject for a comprehensive assessment';
  const comprehensiveDescription = subjectId
    ? `Evaluates your mastery across all ${subject?.topicCount || topics.length} topics in this subject. The engine analyzes your answers and generates a baseline knowledge-gap radar.`
    : 'Each subject assessment evaluates your mastery across that subject and generates a baseline knowledge-gap radar. Pick a subject to begin.';

  return (
    <div className="mx-auto max-w-7xl space-y-6 px-4 py-6 sm:px-6 sm:py-8 lg:px-8">
      <PageHeader
        eyebrow="Assessments"
        title={subject ? `${subject.name} assessments` : 'Assessment center'}
        description={
          subject
            ? 'Take a full subject assessment or focus on a specific module to diagnose your gaps.'
            : 'Choose a subject for a comprehensive evaluation or focus on a specific module to diagnose your gaps.'
        }
      />

      {/* Featured: Comprehensive Assessment */}
      <section className="relative overflow-hidden rounded-2xl bg-gradient-to-r from-[#0F172A] via-[#134E4A] to-primary-700 p-6 text-white shadow-card sm:p-8">
        <div
          className="pointer-events-none absolute -right-16 -top-20 h-56 w-56 rounded-full bg-primary-400/20 blur-3xl"
          aria-hidden="true"
        />
        <div className="relative z-10 flex flex-col gap-6 md:flex-row md:items-center md:justify-between">
          <div className="max-w-xl">
            <span className="mb-3 inline-flex items-center gap-1.5 rounded-full bg-white/10 px-3 py-1 text-xs font-semibold uppercase tracking-wider text-primary-100 ring-1 ring-inset ring-white/15">
              <Zap className="h-3.5 w-3.5 text-amber-300" aria-hidden="true" />
              Recommended first step
            </span>
            <h2 className="text-xl font-bold sm:text-2xl">{comprehensiveTitle}</h2>
            <p className="mt-2 text-sm leading-relaxed text-primary-100">
              {comprehensiveDescription}
            </p>
            <div className="mt-4 flex flex-wrap items-center gap-4 text-xs text-primary-200">
              <span className="flex items-center gap-1">
                <CheckSquare className="h-4 w-4" aria-hidden="true" /> 10 questions
              </span>
              <span className="flex items-center gap-1">
                <Clock className="h-4 w-4" aria-hidden="true" /> 10 minutes
              </span>
              <span className="flex items-center gap-1">
                <Award className="h-4 w-4" aria-hidden="true" /> Instant gap report
              </span>
            </div>
          </div>

          <Link
            to={comprehensiveLink}
            className="inline-flex shrink-0 items-center justify-center gap-2 rounded-xl bg-white px-6 py-3.5 text-sm font-bold text-primary-700 shadow-sm transition-colors hover:bg-primary-50"
          >
            {subjectId ? 'Start full assessment' : 'Browse subjects'}
            <ArrowRight className="h-4 w-4" aria-hidden="true" />
          </Link>
        </div>
      </section>

      {/* Topic-Specific Quizzes */}
      <section className="space-y-4 pt-2">
        <div>
          <h2 className="text-lg font-bold text-navy">Topic-specific focus quizzes</h2>
          <p className="mt-1 text-xs text-slate-500">
            Take a focused four-question quiz on a single module to verify understanding or clear a
            diagnosed gap.
          </p>
        </div>

        {error ? (
          <ErrorState title="Could not load assessments" message={error} onRetry={loadCatalogue} />
        ) : loading ? (
          <div className="card p-8 text-center text-sm text-slate-500">
            <div className="mx-auto mb-3 h-7 w-7 animate-spin rounded-full border-[3px] border-primary-600 border-t-transparent" />
            Loading quiz catalogue...
          </div>
        ) : topics.length === 0 ? (
          <div className="card">
            <EmptyState
              icon={BookOpen}
              title="No topic quizzes yet"
              message="Topic-specific quizzes will appear once the curriculum is published."
            />
          </div>
        ) : (
          <div className="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-4">
            {topics.map((t) => {
              const selected = selectedTopicParam && String(t.id) === selectedTopicParam;
              return (
                <div
                  key={t.id}
                  className={`card flex flex-col justify-between p-5 transition-all hover:shadow-card-hover ${
                    selected
                      ? 'border-primary-500 ring-2 ring-primary-500/20'
                      : 'hover:border-slate-300'
                  }`}
                >
                  <div>
                    <span className="text-[11px] font-bold uppercase tracking-wider text-slate-400">
                      Module {String(t.orderIndex || 1).padStart(2, '0')}
                    </span>
                    <h3 className="mb-2 mt-1 text-sm font-bold text-navy">{t.name}</h3>
                    <p className="line-clamp-2 text-xs leading-relaxed text-slate-500">
                      {t.description}
                    </p>
                  </div>

                  <div className="mt-4 flex items-center justify-between border-t border-slate-100 pt-3">
                    <span className="text-[11px] font-medium text-slate-400">4 questions</span>
                    <Link
                      to={`/quizzes/play?topicId=${t.id}${
                        subjectId || t.subjectId ? `&subjectId=${subjectId || t.subjectId}` : ''
                      }`}
                      className="inline-flex items-center gap-1 text-xs font-semibold text-primary-700 hover:text-primary-800"
                    >
                      Start &rarr;
                    </Link>
                  </div>
                </div>
              );
            })}
          </div>
        )}
      </section>
    </div>
  );
}
