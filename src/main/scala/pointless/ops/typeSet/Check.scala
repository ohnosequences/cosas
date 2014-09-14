package ohnosequences.pointless.ops.typeSet

import ohnosequences.pointless._, AnyTypeSet._

trait AnyTypePredicate {
  type ArgBound
  type Condition[_ <: ArgBound]
}

trait TypePredicate[B] extends AnyTypePredicate { type ArgBound = B }

object AnyTypePredicate {

  // type ArgBoundOf[P <: AnyTypePredicate] = P#ArgBound
  type Accepts[P <: AnyTypePredicate, X <: P#ArgBound] = P#Condition[X]
}
import AnyTypePredicate._


@annotation.implicitNotFound(msg = "Can't check that predicate ${P} is true for every element of ${S}")
sealed class CheckForAll[S <: AnyTypeSet.Of[P#ArgBound], P <: AnyTypePredicate]

object CheckForAll {

  implicit def empty[E <: AnyEmptySet.Of[P#ArgBound], P <: AnyTypePredicate]: 
        CheckForAll[E, P] =
    new CheckForAll[E, P]

  implicit def cons[P <: AnyTypePredicate, H <: T#Bound, T <: AnyTypeSet { type Bound <: P#ArgBound }]
    (implicit 
      h: P Accepts H,
      t: CheckForAll[T, P]
    ):  CheckForAll[H :~: T, P] =
    new CheckForAll[H :~: T, P]
}

@annotation.implicitNotFound(msg = "Can't check that predicate ${P} is true for any element of ${S}")
sealed class CheckForAny[S <: AnyTypeSet.Of[P#ArgBound], P <: AnyTypePredicate]

object CheckForAny extends CheckForAny_2 {

  implicit def head[P <: AnyTypePredicate, H <: T#Bound, T <: AnyTypeSet { type Bound <: P#ArgBound }]
    (implicit h: P Accepts H):
        CheckForAny[H :~: T, P] =
    new CheckForAny[H :~: T, P]
}

trait CheckForAny_2 {

  implicit def tail[P <: AnyTypePredicate, H <: T#Bound, T <: AnyTypeSet { type Bound <: P#ArgBound }]
    (implicit t: CheckForAny[T, P]):
        CheckForAny[H :~: T, P] =
    new CheckForAny[H :~: T, P]
}
