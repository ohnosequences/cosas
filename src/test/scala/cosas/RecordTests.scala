package ohnosequences.cosas.tests

import ohnosequences.cosas._, types._, klists._, records._

case object recordTestsContext {

  case object id    extends Type[Int]("id")
  case object name  extends Type[String]("name")
  case object email extends Type[String]("email")
  case object color extends Type[String]("color")

  // funny square is an option too
  case object simpleUser extends RecordType(id :×: name :×: unit)
  case object normalUser extends RecordType(id :×: name :×: email :×: color :×: unit)

  val volatileRec = new RecordType(email :×: color :×: unit)

  val volatileRecEntry = volatileRec (
    email("oh@buh.com") ::
    color("blue") ::
    *[AnyDenotation]
  )

  // creating a record instance is easy and neat:
  val simpleUserEntry = simpleUser (
    id(123)     ::
    name("foo") ::
    *[AnyDenotation]
  )

  // this way the order of properties does not matter
  val normalUserEntry = normalUser := (
    id(123)               ::
    name("foo")           ::
    email("foo@bar.qux")  ::
    color("orange")       ::
    *[AnyDenotation]
  )
}
//
class RecordTypeTests extends org.scalatest.FunSuite {

  import recordTestsContext._

  test("should fail when some properties are missing") {
    assertTypeError("""
    val wrongAttrSet = simpleUser(
      id(123) ::
      *[AnyDenotation]
    )
    """)

    assertTypeError("""
    val wrongAttrSet = normalUser(
      id(123)     ::
      name("foo") ::
      *[AnyDenotation]
    )
    """)
  }

  test("can access fields and field values") {

    assert { (simpleUserEntry get id) === id(123) }
    assert { (simpleUserEntry getV id) === 123 }

    assert { (simpleUserEntry get name) === name("foo") }
    assert { (simpleUserEntry getV name) === "foo" }

    assert { (normalUserEntry get email) === email("foo@bar.qux") }
    assert { (normalUserEntry getV email) === "foo@bar.qux" }

    // FIXME: assert doesn't work as expected (value classes, etc.)
    assert { (simpleUserEntry get name) === email("foo") }
  }


  test("can access fields from vals and volatile vals") {

    assert { (volatileRecEntry get color) === color("blue") }
    assert { (volatileRecEntry get email) === email("oh@buh.com") }
  }

  test("can update fields") {

    assert {
      (normalUserEntry update color("albero")) ===
        (normalUser(
          (normalUserEntry get id)    ::
          (normalUserEntry get name)  ::
          (normalUserEntry get email) ::
          color("albero")             ::
          *[AnyDenotation]
        )
      )
    }

    assert {
      // NOTE can update in any order
      (normalUserEntry update name("bar") :: id(321) :: *[AnyDenotation]) === (
        normalUser(
          id(321)                     ::
          name("bar")                 ::
          (normalUserEntry get email) ::
          (normalUserEntry get color) ::
          *[AnyDenotation]
        )
      )
    }
  }

  ignore("can transform a klist of values as a record") {

    // assertResult(normalUserEntry) {
    //   records.syntax.RecordReorderSyntax (
    //     name("foo")           ::
    //     color("orange")       ::
    //     email("foo@bar.qux")  ::
    //     id(123)               ::
    //     *[AnyDenotation]
    //   ).as(normalUser)
    // }
  }

