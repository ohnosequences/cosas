package ohnosequences.pointless

import shapeless.{ <:!< }

/*
  The two type-level constructors of a type union. 
  A generic term looks like `either[A]#or[B]#or[C]`.
*/
trait AnyTypeUnion {

  type or[Y] <: AnyTypeUnion
  type union // kind of return
}

object AnyTypeUnion {

  private[pointless] type not[T] = T => Nothing
  private[pointless] type just[T] = not[not[T]]

  /*
    Type-level operations
  */
  @annotation.implicitNotFound(msg = "isOneOf check is not implemented")
  type    isOneOf[X, U <: AnyTypeUnion] = just[X] <:<  U#union

  @annotation.implicitNotFound(msg = "isNotOneOf check is not implemented")
  type isNotOneOf[X, U <: AnyTypeUnion] = just[X] <:!< U#union

  final type oneOf[U <: AnyTypeUnion] = {
    type    is[X] = X    isOneOf U
    type isNot[X] = X isNotOneOf U
  }
}

import AnyTypeUnion._

sealed trait either[X] extends TypeUnion[not[X]]

/* Builder */
trait TypeUnion[T] extends AnyTypeUnion {

  type or[S] = TypeUnion[T with not[S]]  
  type union = not[T]
}
