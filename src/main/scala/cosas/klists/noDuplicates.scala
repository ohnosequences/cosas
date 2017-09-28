package ohnosequences.cosas.klists

import ohnosequences.cosas._, fns._, typeUnions._

case object NoDuplicates extends PredicateOver[AnyKList] {

  implicit def empty[A]:
    NoDuplicates.type isTrueOn *[A] =
    NoDuplicates.isTrueOn[*[A]]

  implicit def nonEmpty[H <: T#Bound, T <: AnyKList]
    (implicit
      noH: H isNotOneOf T#AllTypes,
      ev: NoDuplicates.type isTrueOn T
    ): NoDuplicates.type isTrueOn(H :: T) =
       NoDuplicates.isTrueOn[H :: T]
}
