/* ## Sum/union of two type sets */

package ohnosequences.pointless.ops.typeSet

import ohnosequences.pointless._, AnyFn._, typeSet._

@annotation.implicitNotFound(msg = "Can't union ${S} with ${Q}")
trait Union[S <: AnyTypeSet, Q <: AnyTypeSet] extends Fn2[S, Q] with WithCodomain[AnyTypeSet]

/* * Case when S is a subset of Q => just Q: */
object Union extends UnionSets_2 {

  def apply[S <: AnyTypeSet, Q <: AnyTypeSet]
    (implicit uni: Union[S, Q]): Union[S, Q] with out[uni.Out] = uni

  implicit def sInQ[S <: AnyTypeSet, Q <: AnyTypeSet]
    (implicit e: S ⊂ Q): Union[S, Q] with out[Q] =
      new Union[S, Q] {
        type Out = Q
        def apply(s: S, q: Q) = q
      }
}

/* * (Dual) case when Q is a subset of S => just S: */
trait UnionSets_2 extends UnionSets_3 {
  implicit def qInS[S <: AnyTypeSet, Q <: AnyTypeSet]
    (implicit e: Q ⊂ S): Union[S, Q] with out[S] =
      new Union[S, Q] { type Out = S
        def apply(s: S, q: Q) = s
      }
}

/* * Case when S.head is in Q => throwing it away: */
trait UnionSets_3 extends UnionSets_4 {
  implicit def sConsWithoutHead[SH, ST <: AnyTypeSet,  Q <: AnyTypeSet]
    (implicit sh: SH ∈ Q, rest: ST ∪ Q): Union[SH :~: ST, Q] with out[rest.Out] =
      new Union[SH :~: ST, Q] {
        type Out = rest.Out
        def apply(s: SH :~: ST, q: Q) = rest(s.tail, q)
      }
}

/* * (Dual) case when Q.head is in S => throwing it away: */
trait UnionSets_4 extends UnionSets_5 {
  implicit def qConsWithoutHead[S <: AnyTypeSet,  QH, QT <: AnyTypeSet]
    (implicit qh: QH ∈ S, rest: S ∪ QT): Union[S, QH :~: QT] with out[rest.Out] =
      new Union[S, QH :~: QT] {
        type Out = rest.Out
        def apply(s: S, q: QH :~: QT) = rest(s, q.tail)
      }
}

/* * Otherwise both heads are new => adding both: */
trait UnionSets_5 {
  implicit def newHeads[SH, ST <: AnyTypeSet,  QH, QT <: AnyTypeSet]
    (implicit rest: ST ∪ QT): Union[SH :~: ST, QH :~: QT] with out[SH :~: QH :~: rest.Out] =
      new Union[SH :~: ST, QH :~: QT] {
        type Out = SH :~: QH :~: rest.Out
        def apply(s: SH :~: ST, q: QH :~: QT) = s.head :~: q.head :~: rest(s.tail, q.tail)
      }
}
