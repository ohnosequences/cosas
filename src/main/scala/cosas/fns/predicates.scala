package ohnosequences.cosas.fns

import ohnosequences.cosas._

trait AnyPredicate extends AnyDepFn1 { type Out = AnyBool }

case object AnyPredicate {

  type Over[T] = AnyPredicate { type In1 <: T }

  // NOTE: any predicate is False by default
  implicit def default[P <: AnyPredicate { type In1 >: X }, X]:
    P isFalseOn X = App1 { _: X => False }


  implicit def predicateSyntax[P <: AnyPredicate](p: P):
    syntax.PredicateSyntax[P] =
    syntax.PredicateSyntax(p)
}

// TODO: add Not, And, Or
trait PredicateOver[T] extends AnyPredicate { type In1 = T }

trait AnyAnd extends AnyPredicate { and =>

  type In1 = First#In1 //<: First#In1

  type First <: AnyPredicate
  val first: First

  type Second <: AnyPredicate { type In1 = and.First#In1 }
  val second: Second
}
case class and[
  P1 <: AnyPredicate,
  P2 <: AnyPredicate { type In1 = P1#In1 }
](val first: P1, val second: P2) extends AnyAnd {

  // type In1 = P1#In1
  type First = P1; type Second = P2
}

case object AnyAnd {

  implicit def bothTrue[
    AP <: AnyAnd { type Second <: AnyPredicate {  type In1 >: V } },
    V <: AP#In1
  ](implicit
    p1: AP#First isTrueOn V,
    p2: AP#Second isTrueOn V
    // NOTE this is really strange
  )
  : AP isTrueOn V =
    App1 { x: V => True }
}
