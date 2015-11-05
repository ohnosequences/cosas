// package ohnosequences.cosas.typeSets
//
// import ohnosequences.cosas._, fns._, types._
//
// class Lookup[A] extends DepFn1[AnyTypeSet, A]
//
// case object Lookup extends Lookup_2 {
//
//   implicit def foundInHead[A <: T#Bound, H <: A, T <: AnyTypeSet]: App1[Lookup[A], H :~: T, A] =
//     App1 { s: H :~: T => s.head }
// }
//
// trait Lookup_2 {
//
//   implicit def foundInTail[H <: T#Bound, T <: AnyTypeSet, A](implicit
//     e: A ∈ T,
//     lkp: App1[Lookup[A], T, A]
//   )
//   : App1[Lookup[A], H :~: T, A] =
//     App1 { s: H :~: T => lkp(s.tail) }
// }
