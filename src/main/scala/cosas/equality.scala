package ohnosequences.cosas

object equals {
  
  @annotation.implicitNotFound( msg = 
  """
  No proof of equality found for types:  

    ${A}

    ${B}
  """)
  sealed trait ≃[A, B] { 

    type Left = A
    type Right = B
    type Out >: A with B <: A with B 

    implicit def inL(a: A): Out
    implicit def inR(b: B): Out

    final implicit def elimL(o: Out): A = o
    final implicit def elimR(o: Out): B = o

    def sym: ≃[B, A]
  }

  final case class Refl[A]() extends (A ≃ A) { 

    final type Out = A

    final implicit def inL(a: A): Out = a
    final implicit def inR(b: A): Out = b

    final def sym = this
  } 

  type ?≃[X, Y] = Either[X,Y]
  type <≃>[A, B] = (A ?≃ B) => (A ≃ B)

  implicit def refl[A >: B <: B, B]: (A <≃> B) = x => Refl[B]()
  implicit def sym[A, B](implicit p: B <≃> A): A <≃> B = x => (p(x.swap).sym)
  implicit def reflInst[B]: B ≃ B = Refl[B]()
}