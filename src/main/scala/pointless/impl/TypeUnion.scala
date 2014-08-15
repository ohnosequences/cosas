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

  trait AnyTypeUnionImpl {
    type or[Y] <: AnyTypeUnionImpl
    type get
  }

  trait TypeUnionImpl[T] extends AnyTypeUnionImpl {
    type or[S] = TypeUnionImpl[T with not[S]]  
    type get = not[T]
  }

  type    isOneOf[X, U <: AnyTypeUnion] = either[X]#get <:<  U#get
  type isNotOneOf[X, U <: AnyTypeUnion] = either[X]#get <:!< U#get

}
