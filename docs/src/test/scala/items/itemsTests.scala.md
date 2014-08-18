
```scala
// package ohnosequences.typesets.items

// import ohnosequences.typesets._
// import shapeless.FieldOf
// import sampleFields._

// object sampleFields {

//   object intField1 extends FieldOf[Int]
//   object intField2 extends FieldOf[Int]
//   object stringField1 extends FieldOf[String]
//   object stringField2 extends FieldOf[String]
//   object boolField1 extends FieldOf[Boolean]
//   object boolField2 extends FieldOf[Boolean]
//   object doubleField1 extends FieldOf[Double]
//   object doubleField2 extends FieldOf[Double]

//   def typed[T](t : => T) {}
// }

// class ItemTestsTypeSetSyntax extends org.scalatest.FunSuite {

//   import experiment.TypeSetSyntax._

//   test("no duplicated fields") {

//     // val notOK =
//     //   (intField1    ->>    23) :~:
//     //   (stringField1 ->> "foo") :~:
//     //   (boolField1   ->>  true) :~:
//     //   (intField1    ->>    43) :~:
//     //   (doubleField1 ->>   2.0) :~:
//     //   ?
//   }

//   test("get values") {


//     val r1 =
//       (intField1    ->>    23) :~:
//       (stringField1 ->> "foo") :~:
//       (boolField1   ->>  true) :~:
//       (doubleField1 ->>   2.0) :~:
//       ?
    
//     val v1 = r1.get(intField1)
//     typed[Int](v1)
//     assert(23 === v1)

//     val v2 = r1.get(stringField1)
//     typed[String](v2)
//     assert("foo" === v2)
//   }
// }

// class FunnyHListItemsTests extends org.scalatest.FunSuite {

//   import shapeless.{::, HNil}
//   import experiment.HListSyntax._

//    def typed[T](t : => T) {}

//   test("no duplicated fields") {

//     // val notOK =
//     //   (intField1    ->>    23) :~:
//     //   (stringField1 ->> "foo") :~:
//     //   (boolField1   ->>  true) :~:
//     //   (intField1    ->>    43) :~:
//     //   (doubleField1 ->>   2.0) :~:
//     //   ?


//   }

//   test("get values") {
//     val r1 =
//       (intField1    ->>    23) ::
//       (stringField1 ->> "foo") ::
//       (boolField1   ->>  true) ::
//       (doubleField1 ->>   2.0) ::
//       HNil
    
//     val v1 = r1.get(intField1)
//     typed[Int](v1)
//     assert(23 === v1)

//   }

// }



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

[main/scala/items/items.scala]: ../../../main/scala/items/items.scala.md
[main/scala/ops/Choose.scala]: ../../../main/scala/ops/Choose.scala.md
[main/scala/ops/Lookup.scala]: ../../../main/scala/ops/Lookup.scala.md
[main/scala/ops/Map.scala]: ../../../main/scala/ops/Map.scala.md
[main/scala/ops/MapFold.scala]: ../../../main/scala/ops/MapFold.scala.md
[main/scala/ops/Pop.scala]: ../../../main/scala/ops/Pop.scala.md
[main/scala/ops/Reorder.scala]: ../../../main/scala/ops/Reorder.scala.md
[main/scala/ops/Replace.scala]: ../../../main/scala/ops/Replace.scala.md
[main/scala/ops/Subtract.scala]: ../../../main/scala/ops/Subtract.scala.md
[main/scala/ops/ToList.scala]: ../../../main/scala/ops/ToList.scala.md
[main/scala/ops/Union.scala]: ../../../main/scala/ops/Union.scala.md
[main/scala/package.scala]: ../../../main/scala/package.scala.md
[main/scala/Property.scala]: ../../../main/scala/Property.scala.md
[main/scala/Record.scala]: ../../../main/scala/Record.scala.md
[main/scala/Representable.scala]: ../../../main/scala/Representable.scala.md
[main/scala/TypeSet.scala]: ../../../main/scala/TypeSet.scala.md
[main/scala/TypeUnion.scala]: ../../../main/scala/TypeUnion.scala.md
[test/scala/items/itemsTests.scala]: itemsTests.scala.md
[test/scala/RecordTests.scala]: ../RecordTests.scala.md
[test/scala/TypeSetTests.scala]: ../TypeSetTests.scala.md