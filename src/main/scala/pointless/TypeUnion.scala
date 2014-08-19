package ohnosequences.pointless

/*
  The two type-level constructors of a type union. A generic term looks like either[A]#or[B]#or[C]
*/
trait AnyTypeUnion {

  type or[Y] <: AnyTypeUnion
}

trait either[X] extends AnyTypeUnion

trait typeUnion {

  /*
    Type-level operations
  */
  @annotation.implicitNotFound(msg = "isOneOf check is not implemented")
  type    isOneOf[X, U <: AnyTypeUnion]

  @annotation.implicitNotFound(msg = "isNotOneOf check is not implemented")
  type isNotOneOf[X, U <: AnyTypeUnion]

  final type oneOf[U <: AnyTypeUnion] = {
    type    is[X] = X    isOneOf U
    type isNot[X] = X isNotOneOf U
  }
}
