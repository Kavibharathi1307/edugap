import React, { useState, useEffect } from 'react';
import api from '../api/client';
import ProgressLineChart from '../components/charts/ProgressLineChart';
import TopicRadarChart from '../components/charts/TopicRadarChart';
import PageHeader from '../components/ui/PageHeader';
import ProgressBar from '../components/ui/ProgressBar';
import { ErrorState, LoadingState } from '../components/ui/StateViews';
import { TrendingUp, BarChart2 } from 'lucide-react';

export default function ProgressAnalytics() {
  const [progressData, setProgressData] = useState(null);
  const [topicsData, setTopicsData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    setLoading(true);
    setError('');
    try {
      const [progressRes, topicsRes] = await Promise.all([
        api.get('/analytics/progress'),
        api.get('/analytics/topics'),
      ]);

      if (progressRes.data?.data) {
        setProgressData(progressRes.data.data);
      }
      if (topicsRes.data?.data) {
        setTopicsData(topicsRes.data.data);
      }
    } catch (err) {
      console.error('Failed to load analytics', err);
      setError('Unable to load learning analytics. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="mx-auto max-w-7xl space-y-6 px-4 py-6 sm:px-6 sm:py-8 lg:px-8">
      <PageHeader
        eyebrow="Progress & trajectory"
        title="Learning growth analytics"
        description="Compare performance across consecutive assessments to verify knowledge retention and improvement."
      />

      {loading ? (
        <LoadingState message="Aggregating historical learning analytics..." />
      ) : error ? (
        <ErrorState title="Could not load analytics" message={error} onRetry={loadData} />
      ) : (
        <>
          {/* Progress Line Chart */}
          <section className="card space-y-4 p-6 sm:p-8">
            <div className="flex flex-col gap-2 sm:flex-row sm:items-center sm:justify-between">
              <div>
                <h2 className="flex items-center gap-2 text-base font-bold text-navy">
                  <TrendingUp className="h-5 w-5 text-primary-600" aria-hidden="true" />
                  Score progression over time
                </h2>
                <p className="text-xs text-slate-500">
                  Your overall assessment score against the 75% proficiency benchmark
                </p>
              </div>
              <span className="badge-neutral self-start sm:self-auto">
                {progressData?.labels?.length || 0} attempts recorded
              </span>
            </div>

            <div className="pt-4">
              <ProgressLineChart
                labels={progressData?.labels}
                overallScores={progressData?.overallScores}
              />
            </div>
          </section>

          {/* Radar & Topic Matrix */}
          <section className="grid grid-cols-1 gap-6 lg:grid-cols-12">
            <div className="card p-6 lg:col-span-6">
              <h2 className="mb-1 flex items-center gap-2 text-base font-bold text-navy">
                <BarChart2 className="h-5 w-5 text-primary-600" aria-hidden="true" />
                Current topic mastery spectrum
              </h2>
              <p className="mb-6 text-xs text-slate-500">
                Multi-axis radar comparing your latest percentage across all eight Java topics
              </p>

              <TopicRadarChart topicPerformances={topicsData} />
            </div>

            <div className="card p-6 lg:col-span-6">
              <h2 className="mb-1 text-base font-bold text-navy">Topic mastery status</h2>
              <p className="mb-4 text-xs text-slate-500">
                Current diagnosis per module with knowledge gap severity flags
              </p>

              {topicsData.length === 0 ? (
                <p className="py-8 text-center text-sm text-slate-500">
                  Complete an assessment to see topic-level mastery.
                </p>
              ) : (
                <div className="divide-y divide-slate-100">
                  {topicsData.map((t) => (
                    <div key={t.topicId} className="py-3">
                      <div className="flex items-start justify-between gap-3">
                        <div className="min-w-0">
                          <h3 className="truncate text-sm font-semibold text-navy">{t.topicName}</h3>
                          <div className="mt-1 flex flex-wrap items-center gap-2">
                            <span
                              className="rounded-full px-2 py-0.5 text-[11px] font-semibold"
                              style={{
                                backgroundColor: `${t.badgeColor}15`,
                                color: t.badgeColor,
                              }}
                            >
                              {t.gapLabel}
                            </span>
                            <span className="text-[11px] text-slate-400">
                              {t.questionsAttempted > 0
                                ? `${t.correctAnswers}/${t.questionsAttempted} correct`
                                : 'Unassessed'}
                            </span>
                          </div>
                        </div>
                        <span className="font-mono text-sm font-bold text-slate-700">
                          {t.accuracyPercentage}%
                        </span>
                      </div>
                      <ProgressBar value={t.accuracyPercentage} size="sm" className="mt-2" />
                    </div>
                  ))}
                </div>
              )}
            </div>
          </section>
        </>
      )}
    </div>
  );
}
