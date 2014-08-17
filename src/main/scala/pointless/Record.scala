package ohnosequences.pointless

trait anyRecord {

  // abstract dep
  type representable <: anyRepresentable
  type property <: anyProperty
  type typeSet <: anyTypeSet

  type AnyRecord <: AnyRecordImpl with representable#AnyRepresentableImpl

  trait AnyRecordImpl { self: representable#AnyRepresentableImpl =>

    type Properties <: typeSet#AnyTypeSet
    val  properties: Properties
    // should be provided implicitly:
    val  propertiesBound: typeSet#isBoundedBy[Properties, property#AnyProperty]

    type Raw <: typeSet#AnyTypeSet
    // should be provided implicitly:
    // val  representedProperties: Raw isValuesOf Properties
  }

  type PropertiesOf[R <: AnyRecordImpl] = R#Properties


  abstract class AnyRecordOps[R <: AnyRecord](val record: R) {}
}
