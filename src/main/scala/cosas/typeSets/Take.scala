/* ## Taking a subset */

package ohnosequences.cosas.typeSets

import ohnosequences.cosas._, fns._, typeSets._

class take[Q <: AnyTypeSet] extends DepFn1[AnyTypeSet, Q]

case object take {

  implicit def empty[S <: AnyTypeSet]: App1[take[∅],S,∅] =
    App1 { s: S => ∅ }

  implicit def nonEmpty[
    TailToTake <: AnyTypeSet, From <: AnyTypeSet, Rest <: AnyTypeSet,
    HeadToTake
  ](implicit
    pop: App1[pop[HeadToTake], From, (HeadToTake, Rest)],
    take: App1[take[TailToTake], Rest, TailToTake]
  )
  : App1[take[HeadToTake :~: TailToTake], From, HeadToTake :~: TailToTake] =
    App1 { s: From => { val (h, t) = pop(s); h :~: take(t) } }
}
