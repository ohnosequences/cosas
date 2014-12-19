package ohnosequences.cosas.ops.typeSets

import ohnosequences.cosas._, fns._, typeSets._

@annotation.implicitNotFound(msg = "Can't filter set ${S} using type predicate ${P}")
trait Filter[S <: AnyTypeSet, P <: AnyTypePredicate]
  extends Fn1[S] with OutBound[AnyTypeSet]

object Filter extends Filter_2 {

  implicit def empty[P <: AnyTypePredicate]: 
        Filter[∅, P] with Out[∅] =
    new Filter[∅, P] with Out[∅] {
      def apply(s: In1): Out = ∅
    }


  implicit def cons[P <: AnyTypePredicate, H <: P#ArgBound, T <: AnyTypeSet, TO <: AnyTypeSet]
    (implicit 
      h: P Accepts H,
      t: Filter[T, P] { type Out = TO }
    ):  Filter[H :~: T, P] with Out[H :~: TO] =
    new Filter[H :~: T, P] with Out[H :~: TO] {
      def apply(s: In1): Out = s.head :~: t(s.tail)
    }
}

trait Filter_2 {

  implicit def skip[P <: AnyTypePredicate, H <: P#ArgBound, T <: AnyTypeSet, TO <: AnyTypeSet]
    (implicit 
      t: Filter[T, P] { type Out = TO }
    ):  Filter[H :~: T, P] with Out[TO] =
    new Filter[H :~: T, P] with Out[TO] {
      def apply(s: In1): Out = t(s.tail)
    }
}


@annotation.implicitNotFound(msg = "Can't check that predicate ${P} is true for every element of ${S}")
sealed class CheckForAll[S <: AnyTypeSet, P <: AnyTypePredicate]

object CheckForAll {

  implicit def filterCheck[S <: AnyTypeSet, P <: AnyTypePredicate, Q <: AnyTypeSet]
    (implicit 
      f: Filter[S, P] { type Out = Q },
      s: S ~:~ Q
    ):  CheckForAll[S, P] =
    new CheckForAll[S, P]
}


@annotation.implicitNotFound(msg = "Can't check that predicate ${P} is true for any element of ${S}")
sealed class CheckForAny[S <: AnyTypeSet, P <: AnyTypePredicate]

// NOTE: this doesn't use Filter because it stop as soon as finds an element accepted byt the predicate
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
