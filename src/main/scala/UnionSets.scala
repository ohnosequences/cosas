/* ## Sum/union of two type sets */

package ohnosequences.typesets


trait UnionSets[S <: TypeSet, Q <: TypeSet] {
  type Out <: TypeSet
  def apply(s: S, q: Q): Out
}

/* * Case when S is a subset of Q => just Q: */
object UnionSets extends UnionSets_2 {
  implicit def sInQ[S <: TypeSet, Q <: TypeSet]
    (implicit e: S ⊂ Q) =
      new UnionSets[S, Q] { type Out = Q
        def apply(s: S, q: Q) = q
      }
}

/* * (Dual) case when Q is a subset of S => just S: */
trait UnionSets_2 extends UnionSets_3 {
  implicit def qInS[S <: TypeSet, Q <: TypeSet]
    (implicit e: Q ⊂ S) =
      new UnionSets[S, Q] { type Out = S
        def apply(s: S, q: Q) = s
      }
}

/* * Case when S.head is in Q => throwing it away: */
trait UnionSets_3 extends UnionSets_4 {
  implicit def sConsWithoutHead[SH, ST <: TypeSet,  Q <: TypeSet]
    (implicit sh: SH ∈ Q, rest: ST U Q) =
      new UnionSets[ SH :~: ST,    Q] { type Out = rest.Out
        def apply(s: SH :~: ST, q: Q) = rest(s.tail, q)
      }
}

/* * (Dual) case when Q.head is in S => throwing it away: */
trait UnionSets_4 extends UnionSets_5 {
  implicit def qConsWithoutHead[S <: TypeSet,  QH, QT <: TypeSet]
    (implicit qh: QH ∈ S, rest: S U QT) =
      new UnionSets[ S,    QH :~: QT] { type Out = rest.Out
        def apply(s: S, q: QH :~: QT) = rest(s, q.tail)
      }
}

/* * Otherwise both heads are new => adding both: */
trait UnionSets_5 {
  implicit def newHeads[SH, ST <: TypeSet,  QH, QT <: TypeSet]
    (implicit rest: ST U QT) =
      new UnionSets[ SH :~: ST,    QH :~: QT] { type Out = SH :~: QH :~: rest.Out
        def apply(s: SH :~: ST, q: QH :~: QT) = s.head :~: q.head :~: rest(s.tail, q.tail)
      }
}
