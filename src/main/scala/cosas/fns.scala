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
  trait AnyDepFn { type Out }
  trait AnyDepFn0 extends AnyDepFn
  trait AnyDepFn1 extends AnyDepFn { type In1 }
  trait DepFn1[I,O] extends AnyDepFn1 { type In1 = I; type Out = O }
  trait AnyDepFn2 extends AnyDepFn {
    type In1; type In2
  }
  trait DepFn2[I1,I2,O] extends AnyDepFn2 { type In1 = I1; type In2 = I2; type Out = O }
  trait AnyDepFn3 extends AnyDepFn { type In1; }

  implicit final def depFn1Ops[DF <: AnyDepFn1](df: DF): AnyDepFn1Ops[DF] =
    AnyDepFn1Ops(df)
  case class AnyDepFn1Ops[DF <: AnyDepFn1](df: DF) extends AnyVal {

    def apply[X <: DF#In1, Y <: DF#Out](x: X)(implicit at: App1[DF,X] { type Out = Y }): Y =
      at(x)


    def at[X <: DF#In1, Y <: DF#Out](f: X => Y): app1[DF,X,Y] =
      app1[DF,X,Y](f)
  }

  implicit final def depFn2Ops[DF <: AnyDepFn2](df: DF): AnyDepFn2Ops[DF] =
    AnyDepFn2Ops(df)

  case class AnyDepFn2Ops[DF <: AnyDepFn2](df: DF) extends AnyVal {

    // def apply[X1 <: DF#In1, X2 <: DF#In2, Y <: DF#Out](x1: X1, x2: X2)(implicit at: App2[DF,X1,X2] { type Out = Y }): Y =
      // at(x1,x2)

    def at[X1 <: DF#In1, X2 <: DF#In2, Y <: DF#Out](f: (X1,X2) => Y): app2[DF, X1, X2, Y] =
      app2[DF,X1,X2,Y](f)
  }

  implicit def getDepFn2ApplyOps[DF <: AnyDepFn2, X1 <: DF#In1, X2 <: DF#In2](df: DF): DepFn2ApplyOps[DF,X1,X2] =
    DepFn2ApplyOps(df)

  case class DepFn2ApplyOps[DF <: AnyDepFn2, X1 <: DF#In1, X2 <: DF#In2](val df: DF) {

    def apply(x1: X1, x2: X2)(implicit app: App2[DF,X1,X2]): app.Out = app(x1,x2)
  }











  trait AnyAt {

    type DepFn <: AnyDepFn
  }
  trait At[P0 <: AnyDepFn] extends AnyAt {  type DepFn = P0  }

  trait At1[DF1 <: AnyDepFn1] extends At[DF1] {

    type In1 <: DepFn#In1
    type Out <: DepFn#Out

    def apply(in: In1): Out
  }

  trait At2[DF2 <: AnyDepFn2] extends At[DF2] {

    type In1 <: DepFn#In1
    type In2 <: DepFn#In2
    type Out <: DepFn#Out

    def apply(in1: In1, in2: In2): Out

  }

  trait App1[DF <: AnyDepFn1, I <: DF#In1] extends At1[DF] { type In1 = I }
  case class app1[DF <: AnyDepFn1, I <: DF#In1,O <: DF#Out](val does: I => O) extends App1[DF,I] {

    // type In1 = I
    type Out = O

    final def apply(in: In1): Out = does(in)
  }

  trait App2[P <: AnyDepFn2, I1 <: P#In1,I2 <: P#In2] extends At2[P] {

    type In1 = I1; type In2 = I2
  }
  case class app2[P <: AnyDepFn2, I1 <: P#In1,I2 <: P#In2, O <: P#Out](val does: (I1,I2) => O) extends App2[P,I1,I2] {

    // type In1 = I1; type In2 = I2
    type Out = O

    final def apply(in1: In1, in2: In2): Out = does(in1,in2)
  }
}
