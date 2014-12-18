package ohnosequences.cosas

import typeUnion._
import shapeless.{ <:!<, _ }, Nat._


object typeUnion {

  /* There are two type-level constructors of a type union.
     A generic term looks like `either[A]#or[B]#or[C]`. */
  trait AnyTypeUnion {

    type or[Y] <: AnyTypeUnion
    type union // this is like return
    type Arity <: Nat
    type PrevBoundNot
  }

  // object AnyTypeUnion {

  private[cosas] type not[T] = (T => Nothing)
  private[cosas] type just[T] = not[not[T]]

  type empty = empty.type
  object empty extends AnyTypeUnion {

    type Arity = shapeless.nat._0
    type union = not[not[Nothing]]
    type Head = Nothing

    type PrevBoundNot = not[Nothing] 
    type or[Z] = either[Z]
  }

  sealed trait either[X] extends AnyTypeUnion {

    type Arity = shapeless.nat._1
    type union = not[not[X]]
    type Head = X

    type PrevBoundNot = not[X] 
    type or[Z] = typeUnion.or[either[X], Z]
  }

  sealed trait or[T <: AnyTypeUnion, S] extends AnyTypeUnion {

    type Head = S
    type Arity = shapeless.Succ[T#Arity]
    type union = not[ T#PrevBoundNot with not[S] ]
    type PrevBoundNot = T#PrevBoundNot with not[S]
    type or[Z] = typeUnion.or[T#or[S], Z]
  }

  type :∨:[S, T <: AnyTypeUnion] = T#or[S]

  /* Builder */
  // trait TypeUnion[T] extends AnyTypeUnion { self =>

  //   type or[S] = TypeUnion[T with not[S]] { type Arity = Succ[self.Arity] }
  //   type union = not[T]

  // }

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
