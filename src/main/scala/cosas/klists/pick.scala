package ohnosequences.cosas.klists

import ohnosequences.cosas._, fns._

// TODO a version with (Left, E, Right) as output
class Pick[E] extends DepFn1[AnyKList, (E, AnyKList)]

case object Pick extends PickFoundInTail {

  implicit def foundInHead[E <: T#Bound, H <: E, T <: AnyKList]
  : App1[Pick[E], H :: T, (E, T)] =
    App1 { (s: H :: T) => (s.head, s.tail) }
}

sealed trait PickFoundInTail {

  implicit def foundInTail[
    H, T <: AnyKList { type Bound >: H }, E,
    TO <: AnyKList { type Bound >: H }
  ](implicit
      l: App1[Pick[E], T, (E, TO)]
    )
    : App1[Pick[E], H :: T, (E, H :: TO)] =
      App1 { (s: H :: T) => val (e, t) = l(s.tail); (e, s.head :: t) }
}
