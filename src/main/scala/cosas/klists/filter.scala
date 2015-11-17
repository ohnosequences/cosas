package ohnosequences.cosas.klists

import ohnosequences.cosas._, fns._

/* Does cons if predicate is true or skips the element if not */
class consIf[P <: AnyPredicate] extends DepFn2[
  P#In1, AnyKList.Of[P#In1],
  AnyKList.Of[P#In1]
]

case object consIf extends consIf_false {

  implicit def pTrue[
    P <: AnyPredicate { type In1 >: H },
    H <: T#Bound, T <: AnyKList { type Bound <: P#In1 }
  ](implicit
    ev: P isTrueOn H
  ): AnyApp2At[consIf[P],
      H, T
    ] { type Y = H :: T } =
    App2 { (h: H, t: T) => h :: t }
}

trait consIf_false {

  implicit def pFalse[
    P <: AnyPredicate { type In1 >: H },
    H <: T#Bound, T <: AnyKList { type Bound <: P#In1 }
  ](implicit
    ev: P isFalseOn H
  ): AnyApp2At[consIf[P],
      H, T
    ] { type Y = T } =
    App2 { (_, t: T) => t }
}

/* p.filter(l) is just consIf(p).foldRight(KNil)(l) */
class filter[P <: AnyPredicate] extends DepFn2[
  P, AnyKList.Of[P#In1],
  AnyKList.Of[P#In1]
]

case object filter {

  implicit def default[
    P <: AnyPredicate { type In1 >: O#Bound },
    L <: AnyKList { type Bound <: P#In1 },
    O <: AnyKList
  ](implicit
    appFold: AnyApp3At[FoldRight[consIf[P]],
      consIf[P], *[L#Bound], L
    ] { type Y = O }
  ): AnyApp2At[filter[P],
      P, L
    ] { type Y = O } =
    App2 { (p: P, l: L) => appFold(new consIf[P], *[L#Bound], l) }
}
