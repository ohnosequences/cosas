package ohnosequences.cosas

object equality extends LowPrior {
  

  @annotation.implicitNotFound( msg = 
  """
  No proof of equality found for types:  

    ${A}

    ${B}
  """)
  trait Equality[A, B] { type Out >: A with B <: A with B }
  type EqualityContext[X, Y] = (X,Y)
  type ?≃[A, B] = EqualityContext[A,B] => (A Equality B)
  type ≃[A,B] = Equality[A,B]#Out
  trait Refl[A] extends (A Equality A) { type Out = A }
  implicit def refl[A >: B <: B, B]: 
    (EqualityContext[A,B] => A Equality B) = x => new Refl[B] {}
  
  // implicit def sym[A,B](implicit eqWitness: Equality)
  // implicit def coerce2[A,B](b: B)(implicit eqWitness: A ?≃ B): A ≃ B = b

  object coercion {

    implicit def coerceL[A, B](a: A)(implicit eqWitness: A ?≃ B): A ≃ B = a
    implicit def coerceR[A, B](a: A)(implicit eqWitness: B ?≃ A): B ≃ A = a
  }

}

trait LowPrior {

    import equality._

    implicit def refl2[A >: B <: B, B]: (EqualityContext[A,B] => B Equality A) = x => new Refl[A] {} 

    implicit def sym[A,B >: A <: A, X <: ?≃[A, B]]: (B ?≃ A) = refl[B,A]
  }