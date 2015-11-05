package ohnosequences.cosas.types

import ohnosequences.cosas._, klists._, fns._, typeUnions._

sealed trait AnyRecordType extends AnyType {

  type Types <: AnyProductType

  type Raw = Types#Raw
}

class RecordType[Ts <: AnyProductType](val types: Ts)(implicit ev: NoDuplicates[Ts#Types]) extends AnyRecordType {

  type Types = Ts

  lazy val label: String = toString
}

// TODO move to predicate
trait NoDuplicates[L <: AnyKList]

case object NoDuplicates {

  implicit def empty[A]: NoDuplicates[KNil[A]] = new NoDuplicates[KNil[A]] {}

  implicit def nonEmpty[H <: T#Bound, T <: AnyKList](implicit
    ev: NoDuplicates[T],
    noH: H isNotOneOf T#Types
  )
  : NoDuplicates[H :: T] = new NoDuplicates[H :: T] {}
}
