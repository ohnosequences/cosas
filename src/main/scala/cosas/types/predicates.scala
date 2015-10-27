package ohnosequences.cosas.types

import ohnosequences.cosas._, fns._

// TODO DepFn1 to Bool?
trait AnyTypePredicate {

  type ArgBound
  type Condition[X <: ArgBound]
}

trait TypePredicate[B] extends AnyTypePredicate { type ArgBound = B }

trait AnyPredicateOn[B] extends DepFn1[B,AnyBool]
