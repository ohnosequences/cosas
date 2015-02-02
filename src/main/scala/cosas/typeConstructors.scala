package ohnosequences.cosas

object typeConstructors {
  
  trait AnyTypeConstructor { type of[X] }

  object SList  extends AnyTypeConstructor { type of[X] = scala.List[X] }; type SList = SList.type
  object Id     extends AnyTypeConstructor { type of[X] = X             }; type Id    = Id.type
  object Maybe  extends AnyTypeConstructor { type of[X] = Option[X]     }; type Maybe = Maybe.type
}