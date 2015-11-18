package ohnosequences.cosas.klists

import ohnosequences.cosas._, fns._

class drop[N <: AnyNat] extends DepFn1[AnyKList, AnyKList]

case object drop extends dropAtN {

  implicit def atN[
    H <: Z#Bound, T <: AnyKList { type Bound = Z#Bound },
    Z <: AnyKList,
    N0 <: AnyNat
  ](implicit
    dropN: AnyApp1At[drop[N0], T] {type Y = Z }
  )
  : AnyApp1At[drop[Successor[N0]], H :: T] { type Y = Z } =
    App1 { ht: H :: T => dropN(ht.tail) }
}

trait dropAtN {

  implicit def atZero[L <: AnyKList]
  : AnyApp1At[drop[_0], L] { type Y = L } =
    App1 { l: L => l }
}
