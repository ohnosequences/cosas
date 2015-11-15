package ohnosequences.cosas.klists

import ohnosequences.cosas.fns._

// TODO review this signature
class FoldLeft[L <: AnyKList, F <: AnyDepFn2, Z] extends DepFn3[
  L, Z, F,
  F#Out
]

case object FoldLeft {

  implicit def empty[
    F <: AnyDepFn2 { type In2 >: A; type Out >: Z },
    Z, A
  ]
  : AnyApp3At[FoldLeft[KNil[A],F,Z], KNil[A], Z, F] { type Y = Z } =
    App3 {
      (n: KNil[F#In2], z: Z, f: F) => z
    }

  implicit def cons[
    F <: AnyDepFn2 { type In1 >: O; type In2 >: H; type Out >: FO },
    Z,
    H <: T#Bound, T <: AnyKList,
    O, FO
  ](implicit
    evF: AnyApp2At[F, O, H] { type Y = FO },
    foldLeft: AnyApp3At[FoldLeft[T,F,Z], T, Z, F] { type Y = O }
  )
  : AnyApp3At[
      FoldLeft[H :: T,F,Z],
      H :: T,
      Z,F
    ] { type Y = FO }=
    App3 {
      (xs: H :: T, z: Z, f: F) => {

        val uh: H = xs.head
        val oh = foldLeft(xs.tail,z,f)
        evF(oh,uh)
      }
    }
}
