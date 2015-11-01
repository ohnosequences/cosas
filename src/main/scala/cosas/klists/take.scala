package ohnosequences.cosas.klists

import ohnosequences.cosas._, fns._

class take[L <: AnyKList, N <: AnyNat] extends DepFn1[L, AnyKList]

case object take {

  implicit def atZero[L <: AnyKList]
  : App1[L take _0, L, KNil[L#Bound]] =
    App1 { l: L => KNil[L#Bound] }

  implicit def atN[
    H <: Z#Bound, T <: AnyKList { type Bound = Z#Bound },
    Z <: AnyKList,
    N <: AnyNat
  ](implicit
    takeN: App1[T take N, T, Z]
  )
  : App1[(H :: T) take Successor[N], H :: T, H :: Z] =
    App1 { ht: H :: T => ht.head :: takeN(ht.tail) }
}
