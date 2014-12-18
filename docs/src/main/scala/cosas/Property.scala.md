
```scala
package ohnosequences.cosas

import AnyTypeSet._, AnyFn._, AnyType._
import scala.reflect.ClassTag

trait AnyProperty extends AnyType {}
```

Properties should be defined as case objects: `case object name extends Property[String]`

```scala
// class Property[V](implicit val classTag: ClassTag[V]) extends AnyProperty {
class Property[V](val label: String) extends AnyProperty {
  
  type Raw = V
}

class LabeledProperty[V](val label: String) extends AnyProperty {

  type Raw = V
}

object AnyProperty {

  type ofType[T] = AnyProperty { type Raw = T }

  implicit def propertyOps[P <: AnyProperty](p: P): PropertyOps[P] = new PropertyOps[P](p)
}

class PropertyOps[P <: AnyProperty](val p: P) extends AnyVal {

  def apply(v: P#Raw): ValueOf[P] = new ValueOf[P](v)
}


// TODO: restore this
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

[test/scala/cosas/PropertyTests.scala]: ../../../test/scala/cosas/PropertyTests.scala.md
[test/scala/cosas/TypeUnionTests.scala]: ../../../test/scala/cosas/TypeUnionTests.scala.md
[test/scala/cosas/ScalazEquality.scala]: ../../../test/scala/cosas/ScalazEquality.scala.md
[test/scala/cosas/WrapTests.scala]: ../../../test/scala/cosas/WrapTests.scala.md
[test/scala/cosas/RecordTests.scala]: ../../../test/scala/cosas/RecordTests.scala.md
[test/scala/cosas/TypeSetTests.scala]: ../../../test/scala/cosas/TypeSetTests.scala.md
[main/scala/cosas/PropertiesHolder.scala]: PropertiesHolder.scala.md
[main/scala/cosas/Record.scala]: Record.scala.md
[main/scala/cosas/ops/typeSet/Check.scala]: ops/typeSet/Check.scala.md
[main/scala/cosas/ops/typeSet/Reorder.scala]: ops/typeSet/Reorder.scala.md
[main/scala/cosas/ops/typeSet/Conversions.scala]: ops/typeSet/Conversions.scala.md
[main/scala/cosas/ops/typeSet/AggregateProperties.scala]: ops/typeSet/AggregateProperties.scala.md
[main/scala/cosas/ops/typeSet/Subtract.scala]: ops/typeSet/Subtract.scala.md
[main/scala/cosas/ops/typeSet/Pop.scala]: ops/typeSet/Pop.scala.md
[main/scala/cosas/ops/typeSet/Representations.scala]: ops/typeSet/Representations.scala.md
[main/scala/cosas/ops/typeSet/Replace.scala]: ops/typeSet/Replace.scala.md
[main/scala/cosas/ops/typeSet/Take.scala]: ops/typeSet/Take.scala.md
[main/scala/cosas/ops/typeSet/Union.scala]: ops/typeSet/Union.scala.md
[main/scala/cosas/ops/typeSet/Mappers.scala]: ops/typeSet/Mappers.scala.md
[main/scala/cosas/ops/record/Update.scala]: ops/record/Update.scala.md
[main/scala/cosas/ops/record/Conversions.scala]: ops/record/Conversions.scala.md
[main/scala/cosas/ops/record/Get.scala]: ops/record/Get.scala.md
[main/scala/cosas/TypeUnion.scala]: TypeUnion.scala.md
[main/scala/cosas/Fn.scala]: Fn.scala.md
[main/scala/cosas/Types.scala]: Types.scala.md
[main/scala/cosas/csv/csv.scala]: csv/csv.scala.md
[main/scala/cosas/Property.scala]: Property.scala.md
[main/scala/cosas/TypeSet.scala]: TypeSet.scala.md