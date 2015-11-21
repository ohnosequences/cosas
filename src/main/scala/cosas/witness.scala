package ohnosequences.cosas

trait AnyWitness extends Any {

  type For
}
case object Witness
case class Witness[X](val unique: Witness.type) extends AnyVal with AnyWitness { type For = X }
