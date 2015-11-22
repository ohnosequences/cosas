
```scala
package ohnosequences.cosas.tests

import ohnosequences.cosas._, types._, klists._, records._
import recordTestsContext._

case object recordTestsContext {

  case object id    extends Type[Int]("id")
  case object name  extends Type[String]("name")
  case object email extends Type[String]("email")
  case object color extends Type[String]("color")

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

  val normalUserEntry = normalUser := (
    id(123)               ::
    name("foo")           ::
    email("foo@bar.qux")  ::
    color("orange")       ::
    *[AnyDenotation]
  )
}

class RecordTypeTests extends org.scalatest.FunSuite {


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

  test("can transform a klist of values as a record") {

    assertResult(normalUserEntry) {
      (
        name("foo")           ::
        color("orange")       ::
        email("foo@bar.qux")  ::
        id(123)               ::
        *[AnyDenotation]
      ) as normalUser
    }

    assertResult(normalUserEntry) {
      normalUser from (
        name("foo")           ::
        email("foo@bar.qux")  ::
        color("orange")       ::
        id(123)               ::
        *[AnyDenotation]
      )
    }
  }

  test("product type interop") {

    assert { (simpleUser := simpleUserEntry.toProduct.value) === simpleUserEntry }
  }


  ignore("can check if record has properties") {

    // implicitly[simpleUser.SetOfTypes HasProperties (id.type :: name.type :: KNil[AnyDenotation])]
    // implicitly[simpleUser.SetOfTypes HasProperties (name.type :: id.type :: KNil)]
    // implicitly[simpleUser.SetOfTypes HasProperties (name.type :: KNil)]
    // implicitly[simpleUser.SetOfTypes HasProperties (id.type :: KNil)]
    //
    // implicitly[simpleUser.SetOfTypes HasProperty name.type]
    // implicitly[simpleUser.SetOfTypes HasProperty id.type]
    //
    // assertTypeError { """implicitly[simpleUser.SetOfTypes HasProperties (email.type :: id.type :: KNil)]""" }
    // assertTypeError { """implicitly[simpleUser.SetOfTypes HasProperties (email.type :: name.type :: color.type :: KNil)]""" }
  }

  case object propertyConverters {

    val idVParser: String => Option[Int] = str => {
      import scala.util.control.Exception._
      catching(classOf[NumberFormatException]) opt str.toInt
    }
    implicit def idParser: DenotationParser[id.type, Int, String]  = new DenotationParser(id, id.label)(idVParser)

    implicit val idSerializer: DenotationSerializer[id.type, Int, String] = new DenotationSerializer(id, id.label)( { x: Int => Some(x.toString )} )
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
      (simpleUser.parse(simpleUserEntryMap)) ===
      Right(simpleUser := id(29681) :: name("Antonio") :: *[AnyDenotation])
    }
    assert {
      simpleUser.parse(wrongKeyMap) ===
      Left(KeyNotFound(id.label, wrongKeyMap))
    }
    assert {
      simpleUser.parse(notIntValueMap) ===
      Left(ErrorParsing(ErrorParsingValue(id)("twenty-two")))
    }
    assert {
      simpleUser.parse(mapWithOtherStuff) ===
      Right(simpleUser := id(29681) :: name("Antonio") :: *[AnyDenotation])
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
      (simpleUser := id(29681) :: name("Antonio") :: KNil[AnyDenotation]).serialize
    }

    val unrelatedMap = Map(
      "lala" -> "hola!",
      "ohno" -> "pigeons"
    )

    val mapWithKey = unrelatedMap + ("id" -> "1321")

    assert {
      Right(simpleUserEntryMap ++ unrelatedMap) ===
        (simpleUser := id(29681) :: name("Antonio") :: KNil[AnyDenotation]).serializeUsing(unrelatedMap)
    }

    assert {
      Left(KeyPresent(id.label, mapWithKey)) ===
        (simpleUser := id(29681) :: name("Antonio") :: *[AnyDenotation]).serializeUsing(mapWithKey)
    }
  }
}

```




