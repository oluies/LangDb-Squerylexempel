import sbt._

class SquerylDemoProject(info: ProjectInfo) extends DefaultProject(info)
{
  //lazy val hi = task { println("Hello World"); None }
  val h2 = "com.h2database" % "h2" % "1.2.141"
  val cglib = "cglib" % "cglib-nodep" % "2.2"
  //val squeryl = "org.squeryl" % "squeryl_2.8.0" % "0.9.4beta8"
  val specs = "org.scala-tools.testing" % "specs_2.8.0" % "1.6.5"
}
