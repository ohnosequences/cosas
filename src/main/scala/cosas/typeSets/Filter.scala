package ohnosequences.cosas.typeSets

import ohnosequences.cosas._, fns._, types._

// TODO make the type predicate only a type, not a param?
trait Filter extends DepFn2[AnyTypePredicate, AnyTypeSet, AnyTypeSet] {

  type filter <: this.type
  val filter: filter

  implicit def skip[
    P <: In1,
    H <: P#ArgBound, T <: AnyTypeSet.Of[P#ArgBound],
    TO <: Out
  ](implicit
      ev: App2[filter, P, T, TO]
    ): App2[filter, P, H :~: T, TO] =
      filter at { (p: P, s: H :~: T) => filter(p,s.tail) }
}

case object Filter extends Filter {

  type filter = this.type
  val filter = this

  implicit def empty[P <: In1, X]: App2[filter, P, ∅[X], ∅[X]] =
    filter at { (p: P, empty: ∅[X]) => ∅[X] }

  implicit def nonEmpty[
    P <: AnyTypePredicate,
    H <: P#ArgBound, T <: AnyTypeSet.Of[P#ArgBound],
    TO <: AnyTypeSet.Of[P#ArgBound]
  ](implicit
    h: P Accepts H,
    ev: App2[filter, P, T, TO]
  ): App2[filter, P, H :~: T, H :~: TO] =
      filter at { (p: P, hs: H :~: T) => hs.head :~: filter(p,hs.tail)(ev) }
}


// @annotation.implicitNotFound(msg = "Can't check that predicate ${P} is true for every element of ${S}")
// sealed class CheckForAll[S <: AnyTypeSet, P <: AnyTypePredicate]
//
// object CheckForAll {
//
//   implicit def filterCheck[S <: AnyTypeSet, P <: AnyTypePredicate, Q <: AnyTypeSet]
//     (implicit
//       f: Filter[S, P] { type Out = Q },
//       s: S ~:~ Q
//     ):  CheckForAll[S, P] =
//     new CheckForAll[S, P]
// }
//
//
// @annotation.implicitNotFound(msg = "Can't check that predicate ${P} is true for any element of ${S}")
// sealed class CheckForAny[S <: AnyTypeSet, P <: AnyTypePredicate]
//
// // NOTE: this doesn't use Filter because it stop as soon as finds an element accepted byt the predicate
// object CheckForAny extends CheckForAny_2 {
//
//   implicit def head[P <: AnyTypePredicate, H <: P#ArgBound, T <: AnyTypeSet]
//     (implicit h: P Accepts H):
//         CheckForAny[H :~: T, P] =
//     new CheckForAny[H :~: T, P]
// }
//
// trait CheckForAny_2 {
//
//   implicit def tail[P <: AnyTypePredicate, H <: P#ArgBound, T <: AnyTypeSet]
//     (implicit t: CheckForAny[T, P]):
//         CheckForAny[H :~: T, P] =
//     new CheckForAny[H :~: T, P]
// }
