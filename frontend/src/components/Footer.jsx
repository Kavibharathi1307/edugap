import React from 'react';

export default function Footer() {
  return (
    <footer className="border-t border-slate-200 bg-white py-5">
      <div className="mx-auto flex max-w-7xl flex-col items-center justify-between gap-2 px-4 text-center text-xs text-slate-500 sm:flex-row sm:px-6 sm:text-left lg:px-8">
        <p>
          <span className="font-semibold text-slate-700">EduGap</span> &bull; Intelligent Knowledge
          Gap Detection and Personalized Learning System
        </p>
        <p className="text-slate-400">
          React &bull; Spring Boot &bull; PostgreSQL
        </p>
      </div>
    </footer>
  );
}
