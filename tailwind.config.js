/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./src/**/*.{js,jsx,ts,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        'custom-green': '#5FDFB6',
      },
      fontFamily: {
        'montserrat': ['Montserrat', 'serif'],
        'press-start': ['"Press Start 2P"', 'serif'],
        'rock-salt': ['"Rock Salt"', 'serif'],
      },
    },
  },
  plugins: [],
}