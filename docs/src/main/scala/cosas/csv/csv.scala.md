
```scala
package ohnosequences.cosas.csv

import ohnosequences.cosas._

import shapeless._
```


## csv

If one looks at `Sized` what we have is

- a predicate on `GenTraversableLike`: check wether the length of it equals a value. This is indexed by a type (`Nat` in this case) so that we have `check(l: GenTraversableLike): Boolean`; this check is as I said indexed by `Nat` but this is not so important in general.
- a value class wrapping something `GenTraversableLike` whatever; you can only get instances of it _if_ that check works; so in the end is something like `Option[ValueOf[T]]` where `T` is a _static_ type (either an object of a sealed class indexed by `Nat` in this case) which contains the `check` we mentioned above. Of course in practice one needs an ops-private variant which does not require the check; see next point
- You use then values of this type happily, and can write ops that return `ValueOf` for different static types (like concat that would return its sum etc)

#### parsing, serializing

- A record can be mapped to a CSV row with header derived from the record; just map over the properties and call its FQN (for example)
- Given a record you can try to parse it from a Row. For this you need a function from this record and the value to (an option of) Value[Record]. Something like `Row => Value[Record]` should work.

I think that a generic Poly acting over a Map and requiring a conversion from P to Option[P#Raw] should be enough.


```scala
// object parsing {

//   type Parse[P]     =         P   => Option[ValueOf[P]]
//   type Serialize[P] = ValueOf[P]  => P#Raw
// }

// trait AnyHeader {

//   type Props <: AnyTypeSet.Of[AnyProperty]
// }

// trait AnyRow extends AnyWrap {

//   type Header <: AnyHeader

//   type Raw = Map[String,String]
// }

// class Row[H <: AnyHeader](val header: H) extends AnyRow {

//   type Header = H
// }




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

[test/scala/cosas/PropertyTests.scala]: ../../../../test/scala/cosas/PropertyTests.scala.md
[test/scala/cosas/TypeUnionTests.scala]: ../../../../test/scala/cosas/TypeUnionTests.scala.md
[test/scala/cosas/ScalazEquality.scala]: ../../../../test/scala/cosas/ScalazEquality.scala.md
[test/scala/cosas/WrapTests.scala]: ../../../../test/scala/cosas/WrapTests.scala.md
[test/scala/cosas/RecordTests.scala]: ../../../../test/scala/cosas/RecordTests.scala.md
[test/scala/cosas/TypeSetTests.scala]: ../../../../test/scala/cosas/TypeSetTests.scala.md
[main/scala/cosas/PropertiesHolder.scala]: ../PropertiesHolder.scala.md
[main/scala/cosas/Record.scala]: ../Record.scala.md
[main/scala/cosas/ops/typeSet/Check.scala]: ../ops/typeSet/Check.scala.md
[main/scala/cosas/ops/typeSet/Reorder.scala]: ../ops/typeSet/Reorder.scala.md
[main/scala/cosas/ops/typeSet/Conversions.scala]: ../ops/typeSet/Conversions.scala.md
[main/scala/cosas/ops/typeSet/AggregateProperties.scala]: ../ops/typeSet/AggregateProperties.scala.md
[main/scala/cosas/ops/typeSet/Subtract.scala]: ../ops/typeSet/Subtract.scala.md
[main/scala/cosas/ops/typeSet/Pop.scala]: ../ops/typeSet/Pop.scala.md
[main/scala/cosas/ops/typeSet/Representations.scala]: ../ops/typeSet/Representations.scala.md
[main/scala/cosas/ops/typeSet/Replace.scala]: ../ops/typeSet/Replace.scala.md
[main/scala/cosas/ops/typeSet/Take.scala]: ../ops/typeSet/Take.scala.md
[main/scala/cosas/ops/typeSet/Union.scala]: ../ops/typeSet/Union.scala.md
[main/scala/cosas/ops/typeSet/Mappers.scala]: ../ops/typeSet/Mappers.scala.md
[main/scala/cosas/ops/record/Update.scala]: ../ops/record/Update.scala.md
[main/scala/cosas/ops/record/Conversions.scala]: ../ops/record/Conversions.scala.md
[main/scala/cosas/ops/record/Get.scala]: ../ops/record/Get.scala.md
[main/scala/cosas/TypeUnion.scala]: ../TypeUnion.scala.md
[main/scala/cosas/Fn.scala]: ../Fn.scala.md
[main/scala/cosas/Types.scala]: ../Types.scala.md
[main/scala/cosas/csv/csv.scala]: csv.scala.md
[main/scala/cosas/Property.scala]: ../Property.scala.md
[main/scala/cosas/TypeSet.scala]: ../TypeSet.scala.md