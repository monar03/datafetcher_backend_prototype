logLevel := Level.Warn

resolvers += "sbt-idea-repo" at "http://mpeltonen.github.com/maven/"

autoCompilerPlugins := true

//addCompilerPlugin("org.scala-lang.plugins" % "scala-continuations-plugin_2.11.0" % "1.0.1")

//scalacOptions += "-P:continuations:enable"

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.5.1")


