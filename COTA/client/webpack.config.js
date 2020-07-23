const path = require("path");
const HtmlWebpackPlugin = require('html-webpack-plugin');

module.exports = {

    mode: 'development',
    entry: "./src/js/index.js",
    output: {
        filename: "main.js",
        path: path.resolve(__dirname,"dist"),
    },

    plugins: [
        new HtmlWebpackPlugin({
            title: 'COTa',
            favicon: "./src/images/old-woman.png"
        })
    ],

    module: {
        rules: [
            {
                test: /\.css$/i,
                use: ['style-loader', 'css-loader']
            },
            {
                test: /\.(png|gif|jpe?g|ico)$/i,
                use: ['file-loader']
            },
            { test: /\.woff(2)?(\?v=[0-9]\.[0-9]\.[0-9])?$/, loader: "url-loader" },
            { test: /\.(ttf|eot|svg)(\?v=[0-9]\.[0-9]\.[0-9])?$/, loader: "url-loader" },
        ],

    }
}
