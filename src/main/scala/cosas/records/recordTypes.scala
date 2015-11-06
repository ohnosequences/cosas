package ohnosequences.cosas.records

import ohnosequences.cosas._, types._, klists._, typeUnions._

sealed trait AnyRecordType extends AnyType {

  type Keys <: AnyProductType
  val  keys: Keys

  // should be provided implicitly:
  val noDuplicates: NoDuplicates[Keys#Types]

  type Raw = Keys#Raw

  lazy val label: String = toString
}

class RecordType[Ks <: AnyProductType](
  val keys: Ks
)(implicit
  val noDuplicates: NoDuplicates[Ks#Types]
) extends AnyRecordType {

  type Keys = Ks
}

case object AnyRecordType {

  implicit def recordDenotationSyntax[RT <: AnyRecordType,Vs <: RT#Raw](rv: RT := Vs)
  : syntax.RecordTypeDenotationSyntax[RT,Vs] =
    syntax.RecordTypeDenotationSyntax(rv.value)
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
