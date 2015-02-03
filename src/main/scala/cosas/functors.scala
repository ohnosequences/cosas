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

  trait AnyFunctorSyntax extends Any {

    type TC <: AnyTypeConstructor
    type X
    val Fx: TC#of[X]

    final def map[Funct <: AnyFunctor.For[TC], Y](f: X => Y)(implicit functor: Funct): Funct#F[Y] = functor.map(Fx)(f)

    // for testing
    final def mapp[Funct <: AnyFunctor.For[TC], Y](f: X => Y)(implicit functor: Funct): Funct#F[Y] = map(f)
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

    type Funct <: AnyFunctor

    implicit def functorSyntax[X](x: Funct#F[X]): FunctorSyntax[Funct#TypeConstructor,X] = FunctorSyntax(x)

    implicit val functInst: Funct
  }

  class FunctorModule[Funct0 <: AnyFunctor](val funct: Funct0) extends AnyFunctorModule { 

    type Funct = Funct0 
    implicit val functInst: Funct = funct
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
  object idFunctor extends FunctorModule(IdFunctor)

  object MaybeFunctor extends Functor(Maybe) {

    final def map[X,Y](Fx: F[X])(f: X => Y): F[Y] = Fx map f
  }
  object maybeFunctor extends FunctorModule(MaybeFunctor)


}