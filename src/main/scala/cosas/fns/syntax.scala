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

  case class DepFn1ApplySyntax[DF0 <: AnyDepFn1, I0 <: DF0#In1, O0 <: DF0#Out](val df: DF0) extends AnyVal {

    final def apply(x1: I0)(implicit app: App1[DF0,I0,O0]): O0 =
      app(x1)
  }

  case class DepFn2ApplySyntax[DF <: AnyDepFn2, X1 <: DF#In1, X2 <: DF#In2, O <: DF#Out](val df: DF) extends AnyVal {

    final def apply(x1: X1, x2: X2)(implicit app: App2[DF,X1,X2,O]): O =
      app(x1,x2)
  }
}
