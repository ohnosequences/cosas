package ohnosequences.cosas.klists

import ohnosequences.cosas._, fns._

class split[E] extends DepFn1[AnyKList, (AnyKList, E, AnyKList)]

case object split extends splitFoundInTail {

  implicit def foundInHead[
    E <: T#Bound,
    T <: AnyKList
  ]: AnyApp1At[split[E], E :: T] { type Y = (*[T#Bound], E, T) } =
     App1 { (s: E :: T) => (*[T#Bound], s.head, s.tail) }
}

sealed trait splitFoundInTail {

  implicit def foundInTail[
    E <: T#Bound,
    // picking from H :: T
    H <: OL#Bound, T  <: AnyKList { type Bound = OL#Bound },
    OL <: AnyKList,
    OR <: AnyKList  {type Bound = OL#Bound }
  ](implicit
      l: AnyApp1At[split[E], T] { type Y = (OL, E, OR) }
  )
  : AnyApp1At[split[E], H :: T] { type Y = (H :: OL, E, OR) } =
    App1 { (s: H :: T) => val (lo, e, ro) = l(s.tail); (s.head :: lo, e, ro) }
}

class splitS[E] extends DepFn1[AnyKList, (AnyKList, E, AnyKList)]

case object splitS extends splitSFoundInTail {

  implicit def foundInHead[
    E <: T#Bound, H <: E,
    T <: AnyKList { type Bound >: H }
  ]: AnyApp1At[splitS[E], H :: T] { type Y = (*[T#Bound], H, T) } =
     App1 { (s: H :: T) => (*[T#Bound], s.head, s.tail) }
}

sealed trait splitSFoundInTail {

  implicit def foundInTail[
    E >: X <: T#Bound,
    // picking from H :: T
    H <: OL#Bound, T  <: AnyKList { type Bound = OL#Bound },
    OL <: AnyKList,
    X,
    OR <: AnyKList  {type Bound = OL#Bound }
  ](implicit
      l: AnyApp1At[splitS[E], T] { type Y = (OL, X, OR) }
  )
  : AnyApp1At[splitS[E], H :: T] { type Y = (H :: OL, X, OR) } =
    App1 { (s: H :: T) => val (lo, x, ro) = l(s.tail); (s.head :: lo, x, ro) }
}


class pick[E] extends DepFn1[AnyKList, (E, AnyKList)]

case object pick extends pickFoundInTail {

  implicit def foundInHead[
    E,
    T <: AnyKList { type Bound >: E }
  ]: AnyApp1At[pick[E], E :: T] { type Y = (E, T) } =
     App1 { (s: E :: T) => (s.head, s.tail) }
}

sealed trait pickFoundInTail {

  implicit def foundInTail[
    E, H <: TO#Bound,
    T  <: AnyKList { type Bound = TO#Bound },
    TO <: AnyKList
  ](implicit
      l: AnyApp1At[pick[E], T] { type Y = (E, TO) }
  ): AnyApp1At[pick[E], H :: T] { type Y = (E, H :: TO) } =
     App1 { (s: H :: T) => val (e, t) = l(s.tail); (e, s.head :: t) }
}


class pickS[E] extends DepFn1[AnyKList, (E, AnyKList)]

case object pickS extends pickSFoundInTail  {

  implicit def foundInHead[E <: T#Bound, H <: E, T <: AnyKList { type Bound >: H }]
  : AnyApp1At[pickS[E], H :: T] { type Y = (H, T) } =
    App1 { (s: H :: T) => (s.head, s.tail) }
}

trait pickSFoundInTail {

  implicit def foundInTail[
    X, E >: X, H <: TO#Bound,
    T  <: AnyKList { type Bound >: H },
    TO <: AnyKList
  ](implicit
      l: AnyApp1At[pickS[E], T] { type Y = (X, TO) }
  )
  : AnyApp1At[pickS[E], H :: T] { type Y = (X, H :: TO) } =
    App1 { (s: H :: T) => val (e, t) = l(s.tail); (e, s.head :: t) }
}
