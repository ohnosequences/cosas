package ohnosequences.cosas.tests

import ohnosequences.cosas._, types._, typeSets._, properties._, records._

object recordTestsContext {

  case object id    extends Property[Integer]("id")
  case object name  extends Property[String]("name")
  case object notProperty
  case object email extends Property[String]("email")
  case object color extends Property[String]("color")

  // funny square ins an option too
  case object simpleUser extends Record(id :&: name :&: □)
  case object normalUser extends Record(id :&: name :&: email :&: color :&: FNil)
  val vProps  = email :&: color :&: FNil
  val vRecord = new Record(vProps)
  val vEmail = "oh@buh.com"

  val vRecordEntry = vRecord (
    email(vEmail) :~:
    color("blue") :~:
    ∅
  )
  // creating a record instance is easy and neat:
  val simpleUserEntry = simpleUser (
    id(123)     :~:
    name("foo") :~:
    ∅
  )

  // this way the order of properties does not matter
  val normalUserEntry = normalUser (
    email("foo@bar.qux")  :~:
    id(123)               :~:
    color("orange")       :~:
    name("foo")           :~:
    ∅
  )
}


class RecordTests extends org.scalatest.FunSuite {

  import recordTestsContext._

  test("should fail when some properties are missing") {
    // you have to set _all_ properties
    assertTypeError("""
    val wrongAttrSet = simpleUser(id(123) :~: ∅)
    """)

    // but you still have to present all properties:
    assertTypeError("""
    val wrongAttrSet = normalUser(
      id(123)     :~:
      name("foo") :~:
      ∅
    )
    """)
  }

  test("can access fields") {

    assert { (simpleUserEntry get id)   === id(123)     }
    assert { (simpleUserEntry get name) === name("foo") }
  }

  test("can access fields from vals and volatile vals") {

    assert{ (vRecordEntry get email) === email("oh@buh.com") }
  }

  test("can update fields") {

    assert {

      ( normalUserEntry update color("albero") ) === normalUser (
        (normalUserEntry get id)    :~:
        (normalUserEntry get name)  :~:
        (normalUserEntry get email) :~:
        color("albero")             :~:
        ∅
      )
    }

    assert {

      ( normalUserEntry update name("bar") :~: id(321) :~: ∅ ) === normalUser (
          id(321)               :~:
          name("bar")           :~:
          email("foo@bar.qux")  :~:
          color("orange")       :~:
          ∅
        )
    }
  }

  test("can see a record entry as another") {

    assert { normalUserEntry === ( simpleUserEntry as (normalUser, email("foo@bar.qux") :~: color("orange") :~: ∅) ) }
  }

  test("can provide properties in different order") {

    // the declared property order
    implicitly [
      simpleUser.Raw =:= (ValueOf[id.type] :~: ValueOf[name.type] :~: ∅)
    ]

    // they get reordered
    val simpleUserV: ValueOf[simpleUser.type] = simpleUser {
      name("Antonio") :~:
      id(29681)       :~: ∅
    }

    val sameSimpleUserV: ValueOf[simpleUser.type] = simpleUser {
      id(29681)       :~:
      name("Antonio") :~: ∅
    }

    assert { simpleUserV == sameSimpleUserV }
  }

  test("can check if record has properties") {
    import ops.records._

    implicitly[simpleUser.Fields HasProperties (id.type :~: name.type :~: ∅)]
    implicitly[simpleUser.Fields HasProperties (name.type :~: id.type :~: ∅)]
    implicitly[simpleUser.Fields HasProperties (name.type :~: ∅)]
    implicitly[simpleUser.Fields HasProperties (id.type :~: ∅)]

    implicitly[simpleUser.Fields HasProperty name.type]
    implicitly[simpleUser.Fields HasProperty id.type]

    assertTypeError { """implicitly[simpleUser.Fields HasProperties (email.type :~: id.type :~: ∅)]""" }
    assertTypeError { """implicitly[simpleUser.Fields HasProperties (email.type :~: name.type :~: color.type :~: ∅)]""" }
  }

  test("can parse records from Maps") {

    val idVParser: String => Option[Integer] = str => {
        import scala.util.control.Exception._
        catching(classOf[NumberFormatException]) opt str.toInt
      }
    implicit val idParser = PropertyParser[id.type, String](id, id.label, idVParser)

    implicit val nameParser = PropertyParser[name.type, String](name, name.label, { str: String => Some(str) } )

    val simpleUserEntryMap =  Map(
      "id" -> "29681",
      "name" -> "Antonio"
    )
    val wrongKeyMap = Map(
      "idd" -> "29681",
      "name" -> "Antonio"
    )

    val notIntValueMap = Map(
      "name" -> "Antonio",
      "id" -> "twenty-two"
    )

    assert { ( simpleUser parseFrom simpleUserEntryMap ) === Right(simpleUser(id(29681) :~: name("Antonio") :~: ∅)) }
    assert { ( simpleUser parseFrom wrongKeyMap ) === Left(KeyNotFound(id)) }
    assert { ( simpleUser parseFrom notIntValueMap ) === Left(ErrorParsingValue(id,"twenty-two")) }
  }
}
