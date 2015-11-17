package ohnosequences.cosas.klists

import ohnosequences.cosas._, fns._

// TODO signature as in fold
class MapKListOf[
  F <: AnyDepFn1,
  Y
]
extends DepFn1[
  AnyKList,
  AnyKList { type Bound = Y }
]

case object MapKListOf {

  implicit def empty[
    F <: AnyDepFn1,
    A,
    B0
  ]
  : AnyApp1At[MapKListOf[F,B0],KNil[A]] { type Y = KNil[B0] } =
    App1 { (e: KNil[A]) => KNil[B0] }

  implicit def kcons[
    F <: AnyDepFn1 { type Out >: O },
    H <: InT#Bound, InT <: AnyKList { type Bound <: F#In1 },
    Y >: O,
    O
  ](implicit
    evF: AnyApp1At[F,H] { type Y = O },
    mapof: AnyApp1At[
      MapKListOf[F,Y],
      InT
    ]
  )
  : AnyApp1At[MapKListOf[F,Y], H :: InT] { type Y = O :: mapof.Y } =
    App1[MapKListOf[F,Y], H :: InT,  O :: mapof.Y] {
      (s: H :: InT) => evF(s.head) :: mapof(s.tail)
    }
}
