package ohnosequences.cosas

object typeConstructors {
  
  trait AnyTypeConstructor { type of[X] }

  trait AnyTypeConstructorComposition extends AnyTypeConstructor {

    type First <: AnyTypeConstructor
    val first: First
    type Second <: AnyTypeConstructor
    val second: Second

    type of[X] = Second#of[First#of[X]]
  }

  case class TypeConstructorComposition[F0 <: AnyTypeConstructor, S0 <: AnyTypeConstructor](val first: F0, val second: S0) extends AnyTypeConstructorComposition {

    type First = F0
    type Second = S0
  }

  object SList  extends AnyTypeConstructor { type of[X] = scala.List[X] }; //type SList = SList.type
  object Id     extends AnyTypeConstructor { type of[X] = X             }; //type Id    = Id.type
  object Maybe  extends AnyTypeConstructor { type of[X] = Option[X]     }; //type Maybe = Maybe.type
}