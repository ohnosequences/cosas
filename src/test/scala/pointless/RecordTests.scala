package ohnosequences.pointless.tests

import shapeless.test.{typed, illTyped}
import ohnosequences.pointless._, AnyTaggedType.Tagged, AnyProperty._, AnyTypeSet._, AnyRecord._, AnyTypeUnion._

object RecordTestsContext {

  case object id extends Property[Integer]
  case object name extends Property[String]
  case object notProperty

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
    val getId: Lookup[RawOf[R], Tagged[Id]]
  )
  {

    def getId(entry: Tagged[R]): Tagged[Id] = entry get id

    val p = r.properties
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

  test("record property bound works") {

    illTyped("""

      val uhoh = Record(id :~: name :~: notProperty :~: ∅)
    """)
  }

  test("recognizing record value types") {

    implicitly [∅ isRepresentedBy ∅]

    implicitly [
      // using external bounds
      (id.type :~: name.type :~: ∅) isRepresentedBy (Tagged[id.type] :~: Tagged[name.type] :~: ∅)
    ]

    // implicitly [
    //   // using the local Rep of each property
    //   (id.type :~: name.type :~: ∅) isRepresentedBy (id.Rep :~: name.Rep :~: ∅)
    // ] 

    implicitly [ 
      RawOf[simpleUser.type] =:= (Tagged[id.type] :~: Tagged[name.type] :~: ∅)
    ]

    implicitly [ 
      // check the Values alias
      simpleUser.Raw =:= (Tagged[id.type] :~: Tagged[name.type] :~: ∅)
    ]

    implicitly [
      simpleUser.representedProperties.Out =:= (Tagged[id.type] :~: Tagged[name.type] :~: ∅)
    ]
  }

  test("can provide properties in different order") {

    implicitly [ 
      // the declared property order
      simpleUser.Raw =:= (Tagged[id.type] :~: Tagged[name.type] :~: ∅)
    ]

    // they get reordered
    val simpleUserV: Tagged[simpleUser.type] = simpleUser fields {
      (name is "Antonio") :~:
      (id is 29681) :~: ∅
    }

    val sameSimpleUserV: Tagged[simpleUser.type] = simpleUser fields {
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

    val hey: Tagged[simpleUser.type] = normalUserEntry as simpleUser
  }

  test("having properties") {

    // just somtehing having properties
    object foo
    implicit val foo_props = foo has name :~: color :~: ∅

    implicitly[foo.type HasProperty name.type]
    implicitly[foo.type HasProperty color.type]

    implicitly[foo.type HasProperties ∅]
    implicitly[foo.type HasProperties (name.type :~: ∅)]
    implicitly[foo.type HasProperties (color.type :~: name.type :~: ∅)]

    // a record which has properties
    implicitly[simpleUser.type HasProperties (id.type :~: name.type :~: ∅)]
    implicitly[simpleUser.type HasProperties (name.type :~: id.type :~: ∅)]
    implicitly[simpleUser.type HasProperties (name.type :~: ∅)]
    implicitly[simpleUser.type HasProperties (id.type :~: ∅)]

    implicitly[simpleUser.type HasProperty name.type]
    implicitly[simpleUser.type HasProperty id.type]

  }

}
