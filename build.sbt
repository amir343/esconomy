

name := "esconomy"

organization := "Jayway"

version := "1.0"

scalaVersion := "2.9.1"

seq((webSettings ++ vaadinSettings ++ Seq(
  port := 8081,
  vaadinWidgetSet := "widgetset",
  vaadinVersion := "6.7.4"
)) :_*)

resolvers ++= Seq(
                  "Scala Tools" at "http://scala-tools.org/repo-releases/",
                  "Spring Maven MILESTONE Repository" at "http://maven.springframework.org/milestone",
                  "Vaadin Addons" at "http://maven.vaadin.com/vaadin-addons"
                  )

libraryDependencies ++= Seq(
  "com.vaadin" % "vaadin" % "6.7.4",
  "org.vaadin.addons" % "autoreplacefield" % "1.0.1",
  "org.vaadin.addons" % "invient-charts" % "0.8.6",
  "org.vaadin.addons" % "detachedtabs" % "0.0.3",
  "javax.servlet" % "servlet-api" % "2.4",
  "org.eclipse.jetty" % "jetty-server" % "8.0.1.v20110908",
  "org.mongodb" % "mongo-java-driver" % "2.6",
  "org.springframework" % "spring-core" % "3.0.5.RELEASE",
  "org.springframework" % "spring-context" % "3.0.5.RELEASE",
  "org.springframework.data" % "spring-data-mongodb" % "1.0.0.RC1",
  "cglib" % "cglib" % "2.2",
  "org.mortbay.jetty" % "jetty" % "6.1.22" % "container",
  "commons-io" % "commons-io" % "2.1",
  "org.scalaz" %% "scalaz-core" % "6.0.4",
  "org.specs2" %% "specs2" % "1.7.1" % "test",
  "org.specs2" %% "specs2-scalaz-core" % "6.0.1" % "test",
  "junit" % "junit" % "4.7" % "test"
)
