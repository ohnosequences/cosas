package ohnosequences.cosas.typeSets

import ohnosequences.cosas._, fns._, typeSets._
import shapeless.{HList,HNil,::}

case object MapToHList extends DepFn2[AnyDepFn1, AnyTypeSet, shapeless.HList] {

  implicit def empty[F <: In1]: App2[mapToHList,F,∅,HNil] =
    mapToHList at { (f: F, e: ∅) => HNil }

  implicit def cons[
    F <: In1,
    H <: F#In1, T <: In2,
    OutH <: F#Out, OutT <: Out
  ](implicit
    evF: App1[F,H,OutH],
    evThis: App2[mapToHList,F,T,OutT]
  )
  : App2[mapToHList, F, H :~: T, OutH :: OutT] =
    mapToHList at { (f: F, ht: H :~: T) => f(ht.head) :: mapToHList(f,ht.tail) }
}

class MapToListOf[A] extends DepFn2[AnyDepFn1 { type Out <: A }, AnyTypeSet, List[A]]

case object MapToListOf {

  implicit def empty[F <: AnyDepFn1 { type Out <: A}, A]: App2[mapToListOf[A],F,∅,List[A]] =
    App2 { (f: F, e: ∅) => Nil }

  implicit def nonEmpty[
    F <: AnyDepFn1 { type Out <: A },
    H <: F#In1, T <: AnyTypeSet,
    A0 <: F#Out, A
  ](implicit
    evF: App1[F,H,A0],
    maptolistof: App2[mapToListOf[A],F,T,List[A]]
  ): App2[mapToListOf[A], F, H :~: T, List[A]] =
    App2 { (f: F, s: H :~: T) => f(s.head) :: maptolistof(f, s.tail) }
}

case object MapSet extends DepFn2[AnyDepFn1, AnyTypeSet, AnyTypeSet] {

  implicit def empty[F <: In1]: App2[mapSet.type, F, ∅, ∅] =
    mapSet at { (f: F, s: ∅) => ∅ }

  implicit def nonEmpty[
    F <: In1,
    H <: F#In1, OutH <: F#Out,
    T <: In2, OutT <: Out
  ](implicit
    evF: App1[F,H,OutH],
    evMe: App2[mapSet.type, F, T, OutT],
    e: OutH ∉ OutT  // the key check here
  )
  : App2[mapSet.type, F, H :~: T, OutH :~: OutT] =
    mapSet at { (f: F, xs: H :~: T) => f(xs.head) :~: mapSet(f,xs.tail) }
}
