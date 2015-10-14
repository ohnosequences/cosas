package ohnosequences.cosas.klists

import ohnosequences.cosas.fns._

// TODO review this signature
class FoldLeft[L <: AnyKList, F <: AnyDepFn2, Z <: F#Out] extends DepFn3[
  L, Z, F,
  F#Out
]

case object FoldLeft {

  implicit def empty[
    F <: AnyDepFn2 { type In2 = A; type Out >: Z },
    Z, A
  ]
  : AnyApp3At[FoldLeft[KNil[A],F,Z], KNil[A], Z, F] { type Y = Z } =
    App3 {
      (n: KNil[F#In2], z: Z, f: F) => z
    }

  implicit def cons[
    F <: AnyDepFn2 {
      type In1 = B; type In2 = A; type Out = B
    },
    // head cons
    H <: A, T <: AnyKList { type Bound = A },
    // zero
    Z <: B,
    O,
    A,
    B >: O
  ](implicit
    evF: AnyApp2At[F, O, H],
    foldLeft: AnyApp3At[FoldLeft[T,F,Z], T, Z, F] { type Y = O }
  )
  : App3[
      FoldLeft[H :: T,F,Z],
      H :: T,
      Z,F,
      evF.Y
    ] =
    App3 {
      (xs: H :: T, z: Z, f: F) => {

        val uh: H = xs.head
        val oh = foldLeft(xs.tail,z,f)
        evF(oh,uh)

      }
    }
}
