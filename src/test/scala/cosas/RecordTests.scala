package ohnosequences.cosas.tests

import ohnosequences.cosas._, types._, klists._

case object recordTestsContext {

  val □ = EmptyProductType
  class Property[V](val label: String) extends AnyType { type Raw = V }

  case object id    extends Property[Int]("id")
  case object name  extends Property[String]("name")
  case object notProperty
  case object email extends Property[String]("email")
  case object color extends Property[String]("color")

  // funny square is an option too
  case object simpleUser extends RecordType(id :×: name :×: □)
  case object normalUser extends RecordType(id :×: name :×: email :×: color :×: □)
  val vProps  = email :×: color :×: □
  val vRecordType = new RecordType(vProps)
  val vEmail = "oh@buh.com"

  val vRecordTypeEntry = vRecordType (
    email(vEmail) ::
    color("blue") ::
    KNil[AnyDenotation]
  )

  // creating a record instance is easy and neat:
  val simpleUserEntry = simpleUser (
    id(123)     ::
    name("foo") ::
    KNil[AnyDenotation]
  )

  // this way the order of properties does not matter
  val normalUserEntry = normalUser (
    id(123)               ::
    name("foo")           ::
    email("foo@bar.qux")  ::
    color("orange")       ::
    KNil[AnyDenotation]
  )
}
//
// class RecordTypeTests extends org.scalatest.FunSuite {
//
//   import recordTestsContext._
//
//   test("should fail when some properties are missing") {
//     // you have to set _all_ properties
//     assertTypeError("""
//     val wrongAttrSet = simpleUser(id(123) :: KNil[AnyDenotation])
//     """)
//
//     // but you still have to present all properties:
//     assertTypeError("""
//     val wrongAttrSet = normalUser(
//       id(123)     ::
//       name("foo") ::
//       KNil[AnyDenotation]
//     )
//     """)
//   }
//
//   test("can access fields and field values") {
//
//     assert { (new Get[id.type,simpleUser.type])(simpleUserEntry)(Get.default(Lookup.foundInHead))   === id(123)     }
//     assert { (simpleUserEntry get name) === name("foo") }
//
//     assert { (simpleUserEntry getV id) === 123 }
//     assert { (simpleUserEntry getV name) === "foo" }
//   }
//
//   test("can access fields from vals and volatile vals") {
//
//     assert{ (vRecordTypeEntry get email) === email("oh@buh.com") }
//   }
//
//   // test("can update fields") {
//   //
//   //   assert {
//   //
//   //     ( normalUserEntry update color("albero") ) === normalUser (
//   //       (normalUserEntry get id)    ::
//   //       (normalUserEntry get name)  ::
//   //       (normalUserEntry get email) ::
//   //       color("albero")             ::
//   //       KNil[AnyDenotation]
//   //     )
//   //   }
//   //
//   //   assert {
//   //
//   //     ( normalUserEntry update name("bar") :: id(321) :: KNil ) === normalUser (
//   //         id(321)               ::
//   //         name("bar")           ::
//   //         email("foo@bar.qux")  ::
//   //         color("orange")       ::
//   //         KNil[AnyDenotation]
//   //       )
//   //   }
//   // }
//
//   // test("can see a record entry as another") {
//   //
//   //   assert { normalUserEntry === ( simpleUserEntry as (normalUser, email("foo@bar.qux") :: color("orange") :: KNil[AnyDenotation]) ) }
//   // }
//
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
//   //     vRecordTypeEntry.value: ValueOf[email.type] :: ValueOf[color.type] :: KNil[AnyDenotation]
//   //   )
//   //
//   //   val simpleUserValues: Int :: String :: KNil[Any] = simpleUserEntry.value map denotationValue
//   //   val simpleUserValuesAny = mapToListOf[Any](denotationValue, simpleUserEntry.value)
//   //
//   //   assert { simpleUserValuesAny === simpleUserValues.toList[Any] }
//   // }
// }
