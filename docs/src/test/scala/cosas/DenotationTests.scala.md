
```scala
package ohnosequences.cosas.tests

import ohnosequences.cosas._, types._, AnySubsetType._

object DenotationTestsContext {

  case object Color extends Wrap[String]("Color")
  object User extends Type("User")
  type User = User.type
  object Friend extends Type("Friend")
  case class userInfo(id: String, name: String, age: Int)
```

The NEList stuff

```scala
  final class WrappedList[E] extends Wrap[List[E]]("WrappedList")

  class NEList[E] extends SubsetType[WrappedList[E]] {

    lazy val label = "NEList"
    def predicate(l: List[E]): Boolean = ! l.isEmpty

    def apply(e: E): ValueOf[NEList[E]] = new ValueOf[NEList[E]](e :: Nil)
  }

  object NEList {

    implicit def toOps[E](v: ValueOf[NEList[E]]): NEListOps[E] = new NEListOps(v.value)
    implicit def toSSTops[E](v: NEList[E]): SubSetTypeOps[WrappedList[E], NEList[E]] = new SubSetTypeOps(v)
  }

  def NEListOf[E]: NEList[E] = new NEList()

  class NEListOps[E](val l: List[E]) extends AnyVal with ValueOfSubsetTypeOps[WrappedList[E], NEList[E]] {

    def ::(x: E): ValueOf[NEList[E]] = unsafeValueOf[NEList[E]](x :: l)
  }
}

class DenotationTests extends org.scalatest.FunSuite with ScalazEquality {

  import DenotationTestsContext._

  test("creating values") {

    val azul = Color := "blue"
    val verde = valueOf(Color)("green")
    val amarillo = Color := "yellow"

    val x1 = "yellow" =: Color

    assert{ azul.value == "blue" }
    assert{ verde.value == "green" }
    assert{ amarillo.value == "yellow" }
    assert{ amarillo == x1 }
  }

  test("create denotations") {

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

  test("type-safe equals") {

    val paco = "Paco"
    val jose = "Jose"

    val u1 = paco =: User
    val u1Again = paco =: User

    val u2 = paco =: Friend
    val v = jose =: Friend

    assert { u1 == u1 }
    assert { u1 == u1Again }
    // assert { u2 =/= v } // not there in ScalaTest :-/
    // assert { u1 === u2 }
    assertTypeError("u1 === u2")
    assert{ !( u2 == v ) }
  }

  test("Denotation show") {

    assert { (User := "hola").show == "User := hola" }
  }

  test("naive nonempty lists") {

    import DenotationTestsContext._

    import AnySubsetType._
    // this is Some(...) but we don't know at runtime. What about a macro for this? For literals of course
    val oh = NEListOf[Int](12 :: 232 :: Nil)

    val nelint = NEListOf(232)

    val u1 = 23 :: nelint
  }
}

```


------

### Index

+ src
  + test
    + scala
      + cosas
        + [PropertyTests.scala][test/scala/cosas/PropertyTests.scala]
        + [TypeUnionTests.scala][test/scala/cosas/TypeUnionTests.scala]
        + [ScalazEquality.scala][test/scala/cosas/ScalazEquality.scala]
        + [DenotationTests.scala][test/scala/cosas/DenotationTests.scala]
        + [RecordTests.scala][test/scala/cosas/RecordTests.scala]
        + [TypeSetTests.scala][test/scala/cosas/TypeSetTests.scala]
  + main
    + scala
      + cosas
        + [properties.scala][main/scala/cosas/properties.scala]
        + [typeSets.scala][main/scala/cosas/typeSets.scala]
        + ops
          + records
            + [Update.scala][main/scala/cosas/ops/records/Update.scala]
            + [Conversions.scala][main/scala/cosas/ops/records/Conversions.scala]
            + [Get.scala][main/scala/cosas/ops/records/Get.scala]
          + typeSets
            + [Filter.scala][main/scala/cosas/ops/typeSets/Filter.scala]
            + [Reorder.scala][main/scala/cosas/ops/typeSets/Reorder.scala]
            + [Conversions.scala][main/scala/cosas/ops/typeSets/Conversions.scala]
            + [AggregateProperties.scala][main/scala/cosas/ops/typeSets/AggregateProperties.scala]
            + [Subtract.scala][main/scala/cosas/ops/typeSets/Subtract.scala]
            + [Pop.scala][main/scala/cosas/ops/typeSets/Pop.scala]
            + [Representations.scala][main/scala/cosas/ops/typeSets/Representations.scala]
            + [Replace.scala][main/scala/cosas/ops/typeSets/Replace.scala]
            + [Take.scala][main/scala/cosas/ops/typeSets/Take.scala]
            + [Union.scala][main/scala/cosas/ops/typeSets/Union.scala]
            + [Mappers.scala][main/scala/cosas/ops/typeSets/Mappers.scala]
        + [typeUnions.scala][main/scala/cosas/typeUnions.scala]
        + [records.scala][main/scala/cosas/records.scala]
        + csv
          + [csv.scala][main/scala/cosas/csv/csv.scala]
        + [fns.scala][main/scala/cosas/fns.scala]
        + [propertyHolders.scala][main/scala/cosas/propertyHolders.scala]
        + [types.scala][main/scala/cosas/types.scala]

