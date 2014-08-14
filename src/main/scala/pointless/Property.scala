package ohnosequences.pointless

trait property {

  // abstract dep
  type representable <: ohnosequences.pointless.representable
  val representable: representable
  import representable._

  type Property <: Representable

  case class ops[P <: Property](property: P)(implicit getOpsRep: P => representable.ops[P]) {

    def is(value: P#Raw): P#Rep = getOpsRep(property) =>> value
  }
}