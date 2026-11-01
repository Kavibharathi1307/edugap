import React from 'react';
import { Link } from 'react-router-dom';
import { BrainCircuit, GraduationCap, ShieldCheck, Sparkles, Target } from 'lucide-react';

const features = [
  {
    icon: BrainCircuit,
    title: 'Deterministic gap detection',
    text: 'Topic-level diagnostics uncover exactly where understanding breaks down.',
  },
  {
    icon: Target,
    title: 'Personalized learning paths',
    text: 'Targeted notes, examples, and practice mapped to your weakest areas.',
  },
  {
    icon: Sparkles,
    title: 'Visual progress tracking',
    text: 'Track mastery over time with radar and trend analytics.',
  },
];

export default function AuthLayout({ title, subtitle, children, footer }) {
  return (
    <div className="grid min-h-[calc(100vh-4rem)] bg-[#F8FAFC] lg:grid-cols-2">
      {/* Brand panel */}
      <aside className="relative hidden overflow-hidden bg-[#0F172A] p-10 lg:flex lg:flex-col lg:justify-between xl:p-14">
        <div
          className="pointer-events-none absolute -right-24 top-1/4 h-80 w-80 rounded-full bg-primary-600/20 blur-3xl"
          aria-hidden="true"
        />
        <div
          className="pointer-events-none absolute -bottom-24 -left-16 h-72 w-72 rounded-full bg-primary-400/10 blur-3xl"
          aria-hidden="true"
        />

        <Link to="/" className="relative z-10 flex items-center gap-3">
          <span className="flex h-10 w-10 items-center justify-center rounded-xl bg-primary-600 text-white">
            <GraduationCap className="h-6 w-6" aria-hidden="true" />
          </span>
          <span>
            <span className="block text-xl font-bold leading-tight text-white">EduGap</span>
            <span className="block text-[10px] font-medium uppercase tracking-wider text-slate-400">
              Learning System
            </span>
          </span>
        </Link>

        <div className="relative z-10 max-w-lg py-12">
          <span className="inline-flex items-center gap-1.5 rounded-full bg-white/10 px-3 py-1 text-xs font-semibold text-primary-100 ring-1 ring-inset ring-white/15">
            <Sparkles className="h-3.5 w-3.5 text-amber-300" aria-hidden="true" />
            Smarter learning, powered by diagnostics
          </span>
          <h2 className="mt-5 text-3xl font-bold leading-tight tracking-tight text-white xl:text-4xl">
            Turn assessment data into a{' '}
            <span className="text-primary-300">personalized learning path.</span>
          </h2>
          <p className="mt-4 text-sm leading-relaxed text-slate-300">
            EduGap helps students identify knowledge gaps, focus study time, and measure real
            progress across every topic.
          </p>

          <div className="mt-10 space-y-5">
            {features.map((feature) => {
              const Icon = feature.icon;
              return (
                <div key={feature.title} className="flex items-start gap-3.5">
                  <span className="mt-0.5 flex h-9 w-9 shrink-0 items-center justify-center rounded-xl bg-white/5 text-primary-300 ring-1 ring-inset ring-white/10">
                    <Icon className="h-5 w-5" aria-hidden="true" />
                  </span>
                  <div>
                    <h3 className="text-sm font-semibold text-white">{feature.title}</h3>
                    <p className="mt-0.5 text-xs leading-relaxed text-slate-400">{feature.text}</p>
                  </div>
                </div>
              );
            })}
          </div>
        </div>

        <div className="relative z-10 flex items-center gap-2 text-xs text-slate-400">
          <ShieldCheck className="h-4 w-4 text-primary-300" aria-hidden="true" />
          Secure JWT authentication &bull; Your learning data stays private
        </div>
      </aside>

      {/* Form panel */}
      <div className="flex items-center justify-center px-4 py-10 sm:px-6 lg:px-10">
        <div className="w-full max-w-md">
          <Link to="/" className="mb-8 flex items-center justify-center gap-2.5 lg:hidden">
            <span className="flex h-9 w-9 items-center justify-center rounded-xl bg-primary-600 text-white">
              <GraduationCap className="h-5 w-5" aria-hidden="true" />
            </span>
            <span className="text-lg font-bold text-navy">EduGap</span>
          </Link>

          <div className="card p-6 sm:p-8">
            <div className="mb-6">
              <h1 className="text-2xl font-bold tracking-tight text-navy">{title}</h1>
              <p className="mt-1.5 text-sm leading-relaxed text-slate-500">{subtitle}</p>
            </div>
            {children}
          </div>

          {footer && <div className="mt-5 text-center text-xs text-slate-500">{footer}</div>}
        </div>
      </div>
    </div>
  );
}
