package ohnosequences.cosas.fns

case object identity extends DepFn1[Any, Any] {

  implicit def default[X]: App1[identity.type,X,X] = identity at { x: X => x }
}
