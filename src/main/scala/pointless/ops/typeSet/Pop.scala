/*
## Popping an element from a set

Returns the element of the set of the given type and the rest of the set
*/

package ohnosequences.pointless.ops.typeSet

import ohnosequences.pointless._, AnyFn._, AnyTypeSet._

@annotation.implicitNotFound(msg = "Can't pop an element of type ${E} from the set ${S}")
trait Pop[S <: AnyTypeSet, E] extends Fn1[S] {
  type SOut <: AnyTypeSet //.Of[S#Bound]
  type Out = (E, SOut)
}

trait PopSOut[S <: AnyTypeSet, E, SO <: AnyTypeSet] extends Pop[S, E] { type SOut = SO }

object Pop extends Pop_2 {
  def apply[S <: AnyTypeSet, E](implicit pop: Pop[S, E]): Pop[S, E] = pop

  implicit def foundInHead[E <: T#Bound, H <: E, T <: AnyTypeSet]: 
        PopSOut[H :~: T, E, T] =
    new PopSOut[H :~: T, E, T] { 

      def apply(s: In1): Out = (s.head, s.tail)
    }
}

trait Pop_2 {
  implicit def foundInTail[H <: T#Bound with TO#Bound, T <: AnyTypeSet, E, TO <: AnyTypeSet]
    (implicit 
      e: E ∈ T, 
      l: Pop[T, E] { type SOut = TO }
    ):  PopSOut[H :~: T, E, H :~: TO] =
    new PopSOut[H :~: T, E, H :~: TO] { 

      def apply(s: In1): Out = {
        val (e, t) = l(s.tail)
        (e, s.head :~: t)
      }
    }
}

/* This just returns the first part of pop (the element) */
@annotation.implicitNotFound(msg = "Can't lookup an element of type ${E} from the set ${S}")
trait Lookup[S <: AnyTypeSet, E] extends Fn1[S] with Out[E]

object Lookup {
  implicit def popToLookup[S <: AnyTypeSet, E]
    (implicit p: S Pop E): 
        Lookup[S, E] = 
    new Lookup[S, E] { def apply(s: In1): Out = p(s)._1 }
}


/* This just returns the second part of pop (the set without the element) */
@annotation.implicitNotFound(msg = "Can't delete an element of type ${E} from the set ${S}")
trait Delete[S <: AnyTypeSet, E] extends Fn1[S] with OutBound[AnyTypeSet]

object Delete {
  implicit def popToDelete[S <: AnyTypeSet, E, SO <: AnyTypeSet]
    (implicit p: PopSOut[S, E, SO]): 
        Delete[S, E] with Out[SO] = 
    new Delete[S, E] with Out[SO] { def apply(s: In1): Out = p(s)._2 }
}
