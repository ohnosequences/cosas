package ohnosequences.pointless

import scala.reflect.ClassTag

trait anyProperty {

  // abstract dep
  type representable <: anyRepresentable

  type AnyProperty <: AnyPropertyImpl with representable#AnyRepresentableImpl

  trait AnyPropertyImpl { self: representable#AnyRepresentableImpl =>

    val label: String
    val classTag: ClassTag[_ <: Raw]
  }

  abstract class AnyPropertyOps[P <: AnyProperty](val property: P)
    (implicit getOpsRep: P => representable#AnyRepresentableOps[P]) {

    val ops: representable#AnyRepresentableOps[P] = getOpsRep(property)

    def is(value: P#Raw): P#Rep = (ops) =>> value
  }
}
