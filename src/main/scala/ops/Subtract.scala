/* ## Subtract one set from another */

package ohnosequences.typesets


@annotation.implicitNotFound(msg = "Can't subtract ${Q} from ${S}")
trait Subtract[S <: TypeSet, Q <: TypeSet] extends Fn2[S, Q] with AnyFn.WithCodomain[TypeSet] {
  type Out <: TypeSet
  def apply(s: S, q: Q): Out
}

/* * Case when S is inside Q => result is ∅: */
object Subtract extends SubtractSets_2 {
  type Aux[S <: TypeSet, Q <: TypeSet, O <: TypeSet] = Subtract[S, Q] { type Out = O }

  implicit def sInQ[S <: TypeSet, Q <: TypeSet]
    (implicit e: S ⊂ Q) = 
      new Subtract[S,    Q] { type Out = ∅
          def apply(s: S, q: Q) = ∅
      }
}

/* * Case when Q is empty => result is S: */
trait SubtractSets_2 extends SubtractSets_3 {
  implicit def qEmpty[S <: TypeSet] = 
    new Subtract[S,    ∅] { type Out = S
        def apply(s: S, q: ∅) = s
    }

  /* * Case when S.head ∈ Q => result is S.tail \ Q: */
  implicit def sConsWithoutHead[H, T <: TypeSet,  Q <: TypeSet] 
    (implicit h: H ∈ Q, rest: T \ Q) = 
      new Subtract[H :~: T,    Q] { type Out = rest.Out
          def apply(s: H :~: T, q: Q) = rest(s.tail, q)
      }
}

/* * Case when we just leave S.head and traverse further: */
trait SubtractSets_3 {
  implicit def sConsAnyHead[H, T <: TypeSet, Q <: TypeSet] 
    (implicit rest: T \ Q) =
      new Subtract[H :~: T,    Q] { type Out = H :~: rest.Out
          def apply(s: H :~: T, q: Q) = s.head :~: rest(s.tail, q)
      }
}
