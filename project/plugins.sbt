resolvers += Resolver.url("Typesafe repository", new java.net.URL("http://typesafe.artifactoryonline.com/typesafe/ivy-releases/"))(Resolver.defaultIvyPatterns)

resolvers += "Web plugin repo" at "http://siasia.github.com/maven2"

addSbtPlugin("com.github.siasia" %% "xsbt-web-plugin" % "0.1.2")

libraryDependencies += "org.eclipse.jetty" % "jetty-server" % "8.0.1.v20110908"