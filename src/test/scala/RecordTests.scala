package ohnosequences.typesets.tests

import shapeless._
import shapeless.test.{typed, illTyped}
import ohnosequences.typesets._

import org.scalatest.FunSuite

import AnyTag._


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
    val idIsThere: Id ∈ AnyRecord.PropertiesOf[R],
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
    (id ->> 123) :~: 
    (name ->> "foo") :~: 
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

  test("shapeless witnesses work for properties") {

    val wid = implicitly[Witness.Aux[id.type]]
    typed[id.type](wid.value)
    typed[wid.T](id)
    implicitly[wid.T =:= id.type]
    implicitly[wid.value.Raw =:= Integer]
    assert(wid.value == id)
    
    val wname = implicitly[Witness.Aux[name.type]]


    val x = name ->> "foo"
    val y = implicitly[name.Rep => name.type]
    assert(y(x) == name)
  }

  test("recognizing record value types") {

    implicitly [

      ∅ isValuesOf ∅
    ]

    implicitly [

      // using external bounds
      (RepOf[id.type] :~: RepOf[name.type] :~: ∅) isValuesOf (id.type :~: name.type :~: ∅)
    ]

    implicitly [

      // using the local Rep of each property
      (id.Rep :~: name.Rep :~: ∅) isValuesOf (id.type :~: name.type :~: ∅)
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
    val wrongAttrSet = simpleUser ->> (
      (id ->> 123) :~: ∅
    )
    """)

    // but you still have to present all properties:
    assertTypeError("""
    val wrongAttrSet = normalUser fields (
      (id ->> 123) :~:
      (name ->> "foo") :~: 
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

    val uhoh = simpleUserEntry getIt id
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
