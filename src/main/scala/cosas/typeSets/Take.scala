package ohnosequences.cosas.typeSets

import ohnosequences.cosas._, fns._, typeSets._

class Take[Q <: AnyTypeSet] extends DepFn1[AnyTypeSet, Q]

case object Take {

  implicit def empty[S <: AnyTypeSet]: App1[Take[∅],S,∅] =
    App1 { s: S => ∅ }

  implicit def nonEmpty[
    TailToTake <: AnyTypeSet, From <: AnyTypeSet, Rest <: AnyTypeSet,
    HeadToTake
  ](implicit
    pop: App1[pop[HeadToTake], From, (HeadToTake, Rest)],
    take: App1[Take[TailToTake], Rest, TailToTake]
  )
  : App1[Take[HeadToTake :~: TailToTake], From, HeadToTake :~: TailToTake] =
    App1 { s: From => { val (h, t) = pop(s); h :~: take(t) } }
}
