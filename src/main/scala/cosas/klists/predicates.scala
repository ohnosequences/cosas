package ohnosequences.cosas.klists

import ohnosequences.cosas._, fns._

case class any[P <: AnyPredicate](p: P) extends PredicateOver[AnyKList.withBound[P#In1]]

case object any extends TrueSomewhereInTail {

  implicit def trueAtHead[
    H <: L#Bound, L <: AnyKList,
    P <: AnyPredicate { type In1 = L#Bound }
  ](implicit
    p: P isTrueOn H
  ): any[P] isTrueOn (H :: L) = App1 { xs: H :: L => () }

  implicit def trueAtEmpty[P <: AnyPredicate { type In1 = X }, X]: any[P] isTrueOn *[X] = App1 { n: *[X] => () }
}

trait TrueSomewhereInTail {

  implicit def trueAtTail[
    H <: L#Bound, L <: AnyNonEmptyKList { type Bound = P#In1 },
    P <: AnyPredicate
  ](implicit
    p: any[P] isTrueOn L
  ): any[P] isTrueOn (H :: L) = App1 { xs: H :: L => () }
}

case class all[P <: AnyPredicate](p: P) extends PredicateOver[AnyKList.withBound[P#In1]]

case object all {

  implicit def trueAtEmpty[P <: AnyPredicate { type In1 = X }, X]: all[P] isTrueOn *[X] = App1 { n: *[X] => () }

  implicit def recAtHead[
    H <: L#Bound, L <: AnyKList { type Bound = P#In1 },
    P <: AnyPredicate
  ](implicit
    pHead: P isTrueOn H,
    pTail: all[P] isTrueOn L
  ): all[P] isTrueOn (H :: L) = App1 { xs: H :: L => () }
}
