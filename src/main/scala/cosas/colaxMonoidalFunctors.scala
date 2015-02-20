package ohnosequences.cosas

object colaxMonoidalFunctors {

  import functors._
  import typeConstructors._

  trait AnyLaxMonoidalFunctor {

    type TC <: AnyTypeConstructor
    type Functor <: AnyFunctor { type TypeConstructor = TC }
    val functor: Functor

    // The need for this is a compiler bug. Ah val _32 I love you!
    type C[X] = TC#of[X]

    // the unit type here for the cartesian struct is suppposed to be Unit; there's just (in theory) one function to unit: f(x) = ()
    final def ε(u: C[Unit]): Unit = ()

    // TODO: add projections
    final def δ[X,Y](Fxy: C[(X,Y)]): (C[X],C[Y]) = (
      functor.map(Fxy) { x: (X,Y) => x._1: X },
      functor.map(Fxy) { x: (X,Y) => x._2: Y }
    )

  }
}


