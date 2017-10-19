package ohnosequences.cosas.klists

import ohnosequences.cosas._, typeUnions._

sealed trait AnyKList extends Any {

  type Bound

  type Length <: AnyNat

  type AllTypes <: AnyTypeUnion
  // NOTE: should be AllTypes#union, but we can't set it here; scalac bugs
  type Union >: AllTypes#union <: AllTypes#union
}

sealed trait AnyEmptyKList extends Any with AnyKList {

  type AllTypes = TypeUnion.empty
  type Union = AllTypes#union

  type Length = _0
}

case class KNilOf[+A]() extends AnyEmptyKList {

  type Bound = A @uv
}

sealed trait AnyNonEmptyKList extends Any with AnyKList {

  type Head <: Bound
  def  head: Head

  type Tail <: AnyKList
  def  tail: Tail

  type Bound >: Tail#Bound <: Tail#Bound // NOTE again this is for forcing type inference

  type AllTypes = Tail#AllTypes#or[Head]
  type Union = AllTypes#union

  type Length = Successor[Tail#Length]
}

case object AnyNonEmptyKList {

  type Of[+B] = AnyNonEmptyKList { type Bound <: B }
  type withBound[B] = AnyNonEmptyKList { type Bound = B }
}

case class KCons[+H <: T#Bound, +T <: AnyKList](val head: H, val tail: T) extends AnyNonEmptyKList {

  type Bound = T#Bound @uv
  type Head = H @uv
  type Tail = T @uv
}

case object AnyKList {

  type Of[+B] = AnyKList { type Bound <: B }
  type withBound[B] = AnyKList { type Bound = B }

  implicit def klistSyntax[L <: AnyKList](l: L)
  : syntax.KListSyntax[L] =
    syntax.KListSyntax[L](l)

  def fromList[X](l: List[X]): AnyKList { type Bound = X } =
    fromList_rec(l.reverse, *[X])

  @scala.annotation.tailrec
  private def fromList_rec[X](
    reversedList: List[X],
    acc: AnyKList.withBound[X]
  ): AnyKList.withBound[X] = reversedList match {
    case x :: xs  => fromList_rec[X](xs, x :: acc)
    case Nil      => acc
  }

}
