var path = require('path')

module.exports = {
  entry: {
    main: './src/index.js',
    redirect: './src/redirect.js'
  },
  output: {
    filename: '[name].js',
    path: path.resolve(__dirname, 'dist')
  },
  mode: 'development',
  devServer: {
	  proxy: {
		'/api': {
		  target: 'http://localhost:8000',
		  pathRewrite: {'^/api' : ''}
		}
	  }
  },
  module: {
    rules: [
      {
        test: /\.(js)$/,
        exclude: /node_modules/,
        use: {
          loader: 'babel-loader'
        }
      },
      {
          test: /\.css$/,
          use: [ 'style-loader', 'css-loader' ]
      }
    ]
  }
}
