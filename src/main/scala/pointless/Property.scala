package ohnosequences.pointless

import scala.reflect.ClassTag

trait AnyProperty extends AnyRepresentable {

  val label: String
  val classTag: ClassTag[_ <: Raw]
}

object AnyProperty {

  abstract class Ops[P <: AnyProperty](val property: P)
    (implicit repOps: P => AnyRepresentable.Ops[P]) {

    val ops: AnyRepresentable.Ops[P] = repOps(property)

    def is(value: P#Raw): P#Rep = (ops) =>> value
  }
}
