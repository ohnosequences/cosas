package ohnosequences.pointless

trait anyTypeUnion {
 
  /*
    The two type-level constructors of a type union. A generic term looks like either[A]#or[B]#or[C]
  */
  type AnyTypeUnion <: {
    type or[Y] <: AnyTypeUnion
  }

  type either[X] <: AnyTypeUnion

  /*
    Type-level operations
  */
  type    isOneOf[X, U <: AnyTypeUnion]
  type isNotOneOf[X, U <: AnyTypeUnion]

  final type oneOf[U <: AnyTypeUnion] = {
    type    is[X] = X    isOneOf U
    type isNot[X] = X isNotOneOf U
  }
}
