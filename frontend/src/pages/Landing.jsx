import React from 'react';
import { Link } from 'react-router-dom';
import {
  ArrowRight,
  BarChart2,
  BookOpen,
  BrainCircuit,
  CheckCircle2,
  ShieldCheck,
  Sparkles,
  Target,
  Zap,
} from 'lucide-react';

const features = [
  {
    icon: BrainCircuit,
    title: 'Deterministic gap detection',
    text: 'Our evaluation engine breaks down performance across eight Java topics and flags priority gaps with clear thresholds.',
  },
  {
    icon: Target,
    title: 'Personalized learning paths',
    text: 'Receive remedial study notes, common pitfall analyses, and targeted practice activities tailored to your weak points.',
  },
  {
    icon: BarChart2,
    title: 'Visual growth analytics',
    text: 'Track assessment attempts with radar and trend charts to monitor mastery and verify knowledge retention.',
  },
];

const topics = [
  'Java Fundamentals',
  'Variables & Data Types',
  'Conditional Statements',
  'Loops & Iterations',
  'Arrays & Algorithms',
  'Strings & Pool',
  'Methods & Signatures',
  'Object-Oriented Programming',
];

export default function Landing() {
  return (
    <div className="bg-[#F8FAFC]">
      {/* Hero */}
      <section className="relative overflow-hidden border-b border-slate-200">
        <div
          className="pointer-events-none absolute inset-x-0 top-0 h-72 bg-gradient-to-b from-primary-50/80 to-transparent"
          aria-hidden="true"
        />
        <div className="relative mx-auto max-w-7xl px-4 py-16 sm:px-6 lg:px-8 lg:py-24">
          <div className="grid items-center gap-12 lg:grid-cols-2">
            <div>
              <span className="inline-flex items-center gap-2 rounded-full border border-primary-200 bg-white px-3.5 py-1.5 text-xs font-semibold uppercase tracking-wider text-primary-700 shadow-sm">
                <Sparkles className="h-3.5 w-3.5 text-primary-600" aria-hidden="true" />
                Intelligent educational platform
              </span>

              <h1 className="mt-6 text-4xl font-extrabold leading-[1.1] tracking-tight text-navy sm:text-5xl lg:text-6xl">
                Detect knowledge gaps.
                <br />
                <span className="text-primary-700">Master core Java faster.</span>
              </h1>

              <p className="mt-6 max-w-xl text-base leading-relaxed text-slate-600 sm:text-lg">
                An intelligent assessment and personalized learning system built for college
                students. Pinpoint exact sub-topic weaknesses, receive tailored study resources, and
                watch your skills grow.
              </p>

              <div className="mt-9 flex flex-col gap-3 sm:flex-row">
                <Link to="/register" className="btn-primary !px-6 !py-3 text-base">
                  Get started free
                  <ArrowRight className="h-4 w-4" aria-hidden="true" />
                </Link>
                <Link to="/login" className="btn-secondary !px-6 !py-3 text-base">
                  Student sign in
                </Link>
              </div>

              <div className="mt-6 inline-flex flex-wrap items-center gap-2 rounded-xl border border-amber-200 bg-amber-50 px-4 py-2 text-xs font-medium text-amber-800">
                <span>Demo account:</span>
                <code className="rounded bg-amber-100 px-1.5 py-0.5 font-mono">
                  student@edugap.ac.in
                </code>
                <span className="text-amber-500">/</span>
                <code className="rounded bg-amber-100 px-1.5 py-0.5 font-mono">Password@123</code>
              </div>
            </div>

            {/* Product preview */}
            <div className="relative mx-auto w-full max-w-lg lg:max-w-none">
              <div
                className="absolute -inset-4 rounded-3xl bg-gradient-to-tr from-primary-100 via-white to-primary-50 blur-2xl"
                aria-hidden="true"
              />
              <div className="relative overflow-hidden rounded-2xl border border-slate-200 bg-white shadow-card-hover">
                <div className="flex items-center justify-between border-b border-slate-100 bg-slate-50/80 px-5 py-3">
                  <div className="flex items-center gap-2">
                    <span className="h-2.5 w-2.5 rounded-full bg-red-400" />
                    <span className="h-2.5 w-2.5 rounded-full bg-amber-400" />
                    <span className="h-2.5 w-2.5 rounded-full bg-green-400" />
                  </div>
                  <span className="text-[10px] font-semibold uppercase tracking-wider text-slate-400">
                    Dashboard preview
                  </span>
                </div>

                <div className="space-y-4 p-5">
                  <div className="flex items-center justify-between">
                    <div>
                      <p className="text-sm font-bold text-navy">Welcome back, Student</p>
                      <p className="text-[11px] text-slate-500">Your learning snapshot</p>
                    </div>
                    <span className="badge-teal">
                      <Sparkles className="h-3 w-3" aria-hidden="true" />
                      Diagnostic ready
                    </span>
                  </div>

                  <div className="grid grid-cols-3 gap-3">
                    {[
                      { label: 'Mastery', value: '75%', tone: 'text-primary-700', bg: 'bg-primary-50' },
                      { label: 'Topics', value: '8', tone: 'text-navy', bg: 'bg-slate-50' },
                      { label: 'Gaps', value: '2', tone: 'text-danger', bg: 'bg-red-50' },
                    ].map((item) => (
                      <div key={item.label} className={`rounded-xl border border-slate-200 ${item.bg} p-3`}>
                        <p className="text-[10px] font-semibold uppercase tracking-wider text-slate-500">
                          {item.label}
                        </p>
                        <p className={`mt-1 text-lg font-bold ${item.tone}`}>{item.value}</p>
                      </div>
                    ))}
                  </div>

                  <div className="rounded-xl border border-slate-200 p-4">
                    <div className="mb-3 flex items-center justify-between text-[11px] font-semibold text-slate-600">
                      <span>Topic mastery</span>
                      <span className="text-primary-700">Target 75%</span>
                    </div>
                    <div className="space-y-2.5">
                      {[
                        { name: 'OOP', value: 88, tone: 'bg-success' },
                        { name: 'Arrays', value: 64, tone: 'bg-warning' },
                        { name: 'Loops', value: 42, tone: 'bg-danger' },
                      ].map((row) => (
                        <div key={row.name} className="flex items-center gap-3">
                          <span className="w-14 text-[11px] font-medium text-slate-500">{row.name}</span>
                          <div className="h-1.5 flex-1 overflow-hidden rounded-full bg-slate-100">
                            <div className={`h-1.5 rounded-full ${row.tone}`} style={{ width: `${row.value}%` }} />
                          </div>
                          <span className="w-8 text-right text-[10px] font-semibold text-slate-500">
                            {row.value}%
                          </span>
                        </div>
                      ))}
                    </div>
                  </div>

                  <div className="flex items-center gap-2 rounded-xl border border-primary-200 bg-primary-50 px-3.5 py-2.5 text-[11px] font-medium text-primary-800">
                    <CheckCircle2 className="h-4 w-4 shrink-0 text-primary-600" aria-hidden="true" />
                    Personalized recommendations updated after every assessment.
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* Value strip */}
      <section className="border-b border-slate-200 bg-white">
        <div className="mx-auto grid max-w-7xl grid-cols-1 divide-y divide-slate-200 px-4 sm:grid-cols-3 sm:divide-x sm:divide-y-0 sm:px-6 lg:px-8">
          {[
            { icon: BookOpen, title: '8 core Java topics', text: 'Structured curriculum coverage' },
            { icon: Zap, title: 'Instant gap reports', text: 'Diagnosis after every attempt' },
            { icon: ShieldCheck, title: 'Private by design', text: 'Secure JWT-protected accounts' },
          ].map((item) => {
            const Icon = item.icon;
            return (
              <div key={item.title} className="flex items-center gap-3 py-5 sm:justify-center">
                <span className="flex h-9 w-9 items-center justify-center rounded-xl bg-primary-100 text-primary-700">
                  <Icon className="h-4 w-4" aria-hidden="true" />
                </span>
                <div>
                  <p className="text-sm font-semibold text-navy">{item.title}</p>
                  <p className="text-[11px] text-slate-500">{item.text}</p>
                </div>
              </div>
            );
          })}
        </div>
      </section>

      {/* Features */}
      <section className="py-16 sm:py-20">
        <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
          <div className="mx-auto max-w-2xl text-center">
            <p className="text-xs font-semibold uppercase tracking-wider text-primary-700">
              Why EduGap
            </p>
            <h2 className="mt-2 text-2xl font-bold tracking-tight text-navy sm:text-3xl">
              How EduGap accelerates learning
            </h2>
            <p className="mt-3 text-sm leading-relaxed text-slate-600 sm:text-base">
              Unlike traditional quizzes that only show a total percentage, EduGap inspects
              topic-level understanding and turns it into action.
            </p>
          </div>

          <div className="mt-12 grid grid-cols-1 gap-6 md:grid-cols-3">
            {features.map((feature) => {
              const Icon = feature.icon;
              return (
                <div key={feature.title} className="card card-hover p-6 sm:p-8">
                  <div className="mb-5 flex h-11 w-11 items-center justify-center rounded-xl bg-primary-100 text-primary-700">
                    <Icon className="h-5 w-5" aria-hidden="true" />
                  </div>
                  <h3 className="text-base font-bold text-navy">{feature.title}</h3>
                  <p className="mt-2 text-sm leading-relaxed text-slate-600">{feature.text}</p>
                </div>
              );
            })}
          </div>
        </div>
      </section>

      {/* Curriculum */}
      <section className="border-y border-slate-200 bg-white py-16 sm:py-20">
        <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
          <div className="mx-auto max-w-2xl text-center">
            <p className="text-xs font-semibold uppercase tracking-wider text-primary-700">
              Curriculum
            </p>
            <h2 className="mt-2 text-2xl font-bold tracking-tight text-navy sm:text-3xl">
              Core Java topics covered
            </h2>
            <p className="mt-3 text-sm leading-relaxed text-slate-600">
              Master fundamental to advanced Java concepts required for academic excellence and
              interviews.
            </p>
          </div>

          <div className="mx-auto mt-10 grid max-w-4xl grid-cols-1 gap-3 sm:grid-cols-2 lg:grid-cols-4">
            {topics.map((topic) => (
              <div
                key={topic}
                className="flex items-center gap-3 rounded-xl border border-slate-200 bg-slate-50/60 px-4 py-3 transition-colors hover:border-primary-300 hover:bg-primary-50/40"
              >
                <span className="h-1.5 w-1.5 shrink-0 rounded-full bg-primary-600" />
                <span className="text-sm font-medium text-slate-700">{topic}</span>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* CTA */}
      <section className="py-16 sm:py-20">
        <div className="mx-auto max-w-7xl px-4 sm:px-6 lg:px-8">
          <div className="relative overflow-hidden rounded-2xl bg-[#0F172A] px-6 py-10 sm:px-10 sm:py-12">
            <div
              className="pointer-events-none absolute -right-16 -top-20 h-64 w-64 rounded-full bg-primary-600/20 blur-3xl"
              aria-hidden="true"
            />
            <div className="relative z-10 flex flex-col items-start justify-between gap-6 lg:flex-row lg:items-center">
              <div className="max-w-2xl">
                <h2 className="text-2xl font-bold tracking-tight text-white sm:text-3xl">
                  Ready to find your knowledge gaps?
                </h2>
                <p className="mt-2 text-sm leading-relaxed text-slate-300">
                  Create a free student account, take a short diagnostic assessment, and get your
                  personalized learning plan in minutes.
                </p>
              </div>
              <div className="flex flex-wrap gap-3">
                <Link
                  to="/register"
                  className="inline-flex items-center gap-2 rounded-xl bg-primary-600 px-5 py-2.5 text-sm font-semibold text-white transition-colors hover:bg-primary-700"
                >
                  Get started free
                  <ArrowRight className="h-4 w-4" aria-hidden="true" />
                </Link>
                <Link
                  to="/login"
                  className="inline-flex items-center gap-2 rounded-xl bg-white/10 px-5 py-2.5 text-sm font-medium text-white ring-1 ring-inset ring-white/20 transition-colors hover:bg-white/15"
                >
                  Sign in
                </Link>
              </div>
            </div>
          </div>
        </div>
      </section>
    </div>
  );
}
