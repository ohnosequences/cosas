
```scala
package ohnosequences.cosas.tests

import ohnosequences.cosas._, types._, typeSets._, properties._, records._

object recordTestsContext {

  case object id    extends Property[Integer]("id")
  case object name  extends Property[String]("name")
  case object notProperty
  case object email extends Property[String]("email")
  case object color extends Property[String]("color")

  // funny square ins an option too
  case object simpleUser extends Record(id :&: name :&: □)
  case object normalUser extends Record(id :&: name :&: email :&: color :&: FNil)
  val vProps  = email :&: color :&: FNil
  val vRecord = new Record(vProps)
  val vEmail = "oh@buh.com"

  val vRecordEntry = vRecord (
    email(vEmail) :~:
    color("blue") :~:
    ∅
  )
  // creating a record instance is easy and neat:
  val simpleUserEntry = simpleUser (
    id(123)     :~:
    name("foo") :~:
    ∅
  )

  // this way the order of properties does not matter
  val normalUserEntry = normalUser (
    email("foo@bar.qux")  :~:
    id(123)               :~:
    color("orange")       :~:
    name("foo")           :~:
    ∅
  )
}


class RecordTests extends org.scalatest.FunSuite {

  import recordTestsContext._

  test("should fail when some properties are missing") {
    // you have to set _all_ properties
    assertTypeError("""
    val wrongAttrSet = simpleUser(id(123) :~: ∅)
    """)

    // but you still have to present all properties:
    assertTypeError("""
    val wrongAttrSet = normalUser(
      id(123)     :~:
      name("foo") :~:
      ∅
    )
    """)
  }

  test("can access fields") {

    assert { (simpleUserEntry get id)   === id(123)     }
    assert { (simpleUserEntry get name) === name("foo") }
  }

  test("can access fields from vals and volatile vals") {

    assert{ (vRecordEntry get email) === email("oh@buh.com") }
  }

  test("can update fields") {

    assert {

      ( normalUserEntry update color("albero") ) === normalUser (
        (normalUserEntry get id)    :~:
        (normalUserEntry get name)  :~:
        (normalUserEntry get email) :~:
        color("albero")             :~:
        ∅
      )
    }

    assert {

      ( normalUserEntry update name("bar") :~: id(321) :~: ∅ ) === normalUser (
          id(321)               :~:
          name("bar")           :~:
          email("foo@bar.qux")  :~:
          color("orange")       :~:
          ∅
        )
    }
  }

  test("can see a record entry as another") {

    assert { normalUserEntry === ( simpleUserEntry as (normalUser, email("foo@bar.qux") :~: color("orange") :~: ∅) ) }
  }

  test("can provide properties in different order") {

    // the declared property order
    implicitly [
      simpleUser.Raw =:= (ValueOf[id.type] :~: ValueOf[name.type] :~: ∅)
    ]

    // they get reordered
    val simpleUserV: ValueOf[simpleUser.type] = simpleUser {
      name("Antonio") :~:
      id(29681)       :~: ∅
    }

    val sameSimpleUserV: ValueOf[simpleUser.type] = simpleUser {
      id(29681)       :~:
      name("Antonio") :~: ∅
    }

    assert { simpleUserV == sameSimpleUserV }
  }

  test("can check if record has properties") {

    implicitly[simpleUser.Fields HasProperties (id.type :~: name.type :~: ∅)]
    implicitly[simpleUser.Fields HasProperties (name.type :~: id.type :~: ∅)]
    implicitly[simpleUser.Fields HasProperties (name.type :~: ∅)]
    implicitly[simpleUser.Fields HasProperties (id.type :~: ∅)]

    implicitly[simpleUser.Fields HasProperty name.type]
    implicitly[simpleUser.Fields HasProperty id.type]

    assertTypeError { """implicitly[simpleUser.Fields HasProperties (email.type :~: id.type :~: ∅)]""" }
    assertTypeError { """implicitly[simpleUser.Fields HasProperties (email.type :~: name.type :~: color.type :~: ∅)]""" }
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