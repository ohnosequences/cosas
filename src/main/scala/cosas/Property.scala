package ohnosequences.cosas

import AnyTypeSet._, AnyFn._, denotation._
import scala.reflect.ClassTag

trait AnyProperty extends AnyType {}

/* Properties should be defined as case objects: `case object name extends Property[String]` */
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
