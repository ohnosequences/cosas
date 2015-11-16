package ohnosequences.cosas.klists

import ohnosequences.cosas.fns._

// TODO review this signature
/* - L for List
   - Z for Zero
   - F for Function
*/
class FoldLeft[L <: AnyKList, Z <: F#Out, F <: AnyDepFn2] extends DepFn3[
  L, Z, F,
  F#Out
]

case object FoldLeft {

  implicit def empty[
    F <: AnyDepFn2 { type In2 >: A; type Out >: Z },
    Z, A
  ]
  : AnyApp3At[FoldLeft[KNil[A], Z, F], KNil[A], Z, F] { type Y = Z } =
    App3 {
      (n: KNil[F#In2], z: Z, f: F) => z
    }

  implicit def cons[
    F <: AnyDepFn2 { type In1 >: O; type In2 >: H; type Out >: TO },
    Z <: F#Out,
    H <: T#Bound, T <: AnyKList,
    O, TO
  ](implicit
    foldLeft: AnyApp3At[
      FoldLeft[T, Z, F],
      T, Z, F
    ] { type Y = O },
    evF: AnyApp2At[F, O, H] { type Y = TO }
  )
  : AnyApp3At[
      FoldLeft[H :: T, Z, F],
      H :: T, Z, F
    ] { type Y = TO }=
    App3 {
      (xs: H :: T, z: Z, f: F) => {

        val uh: H = xs.head
        val oh = foldLeft(xs.tail,z,f)
        evF(oh,uh)
      }
    }
}
