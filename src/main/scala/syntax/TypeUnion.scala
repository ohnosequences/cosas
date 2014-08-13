package ohnosequences.typesets.syntax

trait TypeUnionSyntax {
 
  /*
    The two type-level constructors of a type union. A generic term looks like either[A]#or[B]#or[C]
  */
  type TypeUnion <: {

    type or[S] <: TypeUnion
  }

  type either[T] <: TypeUnion

  /*
    Type-level operations:

    - evidence for a type being a member of the type union
    - evidence for a type being **not** a member of the type union
  */
  type oneOf[U <: TypeUnion] <: { 

    type is[T]
    type isNot[T]
  }

  implicit def getIsIn[X: oneOf[U]#is, U <: TypeUnion]: (X :<: U) = new (X :<: U)
  @annotation.implicitNotFound(msg = "Can't prove that ${X} IS one of the types in the union type ${U}")
  sealed class :<:[X: oneOf[U]#is, U <: TypeUnion]

  implicit def getIsNotIn[X: oneOf[U]#isNot, U <: TypeUnion]: (X :<!: U) = new (X :<!: U)
  @annotation.implicitNotFound(msg = "Can't prove that ${X} is NOT one of the types in the union type  ${U}")
  sealed class :<!:[X : oneOf[U]#isNot, U <: TypeUnion]
}