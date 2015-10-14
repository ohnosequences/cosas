package ohnosequences.cosas.klists

import ohnosequences.cosas.fns._

// NOTE L is here due to a scalac bug
class Concatenate[L <: AnyKList] extends DepFn2[
  L, AnyKList { type Bound = L#Bound },
  AnyKList { type Bound = L#Bound }
]
case object Concatenate {

  implicit def leftEmpty[L <: AnyKList { type Bound = A }, A]
  : App2[Concatenate[KNil[A]], KNil[A], L, L] =
    App2 { (n: KNil[A], l: L) => l }

  implicit def leftKCons[
    H <: T#Bound, T <: AnyKList, L <: AnyKList { type Bound = T#Bound }
  ](implicit
    concat: AnyApp2At[Concatenate[T], T, L]
  )
  : App2[Concatenate[H :: T], H :: T, L, H :: concat.Y] =
    App2 { (xs: H :: T, l: L) => xs.head :: concat(xs.tail,l) }
}
