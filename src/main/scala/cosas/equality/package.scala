package ohnosequences.cosas

package object equality {

  type ?≃[X, Y] = Either[X,Y]
  type <≃>[A, B] = (A ?≃ B) => (A ≃ B)

  implicit def refl[A >: B <: B, B]: (A <≃> B) = x => Refl[B]()
  implicit def sym[A, B](implicit p: B <≃> A): A <≃> B = x => (p(x.swap).sym)
  implicit def reflInst[B]: B ≃ B = Refl[B]()
}
