package ohnosequences.cosas.typeSets

import ohnosequences.cosas._, fns._, typeSets._

class Take[Q <: AnyTypeSet] extends DepFn1[AnyTypeSet, Q]

case object Take {

  implicit def empty[S <: AnyTypeSet]: App1[Take[∅[S#Bound]],S,∅[S#Bound]] =
    App1 { s: S => ∅[S#Bound] }

  implicit def nonEmpty[
    TailToTake <: AnyTypeSet, From <: AnyTypeSet, Rest <: AnyTypeSet,
    HeadToTake <: TailToTake#Bound
  ](implicit
    pop: App1[pop[HeadToTake], From, (HeadToTake, Rest)],
    take: App1[Take[TailToTake], Rest, TailToTake]
  )
  : App1[Take[HeadToTake :~: TailToTake], From, HeadToTake :~: TailToTake] =
    App1 { s: From => { val (h, t) = pop(s); h :~: take(t) } }
}
