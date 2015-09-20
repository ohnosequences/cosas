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
  trait AnyDepFn2 extends AnyDepFn { type In1; type In2 }
  trait DepFn2[I1,I2,O] extends AnyDepFn2 { type In1 = I1; type In2 = I2; type Out = O }
  // TODO moar arities
  // trait AnyDepFn3 extends AnyDepFn { type In1; type In2 }

  trait AnyDepFn1Composition extends AnyDepFn1 {

    type First <: AnyDepFn1
    val first: First
    type Second <: AnyDepFn1 { type In1 >: First#Out }
    val second: Second

    type In1 <: First#In1
    type Out <: Second#Out
  }

  case class Composition[F <: AnyDepFn1, S <: AnyDepFn1 { type In1 >: F#Out }](val first: F, val second: S) extends AnyDepFn1Composition {

    type First = F
    type In1 = First#In1
    type Second = S
    type Out = Second#Out

    implicit def appForMe[
      X1 <: F#In1,
      M <: S#In1,
      O <: S#Out
    ](implicit
      appF: App1[F,X1] { type Out = M },
      appS: App1[S,M] { type Out = O }
    ): App1[this.type,X1] { type Out = O } = app1 { x1: X1 => appS(appF(x1)) }
  }

  implicit final def depFn1Ops[DF <: AnyDepFn1](df: DF): DepFn1Ops[DF] =
    DepFn1Ops(df)
  case class DepFn1Ops[DF <: AnyDepFn1](val df: DF) extends AnyVal {

    def at[X <: DF#In1, Y <: DF#Out](f: X => Y): app1[DF,X,Y] =
      app1[DF,X,Y](f)
  }

  implicit final def depFn2Ops[DF <: AnyDepFn2](df: DF): DepFn2Ops[DF] =
    DepFn2Ops(df)
  case class DepFn2Ops[DF <: AnyDepFn2](val df: DF) extends AnyVal {

    def at[X1 <: DF#In1, X2 <: DF#In2, Y <: DF#Out](f: (X1,X2) => Y): app2[DF,X1,X2,Y] =
      app2(f)
  }

  /* dependent function application machinery. These are to be thought of as the building blocks for terms of a dependent function type. */
  trait AnyAt {

    type DepFn <: AnyDepFn
    type Out <: DepFn#Out
  }
  trait At[P0 <: AnyDepFn] extends AnyAt {  type DepFn = P0  }

  trait At0[DF0 <: AnyDepFn0] extends At[DF0] {

    def apply: Out
  }

  trait At1[DF1 <: AnyDepFn1] extends At[DF1] {

    type In1 <: DepFn#In1

    def apply(in: In1): Out
  }

  trait At2[DF2 <: AnyDepFn2] extends At[DF2] {

    type In1 <: DepFn#In1
    type In2 <: DepFn#In2

    def apply(in1: In1, in2: In2): Out
  }

  // TODO case-sensitive blahblahblah
  trait App1[DF <: AnyDepFn1, I <: DF#In1] extends At1[DF] { type In1 = I }
  case class app1[DF <: AnyDepFn1, I <: DF#In1, O <: DF#Out](val does: I => O) extends App1[DF,I] {

    type Out = O

    final def apply(in: In1): Out =
      does(in)
  }

  implicit def depFn1ApplyOps[DF <: AnyDepFn1, X1 <: DF#In1](df: DF): DepFn1ApplyOps[DF,X1] =
    DepFn1ApplyOps(df)
  case class DepFn1ApplyOps[DF <: AnyDepFn1, X1 <: DF#In1](val df: DF) extends AnyVal {

    def apply[O](x1: X1)(implicit app: App1[DF,X1] { type Out = O }): O =
      app(x1)
  }

  implicit def compositionApplyOps[
    F <: AnyDepFn1, S <: AnyDepFn1 { type In1 >: F#Out },
    X1 <: F#In1, M <: F#Out
  ](comp: Composition[F,S])(implicit
    appS: App1[S,M]
  ): CompositionApplyOps[F,S,X1,M] = CompositionApplyOps(comp)

  case class CompositionApplyOps[
    F <: AnyDepFn1, S <: AnyDepFn1 { type In1 >: F#Out },
    X1 <: F#In1, M <: F#Out
  ](val comp: Composition[F,S]) extends AnyVal {

    def apply[O](x1: X1)(implicit
      appF: App1[F,X1] { type Out = M },
      appS: App1[S,M] { type Out = O }
    ): O = appS(appF(x1))
  }

  trait App2[P <: AnyDepFn2, I1 <: P#In1,I2 <: P#In2] extends At2[P] { type In1 = I1; type In2 = I2 }
  case class app2[P <: AnyDepFn2, I1 <: P#In1,I2 <: P#In2, O <: P#Out](val does: (I1,I2) => O) extends App2[P,I1,I2] {

    type Out = O

    final def apply(in1: In1, in2: In2): Out =
      does(in1,in2)
  }

  implicit def depFn2ApplyOps[DF <: AnyDepFn2, X1 <: DF#In1, X2 <: DF#In2](df: DF): DepFn2ApplyOps[DF,X1,X2] =
    DepFn2ApplyOps(df)
  case class DepFn2ApplyOps[DF <: AnyDepFn2, X1 <: DF#In1, X2 <: DF#In2](val df: DF) extends AnyVal {

    def apply[O](x1: X1, x2: X2)(implicit app: App2[DF,X1,X2] { type Out = O }): O =
      app(x1,x2)
  }
}
