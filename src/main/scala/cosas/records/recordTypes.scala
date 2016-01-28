package ohnosequences.cosas.records

import ohnosequences.cosas._, fns._, types._, klists._

sealed trait AnyRecordType extends AnyType {

  type Keys <: AnyProductType
  val  keys: Keys

  // NOTE should be provided implicitly:
  val noDuplicates: noDuplicates isTrueOn Keys#Types

  type Raw = Keys#Raw

  lazy val label: String = toString
}

class RecordType[Ks <: AnyProductType](
  val keys: Ks
)(implicit
  val noDuplicates: noDuplicates isTrueOn Ks#Types
) extends AnyRecordType {

  type Keys = Ks
}

case object AnyRecordType {

  type Of[+B <: AnyType] = AnyRecordType { type Keys <: AnyProductType.Of[B] }
  type withBound[B <: AnyType] = AnyRecordType { type Keys <: AnyProductType.withBound[B] }

  type withKeys[Ks <: AnyProductType] = AnyRecordType { type Keys = Ks }

  implicit def recordTypeSyntax[RT <: AnyRecordType](rt: RT)
  : syntax.RecordTypeSyntax[RT] =
    syntax.RecordTypeSyntax(rt)

  implicit def recordDenotationSyntax[RT <: AnyRecordType, Vs <: RT#Raw](rv: RT := Vs)
  : syntax.RecordTypeDenotationSyntax[RT, Vs] =
    syntax.RecordTypeDenotationSyntax(rv)
}
