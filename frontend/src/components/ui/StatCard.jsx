import React from 'react';

const tones = {
  teal: 'bg-primary-100 text-primary-700',
  green: 'bg-green-50 text-green-700',
  amber: 'bg-amber-50 text-amber-700',
  red: 'bg-red-50 text-red-700',
  slate: 'bg-slate-100 text-slate-600',
};

export default function StatCard({
  label,
  value,
  hint,
  icon: Icon,
  tone = 'teal',
  className = '',
}) {
  return (
    <div className={`card card-hover p-5 ${className}`}>
      <div className="mb-3 flex items-start justify-between gap-3">
        <span className="text-xs font-semibold uppercase tracking-wider text-slate-500">
          {label}
        </span>
        {Icon && (
          <span className={`flex h-9 w-9 items-center justify-center rounded-xl ${tones[tone] || tones.teal}`}>
            <Icon className="h-5 w-5" aria-hidden="true" />
          </span>
        )}
      </div>
      <div className="text-2xl font-bold tracking-tight text-navy sm:text-3xl">
        {value}
      </div>
      {hint && <p className="mt-1 text-xs text-slate-500">{hint}</p>}
    </div>
  );
}
