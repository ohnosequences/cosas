// package ohnosequences.cosas.typeSets
//
// import ohnosequences.cosas._, fns._, typeSets._
//
// trait union_1 extends DepFn2[AnyTypeSet, AnyTypeSet, AnyTypeSet] {
//
//   // use this, bound it at the end
//   type union <: this.type
//   val union: union
//
//   implicit def bothHeads[
//     SH <: ST#Bound, ST <: AnyTypeSet { type Bound = B },
//     QH <: QT#Bound, QT <: AnyTypeSet { type Bound = B },
//     O <: AnyTypeSet { type Bound = B },
//     B
//   ](implicit
//     sh: SH ∉ (QH :~: QT),
//     qh: QH ∉ (SH :~: ST),
//     rest: App2[union, ST, QT, O]
//   ): App2[union, SH :~: ST, QH :~: QT, SH :~: QH :~: O] =
//     union at { (s: SH :~: ST, q: QH :~: QT) => s.head :~: q.head :~: union(s.tail, q.tail) }
// }
// trait union_2 extends union_1 {
//
//   implicit def qHead[S <: AnyTypeSet, QH <: QT#Bound, QT <: AnyTypeSet, O <: AnyTypeSet](implicit
//     qh: QH ∈ S,
//     rest: App2[union, S, QT, O]
//   ): App2[union,S,QH :~: QT, O] =
//     union at { (s: S, q: QH :~: QT) => union(s, q.tail) }
// }
// trait union_3 extends union_2 {
//
//   implicit def sHead[SH <: ST#Bound, ST <: AnyTypeSet, Q <: AnyTypeSet, O <: AnyTypeSet](implicit
//     sh: SH ∈ Q,
//     rest: App2[union,ST,Q,O]
//   ): App2[union,SH :~: ST,Q,O] =
//     union at { (s: SH :~: ST, q: Q) => union(s.tail,q) }
// }
// trait union_4 extends union_3 {
//
//   implicit def qInS[S <: AnyTypeSet.SubsetOf[Q], Q <: AnyTypeSet]: App2[union,Q,S,Q] =
//     union at { (q:Q, s: S) => q }
// }
//
// case object Union extends union_4 {
//
//   type union = this.type
//   val union = this
//
//   implicit def sInQ[S <: AnyTypeSet.SubsetOf[Q], Q <: AnyTypeSet]: App2[union,S,Q,Q] =
//     union at { (s:S, q:Q) => q }
// }
