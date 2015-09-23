/* ## Taking a subset */

package ohnosequences.cosas.typeSets

import ohnosequences.cosas._, fns._, typeSets._

class take[Q <: AnyTypeSet] extends DepFn1[AnyTypeSet, Q]

case object take {

  implicit def empty[S <: AnyTypeSet]: App1[take[∅],S,∅] =
    App1 { s: S => ∅ }

  implicit def nonEmpty[
    S <: AnyTypeSet, S_ <: AnyTypeSet,
    H, T <: AnyTypeSet
  ](implicit
    pop: App1[pop[H], S, (E, T)],
    rest: App1[take[S_], S, S_]
  ): App1[take[S], H :~: T, S] =
    App1 { s: S => val (h, t) = pop(s); h :~: take[S_](t) }
}
