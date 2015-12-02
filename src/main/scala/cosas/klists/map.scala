package ohnosequences.cosas.klists

import ohnosequences.cosas._, fns._

class mapKList[F <: AnyDepFn1, Y <: F#Out] extends DepFn2[
  F,
  AnyKList,
  AnyKList { type Bound = Y }
]

case object mapKList extends WithSameType {

  def fromList[X](l: List[X]): AnyKList { type Bound = X } = fromList_rec(l,*[X])

  @scala.annotation.tailrec
  private def fromList_rec[X](
    list: List[X],
    acc: AnyKList.withBound[X]
  )
  : AnyKList.withBound[X] = list.reverse match {
    case x :: xs  => fromList_rec[X](xs, x :: acc)
    case Nil      => acc
  }

  implicit def optimizeFn1[
    A0 >: L#Bound, B, L <: AnyKList
  ]
  : AnyApp2At[mapKList[Fn1[A0,B], B], Fn1[A0,B], L] { type Y = AnyKList.withBound[B] } =
    App2 { (fn: Fn1[A0,B], l: L) => fromList( (l.asList: List[A0]) map fn.f ) }

  implicit def empty[
    F <: AnyDepFn1 { type Out >: B },
    A,
    B
  ]
  : AnyApp2At[mapKList[F,B], F, KNil[A]] { type Y = KNil[B] } =
    App2 { (f: F, e: KNil[A]) => KNil[B] }

  implicit def kconsBis[
    F <: AnyDepFn1 { type Out >: U },
    H <: InT#Bound, InT <: AnyKList { type Bound <: F#In1 },
    U >: O,
    O
  ](implicit
    evF: AnyApp1At[F,H] { type Y = O },
    mapof: AnyApp2At[
      mapKList[F,U],
      F,
      InT
    ]
  )
  : AnyApp2At[mapKList[F,U], F, H :: InT] { type Y = O :: mapof.Y } =
    App2[mapKList[F,U], F, H :: InT, O :: mapof.Y] {
      (f: F, s: H :: InT) => evF(s.head) :: mapof(f,s.tail)
    }
}

trait WithSameType {

  implicit def kcons[
    F <: AnyDepFn1 { type Out >: O },
    H <: InT#Bound, InT <: AnyKList { type Bound <: F#In1 },
    O
  ](implicit
    evF: AnyApp1At[F,H] { type Y = O },
    mapof: AnyApp2At[
      mapKList[F,F#Out],
      F,
      InT
    ]
  )
  : AnyApp2At[mapKList[F,F#Out], F, H :: InT] { type Y = O :: mapof.Y } =
    App2[mapKList[F,F#Out], F, H :: InT, O :: mapof.Y] {
      (f: F, s: H :: InT) => evF(s.head) :: mapof(f,s.tail)
    }
}
