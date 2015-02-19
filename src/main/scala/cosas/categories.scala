package ohnosequences.cosas

object categories {
  
  trait AnyCategory {

    type MorphismsType <: AnyMorphismType
    type Obj = MorphismsType#ObjectType#Bound
    type C[X <: Obj, Y <: Obj] = MorphismsType#Morphisms[X,Y]

    def id[X <: Obj]: C[X,X]

    def compose[X <: Obj, Y <: Obj, Z <: Obj](g: C[Y,Z], f: C[X,Y]): C[X,Z]

  }

  trait AnyObjectType {

    type Bound
  }
  trait AnyMorphismType {

    type ObjectType <: AnyObjectType
    type Morphisms[X <: ObjectType#Bound, Y <: ObjectType#Bound]
  }

  object ScalaType extends AnyObjectType { type Bound = Any }
  object FunctionMorphismType extends AnyMorphismType {

    final type ObjectType = ScalaType.type
    type Morphisms[X <: Any, Y <: Any] = (X => Y)
  }

  object ScalaCategory extends AnyCategory {

    type MorphismsType = FunctionMorphismType.type

    def id[X <: Obj]: C[X,X] = { x: X => x }

    def compose[X <: Obj, Y <: Obj, Z <: Obj](g: C[Y,Z], f: C[X,Y]): C[X,Z] = f andThen g
  }

  trait AnyKleisliMorphisms extends AnyMorphismType {

    import monads._

    type Monad <: AnyMonad
    val monad: Monad

    final type ObjectType = ScalaType.type
    type Morphisms[X <: Any, Y <: Any] = (X => Monad#M[Y])
  }

  trait AnyKleisliCategory extends AnyCategory { kc =>

    type Mnd <: monads.AnyMonad with Singleton
    val mnd: Mnd

    type MorphismsType <: AnyKleisliMorphisms { type Monad = kc.Mnd }

    final def id[X <: Obj]: C[X,X] = { x: X => mnd.unit(x) }
    final def compose[X <: Obj, Y <: Obj, Z <: Obj](g: C[Y,Z], f: C[X,Y]): C[X,Z] = { 

      x: X => (mnd: Mnd).flatMap(f(x), g) 
    }

  }

  // TODO: a more interesting example
  // trait AnyEMmorphism extends AnyMorphismType {

  //   import monads._

  //   type type Monad <: AnyMonad

  //   final type ObjectType = AnyEMalgebra.for[Monad]
  //   final type SourceBound = ObjectType#Bound
  //   final type TargetBound = ObjectType#Bound
  // }


  /*
  the canonical example is where 

  - objects are just subtypes of Any
  - the type of morphisms is X => Y

  So for objects we use a bound X <: Any, and for morphisms assuming something is an object
  */



  trait AnyBoundedTypeConstructor {

    type Domain
    type Codomain

    type of[X <: Domain] <: Codomain
  }

  trait AnyF { functor =>

    type TypeConstructor <: AnyBoundedTypeConstructor with Singleton { 
      type Domain >: functor.Source#Obj 
      type Codomain <: functor.Target#Obj
    }
    val typeConstructor: TypeConstructor

    type F[Z <: Source#Obj] = TypeConstructor#of[Z]

    type Source <: AnyCategory with Singleton
    type Target <: AnyCategory with Singleton

    def map[X <: Source#Obj, Y <: Source#Obj](f: Source#C[X,Y]): Target#C[F[X], F[Y]]
  }
}