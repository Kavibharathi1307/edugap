import React from 'react';

function toneForValue(value) {
  if (value >= 75) return 'bg-success';
  if (value >= 50) return 'bg-warning';
  return 'bg-danger';
}

export default function ProgressBar({
  value = 0,
  tone,
  label,
  showValue = false,
  size = 'md',
  className = '',
}) {
  const width = Math.max(0, Math.min(100, Number(value) || 0));
  const barColor = tone || toneForValue(width);
  const height = size === 'sm' ? 'h-1.5' : size === 'lg' ? 'h-3' : 'h-2';

  return (
    <div className={className}>
      {(label || showValue) && (
        <div className="mb-1.5 flex items-center justify-between gap-3">
          {label && <span className="text-xs font-medium text-slate-600">{label}</span>}
          {showValue && <span className="text-xs font-semibold text-slate-500">{width}%</span>}
        </div>
      )}
      <div
        className={`w-full overflow-hidden rounded-full bg-slate-100 ${height}`}
        role="progressbar"
        aria-valuenow={width}
        aria-valuemin={0}
        aria-valuemax={100}
        aria-label={label || 'Progress'}
      >
        <div
          className={`${height} rounded-full transition-all duration-300 ${barColor}`}
          style={{ width: `${width}%` }}
        />
      </div>
    </div>
  );
}
