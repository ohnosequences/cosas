package ohnosequences.cosas.test

import ohnosequences.cosas._, AnyWrap._, AnyProperty._, AnyTypeSet._

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

    val k = key("2aE5Cgo7Gv62")

    val n = name("Rigoberto Smith")

    val a = age(13123)
    val a0: ValueOf[age.type] = age(13123: Integer)

    illTyped (
    """
      val z = key(34343)
    """
    )

    illTyped (
    """
      val uhoh = age(true)
    """
    )
  }

  test("valueless properties lead to nothing") {

    implicitly[RawOf[valueless.type] =:= Nothing]: Unit
  }

  test("having properties") {
    import AnyPropertiesHolder._

    object foo
    implicit val foo_name = foo has name
    implicit val foo_age = foo has age

    implicitly[foo.type HasProperty name.type]
    implicitly[foo.type HasProperty age.type]

    implicitly[foo.type HasProperties ∅]
    implicitly[foo.type HasProperties (name.type :~: ∅)]
    implicitly[foo.type HasProperties (age.type :~: name.type :~: ∅)]

    implicit val foo_key = foo has key

    implicitly[foo.type HasProperty key.type]
    implicitly[foo.type HasProperties (key.type :~: ∅)]
    implicitly[foo.type HasProperties (age.type :~: key.type :~: ∅)]
    implicitly[foo.type HasProperties (age.type :~: key.type :~: name.type :~: ∅)]
  }

}
