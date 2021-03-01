name := "Multimode"

version := "0.1"
organization := "com.orange"
scalaVersion := "2.13.5"
// PROJECTS
val sparkcore = "org.apache.spark" %% "spark-core" % "2.4.6"
val sparksql = "org.apache.spark" %% "spark-sql" % "2.4.6"
val sparkmlib = "org.apache.spark" %% "spark-mllib" % "2.4.6"
val sparklog = "org.scala-sbt" %% "util-logging" % "1.3.0-M2"

// SETTINGS
lazy val global = project
  .in(file("."))
  .aggregate(
    SalesDistribution,
    Sales2013,
    Sales2013Refund,
    UserProduct
  )

lazy val SalesDistribution = project
  .settings(
    name := "SalesDistribution",
    libraryDependencies ++= Seq(sparkcore, sparklog, sparkmlib, sparksql)
  )
  .enablePlugins(AssemblyPlugin)

lazy val Sales2013 = project
  .settings(
    name := "Sales2013",
    libraryDependencies ++= Seq(sparkcore, sparklog, sparkmlib, sparksql)
  )
  .enablePlugins(AssemblyPlugin)



lazy val Sales2013Refund = project
  .settings(
    name := "Sales2013Refund",
    libraryDependencies ++= Seq(sparkcore, sparklog, sparkmlib, sparksql)
  )
  .enablePlugins(AssemblyPlugin)
lazy val UserProduct = project
  .settings(
    name := "UserProduct",
    libraryDependencies ++= Seq(sparkcore, sparklog, sparkmlib, sparksql)
  )
  .enablePlugins(AssemblyPlugin)