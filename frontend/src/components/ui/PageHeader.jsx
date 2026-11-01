import React from 'react';

export default function PageHeader({ eyebrow, title, description, actions, className = '' }) {
  return (
    <div
      className={`flex flex-col gap-4 border-b border-slate-200 pb-6 sm:flex-row sm:items-end sm:justify-between ${className}`}
    >
      <div className="min-w-0">
        {eyebrow && (
          <p className="mb-1.5 text-[11px] font-semibold uppercase tracking-wider text-primary-700">
            {eyebrow}
          </p>
        )}
        <h1 className="page-title">{title}</h1>
        {description && <p className="page-subtitle max-w-2xl">{description}</p>}
      </div>
      {actions && <div className="flex shrink-0 flex-wrap items-center gap-3">{actions}</div>}
    </div>
  );
}
