package ohnosequences.cosas


object naturalTransformations {
  
  import functors._  

  trait AnyNaturalTransformation {

    type Source <: AnyFunctor
    val source: Source

    type Target <: AnyFunctor
    val target: Target

    def at[X](fx: Source#F[X]): Target#F[X]
  }

  abstract class NaturalTransformation[
    S <: AnyFunctor,
    T <: AnyFunctor
  ](
    val source: S,
    val target: T
  )
  extends AnyNaturalTransformation {

    type Source = S
    type Target = T
  }

  type ~>[S <: AnyFunctor, T <: AnyFunctor] = AnyNaturalTransformation { type Source = S; type Target = T }

  trait AnyId extends AnyNaturalTransformation {

    type On <: AnyFunctor
    val on: On
    type Source = On
    lazy val source = on
    type Target = On
    lazy val target = on

    final def at[X](fx: Source#F[X]): Target#F[X] = fx
  }

  class Id[F <: AnyFunctor](val on: F) extends AnyId {

    type On = F
  }

  trait AnyVerticalComposition extends AnyNaturalTransformation { composition =>

    type First <: AnyNaturalTransformation
    val first: First
    type Second <: AnyNaturalTransformation { type Source = First#Target }
    val second: Second

    type Source = first.Source
    lazy val source = first.source
    type Target = Second#Target
    lazy val target = second.target

    final def at[X](fx: Source#F[X]): Target#F[X] = second.at(first.at(fx))
  }

  case class VerticalComposition[
    U <: AnyNaturalTransformation,
    V <: AnyNaturalTransformation { type Source = U#Target }
  ](val first: U, val second: V) extends AnyVerticalComposition {

    type First = U
    type Second = V
  }

  /* Functors are all Scala => Scala so you can always do this */
  trait AnyHorizontalComposition {

    type First <: AnyNaturalTransformation
    val first: First
    type Second <: AnyNaturalTransformation
    val second: Second

    // type Source

  }


  object SListUnit extends NaturalTransformation(IdFunctor, SListFunctor) {

    final def at[X](x: X): List[X] = List(x)
  }
}