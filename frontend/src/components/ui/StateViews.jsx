import React from 'react';
import { AlertCircle, Inbox, Loader2, RefreshCw } from 'lucide-react';

export function LoadingState({ message = 'Loading...', className = '' }) {
  return (
    <div className={`flex min-h-[40vh] flex-col items-center justify-center gap-3 ${className}`}>
      <Loader2 className="h-8 w-8 animate-spin text-primary-600" aria-hidden="true" />
      <p className="text-sm font-medium text-slate-500">{message}</p>
      <span className="sr-only" role="status">{message}</span>
    </div>
  );
}

export function ErrorState({
  title = 'Something went wrong',
  message = 'We could not load this data. Please try again.',
  onRetry,
  className = '',
}) {
  return (
    <div
      role="alert"
      className={`card flex flex-col items-center justify-center gap-3 p-8 text-center ${className}`}
    >
      <span className="flex h-11 w-11 items-center justify-center rounded-xl bg-red-50 text-danger">
        <AlertCircle className="h-5 w-5" aria-hidden="true" />
      </span>
      <div>
        <h3 className="text-sm font-semibold text-navy">{title}</h3>
        <p className="mt-1 max-w-md text-xs leading-relaxed text-slate-500">{message}</p>
      </div>
      {onRetry && (
        <button type="button" onClick={onRetry} className="btn-secondary mt-1">
          <RefreshCw className="h-4 w-4" aria-hidden="true" />
          Try again
        </button>
      )}
    </div>
  );
}

export function EmptyState({
  icon: Icon = Inbox,
  title = 'Nothing here yet',
  message = 'There is no data to display right now.',
  action,
  className = '',
}) {
  return (
    <div className={`flex flex-col items-center justify-center gap-3 p-8 text-center ${className}`}>
      <span className="flex h-12 w-12 items-center justify-center rounded-xl bg-primary-100 text-primary-700">
        <Icon className="h-6 w-6" aria-hidden="true" />
      </span>
      <div>
        <h3 className="text-sm font-semibold text-navy">{title}</h3>
        <p className="mt-1 max-w-sm text-xs leading-relaxed text-slate-500">{message}</p>
      </div>
      {action}
    </div>
  );
}
