package ohnosequences.cosas.typeSets

import ohnosequences.cosas._, fns._

class reorderTo[Q <: AnyTypeSet] extends DepFn1[AnyTypeSet,Q]
case object reorderTo {

  implicit def any[S <: AnyTypeSet, O <: AnyTypeSet]
    (implicit
      eq: S ~:~ O,
      take: App1[take[O], S, O]
    ): App1[reorderTo[O], S, O] =
      App1 { (s: S) => take(s) }
}
