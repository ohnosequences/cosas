package ohnosequences.cosas.tests

import ohnosequences.cosas._, types._, typeSets._, properties._, records._

case object recordTestsContext {

  case object id    extends Property[Integer]("id")
  case object name  extends Property[String]("name")
  case object notProperty
  case object email extends Property[String]("email")
  case object color extends Property[String]("color")

  // funny square ins an option too
  case object simpleUser extends Record(id :&: name :&: □)
  case object normalUser extends Record(id :&: name :&: email :&: color :&: □)
  val vProps  = email :&: color :&: □
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

    implicitly[simpleUser.PropertySet HasProperties (id.type :~: name.type :~: ∅)]
    implicitly[simpleUser.PropertySet HasProperties (name.type :~: id.type :~: ∅)]
    implicitly[simpleUser.PropertySet HasProperties (name.type :~: ∅)]
    implicitly[simpleUser.PropertySet HasProperties (id.type :~: ∅)]

    implicitly[simpleUser.PropertySet HasProperty name.type]
    implicitly[simpleUser.PropertySet HasProperty id.type]

    assertTypeError { """implicitly[simpleUser.PropertySet HasProperties (email.type :~: id.type :~: ∅)]""" }
    assertTypeError { """implicitly[simpleUser.PropertySet HasProperties (email.type :~: name.type :~: color.type :~: ∅)]""" }
  }

  object propertyConverters {

    val idVParser: String => Option[Integer] = str => {
      import scala.util.control.Exception._
      catching(classOf[NumberFormatException]) opt str.toInt
    }
    implicit val idParser   = PropertyParser(id, id.label)(idVParser)
    implicit val nameParser = PropertyParser(name, name.label){ str: String => Some(str) }

    implicit val idSerializer   = PropertySerializer(id, id.label)( x => Some(x.toString) )
    implicit val nameSerializer = PropertySerializer(name, name.label){ x: String => Some(x) }
  }

  test("can parse records from Maps") {

    import propertyConverters._
    import types._
    import ops.typeSets.{ParseDenotations, ParseDenotationsError, KeyNotFound, ErrorParsing}

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

    val mapWithOtherStuff = simpleUserEntryMap + ("other" -> "stuff")


    assert { ( simpleUser parse simpleUserEntryMap ) === Right(simpleUser(id(29681) :~: name("Antonio") :~: ∅)) }
    assert { ( simpleUser parse wrongKeyMap ) === Left(KeyNotFound(id.label, wrongKeyMap)) }
    assert { ( simpleUser parse notIntValueMap ) === Left(ErrorParsing(ErrorParsingValue(id)("twenty-two"))) }
    assert { ( simpleUser parse mapWithOtherStuff ) === Right(simpleUser(id(29681) :~: name("Antonio") :~: ∅)) }
  }

  test("can serialize records to Maps") {

    import propertyConverters._
    import ops.typeSets.{SerializeDenotations, SerializeDenotationsError, KeyPresent}


    val simpleUserEntryMap =  Map(
      "id" -> "29681",
      "name" -> "Antonio"
    )
    assert { Right(simpleUserEntryMap) === simpleUser(id(29681) :~: name("Antonio") :~: ∅).serialize[String] }

    val unrelatedMap = Map(
      "lala" -> "hola!",
      "ohno" -> "pigeons"
    )

    val mapWithKey = unrelatedMap + ("id" -> "1321")

    assert {
      Right(simpleUserEntryMap ++ unrelatedMap) ===
        ( simpleUser(id(29681) :~: name("Antonio") :~: ∅) serializeUsing unrelatedMap )
    }

    assert {
      Left(KeyPresent(id.label, mapWithKey)) ===
        ( simpleUser(id(29681) :~: name("Antonio") :~: ∅) serializeUsing mapWithKey )
    }
  }
}
