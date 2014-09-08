package ohnosequences.pointless

/*
  This trait represents a mapping between 

  - members `Tpe` of a universe of types `TpeBound`
  - and `Raw` a type meant to be a denotation of `Tpe` thus the name
*/
trait AnyDenotation extends AnyWrap {

  /* The base type for the types that this thing denotes */
  type TpeBound
  type Tpe <: TpeBound
  val  tpe: Tpe
}

/*
  Bound the universe of types to be `T`s
*/
trait Denotation[T] extends AnyDenotation { type TpeBound = T }

object AnyDenotation {

  /* Refiners */
  type withTpeBound[T] = AnyDenotation { type TpeBound = T }

  /* Accessors */
  type TpeBoundOf[A <: AnyDenotation] = A#TpeBound
}
