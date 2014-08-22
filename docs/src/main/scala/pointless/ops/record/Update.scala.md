
## Popping an element from a set

It's like `Lookup`, but it removes the element



```scala
package ohnosequences.pointless.ops.record

import ohnosequences.pointless._, AnyFn._, taggedType._, property._, typeSet._, record._

@annotation.implicitNotFound(msg = "Can't update record ${R} with property values ${Ps}")
trait Update[R <: AnyRecord, Ps <: AnyTypeSet] extends Fn2[Tagged[R], Ps] with Constant[Tagged[R]]

object Update {

  implicit def update[R <: AnyRecord, Ps <: AnyTypeSet]
    (implicit 
      check: Ps ⊂ Tagged[R],
      replace: Replace[Tagged[R], Ps]
    ):  Update[R, Ps] with out[Tagged[R]] = 
    new Update[R, Ps] {
      def apply(recEntry: Tagged[R], propReps: Ps): Out = replace(recEntry, propReps)
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

[test/scala/pointless/PropertyTests.scala]: ../../../../../test/scala/pointless/PropertyTests.scala.md
[test/scala/pointless/RecordTests.scala]: ../../../../../test/scala/pointless/RecordTests.scala.md
[test/scala/pointless/TypeSetTests.scala]: ../../../../../test/scala/pointless/TypeSetTests.scala.md
[main/scala/pointless/TaggedType.scala]: ../../TaggedType.scala.md
[main/scala/pointless/Record.scala]: ../../Record.scala.md
[main/scala/pointless/ops/typeSet/Lookup.scala]: ../typeSet/Lookup.scala.md
[main/scala/pointless/ops/typeSet/Reorder.scala]: ../typeSet/Reorder.scala.md
[main/scala/pointless/ops/typeSet/Conversions.scala]: ../typeSet/Conversions.scala.md
[main/scala/pointless/ops/typeSet/Subtract.scala]: ../typeSet/Subtract.scala.md
[main/scala/pointless/ops/typeSet/Pop.scala]: ../typeSet/Pop.scala.md
[main/scala/pointless/ops/typeSet/Representations.scala]: ../typeSet/Representations.scala.md
[main/scala/pointless/ops/typeSet/Replace.scala]: ../typeSet/Replace.scala.md
[main/scala/pointless/ops/typeSet/Take.scala]: ../typeSet/Take.scala.md
[main/scala/pointless/ops/typeSet/Union.scala]: ../typeSet/Union.scala.md
[main/scala/pointless/ops/typeSet/Mappers.scala]: ../typeSet/Mappers.scala.md
[main/scala/pointless/ops/record/Update.scala]: Update.scala.md
[main/scala/pointless/ops/record/Conversions.scala]: Conversions.scala.md
[main/scala/pointless/ops/record/Get.scala]: Get.scala.md
[main/scala/pointless/Denotation.scala]: ../../Denotation.scala.md
[main/scala/pointless/TypeUnion.scala]: ../../TypeUnion.scala.md
[main/scala/pointless/Fn.scala]: ../../Fn.scala.md
[main/scala/pointless/Property.scala]: ../../Property.scala.md
[main/scala/pointless/TypeSet.scala]: ../../TypeSet.scala.md