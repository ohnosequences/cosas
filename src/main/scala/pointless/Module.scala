package ohnosequences.pointless

trait Module {

  trait Types {
    /*
      the underlying type
    */
    type Carrier

    abstract class Ops[X <: Carrier](val x: X)

    object Ops {

      // implicit def toOps[Z <: Carrier](x: Z)(implicit getOps: Z => Ops[Z]): Ops[Z] = getOps(x)
    }
  }
}