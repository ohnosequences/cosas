package ohnosequences.pointless

/*
  This trait represents a mapping between 

  - members `Tpe` of a universe of types `TYPE`
  - and `Raw` a type meant to be a denotation of `Tpe` thus the name
*/
trait AnyDenotation extends AnyTaggedType {

  /* The base type for the types that this thing denotes */
  type TYPE
  type Tpe <: TYPE
  val  tpe: Tpe
}

/*
  Bound the universe of types to be `T`s
*/
trait Denotation[T] extends AnyDenotation { type TYPE = T }

object AnyDenotation {

  type withTYPE[T] = AnyDenotation { type TYPE = T }
  type TYPE[A <: AnyDenotation] = A#TYPE
}
