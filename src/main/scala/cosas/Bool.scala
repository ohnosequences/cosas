package ohnosequences.cosas

trait AnyBool {

  type If[T <: Bound, F <: Bound, Bound] <: Bound
}

case object TRUE extends AnyBool {

  type If[T <: Bound, F <: Bound, Bound] = T
}

case object FALSE extends AnyBool {

  type If[T <: Bound, F <: Bound, Bound] = F
}
