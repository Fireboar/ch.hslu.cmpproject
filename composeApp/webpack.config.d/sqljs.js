const CopyWebpackPlugin = require('copy-webpack-plugin');

module.exports = {
  resolve: {
    fallback: { fs: false, path: false, crypto: false }
  },
  plugins: [
    new CopyWebpackPlugin({
      patterns: [
        '../../node_modules/sql.js/dist/sql-wasm.wasm'
      ]
    })
  ]
};