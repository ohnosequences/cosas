package ohnosequences.pointless.test

import ohnosequences.pointless._, AnyTaggedType._, AnyProperty._, AnyTypeSet._

object exampleProperties {
  
  case object key extends Property[String]
  case object name extends Property[String]
  case object age extends Property[Integer]
  case object valueless extends Property
}

class uhoh extends org.scalatest.FunSuite {
    
  import exampleProperties._
  import shapeless.test.illTyped

  test("create property instances") {

    val k = key is "2aE5Cgo7Gv62"

    val n = name is "Rigoberto Smith"

    val a = age is 13123
    val a0: Tagged[age.type] = age =>> (13123: Integer)

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

  test("valueless properties lead to nothing") {

    implicitly[AnyTaggedType.RawOf[valueless.type] =:= Nothing]: Unit
  }

  test("having properties") {
    object foo
    implicit val foo_props = foo has name :~: age :~: ∅

    implicitly[foo.type HasProperty name.type]
    implicitly[foo.type HasProperty age.type]

    implicitly[foo.type HasProperties ∅]
    implicitly[foo.type HasProperties (name.type :~: ∅)]
    implicitly[foo.type HasProperties (age.type :~: name.type :~: ∅)]
  }

}
