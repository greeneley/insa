module.exports = function(grunt) {

    // Project configuration.
    grunt.initConfig({
      pkg: grunt.file.readJSON('package.json'),
      sprite:{
        all: {
          src: 'img/*.png',
          dest: 'spritesheet.png',
          destCss: 'sprites.css'
        }
      }
    });
  
    // Load the plugins
    grunt.loadNpmTasks('grunt-spritesmith');
  
    // Default task(s)
    grunt.registerTask('default', ['sprite']);
  
  };