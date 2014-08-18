
```scala
package ohnosequences.typesets.tests

import shapeless._
import shapeless.test.{typed, illTyped}
import ohnosequences.typesets._

import org.scalatest.FunSuite

import AnyTag._


object RecordTestsContext {

  case object id extends Property[Integer]
  case object name extends Property[String]

  case object simpleUser extends Record(id :~: name :~: ?)

  // more properties:
  case object email extends Property[String]
  case object color extends Property[String]

  case object normalUser extends Record(id :~: name :~: email :~: color :~: ?)

  // nothing works with this
  val volatileUser = new Record(email :~: color :~: ?)

  val volatileUserEntry = volatileUser fields (

    (color is "orange") :~:
    (email is "foo@bar.qux") :~:
    ?
  )


  // creating a record instance is easy and neat:
  val simpleUserEntry = simpleUser ->> (
    (id ->> 123) :~: 
    (name ->> "foo") :~: 
    ?
  )

  // this way the order of properties does not matter
  val normalUserEntry = normalUser fields (
    (name is "foo") :~: 
    (color is "orange") :~:
    (id is 123) :~: 
    (email is "foo@bar.qux") :~:
    ?
  )

}

class RecordTests extends org.scalatest.FunSuite {

  import RecordTestsContext._

  test("shapeless witnesses work for properties") {

    val wid = implicitly[Witness.Aux[id.type]]
    typed[id.type](wid.value)
    typed[wid.T](id)
    implicitly[wid.T =:= id.type]
    implicitly[wid.value.Raw =:= Integer]
    assert(wid.value == id)
    
    val wname = implicitly[Witness.Aux[name.type]]


    val x = name ->> "foo"
    val y = implicitly[name.Rep => name.type]
    assert(y(x) == name)
  }

  test("recognizing record value types") {

    implicitly [

      ? isValuesOf ?
    ]

    implicitly [

      // using external bounds
      (TaggedWith[id.type] :~: TaggedWith[name.type] :~: ?) isValuesOf (id.type :~: name.type :~: ?)
    ]

    implicitly [

      // using the local Rep of each property
      (id.Rep :~: name.Rep :~: ?) isValuesOf (id.type :~: name.type :~: ?)
    ] 

    implicitly [ 

      simpleUser.Raw =:= (id.Rep :~: name.Rep :~: ?)
    ]

    implicitly [ 

      // check the Values alias
      simpleUser.Values =:= (id.Rep :~: name.Rep :~: ?)
    ]

    implicitly [

      simpleUser.representedProperties.Out =:= (id.Rep :~: name.Rep :~: ?)
    ]
  }

  test("can provide properties in different order") {

    implicitly [ 

      // the declared property order
      simpleUser.Values =:= (id.Rep :~: name.Rep :~: ?)
    ]

    // they get reordered
    val simpleUserV: simpleUser.Entry = simpleUser fields {

      (name is "Antonio") :~:
      (id is 29681) :~: ?
    }

    val sameSimpleUserV: simpleUser.Entry = simpleUser fields {

      (id is 29681) :~:
      (name is "Antonio") :~: ?
    }

    assert {

      simpleUserV === sameSimpleUserV
    }
  }

  test("should fail when some properties are missing") {
    // you have to set _all_ properties
    assertTypeError("""
    val wrongAttrSet = simpleUser ->> (
      (id ->> 123) :~: ?
    )
    """)

    // but you still have to present all properties:
    assertTypeError("""
    val wrongAttrSet = normalUser fields (
      (id ->> 123) :~:
      (name ->> "foo") :~: 
      ?
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

    // assert {

    //   true === false
    // }
  }

  test("generic ops outside record work") {


    // import AnyRecord._

    val uhoh = simpleUserEntry getIt id
  }

}

```


------

### Index

+ src
  + main
    + scala
      + items
        + [items.scala][main/scala/items/items.scala]
      + ops
        + [Choose.scala][main/scala/ops/Choose.scala]
        + [Lookup.scala][main/scala/ops/Lookup.scala]
        + [Map.scala][main/scala/ops/Map.scala]
        + [MapFold.scala][main/scala/ops/MapFold.scala]
        + [Pop.scala][main/scala/ops/Pop.scala]
        + [Reorder.scala][main/scala/ops/Reorder.scala]
        + [Replace.scala][main/scala/ops/Replace.scala]
        + [Subtract.scala][main/scala/ops/Subtract.scala]
        + [ToList.scala][main/scala/ops/ToList.scala]
        + [Union.scala][main/scala/ops/Union.scala]
      + [package.scala][main/scala/package.scala]
      + pointless
        + impl
      + [Property.scala][main/scala/Property.scala]
      + [Record.scala][main/scala/Record.scala]
      + [Representable.scala][main/scala/Representable.scala]
      + [TypeSet.scala][main/scala/TypeSet.scala]
      + [TypeUnion.scala][main/scala/TypeUnion.scala]
  + test
    + scala
      + items
        + [itemsTests.scala][test/scala/items/itemsTests.scala]
      + [RecordTests.scala][test/scala/RecordTests.scala]
      + [TypeSetTests.scala][test/scala/TypeSetTests.scala]

[main/scala/items/items.scala]: ../../main/scala/items/items.scala.md
[main/scala/ops/Choose.scala]: ../../main/scala/ops/Choose.scala.md
[main/scala/ops/Lookup.scala]: ../../main/scala/ops/Lookup.scala.md
[main/scala/ops/Map.scala]: ../../main/scala/ops/Map.scala.md
[main/scala/ops/MapFold.scala]: ../../main/scala/ops/MapFold.scala.md
[main/scala/ops/Pop.scala]: ../../main/scala/ops/Pop.scala.md
[main/scala/ops/Reorder.scala]: ../../main/scala/ops/Reorder.scala.md
[main/scala/ops/Replace.scala]: ../../main/scala/ops/Replace.scala.md
[main/scala/ops/Subtract.scala]: ../../main/scala/ops/Subtract.scala.md
[main/scala/ops/ToList.scala]: ../../main/scala/ops/ToList.scala.md
[main/scala/ops/Union.scala]: ../../main/scala/ops/Union.scala.md
[main/scala/package.scala]: ../../main/scala/package.scala.md
[main/scala/Property.scala]: ../../main/scala/Property.scala.md
[main/scala/Record.scala]: ../../main/scala/Record.scala.md
[main/scala/Representable.scala]: ../../main/scala/Representable.scala.md
[main/scala/TypeSet.scala]: ../../main/scala/TypeSet.scala.md
[main/scala/TypeUnion.scala]: ../../main/scala/TypeUnion.scala.md
[test/scala/items/itemsTests.scala]: items/itemsTests.scala.md
[test/scala/RecordTests.scala]: RecordTests.scala.md
[test/scala/TypeSetTests.scala]: TypeSetTests.scala.md