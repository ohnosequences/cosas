package ohnosequences.pointless.ops.typeSet

import ohnosequences.pointless._, AnyTypeSet._

trait AnyTypePredicate {
  type ArgBound
  type Condition[_ <: ArgBound]
}

trait TypePredicate[B] extends AnyTypePredicate { type ArgBound = B }

object AnyTypePredicate {

  type ArgBoundOf[P <: AnyTypePredicate] = P#ArgBound
  type Accepts[P <: AnyTypePredicate, X <: ArgBoundOf[P]] = P#Condition[X]
}
import AnyTypePredicate._


@annotation.implicitNotFound(msg = "Can't check type predicate ${P} for every element of ${S}")
sealed class Check[S <: AnyTypeSet, P <: AnyTypePredicate]

object Check {

  implicit def empty[P <: AnyTypePredicate]: 
        Check[∅, P] =
    new Check[∅, P]

  implicit def cons[P <: AnyTypePredicate, H <: ArgBoundOf[P], T <: AnyTypeSet]
    (implicit 
      h: P Accepts H,
      t: Check[T, P]
    ):  Check[H :~: T, P] =
    new Check[H :~: T, P]
}
