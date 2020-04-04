const path = require('path');
const nodeExternals = require('webpack-node-externals');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const CopyWebpackPlugin = require('copy-webpack-plugin');
const CspHtmlWebpackPlugin = require('csp-html-webpack-plugin');

const commonConfig = {
  mode: 'development',
  output: {
    path: path.resolve(__dirname, 'dist'),
    filename: '[name].js'
  },
  module: {
    rules: [
      {
        test: /\.ts$/,
        enforce: 'pre',
        loader: 'tslint-loader',
        options: {
          typeCheck: true,
          emitErrors: true
        }
      },
      {
        test: /\.tsx?$/,
        loader: ['babel-loader', 'ts-loader']
      },
      {
        test: /\.jsx?$/,
        loader: 'babel-loader'
      }
    ]
  },
  resolve: {
    extensions: ['.js', '.ts', '.tsx', '.jsx', '.json']
  },
  node: {
    __dirname: false
  },
  externals: [nodeExternals({
    modulesFromFile: true
  })]
};

module.exports = [
  Object.assign({
      target: 'electron-main',
      entry: { main: './src/main.ts' }
    },
    commonConfig),
  Object.assign({
      target: 'electron-renderer',
      entry: { desktop: './src/desktop.tsx' },
      plugins: [
        new HtmlWebpackPlugin({
          title: 'Zephyr β',
          template: './src/index.ejs',
        }),
        new CopyWebpackPlugin([
          {
            from: './src/assets',
            to: 'assets',
            ignore: [
              'config/*'
            ]
          },
          {
            from: './src/assets/config/config.dev.json',
            to: 'assets/config/config.json'
          }
        ]),
        new CspHtmlWebpackPlugin()
      ]
    },
    commonConfig),
  Object.assign({
    target: 'electron-renderer',
    entry: { overlay: './src/overlay.tsx' },
    plugins: [
      new HtmlWebpackPlugin({
        title: 'Zephyr β',
        filename: 'overlay.html',
        template: './src/index.ejs',
      }),
      new CspHtmlWebpackPlugin()
    ]
  },
  commonConfig)
];
