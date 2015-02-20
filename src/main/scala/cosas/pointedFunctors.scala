package ohnosequences.cosas

object pointedFunctors {

  import functors._
  import naturalTransformations._
  
  trait AnyPointedFunctor { pointedFunctor =>

    type Functor <: AnyFunctor
    val functor: Functor

    type Point <: AnyNaturalTransformation {

      type Source = IdFunctor 
      type Target = pointedFunctor.Functor
    }
    
    val point: Point
  }
}