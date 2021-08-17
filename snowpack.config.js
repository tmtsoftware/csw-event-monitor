import { AppConfig } from './src/config/AppConfig.js'
export default {
  devOptions: {
    port: 9001
  },
  buildOptions: {
    clean: true,
    out: AppConfig.applicationName,
    sourcemap: 'inline'
  },
  testOptions: { files: ['**/test/**/*.*'] },
  mount: {
    public: '/',
    src: '/dist',
    test: '/dist_test'
  },
  plugins: [['@snowpack/plugin-typescript']],
  packageOptions: {
    polyfillNode: true,
    external: ['fs', 'os', 'path']
  },
  alias: {
    'io-ts/lib': 'io-ts/es6',
    'fp-ts/lib': 'fp-ts/es6'
  },
  routes: [{ match: 'routes', src: '.*', dest: '/index.html' }]
}

// Base URL for icd web server: This is read in EventTreeData.tsx
process.env.SNOWPACK_PUBLIC_API_URL = 'http://localhost:9000'

