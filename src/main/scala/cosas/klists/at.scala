package ohnosequences.cosas.klists

import ohnosequences.cosas._, fns._

class at[L <: AnyKList, N <: AnyNat] extends DepFn1[L,L#Bound]

case object at {

  implicit def atZero[H <: T#Bound, T <: AnyKList]
  : App1[(H :: T) at _0, H :: T, H] =
    App1 { s: H :: T => s.head }

  implicit def atN[
    H <: T#Bound, T <: AnyKList { type Bound >: Z},
    N <: AnyNat,
    Z
  ](implicit
    atT: App1[T at N, T, Z]
  )
  : App1[(H :: T) at Successor[N], H :: T, Z] =
    App1 { ht: H :: T => atT(ht.tail)}
}

// trait FoundInTail {
//
//   implicit def foundInTail[
//     H <: T#Bound,
//     T <: AnyKList { type Bound >: A },
//     A
//   ](implicit
//     findIn: App1[A FindIn T, T, A]
//   )
//   : App1[A FindIn (H :: T), H :: T, A] =
//     App1 { s: H :: T => findIn(s.tail) }
// }
