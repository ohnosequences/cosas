resolvers += "Era7 maven releases" at "http://releases.era7.com.s3.amazonaws.com"
resolvers += "Typesafe Repository" at "https://repo.typesafe.com/typesafe/releases/"

addSbtPlugin("ohnosequences" % "nice-sbt-settings" % "0.7.0-104-g945daa8")
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.3.3")
addSbtPlugin("com.codacy" % "sbt-codacy-coverage" % "1.2.1")
