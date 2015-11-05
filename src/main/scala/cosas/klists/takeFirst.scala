package ohnosequences.cosas.klists

import ohnosequences.cosas._, fns._, klists._

class TakeFirst[Q <: AnyKList] extends DepFn1[AnyKList, Q]

case object TakeFirst {

  implicit def empty[S <: AnyKList { type Bound = X }, X]
  : App1[TakeFirst[*[X]], S, *[X]] =
    App1 { s: S => *[X] }

  implicit def nonEmpty[
    TailToTake <: AnyKList, From <: AnyKList, Rest <: AnyKList,
    HeadToTake <: TailToTake#Bound
  ](implicit
    pick: App1[pick[HeadToTake], From, (HeadToTake, Rest)],
    take: App1[TakeFirst[TailToTake], Rest, TailToTake]
  )
  : App1[TakeFirst[HeadToTake :: TailToTake], From, HeadToTake :: TailToTake] =
    App1 { s: From => { val (h, t) = pick(s); h :: take(t) } }
}
