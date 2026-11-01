/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        primary: {
          50: '#F0FDFA',
          100: '#CCFBF1',
          200: '#99F6E4',
          300: '#5EEAD4',
          400: '#2DD4BF',
          500: '#14B8A6',
          600: '#0F766E',
          700: '#115E59',
          800: '#134E4A',
          900: '#0F3D3A',
        },
        navy: {
          DEFAULT: '#0F172A',
          900: '#0F172A',
          800: '#131E36',
          700: '#1E293B',
          600: '#334155',
        },
        surface: '#F8FAFC',
        card: '#FFFFFF',
        success: '#16A34A',
        warning: '#D97706',
        danger: '#DC2626',
        tealLight: '#CCFBF1',
        gapCritical: '#DC2626',
        gapModerate: '#D97706',
        gapGood: '#0F766E',
        gapStrong: '#16A34A',
      },
      fontFamily: {
        sans: ['Inter', 'ui-sans-serif', 'system-ui', '-apple-system', 'Segoe UI', 'sans-serif'],
      },
      boxShadow: {
        card: '0 1px 2px 0 rgba(15, 23, 42, 0.04), 0 1px 3px 0 rgba(15, 23, 42, 0.06)',
        'card-hover': '0 8px 24px -12px rgba(15, 23, 42, 0.18)',
      },
    },
  },
  plugins: [],
}
