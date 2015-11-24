package ohnosequences.cosas.klists

import ohnosequences.cosas.fns._

case object Cons extends DepFn2[Any, AnyKList, AnyKList] {

  implicit def default[X <: L#Bound, L <: AnyKList]:
    AnyApp2At[cons, X, L] { type Y = X :: L } =
    App2 { (x: X, l: L) => x :: l }
}

case object Snoc extends Flip[cons]

case object Uncons extends DepFn1[AnyKList, (Any,AnyKList)] {

  implicit def default[H <: T#Bound, T <: AnyKList]
  : AnyApp1At[Uncons.type, H :: T] { type Y = (H,T) } =
    App1 { ht: H :: T => (ht.head, ht.tail) }
}
