package ohnosequences.pointless.impl

import shapeless.{ <:!< }

trait AnyTypeUnion extends ohnosequences.pointless.AnyTypeUnion {

  type or[Y] <: AnyTypeUnion
  type union
}

trait TypeUnion[T] extends AnyTypeUnion {

  type or[S] = TypeUnion[T with typeUnion.not[S]]  
  type union = typeUnion.not[T]
}


trait either[T] extends ohnosequences.pointless.either[T] with TypeUnion[typeUnion.not[T]]


object typeUnion extends ohnosequences.pointless.typeUnion {

  type not[T] = T => Nothing
  final type just[T] = not[not[T]]

  type    isOneOf[X, U <: AnyTypeUnion] = just[X] <:<  U#union
  type isNotOneOf[X, U <: AnyTypeUnion] = just[X] <:!< U#union

}
