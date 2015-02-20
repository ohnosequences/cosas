package ohnosequences.cosas

object functors {

  type ==>[A,B] = Function1[A,B]

  import typeConstructors._
  
  trait AnyFunctor {

    type TypeConstructor <: AnyTypeConstructor
    val typeConstructor: TypeConstructor

    // just for clarity, not essential
    type F[X] = TypeConstructor#of[X]

    def map[X,Y](Fx: F[X])(f: X => Y): F[Y]

    final def apply[X,Y](f: X => Y): F[X] => F[Y] = { fx => map(fx)(f) }
  }

  abstract class Functor[TC <: AnyTypeConstructor](val typeConstructor: TC) extends AnyFunctor {

    type TypeConstructor = TC
  }

  object AnyFunctor {

    type For[TC <: AnyTypeConstructor] = AnyFunctor { type TypeConstructor = TC }
  }

  trait AnyFunctorComposition extends AnyFunctor { composition =>

    type First <: AnyFunctor
    val first: First
    type Second <: AnyFunctor //{ type TypeConstructor = First# }
    val second: Second

    implicit object SFTC extends AnyTypeConstructor {

      type of[X] = second.TypeConstructor#of[first.TypeConstructor#of[X]]
    }
    type TypeConstructor = SFTC.type
    lazy val typeConstructor: TypeConstructor = SFTC

    final def map[X,Y](Fx: F[X])(f: X => Y): F[Y] = second.map(Fx)(first(f))
  }

  case class FunctorComposition[F0 <: AnyFunctor, S0 <: AnyFunctor](val first: F0, val second: S0) extends AnyFunctorComposition {

    type First = F0
    type Second = S0
  }

  type ∘[G0 <: AnyFunctor, F0 <: AnyFunctor] = FunctorComposition[F0, G0]

  trait AnyFunctorSyntax extends Any {

    type TC <: AnyTypeConstructor
    type X
    val Fx: TC#of[X]

    final def map[Fnctr <: AnyFunctor.For[TC], Y](f: X => Y)(implicit functor: Fnctr): Fnctr#F[Y] = functor.map(Fx)(f)

    // for testing
    final def mapp[Fnctr <: AnyFunctor.For[TC], Y](f: X => Y)(implicit functor: Fnctr): Fnctr#F[Y] = map(f)
  }

  final case class FunctorSyntax[TC0 <: AnyTypeConstructor, X0](val Fx: TC0#of[X0]) 
    extends 
      AnyVal with 
      AnyFunctorSyntax 
  {  
    type TC = TC0
    type X = X0
  }

  trait AnyFunctorModule {

    type Fnctr <: AnyFunctor

    implicit def functorSyntax[X](x: Fnctr#F[X]): FunctorSyntax[Fnctr#TypeConstructor,X] = FunctorSyntax(x)

    implicit val functInst: Fnctr
  }

  class FunctorModule[Funct0 <: AnyFunctor](val funct: Funct0) extends AnyFunctorModule { 

    type Fnctr = Funct0 
    implicit val functInst: Fnctr = funct
  }

  trait AnyFunctorLaws {

    type TC <: AnyTypeConstructor
    // TODO
    type Fnctr <: AnyFunctor { type TypeConstructor = TC }
    type F[Z] = TC#of[Z] 
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