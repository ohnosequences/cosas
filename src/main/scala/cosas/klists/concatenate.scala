package ohnosequences.cosas.klists

import ohnosequences.cosas.fns._

// NOTE L is here due to a scalac bug
class Concatenate[L <: AnyKList] extends DepFn2[
  L, AnyKList,
  AnyKList
]
case object Concatenate {

  implicit def leftEmpty[L <: AnyKList { type Bound <: A }, A]
  : AnyApp2At[Concatenate[KNil[A]], KNil[A], L] { type Y = L } =
    App2 { (n: KNil[A], l: L) => l }

  implicit def leftKCons[
    H <: T#Bound, T <: AnyKList { type Bound = O#Bound }, L <: AnyKList { type Bound <: T#Bound },
    O <: AnyKList

  ](implicit
    concat: AnyApp2At[Concatenate[T], T, L] { type Y = O }
  )
  : AnyApp2At[Concatenate[H :: T], H :: T, L] { type Y = H :: O } =
    App2 { (xs: H :: T, l: L) => xs.head :: concat(xs.tail,l) }
}

case object cons extends DepFn2[Any, AnyKList, AnyKList] {

  implicit def default[X <: L#Bound, L <: AnyKList]: AnyApp2At[cons.type, X, L] { type Y = X :: L } =
    App2 { (x: X, l: L) => x :: l }
}


case object snoc extends DepFn2[AnyKList, Any, AnyKList] {

  implicit def default[X <: L#Bound, L <: AnyKList]: AnyApp2At[snoc.type, L, X] { type Y = X :: L } =
    App2 { (l: L, x: X) => x :: l }
}
