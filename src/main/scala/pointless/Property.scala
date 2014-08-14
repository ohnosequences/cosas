package ohnosequences.pointless

trait property {

  // abstract dep
  type representable <: ohnosequences.pointless.representable
  val representable: representable
  import representable._

  type Property <: Representable

  case class ops[P <: Property](property: P)(implicit rep: representable.ops[P]) {

    def is(value: P#Raw): P#Rep = rep =>> value
  }
}