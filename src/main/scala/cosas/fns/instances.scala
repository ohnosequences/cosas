package ohnosequences.cosas.fns

case object id extends DepFn1[Any, Any] {

  implicit def buhbuh[X]: App1[id.type,X,X] = id at { x: X => x }
}
