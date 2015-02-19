package ohnosequences.cosas

object monads {

  import typeConstructors._
  import functors._
  import naturalTransformations._
  
  trait AnyMonad {

    // type TypeConstructor <: AnyTypeConstructor

    type Functor <: AnyFunctor//.For[TypeConstructor]
    val functor: Functor

    // just for clarity, not essential
    type M[Z] = Functor#F[Z]

    type η <: IdFunctor ~> Functor
    val η: η

    // this is actually syntax
    final def unit[X](x: X): M[X] = η.at(x)

    type μ <: (Functor ∘ Functor) ~> Functor
    val μ: μ

    def flatMap[X,Y](x: Functor#F[X], f: X => M[Y]): M[Y]
  }

  abstract class Monad[Fnctr <: AnyFunctor](val functor: Fnctr) extends AnyMonad {

    type Functor = Fnctr
    // type TypeConstructor = Fnctr#TypeConstructor
  }

  object AnyMonad {

    type For[F <: AnyFunctor] = AnyMonad { type Functor = F }
    // type For[F <: AnyTypeConstructor] = AnyMonad { type TypeConstructor = TC }
  }


  trait AnyMonadSyntax extends Any {

    type Fnctr <: AnyFunctor
    type X
    val Mx: Fnctr#F[X]

    final def flatMap[Mnd <: AnyMonad.For[Fnctr], Y](f: X => Mnd#M[Y])(implicit monad: Mnd): Mnd#M[Y] = 
      monad.flatMap(Mx, f)
  }

  final case class MonadSyntax[Fnctr0 <: AnyFunctor, X0](val Mx: Fnctr0#F[X0]) 
    extends 
      AnyVal with 
      AnyMonadSyntax 
  {  
    type Fnctr = Fnctr0
    type X = X0
  }

  trait AnyMonadModule {

    type Monad <: AnyMonad

    implicit final def functorSyntax[X](x: Monad#M[X]): FunctorSyntax[Monad#Functor#TypeConstructor,X] = FunctorSyntax(x)
    implicit val functorInstance: Monad#Functor

    implicit final def monadSyntax[X](x: Monad#M[X]): MonadSyntax[Monad#Functor, X] = MonadSyntax(x)
    implicit val monadInstance: Monad
  }

  class MonadModule[Mnd <: AnyMonad](val monad: Mnd) extends AnyMonadModule {

    type Monad = Mnd

    implicit val functorInstance: Monad#Functor = monad.functor
    implicit val monadInstance: Monad = monad
  }

  // TODO: move it somewhere else

  object IdMonad extends Monad(IdFunctor) {

    type η = naturalTransformations.Id[IdFunctor]
    val η = new naturalTransformations.Id(IdFunctor)

    object idMult extends NaturalTransformation[
      FunctorComposition[IdFunctor,IdFunctor],
      IdFunctor
    ](new FunctorComposition(IdFunctor,IdFunctor), IdFunctor) {

      final def at[X](x: X) = x
    }

    type μ = idMult.type
    val μ = idMult

    // final def unit[X](x: X): M[X] = x

    final def flatMap[X,Y](x: M[X], f: X => M[Y]): M[Y] = f(x)
  }
  object idMonad extends MonadModule(IdMonad)

  object SListMonad extends Monad(SListFunctor) {

    object LUnit extends NaturalTransformation(IdFunctor, SListFunctor) {

      final def at[X](x: X): M[X] = List(x)
    }

    type η = LUnit.type
    val η = LUnit

    type μ = SListMult.type
    val μ = SListMult

    // final def unit[X](x: X): M[X] = List(x)

    final def flatMap[X,Y](x: M[X], f: X => M[Y]): M[Y] = x flatMap f
  }
  object listMonad extends MonadModule(SListMonad)
}