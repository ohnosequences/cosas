package ohnosequences.cosas.klists

import ohnosequences.cosas._, fns._

class Split[E] extends DepFn1[AnyKList, (AnyKList, E, AnyKList)]

case object Split extends SplitFoundInTail {

  implicit def foundInHead[
    E <: T#Bound,
    T <: AnyKList
  ]: AnyApp1At[Split[E], E :: T] { type Y = (*[T#Bound], E, T) } =
     App1 { (s: E :: T) => (*[T#Bound], s.head, s.tail) }
}

sealed trait SplitFoundInTail {

  implicit def foundInTail[
    E <: T#Bound,
    // picking from H :: T
    H <: OL#Bound, T  <: AnyKList { type Bound = OL#Bound },
    OL <: AnyKList,
    OR <: AnyKList  {type Bound = OL#Bound }
  ](implicit
      l: AnyApp1At[Split[E], T] { type Y = (OL, E, OR) }
  )
  : AnyApp1At[Split[E], H :: T] { type Y = (H :: OL, E, OR) } =
    App1 { (s: H :: T) => val (lo, e, ro) = l(s.tail); (s.head :: lo, e, ro) }
}

class SplitS[E] extends DepFn1[AnyKList, (AnyKList, E, AnyKList)]

case object SplitS extends SplitSFoundInTail {

  implicit def foundInHead[
    E <: T#Bound, H <: E,
    T <: AnyKList { type Bound >: H }
  ]: AnyApp1At[SplitS[E], H :: T] { type Y = (*[T#Bound], H, T) } =
     App1 { (s: H :: T) => (*[T#Bound], s.head, s.tail) }
}

sealed trait SplitSFoundInTail {

  implicit def foundInTail[
    E >: X <: T#Bound,
    // picking from H :: T
    H <: OL#Bound, T  <: AnyKList { type Bound = OL#Bound },
    OL <: AnyKList,
    X,
    OR <: AnyKList  {type Bound = OL#Bound }
  ](implicit
      l: AnyApp1At[SplitS[E], T] { type Y = (OL, X, OR) }
  )
  : AnyApp1At[SplitS[E], H :: T] { type Y = (H :: OL, X, OR) } =
    App1 { (s: H :: T) => val (lo, x, ro) = l(s.tail); (s.head :: lo, x, ro) }
}


class Pick[E] extends DepFn1[AnyKList, (E, AnyKList)]

case object Pick extends PickFoundInTail {

  implicit def foundInHead[
    E,
    T <: AnyKList { type Bound >: E }
  ]: AnyApp1At[Pick[E], E :: T] { type Y = (E, T) } =
     App1 { (s: E :: T) => (s.head, s.tail) }
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
