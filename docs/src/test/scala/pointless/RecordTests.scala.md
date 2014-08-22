
```scala
package ohnosequences.pointless.tests

import shapeless.test.{typed, illTyped}
import ohnosequences.pointless._, taggedType._, property._, typeSet._, record._

object RecordTestsContext {

  case object id extends Property[Integer]
  case object name extends Property[String]

  case object simpleUser extends Record(id :~: name :~: ∅)

  // more properties:
  case object email extends Property[String]
  case object color extends Property[String]

  case object normalUser extends Record(id :~: name :~: email :~: color :~: ∅)

  case class HasRecordWithId[
    Id <: AnyProperty, 
    R <: AnyRecord
  ](
    val id: Id,
    val r: R
  )(implicit
    val idIsThere: Id ∈ PropertiesOf[R],
    val getId: Lookup[RawOf[R], Tagged[Id]]
  )
  {

    def getId(entry: Tagged[R]): Tagged[Id] = entry get id
  }

  val vProps = email :~: color :~: ∅
  // nothing works with this
  val vRecord = new Record(email :~: color :~: ∅)

  val vEmail = "oh@buh.com"

  val vRecordEntry = vRecord =>> (
    (email is vEmail) :~:
    (color is "blue") :~:
    ∅
  )

  val hasRecordWithId = new HasRecordWithId(id, normalUser)

  // creating a record instance is easy and neat:
  val simpleUserEntry = simpleUser fields (
    (id is 123) :~: 
    (name is "foo") :~: 
    ∅
  )

  // this way the order of properties does not matter
  val normalUserEntry = normalUser fields (
    (name is "foo") :~: 
    (color is "orange") :~:
    (id is 123) :~: 
    (email is "foo@bar.qux") :~:
    ∅
  )

}

class RecordTests extends org.scalatest.FunSuite {

  import RecordTestsContext._

  test("recognizing record value types") {

    implicitly [∅ isRepresentedBy ∅]

    implicitly [
      // using external bounds
      (id.type :~: name.type :~: ∅) isRepresentedBy (Tagged[id.type] :~: Tagged[name.type] :~: ∅)
    ]

    // implicitly [
    //   // using the local Rep of each property
    //   (id.type :~: name.type :~: ∅) isRepresentedBy (id.Rep :~: name.Rep :~: ∅)
    // ] 

    implicitly [ 
      RawOf[simpleUser.type] =:= (Tagged[id.type] :~: Tagged[name.type] :~: ∅)
    ]

    implicitly [ 
      // check the Values alias
      simpleUser.Raw =:= (Tagged[id.type] :~: Tagged[name.type] :~: ∅)
    ]

    implicitly [
      simpleUser.representedProperties.Out =:= (Tagged[id.type] :~: Tagged[name.type] :~: ∅)
    ]
  }

  test("can provide properties in different order") {

    implicitly [ 
      // the declared property order
      simpleUser.Raw =:= (Tagged[id.type] :~: Tagged[name.type] :~: ∅)
    ]

    // they get reordered
    val simpleUserV: Tagged[simpleUser.type] = simpleUser fields {
      (name is "Antonio") :~:
      (id is 29681) :~: ∅
    }

    val sameSimpleUserV: Tagged[simpleUser.type] = simpleUser fields {
      (id is 29681) :~:
      (name is "Antonio") :~: ∅
    }

    assert {
      simpleUserV === sameSimpleUserV
    }
  }

  test("should fail when some properties are missing") {
    // you have to set _all_ properties
    assertTypeError("""
    val wrongAttrSet = simpleUser is (
      (id is 123) :~: ∅
    )
    """)

    // but you still have to present all properties:
    assertTypeError("""
    val wrongAttrSet = normalUser fields (
      (id is 123) :~:
      (name is "foo") :~: 
      ∅
    )
    """)
  }

  test("can access property values") {

    assert {

      (simpleUserEntry get id) === 123
    }

    assert {

      (simpleUserEntry get name) === "foo"
    }
  }

  test("can access property values from vals and volatile vals") {

    assert {

      (vRecordEntry get email) === "oh@buh.com"
    }
  }

  test("generic ops outside record work") {

    val uhoh = simpleUserEntry get id
  }

  test("recognize entries coming from different paths") {

    assert {
      (hasRecordWithId getId normalUserEntry) == ( normalUserEntry get id)
    }
  }

  test("can see a record entry as another") {

    val hey: Tagged[simpleUser.type] = normalUserEntry as simpleUser
  }

}

```


------

### Index

