package ohnosequences.pointless

import AnyTaggedType._, AnyTypeSet._, AnyFn._
import scala.reflect.ClassTag

trait AnyProperty extends AnyTaggedType {

  val label: String
  val classTag: ClassTag[Raw]
}

/* Properties should be defined as case objects: `case object name extends Property[String]` */
class Property[V](implicit val classTag: ClassTag[V]) extends AnyProperty {

  val label = this.toString

  type Raw = V
}

class PropertyOps[P <: AnyProperty](val p: P) extends TaggedTypeOps[P](p) { self =>

  def is(value: RawOf[P]): Tagged[P] = p =>> value
}

object AnyProperty extends AnyProperty_2 {

  type Of[T] = AnyProperty { type Raw = T }

  implicit def propertyOps[P <: AnyProperty](p: P): PropertyOps[P] = new PropertyOps[P](p)

  implicit def hasPropertiesOps[T](t: T): HasPropertiesOps[T] = new HasPropertiesOps[T](t)

  // NOTE: this is not in the companion object, because if it's there, it loops the implicit search

  implicit def setToProp[T, P <: AnyProperty, Ps <: AnyTypeSet.Of[AnyProperty]]
    (implicit ps: T Has Ps, in: P ∈ Ps):
         T HasProperty P =
    new (T HasProperty P)

  // /* ∀ T, Ps, Qs ⊂ Ps  (T Has Ps => T Has Qs) */
  // implicit def setToSubset[T, Ps <: AnyTypeSet.Of[AnyProperty], Qs <: SubsetOf[Ps]]
  //   (implicit ps: T Has Ps):
  //        T HasProperties Qs =
  //   new (T HasProperties Qs)

}

trait AnyProperty_2 {

  // @annotation.implicitNotFound(msg = "Can't prove that ${Smth} has property ${P}")
  // final type HasProperty[Smth, P <: AnyProperty] = Has[Smth, P :~: ∅]

  implicit def empty[T]: T HasProperties ∅ = new (T HasProperties ∅)
  /* ∀ T, P, Ps  (T HasProperty P & T Has Ps => T Has (P :~: Ps)) */
  implicit def cons[T, P <: AnyProperty, Ps <: AnyTypeSet.Of[AnyProperty]]
    (implicit p: T HasProperty P, ps: T HasProperties Ps):
         T HasProperties (P :~: Ps) =
    new (T HasProperties (P :~: Ps))

}

// TODO: separate as ops
/* Evidence that an arbitrary type `Smth` has a set of properties `Ps` */
sealed class Has[Smth, Ps <: AnyTypeSet.Of[AnyProperty]] // for declaration

// For checks
@annotation.implicitNotFound(msg = "Can't prove that ${Smth} has property ${P}")
sealed class HasProperty[Smth, P <: AnyProperty]
@annotation.implicitNotFound(msg = "Can't prove that ${Smth} has properties ${Ps}")
sealed class HasProperties[Smth, Ps <: AnyTypeSet]


class HasPropertiesOps[Smth](val smth: Smth) {

  def has[Ps <: AnyTypeSet.Of[AnyProperty]](ps: Ps): 
         Smth Has Ps = 
    new (Smth Has Ps)

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
