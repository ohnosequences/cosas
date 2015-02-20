package ohnosequences.cosas

object colaxMonoidalFunctors {

  import functors._
  import typeConstructors._

  trait AnyColaxMonoidalFunctor {

    type TC0 <: AnyTypeConstructor
    type Fnctr <: AnyFunctor { type TC = TC0 }
    val functor: Fnctr

    // The need for this is a compiler bug. Ah val _32 I love you!
    type C[Z] = TC0#of[Z]

    // the unit type here for the cartesian struct is suppposed to be Unit; there's just (in theory) one function to unit: f(x) = ()
    final def ε(u: C[Unit]): Unit = ()

    // TODO: add projections
    final def δ[X,Y](Fxy: C[(X,Y)]): (C[X],C[Y]) = (
      functor.map(Fxy) { x: (X,Y) => x._1: X },
      functor.map(Fxy) { x: (X,Y) => x._2: Y }
    )
  }

  
  class colaxMonoidalFunctor[
    Fnctr0 <: AnyFunctor
  ](val functor: Fnctr0 with AnyFunctor { type TC = Fnctr0#TC }) extends AnyColaxMonoidalFunctor {

    type TC0 = Fnctr0#TC
    type Fnctr = Fnctr0 with AnyFunctor { type TC = Fnctr0#TC }
  }

  object colaxList extends colaxMonoidalFunctor(SListFunctor)


  trait A {

    type MyB <: B
  }

  trait B {

    type of[X]
  }

  trait AnyUsesA {

    type BofMyA <: B
    type MyA <: A { type MyB = BofMyA }

    def buh[Z](b: BofMyA#of[Z]): BofMyA#of[Z] 
  }

  class UsesA[A0 <: A] {

    type BofMyA = A0#MyB
    type MyA = A0

    def buh[Z](b: BofMyA#of[Z]): BofMyA#of[Z] = b
  }

}


