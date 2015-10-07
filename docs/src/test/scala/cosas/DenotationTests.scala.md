
```scala
package ohnosequences.cosas.tests

import ohnosequences.cosas._, types._, AnySubsetType._
import ohnosequences.cosas.tests.asserts._

case object DenotationTestsContext {

  case object Color extends Wrap[String]("Color")
  object User extends Type("User")
  type User = User.type
  object Friend extends Type("Friend")
  case class userInfo(id: String, name: String, age: Int)
}

class DenotationTests extends org.scalatest.FunSuite {

  import DenotationTestsContext._

  test("can create denotations of types") {

    val azul = Color := "blue"
    val verde = valueOf(Color)("green")
    val amarillo = Color := "yellow"

    val x1 = "yellow" =: Color

    assert(azul.value == "blue")
    assert(verde.value == "green")
    assert(amarillo.value == "yellow")
    assertTaggedEq(amarillo, x1)
  }

  test("can use syntax for creating denotations") {

    val z = User := 2423423
```

the right-associative syntax

```scala
    val uh: userInfo =: User = userInfo(id = "adqwr32141", name = "Salustiano", age = 143) =: User
```

or with equals-sign style

```scala
    val oh = userInfo(id = "adqwr32141", name = "Salustiano", age = 143) =: User
```

or in the other order

```scala
    val ah = User := userInfo(id = "adqwr32141", name = "Salustiano", age = 143)
  }

  test("Equality is type-safe for denotations") {

    val paco = "Paco"
    val jose = "Jose"

    val u1 = paco =: User
    val u1Again = paco =: User

    val u2 = paco =: Friend
    val v = jose =: Friend

    assertTaggedEq(u1, u1)
    assertTaggedEq(u1, u1Again)
    // assertTaggedEq { u2, v } // not there in ScalaTest :-/
    // assertTaggedEq { u1, u2 }
    assertTypeError("u1 =~= u2")
    assert{ !(v =~= u2) }
  }

  test("Can use show for denotations") {

    assert{ (User := "hola").show == "(User := hola)" }

    val azul = Color := "blue"

    assert{ azul.show == "(Color := blue)" }
  }

  test("can get poly values of denotations") {

    assert { "blue" === denotationValue(User := "blue") }
  }
}

```




[test/scala/cosas/asserts.scala]: asserts.scala.md
[test/scala/cosas/DenotationTests.scala]: DenotationTests.scala.md
[test/scala/cosas/SubsetTypesTests.scala]: SubsetTypesTests.scala.md
[test/scala/cosas/EqualityTests.scala]: EqualityTests.scala.md
[test/scala/cosas/PropertyTests.scala]: PropertyTests.scala.md
[test/scala/cosas/RecordTests.scala]: RecordTests.scala.md
[test/scala/cosas/TypeSetTests.scala]: TypeSetTests.scala.md
[test/scala/cosas/TypeUnionTests.scala]: TypeUnionTests.scala.md
[main/scala/cosas/typeUnions.scala]: ../../../main/scala/cosas/typeUnions.scala.md
[main/scala/cosas/properties.scala]: ../../../main/scala/cosas/properties.scala.md
[main/scala/cosas/records.scala]: ../../../main/scala/cosas/records.scala.md
[main/scala/cosas/fns.scala]: ../../../main/scala/cosas/fns.scala.md
[main/scala/cosas/types.scala]: ../../../main/scala/cosas/types.scala.md
[main/scala/cosas/typeSets.scala]: ../../../main/scala/cosas/typeSets.scala.md
[main/scala/cosas/ops/records/Update.scala]: ../../../main/scala/cosas/ops/records/Update.scala.md
[main/scala/cosas/ops/records/Transform.scala]: ../../../main/scala/cosas/ops/records/Transform.scala.md
[main/scala/cosas/ops/records/Get.scala]: ../../../main/scala/cosas/ops/records/Get.scala.md
[main/scala/cosas/ops/typeSets/SerializeDenotations.scala]: ../../../main/scala/cosas/ops/typeSets/SerializeDenotations.scala.md
[main/scala/cosas/ops/typeSets/ParseDenotations.scala]: ../../../main/scala/cosas/ops/typeSets/ParseDenotations.scala.md
[main/scala/cosas/ops/typeSets/Conversions.scala]: ../../../main/scala/cosas/ops/typeSets/Conversions.scala.md
[main/scala/cosas/ops/typeSets/Filter.scala]: ../../../main/scala/cosas/ops/typeSets/Filter.scala.md
[main/scala/cosas/ops/typeSets/Subtract.scala]: ../../../main/scala/cosas/ops/typeSets/Subtract.scala.md
[main/scala/cosas/ops/typeSets/Mappers.scala]: ../../../main/scala/cosas/ops/typeSets/Mappers.scala.md
[main/scala/cosas/ops/typeSets/Union.scala]: ../../../main/scala/cosas/ops/typeSets/Union.scala.md
[main/scala/cosas/ops/typeSets/Reorder.scala]: ../../../main/scala/cosas/ops/typeSets/Reorder.scala.md
[main/scala/cosas/ops/typeSets/Take.scala]: ../../../main/scala/cosas/ops/typeSets/Take.scala.md
[main/scala/cosas/ops/typeSets/Representations.scala]: ../../../main/scala/cosas/ops/typeSets/Representations.scala.md
[main/scala/cosas/ops/typeSets/Pop.scala]: ../../../main/scala/cosas/ops/typeSets/Pop.scala.md
[main/scala/cosas/ops/typeSets/Replace.scala]: ../../../main/scala/cosas/ops/typeSets/Replace.scala.md
[main/scala/cosas/equality.scala]: ../../../main/scala/cosas/equality.scala.md