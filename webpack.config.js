'use strict';

let precss       = require('precss');
let autoprefixer = require('autoprefixer');

let ExtractTextPlugin = require('extract-text-webpack-plugin');

module.exports = {
	context: __dirname + '/frontend',

	entry: ['bootstrap-loader', './styles'],

	output: {
		path: __dirname + '/public',
		filename: '[name].js'
	},

	resolve: {
		extensions: ['', '.js', '.scss']
	},

	module: {
		loaders: [{
			test: /\.scss$/,
			loader: ExtractTextPlugin.extract('style','css!postcss!sass')
		},{
			test: /\.js$/,
			loader: 'babel?presets[]=es2015'
		}]
	},

	postcss: function () {
		return [autoprefixer({ browsers: ['last 2 Chrome versions'] })];
	},

	plugins: [
		new ExtractTextPlugin('styles.css')
	]
};
