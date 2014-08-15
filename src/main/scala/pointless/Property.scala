package ohnosequences.pointless

import scala.reflect.ClassTag

trait anyProperty {

  // abstract dep
  type representable <: ohnosequences.pointless.anyRepresentable

  type AnyProperty <: representable#AnyRepresentable

  abstract class AnyPropertyOps[P <: AnyProperty](val property: P)
    (implicit getOpsRep: P => representable#AnyRepresentableOps[P]) {

    val ops: representable#AnyRepresentableOps[P] = getOpsRep(property)

    def is(value: P#Raw): P#Rep = (ops) =>> value

    def label: String
    def classTag: ClassTag[_ <: P#Raw]
  }
}
