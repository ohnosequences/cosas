
```scala
package ohnosequences.pointless

import ohnosequences.pointless._, taggedType._
import scala.reflect.ClassTag

object property {

  trait AnyProperty extends AnyTaggedType {

    val label: String
    val classTag: ClassTag[Raw]
  }

  class Property[V](implicit val classTag: ClassTag[V]) extends AnyProperty {

    val label = this.toString

    type Raw = V
  }

  implicit def propertyOps[P <: AnyProperty](p: P): PropertyOps[P] = new PropertyOps[P](p)
  class PropertyOps[P <: AnyProperty](val p: P) extends TaggedTypeOps(p) { self =>

    def is(value: RawOf[P]): Tagged[P] = self =>> value
  }

}

// /* 
//   For a given arbitrary type `Smth`, filters any property set, 
//   leaving only those which have the `Smth HasProperty _` evidence
// */
// trait FilterProps[Smth, Ps <: TypeSet] extends DepFn1[Ps] {
//   type Out <: TypeSet
// }

// object FilterProps extends FilterProps2 {
//   // the case when there is this evidence (leaving the head)
//   implicit def consFilter[Smth, H <: AnyProperty, T <: TypeSet, OutT <: TypeSet]
//     (implicit
//       h: Smth HasProperty H,
//       t: Aux[Smth, T, OutT]
//     ): Aux[Smth, H :~: T, H :~: OutT] =
//       new FilterProps[Smth, H :~: T] { type Out = H :~: OutT
//         def apply(s: H :~: T): Out = s.head :~: t(s.tail)
//       }
// }

// trait FilterProps2 {
//   def apply[Smth, Ps <: TypeSet](implicit filt: FilterProps[Smth, Ps]): Aux[Smth, Ps, filt.Out] = filt

//   type Aux[Smth, In <: TypeSet, O <: TypeSet] = FilterProps[Smth, In] { type Out = O }
  
//   implicit def emptyFilter[Smth]: Aux[Smth, ∅, ∅] =
//     new FilterProps[Smth, ∅] {
//       type Out = ∅
//       def apply(s: ∅): Out = ∅
//     }

//   // the low-priority case when there is no evidence (just skipping head)
//   implicit def skipFilter[Smth, H <: AnyProperty, T <: TypeSet, OutT <: TypeSet]
//     (implicit t: Aux[Smth, T, OutT]): Aux[Smth, H :~: T, OutT] =
//       new FilterProps[Smth, H :~: T] { type Out = OutT
//         def apply(s: H :~: T): Out = t(s.tail)
//       }
// }


// /* This applies `FilterProps` to a list of `Smth`s (`Ts` here) */
// trait ZipWithProps[Ts <: TypeSet, Ps <: TypeSet] extends DepFn2[Ts, Ps] {
//   type Out <: TypeSet
// }

// object ZipWithProps {
//   def apply[Ts <: TypeSet, Ps <: TypeSet]
//     (implicit z: ZipWithProps[Ts, Ps]): Aux[Ts, Ps, z.Out] = z

//   type Aux[Ts <: TypeSet, Ps <: TypeSet, O <: TypeSet] = ZipWithProps[Ts, Ps] { type Out = O }
  
//   implicit def emptyZipWithProps[Ps <: TypeSet]: Aux[∅, Ps, ∅] =
//     new ZipWithProps[∅, Ps] {
//       type Out = ∅
//       def apply(s: ∅, ps: Ps): Out = ∅
//     }

//   implicit def consZipWithProps[H, T <: TypeSet, Ps <: TypeSet, OutT <: TypeSet]
//     (implicit 
//       h: FilterProps[H, Ps],
//       t: Aux[T, Ps, OutT]
//     ): Aux[H :~: T, Ps, (H, h.Out) :~: OutT] =
//       new ZipWithProps[H :~: T, Ps] { type Out = (H, h.Out) :~: OutT
//         def apply(s: H :~: T, ps: Ps): Out = (s.head, h(ps)) :~: t(s.tail, ps)
//       }
// }

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

[test/scala/pointless/PropertyTests.scala]: ../../../test/scala/pointless/PropertyTests.scala.md
[test/scala/pointless/RecordTests.scala]: ../../../test/scala/pointless/RecordTests.scala.md
[test/scala/pointless/TypeSetTests.scala]: ../../../test/scala/pointless/TypeSetTests.scala.md
[main/scala/pointless/TaggedType.scala]: TaggedType.scala.md
[main/scala/pointless/Record.scala]: Record.scala.md
[main/scala/pointless/ops/typeSet/Lookup.scala]: ops/typeSet/Lookup.scala.md
[main/scala/pointless/ops/typeSet/Reorder.scala]: ops/typeSet/Reorder.scala.md
[main/scala/pointless/ops/typeSet/Conversions.scala]: ops/typeSet/Conversions.scala.md
[main/scala/pointless/ops/typeSet/Subtract.scala]: ops/typeSet/Subtract.scala.md
[main/scala/pointless/ops/typeSet/Pop.scala]: ops/typeSet/Pop.scala.md
[main/scala/pointless/ops/typeSet/Representations.scala]: ops/typeSet/Representations.scala.md
[main/scala/pointless/ops/typeSet/Replace.scala]: ops/typeSet/Replace.scala.md
[main/scala/pointless/ops/typeSet/Take.scala]: ops/typeSet/Take.scala.md
[main/scala/pointless/ops/typeSet/Union.scala]: ops/typeSet/Union.scala.md
[main/scala/pointless/ops/typeSet/Mappers.scala]: ops/typeSet/Mappers.scala.md
[main/scala/pointless/ops/record/Update.scala]: ops/record/Update.scala.md
[main/scala/pointless/ops/record/Conversions.scala]: ops/record/Conversions.scala.md
[main/scala/pointless/ops/record/Get.scala]: ops/record/Get.scala.md
[main/scala/pointless/Denotation.scala]: Denotation.scala.md
[main/scala/pointless/TypeUnion.scala]: TypeUnion.scala.md
[main/scala/pointless/Fn.scala]: Fn.scala.md
[main/scala/pointless/Property.scala]: Property.scala.md
[main/scala/pointless/TypeSet.scala]: TypeSet.scala.md