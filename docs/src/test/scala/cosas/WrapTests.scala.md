
```scala
package ohnosequences.cosas.tests

import ohnosequences.cosas._, AnyType._, AnySubsetType._

object WrapTestsContext {

  case object Color extends Wrap[String]("Color")
  object User extends Type("User")
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

class WrapTests extends org.scalatest.FunSuite {

  import WrapTestsContext._

  test("creating values") {

    val azul = Color denoteWith "blue"
    val verde = new ValueOf[Color.type]("green")
    val amarillo = Color denoteWith "yellow"

    assert{ azul.value == "blue" }
    assert{ verde.value == "green" }
    assert{ amarillo.value == "yellow" }
  }
}

class DenotationTests extends org.scalatest.FunSuite with ScalazEquality {
  import WrapTestsContext._

  test("create denotations") {
```

the right-associative syntax

```scala
    val uh: userInfo :%: User.type = userInfo(id = "adqwr32141", name = "Salustiano", age = 143) :%: User
    val z = User denoteWith 2423423
  }

  test("type-safe equals") {

    val paco = "Paco"
    val jose = "Jose"

    val u1 = paco :%: User
    val u1Again = paco :%: User

    val u2 = paco :%: Friend
    val v = jose :%: Friend

    assert { u1 == u1 }
    assert { u1 == u1Again }
    // assert { u2 =/= v } // not there in ScalaTest :-/
    // assert { u1 === u2 }
    assertTypeError("u1 === u2")
    assert{ !( u2 == v ) }
  }

  test("naive nonempty lists") {

    import WrapTestsContext._

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
        + [WrapTests.scala][test/scala/cosas/WrapTests.scala]
        + [RecordTests.scala][test/scala/cosas/RecordTests.scala]
        + [TypeSetTests.scala][test/scala/cosas/TypeSetTests.scala]
  + main
    + scala
      + cosas
        + [PropertiesHolder.scala][main/scala/cosas/PropertiesHolder.scala]
        + [Record.scala][main/scala/cosas/Record.scala]
        + ops
          + typeSet
            + [Check.scala][main/scala/cosas/ops/typeSet/Check.scala]
            + [Reorder.scala][main/scala/cosas/ops/typeSet/Reorder.scala]
            + [Conversions.scala][main/scala/cosas/ops/typeSet/Conversions.scala]
            + [AggregateProperties.scala][main/scala/cosas/ops/typeSet/AggregateProperties.scala]
            + [Subtract.scala][main/scala/cosas/ops/typeSet/Subtract.scala]
            + [Pop.scala][main/scala/cosas/ops/typeSet/Pop.scala]
            + [Representations.scala][main/scala/cosas/ops/typeSet/Representations.scala]
            + [Replace.scala][main/scala/cosas/ops/typeSet/Replace.scala]
            + [Take.scala][main/scala/cosas/ops/typeSet/Take.scala]
            + [Union.scala][main/scala/cosas/ops/typeSet/Union.scala]
            + [Mappers.scala][main/scala/cosas/ops/typeSet/Mappers.scala]
          + record
            + [Update.scala][main/scala/cosas/ops/record/Update.scala]
            + [Conversions.scala][main/scala/cosas/ops/record/Conversions.scala]
            + [Get.scala][main/scala/cosas/ops/record/Get.scala]
        + [TypeUnion.scala][main/scala/cosas/TypeUnion.scala]
        + [Fn.scala][main/scala/cosas/Fn.scala]
        + [Types.scala][main/scala/cosas/Types.scala]
        + csv
          + [csv.scala][main/scala/cosas/csv/csv.scala]
        + [Property.scala][main/scala/cosas/Property.scala]
        + [TypeSet.scala][main/scala/cosas/TypeSet.scala]

[test/scala/cosas/PropertyTests.scala]: PropertyTests.scala.md
[test/scala/cosas/TypeUnionTests.scala]: TypeUnionTests.scala.md
[test/scala/cosas/ScalazEquality.scala]: ScalazEquality.scala.md
[test/scala/cosas/WrapTests.scala]: WrapTests.scala.md
[test/scala/cosas/RecordTests.scala]: RecordTests.scala.md
[test/scala/cosas/TypeSetTests.scala]: TypeSetTests.scala.md
[main/scala/cosas/PropertiesHolder.scala]: ../../../main/scala/cosas/PropertiesHolder.scala.md
[main/scala/cosas/Record.scala]: ../../../main/scala/cosas/Record.scala.md
[main/scala/cosas/ops/typeSet/Check.scala]: ../../../main/scala/cosas/ops/typeSet/Check.scala.md
[main/scala/cosas/ops/typeSet/Reorder.scala]: ../../../main/scala/cosas/ops/typeSet/Reorder.scala.md
[main/scala/cosas/ops/typeSet/Conversions.scala]: ../../../main/scala/cosas/ops/typeSet/Conversions.scala.md
[main/scala/cosas/ops/typeSet/AggregateProperties.scala]: ../../../main/scala/cosas/ops/typeSet/AggregateProperties.scala.md
[main/scala/cosas/ops/typeSet/Subtract.scala]: ../../../main/scala/cosas/ops/typeSet/Subtract.scala.md
[main/scala/cosas/ops/typeSet/Pop.scala]: ../../../main/scala/cosas/ops/typeSet/Pop.scala.md
[main/scala/cosas/ops/typeSet/Representations.scala]: ../../../main/scala/cosas/ops/typeSet/Representations.scala.md
[main/scala/cosas/ops/typeSet/Replace.scala]: ../../../main/scala/cosas/ops/typeSet/Replace.scala.md
[main/scala/cosas/ops/typeSet/Take.scala]: ../../../main/scala/cosas/ops/typeSet/Take.scala.md
[main/scala/cosas/ops/typeSet/Union.scala]: ../../../main/scala/cosas/ops/typeSet/Union.scala.md
[main/scala/cosas/ops/typeSet/Mappers.scala]: ../../../main/scala/cosas/ops/typeSet/Mappers.scala.md
[main/scala/cosas/ops/record/Update.scala]: ../../../main/scala/cosas/ops/record/Update.scala.md
[main/scala/cosas/ops/record/Conversions.scala]: ../../../main/scala/cosas/ops/record/Conversions.scala.md
[main/scala/cosas/ops/record/Get.scala]: ../../../main/scala/cosas/ops/record/Get.scala.md
[main/scala/cosas/TypeUnion.scala]: ../../../main/scala/cosas/TypeUnion.scala.md
[main/scala/cosas/Fn.scala]: ../../../main/scala/cosas/Fn.scala.md
[main/scala/cosas/Types.scala]: ../../../main/scala/cosas/Types.scala.md
[main/scala/cosas/csv/csv.scala]: ../../../main/scala/cosas/csv/csv.scala.md
[main/scala/cosas/Property.scala]: ../../../main/scala/cosas/Property.scala.md
[main/scala/cosas/TypeSet.scala]: ../../../main/scala/cosas/TypeSet.scala.md