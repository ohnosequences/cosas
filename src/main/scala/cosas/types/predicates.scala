package ohnosequences.cosas.types


trait AnyTypePredicate {

  type ArgBound
  type Condition[X <: ArgBound]
}

trait TypePredicate[B] extends AnyTypePredicate { type ArgBound = B }

trait Bool
case object True extends Bool
case object False extends Bool
