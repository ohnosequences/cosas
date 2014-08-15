/* ## Subtract one set from another */

package ohnosequences.pointless.impl.ops

import ohnosequences.pointless._, AnyFn._, impl._, typeSet._

@annotation.implicitNotFound(msg = "Can't subtract ${Q} from ${S}")
trait Subtract[S <: AnyTypeSet, Q <: AnyTypeSet] extends Fn2[S, Q] with AnyFn.WithCodomain[AnyTypeSet] {
  type Out <: AnyTypeSet
  def apply(s: S, q: Q): Out
}

/* * Case when S is inside Q => result is ∅: */
object Subtract extends SubtractSets_2 {
  type Aux[S <: AnyTypeSet, Q <: AnyTypeSet, O <: AnyTypeSet] = Subtract[S, Q] { type Out = O }

  implicit def sInQ[S <: AnyTypeSet, Q <: AnyTypeSet]
    (implicit e: S ⊂ Q) = 
      new Subtract[S,    Q] { type Out = ∅
          def apply(s: S, q: Q) = ∅
      }
}

/* * Case when Q is empty => result is S: */
trait SubtractSets_2 extends SubtractSets_3 {
  implicit def qEmpty[S <: AnyTypeSet] = 
    new Subtract[S,    ∅] { type Out = S
        def apply(s: S, q: ∅) = s
    }

  /* * Case when S.head ∈ Q => result is S.tail \ Q: */
  implicit def sConsWithoutHead[H, T <: AnyTypeSet,  Q <: AnyTypeSet] 
    (implicit h: H ∈ Q, rest: T \ Q) = 
      new Subtract[H :~: T,    Q] { type Out = rest.Out
          def apply(s: H :~: T, q: Q) = rest(s.tail, q)
      }
}

/* * Case when we just leave S.head and traverse further: */
trait SubtractSets_3 {
  implicit def sConsAnyHead[H, T <: AnyTypeSet, Q <: AnyTypeSet] 
    (implicit rest: T \ Q) =
      new Subtract[H :~: T,    Q] { type Out = H :~: rest.Out
          def apply(s: H :~: T, q: Q) = s.head :~: rest(s.tail, q)
      }
}
