package ohnosequences.cosas.klists

import ohnosequences.cosas._, fns._

class MapKListOf[
  F <: AnyDepFn1,
  X >: F#In1 <: F#In1, Y >: F#Out <: F#Out
]
extends DepFn1[
  AnyKList { type Bound = X },
  AnyKList { type Bound = Y }
]

case object MapKListOf {

  implicit def empty[
    F <: AnyDepFn1 { type In1 = A; type Out = B0 },
    A,
    B0
  ]
  : AnyApp1At[MapKListOf[F,A,B0],KNil[A]] { type Y = KNil[B0] } =
    App1 { (e: KNil[A]) => KNil[B0] }

  implicit def kcons[
    F <: AnyDepFn1 { type In1 = A; type Out = B },
    A, B >: O,
    H <: F#In1, InT <: AnyKList { type Bound = A },
    O, OutT <: AnyKList { type Bound = B }
  ](implicit
    evF: App1[F,H,O],
    mapof: AnyApp1At[
      MapKListOf[F,A,B],
      InT
    ]
  )
  : AnyApp1At[MapKListOf[F,A,B], H :: InT] { type Y = O :: mapof.Y } =
    App1[MapKListOf[F,A,B], H :: InT,  O :: mapof.Y] {
      (s: H :: InT) => evF(s.head) :: mapof(s.tail)
      //
      // type Y = O :: mapof.Y
      //
      // def apply(f: F, s: H :: InT): Y = f(s.head) :: mapof(f,s.tail)
    }
}
