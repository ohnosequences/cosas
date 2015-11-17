package ohnosequences.cosas.klists

import ohnosequences.cosas._, typeUnions._

// TODO make this a predicate on KList
trait NoDuplicates[L <: AnyKList]

case object NoDuplicates {

  implicit def empty[A]: NoDuplicates[KNil[A]] = new NoDuplicates[KNil[A]] {}

  implicit def nonEmpty[H <: T#Bound, T <: AnyKList]
    (implicit
      noH: H isNotOneOf T#Types,
      ev: NoDuplicates[T]
    ): NoDuplicates[H :: T] = new NoDuplicates[H :: T] {}
}
