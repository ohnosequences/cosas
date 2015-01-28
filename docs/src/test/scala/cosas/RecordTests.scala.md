
```scala
package ohnosequences.cosas.tests

import ohnosequences.cosas._, propertyHolders._, types._, typeSets._, properties._, records._
import ops.typeSets._

object RecordTestsContext {

  case object id extends Property[Integer]("id")
  case object name extends Property[String]("name")
  case object notProperty

  case object simpleUser extends Record(id :~: name :~: ∅)

  // more properties:
  case object email extends Property[String]("email")
  case object color extends Property[String]("color")

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
  val simpleUserEntry = simpleUser(
    (id(123)) :~: 
    (name("foo")) :~: 
    ∅
  )
 
  // this way the order of properties does not matter
  val normalUserEntry = normalUser(
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

    assertTypeError("""
      val uhoh = Record(id :~: name :~: notProperty :~: ∅)
    """)
  }

  test("type level record length") {

    import shapeless.Nat._

    type Four = records.size[normalUser.type]

    implicitly [ Four =:= _4 ]
    implicitly [ records.size[simpleUser.type] =:= _2 ]
  }

  test("recognizing record value types") {

    implicitly [∅ areValuesOf ∅]

    implicitly [
      // using external bounds
      (ValueOf[id.type] :~: ValueOf[name.type] :~: ∅) areValuesOf (id.type :~: name.type :~: ∅)
    ]

    implicitly [ 
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
    val simpleUserV: ValueOf[simpleUser.type] = simpleUser{
      (name("Antonio")) :~:
      (id(29681)) :~: ∅
    }

    val sameSimpleUserV: ValueOf[simpleUser.type] = simpleUser{
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
    val wrongAttrSet = normalUser(
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
      (normalUser(
        (normalUserEntry get name) :~: 
        (normalUserEntry get id) :~: 
        (normalUserEntry get email) :~:
        (color("albero")) :~:
        ∅
      ))
    )

    assert(
      (normalUserEntry update ((name("bar")) :~: (id(321)) :~: ∅)) ==
      (normalUser(
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
      implicit def caseInteger[P <: AnyProperty with AnyType.withRaw[Integer]](p: P, m: Map[String, String]):
        (ValueOf[P], Map[String, String]) = (p(m(p.label).toInt), m)

      implicit def caseString[P <: AnyProperty with AnyType.withRaw[String]](p: P, m: Map[String, String]):
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
      implicit def caseInteger[P <: AnyProperty with AnyType.withRaw[Integer]](p: P, l: List[String]):
        (ValueOf[P], List[String]) = (p(l.head.toInt), l.tail)

      implicit def caseString[P <: AnyProperty with AnyType.withRaw[String]](p: P, l: List[String]):
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

    import spire.algebra.Monoid
    // Map //
    implicit def anyMapMonoid[X, Y]: Monoid[Map[X, Y]] = new Monoid[Map[X, Y]] {

      type M = Map[X,Y]

      def id: M = Map[X, Y]()
      def op(a: M, b: M): M = a ++ b
    }

    implicit def serializeProperty[P <: AnyProperty](t: ValueOf[P])
      (implicit getP: ValueOf[P] => P): Map[String, String] = Map(getP(t).label -> t.toString)

    // assert(
    //   normalUserEntry.serializeTo[Map[String, String]] == 
    //   Map(
    //     "name" -> "foo",
    //     "color" -> "orange",
    //     "id" -> "123",
    //     "email" -> "foo@bar.qux"
    //   )
    // )

    // List //
    implicit def anyListMonoid[X]: Monoid[List[X]] = new Monoid[List[X]] {

      type M = List[X]
      
      def id: M = List[X]()
      def op(a: M, b: M): M = a ++ b
    }

    implicit def propertyIntToStr[P <: AnyProperty](t: ValueOf[P])
      (implicit getP: ValueOf[P] => P): List[String] = List(getP(t).label + " -> " + t.toString)

    // implicit def toStr[P](p: P): List[String] = List(p.toString)

    // assert(
    //   normalUserEntry.serializeTo[List[String]] ==
    //   List("id -> 123", "name -> foo", "email -> foo@bar.qux", "color -> orange")
    // )

  }

}

```


------

### Index

+ src
  + test
    + scala
      + cosas
        + [SubsetTypesTests.scala][test/scala/cosas/SubsetTypesTests.scala]
        + [PropertyTests.scala][test/scala/cosas/PropertyTests.scala]
        + [TypeUnionTests.scala][test/scala/cosas/TypeUnionTests.scala]
        + [ScalazEquality.scala][test/scala/cosas/ScalazEquality.scala]
        + [EqualityTests.scala][test/scala/cosas/EqualityTests.scala]
        + [DenotationTests.scala][test/scala/cosas/DenotationTests.scala]
        + [RecordTests.scala][test/scala/cosas/RecordTests.scala]
        + [TypeSetTests.scala][test/scala/cosas/TypeSetTests.scala]
  + main
    + scala
      + cosas
        + [equality.scala][main/scala/cosas/equality.scala]
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
        + [subsetTypes.scala][main/scala/cosas/subsetTypes.scala]
        + [fns.scala][main/scala/cosas/fns.scala]
        + [propertyHolders.scala][main/scala/cosas/propertyHolders.scala]
        + [types.scala][main/scala/cosas/types.scala]

[test/scala/cosas/SubsetTypesTests.scala]: SubsetTypesTests.scala.md
[test/scala/cosas/PropertyTests.scala]: PropertyTests.scala.md
[test/scala/cosas/TypeUnionTests.scala]: TypeUnionTests.scala.md
[test/scala/cosas/ScalazEquality.scala]: ScalazEquality.scala.md
[test/scala/cosas/EqualityTests.scala]: EqualityTests.scala.md
[test/scala/cosas/DenotationTests.scala]: DenotationTests.scala.md
[test/scala/cosas/RecordTests.scala]: RecordTests.scala.md
[test/scala/cosas/TypeSetTests.scala]: TypeSetTests.scala.md
[main/scala/cosas/equality.scala]: ../../../main/scala/cosas/equality.scala.md
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
[main/scala/cosas/subsetTypes.scala]: ../../../main/scala/cosas/subsetTypes.scala.md
[main/scala/cosas/fns.scala]: ../../../main/scala/cosas/fns.scala.md
[main/scala/cosas/propertyHolders.scala]: ../../../main/scala/cosas/propertyHolders.scala.md
[main/scala/cosas/types.scala]: ../../../main/scala/cosas/types.scala.md