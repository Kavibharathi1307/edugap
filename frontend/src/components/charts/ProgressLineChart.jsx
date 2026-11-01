import React from 'react';
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
  Filler,
} from 'chart.js';
import { Line } from 'react-chartjs-2';

ChartJS.register(
  CategoryScale,
  LinearScale,
  PointElement,
  LineElement,
  Title,
  Tooltip,
  Legend,
  Filler
);

export default function ProgressLineChart({ labels = [], overallScores = [] }) {
  if (!labels || labels.length === 0 || !overallScores || overallScores.length === 0) {
    return (
      <div className="h-64 flex items-center justify-center text-slate-400 text-sm italic">
        Take at least one assessment to begin tracking your score progression.
      </div>
    );
  }

  const data = {
    labels,
    datasets: [
      {
        label: 'Overall Assessment Score (%)',
        data: overallScores,
        borderColor: '#0F766E',
        backgroundColor: 'rgba(15, 118, 110, 0.08)',
        fill: true,
        tension: 0.35,
        pointBackgroundColor: '#0F766E',
        pointBorderColor: '#fff',
        pointBorderWidth: 2,
        pointRadius: 5,
        pointHoverRadius: 7,
      },
      {
        label: 'Proficiency Line (75%)',
        data: labels.map(() => 75),
        borderColor: 'rgba(22, 163, 74, 0.5)',
        borderDash: [6, 6],
        pointRadius: 0,
        fill: false,
      },
    ],
  };

  const options = {
    responsive: true,
    maintainAspectRatio: false,
    scales: {
      y: {
        min: 0,
        max: 100,
        grid: {
          color: 'rgba(241, 245, 249, 1)',
        },
        ticks: {
          stepSize: 20,
          callback: (value) => `${value}%`,
          color: '#64748b',
          font: { family: 'Inter, sans-serif', size: 11 },
        },
      },
      x: {
        grid: {
          display: false,
        },
        ticks: {
          color: '#64748b',
          font: { family: 'Inter, sans-serif', size: 11 },
        },
      },
    },
    plugins: {
      legend: {
        position: 'top',
        align: 'end',
        labels: {
          boxWidth: 12,
          usePointStyle: true,
          pointStyle: 'circle',
          font: { family: 'Inter, sans-serif', size: 12 },
        },
      },
      tooltip: {
        callbacks: {
          label: (context) => ` ${context.dataset.label}: ${context.raw}%`,
        },
      },
    },
  };

  return (
    <div className="w-full h-72 sm:h-80">
      <Line data={data} options={options} />
    </div>
  );
}
