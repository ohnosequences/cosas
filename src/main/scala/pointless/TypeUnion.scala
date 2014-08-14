package ohnosequences.pointless

trait TypeUnion {
 
  /*
    The two type-level constructors of a type union. A generic term looks like either[A]#or[B]#or[C]
  */
  type TypeUnion <: {

    type or[Y] <: TypeUnion
  }

  type either[X] <: TypeUnion

  /*
    Type-level operations:

    - evidence for a type being a member of the type union
    - evidence for a type being **not** a member of the type union
  */
  type oneOf[U <: TypeUnion] <: { 

    type is[E]
    type isNot[E]
  }

  type sameAs[U <: TypeUnion] <: { 

    type    is[V <: TypeUnion]
    type isnot[V <: TypeUnion]
  }

  /*
    All the elements of V are bounded by _at least one_ element in U
  */
  type boundedByOneIn[U <: TypeUnion] <: { 

    type everyoneIs[V <: TypeUnion]
    type notEveryoneIs[V <: TypeUnion]
  }

  /* - All elements of the set are bounded by the given type */
  type boundedBy[B] = {

    type    everyoneIs[S <: TypeUnion] = boundedByOneIn[either[B]]#everyoneIs[S]
    type notEveryoneIs[S <: TypeUnion] = boundedByOneIn[either[B]]#notEveryoneIs[S]
  }

  /*
    ### proofs
  */

  // TODO add the rest: bounded, same as

  implicit def getIsIn[E: oneOf[U]#is, U <: TypeUnion]: (E ∈ U) = new (E ∈ U)
  @annotation.implicitNotFound(msg = "Can't prove that ${E} IS one of the types in the union type ${U}")
  sealed class isOneOf[E: oneOf[U]#is, U <: TypeUnion]
  final type ∈[E, U <: TypeUnion] = isOneOf[E,U]

  implicit def getIsNotIn[E: oneOf[U]#isNot, U <: TypeUnion]: (E ∉ U) = new (E ∉ U)
  @annotation.implicitNotFound(msg = "Can't prove that ${E} is NOT one of the types in the union type  ${U}")
  sealed class isNotOneOf[E: oneOf[U]#isNot, U <: TypeUnion]
  final type ∉[E, U <: TypeUnion] = isNotOneOf[E,U]
}