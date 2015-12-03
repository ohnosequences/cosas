package ohnosequences.cosas.klists

import ohnosequences.cosas._, fns._, typeUnions._

case object NoDuplicates extends PredicateOver[AnyKList] {

  implicit def empty[A]:
    noDuplicates isTrueOn *[A] =
    noDuplicates.isTrueOn[*[A]]

  implicit def nonEmpty[H <: T#Bound, T <: AnyKList]
    (implicit
      noH: H isNotOneOf T#Hola,
      ev: noDuplicates isTrueOn T
    ): noDuplicates isTrueOn(H :: T) =
       noDuplicates.isTrueOn[H :: T]
}
