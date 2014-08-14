package ohnosequences.pointless

import scala.reflect.ClassTag

trait property {

  // abstract dep
  type representable <: ohnosequences.pointless.representable

  type Property <: representable#Representable

  abstract class PropertyOps[P <: Property](val property: P)(implicit getOpsRep: P => representable#RepresentableOps[P]) {

    val ops: representable#RepresentableOps[P] = getOpsRep(property)

    def is(value: P#Raw): P#Rep = (ops) =>> value

    def label: String
    def classTag: ClassTag[_ <: P#Raw]
  }
}