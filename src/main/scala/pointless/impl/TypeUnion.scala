package ohnosequences.pointless.impl

import ohnosequences.pointless._
import shapeless.{ <:!< }

object typeUnion extends anyTypeUnion {

  type AnyTypeUnion = AnyTypeUnionImpl

  type either[T] = TypeUnionImpl[not[T]]


  /*
    Implementations
  */
  type not[T] = T => Nothing
  final type just[T] = not[not[T]]

  protected trait AnyTypeUnionImpl {
    type or[Y] <: AnyTypeUnionImpl
    type union
  }

  protected trait TypeUnionImpl[T] extends AnyTypeUnionImpl {
    type or[S] = TypeUnionImpl[T with not[S]]  
    type union = not[T]
  }

  type    isOneOf[X, U <: AnyTypeUnion] = just[X] <:<  U#union
  type isNotOneOf[X, U <: AnyTypeUnion] = just[X] <:!< U#union

}
