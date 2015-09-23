package ohnosequences.cosas.fns

/* Dependent functions aka dependent products */
trait AnyDepFn { type Out }
trait AnyDepFn0 extends AnyDepFn
trait AnyDepFn1 extends AnyDepFn { type In1 }
trait DepFn1[I,O] extends AnyDepFn1 { type In1 = I; type Out = O }
trait AnyDepFn2 extends AnyDepFn { type In1; type In2 }
trait DepFn2[I1,I2,O] extends AnyDepFn2 { type In1 = I1; type In2 = I2; type Out = O }
// TODO moar arities
// trait AnyDepFn3 extends AnyDepFn { type In1; type In2 }

trait Out[F[_]] extends AnyDepFn {

  type O
  type Out = F[O]
}


trait AnyDepFn1Composition extends AnyDepFn1 {

  type First <: AnyDepFn1 { type Out <: Second#In1 }
  val first: First
  type Second <: AnyDepFn1
  val second: Second

  type In1 = First#In1
  type Out = Second#Out
}

class Composition[
  F <: AnyDepFn1 { type Out <: S#In1 },
  S <: AnyDepFn1
]
(val first: F, val second: S) extends AnyDepFn1Composition {

  type First = F
  type Second = S

  implicit def appForMe[
    X1 <: First#In1,
    M <: First#Out,
    O <: Second#Out
  ](implicit
    appF: App1[F,X1,M],
    appS: App1[S,M,O]
  ): App1[this.type,X1,O] = App1 { x1: X1 => appS(appF(x1)) }
}

/* dependent function application machinery. These are to be thought of as the building blocks for terms of a dependent function type. */
trait AnyAt extends Any {

  type DepFn <: AnyDepFn
  type Y <: DepFn#Out
}

trait AnyAt0 extends Any with AnyAt {

  type DepFn <: AnyDepFn0

  def apply: Y
}

trait AnyAt1 extends Any with AnyAt {

  type DepFn <: AnyDepFn1
  type X1 <: DepFn#In1

  def apply(in: X1): Y
}

trait AnyAt2 extends Any with AnyAt {

  type DepFn <: AnyDepFn2
  type X1 <: DepFn#In1; type X2 <: DepFn#In2

  def apply(in1: X1, in2: X2): Y
}


case class App1[DF <: AnyDepFn1, I <: DF#In1, O <: DF#Out](val does: I => O) extends AnyVal with AnyAt1 {

  type DepFn = DF
  type X1 = I
  type Y = O

  final def apply(in: X1): Y =
    does(in)
}

case class App2[DF <: AnyDepFn2, I1 <: DF#In1, I2 <: DF#In2, O <: DF#Out](val does: (I1,I2) => O) extends AnyVal with AnyAt2 {

  type DepFn = DF
  type X1 = I1; type X2 = I2
  type Y = O

  final def apply(in1: X1, in2: X2): Y =
    does(in1,in2)
}
