# Map Transform #

Transform spherical and cylindrical objects to planar 2D and 3D maps using [ImageJ](http://imagej.net/).


## Building from source and installing to ImageJ ##

A build script for [Gradle](https://gradle.org/) is provided. Please consult the
[Gradle documentation](https://gradle.org/docs/current/userguide/tutorial_java_projects.html)
for build instructions and the
[ImageJ Wiki](http://imagejdocu.tudor.lu/doku.php?id=howto:plugins:how_to_install_a_plugin)
for instructions how to install plugins.

If ImageJ is installed to the 'Programs/ImageJ/' subfolder of your home folder ('/home/username/Programs/ImageJ/'
on Linux, and 'C:\Users\Username\Programs\ImageJ\' on Windows), you can automatically
build and install the plugin with the 'deployToImageJ' task. Please note that
this was never tested on MacOS.
