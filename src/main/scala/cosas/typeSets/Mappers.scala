// package ohnosequences.cosas.typeSets
//
// import ohnosequences.cosas._, fns._, typeSets._
// import shapeless.{HList,HNil,::}
//
// // TODO migrate to KList
// case object MapToHList extends DepFn2[AnyDepFn1, AnyTypeSet, shapeless.HList] {
//
//   implicit def empty[F <: In1, X]: App2[mapToHList,F,∅[X],HNil] =
//     mapToHList at { (f: F, e: ∅[X]) => HNil }
//
//   implicit def cons[
//     F <: In1 { type Out >: OutH },
//     H <: F#In1, T <: AnyTypeSet.Of[F#In1],
//     OutH, OutT <: HList
//   ](implicit
//     evF: App1[F,H,OutH],
//     evThis: App2[mapToHList,F,T,OutT]
//   )
//   : App2[mapToHList, F, H :~: T, OutH :: OutT] =
//     mapToHList at { (f: F, ht: H :~: T) => f(ht.head) :: mapToHList(f,ht.tail) }
// }
//
// class MapToListOf[A] extends DepFn2[AnyDepFn1, AnyTypeSet, List[A]]
//
// case object MapToListOf {
//
//   implicit def empty[F <: AnyDepFn1, X]: App2[MapToListOf[X],F,∅[X],List[X]] =
//     App2 { (f: F, e: ∅[X]) => Nil: List[X] }
//
//   implicit def nonEmpty[
//     F <: AnyDepFn1 { type Out >: X },
//     H1 <: F#In1, H2 <: T#Bound, T <: AnyTypeSet { type Bound = F#In1 },
//     X
//   ](implicit
//     evF: App1[F,H1,X],
//     maptolistof: App2[MapToListOf[X], F, H2 :~: T, List[X]]
//   )
//   : App2[MapToListOf[X], F, H1 :~: H2 :~: T, List[X]] =
//     App2 { (f: F, s: H1 :~: H2 :~: T) => f(s.head) :: maptolistof(f, s.tail) }
//
//   implicit def one[
//     F <: AnyDepFn1,
//     H <: F#In1,
//     X <: F#Out
//   ](implicit
//     evF: App1[F,H,X]
//   )
//   : App2[MapToListOf[X], F, H :~: ∅[F#In1], List[X]] =
//     App2 { (f: F, s: H :~: ∅[F#In1]) =>  List[X](f(s.head)) }
// }
//
// case object MapSet extends DepFn2[AnyDepFn1, AnyTypeSet, AnyTypeSet] {
//
//   implicit def empty[F <: In1,X]: App2[MapSet.type, F, ∅[X], ∅[X]] =
//     MapSet at { (f: F, s: ∅[X]) => ∅[X] }
//
//
//   implicit def nonEmpty[
//     // por qué señor por qué?
//     F <: In1 { type Out >: OutH },
//     H <: F#In1, T <: AnyTypeSet.Of[F#In1],
//     OutH, OutT <: AnyTypeSet.Of[F#Out]
//   ](implicit
//     evF: App1[F,H,OutH],
//     evMe: App2[MapSet.type, F, T, OutT],
//     e: OutH ∉ OutT  // the key check here
//   )
//   : App2[MapSet.type, F, H :~: T, OutH :~: OutT] =
//     MapSet at { (f: F, xs: H :~: T) => f(xs.head) :~: MapSet(f,xs.tail) }
// }
