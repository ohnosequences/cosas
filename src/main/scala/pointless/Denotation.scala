package ohnosequences.pointless

/* Something super-generic and ultra-abstract */
trait AnyType {

  val label: String
}

/*
  This trait represents a mapping between 

  - `Tpe` of a universe of types `TypeBound`
  - `Raw` a type meant to be a denotation of `Tpe` thus the name
*/
trait AnyDenotation extends AnyWrap {

  /* The base type for the types that this thing denotes */

  type Tpe <: AnyType
  val  tpe: Tpe
}

/*
  Bound the universe of types to be `T`s
*/
trait AnyDenotationOf[T <: AnyType] extends AnyDenotation { type Tpe <: T }

object AnyDenotation {

  type withTpe[T <: AnyType] = AnyDenotation { type Tpe = T }

  type TpeOf[D <: AnyDenotation] = D#Tpe
}
