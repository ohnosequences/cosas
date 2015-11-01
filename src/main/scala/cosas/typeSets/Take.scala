package ohnosequences.cosas.typeSets

import ohnosequences.cosas._, fns._, klists._

class Take[Q <: AnyTypeSet] extends DepFn1[AnyTypeSet, Q]

case object Take {

  implicit def empty[S <: AnyTypeSet]: App1[Take[∅[S#Elements#Bound]],S,∅[S#Elements#Bound]] =
    App1 { s: S => ∅[S#Elements#Bound] }

  // implicit def nonEmpty[
  //   TailToTake <: AnyKList, From <: AnyKList, Rest <: AnyKList,
  //   HeadToTake <: TailToTake#Elements#Bound
  // ](implicit
  //   pop: App1[pop[HeadToTake], From, (HeadToTake, Rest)],
  //   take: App1[Take[TailToTake], Rest, TailToTake]
  // )
  // : App1[Take[HeadToTake :~: TailToTake], From, HeadToTake :~: TailToTake] =
  //   App1 { s: From => { val (h, t) = pop(s); h :~: take(t) } }
}
