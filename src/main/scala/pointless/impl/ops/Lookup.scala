/*
## Popping an element from a set

It's like `Lookup`, but it removes the element

*/

package ohnosequences.pointless.impl.ops

import ohnosequences.pointless._, AnyFn._, impl._, typeSet._

@annotation.implicitNotFound(msg = "Can't find an element of type ${E} in the set ${S}")
trait Lookup[S <: AnyTypeSet, E] extends Fn1[S] with WithCodomain[E] 

object Lookup {
  implicit def findPop[S <: AnyTypeSet, E](implicit pop: Pop[S, E]):
    Lookup[S, E] with out[pop.EOut] =
      new Lookup[S, E] {
        type Out = pop.EOut
        def apply(s: S): Out = pop(s)._1
      }
}
