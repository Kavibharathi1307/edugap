import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import api from '../api/client';
import PageHeader from '../components/ui/PageHeader';
import { EmptyState, ErrorState, LoadingState } from '../components/ui/StateViews';
import { History as HistoryIcon, PlayCircle } from 'lucide-react';

export default function History() {
  const [history, setHistory] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    loadHistory();
  }, []);

  const loadHistory = async () => {
    setLoading(true);
    setError('');
    try {
      const res = await api.get('/quizzes/history');
      if (res.data?.data) {
        setHistory(res.data.data);
      }
    } catch (err) {
      console.error('Failed to load history', err);
      setError('Unable to load assessment history. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="mx-auto max-w-5xl space-y-6 px-4 py-6 sm:px-6 sm:py-8 lg:px-8">
      <PageHeader
        eyebrow="Records"
        title="Assessment history"
        description="Audit log of all your submitted assessments, scores, and question evaluations."
        actions={
          <Link to="/quizzes" className="btn-primary">
            <PlayCircle className="h-4 w-4" aria-hidden="true" />
            Take new quiz
          </Link>
        }
      />

      {loading ? (
        <LoadingState message="Loading assessment records..." />
      ) : error ? (
        <ErrorState title="Could not load history" message={error} onRetry={loadHistory} />
      ) : history.length > 0 ? (
        <div className="table-shell">
          <div className="overflow-x-auto">
            <table className="data-table min-w-[720px]">
              <thead>
                <tr>
                  <th>Attempt</th>
                  <th>Subject / assessment</th>
                  <th>Questions answered</th>
                  <th>Mastery score</th>
                  <th>Timestamp</th>
                  <th className="text-right">Details</th>
                </tr>
              </thead>
              <tbody>
                {history.map((a) => (
                  <tr key={a.attemptId}>
                    <td className="font-mono font-medium text-slate-600">#{a.attemptId}</td>
                    <td className="font-semibold text-navy">{a.subjectName}</td>
                    <td className="text-slate-600">
                      {a.correctAnswers} / {a.totalQuestions}
                    </td>
                    <td>
                      <span
                        className={
                          a.scorePercentage >= 75
                            ? 'badge-success'
                            : a.scorePercentage >= 50
                            ? 'badge-warning'
                            : 'badge-danger'
                        }
                      >
                        {a.scorePercentage}%
                      </span>
                    </td>
                    <td className="whitespace-nowrap text-xs text-slate-500">
                      {new Date(a.completedAt).toLocaleString(undefined, {
                        month: 'short',
                        day: 'numeric',
                        year: 'numeric',
                        hour: '2-digit',
                        minute: '2-digit',
                      })}
                    </td>
                    <td className="text-right">
                      <Link
                        to={`/quizzes/result/${a.attemptId}`}
                        className="inline-flex items-center gap-1 text-xs font-semibold text-primary-700 hover:text-primary-800"
                      >
                        Review &rarr;
                      </Link>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      ) : (
        <div className="card mx-auto max-w-md">
          <EmptyState
            icon={HistoryIcon}
            title="No assessments yet"
            message="Take your first assessment to record your scores and generate knowledge gap analysis."
            action={
              <Link to="/quizzes" className="btn-primary mt-1 !text-xs">
                Start first quiz &rarr;
              </Link>
            }
          />
        </div>
      )}
    </div>
  );
}
