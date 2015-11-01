package ohnosequences.cosas.fns

case object syntax {

  case class DepFn1Syntax[DF <: AnyDepFn1](val df: DF) extends AnyVal {

    final def at[I <: DF#In1, O <: DF#Out](f: I => O): App1[DF,I,O] =
      App1(f)

    final def ∘[F <: AnyDepFn1 { type Out <: DF#In1 }](f: F): Composition[F,DF] =
      Composition(f,df)
  }

  case class DepFn2Syntax[DF <: AnyDepFn2](val df: DF) extends AnyVal {

    final def at[X1 <: DF#In1, X2 <: DF#In2, Y <: DF#Out](f: (X1,X2) => Y): App2[DF,X1,X2,Y] =
      App2(f)
  }

  case class DepFn3Syntax[DF <: AnyDepFn3](val df: DF) extends AnyVal {

    final def at[
      X1 <: DF#In1, X2 <: DF#In2, X3 <: DF#In3,
      Y <: DF#Out
    ]
    (f: (X1,X2,X3) => Y): App3[DF,X1,X2,X3,Y] =
      App3(f)
  }

  case class DepFn1ApplyAt[DF <: AnyDepFn1, A <: DF#In1](val df: DF) extends AnyVal {

    final def apply[Y0 <: DF#Out](x1: A)(implicit app: AnyApp1At[DF,A] { type Y = Y0 }): Y0 =
      app(x1)
  }

  case class DepFn2ApplyAt[DF <: AnyDepFn2, A <: DF#In1, B <: DF#In2](val df: DF) extends AnyVal {

    final def apply[Y0 <: DF#Out](x1: A, x2: B)(implicit app: AnyApp2At[DF,A,B] { type Y = Y0 }): Y0 = app(x1,x2)
  }

  case class DepFn3ApplyAt[DF <: AnyDepFn3, A <: DF#In1, B <: DF#In2, C <: DF#In3](val df: DF) extends AnyVal {

    final def apply[Y0 <: DF#Out](x1: A, x2: B, x3: C)(implicit app: AnyApp3At[DF,A,B,C] { type Y = Y0 }): Y0 =
      app(x1,x2,x3)
  }
}
