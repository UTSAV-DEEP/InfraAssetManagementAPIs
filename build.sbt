
name := """InfraAssetManagementAPIs"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayEbean, PlayJava, PlayEnhancer)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "junit"             % "junit"           % "4.12"  % "test",
  "mysql" % "mysql-connector-java" % "5.1.36",
  "com.novocode"      % "junit-interface" % "0.11"  % "test",
  "org.slf4j" % "slf4j-api" % "1.7.25",
  "org.slf4j" % "slf4j-simple" % "1.7.25"
)
