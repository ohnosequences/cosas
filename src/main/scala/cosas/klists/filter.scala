package ohnosequences.cosas.klists

import ohnosequences.cosas._, fns._

class Filter[A] extends DepFn2[
  AnyDepFn1 { type In1 = A; type Out = AnyBool },
  AnyKList { type Bound = A },
  AnyKList { type Bound = A }
]

case object Filter extends SkipIfFalse {

  implicit def empty[P <: AnyDepFn1 { type Out = AnyBool; type In1 = A }, A]
  : App2[filter[A], P, KNil[A], KNil[A]] =
    filter[A] at { (p: P, nil: KNil[A]) => KNil[A] }

  implicit def nonEmpty[
    P <: AnyDepFn1 { type Out = AnyBool; type In1 = A },
    H <: A, T <: AnyKList { type Bound = A }, A
  ](implicit
    ev: AnyApp2At[filter[A], P, T],
    h: AnyApp1At[P, H] { type Y = True }
  )
  : App2[filter[A], P, H :: T, H :: ev.Y] =
    App2 { (p: P, hs: H :: T) => hs.head :: (ev(p,hs.tail): ev.Y) }
}

trait SkipIfFalse {

  implicit def skip[
    H <: A, T <: AnyKList { type Bound = A },
    P <: AnyDepFn1 { type Out = AnyBool; type In1 = A },
    A
    // TO <: AnyKList { type Bound = X0 }
  ](implicit
    ev: AnyApp2At[filter[A], P, T]
  )
  : App2[filter[A], P, H :: T, ev.Y] =
    App2 { (p: P, s: H :: T) => ev(p,s.tail) }
}
