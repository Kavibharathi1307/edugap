import React, { useState, useEffect } from 'react';
import { useParams, useLocation, Link } from 'react-router-dom';
import api from '../api/client';
import { LoadingState } from '../components/ui/StateViews';
import ProgressBar from '../components/ui/ProgressBar';
import {
  Award,
  CheckCircle2,
  AlertTriangle,
  ArrowRight,
  BookOpen,
  LayoutDashboard,
  Check,
  X,
} from 'lucide-react';

export default function QuizResult() {
  const { attemptId } = useParams();
  const location = useLocation();

  const [result, setResult] = useState(location.state?.result || null);
  const [loading, setLoading] = useState(!result);
  const [error, setError] = useState('');
  const [activeTab, setActiveTab] = useState('gaps');

  useEffect(() => {
    if (!result && attemptId) {
      fetchResult();
    }
  }, [attemptId]);

  const fetchResult = async () => {
    try {
      const res = await api.get(`/quizzes/attempts/${attemptId}`);
      if (res.data?.data) {
        setResult(res.data.data);
      }
    } catch (err) {
      console.error('Failed to load quiz results', err);
      setError('Unable to load assessment result.');
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return (
      <div className="mx-auto max-w-4xl px-4 sm:px-6 lg:px-8">
        <LoadingState message="Evaluating assessment responses..." />
      </div>
    );
  }

  if (error || !result) {
    return (
      <div className="mx-auto max-w-md px-4 py-16 text-center">
        <div className="card p-8">
          <AlertTriangle className="mx-auto mb-3 h-8 w-8 text-warning" aria-hidden="true" />
          <p className="text-sm text-slate-600">{error || 'Result not found.'}</p>
          <Link to="/dashboard" className="btn-primary mt-5">
            <LayoutDashboard className="h-4 w-4" aria-hidden="true" />
            Return to dashboard
          </Link>
        </div>
      </div>
    );
  }

  const isPassed = result.scorePercentage >= 70;
  const scoreTone = result.scorePercentage >= 75
    ? 'text-green-600'
    : result.scorePercentage >= 50
    ? 'text-amber-600'
    : 'text-red-600';

  const tabs = [
    { id: 'gaps', label: 'Knowledge gap diagnosis', count: result.topicPerformances?.length || 0 },
    { id: 'recommendations', label: 'Recommendations', count: result.recommendations?.length || 0 },
    { id: 'review', label: 'Question review', count: result.answersReview?.length || 0 },
  ];

  return (
    <div className="mx-auto max-w-4xl space-y-6 px-4 py-6 sm:px-6 sm:py-8 lg:px-8">
      {/* Score Hero */}
      <section className="card relative overflow-hidden p-6 text-center sm:p-8">
        <div
          className={`pointer-events-none absolute inset-x-0 top-0 h-1 ${
            isPassed ? 'bg-primary-600' : 'bg-warning'
          }`}
          aria-hidden="true"
        />
        <div
          className={`mx-auto mb-4 flex h-14 w-14 items-center justify-center rounded-2xl ${
            isPassed ? 'bg-primary-100 text-primary-700' : 'bg-amber-50 text-amber-700'
          }`}
        >
          <Award className="h-7 w-7" aria-hidden="true" />
        </div>

        <h1 className="text-2xl font-bold tracking-tight text-navy sm:text-3xl">
          Assessment evaluation complete
        </h1>
        <p className="mt-1 text-sm text-slate-500">
          {result.subjectName} &bull; Completed on {new Date(result.completedAt).toLocaleString()}
        </p>

        <div className="mx-auto mt-6 max-w-md">
          <div className="flex items-end justify-center gap-2">
            <span className={`text-5xl font-black tracking-tight ${scoreTone}`}>
              {result.scorePercentage}%
            </span>
            <span className="pb-1 text-sm text-slate-400">overall score</span>
          </div>
          <ProgressBar value={result.scorePercentage} className="mt-4" size="lg" />
          <div className="mt-3 flex items-center justify-center gap-2 text-sm">
            <span className="font-semibold text-slate-700">
              {result.correctAnswers} / {result.totalQuestions} correct
            </span>
            <span className="text-slate-300">&bull;</span>
            <span className="text-xs text-slate-500">
              {result.totalQuestions - result.correctAnswers} missed questions analyzed
            </span>
          </div>
        </div>

        <div className="mt-6 flex flex-wrap justify-center gap-3">
          <Link to="/dashboard" className="btn-secondary !text-xs">
            <LayoutDashboard className="h-4 w-4" aria-hidden="true" />
            Dashboard
          </Link>
          <Link
            to={result.subjectId ? `/subjects/${result.subjectId}` : '/subjects'}
            className="btn-primary !text-xs"
          >
            {result.subjectId ? 'Take another quiz' : 'Browse subjects'}
            <ArrowRight className="h-4 w-4" aria-hidden="true" />
          </Link>
        </div>
      </section>

      {/* Tab Navigation */}
      <div className="overflow-x-auto border-b border-slate-200">
        <div className="flex min-w-max gap-6" role="tablist" aria-label="Assessment result sections">
          {tabs.map((tab) => (
            <button
              key={tab.id}
              type="button"
              role="tab"
              aria-selected={activeTab === tab.id}
              onClick={() => setActiveTab(tab.id)}
              className={`cursor-pointer border-b-2 pb-3 text-sm font-semibold transition-colors ${
                activeTab === tab.id
                  ? 'border-primary-600 text-primary-700'
                  : 'border-transparent text-slate-500 hover:text-navy'
              }`}
            >
              {tab.label} ({tab.count})
            </button>
          ))}
        </div>
      </div>

      {/* TAB 1: KNOWLEDGE GAP DIAGNOSIS */}
      {activeTab === 'gaps' && (
        <div className="space-y-4">
          <p className="text-xs leading-relaxed text-slate-500">
            Topic accuracy analyzed by the deterministic Knowledge Gap Engine. Topics under 40% are
            flagged as high-priority gaps.
          </p>

          {result.topicPerformances?.length ? (
            <div className="grid grid-cols-1 gap-4 sm:grid-cols-2">
              {result.topicPerformances.map((tp) => (
                <div key={tp.topicId} className="card flex flex-col justify-between p-5">
                  <div>
                    <div className="mb-2 flex items-center justify-between gap-2">
                      <span
                        className="rounded-full px-2.5 py-0.5 text-xs font-semibold"
                        style={{
                          backgroundColor: `${tp.badgeColor}15`,
                          color: tp.badgeColor,
                        }}
                      >
                        {tp.gapLabel}
                      </span>
                      <span className="font-mono text-xs font-bold text-slate-700">
                        {tp.correctAnswers}/{tp.questionsAttempted} correct
                      </span>
                    </div>

                    <h3 className="mb-3 text-base font-bold text-navy">{tp.topicName}</h3>
                    <div className="h-2 w-full overflow-hidden rounded-full bg-slate-100">
                      <div
                        className="h-2 rounded-full transition-all"
                        style={{
                          width: `${Math.min(100, tp.accuracyPercentage)}%`,
                          backgroundColor: tp.badgeColor,
                        }}
                      />
                    </div>
                    <div className="mt-1 text-right text-[11px] font-semibold text-slate-400">
                      Accuracy: {tp.accuracyPercentage}%
                    </div>
                  </div>

                  <div className="mt-4 flex items-center justify-between border-t border-slate-100 pt-3">
                    <Link
                      to={`/topics/${tp.topicId}`}
                      className="inline-flex items-center gap-1 text-xs font-semibold text-primary-700 hover:underline"
                    >
                      <BookOpen className="h-3.5 w-3.5" aria-hidden="true" />
                      Read topic notes
                    </Link>
                    <Link
                    to={`/quizzes/play?topicId=${tp.topicId}${
                      result.subjectId ? `&subjectId=${result.subjectId}` : ''
                    }`}
                    className="text-xs text-slate-500 hover:text-navy"
                  >
                    Practice quiz &rarr;
                  </Link>
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <div className="card p-8 text-center text-sm text-slate-500">
              No topic performance data was returned for this attempt.
            </div>
          )}
        </div>
      )}

      {/* TAB 2: PERSONALIZED RECOMMENDATIONS */}
      {activeTab === 'recommendations' && (
        <div className="space-y-4">
          <p className="text-xs leading-relaxed text-slate-500">
            Curated remedial activities generated specifically for your identified gaps.
          </p>

          {result.recommendations && result.recommendations.length > 0 ? (
            <div className="space-y-3">
              {result.recommendations.map((rec) => (
                <div
                  key={rec.id}
                  className="card flex flex-col gap-4 p-5 sm:flex-row sm:items-center sm:justify-between"
                >
                  <div className="space-y-1">
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
                    </div>
                    <p className="text-sm font-semibold text-navy">{rec.recommendationText}</p>
                    <p className="text-xs italic text-slate-500">{rec.reason}</p>
                  </div>

                  <Link to={`/topics/${rec.topicId}`} className="btn-primary shrink-0 !text-xs">
                    Remediate gap
                    <ArrowRight className="h-3.5 w-3.5" aria-hidden="true" />
                  </Link>
                </div>
              ))}
            </div>
          ) : (
            <div className="card p-8 text-center text-sm text-slate-500">
              <CheckCircle2 className="mx-auto mb-2 h-8 w-8 text-success" aria-hidden="true" />
              Great job! No high-priority gaps were detected for this attempt.
            </div>
          )}
        </div>
      )}

      {/* TAB 3: QUESTION REVIEW */}
      {activeTab === 'review' && (
        <div className="space-y-5">
          <p className="text-xs leading-relaxed text-slate-500">
            Verify every answer against verified keys and read detailed Java conceptual
            explanations.
          </p>

          {result.answersReview?.length ? (
            result.answersReview.map((ans, idx) => (
              <div
                key={idx}
                className={`card space-y-4 p-5 sm:p-6 ${
                  ans.correct ? 'border-green-200' : 'border-red-200'
                }`}
              >
                <div className="flex flex-wrap items-center justify-between gap-2">
                  <span className="text-xs font-bold text-slate-400">
                    Question {idx + 1} &bull; <span className="text-slate-600">{ans.topicName}</span>
                  </span>
                  {ans.correct ? (
                    <span className="badge-success">
                      <Check className="h-3.5 w-3.5" aria-hidden="true" />
                      Correct (+1)
                    </span>
                  ) : (
                    <span className="badge-danger">
                      <X className="h-3.5 w-3.5" aria-hidden="true" />
                      Incorrect (0)
                    </span>
                  )}
                </div>

                <h3 className="text-base font-bold text-navy">{ans.questionText}</h3>

                <div className="grid grid-cols-1 gap-3 text-xs sm:grid-cols-2">
                  <div className="rounded-lg border border-slate-200 bg-slate-50 p-3">
                    <span className="mb-0.5 block font-semibold text-slate-400">
                      Your selected answer:
                    </span>
                    <span className="font-bold text-slate-800">
                      Option {ans.selectedOptionKey || 'None (skipped)'}
                    </span>
                  </div>
                  <div className="rounded-lg border border-green-200 bg-green-50/70 p-3">
                    <span className="mb-0.5 block font-semibold text-green-700">
                      Verified correct key:
                    </span>
                    <span className="font-bold text-green-900">Option {ans.correctOptionKey}</span>
                  </div>
                </div>

                {ans.explanation && (
                  <div className="rounded-lg border border-primary-200 bg-primary-50/70 p-3.5 text-xs leading-relaxed text-primary-900">
                    <strong className="text-primary-800">Explanation: </strong>
                    {ans.explanation}
                  </div>
                )}
              </div>
            ))
          ) : (
            <div className="card p-8 text-center text-sm text-slate-500">
              No question review data is available for this attempt.
            </div>
          )}
        </div>
      )}
    </div>
  );
}
