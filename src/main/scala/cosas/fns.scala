package ohnosequences.cosas

object fns {

  trait AnyFn { type Out }

  trait OutBound[X] extends AnyFn { type Out <: X }
  trait Out[X]      extends AnyFn { type Out = X }

  trait ContainerOut extends AnyFn { type O }
  trait OutInContainer[F[_]] extends ContainerOut { type Out = F[O] }
  trait InContainer[X] extends ContainerOut { type O = X }


  /* Fns with different number of arguments */
  trait AnyFn0 extends AnyFn with shapeless.DepFn0

  trait Fn0 extends AnyFn0


  trait AnyFn1 extends AnyFn {

    type In1

    def apply(in1: In1): Out
  }

  trait Fn1[A] extends AnyFn1 with shapeless.DepFn1[A] { type In1 = A }


  trait AnyFn2 extends AnyFn {

    type In1; type In2

    def apply(in1: In1, in2: In2): Out
  }

  trait Fn2[A, B] extends AnyFn2 with shapeless.DepFn2[A, B] { type In1 = A; type In2 = B }


  trait AnyFn3 extends AnyFn {

    type In1; type In2; type In3

    def apply(in1: In1, in2: In2, in3: In3): Out
  }

  trait Fn3[A, B, C] extends AnyFn3 { type In1 = A; type In2 = B; type In3 = C }



  /* Dependent functions aka dependent products */
  trait AnyDepFn
  trait DepFn1 extends AnyDepFn
  trait DepFn2 extends AnyDepFn
  trait DepFn3 extends AnyDepFn

  implicit final def depFn1Ops[DF <: DepFn1](df: DF): DepFn1Ops[DF] =
    DepFn1Ops(df)
  case class DepFn1Ops[DF <: DepFn1](df: DF) extends AnyVal {

    def apply[X, Y](x: X)(implicit at: At1[DF] { type In = X; type Out = Y }): Y =
      at(x)

    def at[X,Y](f: X => Y): At1[DF] { type In = X; type Out = Y } =
      app1[DF,X,Y](f)
  }

  trait AnyAt {

    type DepFn <: AnyDepFn
  }
  trait At[P0 <: AnyDepFn] extends AnyAt {  type DepFn = P0  }

  trait At1[DF1 <: DepFn1] extends At[DF1] {

    type In
    type Out

    def apply(in: In): Out
  }

  case class app1[DF <: DepFn1,I,O](val does: I => O) extends At1[DF] {

    type In = I
    type Out = O

    final def apply(in: In): Out = does(in)
  }

  case class app2[P <: DepFn2, I1,I2,O](val does: (I1,I2) => O) {

    type In1 = I1; type In2 = I2
    type Out = O

    final def apply(in1: In1, in2: In2): Out = does(in1,in2)
  }
}
