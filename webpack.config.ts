import path from "path";
import webpack from "webpack";

const config: webpack.Configuration = {
  entry: "./src/main/ui/index.tsx",
  devtool: false,
  mode: 'development',
  module: {
    rules: [
      {
        test: /\.(ts|js)x?$/,
        exclude: /node_modules/,
        use: {
          loader: "babel-loader",
          options: {
            presets: [
              "@babel/preset-env",
              "@babel/preset-react",
              "@babel/preset-typescript",
            ],
          },
        },
      },
      {
        test: /\.tsx?$/,
        exclude: /node_modules/,
        use: {
          loader: 'ts-loader',
        }
      },
      {
        test: /\.(png)(\?|$)/,
        use: {
          loader: 'url-loader?limit=100000'
        }
      },
      {
        test: /\.(svg|ttf|woff|woff2|eot)$/,
        loader: 'url-loader',
        options: {
          name: '[name].[ext]?[hash]',
        },
      },
      {
        test: /\.(sass|scss|css)$/i,
        use: [
          'style-loader',
          'css-loader',
          'sass-loader',
        ],
      },
    ],
  },
  resolve: {
    extensions: [".tsx", ".ts", ".js"],
  },
  output: {
    path: path.join(__dirname, './src/main/resources/static/built/'),
    filename: 'bundle.js',
  },
  devServer: {
    contentBase: path.join(__dirname, './src/main/resources/static/built/'),
    compress: true,
    port: 4000,
  }
};

export default config;