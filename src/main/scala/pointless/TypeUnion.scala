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
  type oneOf[U <: AnyTypeUnion] <: PredicateOn[Any]
}
