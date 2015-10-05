package ohnosequences.cosas

package object fns {

  implicit def depFn1Syntax[DF <: AnyDepFn1](df: DF): syntax.DepFn1Syntax[DF] =
    syntax.DepFn1Syntax(df)

  implicit def depFn2Syntax[DF <: AnyDepFn2](df: DF): syntax.DepFn2Syntax[DF] =
    syntax.DepFn2Syntax(df)

  implicit def depFn3Syntax[DF <: AnyDepFn3](df: DF): syntax.DepFn3Syntax[DF] =
    syntax.DepFn3Syntax(df)

  implicit def depFn1ApplySyntax[
    DF0 <: AnyDepFn1,
    X10 <: DF0#In1,
    Y0 <: DF0#Out
  ](df: DF0): syntax.DepFn1ApplySyntax[DF0,X10,Y0] =
    syntax.DepFn1ApplySyntax(df)

  implicit def depFn2ApplySyntax[
    DF0 <: AnyDepFn2,
    X10 <: DF0#In1, X20 <: DF0#In2,
    O0 <: DF0#Out
  ](df: DF0): syntax.DepFn2ApplySyntax[DF0,X10,X20,O0] =
    syntax.DepFn2ApplySyntax(df)

  implicit def depFn3ApplySyntax[
    DF0 <: AnyDepFn3,
    X10 <: DF0#In1, X20 <: DF0#In2, X30 <: DF0#In3,
    O0 <: DF0#Out
  ](df: DF0): syntax.DepFn3ApplySyntax[DF0,X10,X20,X30,O0] =
    syntax.DepFn3ApplySyntax(df)
}
