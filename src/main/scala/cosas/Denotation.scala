package ohnosequences.cosas

/* Something super-generic and ultra-abstract */
trait AnyType {

  val label: String
}

/*
  This trait represents a mapping between 

  - `DenotedType` of a universe of types `TypeBound`
  - `Raw` a type meant to be a denotation of `DenotedType` thus the name
*/
trait AnyDenotation extends AnyWrap {

  /* The base type for the types that this thing denotes */
  type TypeBound <: AnyType

  type DenotedType <: TypeBound
  val  denotedType: DenotedType
}

/*
  Bound the universe of types to be `T`s
*/
trait Denotation[T <: AnyType] extends AnyDenotation { type TypeBound = T }

object AnyDenotation {

  type withTypeBound[T <: AnyType] = AnyDenotation { type TypeBound = T }

  type TypeBoundOf[D <: AnyDenotation] = D#TypeBound
  type DenotedTypeOf[D <: AnyDenotation] = D#DenotedType
}
