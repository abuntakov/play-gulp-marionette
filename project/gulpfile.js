/*global require */
var gulp = require('gulp');
var sass = require('gulp-sass');

gulp.task('styles', function(){
	gulp.src('../app/assets/stylesheets/**/*.scss')
		.pipe(sass().on('error', sass.logError))
		.pipe(gulp.dest('../public/stylesheets/'));
});

gulp.task('watch', function(){
	gulp.watch('../app/assets/stylesheets/**/*.scss', ['styles']);
});

gulp.task('default', function(){
	
});
