package ohnosequences.cosas.tests

import ohnosequences.cosas._, types._, typeSets._, properties._, records._

case object recordTestsContext {

  case object id    extends Property[Int]("id")
  case object name  extends Property[String]("name")
  case object notProperty
  case object email extends Property[String]("email")
  case object color extends Property[String]("color")

  // funny square is an option too
  case object simpleUser extends Record(id :&: name :&: □)
  case object normalUser extends Record(id :&: name :&: email :&: color :&: □)
  val vProps  = email :&: color :&: □
  val vRecord = new Record(vProps)
  val vEmail = "oh@buh.com"

  val vRecordEntry = vRecord (
    email(vEmail) :~:
    color("blue") :~:
    ∅[AnyDenotation]
  )

  // creating a record instance is easy and neat:
  val simpleUserEntry = simpleUser (
    id(123)     :~:
    name("foo") :~:
    ∅[AnyDenotation]
  )

  // this way the order of properties does not matter
  val normalUserEntry = normalUser (
    id(123)               :~:
    name("foo")           :~:
    email("foo@bar.qux")  :~:
    color("orange")       :~:
    ∅[AnyDenotation]
  )
}

class RecordTests extends org.scalatest.FunSuite {

  import recordTestsContext._

  test("should fail when some properties are missing") {
    // you have to set _all_ properties
    assertTypeError("""
    val wrongAttrSet = simpleUser(id(123) :~: ∅[AnyDenotation])
    """)

    // but you still have to present all properties:
    assertTypeError("""
    val wrongAttrSet = normalUser(
      id(123)     :~:
      name("foo") :~:
      ∅[AnyDenotation]
    )
    """)
  }

  test("can access fields and field values") {

    assert { (simpleUserEntry get id)   === id(123)     }
    assert { (simpleUserEntry get name) === name("foo") }

    assert { (simpleUserEntry getV id) === 123 }
    assert { (simpleUserEntry getV name) === "foo" }
  }

  test("can access fields from vals and volatile vals") {

    assert{ (vRecordEntry get email) === email("oh@buh.com") }
  }

  // test("can update fields") {
  //
  //   assert {
  //
  //     ( normalUserEntry update color("albero") ) === normalUser (
  //       (normalUserEntry get id)    :~:
  //       (normalUserEntry get name)  :~:
  //       (normalUserEntry get email) :~:
  //       color("albero")             :~:
  //       ∅[AnyDenotation]
  //     )
  //   }
  //
  //   assert {
  //
  //     ( normalUserEntry update name("bar") :~: id(321) :~: ∅ ) === normalUser (
  //         id(321)               :~:
  //         name("bar")           :~:
  //         email("foo@bar.qux")  :~:
  //         color("orange")       :~:
  //         ∅[AnyDenotation]
  //       )
  //   }
  // }

  // test("can see a record entry as another") {
  //
  //   assert { normalUserEntry === ( simpleUserEntry as (normalUser, email("foo@bar.qux") :~: color("orange") :~: ∅[AnyDenotation]) ) }
  // }

  test("can provide properties in different order") {

    // the declared property order
    implicitly [
      simpleUser.Raw =:= (ValueOf[id.type] :~: ValueOf[name.type] :~: ∅[AnyDenotation])
    ]

    // they get reordered
    val simpleUserV: ValueOf[simpleUser.type] = simpleUser {
      id(29681)       :~:
      name("Antonio") :~: ∅[AnyDenotation]
    }

    val sameSimpleUserV: ValueOf[simpleUser.type] = simpleUser {
      id(29681)       :~:
      name("Antonio") :~: ∅[AnyDenotation]
    }

    assert { simpleUserV == sameSimpleUserV }
  }
  //
  // test("can check if record has properties") {
  //
  //   implicitly[simpleUser.PropertySet HasProperties (id.type :~: name.type :~: ∅[AnyDenotation])]
  //   implicitly[simpleUser.PropertySet HasProperties (name.type :~: id.type :~: ∅)]
  //   implicitly[simpleUser.PropertySet HasProperties (name.type :~: ∅)]
  //   implicitly[simpleUser.PropertySet HasProperties (id.type :~: ∅)]
  //
  //   implicitly[simpleUser.PropertySet HasProperty name.type]
  //   implicitly[simpleUser.PropertySet HasProperty id.type]
  //
  //   assertTypeError { """implicitly[simpleUser.PropertySet HasProperties (email.type :~: id.type :~: ∅)]""" }
  //   assertTypeError { """implicitly[simpleUser.PropertySet HasProperties (email.type :~: name.type :~: color.type :~: ∅)]""" }
  // }
//
  case object propertyConverters {

    val idVParser: String => Option[Int] = str => {
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

    assert {
      parseDenotations[String,simpleUser.Raw](simpleUserEntryMap) ===
      Right(id(29681) :~: name("Antonio") :~: ∅[AnyDenotation])
    }
    assert {
      parseDenotations[String,simpleUser.Raw](wrongKeyMap) ===
      Left(KeyNotFound(id.label, wrongKeyMap))
    }
    assert {
      parseDenotations[String,simpleUser.Raw](notIntValueMap) ===
      Left(ErrorParsing(ErrorParsingValue(id)("twenty-two")))
    }
    assert {
      parseDenotations[String,simpleUser.Raw](mapWithOtherStuff) ===
      Right(id(29681) :~: name("Antonio") :~: ∅[AnyDenotation])
    }
  }

  test("can serialize records to Maps") {

    import propertyConverters._

    val simpleUserEntryMap =  Map(
      "id" -> "29681",
      "name" -> "Antonio"
    )
    assert {
      Right(simpleUserEntryMap) ===
      serializeDenotations[String,simpleUser.Raw](Map[String,String](), id(29681) :~: name("Antonio") :~: ∅[AnyDenotation])
    }

    val unrelatedMap = Map(
      "lala" -> "hola!",
      "ohno" -> "pigeons"
    )

    val mapWithKey = unrelatedMap + ("id" -> "1321")

    assert {
      Right(simpleUserEntryMap ++ unrelatedMap) ===
      serializeDenotations[String,simpleUser.Raw](
        unrelatedMap,
        id(29681) :~: name("Antonio") :~: ∅[AnyDenotation]
      )
    }

    assert {
      Left(KeyPresent(id.label, mapWithKey)) ===
      serializeDenotations[String,simpleUser.Raw](
        mapWithKey,
        id(29681) :~: name("Antonio") :~: ∅[AnyDenotation]
      )
    }
  }

  // test("can get values from records as lists and typesets") {
  //
  //   val vRecordEntryValues = mapToListOf[String](
  //     denotationValue,
  //     // need to add the type here
  //     vRecordEntry.value: ValueOf[email.type] :~: ValueOf[color.type] :~: ∅[AnyDenotation]
  //   )
  //
  //   val simpleUserValues: Int :~: String :~: ∅[Any] = simpleUserEntry.value map denotationValue
  //   val simpleUserValuesAny = mapToListOf[Any](denotationValue, simpleUserEntry.value)
  //
  //   assert { simpleUserValuesAny === simpleUserValues.toList[Any] }
  // }
}