  ignore("can see a record entry as another") {

    // assert { simpleUserEntry === ( normalUserEntry as simpleUser ) }
  }

//   test("can provide properties in different order") {
//
//     // the declared property order
//     implicitly [
//       simpleUser.Raw =:= (ValueOf[id.type] :: ValueOf[name.type] :: KNil[AnyDenotation])
//     ]
//
//     // they get reordered
//     val simpleUserV: ValueOf[simpleUser.type] = simpleUser {
//       id(29681)       ::
//       name("Antonio") :: KNil[AnyDenotation]
//     }
//
//     val sameSimpleUserV: ValueOf[simpleUser.type] = simpleUser {
//       id(29681)       ::
//       name("Antonio") :: KNil[AnyDenotation]
//     }
//
//     assert { simpleUserV == sameSimpleUserV }
//   }
//   //
//   // test("can check if record has properties") {
//   //
//   //   implicitly[simpleUser.SetOfTypes HasProperties (id.type :: name.type :: KNil[AnyDenotation])]
//   //   implicitly[simpleUser.SetOfTypes HasProperties (name.type :: id.type :: KNil)]
//   //   implicitly[simpleUser.SetOfTypes HasProperties (name.type :: KNil)]
//   //   implicitly[simpleUser.SetOfTypes HasProperties (id.type :: KNil)]
//   //
//   //   implicitly[simpleUser.SetOfTypes HasProperty name.type]
//   //   implicitly[simpleUser.SetOfTypes HasProperty id.type]
//   //
//   //   assertTypeError { """implicitly[simpleUser.SetOfTypes HasProperties (email.type :: id.type :: KNil)]""" }
//   //   assertTypeError { """implicitly[simpleUser.SetOfTypes HasProperties (email.type :: name.type :: color.type :: KNil)]""" }
//   // }
// //
//   case object propertyConverters {
//
//     val idVParser: String => Option[Int] = str => {
//       import scala.util.control.Exception._
//       catching(classOf[NumberFormatException]) opt str.toInt
//     }
//     implicit val idParser   = PropertyParser(id, id.label)(idVParser)
//     implicit val nameParser = PropertyParser(name, name.label){ str: String => Some(str) }
//
//     implicit val idSerializer   = PropertySerializer(id, id.label)( x => Some(x.toString) )
//     implicit val nameSerializer = PropertySerializer(name, name.label){ x: String => Some(x) }
//   }
//
//   test("can parse records from Maps") {
//
//     import propertyConverters._
//
//     val simpleUserEntryMap =  Map(
//       "id" -> "29681",
//       "name" -> "Antonio"
//     )
//     val wrongKeyMap = Map(
//       "idd" -> "29681",
//       "name" -> "Antonio"
//     )
//
//     val notIntValueMap = Map(
//       "name" -> "Antonio",
//       "id" -> "twenty-two"
//     )
//
//     val mapWithOtherStuff = simpleUserEntryMap + ("other" -> "stuff")
//
//     assert {
//       parseDenotations[String,simpleUser.Raw](simpleUserEntryMap) ===
//       Right(id(29681) :: name("Antonio") :: KNil[AnyDenotation])
//     }
//     assert {
//       parseDenotations[String,simpleUser.Raw](wrongKeyMap) ===
//       Left(KeyNotFound(id.label, wrongKeyMap))
//     }
//     assert {
//       parseDenotations[String,simpleUser.Raw](notIntValueMap) ===
//       Left(ErrorParsing(ErrorParsingValue(id)("twenty-two")))
//     }
//     assert {
//       parseDenotations[String,simpleUser.Raw](mapWithOtherStuff) ===
//       Right(id(29681) :: name("Antonio") :: KNil[AnyDenotation])
//     }
//   }
//
//   test("can serialize records to Maps") {
//
//     import propertyConverters._
//
//     val simpleUserEntryMap =  Map(
//       "id" -> "29681",
//       "name" -> "Antonio"
//     )
//     assert {
//       Right(simpleUserEntryMap) ===
//       serializeDenotations[String,simpleUser.Raw](Map[String,String](), id(29681) :: name("Antonio") :: KNil[AnyDenotation])
//     }
//
//     val unrelatedMap = Map(
//       "lala" -> "hola!",
//       "ohno" -> "pigeons"
//     )
//
//     val mapWithKey = unrelatedMap + ("id" -> "1321")
//
//     assert {
//       Right(simpleUserEntryMap ++ unrelatedMap) ===
//       serializeDenotations[String,simpleUser.Raw](
//         unrelatedMap,
//         id(29681) :: name("Antonio") :: KNil[AnyDenotation]
//       )
//     }
//
//     assert {
//       Left(KeyPresent(id.label, mapWithKey)) ===
//       serializeDenotations[String,simpleUser.Raw](
//         mapWithKey,
//         id(29681) :: name("Antonio") :: KNil[AnyDenotation]
//       )
//     }
//   }
//
//   // test("can get values from records as lists and typesets") {
//   //
//   //   val vRecordTypeEntryValues = mapToListOf[String](
//   //     denotationValue,
//   //     // need to add the type here
//   //     volatileRecEntry.value: ValueOf[email.type] :: ValueOf[color.type] :: KNil[AnyDenotation]
//   //   )
//   //
//   //   val simpleUserValues: Int :: String :: KNil[Any] = simpleUserEntry.value map denotationValue
//   //   val simpleUserValuesAny = mapToListOf[Any](denotationValue, simpleUserEntry.value)
//   //
//   //   assert { simpleUserValuesAny === simpleUserValues.toList[Any] }
//   // }
}
