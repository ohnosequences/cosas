package ohnosequences.pointless.impl

import ohnosequences.pointless._, AnyFn._

object record extends anyRecord {

  type representable = impl.representable.type
  import representable._

  type property = impl.property.type
  import property._

  type typeSet = impl.typeSet.type
  import typeSet._

  type AnyRecord = RecordImpl

  trait RecordImpl extends AnyRecordImpl with RepresentableImpl {

    type Properties <: AnyTypeSet
    val  properties: Properties
    // should be provided implicitly:
    val  propertiesBound: Properties isBoundedBy AnyProperty

    type Raw <: AnyTypeSet
    // should be provided implicitly:
    // val  representedProperties: Raw isValuesOf Properties
  }


  /*
    Ops
  */
  implicit def recordOps[R <: AnyRecord](r: R): RecordOps[R] = RecordOps[R](r)
  case class   RecordOps[R <: AnyRecord](r: R) extends AnyRecordOps[R](r) {

  }

}
