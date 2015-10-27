package ohnosequences.cosas

trait AnyWitness extends Any {

  type For
}
case object Witness {

  type Of[Z] = AnyWitness { type For <: Z }
  type For[Z] = AnyWitness { type For = Z }
}
case class Witness[X](val unique: Witness.type) extends AnyVal with AnyWitness { type For = X }
