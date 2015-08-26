package ohnosequences.cosas.test

import ohnosequences.cosas._, types._, typeSets._
import ohnosequences.cosas.ops.types._

class ParsingDenotations extends org.scalatest.FunSuite {

  case object buh extends Wrap[String]("buh")
  implicit val parseBuh = new DenotationParser(buh, buh.label)({ x: String => Some(x) })
  case object bah extends Wrap[String]("bah")
  implicit val parseBah = new DenotationParser(bah, bah.label)({ x: String => Some(x) })

  type BuhBah = ValueOf[buh.type] :~: ValueOf[bah.type] :~: ∅
  // val buhbah: BuhBah = buh :~: bah :~: ∅

  val map = Map(
    "buh" -> "hola buh!",
    "bah" -> "hola bah!"
  )

  implicitly[ParseDenotations[BuhBah,String]]
}
