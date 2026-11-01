import React, { useState, useEffect } from 'react';
import { useSearchParams, useNavigate } from 'react-router-dom';
import api from '../api/client';
import { LoadingState } from '../components/ui/StateViews';
import {
  Clock,
  CheckSquare,
  ArrowRight,
  ArrowLeft,
  Send,
} from 'lucide-react';

export default function QuizPlay() {
  const [searchParams] = useSearchParams();
  const topicId = searchParams.get('topicId');
  const subjectId = searchParams.get('subjectId');
  const navigate = useNavigate();

  const [questions, setQuestions] = useState([]);
  const [currentIndex, setCurrentIndex] = useState(0);
  const [selectedAnswers, setSelectedAnswers] = useState({});
  const [loading, setLoading] = useState(true);
  const [submitting, setSubmitting] = useState(false);
  const [error, setError] = useState('');
  const [timeLeft, setTimeLeft] = useState(600);

  useEffect(() => {
    fetchQuestions();
  }, [topicId, subjectId]);

  // Countdown timer
  useEffect(() => {
    if (loading || questions.length === 0) return;
    const timer = setInterval(() => {
      setTimeLeft((prev) => {
        if (prev <= 1) {
          clearInterval(timer);
          handleSubmitQuiz();
          return 0;
        }
        return prev - 1;
      });
    }, 1000);
    return () => clearInterval(timer);
  }, [loading, questions, selectedAnswers]);

  const fetchQuestions = async () => {
    try {
      let url;
      if (topicId) {
        url = `/quizzes/questions?topicId=${topicId}&limit=10`;
        if (subjectId) url += `&subjectId=${subjectId}`;
      } else if (subjectId) {
        url = `/quizzes/questions?subjectId=${subjectId}&limit=10`;
      } else {
        setError('Choose a subject before starting an assessment.');
        setLoading(false);
        return;
      }
      const res = await api.get(url);
      if (res.data?.data && res.data.data.length > 0) {
        setQuestions(res.data.data);
      } else {
        setError('No questions available for this assessment.');
      }
    } catch (err) {
      console.error('Failed to load questions', err);
      setError('Failed to fetch assessment questions. Please try again.');
    } finally {
      setLoading(false);
    }
  };

  const handleSelectOption = (questionId, optionKey) => {
    setSelectedAnswers((prev) => ({
      ...prev,
      [questionId]: optionKey,
    }));
  };

  const handleSubmitQuiz = async () => {
    if (submitting) return;
    setSubmitting(true);
    setError('');

    try {
      const answersPayload = questions.map((q) => ({
        questionId: q.id,
        selectedOptionKey: selectedAnswers[q.id] || null,
      }));

      const payload = {
        subjectId: subjectId ? Number(subjectId) : questions[0]?.subjectId,
        subjectName:
          questions[0]?.subjectName ||
          (topicId ? `${questions[0]?.topicName || 'Topic'} Assessment` : 'Subject Assessment'),
        answers: answersPayload,
      };

      const res = await api.post('/quizzes/submit', payload);
      if (res.data?.data) {
        navigate(`/quizzes/result/${res.data.data.attemptId}`, {
          state: { result: res.data.data },
        });
      }
    } catch (err) {
      console.error('Quiz submission error', err);
      setError(err.response?.data?.message || 'Submission failed. Please try again.');
      setSubmitting(false);
    }
  };

  const formatTime = (secs) => {
    const mins = Math.floor(secs / 60);
    const s = secs % 60;
    return `${mins}:${s < 10 ? '0' : ''}${s}`;
  };

  if (loading) {
    return (
      <div className="mx-auto max-w-3xl px-4 sm:px-6">
        <LoadingState message="Preparing assessment questions..." />
      </div>
    );
  }

  if (error && questions.length === 0) {
    return (
      <div className="mx-auto max-w-md px-4 py-16 text-center">
        <div className="card p-8">
          <div role="alert" className="mb-4 rounded-xl border border-red-200 bg-red-50 p-4 text-sm text-red-700">
            {error}
          </div>
          <div className="flex flex-wrap justify-center gap-2">
            {subjectId && (
              <button
                type="button"
                onClick={() => navigate(`/subjects/${subjectId}`)}
                className="btn-secondary"
              >
                Back to subject
              </button>
            )}
            <button
              type="button"
              onClick={() => navigate('/subjects')}
              className="btn-primary"
            >
              Choose a subject
            </button>
          </div>
        </div>
      </div>
    );
  }

  const currentQ = questions[currentIndex];
  const progressPercent = Math.round(((currentIndex + 1) / questions.length) * 100);
  const answeredCount = Object.keys(selectedAnswers).length;
  const isLowTime = timeLeft <= 60;

  return (
    <div className="mx-auto max-w-3xl space-y-5 px-4 py-6 sm:px-6 sm:py-8">
      {/* Status Bar */}
      <div className="card flex flex-wrap items-center justify-between gap-3 p-4 sm:p-5">
        <div>
          <span className="text-[11px] font-bold uppercase tracking-wider text-primary-700">
            {currentQ?.topicName}
          </span>
          <div className="text-sm font-bold text-navy">
            Question {currentIndex + 1} of {questions.length}
          </div>
        </div>

        <div className="flex items-center gap-2.5">
          <div
            className={`flex items-center gap-1.5 rounded-lg px-3 py-1.5 font-mono text-sm font-semibold ${
              isLowTime ? 'bg-red-50 text-red-700' : 'bg-slate-100 text-slate-700'
            }`}
          >
            <Clock className="h-4 w-4" aria-hidden="true" />
            {formatTime(timeLeft)}
          </div>
          <button
            type="button"
            onClick={handleSubmitQuiz}
            disabled={submitting}
            className="hidden items-center gap-1.5 rounded-lg bg-primary-600 px-3.5 py-2 text-xs font-semibold text-white transition-colors hover:bg-primary-700 disabled:opacity-60 sm:inline-flex"
          >
            <Send className="h-3.5 w-3.5" aria-hidden="true" />
            {submitting ? 'Submitting...' : 'Finish & submit'}
          </button>
        </div>
      </div>

      {/* Progress */}
      <div>
        <div className="mb-1.5 flex items-center justify-between text-xs text-slate-500">
          <span>Assessment progress</span>
          <span className="font-semibold text-slate-600">{progressPercent}%</span>
        </div>
        <div className="h-2 w-full overflow-hidden rounded-full bg-slate-200">
          <div
            className="h-2 rounded-full bg-primary-600 transition-all duration-300"
            style={{ width: `${progressPercent}%` }}
          />
        </div>
      </div>

      {error && (
        <div role="alert" className="rounded-xl border border-red-200 bg-red-50 p-3.5 text-sm text-red-700">
          {error}
        </div>
      )}

      {/* Question Card */}
      <div className="card space-y-6 p-6 sm:p-8">
        <div className="flex items-center justify-between text-xs text-slate-400">
          <span className="font-semibold uppercase tracking-wider text-slate-500">
            Difficulty: {currentQ?.difficultyLevel}
          </span>
          <span>
            {answeredCount} / {questions.length} answered
          </span>
        </div>

        <h2 className="text-lg font-bold leading-snug text-navy sm:text-xl">
          {currentQ?.questionText}
        </h2>

        {/* Options */}
        <div className="space-y-3 pt-1">
          {currentQ?.options?.map((opt) => {
            const isSelected = selectedAnswers[currentQ.id] === opt.optionKey;
            return (
              <button
                key={opt.id}
                type="button"
                onClick={() => handleSelectOption(currentQ.id, opt.optionKey)}
                aria-pressed={isSelected}
                className={`flex w-full cursor-pointer items-start gap-3.5 rounded-xl border p-4 text-left transition-all ${
                  isSelected
                    ? 'border-primary-600 bg-primary-50/80 text-primary-900 ring-1 ring-primary-600'
                    : 'border-slate-200 text-slate-800 hover:border-slate-300 hover:bg-slate-50'
                }`}
              >
                <div
                  className={`flex h-6 w-6 shrink-0 items-center justify-center rounded-lg text-xs font-bold transition-colors ${
                    isSelected ? 'bg-primary-600 text-white' : 'bg-slate-100 text-slate-600'
                  }`}
                >
                  {opt.optionKey}
                </div>
                <span className="pt-0.5 text-sm font-medium leading-relaxed">
                  {opt.optionText}
                </span>
              </button>
            );
          })}
        </div>

        {/* Navigation Controls */}
        <div className="flex items-center justify-between border-t border-slate-100 pt-6">
          <button
            type="button"
            onClick={() => setCurrentIndex((prev) => Math.max(0, prev - 1))}
            disabled={currentIndex === 0}
            className="inline-flex items-center gap-1.5 rounded-xl bg-slate-100 px-4 py-2 text-xs font-semibold text-slate-700 transition-colors hover:bg-slate-200 disabled:cursor-not-allowed disabled:opacity-40"
          >
            <ArrowLeft className="h-3.5 w-3.5" aria-hidden="true" />
            Previous
          </button>

          {currentIndex < questions.length - 1 ? (
            <button
              type="button"
              onClick={() => setCurrentIndex((prev) => Math.min(questions.length - 1, prev + 1))}
              className="btn-primary !px-5 !py-2.5 !text-xs"
            >
              Next
              <ArrowRight className="h-3.5 w-3.5" aria-hidden="true" />
            </button>
          ) : (
            <button
              type="button"
              onClick={handleSubmitQuiz}
              disabled={submitting}
              className="btn-primary !px-6 !py-2.5 !text-xs"
            >
              <CheckSquare className="h-4 w-4" aria-hidden="true" />
              {submitting ? 'Evaluating answers...' : 'Submit assessment'}
            </button>
          )}
        </div>
      </div>

      {/* Question Quick Jump */}
      <div className="card flex flex-wrap items-center gap-2 p-4">
        <span className="mr-2 text-xs font-medium text-slate-500">Questions:</span>
        {questions.map((q, idx) => {
          const isAnswered = !!selectedAnswers[q.id];
          const isCurrent = idx === currentIndex;
          return (
            <button
              key={q.id}
              type="button"
              onClick={() => setCurrentIndex(idx)}
              aria-label={`Go to question ${idx + 1}${isAnswered ? ', answered' : ''}`}
              aria-current={isCurrent ? 'step' : undefined}
              className={`flex h-7 w-7 items-center justify-center rounded-lg text-xs font-semibold transition-all ${
                isCurrent
                  ? 'bg-primary-100 text-primary-800 ring-2 ring-primary-600'
                  : isAnswered
                  ? 'bg-green-100 text-green-800'
                  : 'bg-slate-100 text-slate-600 hover:bg-slate-200'
              }`}
            >
              {idx + 1}
            </button>
          );
        })}
      </div>
    </div>
  );
}
