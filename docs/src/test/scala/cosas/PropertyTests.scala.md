
```scala
package ohnosequences.cosas.test

import ohnosequences.cosas._, types._, properties._, typeSets._, propertyHolders._

object exampleProperties {
  
  case object key extends Property[String]("key")
  case object name extends Property[String]("name")
  case object age extends Property[Integer]("age")
  case object valueless extends Property("valueless")
}

class uhoh extends org.scalatest.FunSuite {
    
  import exampleProperties._

  test("create property instances") {

    val k: ValueOf[key.type] = key := "2aE5Cgo7Gv62"

    val n: ValueOf[name.type] = name := "Rigoberto Smith"

    val a = age := 13123
    val a0: ValueOf[age.type] = age := (13123: Integer)

    assertTypeError("""
      val z = key(34343)
    """)

    assertTypeError("""
      val uhoh = age(true)
    """)
  }

  test("valueless properties lead to nothing") {

    implicitly[valueless.Raw =:= Nothing]: Unit
  }

  test("having properties") {

    object foo
    implicit val foo_name = foo has name
    implicit val foo_age = foo has age

    implicitly[foo.type HasProperty name.type]
    implicitly[foo.type HasProperty age.type]

    implicitly[foo.type HasProperties ∅]
    implicitly[foo.type HasProperties (name.type :~: ∅)]
    implicitly[foo.type HasProperties (age.type :~: name.type :~: ∅)]

    implicit val foo_key = foo has key

    implicitly[foo.type HasProperty key.type]
    implicitly[foo.type HasProperties (key.type :~: ∅)]
    implicitly[foo.type HasProperties (age.type :~: key.type :~: ∅)]
    implicitly[foo.type HasProperties (age.type :~: key.type :~: name.type :~: ∅)]
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