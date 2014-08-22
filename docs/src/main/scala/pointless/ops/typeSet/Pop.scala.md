
## Popping an element from a set

It's like `Lookup`, but it removes the element



```scala
package ohnosequences.pointless.ops.typeSet

import ohnosequences.pointless._, AnyFn._, typeSet._

@annotation.implicitNotFound(msg = "Can't pop an element of type ${E} from the set ${S}")
trait Pop[S <: AnyTypeSet, E] extends Fn1[S] with WithCodomain[(E, AnyTypeSet)] {
  type SOut <: AnyTypeSet
  type EOut <: E
  type Out = (EOut, SOut)
}

object Pop extends Pop_2 {
  def apply[S <: AnyTypeSet, E](implicit pop: Pop[S, E]): Pop[S, E] with out[pop.Out] = pop

  type Aux[S <: AnyTypeSet, E, SO <: AnyTypeSet, EO <: E] = Pop[S, E] {
    type SOut = SO
    type EOut = EO
  } 

  implicit def foundInHead[E, H <: E , T <: AnyTypeSet]: Pop.Aux[H :~: T, E, T, H] =
    new Pop[H :~: T, E] { 
      type SOut = T
      type EOut = H
      def apply(s: H :~: T): Out = (s.head, s.tail)
    }
}

trait Pop_2 {
  implicit def foundInTail[H, T <: AnyTypeSet, E](implicit e: E ∈ T, l: Pop[T, E]):
    Pop.Aux[H :~: T, E, H :~: l.SOut, l.EOut] =
    new Pop[H :~: T, E] { 
      type SOut = H :~: l.SOut
      type EOut = l.EOut
      def apply(s: H :~: T): Out = {

        // val (e, t) = l(s.tail)
        // (e, s.head :~: t)

        val tpl = l(s.tail)
        (tpl._1, s.head :~: tpl._2)
      }
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
[main/scala/pointless/ops/typeSet/Lookup.scala]: Lookup.scala.md
[main/scala/pointless/ops/typeSet/Reorder.scala]: Reorder.scala.md
[main/scala/pointless/ops/typeSet/Conversions.scala]: Conversions.scala.md
[main/scala/pointless/ops/typeSet/Subtract.scala]: Subtract.scala.md
[main/scala/pointless/ops/typeSet/Pop.scala]: Pop.scala.md
[main/scala/pointless/ops/typeSet/Representations.scala]: Representations.scala.md
[main/scala/pointless/ops/typeSet/Replace.scala]: Replace.scala.md
[main/scala/pointless/ops/typeSet/Take.scala]: Take.scala.md
[main/scala/pointless/ops/typeSet/Union.scala]: Union.scala.md
[main/scala/pointless/ops/typeSet/Mappers.scala]: Mappers.scala.md
[main/scala/pointless/ops/record/Update.scala]: ../record/Update.scala.md
[main/scala/pointless/ops/record/Conversions.scala]: ../record/Conversions.scala.md
[main/scala/pointless/ops/record/Get.scala]: ../record/Get.scala.md
[main/scala/pointless/Denotation.scala]: ../../Denotation.scala.md
[main/scala/pointless/TypeUnion.scala]: ../../TypeUnion.scala.md
[main/scala/pointless/Fn.scala]: ../../Fn.scala.md
[main/scala/pointless/Property.scala]: ../../Property.scala.md
[main/scala/pointless/TypeSet.scala]: ../../TypeSet.scala.md