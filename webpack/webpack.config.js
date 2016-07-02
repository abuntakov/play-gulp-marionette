'use strict';

require('babel-polyfill');

const PROD = JSON.parse(process.env.PROD_ENV || '0');

let webpack = require('webpack');
let path = require('path');
let precss = require('precss');
let autoprefixer = require('autoprefixer');
let assetsPath = path.resolve(__dirname, '../public/dist');
let contextPath = path.resolve(__dirname, '../frontend');

let ExtractTextPlugin = require('extract-text-webpack-plugin');

/**
'bootstrap-loader/extractStyles' - for extract css to single file
**/

module.exports = {
	context: contextPath,

	entry: [
		'webpack-dev-server/client?http://localhost:3000',
		'webpack/hot/only-dev-server',
		'bootstrap-loader/extractStyles',
		'./client'
	],

	output: {
		path: assetsPath,
		filename: '[name].js'
	},

	resolve: {
		extensions: ['', '.js', '.jsx', '.scss']
	},

	module: {
		loaders: [{
			test: /\.scss$/,
			loader: ExtractTextPlugin.extract('style', 'css!postcss!sass')
		}, {
			test: /\.js$/,
			include: [contextPath],
			loader: 'babel?presets[]=es2015'
		}, {
			test: /\.jsx$/,
			include: [contextPath],
			loaders: ['react-hot', 'babel'],
			plugins: ['transform-runtime']
		}]
	},

	postcss: function() {
		return [precss, autoprefixer({
			browsers: ['last 2 Chrome versions']
		})];
	},

	plugins: [
		new webpack.HotModuleReplacementPlugin(),
		new webpack.optimize.OccurenceOrderPlugin(),
		new webpack.NoErrorsPlugin(),
		new ExtractTextPlugin('theme.css', {
			allChunks: true
		})
	]
};

if (PROD) {
	module.exports.plugins.push(
		new webpack.optimize.UglifyJsPlugin({
			compress: {
				warnings: true,
				drop_console: true,
				unsafe: true
			}
		})
	);
}
