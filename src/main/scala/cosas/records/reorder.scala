package ohnosequences.cosas.types

import ohnosequences.cosas._, types._, klists._, fns._

class Reorder[Ts <: AnyProductType, Vs <: AnyKList { type Bound <: AnyDenotation }] extends DepFn1[
  Vs,
  Ts#Raw
]

case object Reorder {

  // implicit def empty[S <: AnyKList { type Bound <: AnyDenotation }]
  // : AnyApp1At[Reorder[|[AnyType], S], S] { type Y = *[AnyDenotation] } =
  //   App1 { s: S => *[AnyDenotation] }
  //
  // implicit def nonEmpty[
  //   TailToTake <: AnyProductType.CompatibleWith[HeadToTake],
  //   From <: AnyKList { type Bound = AnyDenotation }, Rest <: AnyKList { type Bound = AnyDenotation },
  //   HeadToTake <: AnyType { type Raw >: V}, V,
  //   STailToTake <: AnyKList { type Bound = AnyDenotation }
  // ](implicit
  //   pick: AnyApp1At[pickByType[HeadToTake], From] { type Y = ((HeadToTake := V), Rest) },
  //   take: AnyApp1At[Reorder[TailToTake, Rest], Rest] { type Y = STailToTake }
  // )
  // : AnyApp1At[Reorder[HeadToTake × TailToTake, From], From] { type Y = (HeadToTake := V) :: STailToTake } =
  //   App1 { s: From => { val (h, t) = pick(s); h :: take(t) } }
}

class pickByType[T <: AnyType] extends DepFn1[
  AnyKList { type Bound = AnyDenotation},
  (AnyDenotation { type Tpe = T }, AnyKList { type Bound = AnyDenotation})
]

case object pickByType extends pickInTailReally {
  implicit def foundInHead[
    H <: AnyType { type Raw >: V }, V,
    Ds <: AnyKList { type Bound = AnyDenotation }
  ]
  : AnyApp1At[
      pickByType[H],
      (H := V) :: Ds
    ] { type Y = (H := V, Ds) } =
    App1 { x: (H := V) :: Ds => (x.head, x.tail)  }
}

trait pickInTailReally {

implicit def foundInTail[
  H <: AnyType { type Raw >: V }, V,
  Ds <: AnyKList { type Bound = AnyDenotation },
  Rest <: AnyKList { type Bound = AnyDenotation },
  P <: AnyType { type Raw >: W }, W
]
(implicit
  pick: AnyApp1At[pickByType[P], Ds] { type Y = (P := W, Rest) }
)
: AnyApp1At[
  pickByType[P],
  (H := V) :: Ds
] {type Y = (P := W, (H := V) :: Rest) } =
  App1 { x: (H := V) :: Ds => (pick(x.tail)._1, x.head :: pick(x.tail)._2 ) }
}
