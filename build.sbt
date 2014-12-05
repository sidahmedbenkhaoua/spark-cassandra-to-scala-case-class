name := "Simple Case"

version := "0.0.1"

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
    "org.apache.spark" %% "spark-core" % "1.1.1",
    "org.apache.spark" %% "spark-sql" % "1.1.1",
    "com.datastax.spark" %% "spark-cassandra-connector" % "1.1.0" withSources() withJavadoc()
  )
  
