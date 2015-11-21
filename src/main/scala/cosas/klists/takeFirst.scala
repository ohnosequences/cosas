package ohnosequences.cosas.klists

import ohnosequences.cosas._, fns._

// TODO this is not a good name. l pick /[Int :: String :: *[Any]]
class TakeFirst[Q <: AnyKList] extends DepFn1[AnyKList, Q]

case object TakeFirst {

  implicit def empty[S <: AnyKList { type Bound = X }, X]
  : AnyApp1At[TakeFirst[*[X]], S] { type Y = *[X] } =
    App1 { s: S => *[X] }

  implicit def nonEmpty[
    TailToTake <: AnyKList { type Bound >: HeadToTake }, From <: AnyKList, Rest <: AnyKList,
    HeadToTake >: SHeadToTake, SHeadToTake
  ](implicit
    pick: AnyApp1At[pickS[HeadToTake], From] { type Y = (SHeadToTake, Rest) },
    take: AnyApp1At[TakeFirst[TailToTake], Rest] { type Y = TailToTake }
  )
  : AnyApp1At[TakeFirst[HeadToTake :: TailToTake], From] { type Y = SHeadToTake :: TailToTake } =
    App1 { s: From => { val (h, t) = pick(s); h :: take(t) } }
}

// class TakeFirstS[Q <: AnyKList] extends DepFn1[AnyKList, Q]
//
// case object TakeFirstS {
//
//   implicit def empty[S <: AnyKList, X0 <: X, X]
//   : AnyApp1At[TakeFirstS[*[X]], S] { type Y = *[X0] } =
//     App1 { s: S => *[X0] }
//
//   implicit def nonEmpty[
//     From <: AnyKList, Rest <: AnyKList,
//     SHeadToTake, HeadToTake >: SHeadToTake <: TailToTake#Bound,
//     STailToTake <: TailToTake, TailToTake >: STailToTake <: AnyKList { type Bound >: SHeadToTake }
//   ](implicit
//     pick: AnyApp1At[pickS[HeadToTake], From] { type Y = (SHeadToTake, Rest) },
//     take: AnyApp1At[TakeFirstS[TailToTake], Rest] { type Y = STailToTake }
//   )
//   : AnyApp1At[TakeFirstS[HeadToTake :: TailToTake], From] { type Y = SHeadToTake :: STailToTake } =
//     App1 { s: From => { val (h, t): (SHeadToTake, Rest) = pick(s); h :: take(t) } }
// }
