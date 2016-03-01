package ohnosequences.cosas

package object elgot {

  def output[L,R](r: R) : Either[L,R] = Right(r)
  def recurse[L,R](l: L): Either[L,R] = Left(l)
}
