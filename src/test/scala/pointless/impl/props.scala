package ohnosequences.pointless.test

import ohnosequences.pointless.impl.property._

object exampleProperties {
  
  object key extends property[String]("key")
  object name extends property[String]("name")
  object age extends property[Integer]("age")
}

class uhoh extends org.scalatest.FunSuite {
    
  import exampleProperties._
  import shapeless.test.illTyped

  test("create property instances") {

    val k = key is "2aE5Cgo7Gv62"

    val n = name is "Rigoberto Smith"

    val a = age is 13123
    val a0 = age =>> (13123:  Integer)

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