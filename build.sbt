

name := "esconomy"

organization := "Jayawy"

version := "1.0"

scalaVersion := "2.9.1"

seq(webSettings :_*)

libraryDependencies ++= Seq(
  "com.vaadin" % "vaadin" % "6.7.1",
  "javax.servlet" % "servlet-api" % "2.4",
  "org.mortbay.jetty" % "jetty" % "6.1.22" % "jetty",
  "org.eclipse.jetty" % "jetty-server" % "8.0.1.v20110908"
)
