package ohnosequences.cosas

object laxMonoidalFunctors {

  import functors._
  
  trait AnyLaxMonoidalFunctor {

    type Functor <: AnyFunctor
    val functor: Functor

    type F[X] = Functor#F[X]

    // the unit type here for the cartesian struct is suppposed to be Unit; there's just (in theory) one function to unit: f(x) = ()
    def η: F[Unit]

    def μ[X,Y](Fx: F[X], Fy: F[Y]): F[(X,Y)]
  }

  abstract class LaxMonoidalFunctor[Fnctr <: AnyFunctor](val functor: Fnctr) extends AnyLaxMonoidalFunctor {

    type Functor = Fnctr
  }

  import monads._

  trait AnyLaxMonoidalFunctorFromMonad extends AnyLaxMonoidalFunctor {

    type Monad <: AnyMonad
    val monad: Monad

    type Functor = Monad#Functor
    val functor = monad.functor
    

    def η: F[Unit] = monad.unit( () )

    // def μ[X,Y](Fx: F[X], Fy: F[Y]): F[(X,Y)] = 
  }


  // TODO: syntax

  // TODO: again, move to a better place
  object SListLaxMonoidalFunctor extends LaxMonoidalFunctor(SListFunctor) {

    final def η: F[Unit] = List(())

    final def μ[X,Y](Fx: F[X], Fy: F[Y]): F[(X,Y)] = Fx zip Fy

  }
}

