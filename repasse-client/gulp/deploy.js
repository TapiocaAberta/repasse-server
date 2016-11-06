'use strict';

var gulp = require('gulp');
var $ = require('gulp-load-plugins')();

gulp.task('deploy', function() {
    return gulp.src('./dist/**/*')
        .pipe($.ghPages());
});