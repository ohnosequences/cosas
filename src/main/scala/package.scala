/*
## Package object

This is a package object, which mixes other parts of the library, to provide a common namespace,
where all the definitions are accessible. This way you need just to say `import
ohnosequences.typesets._` and you have everything in scope.
*/

package ohnosequences

package object typesets extends TypeSetImplicits {

  /*  Means "is not subtype of". If this doesn't hold, an error about ambiguous implicits will 
      arise. Taken from shapeless-2.0.
      Credits: Miles Sabin. 
  */
  trait <:!<[A, B]
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

  /* These aliases mean that some type is (or isn't) a member of the union */
  sealed class :<:[ X : oneOf[U]#is,   U <: TypeUnion]
  implicit def :<:[ X : oneOf[U]#is,   U <: TypeUnion] = new (X :<: U)
  sealed class :<!:[X : oneOf[U]#isnot, U <: TypeUnion]
  implicit def :<!:[X : oneOf[U]#isnot, U <: TypeUnion] = new (X :<!: U)


  /* ### Type sets */
  type in[S <: TypeSet] = { 
    type    is[E] = E :<:  S#Bound
    type isnot[E] = E :<!: S#Bound
  }
  sealed class ∈[E : in[S]#is, S <: TypeSet]
  implicit def ∈[E : in[S]#is, S <: TypeSet] = new (E ∈ S)
  sealed class ∉[E : in[S]#isnot, S <: TypeSet]
  implicit def ∉[E : in[S]#isnot, S <: TypeSet] = new (E ∉ S)

  /* - `S ⊃ Q` */
  type supersetOf[Q <: TypeSet] = { 
    type    is[S <: TypeSet] = Q#Bound#get <:<  S#Bound#get
    type isnot[S <: TypeSet] = Q#Bound#get <:!< S#Bound#get
  }
  sealed class ⊃[S <: TypeSet : supersetOf[Q]#is, Q <: TypeSet]
  implicit def ⊃[S <: TypeSet : supersetOf[Q]#is, Q <: TypeSet] = new (S ⊃ Q)

  /* - `S ⊂ Q` */
  type subsetOf[Q <: TypeSet] = { 
    type    is[S <: TypeSet] = S#Bound#get <:<  Q#Bound#get
    type isnot[S <: TypeSet] = S#Bound#get <:!< Q#Bound#get
  }
  sealed class ⊂[S <: TypeSet : subsetOf[Q]#is, Q <: TypeSet]
  implicit def ⊂[S <: TypeSet : subsetOf[Q]#is, Q <: TypeSet] = new (S ⊂ Q)

  /* - The union types of two sets are the same */
  type sameAs[Q <: TypeSet] = { 
    type    is[S <: TypeSet] = S#Bound#get =:= Q#Bound#get
    // type isnot[S <: TypeSet] = S#Bound#get =:!= Q#Bound#get
  }
  sealed class ~[S <: TypeSet : sameAs[S]#is, Q <: TypeSet]
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
