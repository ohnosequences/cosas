package ohnosequences.cosas.klists

import ohnosequences.cosas.fns._

case object cons extends DepFn2[Any, AnyKList, AnyKList] {

  implicit def default[X <: L#Bound, L <: AnyKList]: AnyApp2At[cons.type, X, L] { type Y = X :: L } =
    App2 { (x: X, l: L) => x :: l }
}


case object snoc extends DepFn2[AnyKList, Any, AnyKList] {

  implicit def default[X <: L#Bound, L <: AnyKList]: AnyApp2At[snoc.type, L, X] { type Y = X :: L } =
    App2 { (l: L, x: X) => x :: l }
}
