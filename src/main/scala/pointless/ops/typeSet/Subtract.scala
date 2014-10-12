/* ## Subtract one set from another */

package ohnosequences.pointless.ops.typeSet

import ohnosequences.pointless._, AnyFn._, AnyTypeSet._

@annotation.implicitNotFound(msg = "Can't subtract ${Q} from ${S}")
trait Subtract[S <: AnyTypeSet, Q <: AnyTypeSet] 
  extends Fn2[S, Q] 
  with OutBound[AnyTypeSet] //.Of[S#Bound]]

/* * Case when S is inside Q => result is ∅: */
object Subtract extends SubtractSets_2 {

  def apply[S <: AnyTypeSet, Q <: AnyTypeSet]
    (implicit sub: S Subtract Q): S Subtract Q = sub

  implicit def sInQ[S <: SubsetOf[Q], Q <: AnyTypeSet]:
        (S Subtract Q) with Out[∅[S#Bound]] = 
    new (S Subtract Q) with Out[∅[S#Bound]] { 

      def apply(s: In1, q: In2): Out = ∅[S#Bound] 
    }
}

trait SubtractSets_2 extends SubtractSets_3 {

  /* * Case when Q is empty => result is S: */
  implicit def qEmpty[S <: AnyTypeSet, E <: AnyEmptySet]: 
        (S Subtract E) with Out[S] =
    new (S Subtract E) with Out[S] { 

      def apply(s: In1, q: In2): Out = s 
    }
}

trait SubtractSets_3 extends SubtractSets_4 {

  /* * Case when S.head ∈ Q => result is S.tail \ Q: */
  implicit def sConsWithoutHead[
    H <: T#Bound,
    T <: AnyTypeSet,
    Q <: AnyTypeSet,
    TO <: AnyTypeSet
  ](implicit
      h: H ∈ Q, 
      rest: (T \ Q) { type Out = TO }
    ):  ((H :~: T) Subtract Q) with Out[TO] =
    new ((H :~: T) Subtract Q) with Out[TO] { 

      def apply(s: In1, q: In2): Out = rest(s.tail, q) 
    }
}

/* * Case when we just leave S.head and traverse further: */
trait SubtractSets_4 {
  implicit def sConsAnyHead[
    H <: T#Bound with TO#Bound,
    T <: AnyTypeSet,
    Q <: AnyTypeSet,
    TO <: AnyTypeSet
  ](implicit
      h: H ∉ Q, 
      rest: (T \ Q) { type Out = TO }
    ):  ((H :~: T) Subtract Q) with Out[H :~: TO] =
    new ((H :~: T) Subtract Q) with Out[H :~: TO] { 

      def apply(s: In1, q: In2): Out = {
        val h: H = s.head
        val t: TO = rest(s.tail, q) 
        h :~: t
      }
    }
}
