package ohnosequences.cosas

object functors {

  import typeConstructors._
  
  trait AnyFunctor {

    type TC <: AnyTypeConstructor
    val typeConstructor: TC

    // just for clarity, not essential
    type F[Z] = TC#of[Z]

    def map[X,Y](Fx: TC#of[X])(f: X => Y): F[Y]

    final def apply[X,Y](f: X => Y): F[X] => F[Y] = { fx => map(fx)(f) }
  }

  abstract class Functor[TC0 <: AnyTypeConstructor](val typeConstructor: TC0) extends AnyFunctor {

    type TC = TC0
  }

  object AnyFunctor {

    type For[TC0 <: AnyTypeConstructor] = AnyFunctor { type TC = TC0 }
  }

  trait AnyFunctorComposition extends AnyFunctor { composition =>

    type First <: AnyFunctor
    val first: First
    type Second <: AnyFunctor //{ type TC = First# }
    val second: Second

    implicit object SFTC extends AnyTypeConstructor {

      type of[X] = second.TC#of[first.TC#of[X]]
    }
    type TC = SFTC.type
    lazy val typeConstructor: TC = SFTC

    final def map[X,Y](Fx: F[X])(f: X => Y): F[Y] = second.map(Fx)(first(f))
  }

  case class FunctorComposition[F0 <: AnyFunctor, S0 <: AnyFunctor](val first: F0, val second: S0) extends AnyFunctorComposition {

    type First = F0
    type Second = S0
  }

  type ∘[G0 <: AnyFunctor, F0 <: AnyFunctor] = FunctorComposition[F0, G0]

  trait AnyFunctorSyntax extends Any {

    type FTC <: AnyTypeConstructor
    type X
    val Fx: FTC#of[X]

    final def map[Fnctr <: AnyFunctor.For[FTC], Y](f: X => Y)(implicit functor: Fnctr): Fnctr#F[Y] = functor.map(Fx)(f)

    // for testing
    final def mapp[Fnctr <: AnyFunctor.For[FTC], Y](f: X => Y)(implicit functor: Fnctr): Fnctr#F[Y] = map(f)
  }

  final case class FunctorSyntax[TC0 <: AnyTypeConstructor, X0](val Fx: TC0#of[X0]) 
    extends 
      AnyVal with 
      AnyFunctorSyntax 
  {  
    type FTC = TC0
    type X = X0
  }

  trait AnyFunctorModule {

    type Fnctr <: AnyFunctor

    implicit def functorSyntax[X](x: Fnctr#F[X]): FunctorSyntax[Fnctr#TC,X] = FunctorSyntax(x)

    implicit val functInst: Fnctr
  }

  class FunctorModule[Funct0 <: AnyFunctor](val funct: Funct0) extends AnyFunctorModule { 

    type Fnctr = Funct0 
    implicit val functInst: Fnctr = funct
  }

  trait AnyFunctorLaws {

    type FTC <: AnyTypeConstructor
    // TODO
    type Fnctr <: AnyFunctor { type TC = FTC }
    type F[Z] = FTC#of[Z] 
    val functor: Fnctr

    def preservesIdentities[X]: (F[X] => F[X], F[X] => F[X]) = 
      ( identity, (functor: Fnctr)(identity[X]) )

  }






  











  









  // TODO: move to a separate module. Decide on names.

  object SListFunctor extends Functor(SList) {

    final def map[X,Y](Fx: F[X])(f: X => Y): F[Y] = Fx map f
  }
  // modules with camel case
  object listFunctor extends FunctorModule(SListFunctor)

  object IdFunctor extends Functor(Id) {

    final def map[X,Y](Fx: F[X])(f: X => Y): F[Y] = f(Fx)
  }
  type IdFunctor = IdFunctor.type
  object idFunctor extends FunctorModule(IdFunctor)

  object MaybeFunctor extends Functor(Maybe) {

    final def map[X,Y](Fx: F[X])(f: X => Y): F[Y] = Fx map f
  }
  object maybeFunctor extends FunctorModule(MaybeFunctor)


}