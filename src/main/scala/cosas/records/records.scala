package ohnosequences.cosas.records

import ohnosequences.cosas._, types._, typeSets._, properties._

// TODO rewrite this in terms of product types
trait AnyRecord extends AnyType {

  type SetOfTypes <: AnySetOfTypes
  val  propertySet: SetOfTypes

  type Properties = SetOfTypes#Types
  val  properties: Properties = propertySet.types

  type Raw = SetOfTypes#Raw

  type Size = Properties#Size
}

class Record[PS <: AnySetOfTypes](val propertySet: PS) extends AnyRecord {

  type SetOfTypes = PS

  lazy val label = toString
}

case object AnyRecord {

  type withProperties[PS <: AnySetOfTypes] = AnyRecord { type Properties = PS }

  implicit def recordSyntax[RT <: AnyRecord](recType: RT): syntax.RecordSyntax[RT] =
    syntax.RecordSyntax(recType)

  implicit def recordEntrySyntax[RT <: AnyRecord](entry: ValueOf[RT]): syntax.RecordEntrySyntax[RT] =
    syntax.RecordEntrySyntax(entry.value)
}
