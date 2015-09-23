package ohnosequences.cosas.types


trait AnyTypePredicate {

  type ArgBound
  type Condition[X <: ArgBound]
}

trait TypePredicate[B] extends AnyTypePredicate { type ArgBound = B }
