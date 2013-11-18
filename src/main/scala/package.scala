/*
## Implicits and aliases

This is a package object which contains all the needed implicits and type aliases. So when using the library, you should use just one import:

```scala
import ohnosequences.typesets._
```

------
*/

package ohnosequences

package object typesets {

  // for the `<:!<` operator:
  implicit def nsub[A, B]: A <:!< B = new (A <:!< B) {}
  implicit def AisSubtypeOfB_1[A, B >: A]: A <:!< B = sys.error("Unexpected invocation")
  implicit def AisSubtypeOfB_2[A, B >: A]: A <:!< B = sys.error("Unexpected invocation")

  /* ### Type union */
  type not[T] = T => Nothing
  type either[T] = OneOf[not[T]]
  type oneOf[U <: TypeUnion] = { 
    type is[T]    = not[not[T]] <:<  U#get
    type isnot[T] = not[not[T]] <:!< U#get
  }

  implicit def :<:[ X : oneOf[U]#is,   U <: TypeUnion] = new (X :<: U)
  implicit def :<!:[X : oneOf[U]#isnot, U <: TypeUnion] = new (X :<!: U)

  /* ### Type sets */
  val ∅ : ∅ = empty

  // Shortcut for a one element set 
  def set[E](e: E): E :~: ∅ = e :~: ∅

  // Any type can be converted to a one element set
  implicit def typeSetOps[S <: TypeSet](s: S) = new TypeSetOps(s)
  implicit def oneElemSet[E](e: E) = new TypeSetOps(set(e))

  type in[S <: TypeSet] = { 
    type    is[E] = E :<:  S#Bound
    type isnot[E] = E :<!: S#Bound
  }
  implicit def ∈[E : in[S]#is, S <: TypeSet] = new (E ∈ S)
  implicit def ∉[E : in[S]#isnot, S <: TypeSet] = new (E ∉ S)

  /* - `S ⊃ Q` */
  type supersetOf[Q <: TypeSet] = { 
    type    is[S <: TypeSet] = Q#Bound#get <:<  S#Bound#get
    type isnot[S <: TypeSet] = Q#Bound#get <:!< S#Bound#get
  }
  implicit def ⊃[S <: TypeSet : supersetOf[Q]#is, Q <: TypeSet] = new (S ⊃ Q)

  /* - `S ⊂ Q` */
  type subsetOf[Q <: TypeSet] = { 
    type    is[S <: TypeSet] = S#Bound#get <:<  Q#Bound#get
    type isnot[S <: TypeSet] = S#Bound#get <:!< Q#Bound#get
  }
  implicit def ⊂[S <: TypeSet : subsetOf[Q]#is, Q <: TypeSet] = new (S ⊂ Q)

  /* - The union types of two sets are the same */
  type sameAs[Q <: TypeSet] = { 
    type    is[S <: TypeSet] = S#Bound#get =:= Q#Bound#get
    // type isnot[S <: TypeSet] = S#Bound#get =:!= Q#Bound#get
  }
  implicit def ~[S <: TypeSet : sameAs[S]#is, Q <: TypeSet] = new (S ~ Q)

  /* - All elements of the set are bounded by the given type */
  type boundedBy[B] = { 
    type    is[S <: TypeSet] = S#Bound#get <:<  not[not[B]]
    type isnot[S <: TypeSet] = S#Bound#get <:!< not[not[B]]
  }

  /* - Subtraction of sets */
  type \[S <: TypeSet, Q <: TypeSet] = SubtractSets[S, Q]

  /* - Union of sets */
  type U[S <: TypeSet, Q <: TypeSet] = UnionSets[S, Q]

}