package ohnosequences.cosas.klists

import ohnosequences.cosas._, fns._

class drop[L <: AnyKList, N <: AnyNat] extends DepFn1[L, AnyKList]

case object drop {

  implicit def atZero[L <: AnyKList]
  : App1[L drop _0, L, L] =
    App1 { l: L => l }

  implicit def atN[
    H <: Z#Bound, T <: AnyKList { type Bound = Z#Bound },
    Z <: AnyKList,
    N <: AnyNat
  ](implicit
    dropN: App1[T drop N, T, Z]
  )
  : App1[(H :: T) drop Successor[N], H :: T, Z] =
    App1 { ht: H :: T => dropN(ht.tail) }
}
