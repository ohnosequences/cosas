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
