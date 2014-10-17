package ohnosequences.cosas

import shapeless.{ <:!< }
import shapeless._, Nat._

/*
  The two type-level constructors of a type union. 
  A generic term looks like `either[A]#or[B]#or[C]`.
*/
trait AnyTypeUnion {

  type or[Y] <: AnyTypeUnion
  type union // kind of return
  type Arity <: Nat
}

object AnyTypeUnion {

  private[cosas] type not[T] = T => Nothing
  private[cosas] type just[T] = not[not[T]]

  /*
    Type-level operations
  */
  @annotation.implicitNotFound(msg = "Can't prove that ${X} is one of ${U}")
  type    isOneOf[X, U <: AnyTypeUnion] = just[X] <:<  U#union

  @annotation.implicitNotFound(msg = "Can't prove that ${X} is not one of ${U}")
  type isNotOneOf[X, U <: AnyTypeUnion] = just[X] <:!< U#union

  final type oneOf[U <: AnyTypeUnion] = {
    type    is[X] = X    isOneOf U
    type isNot[X] = X isNotOneOf U
  }

  type arity[U <: AnyTypeUnion] = U#Arity

  @annotation.implicitNotFound(msg = "Can't prove that ${V} is subunion of ${U}")
  type    isSubunionOf[V <: AnyTypeUnion, U <: AnyTypeUnion] = V#union <:<  U#union

  @annotation.implicitNotFound(msg = "Can't prove that ${V <: AnyTypeUnion} is not subunion of ${U}")
  type isNotSubunionOf[V <: AnyTypeUnion, U <: AnyTypeUnion] = V#union <:!< U#union

}

import AnyTypeUnion._

sealed trait either[X] extends TypeUnion[not[X]] {

  type Arity = _1
}

/* Builder */
trait TypeUnion[T] extends AnyTypeUnion { self =>

  type or[S] = TypeUnion[T with not[S]] { type Arity = Succ[self.Arity] }
  type union = not[T]

}

object TypeUnion {
  type empty = either[Nothing]
}
