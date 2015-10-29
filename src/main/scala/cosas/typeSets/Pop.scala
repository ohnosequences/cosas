package ohnosequences.cosas.typeSets

import ohnosequences.cosas._, fns._

class Pop[E] extends DepFn1[AnyTypeSet, (E, AnyTypeSet)]

case object Pop extends pop_2 {

  implicit def foundInHead[E <: T#Bound, H <: E, T <: AnyTypeSet]: App1[Pop[E], H :~: T, (E, T)] =
    App1 { (s: H :~: T) => (s.head, s.tail) }
}

trait pop_2  {

  implicit def foundInTail[H, T <: AnyTypeSet { type Bound >: H }, E, TO <: AnyTypeSet { type Bound >: H }](implicit
    e: E ∈ T,
    l: App1[Pop[E], T, (E, TO)]
  )
  : App1[Pop[E], H :~: T, (E, H :~: TO)] =
    App1 { (s: H :~: T) => val (e, t) = l(s.tail); (e, s.head :~: t) }
}
