package ohnosequences.cosas

object equality extends LowPrior {
  

  @annotation.implicitNotFound( msg = 
  """
  No proof of equality found for types:  

    ${A}

    ${B}
  """)
  trait Equality[A, B] { 

    type Out >: A with B <: A with B 

    implicit def inL(a: A): Out
    implicit def inR(b: B): Out

    implicit def swap(a:A): B

    final def left(o: Out): A = o
    final def right(o: Out): B = o
  }
  type EqualityContext[X, Y] = (X,Y)
  type ?≃[A, B] = EqualityContext[A,B] => (A Equality B)
  type ≃[A,B] = Equality[A,B]#Out
  final class Refl[A] extends (A Equality A) { 

    type Out = A

    implicit final def swap(a: A): A = a
    implicit final def inL(a: A): Out = a
    implicit final def inR(b: A): Out = b
  } 
  implicit def refl[A >: B <: B, B]: 
    (EqualityContext[A,B] => A Equality B) = x => new Refl[B]

  implicit def reflEq[B]: B Equality B = new Refl[B]
  implicit def kindEq[F[_], A](implicit p: A Equality A): F[A] Equality F[A] = new Refl[F[A]]
  
  // implicit def sym[A,B](implicit eqWitness: Equality)
  // implicit def coerce2[A,B](b: B)(implicit eqWitness: A ?≃ B): A ≃ B = b

  object coercion {

    implicit def coerceL[A, B](a: A)(implicit eqWitness: A ?≃ B): A ≃ B = a
    implicit def coerceR[A, B](a: A)(implicit eqWitness: B ?≃ A): B ≃ A = a
  }

}

trait LowPrior {

    import equality._

    implicit def refl2[A >: B <: B, B]: (EqualityContext[A,B] => B Equality A) = x => new Refl[A]

    implicit def sym[A,B >: A <: A, X <: ?≃[A, B]]: (B ?≃ A) = refl[B,A]
  }