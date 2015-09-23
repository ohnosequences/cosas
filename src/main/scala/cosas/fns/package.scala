package ohnosequences.cosas

package object fns {


  implicit def depFn1Syntax[DF <: AnyDepFn1](df: DF): DepFn1Syntax[DF] =
    DepFn1Syntax(df)

  implicit def depFn2Syntax[DF <: AnyDepFn2](df: DF): DepFn2Syntax[DF] =
    DepFn2Syntax(df)

  implicit def depFn1ApplySyntax[
    DF0 <: AnyDepFn1,
    X10 <: DF0#In1,
    Y0 <: DF0#Out
  ](df: DF0): DepFn1ApplySyntax[DF0,X10,Y0] =
    DepFn1ApplySyntax(df)

  implicit def depFn2ApplySyntax[
    DF <: AnyDepFn2,
    X1 <: DF#In1, X2 <: DF#In2,
    O <: DF#Out
  ](df: DF): DepFn2ApplySyntax[DF,X1,X2,O] =
    DepFn2ApplySyntax(df)

}
