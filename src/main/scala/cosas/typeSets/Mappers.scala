package ohnosequences.cosas.typeSets

import ohnosequences.cosas._, fns._, typeSets._

case object mapToHList extends DepFn2[AnyDepFn1, AnyTypeSet, shapeless.HList] {

  implicit def empty[F <: In1]: App2[MapToHList.type,F,∅,HNil] =
    mapToHList at { (f: F, e: ∅) => HNil }

  implicit def cons[
    F <: In1,
    H <: F#In1, T <: In2,
    OutH <: F#Out, OutT <: Out
  ](implicit
    evF: App1[F,H,OutH],
    evThis: App2[mapToHList.type,F,T,OutT]
  )
  : App2[MapToHList.type, F, H :~: T, OutH :: OutT] =
    mapToHList at { (f: F, ht: H :~: T) => f(ht.head) :: mapToHList(f,ht.tail) }
}

class mapToListOf[X] extends DepFn2[AnyDepFn1 { type Out <: X }, AnyTypeSet, List[X]] {

  implicit def empty[F <: In1]: App2[mapToList.type,F,∅,List[X]] =
    mapToList at { (f: F, e: ∅) => Nil }

  implicit def nonEmpty[
    F <: In1,
    H <: F#In1, T <: In2,
    OutH <: X
  ](implicit
    evF: App1[F,H,OutH],
    maptolistof: App2[mapToListOf[X],F,T,List[X]]
  ): App2[mapToList[X],F,H :~: T,List[X]] =
    App2 { (f: F, s: H :~: T) => f(s.head) :: maptolistof(s.tail) }
}

/* Mapping a set to another set, i.e. the results of mapping should have distinct types */
@annotation.implicitNotFound(msg = "Can't map ${F} over ${S} (maybe the resulting types are not distinct)")
trait MapSet[F <: Poly1, S <: AnyTypeSet] extends Fn1[S] with OutBound[AnyTypeSet]

object MapSet {

  def apply[F <: Poly1, S <: AnyTypeSet]
    (implicit mapper: MapSet[F, S]): MapSet[F, S] = mapper

  implicit def empty[F <: Poly1]:
        MapSet[F, ∅] with Out[∅] =
    new MapSet[F, ∅] with Out[∅] { def apply(s: ∅): Out = ∅ }

  implicit def cons[
    F <: Poly1,
    H, OutH,
    T <: AnyTypeSet, OutT <: AnyTypeSet
  ](implicit
    h: Case1.Aux[F, H, OutH],
    t: MapSet[F, T] { type Out = OutT },
    e: OutH ∉ OutT  // the key check here
  ):  MapSet[F, H :~: T] with Out[OutH :~: OutT] =
  new MapSet[F, H :~: T] with Out[OutH :~: OutT] {

    def apply(s: H :~: T): Out = h(s.head) :~: t(s.tail)
  }
}

/*
Map-folder for sets

Just a copy of MapFolder for `HList`s from shapeless.
It can be done as a combination of MapToList and list fold, but we don't want traverse it twice.
*/
trait MapFoldSet[F <: Poly1, S <: AnyTypeSet, R]
  extends Fn3[S, R, (R, R) => R] with Out[R]

object MapFoldSet {

  def apply[F <: Poly1, S <: AnyTypeSet, R]
    (implicit mapFolder: MapFoldSet[F, S, R]): MapFoldSet[F, S, R] = mapFolder

  implicit def empty[F <: Poly1, R]:
        MapFoldSet[F, ∅, R] =
    new MapFoldSet[F, ∅, R] {

      def apply(s: ∅, in: R, op: (R, R) => R): R = in
    }

  implicit def cons[F <: Poly1, H, T <: AnyTypeSet, R]
    (implicit
      hc: Case.Aux[F, H :: HNil, R],
      tf: MapFoldSet[F, T, R]
    ):  MapFoldSet[F, H :~: T, R] =
    new MapFoldSet[F, H :~: T, R] {

      def apply(s: H :~: T, in: R, op: (R, R) => R): R = op(hc(s.head), tf(s.tail, in, op))
    }
}
