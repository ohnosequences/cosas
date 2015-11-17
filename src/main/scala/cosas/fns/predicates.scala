package ohnosequences.cosas.fns

import ohnosequences.cosas._

trait AnyPredicate extends AnyDepFn1 { type Out = AnyBool }

case object AnyPredicate {

  type Over[T] = AnyPredicate { type In1 <: T }

  // NOTE: any predicate is False by default
  implicit def default[P <: AnyPredicate { type In1 >: X }, X]:
    AnyApp1At[P, X] { type Y = False } =
    App1 { _: X => False }
}

trait PredicateOver[T] extends AnyPredicate { type In1 = T }

// TODO: add Not, And, Or
