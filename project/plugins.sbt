resolvers += Resolver.url("Typesafe repository", new java.net.URL("http://typesafe.artifactoryonline.com/typesafe/ivy-releases/"))(Resolver.defaultIvyPatterns)

resolvers += "Web plugin repo" at "http://siasia.github.com/maven2"

resolvers += "Arnauld" at "https://github.com/Arnauld/arnauld.github.com/raw/master/maven2"

libraryDependencies <+= sbtVersion(v => "com.github.siasia" %% "xsbt-web-plugin" % (v+"-0.2.10"))

libraryDependencies += "org.eclipse.jetty" % "jetty-server" % "8.0.1.v20110908"

libraryDependencies <+= sbtVersion(v => "org.technbolts" % "sbt-vaadin-plugin" % "0.0.2-SNAPSHOT")
