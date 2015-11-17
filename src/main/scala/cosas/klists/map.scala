package ohnosequences.cosas.klists

import ohnosequences.cosas._, fns._

// TODO signature as in fold
class MapKListOf[F <: AnyDepFn1, Y <: F#Out] extends DepFn2[
  F,
  AnyKList,
  AnyKList { type Bound = Y }
]

case object MapKListOf extends WithSameType {

  implicit def empty[
    F <: AnyDepFn1 { type Out >: B },
    A,
    B
  ]
  : AnyApp2At[MapKListOf[F,B], F, KNil[A]] { type Y = KNil[B] } =
    App2 { (f: F, e: KNil[A]) => KNil[B] }

  implicit def kconsBis[
    F <: AnyDepFn1 { type Out >: U },
    H <: InT#Bound, InT <: AnyKList { type Bound <: F#In1 },
    U >: O,
    O
  ](implicit
    evF: AnyApp1At[F,H] { type Y = O },
    mapof: AnyApp2At[
      MapKListOf[F,U],
      F,
      InT
    ]
  )
  : AnyApp2At[MapKListOf[F,U], F, H :: InT] { type Y = O :: mapof.Y } =
    App2[MapKListOf[F,U], F, H :: InT, O :: mapof.Y] {
      (f: F, s: H :: InT) => evF(s.head) :: mapof(f,s.tail)
    }
}

trait WithSameType {

  implicit def kcons[
    F <: AnyDepFn1 { type Out >: O },
    H <: InT#Bound, InT <: AnyKList { type Bound <: F#In1 },
    O
  ](implicit
    evF: AnyApp1At[F,H] { type Y = O },
    mapof: AnyApp2At[
      MapKListOf[F,F#Out],
      F,
      InT
    ]
  )
  : AnyApp2At[MapKListOf[F,F#Out], F, H :: InT] { type Y = O :: mapof.Y } =
    App2[MapKListOf[F,F#Out], F, H :: InT, O :: mapof.Y] {
      (f: F, s: H :: InT) => evF(s.head) :: mapof(f,s.tail)
    }
}
