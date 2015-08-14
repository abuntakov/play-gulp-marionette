/*global require */
var gulp = require('gulp');
var sass = require('gulp-sass');

gulp.task('styles', function(){
	gulp.src('../app/assets/stylesheets/**/*.scss')
		.pipe(sass().on('error', sass.logError))
		.pipe(gulp.dest('../public/stylesheets/'));
});

gulp.task('default', function(){
	
});
