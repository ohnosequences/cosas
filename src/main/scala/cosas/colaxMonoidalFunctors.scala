package ohnosequences.cosas

object colaxMonoidalFunctors {

  import functors._

  trait AnyLaxMonoidalFunctor {

    type Functor <: AnyFunctor
    val functor: Functor

    // The need for this . here is a compiler bug
    type C[X] = functor.F[X]

    // the unit type here for the cartesian struct is suppposed to be Unit; there's just (in theory) one function to unit: f(x) = ()
    final def ε(u: C[Unit]): Unit = ()

    // TODO: add projections
    final def δ[X,Y](Fxy: C[(X,Y)]): (C[X],C[Y]) = (
      functor.map(Fxy) { x: (X,Y) => x._1: X },
      functor.map(Fxy) { x: (X,Y) => x._2: Y }
    )

  }
}


