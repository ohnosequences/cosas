
## Popping an element from a set

It's like `Lookup`, but it removes the element



```scala
package ohnosequences.cosas.ops.record

import ohnosequences.cosas._
import AnyFn._, AnyType._, AnyTypeSet._, AnyRecord._
import ops.typeSet._

@annotation.implicitNotFound(msg = "Can't get property ${P} of the record ${R}")
trait Get[R <: AnyRecord, P <: AnyProperty] 
  extends Fn1[RawOf[R]] with Out[ValueOf[P]]

object Get {

  implicit def getter[R <: AnyRecord, P <: AnyProperty]
    (implicit 
      lookup: RawOf[R] Lookup ValueOf[P]
    ):  Get[R, P] = 
    new Get[R, P] { def apply(recRaw: RawOf[R]): Out = lookup(recRaw) }

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

[test/scala/cosas/PropertyTests.scala]: ../../../../../test/scala/cosas/PropertyTests.scala.md
[test/scala/cosas/TypeUnionTests.scala]: ../../../../../test/scala/cosas/TypeUnionTests.scala.md
[test/scala/cosas/ScalazEquality.scala]: ../../../../../test/scala/cosas/ScalazEquality.scala.md
[test/scala/cosas/WrapTests.scala]: ../../../../../test/scala/cosas/WrapTests.scala.md
[test/scala/cosas/RecordTests.scala]: ../../../../../test/scala/cosas/RecordTests.scala.md
[test/scala/cosas/TypeSetTests.scala]: ../../../../../test/scala/cosas/TypeSetTests.scala.md
[main/scala/cosas/PropertiesHolder.scala]: ../../PropertiesHolder.scala.md
[main/scala/cosas/Record.scala]: ../../Record.scala.md
[main/scala/cosas/ops/typeSet/Check.scala]: ../typeSet/Check.scala.md
[main/scala/cosas/ops/typeSet/Reorder.scala]: ../typeSet/Reorder.scala.md
[main/scala/cosas/ops/typeSet/Conversions.scala]: ../typeSet/Conversions.scala.md
[main/scala/cosas/ops/typeSet/AggregateProperties.scala]: ../typeSet/AggregateProperties.scala.md
[main/scala/cosas/ops/typeSet/Subtract.scala]: ../typeSet/Subtract.scala.md
[main/scala/cosas/ops/typeSet/Pop.scala]: ../typeSet/Pop.scala.md
[main/scala/cosas/ops/typeSet/Representations.scala]: ../typeSet/Representations.scala.md
[main/scala/cosas/ops/typeSet/Replace.scala]: ../typeSet/Replace.scala.md
[main/scala/cosas/ops/typeSet/Take.scala]: ../typeSet/Take.scala.md
[main/scala/cosas/ops/typeSet/Union.scala]: ../typeSet/Union.scala.md
[main/scala/cosas/ops/typeSet/Mappers.scala]: ../typeSet/Mappers.scala.md
[main/scala/cosas/ops/record/Update.scala]: Update.scala.md
[main/scala/cosas/ops/record/Conversions.scala]: Conversions.scala.md
[main/scala/cosas/ops/record/Get.scala]: Get.scala.md
[main/scala/cosas/TypeUnion.scala]: ../../TypeUnion.scala.md
[main/scala/cosas/Fn.scala]: ../../Fn.scala.md
[main/scala/cosas/Types.scala]: ../../Types.scala.md
[main/scala/cosas/csv/csv.scala]: ../../csv/csv.scala.md
[main/scala/cosas/Property.scala]: ../../Property.scala.md
[main/scala/cosas/TypeSet.scala]: ../../TypeSet.scala.md