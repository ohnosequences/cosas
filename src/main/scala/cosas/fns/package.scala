package ohnosequences.cosas

package object fns {


  implicit def depFn1Ops[DF <: AnyDepFn1](df: DF): DepFn1Ops[DF] =
    DepFn1Ops(df)

  implicit def depFn2Ops[DF <: AnyDepFn2](df: DF): DepFn2Ops[DF] =
    DepFn2Ops(df)

  implicit def depFn2ApplyOps[
    DF <: AnyDepFn2,
    X1 <: DF#In1, X2 <: DF#In2,
    O <: DF#Out
  ](df: DF): DepFn2ApplyOps[DF,X1,X2,O] =
    DepFn2ApplyOps(df)

    implicit def depFn1ApplyOps[
      DF0 <: AnyDepFn1,
      X10 <: DF0#In1,
      Y0 <: DF0#Out
    ](df: DF0): DepFn1ApplyOps[DF0,X10,Y0] =
      DepFn1ApplyOps(df)

}
