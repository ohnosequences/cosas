package ohnosequences.cosas.records

import ohnosequences.cosas._, types._, typeSets._, properties._

/*
  ## Records

  Records are `AnyType`s wrapping a `PropertySet` from which they take their `Raw` type: entries for that set of properties.
*/
trait AnyRecord extends AnyType {

  type PropertySet <: AnyPropertySet
  val  propertySet: PropertySet

  type Properties = PropertySet#Properties
  val  properties: Properties = propertySet.properties

  type Raw = PropertySet#Raw

  type Size = Properties#Size
}

class Record[PS <: AnyPropertySet](val propertySet: PS) extends AnyRecord {

  type PropertySet = PS

  lazy val label = toString
}

case object AnyRecord {

  type withProperties[PS <: AnyPropertySet] = AnyRecord { type Properties = PS }
}
