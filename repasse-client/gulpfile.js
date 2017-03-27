var gulp = require('gulp');

require('es6-promise').polyfill(); 
// Include plugins
var plugins = require("gulp-load-plugins")({
	pattern: ['gulp-*', 'gulp.*', 'main-bower-files', 'browser-sync', 'jshint-stylish'],
	replaceString: /\bgulp[\-.]/
});


gulp.task('default', ['copy'], function() {
    gulp.start('build-img', 'usemin');
});

gulp.task('copy', ['clean'], function() {
    return gulp.src('app/**/*')
        .pipe(gulp.dest('dist'));
});

gulp.task('clean', function() {
    return gulp.src('dist')
        .pipe(plugins.clean());
});

gulp.task('build-img', function() {
  return gulp.src('dist/imagens/**/*')
    .pipe(plugins.imagemin())
    .pipe(gulp.dest('dist/imagens'));
});

gulp.task('usemin', function() {
  return gulp.src('dist/**/*.html')
    .pipe(plugins.usemin({
      js: [plugins.uglify],
      css: [plugins.autoprefixer,plugins.cssmin]
    }))
    .pipe(gulp.dest('dist'));
});

gulp.task('server', function() {
    plugins.browserSync.init({
        server: {
            baseDir: 'app'
        }
    });

    gulp.watch('app/**/*').on('change',  plugins.browserSync.reload);

    gulp.watch('app/js/**/*.js').on('change', function(event) {
        console.log("Linting " + event.path);
        gulp.src(event.path)
            .pipe(plugins.jshint())
            .pipe(plugins.jshint.reporter('jshint-stylish'));
    });

    gulp.watch('app/css/**/*.css').on('change', function(event) {
        console.log("Linting " + event.path);
        gulp.src(event.path)
            .pipe(plugins.csslint())
            .pipe(plugins.csslint.reporter());
    });

});
