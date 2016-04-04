'use strict';

const PROD = JSON.parse(process.env.PROD_ENV || '0');

let webpack      = require('webpack');
let precss       = require('precss');
let autoprefixer = require('autoprefixer');

let ExtractTextPlugin = require('extract-text-webpack-plugin');

module.exports = {
	context: __dirname + '/frontend',

	entry: {
		'style': ['bootstrap-loader', './styles'],
		'app-main': './applications/main'
	},

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
		return [precss, autoprefixer({ browsers: ['last 2 Chrome versions'] })];
	},

	plugins: [
		new ExtractTextPlugin('styles.css')
	]
};

if(PROD){
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
