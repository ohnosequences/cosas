// package ohnosequences.pointless

// trait AnyRecord {

//   type Properties <: AnyTypeSet
//   val  properties: Properties
//   // should be provided implicitly:
//   val  propertiesBound: Properties isboundedBy AnyProperty

//   type Raw <: AnyTypeSet
//   // should be provided implicitly:
//   // val  representedProperties: Raw isValuesOf Properties
// }

// object AnyRecord {

//   type PropertiesOf[R <: AnyRecord] = R#Properties

//   abstract class AnyRecordOps[R <: AnyRecord](val record: R) {}
// }
