package ohnosequences.pointless.tests

import shapeless.test.{typed, illTyped}
import ohnosequences.pointless._, representable._, property._, typeSet._, record._

object RecordTestsContext {

  case object id extends Property[Integer]
  case object name extends Property[String]

  case object simpleUser extends Record(id :~: name :~: ∅)

  // more properties:
  case object email extends Property[String]
  case object color extends Property[String]

  case object normalUser extends Record(id :~: name :~: email :~: color :~: ∅)

  case class HasRecordWithId[
    Id <: AnyProperty, 
    R <: AnyRecord
  ](
    val id: Id,
    val r: R
  )(implicit
    val idIsThere: Id ∈ PropertiesOf[R],
    val getId: Lookup[RawOf[R], RepOf[Id]]
  )
  {

    def getId(entry: RepOf[R]): RepOf[Id] = entry get id
  }

  val vProps = email :~: color :~: ∅
  // nothing works with this
  val vRecord = new Record(email :~: color :~: ∅)

  val vEmail = "oh@buh.com"

  val vRecordEntry = vRecord =>> (
    (email is vEmail) :~:
    (color is "blue") :~:
    ∅
  )

  val hasRecordWithId = new HasRecordWithId(id, normalUser)

  // creating a record instance is easy and neat:
  val simpleUserEntry = simpleUser fields (
    (id is 123) :~: 
    (name is "foo") :~: 
    ∅
  )

  // this way the order of properties does not matter
  val normalUserEntry = normalUser fields (
    (name is "foo") :~: 
    (color is "orange") :~:
    (id is 123) :~: 
    (email is "foo@bar.qux") :~:
    ∅
  )

}

class RecordTests extends org.scalatest.FunSuite {

  import RecordTestsContext._

  test("recognizing record value types") {

    implicitly [∅ isRepresentedBy ∅]

    implicitly [
      // using external bounds
      (id.type :~: name.type :~: ∅) isRepresentedBy (RepOf[id.type] :~: RepOf[name.type] :~: ∅)
    ]

    implicitly [
      // using the local Rep of each property
      (id.type :~: name.type :~: ∅) isRepresentedBy (id.Rep :~: name.Rep :~: ∅)
    ] 

    implicitly [ 
      simpleUser.Raw =:= (id.Rep :~: name.Rep :~: ∅)
    ]

    implicitly [ 
      // check the Values alias
      simpleUser.Raw =:= (id.Rep :~: name.Rep :~: ∅)
    ]

    implicitly [
      simpleUser.representedProperties.Out =:= (id.Rep :~: name.Rep :~: ∅)
    ]
  }

  test("can provide properties in different order") {

    implicitly [ 
      // the declared property order
      simpleUser.Raw =:= (id.Rep :~: name.Rep :~: ∅)
    ]

    // they get reordered
    val simpleUserV: simpleUser.Rep = simpleUser fields {
      (name is "Antonio") :~:
      (id is 29681) :~: ∅
    }

    val sameSimpleUserV: simpleUser.Rep = simpleUser fields {
      (id is 29681) :~:
      (name is "Antonio") :~: ∅
    }

    assert {
      simpleUserV === sameSimpleUserV
    }
  }

  test("should fail when some properties are missing") {
    // you have to set _all_ properties
    assertTypeError("""
    val wrongAttrSet = simpleUser is (
      (id is 123) :~: ∅
    )
    """)

    // but you still have to present all properties:
    assertTypeError("""
    val wrongAttrSet = normalUser fields (
      (id is 123) :~:
      (name is "foo") :~: 
      ∅
    )
    """)
  }

  test("can access property values") {

    assert {

      (simpleUserEntry get id) === 123
    }

    assert {

      (simpleUserEntry get name) === "foo"
    }
  }

  test("can access property values from vals and volatile vals") {

    assert {

      (vRecordEntry get email) === "oh@buh.com"
    }
  }

  test("generic ops outside record work") {

    val uhoh = simpleUserEntry get id
  }

  test("recognize entries coming from different paths") {

    assert {
      (hasRecordWithId getId normalUserEntry) == ( normalUserEntry get id)
    }
  }

  test("can see a record entry as another") {

    val hey: simpleUser.Rep = normalUserEntry as simpleUser
  }

}