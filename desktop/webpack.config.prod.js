const path = require('path');
const nodeExternals = require('webpack-node-externals');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const CopyWebpackPlugin = require('copy-webpack-plugin');
const CspHtmlWebpackPlugin = require('csp-html-webpack-plugin');

const commonConfig = {
  mode: 'production',
  output: {
    path: path.resolve(__dirname, 'dist'),
    filename: '[name].js'
  },
  module: {
    rules: [
      {
        test: /\.(ts|tsx)$/,
        enforce: 'pre',
        use: [
          {
            options: {
              eslintPath: require.resolve('eslint'),
            },
            loader: require.resolve('eslint-loader'),
          },
        ],
        exclude: /node_modules/,
      },
      {
        test: /\.tsx?$/,
        use: ['babel-loader', 'ts-loader']
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
          template: './src/index-desktop.ejs'
        }),
        new CopyWebpackPlugin({
          patterns: [
            {
              from: './src/assets',
              to: 'assets',
              globOptions: {
                ignore: [
                  '**/config/**'
                ]
              }
            },
            {
              from: './src/assets/config/config.prod.json',
              to: 'assets/config/config.json'
            }
          ]
        }),
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
        template: './src/index-overlay.ejs'
      }),
      new CspHtmlWebpackPlugin()
    ]
  },
  commonConfig)
];
