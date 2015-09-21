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

    type In1 = First#In1
    type Out = Second#Out
  }

  class Composition[
    F <: AnyDepFn1,
    S <: AnyDepFn1 { type In1 >: F#Out }
  ]
  (val first: F, val second: S) extends AnyDepFn1Composition {

    type First = F
    type Second = S

    // implicit def appForMe[
    //   X1 <: F#In1,
    //   M <: F#Out,
    //   O <: S#Out
    // ](implicit
    //   appF: App1[F,X1,M],
    //   appS: App1[S,M,O]
    // ): App1[this.type,X1,O] = App1 { x1: X1 => appS(appF(x1)) }
  }

  implicit final def depFn1Ops[DF <: AnyDepFn1, I0 <: DF#In1, O0 <: DF#Out](df: DF): DepFn1Ops[DF,I0,O0] =
    DepFn1Ops(df)
  case class DepFn1Ops[DF <: AnyDepFn1, I <: DF#In1, O <: DF#Out](val df: DF) extends AnyVal {

    def at(f: I => O): App1[DF,I,O] =
      App1(f)
  }

  implicit final def depFn2Ops[DF <: AnyDepFn2](df: DF): DepFn2Ops[DF] =
    DepFn2Ops(df)
  case class DepFn2Ops[DF <: AnyDepFn2](val df: DF) extends AnyVal {

    def at[X1 <: DF#In1, X2 <: DF#In2, Y <: DF#Out](f: (X1,X2) => Y): App2[DF,X1,X2,Y] =
      App2(f)
  }

  /* dependent function application machinery. These are to be thought of as the building blocks for terms of a dependent function type. */
  trait AnyAt {

    type DepFn <: AnyDepFn
  }
  trait At[P0 <: AnyDepFn] extends AnyAt {  type DepFn = P0  }

  trait At0[DF0 <: AnyDepFn0] extends AnyAt {

    type DepFn = DF0
    type Y <: DF0#Out
    def apply: Y
  }

  trait At1[DF1 <: AnyDepFn1] extends AnyAt {

    type DepFn = DF1
    type X1 <: DepFn#In1
    type Y <: DepFn#Out

    def apply(in: X1): Y
  }

  trait At2[DF2 <: AnyDepFn2] extends AnyAt {

    type DepFn = DF2
    type X1 <: DepFn#In1
    type X2 <: DepFn#In2
    type Y <: DepFn#Out

    def apply(in1: X1, in2: X2): Y
  }

  case class App1[DF <: AnyDepFn1, I <: DF#In1, O <: DF#Out](val does: I => O) extends At1[DF] {

    type X1 = I
    type Y = O

    final def apply(in: X1): Y =
      does(in)
  }

  implicit def depFn1ApplyOps[
    DF0 <: AnyDepFn1,
    X10 <: DF0#In1,
    Y0 <: DF0#Out
  ](df: DF0): DepFn1ApplyOps[DF0,X10,Y0] =
    DepFn1ApplyOps(df)

  case class DepFn1ApplyOps[DF0 <: AnyDepFn1, I0 <: DF0#In1, O0 <: DF0#Out](val df: DF0) {

    def apply(x1: I0)(implicit app: App1[DF0,I0,O0]): O0 =
      app(x1)
  }

  // implicit def compositionApplyOps[
  //   F <: AnyDepFn1, S <: AnyDepFn1 { type In1 >: F#Out },
  //   X1 <: F#In1, M <: F#Out
  // ](comp: Composition[F,S])(implicit
  //   appS: App1[S,M]
  // ): CompositionApplyOps[F,S,X1,M] = CompositionApplyOps(comp)
  //
  // case class CompositionApplyOps[
  //   F <: AnyDepFn1, S <: AnyDepFn1 { type In1 >: F#Out },
  //   X1 <: F#In1, M <: F#Out
  // ](val comp: Composition[F,S]) extends AnyVal {
  //
  //   def apply[O](x1: X1)(implicit
  //     appF: App1[F,X1] { type Out = M },
  //     appS: App1[S,M] { type Out = O }
  //   ): O = appS(appF(x1))
  // }

  // trait App2[P <: AnyDepFn2, I1 <: P#In1,I2 <: P#In2] extends At2[P] { type In1 = I1; type In2 = I2 }
  case class App2[P <: AnyDepFn2, I1 <: P#In1,I2 <: P#In2, O <: P#Out](val does: (I1,I2) => O) extends At2[P] {

    type X1 = I1; type X2 = I2
    type Y = O

    final def apply(in1: X1, in2: X2): Y =
      does(in1,in2)
  }

  implicit def depFn2ApplyOps[
    DF <: AnyDepFn2,
    X1 <: DF#In1,
    X2 <: DF#In2
  ](df: DF): DepFn2ApplyOps[DF,X1,X2] =
    DepFn2ApplyOps(df)
  case class DepFn2ApplyOps[DF <: AnyDepFn2, X1 <: DF#In1, X2 <: DF#In2](val df: DF) extends AnyVal {

    def apply[O <: DF#Out](x1: X1, x2: X2)(implicit app: App2[DF,X1,X2,O]): O =
      app(x1,x2)
  }
}
