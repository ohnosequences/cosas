
```scala
package ohnosequences.cosas.tests

class TypeUnionTests extends org.scalatest.FunSuite {


  import ohnosequences.cosas._, AnyTypeUnion._

  test("check arities") {

    type SBS = either[String]#or[Boolean]#or[String]

    type SBS2 = SBS

    type Three = arity[SBS]
    type Three2 = arity[SBS2]


    import shapeless._, Nat._
    
    implicitly[ Three =:= _3 ]
    implicitly[ Three2 =:= _3 ]
    implicitly[ SBS =:= SBS2 ]
  }

  test("check bounds") {

    type S = either[String]
    type SB = either[String]#or[Boolean]
    type SB2 = either[String] or Boolean
    type SBI = either[String] or Boolean or Int
    trait Bar
    type BarBIS = either[String] or Int or Boolean or Bar
    type Uh = Int :∨: Boolean :∨: String :∨: empty

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
    implicitly[just[Byte] <:!< Uh#union]
    implicitly[just[String] <:< SBI#union]

    implicitly[just[Bar] <:< BarBIS#union]
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