[test/scala/cosas/asserts.scala]: asserts.scala.md
[test/scala/cosas/DenotationTests.scala]: DenotationTests.scala.md
[test/scala/cosas/EqualityTests.scala]: EqualityTests.scala.md
[test/scala/cosas/DependentFunctionsTests.scala]: DependentFunctionsTests.scala.md
[test/scala/cosas/KListsTests.scala]: KListsTests.scala.md
[test/scala/cosas/RecordTests.scala]: RecordTests.scala.md
[test/scala/cosas/NatTests.scala]: NatTests.scala.md
[test/scala/cosas/TypeUnionTests.scala]: TypeUnionTests.scala.md
[main/scala/cosas/package.scala]: ../../../main/scala/cosas/package.scala.md
[main/scala/cosas/types/package.scala]: ../../../main/scala/cosas/types/package.scala.md
[main/scala/cosas/types/types.scala]: ../../../main/scala/cosas/types/types.scala.md
[main/scala/cosas/types/parsing.scala]: ../../../main/scala/cosas/types/parsing.scala.md
[main/scala/cosas/types/productTypes.scala]: ../../../main/scala/cosas/types/productTypes.scala.md
[main/scala/cosas/types/syntax.scala]: ../../../main/scala/cosas/types/syntax.scala.md
[main/scala/cosas/types/project.scala]: ../../../main/scala/cosas/types/project.scala.md
[main/scala/cosas/types/denotations.scala]: ../../../main/scala/cosas/types/denotations.scala.md
[main/scala/cosas/types/functionTypes.scala]: ../../../main/scala/cosas/types/functionTypes.scala.md
[main/scala/cosas/types/serialization.scala]: ../../../main/scala/cosas/types/serialization.scala.md
[main/scala/cosas/klists/replace.scala]: ../../../main/scala/cosas/klists/replace.scala.md
[main/scala/cosas/klists/cons.scala]: ../../../main/scala/cosas/klists/cons.scala.md
[main/scala/cosas/klists/klists.scala]: ../../../main/scala/cosas/klists/klists.scala.md
[main/scala/cosas/klists/take.scala]: ../../../main/scala/cosas/klists/take.scala.md
[main/scala/cosas/klists/package.scala]: ../../../main/scala/cosas/klists/package.scala.md
[main/scala/cosas/klists/takeFirst.scala]: ../../../main/scala/cosas/klists/takeFirst.scala.md
[main/scala/cosas/klists/toList.scala]: ../../../main/scala/cosas/klists/toList.scala.md
[main/scala/cosas/klists/filter.scala]: ../../../main/scala/cosas/klists/filter.scala.md
[main/scala/cosas/klists/pick.scala]: ../../../main/scala/cosas/klists/pick.scala.md
[main/scala/cosas/klists/drop.scala]: ../../../main/scala/cosas/klists/drop.scala.md
[main/scala/cosas/klists/map.scala]: ../../../main/scala/cosas/klists/map.scala.md
[main/scala/cosas/klists/at.scala]: ../../../main/scala/cosas/klists/at.scala.md
[main/scala/cosas/klists/syntax.scala]: ../../../main/scala/cosas/klists/syntax.scala.md
[main/scala/cosas/klists/fold.scala]: ../../../main/scala/cosas/klists/fold.scala.md
[main/scala/cosas/klists/noDuplicates.scala]: ../../../main/scala/cosas/klists/noDuplicates.scala.md
[main/scala/cosas/klists/slice.scala]: ../../../main/scala/cosas/klists/slice.scala.md
[main/scala/cosas/klists/find.scala]: ../../../main/scala/cosas/klists/find.scala.md
[main/scala/cosas/records/package.scala]: ../../../main/scala/cosas/records/package.scala.md
[main/scala/cosas/records/recordTypes.scala]: ../../../main/scala/cosas/records/recordTypes.scala.md
[main/scala/cosas/records/syntax.scala]: ../../../main/scala/cosas/records/syntax.scala.md
[main/scala/cosas/records/reorder.scala]: ../../../main/scala/cosas/records/reorder.scala.md
[main/scala/cosas/typeUnions/typeUnions.scala]: ../../../main/scala/cosas/typeUnions/typeUnions.scala.md
[main/scala/cosas/typeUnions/package.scala]: ../../../main/scala/cosas/typeUnions/package.scala.md
[main/scala/cosas/fns/predicates.scala]: ../../../main/scala/cosas/fns/predicates.scala.md
[main/scala/cosas/fns/instances.scala]: ../../../main/scala/cosas/fns/instances.scala.md
[main/scala/cosas/fns/package.scala]: ../../../main/scala/cosas/fns/package.scala.md
[main/scala/cosas/fns/syntax.scala]: ../../../main/scala/cosas/fns/syntax.scala.md
[main/scala/cosas/fns/functions.scala]: ../../../main/scala/cosas/fns/functions.scala.md
[main/scala/cosas/subtyping.scala]: ../../../main/scala/cosas/subtyping.scala.md
[main/scala/cosas/witness.scala]: ../../../main/scala/cosas/witness.scala.md
[main/scala/cosas/equality.scala]: ../../../main/scala/cosas/equality.scala.md
[main/scala/cosas/Nat.scala]: ../../../main/scala/cosas/Nat.scala.md
[main/scala/cosas/Bool.scala]: ../../../main/scala/cosas/Bool.scala.md