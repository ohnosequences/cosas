package ohnosequences.cosas

trait AnyBool {

  type If[T <: Bound, F <: Bound, Bound] <: Bound
}

case object True extends AnyBool {

  type If[T <: Bound, F <: Bound, Bound] = T
}

case object False extends AnyBool {

  type If[T <: Bound, F <: Bound, Bound] = F
}
