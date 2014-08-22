package ohnosequences.pointless.test

import ohnosequences.pointless._, taggedType._, property._

object exampleProperties {
  
  case object key extends Property[String]
  case object name extends Property[String]
  case object age extends Property[Integer]
}

class uhoh extends org.scalatest.FunSuite {
    
  import exampleProperties._
  import shapeless.test.illTyped

  test("create property instances") {

    val k = key is "2aE5Cgo7Gv62"

    val n = name is "Rigoberto Smith"

    val a = age is 13123
    val a0 = age =>> (13123: Integer)

    illTyped (
    """
      val z = key is 34343
    """
    )

    illTyped (
    """
      val uhoh = age is true
    """
    )
  }
}
