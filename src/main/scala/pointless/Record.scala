package ohnosequences.pointless

import ohnosequences.pointless._, typeSet._, representable._, property._

object record {

  trait AnyRecord extends AnyRepresentable {

    type Properties <: AnyTypeSet
    val  properties: Properties
    // should be provided implicitly:
    val  propertiesBound: Properties isBoundedBy AnyProperty

    type Raw <: AnyTypeSet
    // should be provided implicitly:
    // val  representedProperties: Raw isValuesOf Properties
  }

  object AnyRecord {

    type PropertiesOf[R <: AnyRecord] = R#Properties

    implicit def ops[R <: AnyRecord](r: R): Ops[R] = Ops[R](r)
    case class   Ops[R <: AnyRecord](r: R) {
      // ??? //
    }
  }

}
