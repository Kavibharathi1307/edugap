import React from 'react';
import {
  Chart as ChartJS,
  RadialLinearScale,
  PointElement,
  LineElement,
  Filler,
  Tooltip,
  Legend,
} from 'chart.js';
import { Radar } from 'react-chartjs-2';

ChartJS.register(
  RadialLinearScale,
  PointElement,
  LineElement,
  Filler,
  Tooltip,
  Legend
);

export default function TopicRadarChart({ topicPerformances }) {
  if (!topicPerformances || topicPerformances.length === 0) {
    return (
      <div className="h-64 flex items-center justify-center text-slate-400 text-sm italic">
        No assessment data yet. Complete a quiz to view your topic mastery radar.
      </div>
    );
  }

  const labels = topicPerformances.map((t) => t.topicName);
  const dataValues = topicPerformances.map((t) => t.accuracyPercentage);

  const data = {
    labels,
    datasets: [
      {
        label: 'Topic Mastery (%)',
        data: dataValues,
        backgroundColor: 'rgba(15, 118, 110, 0.18)',
        borderColor: '#0F766E',
        borderWidth: 2,
        pointBackgroundColor: '#0F766E',
        pointBorderColor: '#fff',
        pointHoverBackgroundColor: '#fff',
        pointHoverBorderColor: '#0F766E',
        pointRadius: 4,
      },
      {
        label: 'Proficiency Benchmark (75%)',
        data: labels.map(() => 75),
        borderColor: 'rgba(22, 163, 74, 0.4)',
        borderWidth: 1.5,
        borderDash: [5, 5],
        pointRadius: 0,
        fill: false,
      },
    ],
  };

  const options = {
    responsive: true,
    maintainAspectRatio: false,
    scales: {
      r: {
        angleLines: {
          color: 'rgba(226, 232, 240, 0.8)',
        },
        grid: {
          color: 'rgba(226, 232, 240, 0.8)',
        },
        pointLabels: {
          font: {
            size: 11,
            family: 'Inter, sans-serif',
            weight: '500',
          },
          color: '#475569',
        },
        suggestedMin: 0,
        suggestedMax: 100,
        ticks: {
          stepSize: 20,
          backdropColor: 'transparent',
          color: '#94a3b8',
          font: {
            size: 10,
          },
        },
      },
    },
    plugins: {
      legend: {
        position: 'bottom',
        labels: {
          boxWidth: 12,
          padding: 16,
          font: {
            family: 'Inter, sans-serif',
            size: 12,
          },
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
    <div className="w-full h-80 sm:h-96">
      <Radar data={data} options={options} />
    </div>
  );
}
