package ohnosequences.cosas.klists

import ohnosequences.cosas._, fns._

class at[L <: AnyKList, N <: AnyNat] extends DepFn1[L,L#Bound]

case object at {

  implicit def zero[H <: T#Bound, T <: AnyKList]
  : App1[(H :: T) at _0, H :: T, H] =
    App1 { s: H :: T => s.head }

  implicit def n[
    H <: T#Bound, T <: AnyKList { type Bound >: Z },
    N <: AnyNat,
    Z
  ](implicit
    atT: App1[T at N, T, Z]
  )
  : App1[(H :: T) at Successor[N], H :: T, Z] =
    App1 { ht: H :: T => atT(ht.tail)}
}