+ src
  + test
    + scala
      + pointless
        + [PropertyTests.scala][test/scala/pointless/PropertyTests.scala]
        + [RecordTests.scala][test/scala/pointless/RecordTests.scala]
        + [TypeSetTests.scala][test/scala/pointless/TypeSetTests.scala]
  + main
    + scala
      + pointless
        + [TaggedType.scala][main/scala/pointless/TaggedType.scala]
        + [Record.scala][main/scala/pointless/Record.scala]
        + ops
          + typeSet
            + [Lookup.scala][main/scala/pointless/ops/typeSet/Lookup.scala]
            + [Reorder.scala][main/scala/pointless/ops/typeSet/Reorder.scala]
            + [Conversions.scala][main/scala/pointless/ops/typeSet/Conversions.scala]
            + [Subtract.scala][main/scala/pointless/ops/typeSet/Subtract.scala]
            + [Pop.scala][main/scala/pointless/ops/typeSet/Pop.scala]
            + [Representations.scala][main/scala/pointless/ops/typeSet/Representations.scala]
            + [Replace.scala][main/scala/pointless/ops/typeSet/Replace.scala]
            + [Take.scala][main/scala/pointless/ops/typeSet/Take.scala]
            + [Union.scala][main/scala/pointless/ops/typeSet/Union.scala]
            + [Mappers.scala][main/scala/pointless/ops/typeSet/Mappers.scala]
          + record
            + [Update.scala][main/scala/pointless/ops/record/Update.scala]
            + [Conversions.scala][main/scala/pointless/ops/record/Conversions.scala]
            + [Get.scala][main/scala/pointless/ops/record/Get.scala]
        + [Denotation.scala][main/scala/pointless/Denotation.scala]
        + [TypeUnion.scala][main/scala/pointless/TypeUnion.scala]
        + [Fn.scala][main/scala/pointless/Fn.scala]
        + [Property.scala][main/scala/pointless/Property.scala]
        + [TypeSet.scala][main/scala/pointless/TypeSet.scala]

[test/scala/pointless/PropertyTests.scala]: PropertyTests.scala.md
[test/scala/pointless/RecordTests.scala]: RecordTests.scala.md
[test/scala/pointless/TypeSetTests.scala]: TypeSetTests.scala.md
[main/scala/pointless/TaggedType.scala]: ../../../main/scala/pointless/TaggedType.scala.md
[main/scala/pointless/Record.scala]: ../../../main/scala/pointless/Record.scala.md
[main/scala/pointless/ops/typeSet/Lookup.scala]: ../../../main/scala/pointless/ops/typeSet/Lookup.scala.md
[main/scala/pointless/ops/typeSet/Reorder.scala]: ../../../main/scala/pointless/ops/typeSet/Reorder.scala.md
[main/scala/pointless/ops/typeSet/Conversions.scala]: ../../../main/scala/pointless/ops/typeSet/Conversions.scala.md
[main/scala/pointless/ops/typeSet/Subtract.scala]: ../../../main/scala/pointless/ops/typeSet/Subtract.scala.md
[main/scala/pointless/ops/typeSet/Pop.scala]: ../../../main/scala/pointless/ops/typeSet/Pop.scala.md
[main/scala/pointless/ops/typeSet/Representations.scala]: ../../../main/scala/pointless/ops/typeSet/Representations.scala.md
[main/scala/pointless/ops/typeSet/Replace.scala]: ../../../main/scala/pointless/ops/typeSet/Replace.scala.md
[main/scala/pointless/ops/typeSet/Take.scala]: ../../../main/scala/pointless/ops/typeSet/Take.scala.md
[main/scala/pointless/ops/typeSet/Union.scala]: ../../../main/scala/pointless/ops/typeSet/Union.scala.md
[main/scala/pointless/ops/typeSet/Mappers.scala]: ../../../main/scala/pointless/ops/typeSet/Mappers.scala.md
[main/scala/pointless/ops/record/Update.scala]: ../../../main/scala/pointless/ops/record/Update.scala.md
[main/scala/pointless/ops/record/Conversions.scala]: ../../../main/scala/pointless/ops/record/Conversions.scala.md
[main/scala/pointless/ops/record/Get.scala]: ../../../main/scala/pointless/ops/record/Get.scala.md
[main/scala/pointless/Denotation.scala]: ../../../main/scala/pointless/Denotation.scala.md
[main/scala/pointless/TypeUnion.scala]: ../../../main/scala/pointless/TypeUnion.scala.md
[main/scala/pointless/Fn.scala]: ../../../main/scala/pointless/Fn.scala.md
[main/scala/pointless/Property.scala]: ../../../main/scala/pointless/Property.scala.md
[main/scala/pointless/TypeSet.scala]: ../../../main/scala/pointless/TypeSet.scala.md