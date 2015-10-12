package ohnosequences.cosas

package object fns {

  type as[X,Y >: X] = As[X,Y]
  def as[X,Y >: X]: as[X,Y] = new as[X,Y]
}
