import AssemblyKeys._ 

assemblySettings

jarName in assembly := "CaseStudy.jar"

mergeStrategy in assembly := {
  case PathList("com", "datastax", "driver", "core", "Driver.properties") => MergeStrategy.last
  case PathList("META-INF", "maven", "org.slf4j", "slf4j-api", "pom.properties") => MergeStrategy.rename
  case PathList("META-INF", "maven", "org.slf4j", "slf4j-api", "pom.xml") => MergeStrategy.rename
  case PathList("com", "esotericsoftware", "minlog", "Log$Logger.class") => MergeStrategy.first
  case PathList("com", "esotericsoftware", "minlog", "Log.class") => MergeStrategy.first
  case PathList("org", "apache", "commons", "beanutils", xx @ _*) => MergeStrategy.first
  case PathList("org", "apache", "commons", "collections", xy @ _*) => MergeStrategy.first
  case PathList("org", "apache", "commons", "logging", xz @ _*) => MergeStrategy.first
  case PathList("plugin.properties") => MergeStrategy.discard
  case PathList("META-INF", "mailcap") => MergeStrategy.discard
  case PathList("META-INF", "ECLIPSEF.RSA") => MergeStrategy.discard
  case x => {
    val oldStrategy = (mergeStrategy in assembly).value
    oldStrategy(x)
  }
}
