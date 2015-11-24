package ohnosequences.cosas.fns

import ohnosequences.cosas._

trait AnyPredicate extends Any with AnyDepFn1 { type Out = Unit }

case object AnyPredicate {

  type Over[T] = AnyPredicate { type In1 <: T }

  implicit def predicateSyntax[P <: AnyPredicate](p: P):
    syntax.PredicateSyntax[P] =
    syntax.PredicateSyntax(p)
}

trait PredicateOver[T] extends AnyPredicate { type In1 = T }

case class asPredicate[F <: AnyDepFn1 { type Out = Unit }](val f: F) extends AnyVal with AnyPredicate {

  type In1 = F#In1
}

case object asPredicate {

  implicit def fromF[F <: AnyDepFn1 { type Out = Unit }, V <: F#In1](implicit
    p: AnyApp1At[F,V] { type Y = Unit }
  )
  : asPredicate[F] isTrueOn V =
    App1 { v: V => () }
}

trait AnyAnd extends AnyPredicate { and =>

  type In1 = First#In1

  type First <: AnyPredicate
  val first: First

  type Second <: AnyPredicate { type In1 = and.First#In1 }
  val second: Second
}
case class and[
  P1 <: AnyPredicate,
  P2 <: AnyPredicate { type In1 = P1#In1 }
](val first: P1, val second: P2) extends AnyAnd {

  type First = P1; type Second = P2
}

case object AnyAnd {

  implicit def bothTrue[
    AP <: AnyAnd { type Second <: AnyPredicate {  type In1 >: V } },
    V <: AP#In1
  ](implicit
    p1: AP#First isTrueOn V,
    p2: AP#Second isTrueOn V
  )
  : AP isTrueOn V =
    App1 { x: V => () }
}

trait AnyOr extends AnyPredicate { and =>

  type In1 = First#In1

  type First <: AnyPredicate
  val first: First

  type Second <: AnyPredicate { type In1 = and.First#In1 }
  val second: Second
}
case class Or[
  P1 <: AnyPredicate,
  P2 <: AnyPredicate { type In1 = P1#In1 }
](val first: P1, val second: P2) extends AnyOr {

  type First = P1; type Second = P2
}

case object AnyOr {

  implicit def firstTrue[
    AP <: AnyOr,
    V <: AP#In1
  ](implicit
    p1: AP#First isTrueOn V
  )
  : AP isTrueOn V =
    App1 { x: V => () }

  implicit def secondTrue[
    AP <: AnyOr { type Second <: AnyPredicate {  type In1 >: V } },
    V <: AP#In1
  ](implicit
    p1: AP#Second isTrueOn V
  )
  : AP isTrueOn V =
    App1 { x: V => () }
}