[test/scala/cosas/PropertyTests.scala]: PropertyTests.scala.md
[test/scala/cosas/TypeUnionTests.scala]: TypeUnionTests.scala.md
[test/scala/cosas/ScalazEquality.scala]: ScalazEquality.scala.md
[test/scala/cosas/DenotationTests.scala]: DenotationTests.scala.md
[test/scala/cosas/RecordTests.scala]: RecordTests.scala.md
[test/scala/cosas/TypeSetTests.scala]: TypeSetTests.scala.md
[main/scala/cosas/properties.scala]: ../../../main/scala/cosas/properties.scala.md
[main/scala/cosas/typeSets.scala]: ../../../main/scala/cosas/typeSets.scala.md
[main/scala/cosas/ops/records/Update.scala]: ../../../main/scala/cosas/ops/records/Update.scala.md
[main/scala/cosas/ops/records/Conversions.scala]: ../../../main/scala/cosas/ops/records/Conversions.scala.md
[main/scala/cosas/ops/records/Get.scala]: ../../../main/scala/cosas/ops/records/Get.scala.md
[main/scala/cosas/ops/typeSets/Filter.scala]: ../../../main/scala/cosas/ops/typeSets/Filter.scala.md
[main/scala/cosas/ops/typeSets/Reorder.scala]: ../../../main/scala/cosas/ops/typeSets/Reorder.scala.md
[main/scala/cosas/ops/typeSets/Conversions.scala]: ../../../main/scala/cosas/ops/typeSets/Conversions.scala.md
[main/scala/cosas/ops/typeSets/AggregateProperties.scala]: ../../../main/scala/cosas/ops/typeSets/AggregateProperties.scala.md
[main/scala/cosas/ops/typeSets/Subtract.scala]: ../../../main/scala/cosas/ops/typeSets/Subtract.scala.md
[main/scala/cosas/ops/typeSets/Pop.scala]: ../../../main/scala/cosas/ops/typeSets/Pop.scala.md
[main/scala/cosas/ops/typeSets/Representations.scala]: ../../../main/scala/cosas/ops/typeSets/Representations.scala.md
[main/scala/cosas/ops/typeSets/Replace.scala]: ../../../main/scala/cosas/ops/typeSets/Replace.scala.md
[main/scala/cosas/ops/typeSets/Take.scala]: ../../../main/scala/cosas/ops/typeSets/Take.scala.md
[main/scala/cosas/ops/typeSets/Union.scala]: ../../../main/scala/cosas/ops/typeSets/Union.scala.md
[main/scala/cosas/ops/typeSets/Mappers.scala]: ../../../main/scala/cosas/ops/typeSets/Mappers.scala.md
[main/scala/cosas/typeUnions.scala]: ../../../main/scala/cosas/typeUnions.scala.md
[main/scala/cosas/records.scala]: ../../../main/scala/cosas/records.scala.md
[main/scala/cosas/csv/csv.scala]: ../../../main/scala/cosas/csv/csv.scala.md
[main/scala/cosas/fns.scala]: ../../../main/scala/cosas/fns.scala.md
[main/scala/cosas/propertyHolders.scala]: ../../../main/scala/cosas/propertyHolders.scala.md
[main/scala/cosas/types.scala]: ../../../main/scala/cosas/types.scala.md