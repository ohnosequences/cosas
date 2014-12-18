/* ## Sum/union of two type sets */

package ohnosequences.cosas.ops.typeSet

import ohnosequences.cosas._, AnyFn._, AnyTypeSet._

@annotation.implicitNotFound(msg = "Can't union ${S} with ${Q}")
trait Union[S <: AnyTypeSet, Q <: AnyTypeSet] extends Fn2[S, Q] with OutBound[AnyTypeSet]

/* * Case when S is a subset of Q => just Q: */
object Union extends UnionSets_2 {

  def apply[S <: AnyTypeSet, Q <: AnyTypeSet]
    (implicit uni: Union[S, Q]): Union[S, Q] = uni

  implicit def sInQ[S <: AnyTypeSet.SubsetOf[Q], Q <: AnyTypeSet]:
        Union[S, Q] with Out[Q] =
    new Union[S, Q] with Out[Q] { def apply(s: S, q: Q) = q }
}

/* * (Dual) case when Q is a subset of S => just S: */
trait UnionSets_2 extends UnionSets_3 {
  implicit def qInS[S <: AnyTypeSet, Q <: AnyTypeSet.SubsetOf[S]]:
        Union[S, Q] with Out[S] =
    new Union[S, Q] with Out[S] { def apply(s: S, q: Q) = s }
}

/* * Case when S.head is in Q => throwing it away: */
trait UnionSets_3 extends UnionSets_4 {
  implicit def sHead[SH, ST <: AnyTypeSet, Q <: AnyTypeSet, O <: AnyTypeSet]
    (implicit 
      sh: SH ∈ Q, 
      rest: (ST ∪ Q) { type Out = O }
    ):  Union[SH :~: ST, Q] with Out[O] =
    new Union[SH :~: ST, Q] with Out[O] {

      def apply(s: SH :~: ST, q: Q) = rest(s.tail, q)
    }
}

/* * (Dual) case when Q.head is in S => throwing it away: */
trait UnionSets_4 extends UnionSets_5 {
  implicit def qHead[S <: AnyTypeSet, QH, QT <: AnyTypeSet, O <: AnyTypeSet]
    (implicit
      qh: QH ∈ S, 
      rest: (S ∪ QT) { type Out = O }
    ):  Union[S, QH :~: QT] with Out[O] =
    new Union[S, QH :~: QT] with Out[O] {

      def apply(s: S, q: QH :~: QT) = rest(s, q.tail)
    }
}

/* * Otherwise both heads are new => adding both: */
trait UnionSets_5 {
  implicit def bothHeads[SH, ST <: AnyTypeSet, QH, QT <: AnyTypeSet, O <: AnyTypeSet]
    (implicit
      sh: SH ∉ (QH :~: QT), 
      qh: QH ∉ (SH :~: ST), 
      rest: (ST ∪ QT) { type Out = O }
    ):  Union[SH :~: ST, QH :~: QT] with Out[SH :~: QH :~: O] =
    new Union[SH :~: ST, QH :~: QT] with Out[SH :~: QH :~: O] {

      def apply(s: SH :~: ST, q: QH :~: QT) = s.head :~: q.head :~: rest(s.tail, q.tail)
    }
}
