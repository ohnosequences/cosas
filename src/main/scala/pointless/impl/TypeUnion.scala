package ohnosequences.pointless.impl

import ohnosequences.pointless._
import shapeless.{ <:!< }

object typeUnion extends anyTypeUnion {

  type AnyTypeUnion = AnyTypeUnionImpl

  type either[T] = TypeUnionImpl[not[T]]

  type oneOf[U <: AnyTypeUnion] = oneOfImpl[U]


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

  trait oneOfImpl[U <: AnyTypeUnion] extends PredicateOn[Any] { 
    type    is[T] = either[T]#get <:<  U#get
    type isNot[T] = either[T]#get <:!< U#get
  }

}
