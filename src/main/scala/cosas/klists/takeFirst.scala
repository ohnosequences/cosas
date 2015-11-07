package ohnosequences.cosas.klists

import ohnosequences.cosas._, fns._

class TakeFirst[Q <: AnyKList] extends DepFn1[AnyKList, Q]

case object TakeFirst {

  implicit def empty[S <: AnyKList { type Bound = X }, X]
  : AnyApp1At[TakeFirst[*[X]], S] { type Y = *[X] } =
    App1 { s: S => *[X] }

  implicit def nonEmpty[
    TailToTake <: AnyKList { type Bound >: HeadToTake }, From <: AnyKList, Rest <: AnyKList,
    HeadToTake
  ](implicit
    pick: AnyApp1At[pick[HeadToTake], From] { type Y = (HeadToTake, Rest) },
    take: AnyApp1At[TakeFirst[TailToTake], Rest] { type Y = TailToTake }
  )
  : AnyApp1At[TakeFirst[HeadToTake :: TailToTake], From] { type Y = HeadToTake :: TailToTake } =
    App1 { s: From => { val (h, t) = pick(s); h :: take(t) } }
}
