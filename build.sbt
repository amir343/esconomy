

name := "esconomy"

organization := "Jayawy"

version := "1.0"

scalaVersion := "2.9.1"

seq(webSettings :_*)

resolvers += "Spring Maven MILESTONE Repository" at "http://maven.springframework.org/milestone"

libraryDependencies ++= Seq(
  "com.vaadin" % "vaadin" % "6.7.1",
  "javax.servlet" % "servlet-api" % "2.4",
  "org.mortbay.jetty" % "jetty" % "6.1.22" % "jetty",
  "org.eclipse.jetty" % "jetty-server" % "8.0.1.v20110908",
  "org.mongodb" % "mongo-java-driver" % "2.6",
  "org.springframework" % "spring-core" % "3.0.5.RELEASE",
  "org.springframework" % "spring-context" % "3.0.5.RELEASE",
  "org.springframework.data" % "spring-data-mongodb" % "1.0.0.RC1",
  "cglib" % "cglib" % "2.2"
)
