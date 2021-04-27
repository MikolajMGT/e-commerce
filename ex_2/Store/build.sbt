import play.sbt.PlayScala

name := "Store"

version := "1.0" 
      
lazy val `store` = (project in file(".")).enablePlugins(PlayScala)
      
resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"
resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

scalaVersion := "2.12.13"

libraryDependencies ++= Seq(
  ehcache , ws , specs2 % Test , guice,
  "com.typesafe.play" %% "play-slick" % "4.0.0",
  "com.typesafe.play" %% "play-slick-evolutions" % "4.0.0",
  "org.xerial"        %  "sqlite-jdbc" % "3.30.1"
)
