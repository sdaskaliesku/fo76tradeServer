const webpack = require('webpack');
const VueLoaderPlugin = require('vue-loader/lib/plugin');

module.exports = {
  entry: './src/main/ui/app.ts',
  mode: 'production',
  output: {
    path: __dirname,
    filename: './src/main/resources/static/built/bundle.js',
  },
  module: {
    rules: [
      {
        test: /\.vue$/,
        loader: 'vue-loader',
        options: {
          loaders: {
            'scss': 'vue-style-loader!css-loader!sass-loader',
          },
        },
      },
      {
        test: /\.tsx?$/,
        loader: 'ts-loader',
        exclude: /node_modules/,
        options: {
          appendTsSuffixTo: [/\.vue$/],
        },
      },
      {
        test: /\.(png)(\?|$)/,
        loader: 'url-loader?limit=100000'
      },
      {
        test: /\.(svg|ttf|woff|woff2|eot)$/,
        loader: 'url-loader',
        options: {
          name: '[name].[ext]?[hash]',
        },
      },
      // {
      //   test: /\.(png|jpg)$/,
      //   loader: 'url-loader'
      // },
      {
        test: /\.(sass|scss|css)$/i,
        use: [
          'vue-style-loader',
          'style-loader',
          'css-loader',
          'sass-loader',
        ],
      },
    ],
  },
  resolve: {
    extensions: ['.ts', '.js', '.vue', '.json'],
    alias: {
      'vue$': 'vue/dist/vue.esm.js',
    },
  },
  performance: {
    hints: false,
  },
  plugins: [
    new VueLoaderPlugin(),
    // new webpack.LoaderOptionsPlugin({
    //   minimize: true,
    // }),
  ],
};