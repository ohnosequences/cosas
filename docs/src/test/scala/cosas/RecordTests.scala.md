
```scala
package ohnosequences.cosas.tests

import shapeless.test.{typed, illTyped}
import ohnosequences.cosas._
import AnyWrap._, AnyProperty._, AnyTypeSet._, AnyRecord._, AnyTypeUnion._
import ops.typeSet._

object RecordTestsContext {

  case object id extends Property[Integer]
  case object name extends Property[String]
  case object notProperty

  case object simpleUser extends Record(id :~: name :~: ∅)

  // more properties:
  case object email extends Property[String]
  case object color extends Property[String]

  case object normalUser extends Record(id :~: name :~: email :~: color :~: ∅)

  val vProps = email :~: color :~: ∅
  // nothing works with this
  val vRecord = new Record(email :~: color :~: ∅)

  val vEmail = "oh@buh.com"

  val vRecordEntry = vRecord(
    (email(vEmail)) :~:
    (color("blue")) :~:
    ∅
  )

  // val hasRecordWithId = new HasRecordWithId(id, normalUser)

  // creating a record instance is easy and neat:
  val simpleUserEntry = simpleUser fields (
    (id(123)) :~: 
    (name("foo")) :~: 
    ∅
  )

  // this way the order of properties does not matter
  val normalUserEntry = normalUser fields (
    (name("foo")) :~: 
    (color("orange")) :~:
    (id(123)) :~: 
    (email("foo@bar.qux")) :~:
    ∅
  )

}

class RecordTests extends org.scalatest.FunSuite {

  import RecordTestsContext._

  test("record property bound works") {

    illTyped("""

      val uhoh = Record(id :~: name :~: notProperty :~: ∅)
    """)
  }

  test("recognizing record value types") {

    implicitly [∅ areValuesOf ∅]

    implicitly [
      // using external bounds
      (ValueOf[id.type] :~: ValueOf[name.type] :~: ∅) areValuesOf (id.type :~: name.type :~: ∅)
    ]

    implicitly [ 
      RawOf[simpleUser.type] =:= (ValueOf[id.type] :~: ValueOf[name.type] :~: ∅)
    ]

    implicitly [ 
      // check the Values alias
      simpleUser.Raw =:= (ValueOf[id.type] :~: ValueOf[name.type] :~: ∅)
    ]

    implicitly [
      simpleUser.valuesOfProperties.Out =:= (ValueOf[id.type] :~: ValueOf[name.type] :~: ∅)
    ]
  }

  test("can provide properties in different order") {

    implicitly [ 
      // the declared property order
      simpleUser.Raw =:= (ValueOf[id.type] :~: ValueOf[name.type] :~: ∅)
    ]

    // they get reordered
    val simpleUserV: ValueOf[simpleUser.type] = simpleUser fields {
      (name("Antonio")) :~:
      (id(29681)) :~: ∅
    }

    val sameSimpleUserV: ValueOf[simpleUser.type] = simpleUser fields {
      (id(29681)) :~:
      (name("Antonio")) :~: ∅
    }

    assert {
      simpleUserV == sameSimpleUserV
    }
  }

  test("should fail when some properties are missing") {
    // you have to set _all_ properties
    assertTypeError("""
    val wrongAttrSet = simpleUser(id(123) :~: ∅)
    """)

    // but you still have to present all properties:
    assertTypeError("""
    val wrongAttrSet = normalUser fields (
      id(123) :~:
      name("foo") :~: 
      ∅
    )
    """)
  }

  test("can access property values") {

    assert{ (simpleUserEntry get id) == id(123) }
    assert{ (simpleUserEntry get name) == name("foo") }
  }

  test("can access property values from vals and volatile vals") {

    assert{ (vRecordEntry get email) == email("oh@buh.com") }
  }

  test("can see a record entry as another") {

    val hey: ValueOf[simpleUser.type] = normalUserEntry as simpleUser
  }

  test("update fields") {

    assert(
      (normalUserEntry update (color("albero"))) ==
      (normalUser fields (
        (normalUserEntry get name) :~: 
        (normalUserEntry get id) :~: 
        (normalUserEntry get email) :~:
        (color("albero")) :~:
        ∅
      ))
    )

    assert(
      (normalUserEntry update ((name("bar")) :~: (id(321)) :~: ∅)) ==
      (normalUser fields (
        (name("bar")) :~: 
        (color("orange")) :~:
        (id(321)) :~: 
        (email("foo@bar.qux")) :~:
        ∅
      ))
    )

  }

  test("having properties") {

    implicitly[simpleUser.type HasProperties (id.type :~: name.type :~: ∅)]
    implicitly[simpleUser.type HasProperties (name.type :~: id.type :~: ∅)]
    implicitly[simpleUser.type HasProperties (name.type :~: ∅)]
    implicitly[simpleUser.type HasProperties (id.type :~: ∅)]

    implicitly[simpleUser.type HasProperty name.type]
    implicitly[simpleUser.type HasProperty id.type]

    // adding some moar properties
    implicit val useremail = simpleUser has email
    implicit val usercolor = simpleUser has color

    implicitly[simpleUser.type HasProperties (email.type :~: id.type :~: ∅)]
    implicitly[simpleUser.type HasProperties (email.type :~: name.type :~: color.type :~: ∅)]
  }

  test("parsing") {
    // Map parser get's values from map by key, which is the property label
    object MapParser {
      implicit def caseInteger[P <: AnyProperty with AnyWrap.withRaw[Integer]](p: P, m: Map[String, String]):
        (ValueOf[P], Map[String, String]) = (p(m(p.label).toInt), m)

      implicit def caseString[P <: AnyProperty with AnyWrap.withRaw[String]](p: P, m: Map[String, String]):
        (ValueOf[P], Map[String, String]) = (p(m(p.label).toString), m)
    }

    assertResult(normalUserEntry) {
      import MapParser._

      normalUser parseFrom Map(
        "name" -> "foo",
        "color" -> "orange",
        "id" -> "123",
        "email" -> "foo@bar.qux"
      )
    }

    // List parser just takes the values sequentially, so the order must correspond the order of properties
    object ListParser {
      implicit def caseInteger[P <: AnyProperty with AnyWrap.withRaw[Integer]](p: P, l: List[String]):
        (ValueOf[P], List[String]) = (p(l.head.toInt), l.tail)

      implicit def caseString[P <: AnyProperty with AnyWrap.withRaw[String]](p: P, l: List[String]):
        (ValueOf[P], List[String]) = (p(l.head.toString), l.tail)
    }

    assertResult(normalUserEntry) {
      import ListParser._

      normalUser parseFrom List(
        "123",
        "foo",
        "foo@bar.qux",
        "orange"
      )
    }

  }

  test("serialize") {
    // Map //
    implicit def anyMapMonoid[X, Y]: Monoid[Map[X, Y]] = new Monoid[Map[X, Y]] {
      def zero: M = Map[X, Y]()
      def append(a: M, b: M): M = a ++ b
    }

    implicit def serializeProperty[P <: AnyProperty](t: ValueOf[P])
      (implicit getP: ValueOf[P] => P): Map[String, String] = Map(getP(t).label -> t.toString)

    assert(
      normalUserEntry.serializeTo[Map[String, String]] == 
      Map(
        "name" -> "foo",
        "color" -> "orange",
        "id" -> "123",
        "email" -> "foo@bar.qux"
      )
    )

    // List //
    implicit def anyListMonoid[X]: Monoid[List[X]] = new Monoid[List[X]] {
      def zero: M = List[X]()
      def append(a: M, b: M): M = a ++ b
    }

    implicit def propertyIntToStr[P <: AnyProperty](t: ValueOf[P])
      (implicit getP: ValueOf[P] => P): List[String] = List(getP(t).label + " -> " + t.toString)

    // implicit def toStr[P](p: P): List[String] = List(p.toString)

    assert(
      normalUserEntry.serializeTo[List[String]] ==
      List("id -> 123", "name -> foo", "email -> foo@bar.qux", "color -> orange")
    )

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
        + [WrapTests.scala][test/scala/cosas/WrapTests.scala]
        + [RecordTests.scala][test/scala/cosas/RecordTests.scala]
        + [TypeSetTests.scala][test/scala/cosas/TypeSetTests.scala]
  + main
    + scala
      + cosas
        + [Wrap.scala][main/scala/cosas/Wrap.scala]
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
        + [Denotation.scala][main/scala/cosas/Denotation.scala]
        + [TypeUnion.scala][main/scala/cosas/TypeUnion.scala]
        + [Fn.scala][main/scala/cosas/Fn.scala]
        + [Property.scala][main/scala/cosas/Property.scala]
        + [TypeSet.scala][main/scala/cosas/TypeSet.scala]

[test/scala/cosas/PropertyTests.scala]: PropertyTests.scala.md
[test/scala/cosas/WrapTests.scala]: WrapTests.scala.md
[test/scala/cosas/RecordTests.scala]: RecordTests.scala.md
[test/scala/cosas/TypeSetTests.scala]: TypeSetTests.scala.md
[main/scala/cosas/Wrap.scala]: ../../../main/scala/cosas/Wrap.scala.md
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
[main/scala/cosas/Denotation.scala]: ../../../main/scala/cosas/Denotation.scala.md
[main/scala/cosas/TypeUnion.scala]: ../../../main/scala/cosas/TypeUnion.scala.md
[main/scala/cosas/Fn.scala]: ../../../main/scala/cosas/Fn.scala.md
[main/scala/cosas/Property.scala]: ../../../main/scala/cosas/Property.scala.md
[main/scala/cosas/TypeSet.scala]: ../../../main/scala/cosas/TypeSet.scala.md