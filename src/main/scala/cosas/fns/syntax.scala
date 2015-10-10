package ohnosequences.cosas.fns

case object syntax {

  case class DepFn1Syntax[DF <: AnyDepFn1](val df: DF) extends AnyVal {

    final def at[I <: DF#In1, O <: DF#Out](f: I => O): App1[DF,I,O] =
      App1(f)
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

  case class DepFn1ApplySyntax[DF <: AnyDepFn1, I0 <: DF#In1, O <: DF#Out](val df: DF) extends AnyVal {

    final def apply(x1: I0)(implicit app: App1[DF,I0,O]): O =
      app(x1)
  }

  // case class DepFn2ApplySyntax[DF <: AnyDepFn2, X1 <: DF#In1, X2 <: DF#In2, O <: DF#Out](val df: DF) extends AnyVal {
  //
  //   final def apply(x1: X1, x2: X2)(implicit app: App2[DF,X1,X2,O]): O =
  //     app(x1,x2)
  // }

  case class DepFn2ApplyAt[DF <: AnyDepFn2, A <: DF#In1, B <: DF#In2](val df: DF) {

    final def apply[Y0 <: DF#Out](x1: A, x2: B)(implicit app: AnyApp2At[DF,A,B] { type Y = Y0 }): Y0 = app(x1,x2)
  }
  // case class DepFn2ApplySyntax[DF <: AnyDepFn2](val df: DF) extends AnyVal {
  //
  //   final def apply[
  //     A <: DF#In1, B <: DF#In2,
  //     Z <: DF#Out,
  //     App <: AnyApp2 { type DepFn = DF; type X1 = A; type X2 = B; type Y = Z }
  //   ](x1: A, x2: B)(implicit
  //     app: App
  //   ): Z =
  //     app(x1,x2)
  // }



  case class DepFn3ApplySyntax[
    DF <: AnyDepFn3,
    X1 <: DF#In1, X2 <: DF#In2, X3 <: DF#In3,
    O <: DF#Out
  ]
  (val df: DF) extends AnyVal {

    final def apply(x1: X1, x2: X2, x3: X3)(implicit
      app: App3[DF,X1,X2,X3,O]
    )
    : O =
      app(x1,x2,x3)
  }
}
