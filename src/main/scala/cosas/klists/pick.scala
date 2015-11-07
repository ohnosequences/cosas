package ohnosequences.cosas.klists

import ohnosequences.cosas._, fns._

// TODO a version with (Left, E, Right) as output
class Pick[E] extends DepFn1[AnyKList, (E, AnyKList)]

case object Pick extends PickFoundInTail {

  implicit def foundInHead[
    E, H <: E,
    T <: AnyKList { type Bound >: E}
  ]: AnyApp1At[Pick[E], H :: T] { type Y = (E, T) } =
     App1 { (s: H :: T) => (s.head, s.tail) }
}

sealed trait PickFoundInTail {

  implicit def foundInTail[
    E, H <: TO#Bound,
    T  <: AnyKList { type Bound = TO#Bound },
    TO <: AnyKList
  ](implicit
      l: AnyApp1At[Pick[E], T] { type Y = (E, TO) }
  ): AnyApp1At[Pick[E], H :: T] { type Y = (E, H :: TO) } =
     App1 { (s: H :: T) => val (e, t) = l(s.tail); (e, s.head :: t) }
}

class PickS[E] extends DepFn1[AnyKList, (E, AnyKList)]

case object PickS extends PickSFoundInTail  {

  implicit def foundInHead[E <: T#Bound, H <: E, T <: AnyKList { type Bound >: H }]
  : AnyApp1At[PickS[E], H :: T] { type Y = (H, T) } =
    App1 { (s: H :: T) => (s.head, s.tail) }
}

trait PickSFoundInTail {

  implicit def foundInTail[
    X, E >: X, H <: TO#Bound,
    T  <: AnyKList { type Bound >: H },
    TO <: AnyKList
  ](implicit
      l: AnyApp1At[PickS[E], T] { type Y = (X, TO) }
  )
  : AnyApp1At[PickS[E], H :: T] { type Y = (X, H :: TO) } =
    App1 { (s: H :: T) => val (e, t) = l(s.tail); (e, s.head :: t) }
}
