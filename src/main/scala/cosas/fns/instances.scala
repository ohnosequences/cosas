package ohnosequences.cosas.fns

case object identity extends DepFn1[Any, Any] {

  implicit def default[XXXX <: In1]: App1[identity.type,XXXX,XXXX] = identity at { x: XXXX => x }
}
