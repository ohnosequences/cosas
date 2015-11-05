package ohnosequences.cosas.klists

import ohnosequences.cosas._, fns._

class take[N <: AnyNat] extends DepFn1[AnyKList, AnyKList]

case object take extends takeEmptyAtZero {

  implicit def atN[
    H <: Z0#Bound, T <: AnyKList { type Bound = Z0#Bound },
    Z0 <: AnyKList,
    N0 <: AnyNat
  ](implicit
    takeN: AnyApp1At[take[N0], T] { type Y = Z0 }
  )
  : AnyApp1At[take[Successor[N0]], H :: T] { type Y = H :: Z0 } =
    App1 { ht: H :: T => ht.head :: takeN(ht.tail) }
}

trait takeEmptyAtZero {

  implicit def atZero[L <: AnyKList]
  : AnyApp1At[take[_0], L] { type Y = KNil[L#Bound] } =
    App1 { l: L => KNil[L#Bound] }
}
