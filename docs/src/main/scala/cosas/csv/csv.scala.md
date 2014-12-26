
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

import types._, typeSets._, properties._
import scala.collection.GenTraversable
import shapeless.Nat._
import shapeless.ops.nat.ToInt

// TODO: E should be a type member
trait WTraversable[E] extends Wrap[GenTraversable[E]]
trait AnyRow[E] extends SubsetType[WTraversable[E]] {

  val label = "row"
  type Size <: Nat
  val sizeAsInt: ToInt[Size]
  def predicate(l: GenTraversable[E]) = toInt[Size](sizeAsInt) == l.size
}
class row[E, N <: Nat](implicit val sizeAsInt: ToInt[N]) extends AnyRow[E] {

  type Size = N
}

trait AnyHeader {

  type Properties <: AnyTypeSet.Of[AnyProperty]
}
class header[Ps <: AnyTypeSet.Of[AnyProperty]] extends AnyHeader { type Properties = Ps }

trait csv[E] {

  type Header <: AnyHeader
  type Row <: AnyRow[E] // { type Size = typeSets.size[Header#Properties] }
}
```


From here

- parsing is done by taking first property and first value, and requiring an implicit E => P#Raw (Or Option if you're feeling fancy); you should get a record entry for each row
- serializing is dual


```scala
object example {

  object buh extends row[String, _1]
  object id extends Property[String]("id")
  object header extends header[id.type :~: ∅]

  object myCSV extends csv[String] {

    type Header = header.type
    type Row = buh.type
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

[test/scala/cosas/PropertyTests.scala]: ../../../../test/scala/cosas/PropertyTests.scala.md
[test/scala/cosas/TypeUnionTests.scala]: ../../../../test/scala/cosas/TypeUnionTests.scala.md
[test/scala/cosas/ScalazEquality.scala]: ../../../../test/scala/cosas/ScalazEquality.scala.md
[test/scala/cosas/DenotationTests.scala]: ../../../../test/scala/cosas/DenotationTests.scala.md
[test/scala/cosas/RecordTests.scala]: ../../../../test/scala/cosas/RecordTests.scala.md
[test/scala/cosas/TypeSetTests.scala]: ../../../../test/scala/cosas/TypeSetTests.scala.md
[main/scala/cosas/properties.scala]: ../properties.scala.md
[main/scala/cosas/typeSets.scala]: ../typeSets.scala.md
[main/scala/cosas/ops/records/Update.scala]: ../ops/records/Update.scala.md
[main/scala/cosas/ops/records/Conversions.scala]: ../ops/records/Conversions.scala.md
[main/scala/cosas/ops/records/Get.scala]: ../ops/records/Get.scala.md
[main/scala/cosas/ops/typeSets/Filter.scala]: ../ops/typeSets/Filter.scala.md
[main/scala/cosas/ops/typeSets/Reorder.scala]: ../ops/typeSets/Reorder.scala.md
[main/scala/cosas/ops/typeSets/Conversions.scala]: ../ops/typeSets/Conversions.scala.md
[main/scala/cosas/ops/typeSets/AggregateProperties.scala]: ../ops/typeSets/AggregateProperties.scala.md
[main/scala/cosas/ops/typeSets/Subtract.scala]: ../ops/typeSets/Subtract.scala.md
[main/scala/cosas/ops/typeSets/Pop.scala]: ../ops/typeSets/Pop.scala.md
[main/scala/cosas/ops/typeSets/Representations.scala]: ../ops/typeSets/Representations.scala.md
[main/scala/cosas/ops/typeSets/Replace.scala]: ../ops/typeSets/Replace.scala.md
[main/scala/cosas/ops/typeSets/Take.scala]: ../ops/typeSets/Take.scala.md
[main/scala/cosas/ops/typeSets/Union.scala]: ../ops/typeSets/Union.scala.md
[main/scala/cosas/ops/typeSets/Mappers.scala]: ../ops/typeSets/Mappers.scala.md
[main/scala/cosas/typeUnions.scala]: ../typeUnions.scala.md
[main/scala/cosas/records.scala]: ../records.scala.md
[main/scala/cosas/csv/csv.scala]: csv.scala.md
[main/scala/cosas/fns.scala]: ../fns.scala.md
[main/scala/cosas/propertyHolders.scala]: ../propertyHolders.scala.md
[main/scala/cosas/types.scala]: ../types.scala.md