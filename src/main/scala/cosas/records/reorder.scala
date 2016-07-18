package ohnosequences.cosas.types

import ohnosequences.cosas._, types._, klists._, fns._

class Reorder[Ts <: AnyProductType, Vs <: AnyKList { type Bound <: AnyDenotation }] extends DepFn1[
  Vs,
  Ts#Raw
]

case object Reorder {

  // implicit def empty[S <: AnyKList { type Bound <: AnyDenotation { type Tpe <: T } }, T <: AnyType]
  // : AnyApp1At[Reorder[|[T], S], S] { type Y = *[AnyDenotation { type Tpe <: T }] } =
  //   App1 { s: S => *[AnyDenotation { type Tpe <: T }] }
//
//   implicit def nonEmpty[
//     TailToTake <: AnyProductType {
//       type Types <: AnyKList { type Bound >: HeadToTake <: AnyType }
//       type Raw <: AnyKList { type Bound >: (HeadToTake := HeadToTake#Raw) <: AnyDenotation { type Tpe <: Types#Bound } }
//     },
//     From <: AnyKList { type Bound <: AnyDenotation }, Rest <: AnyKList { type Bound <: AnyDenotation },
//     HeadToTake <: AnyType { type Raw >: V }, V,
//     STailToTake <: TailToTake#Raw
//   ](implicit
//     pick: AnyApp1At[pickByType[HeadToTake], From] { type Y = (HeadToTake := V, Rest) },
//     take: AnyApp1At[Reorder[TailToTake, Rest], Rest] { type Y = STailToTake }
//   )
//   : AnyApp1At[Reorder[HeadToTake × TailToTake, From], From] { type Y = (HeadToTake := V) :: STailToTake } =
//     App1 {
//       s: From => {
//         val (h, t): (HeadToTake := V, Rest) = pick(s)
//         h :: take(t)
//       }
//     }
// }
//
// class pickByType[T <: AnyType] extends DepFn1[
//   AnyKList { type Bound <: AnyDenotation},
//   (T := T#Raw, AnyKList { type Bound <: AnyDenotation})
// ]
//
// case object pickByType extends pickInTailReally {
//   implicit def foundInHead[
//     H <: AnyType, V <: H#Raw,
//     Ds <: AnyKList { type Bound >: (H := H#Raw) <: AnyDenotation }
//   ]
//   : AnyApp1At[
//       pickByType[H],
//       (H := V) :: Ds
//     ] { type Y = (H := V, Ds) } =
//     App1 { x: (H := V) :: Ds => (x.head, x.tail)  }
// }
//
// trait pickInTailReally {
//
// implicit def foundInTail[
//   H <: AnyType { type Raw >: V }, V,
//   Ds <: AnyKList { type Bound >: (H := V) <: AnyDenotation },
//   Rest <: AnyKList { type Bound >: (H := V) <: AnyDenotation },
//   P <: AnyType { type Raw >: W }, W
// ]
// (implicit
//   pick: AnyApp1At[pickByType[P], Ds] { type Y = (P := W, Rest) }
// )
// : AnyApp1At[
//   pickByType[P],
//   (H := V) :: Ds
// ] {type Y = (P := W, (H := V) :: Rest) } =
//   App1 { x: (H := V) :: Ds => (pick(x.tail)._1, x.head :: pick(x.tail)._2 ) }
}
