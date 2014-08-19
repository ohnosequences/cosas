/*
## Popping an element from a set

It's like `Lookup`, but it removes the element

*/

package ohnosequences.pointless.ops.typeSet

import ohnosequences.pointless._, AnyFn._, typeSet._

@annotation.implicitNotFound(msg = "Can't find an element of type ${E} in the set ${S}")
trait Lookup[S <: AnyTypeSet, E] extends Fn1[S] with WithCodomain[E] 

object Lookup {
  def apply[S <: AnyTypeSet, E](implicit lookup: Lookup[S, E]): Lookup[S, E] with out[lookup.Out] = lookup

  implicit def findPop[S <: AnyTypeSet, E](implicit pop: Pop[S, E]):
    Lookup[S, E] with out[pop.EOut] =
      new Lookup[S, E] {
        type Out = pop.EOut
        def apply(s: S): Out = pop(s)._1
      }
}
