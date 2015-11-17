package ohnosequences.cosas.klists

import ohnosequences.cosas.fns._

case object Cons extends DepFn2[Any, AnyKList, AnyKList] {

  implicit def default[X <: L#Bound, L <: AnyKList]:
    AnyApp2At[Cons.type, X, L] { type Y = X :: L } =
    App2 { (x: X, l: L) => x :: l }
}

case object Snoc extends Flip[Cons.type]
