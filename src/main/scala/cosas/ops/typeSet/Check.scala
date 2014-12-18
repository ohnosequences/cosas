package ohnosequences.cosas.ops.typeSet

import ohnosequences.cosas._, typeSet._


@annotation.implicitNotFound(msg = "Can't check that predicate ${P} is true for every element of ${S}")
sealed class CheckForAll[S <: AnyTypeSet, P <: AnyTypePredicate]

object CheckForAll {

  implicit def empty[P <: AnyTypePredicate]: 
        CheckForAll[∅, P] =
    new CheckForAll[∅, P]

  implicit def cons[P <: AnyTypePredicate, H <: P#ArgBound, T <: AnyTypeSet]
    (implicit 
      h: P Accepts H,
      t: CheckForAll[T, P]
    ):  CheckForAll[H :~: T, P] =
    new CheckForAll[H :~: T, P]
}

@annotation.implicitNotFound(msg = "Can't check that predicate ${P} is true for any element of ${S}")
sealed class CheckForAny[S <: AnyTypeSet, P <: AnyTypePredicate]

object CheckForAny extends CheckForAny_2 {

  implicit def head[P <: AnyTypePredicate, H <: P#ArgBound, T <: AnyTypeSet]
    (implicit h: P Accepts H):
        CheckForAny[H :~: T, P] =
    new CheckForAny[H :~: T, P]
}

trait CheckForAny_2 {

  implicit def tail[P <: AnyTypePredicate, H <: P#ArgBound, T <: AnyTypeSet]
    (implicit t: CheckForAny[T, P]):
        CheckForAny[H :~: T, P] =
    new CheckForAny[H :~: T, P]
}
