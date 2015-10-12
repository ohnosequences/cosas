package ohnosequences.cosas.fns

case object identity extends DepFn1[Any, Any] {

  implicit def default[XXXX <: In1]: App1[identity.type,XXXX,XXXX] = identity at { x: XXXX => x }
}

class As[X, Y >: X] extends DepFn1[X,Y]

case object As {

  implicit def default[A, B >: A]: App1[As[A,B],A,B] = App1 { a: A =>  a}
}
