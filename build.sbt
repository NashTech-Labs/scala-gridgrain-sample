name := "scala-gridgrain-sample"

version := "1.0"

scalaVersion := "2.11.8"

resolvers += "GridGain External Repository" at "http://www.gridgainsystems.com/nexus/content/repositories/external"

libraryDependencies ++= Seq(
  "org.gridgain" % "gridgain-core" % "7.6.4",
  "org.apache.ignite" % "ignite-indexing" % "1.6.4",
  "org.apache.ignite" % "ignite-spring" % "1.6.4",
  "mysql" % "mysql-connector-java" % "5.1.34"
)

mergeStrategy in assembly := {
  case m if m.toLowerCase.endsWith("manifest.mf") => MergeStrategy.discard
  case m if m.toLowerCase.matches("meta-inf.*\\.sf$") => MergeStrategy.discard
  case "reference.conf" => MergeStrategy.concat
  case _ => MergeStrategy.first
}
