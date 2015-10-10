package ohnosequences.cosas.products

import ohnosequences.cosas._, fns._

class MapKListOf[F <: AnyDepFn1 { type Out = Y; type In1 = X }, X,Y] extends DepFn2[
  AnyDepFn1,
  AnyKList { type Bound = X },
  AnyKList { type Bound = Y }
]

case object MapKListOf {

  implicit def empty[
    F <: AnyDepFn1 {type In1 = A; type Out = B0 },
    A,
    B0
  ]
  : AnyApp2At[MapKListOf[F,A,B0],F,KNil[A]] { type Y = KNil[B0] } =
    App2 { (f: F, e: KNil[A]) => KNil[B0] }

  implicit def kcons[
    F <: AnyDepFn1 { type In1 = A; type Out = B },
    A, B >: O,
    H <: F#In1, InT <: AnyKList { type Bound = A },
    O
  ](implicit
    evF: App1[F,H,O],
    mapof: AnyApp2At[
      MapKListOf[F,A,B],
      F, InT
    ]
  )
  : AnyApp2At[MapKListOf[F,A,B], F, H :: InT] { type Y = O :: mapof.Y } =
    new AnyApp2At[MapKListOf[F,A,B], F, H :: InT] {

      type Y = O :: mapof.Y

      def apply(f: F, s: H :: InT): Y = f(s.head) :: mapof(f,s.tail)
    }
}
