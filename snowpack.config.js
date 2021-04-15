/** @type { import("snowpack").SnowpackUserConfig } */
module.exports = {
  extends: 'electron-snowpack/config/snowpack.js',
  plugins: ['@snowpack/plugin-react-refresh'],
};

// Base URL for icd web server: This is read in EventTreeData.tsx
process.env.SNOWPACK_PUBLIC_API_URL = 'http://localhost:9000';
