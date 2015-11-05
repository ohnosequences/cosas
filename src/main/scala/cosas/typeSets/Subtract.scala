// // /* ## Subtract one set from another */
//
// package ohnosequences.cosas.typeSets
//
// import ohnosequences.cosas._, fns._, typeSets._
//
//
// case object Subtract extends DepFn2[AnyTypeSet, AnyTypeSet, AnyTypeSet] with Subtract_2 {
//
//   type Me = this.type
//
//   implicit def sInQ[S <: AnyTypeSet.SubsetOf[Q], Q <: AnyTypeSet, X]
//     : App2[Me, S, Q, ∅[X]] =
//       App2 { (s: S, q: Q) => ∅[X] }
// }
//
// trait Subtract_2 extends Subtract_3 {
//
//   /* * Case when Q is empty => result is S: */
//   implicit def qEmpty[S <: AnyTypeSet, X]
//   : App2[Me, S, ∅[X], S] =
//     App2 { (s: S, q: ∅[X]) => s }
//
//     /* * Case when S.head ∈ Q => result is S.tail \ Q: */
//     implicit def sConsWithoutHead[
//       H <: T#Bound, T <: AnyTypeSet,
//       Q <: AnyTypeSet, TO <: AnyTypeSet
//     ]
//     (implicit
//       h: H ∈ Q,
//       rest: App2[Me,T,Q,TO]
//     )
//     : App2[Me, H :~: T, Q, TO] =
//       App2 { (s: H :~: T, q: Q) => rest(s.tail, q) }
// }
//
// trait Subtract_3 extends DepFn2[AnyTypeSet, AnyTypeSet, AnyTypeSet] {
//
//   type Me <: this.type
//
//   implicit def sConsAnyHead[
//     H <: T#Bound, T <: AnyTypeSet,
//     Q <: AnyTypeSet, TO <: AnyTypeSet { type Bound >: H }
//   ](implicit
//     h: H ∉ Q,
//     rest: App2[Me,T,Q,TO]
//   )
//   : App2[Me, H :~: T, Q, H :~: TO] =
//     App2 { (s: H :~: T, q: Q) => s.head :~: rest(s.tail, q) }
// }
//
//
//
//
// //
// //
// // @annotation.implicitNotFound(msg = "Can't subtract ${Q} from ${S}")
// // trait Subtract[S <: AnyTypeSet, Q <: AnyTypeSet] extends Fn2[S, Q] with OutBound[AnyTypeSet]
// //
// // /* * Case when S is inside Q => result is ∅: */
// // object Subtract extends SubtractSets_2 {
// //
// //   def apply[S <: AnyTypeSet, Q <: AnyTypeSet]
// //     (implicit sub: S Subtract Q): S Subtract Q = sub
// //
// //   implicit def sInQ[S <: AnyTypeSet.SubsetOf[Q], Q <: AnyTypeSet]:
// //         (S Subtract Q) with Out[∅] =
// //     new (S Subtract Q) with Out[∅] { def apply(s: S, q: Q) = ∅ }
// // }
// //
// // trait SubtractSets_2 extends SubtractSets_3 {
// //   /* * Case when Q is empty => result is S: */
// //   implicit def qEmpty[S <: AnyTypeSet]:
// //         (S Subtract ∅) with Out[S] =
// //     new (S Subtract ∅) with Out[S] { def apply(s: S, q: ∅) = s }
// //
// //   /* * Case when S.head ∈ Q => result is S.tail \ Q: */
// //   implicit def sConsWithoutHead[H, T <: AnyTypeSet,  Q <: AnyTypeSet, TO <: AnyTypeSet]
// //     (implicit
// //       h: H ∈ Q,
// //       rest: (T \ Q) { type Out = TO }
// //     ):  ((H :~: T) Subtract Q) with Out[TO] =
// //     new ((H :~: T) Subtract Q) with Out[TO] { def apply(s: H :~: T, q: Q) = rest(s.tail, q) }
// // }
// //
// // /* * Case when we just leave S.head and traverse further: */
// // trait SubtractSets_4 {
// //
// //   implicit def sConsAnyHead[H, T <: AnyTypeSet, Q <: AnyTypeSet, TO <: AnyTypeSet]
// //     (implicit
// //       h: H ∉ Q,
// //       rest: (T \ Q) { type Out = TO }
// //     ):  ((H :~: T) Subtract Q) with Out[H :~: TO] =
// //     new ((H :~: T) Subtract Q) with Out[H :~: TO] { def apply(s: H :~: T, q: Q) = s.head :~: rest(s.tail, q) }
// // }
