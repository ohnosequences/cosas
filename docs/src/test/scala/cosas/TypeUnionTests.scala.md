
```scala
package ohnosequences.cosas.tests

import ohnosequences.cosas._, typeUnions._


class TypeUnionTests extends org.scalatest.FunSuite {

  test("check bounds") {

    type S      = either[String]
    type SB     = either[String] or Boolean
    type SB2    = either[String] or Boolean
    type SBI    = either[String] or Boolean or Int
    trait Bar
    type BarBIS = either[String] or Int or Boolean or Bar
    type Uh     = either[Byte] or Int or Boolean or String

    implicitly[just[String] <:< Uh#union]
    implicitly[just[Boolean] <:< Uh#union]
    implicitly[just[Int] <:< Uh#union]

    implicitly[S#union =:= just[String]]

    implicitly[just[String] <:< S#union]
    implicitly[just[Boolean] <:< SB#union]
    implicitly[just[String] <:< SB#union]
    implicitly[just[Boolean] <:< SB2#union]
    implicitly[just[String] <:< SB2#union]

    implicitly[just[String] <:< SBI#union]
    implicitly[just[Boolean] <:< SBI#union]
    implicitly[just[Int] <:< SBI#union]

    import shapeless.{ <:!< }
    implicitly[just[Byte] <:!< SBI#union]
    implicitly[just[Byte] <:< Uh#union]
    implicitly[just[String] <:< SBI#union]

    implicitly[just[Bar] <:< BarBIS#union]
  }

  test("bounds with subtyping") {

    // weird issues
    trait Animal
    val animal = new Animal {}
    trait Cat extends Animal
    trait UglyCat extends Cat
    object pipo extends UglyCat
    val uglyCat = new UglyCat {}
    trait Dog extends Animal

    // everyone fits here
    type DCA = either[Dog] or Cat or Animal
    implicitly[Dog isOneOf DCA]
    implicitly[Cat isOneOf DCA]
    implicitly[UglyCat isOneOf DCA]
    implicitly[pipo.type isOneOf DCA]
    implicitly[uglyCat.type isOneOf DCA]

    type DC = either[Dog] or Cat
    implicitly[Dog isOneOf DC]
    implicitly[Cat isOneOf DC]
    implicitly[UglyCat isOneOf DC]
    implicitly[pipo.type isOneOf DC]
    implicitly[uglyCat.type isOneOf DC]
    // not here
    implicitly[animal.type isNotOneOf DC]

    type DUC = either[Dog] or UglyCat
    implicitly[Dog isOneOf DUC]
    implicitly[UglyCat isOneOf DUC]
    implicitly[pipo.type isOneOf DUC]
    implicitly[uglyCat.type isOneOf DUC]
    // not here
    implicitly[Cat isNotOneOf DUC]
    implicitly[animal.type isNotOneOf DUC]

    type ISDUC = either[String] or Int or Dog or UglyCat
    type DUCIS = either[Dog] or UglyCat or String or Int
    implicitly[Dog isOneOf ISDUC]
    implicitly[UglyCat isOneOf ISDUC]
    implicitly[pipo.type isOneOf ISDUC]
    implicitly[uglyCat.type isOneOf ISDUC]
    
    implicitly[Cat isNotOneOf ISDUC]
    implicitly[animal.type isNotOneOf ISDUC]
    implicitly[Cat isNotOneOf DUCIS]
    implicitly[animal.type isNotOneOf DUCIS]
    implicitly[Byte isNotOneOf ISDUC]
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