/* ## Sum/union of two type sets */

package ohnosequences.pointless.impl.ops

import ohnosequences.pointless._, AnyFn._, impl._, typeSet._

@annotation.implicitNotFound(msg = "Can't union ${S} with ${Q}")
trait Union[S <: AnyTypeSet, Q <: AnyTypeSet] extends Fn2[S,Q] with WithCodomain[AnyTypeSet] {

  type Out <: AnyTypeSet
  
  def apply(s: S, q: Q): Out
}

/* * Case when S is a subset of Q => just Q: */
object Union extends UnionSets_2 {
  type Aux[S <: AnyTypeSet, Q <: AnyTypeSet, O <: AnyTypeSet] = Union[S, Q] { type Out = O }

  implicit def sInQ[S <: AnyTypeSet, Q <: AnyTypeSet]
    (implicit e: S ⊂ Q) =
      new Union[S, Q] { type Out = Q
        def apply(s: S, q: Q) = q
      }
}

/* * (Dual) case when Q is a subset of S => just S: */
trait UnionSets_2 extends UnionSets_3 {
  implicit def qInS[S <: AnyTypeSet, Q <: AnyTypeSet]
    (implicit e: Q ⊂ S) =
      new Union[S, Q] { type Out = S
        def apply(s: S, q: Q) = s
      }
}

/* * Case when S.head is in Q => throwing it away: */
trait UnionSets_3 extends UnionSets_4 {
  implicit def sConsWithoutHead[SH, ST <: AnyTypeSet,  Q <: AnyTypeSet]
    (implicit sh: SH ∈ Q, rest: ST ∪ Q) =
      new Union[ SH :~: ST,    Q] { type Out = rest.Out
        def apply(s: SH :~: ST, q: Q) = rest(s.tail, q)
      }
}

/* * (Dual) case when Q.head is in S => throwing it away: */
trait UnionSets_4 extends UnionSets_5 {
  implicit def qConsWithoutHead[S <: AnyTypeSet,  QH, QT <: AnyTypeSet]
    (implicit qh: QH ∈ S, rest: S ∪ QT) =
      new Union[ S,    QH :~: QT] { type Out = rest.Out
        def apply(s: S, q: QH :~: QT) = rest(s, q.tail)
      }
}

/* * Otherwise both heads are new => adding both: */
trait UnionSets_5 {
  implicit def newHeads[SH, ST <: AnyTypeSet,  QH, QT <: AnyTypeSet]
    (implicit rest: ST ∪ QT) =
      new Union[ SH :~: ST,    QH :~: QT] { type Out = SH :~: QH :~: rest.Out
        def apply(s: SH :~: ST, q: QH :~: QT) = s.head :~: q.head :~: rest(s.tail, q.tail)
      }
}
