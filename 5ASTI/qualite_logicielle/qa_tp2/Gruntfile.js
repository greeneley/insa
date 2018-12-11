module.exports = function(grunt) {

    // Project configuration.
    grunt.initConfig({
        pkg: grunt.file.readJSON('package.json'),
        concat: {
            options: {
                stripBanners: true,
                separator: ';\n'
            },
            dist: {
                src: ['js/*.js', 'tests/*.js'],
                dest: 'dist/concat.js'
            }
        },
        uglify: {
            build: {
                src: 'dist/concat.js',
                dest: 'dist/concat.min.js'
            }
        },
        watch: {
            test: {
                files: ['js/*.js', 'tests/*.js'],
                tasks: ['default'],
                options: {
                    livereload:1337
                }
            }
        },
        qunit: {
            all: ['html/qunit_example.html']
        },
        qunit_junit: {
            options: {
                dest: 'dist/'
            }
        }
    });
  
    // Load the plugins
    grunt.loadNpmTasks('grunt-contrib-concat');
    grunt.loadNpmTasks('grunt-contrib-uglify');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-contrib-qunit');
    grunt.loadNpmTasks('grunt-qunit-junit');
  
    // Default task(s).
    grunt.registerTask('default', ['concat', 'uglify', 'qunit_junit', 'qunit']);
  };