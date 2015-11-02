package ohnosequences.cosas

trait AnyPair {
  type First
  type Second
}

case object AnyPair {
  type of[F, S] = AnyPair {
    type First <: F
    type Second <: S
  }
}

trait Pair[F, S] extends AnyPair {
  type First = F
  type Second = S
}
