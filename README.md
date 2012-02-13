Esconomy
==========

Esconomy is your little economy application that helps you keep track of your expenses. The following technologies are used in this application:

* [sbt] as the build tool
* [Vaadin] as the UI framework
* [MongoDB] as the database
* [Scalaz] as complementary functional library to Scala Standard

[sbt]: https://github.com/harrah/xsbt
[Vaadin]: http://vaadin.com/
[MongoDB]: http://www.mongodb.org/
[Scalaz]: https://github.com/scalaz/scalaz

How to build
------------

First try to run `sbt` and let him resolve the dependencies. Then run:

	> vaadin-compile-widgetset

After this stage, all the 3rd party Vaadin addons are compiled and you can start the Jetty web server:

	> container:start

Browse to `localhost:8080` and enjoy the free software!



License
-------

Copyright (C) 2012 [Amir Moulavi](http://amirmoulavi.com)

Distributed under the [Apache Software License](http://www.apache.org/licenses/LICENSE-2.0.html).
