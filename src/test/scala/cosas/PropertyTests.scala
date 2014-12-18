package ohnosequences.cosas.test

import ohnosequences.cosas._, denotation._, property._, typeSet._, propertiesHolder._

object exampleProperties {
  
  case object key extends Property[String]("key")
  case object name extends Property[String]("name")
  case object age extends Property[Integer]("age")
  case object valueless extends Property("valueless")
}

class uhoh extends org.scalatest.FunSuite {
    
  import exampleProperties._

  test("create property instances") {

    val k: ValueOf[key.type] = key := "2aE5Cgo7Gv62"

    val n: ValueOf[name.type] = name := "Rigoberto Smith"

    val a = age := 13123
    val a0: ValueOf[age.type] = age := (13123: Integer)

    assertTypeError("""
      val z = key(34343)
    """)

    assertTypeError("""
      val uhoh = age(true)
    """)
  }

  test("valueless properties lead to nothing") {

    implicitly[valueless.Raw =:= Nothing]: Unit
  }

  test("having properties") {

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
